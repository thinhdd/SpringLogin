package com.login.controller;

import com.login.autologin.Autologin;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.social.providers.*;
import org.apache.commons.lang3.StringUtils;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    FacebookProvider facebookProvider;

    @Autowired
    GoogleProvider googleProvider;

    @Autowired
    LinkedInProvider linkedInProvider;

    @Autowired
    TwitterProvider twitterProviderProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Autologin autologin;

    @Autowired
    InstagramProvider instagramProvider;

    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public String loginToFacebook(Model model) {
        return facebookProvider.getFacebookUserData(model, new UserBean());
    }

    @RequestMapping(value = "/google", method = RequestMethod.GET)
    public String loginToGoogle(Model model) {
        return googleProvider.getGoogleUserData(model, new UserBean());
    }

    @RequestMapping(value = "/linkedin", method = RequestMethod.GET)
    public String helloFacebook(Model model) {
        return linkedInProvider.getLinkedInUserData(model, new UserBean());
    }

    @RequestMapping(value = "/twitter", method = RequestMethod.GET)
    public String helloTwiter(Model model) {
        return twitterProviderProvider.getTwitterUserData(model, new UserBean());
    }
    @RequestMapping(value = "/instagram", method = RequestMethod.GET)
    public String helloInstagram(@RequestParam String code, Model model) throws Exception {
        return instagramProvider.getInstagramUserData( code ,model, new UserBean());
    }
    @RequestMapping(value = "/loginins",method = RequestMethod.GET)
    public String loginIns(){
        return "redirect:https://www.instagram.com/oauth/authorize/?client_id=49b3abf296714830a2d7ad57796030ca&redirect_uri=http://localhost:3000/instagram&response_type=code";
    }

    @RequestMapping(value = {"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistration(UserBean userBean) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(HttpServletResponse httpServletResponse, Model model, @Valid UserBean userBean, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        userBean.setProvider("REGISTRATION");
        // Save the details in DB
        if (StringUtils.isNotEmpty(userBean.getPassword())) {
            userBean.setPassword(bCryptPasswordEncoder.encode(userBean.getPassword()));
        }
        userRepository.save(userBean);

        autologin.setSecuritycontext(userBean);

        model.addAttribute("loggedInUser", userBean);
        return "secure/user";
    }

    /**
     * If we can't find a user/email combination
     */
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

}

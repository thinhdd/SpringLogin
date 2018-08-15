package com.login.controller;

import com.login.autologin.Autologin;
import com.login.config.LineConfig;
import com.login.model.LineEntityProfile;
import com.login.model.LineEntityRepos;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.social.providers.*;
import com.login.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    LineProvider lineProvider;

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
    @Autowired
    LineConfig lineConfig;

    @ResponseBody
    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public UserBean loginToFacebook() {
        return facebookProvider.getFacebookUserData(new UserBean());
    }

    @ResponseBody
    @RequestMapping(value = "/google", method = RequestMethod.GET)
    public UserBean loginToGoogle() {
        return googleProvider.getGoogleUserData(new UserBean());
    }

    @RequestMapping(value = "/linkedin", method = RequestMethod.GET)
    public String helloFacebook(Model model) {
        return linkedInProvider.getLinkedInUserData(model, new UserBean());
    }

    @RequestMapping(value = "/twitter", method = RequestMethod.GET)
    public String helloTwiter(Model model) {
        return twitterProviderProvider.getTwitterUserData(model, new UserBean());
    }

    /*Login using instagram*/
    @ResponseBody
    @RequestMapping(value = "/instagram", method = RequestMethod.GET)
    public UserBean helloInstagram(@RequestParam String code) throws Exception {
        return instagramProvider.getInstagramUserData(code, new UserBean());
    }

    @RequestMapping(value = "/loginins", method = RequestMethod.GET)
    public String loginIns() {
        return "redirect:https://www.instagram.com/oauth/authorize/?client_id=49b3abf296714830a2d7ad57796030ca&redirect_uri=http://localhost:3000/instagram&response_type=code";
    }
    /*Login using instagram*/

    /*Login using line*/
    @RequestMapping(value = "/loginline", method = RequestMethod.GET)
    public String loginLine() {
        return "redirect:"+Constant.LineConst.urlAuthorize +"?"+Constant.LineConst.responseType+"="+Constant.LineConst.code+
                "&"+Constant.LineConst.clientId+"="+lineConfig.getClientId()+
                "&"+Constant.LineConst.redirectUri+"="+lineConfig.getCallBackUrl()+"&"+Constant.LineConst.paramAuthorize;
    }

    @ResponseBody
    @RequestMapping(value = "/line", method = RequestMethod.GET)
    public UserBean line(@RequestParam String code) {
        return lineProvider.loginLine(code, new UserBean());
    }
    /*Login using line*/

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

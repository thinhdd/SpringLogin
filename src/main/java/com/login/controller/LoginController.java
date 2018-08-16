package com.login.controller;

import com.login.autologin.Autologin;
import com.login.config.InstagramConfig;
import com.login.config.LineConfig;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.social.providers.*;
import com.login.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    LineProvider lineProvider;

    @Autowired
    FacebookProvider facebookProvider;

    @Autowired
    GoogleProvider googleProvider;

    @Autowired
    LinkedInProvider linkedInProvider;

    @Autowired
    TwitterProvider twitterProvider;

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

    @Autowired
    InstagramConfig instagramConfig;

    @ResponseBody
    @RequestMapping(value = "/facebook", method = RequestMethod.GET)
    public UserBean loginToFacebook() {
        return facebookProvider.getFacebookUserData(new UserBean());
    }

    @ResponseBody
    @RequestMapping(value = "/facebookToken", method = RequestMethod.GET)
    public ResponseEntity<UserBean> loginFbbyToken(@RequestParam String token){
        return new ResponseEntity<UserBean>(facebookProvider.populateUserDetailsFromFacebook(token, new UserBean()),HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/google", method = RequestMethod.GET)
    public UserBean loginToGoogle() {
        return googleProvider.getGoogleUserData(new UserBean());
    }

    @ResponseBody
    @RequestMapping(value = "/googleToken", method = RequestMethod.GET)
    public ResponseEntity<UserBean> loginGgbyToken(@RequestParam String token){
        return new ResponseEntity<UserBean>(googleProvider.populateUserDetailsFromGoogle(token, new UserBean()),HttpStatus.OK);
    }

    @RequestMapping(value = "/linkedin", method = RequestMethod.GET)
    public String helloFacebook(Model model) {
        return linkedInProvider.getLinkedInUserData(model, new UserBean());
    }

    @RequestMapping(value = "/twitter", method = RequestMethod.GET)
    public String helloTwiter(Model model) {
        return twitterProvider.getTwitterUserData(model, new UserBean());
    }
    @ResponseBody
    @RequestMapping(value = "/twitterToken", method = RequestMethod.GET)
    public ResponseEntity<UserBean> loginTwitterbyToken(@RequestParam String token){
        return new ResponseEntity<UserBean>(twitterProvider.populateUserDetailsFromTwitter(token, new UserBean()),HttpStatus.OK);
    }

    /*Login using instagram*/
    @ResponseBody
    @RequestMapping(value = "/instagram", method = RequestMethod.GET)
    public UserBean helloInstagram(@RequestParam String code) throws Exception {
        return instagramProvider.getInstagramUserData(code, new UserBean());
    }

    @RequestMapping(value = "/loginins", method = RequestMethod.GET)
    public String loginIns() {
        return "redirect:"+Constant.InstagramConst.urlAuthorize+"?"+Constant.InstagramConst.clientId+"="+instagramConfig.clientId+"&"+
                Constant.InstagramConst.redirectUri+"="+instagramConfig.callBackUrl+"&"+Constant.InstagramConst.paramAuthorize;
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

    @ResponseBody
    @RequestMapping(value = "/lineToken", method = RequestMethod.GET)
    public UserBean lineToken(@RequestParam String token) {
        return lineProvider.loginLineByToken(token, new UserBean());
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
        if (StringUtils.isNotEmpty(userBean.getPassword())) {
            userBean.setPassword(bCryptPasswordEncoder.encode(userBean.getPassword()));
        }
        userRepository.save(userBean);
        autologin.setSecuritycontext(userBean);
        model.addAttribute("loggedInUser", userBean);
        return "secure/user";
    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

}

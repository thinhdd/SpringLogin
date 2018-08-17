package com.login.controller;

import com.login.autologin.Autologin;
import com.login.config.InstagramConfig;
import com.login.config.LineConfig;
import com.login.model.RequestLogin;
import com.login.model.ResponseEntityBase;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.social.providers.*;
import com.login.util.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @RequestMapping(value = "/google", method = RequestMethod.GET)
    public UserBean loginToGoogle() {
        return googleProvider.getGoogleUserData(new UserBean());
    }

    @RequestMapping(value = "/twitter", method = RequestMethod.GET)
    public String helloTwiter(Model model) {
        return twitterProvider.getTwitterUserData(model, new UserBean());
    }

    /*Login using instagram*/
    @ResponseBody
    @RequestMapping(value = "/instagram", method = RequestMethod.POST)
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

    @ResponseBody
    @RequestMapping(value = "/loginSocialByToken", method = RequestMethod.POST)
    public ResponseEntity<ResponseEntityBase> loginSocialByToken(@RequestBody RequestLogin requestLogin) throws Exception{
        ResponseEntityBase<UserBean> responseEntityBase;
        switch (requestLogin.getType()){
            case "FACEBOOK": responseEntityBase = new ResponseEntityBase<>("",HttpStatus.OK.value(),facebookProvider.populateUserDetailsFromFacebook(requestLogin.getToken(), new UserBean())); break;
            case "GOOGLE": responseEntityBase = new ResponseEntityBase<>("",HttpStatus.OK.value(),googleProvider.populateUserDetailsFromGoogle(requestLogin.getToken(), new UserBean())); break;
            case "TWITTER": responseEntityBase = new ResponseEntityBase<>("",HttpStatus.OK.value(),twitterProvider.populateUserDetailsFromTwitter(requestLogin.getToken(), new UserBean())); break;
            case "INSTAGRAM": responseEntityBase = new ResponseEntityBase<>("",HttpStatus.OK.value(),instagramProvider.getInstagramUserData(requestLogin.getToken(), new UserBean())); break;
            case "LINE": responseEntityBase = new ResponseEntityBase<>("",HttpStatus.OK.value(),lineProvider.loginLineByToken(requestLogin.getToken(), new UserBean())); break;
            default: responseEntityBase = new ResponseEntityBase<>("",HttpStatus.BAD_REQUEST.value(),null); break;
        }
        return new ResponseEntity<ResponseEntityBase>(responseEntityBase,HttpStatus.OK);
    }

}

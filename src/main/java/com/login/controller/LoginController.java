package com.login.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.login.model.LineEntity;
import com.login.model.LineEntityRepos;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.login.autologin.Autologin;
import com.login.model.UserBean;
import com.login.repository.UserRepository;
import com.login.social.providers.FacebookProvider;
import com.login.social.providers.GoogleProvider;
import com.login.social.providers.InstagramProvider;
import com.login.social.providers.LinkedInProvider;
import com.login.social.providers.TwitterProvider;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    RestTemplate restTemplate;

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
    
    @RequestMapping(value = "/loginline",method = RequestMethod.GET)
    public String loginLine(){
        return "redirect:https://access.line.me/oauth2/v2.1/authorize?response_type=code&client_id=1600641663&redirect_uri=http://localhost:3000/line&state=12345abcde&scope=openid%20profile&nonce=09876xyz";
    }
    @ResponseBody
    @RequestMapping(value = "/line", method = RequestMethod.GET)
    public String line(@RequestParam String code, @RequestParam String state) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        Map<String,String > map = new HashMap();
        map.put("grant_type","authorization_code");
        map.put("code","code");
        map.put("redorect_uri","http://localhost:3000/line");
        map.put("client_id","1600641663");
        map.put("client_secret","2b3811d9a55d17952cdd31b8908cfcb5");
       // LineEntity lineEntity = new LineEntity("authorization_code",code,"http://localhost:3000/line","1600641663","2b3811d9a55d17952cdd31b8908cfcb5");
        HttpEntity httpEntity = new HttpEntity<>(map,headers);
        LineEntityRepos lineEntityRepos = restTemplate.exchange("https://api.line.me/oauth2/v2.1/token",HttpMethod.POST,httpEntity,LineEntityRepos.class).getBody();
        return "av";
    }

    @RequestMapping(value = "/linetoken", method = RequestMethod.POST)
    public String linetoken(@RequestParam String code, @RequestParam String state) {
    	return "a";
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

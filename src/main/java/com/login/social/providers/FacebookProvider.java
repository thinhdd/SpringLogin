package com.login.social.providers;

import com.login.repository.UserRepository;
import com.login.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.login.model.UserBean;


@Service
public class FacebookProvider {

    private static final String FACEBOOK = "facebook";
    @Autowired
    BaseProvider baseProvider;
    @Autowired
    private JwtService jwtService;
    @Autowired
    UserRepository userRepository;

    public UserBean getFacebookUserData(UserBean userForm) {
        ConnectionRepository connectionRepository = baseProvider.getConnectionRepository();
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return null;
        }
        String accessToken = connectionRepository.getPrimaryConnection(Facebook.class).createData().getAccessToken();
        userForm.setAccesstoken(accessToken);
        populateUserDetailsFromFacebook(userForm);
        baseProvider.saveUserDetails(userForm);
        baseProvider.autoLoginUser(userForm);
        return userForm;
    }

    protected void populateUserDetailsFromFacebook(UserBean userForm) {
        Facebook facebook = baseProvider.getFacebook();
        String[] fields = {"id", "cover", "birthday", "email", "gender", "name"};
        User user = facebook.fetchObject("me", User.class, fields);
        userForm.setEmail(user.getEmail());
        userForm.setUserId(user.getId());
        userForm.setFullName(user.getName());
        userForm.setAvatar(user.getCover() == null ? "" : user.getCover().getSource());
        userForm.setGender(user.getGender());
        userForm.setProvider(FACEBOOK);
    }
    public UserBean populateUserDetailsFromFacebook(String token) throws Exception {
        Facebook facebook = new FacebookTemplate(token);
        String[] fields = {"id", "cover", "birthday", "email", "gender", "name"};
        User user = facebook.fetchObject("me", User.class, fields);
        if(user == null || user.getId()==null) {
            throw new Exception("Token is invalid");
        }
        UserBean userBean = userRepository.findByUserId(user.getId());
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setEmail(user.getEmail());
        userBean.setUserId(user.getId());
        userBean.setFullName(user.getName());
        userBean.setAvatar(user.getCover() == null ? "" : user.getCover().getSource());
        userBean.setGender(user.getGender());
        userBean.setProvider(FACEBOOK);
        userBean.setPassword(FACEBOOK);
        try {
            if (baseProvider.checkLoginSocial(userBean)) {
                String result = jwtService.generateTokenLogin(userBean.getEmail());
                userBean.setAccesstoken(result);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        baseProvider.saveUserDetails(userBean);
        baseProvider.autoLoginUser(userBean);
        return userBean;
    }
}

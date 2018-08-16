package com.login.social.providers;

import org.springframework.beans.factory.annotation.Autowired;
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
        String[] fields = {"id", "cover", "birthday", "email", "gender", "first_name", "last_name"};
        User user = facebook.fetchObject("me", User.class, fields);
        userForm.setEmail(user.getEmail());
        userForm.setFirstName(user.getFirstName());
        userForm.setLastName(user.getLastName());
        userForm.setImage(user.getCover().getSource());
        userForm.setProvider(FACEBOOK);
    }
    public UserBean populateUserDetailsFromFacebook(String token, UserBean userForm) {
        Facebook facebook = new FacebookTemplate(token);
        String[] fields = {"id", "cover", "birthday", "email", "gender", "first_name", "last_name"};
        User user = facebook.fetchObject("me", User.class, fields);
        userForm.setEmail(user.getEmail());
        userForm.setFirstName(user.getFirstName());
        userForm.setLastName(user.getLastName());
        userForm.setImage(user.getCover().getSource());
        userForm.setProvider(FACEBOOK);
        baseProvider.saveUserDetails(userForm);
        baseProvider.autoLoginUser(userForm);
        return userForm;
    }

}

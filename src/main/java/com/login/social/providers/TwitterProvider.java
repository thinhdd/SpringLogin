package com.login.social.providers;

import com.login.model.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.TwitterProfile;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class TwitterProvider {
    private static final String TWITTER = "twitter";
    private static final String REDIRECT_LOGIN_TWITTER = "redirect:/login";

    @Autowired
    BaseProvider baseProvider ;


    public String getTwitterUserData(Model model, UserBean userForm) {

        ConnectionRepository connectionRepository = baseProvider.getConnectionRepository();
        if (connectionRepository.findPrimaryConnection(TwitterProvider.class) == null) {
            return REDIRECT_LOGIN_TWITTER;
        }
        //Populate the Bean
        populateUserDetailsFromTwitter(userForm);
        //Save the details in DB
        baseProvider.saveUserDetails(userForm);
        //Login the User
        baseProvider.autoLoginUser(userForm);
        model.addAttribute("loggedInUser",userForm);
        return "secure/user";
    }

    protected void populateUserDetailsFromTwitter(UserBean userForm) {
        org.springframework.social.twitter.api.Twitter twitter = baseProvider.getTwitter();
        TwitterProfile twitterProfile = twitter.userOperations().getUserProfile();
        userForm.setEmail(twitterProfile.getProfileUrl());
        userForm.setFirstName(twitterProfile.getName());
        userForm.setLastName(twitterProfile.getName());
        userForm.setImage(twitterProfile.getProfileImageUrl());
        userForm.setProvider(TWITTER);
    }
}

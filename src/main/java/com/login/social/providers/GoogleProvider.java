package com.login.social.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.login.model.UserBean;

@Service
public class GoogleProvider   {

	private static final String GOOGLE = "google";

	@Autowired
    BaseProvider baseProvider ;

	public UserBean getGoogleUserData(UserBean userForm) {
		ConnectionRepository connectionRepository = baseProvider.getConnectionRepository();
		if (connectionRepository.findPrimaryConnection(Google.class) == null) {
			return null;
		}
		String accessToken = connectionRepository.getPrimaryConnection(Google.class).createData().getAccessToken();
		userForm.setAccesstoken(accessToken);
		populateUserDetailsFromGoogle(userForm);
		//Save the details in DB
		baseProvider.saveUserDetails(userForm);
		//Login the User
		baseProvider.autoLoginUser(userForm);
		return userForm;
	}


	protected void populateUserDetailsFromGoogle(UserBean userform) {
		Google google = baseProvider.getGoogle();
		Person googleUser = google.plusOperations().getGoogleProfile();
		userform.setEmail(googleUser.getAccountEmail());
		userform.setFirstName(googleUser.getGivenName());
		userform.setLastName(googleUser.getFamilyName());
		userform.setImage("");
		userform.setProvider(GOOGLE);
	}

}

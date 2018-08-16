package com.login.config;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstagramBuildService {
    InstagramService service;

    @Autowired
    InstagramConfig instagramConfig;

    public InstagramService build() {
        service = new InstagramAuthService().apiKey(instagramConfig.clientId).apiSecret(instagramConfig.clientSecret).callback(instagramConfig.callBackUrl).build();
        return service;
    }

    public Instagram getInstagram(String code){
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(verifier);
        Instagram instagram = new Instagram(accessToken);
        return  instagram;
    }
}

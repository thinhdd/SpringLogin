package com.login.config;

import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;

public class InstagramBuildService {
    String clientId = "49b3abf296714830a2d7ad57796030ca";
    String clientSecret = "599d21bf74a045e6946bbb0b7134fc31";
    String callBack = "http://localhost:3000/instagram";
    InstagramService service;

    public InstagramService build() {
        service = new InstagramAuthService().apiKey(clientId).apiSecret(clientSecret).callback(callBack).build();
        return service;
    }

    public Instagram getInstagram(String code){
        Verifier verifier = new Verifier(code);
        Token accessToken = service.getAccessToken(verifier);
        Instagram instagram = new Instagram(accessToken);
        return  instagram;
    }
}

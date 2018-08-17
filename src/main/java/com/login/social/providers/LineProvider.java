package com.login.social.providers;

import com.login.config.LineConfig;
import com.login.model.LineEntityProfile;
import com.login.model.LineEntityRepos;
import com.login.model.UserBean;
import com.login.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class LineProvider {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LineConfig lineConfig;

    private static final String LINE = "line";

    public UserBean loginLine(String code, UserBean userBean){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(Constant.LineConst.grantType, lineConfig.getGrantType());
        map.add(Constant.LineConst.code, code);
        map.add(Constant.LineConst.redirectUri, lineConfig.getCallBackUrl());
        map.add(Constant.LineConst.clientId, lineConfig.getClientId());
        map.add(Constant.LineConst.clientSecret, lineConfig.getClientSecret());
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        LineEntityRepos lineEntityRepos = restTemplate.exchange(Constant.LineConst.urlToken, HttpMethod.POST, httpEntity, LineEntityRepos.class).getBody();
        headers.set(Constant.LineConst.authorization, Constant.LineConst.bearer + lineEntityRepos.getAccess_token());
        HttpEntity<HttpHeaders> httpEntity1 = new HttpEntity<>(headers);
        LineEntityProfile lineEntityProfile = restTemplate.exchange(Constant.LineConst.urlProfile, HttpMethod.GET, httpEntity1, LineEntityProfile.class).getBody();
        userBean.setFullName(lineEntityProfile.getDisplayName());
        userBean.setAvatar(lineEntityProfile.getPictureUrl());
        userBean.setUserId(lineEntityProfile.getUserId());
        userBean.setProvider(LINE);
        return userBean;
    }

    public UserBean loginLineByToken(String token, UserBean userBean){
        HttpHeaders headers = new HttpHeaders();
        headers.set(Constant.LineConst.authorization, Constant.LineConst.bearer + token);
        HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);
        LineEntityProfile lineEntityProfile = restTemplate.exchange(Constant.LineConst.urlProfile, HttpMethod.GET, httpEntity, LineEntityProfile.class).getBody();
        userBean.setFullName(lineEntityProfile.getDisplayName());
        userBean.setAvatar(lineEntityProfile.getPictureUrl());
        userBean.setUserId(lineEntityProfile.getUserId());
        userBean.setAccesstoken(token);
        userBean.setProvider(LINE);
        return userBean;
    }
}

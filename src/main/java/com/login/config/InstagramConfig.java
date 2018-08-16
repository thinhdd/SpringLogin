package com.login.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class InstagramConfig {
    @Value("${social.instagram.clientId}")
    public String clientId;
    @Value("${socical.instagram.clientSecret}")
    public String clientSecret;
    @Value("${socical.instagram.callBackUrl}")
    public String callBackUrl;
}

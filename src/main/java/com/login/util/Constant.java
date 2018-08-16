package com.login.util;

public class Constant {
    public class LineConst{
        public static final String urlToken = "https://api.line.me/oauth2/v2.1/token";
        public static final String urlProfile = "https://api.line.me/v2/profile";
        public static final String grantType = "grant_type";
        public static final String code = "code";
        public static final String redirectUri = "redirect_uri";
        public static final String clientId = "client_id";
        public static final String clientSecret = "client_secret";
        public static final String authorization = "Authorization";
        public static final String bearer = "Bearer ";
        public static final String urlAuthorize = "https://access.line.me/oauth2/v2.1/authorize";
        public static final String responseType = "response_type";
        public static final String paramAuthorize = "state=12345abcde&scope=openid%20profile&nonce=09876xyz";
    }
    public class InstagramConst{
        public static final String urlAuthorize = "https://www.instagram.com/oauth/authorize/";
        public static final String redirectUri = "redirect_uri";
        public static final String clientId = "client_id";
        public static final String paramAuthorize = "response_type=code";
    }
}

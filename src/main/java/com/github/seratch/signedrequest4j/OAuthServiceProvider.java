package com.github.seratch.signedrequest4j;

public class OAuthServiceProvider {

    private final String requestTokenURL;
    private final String userAuthorizationURL;
    private final String accessTokenURL;

    public OAuthServiceProvider(String requestTokenURL, String userAuthorizationURL,
                         String accessTokenURL) {
        this.requestTokenURL = requestTokenURL;
        this.userAuthorizationURL = userAuthorizationURL;
        this.accessTokenURL = accessTokenURL;
    }

    public String getAccessTokenURL() {
        return this.accessTokenURL;
    }

    public String getRequestTokenURL() {
        return this.requestTokenURL;
    }

    public String getUserAuthorizationURL() {
        return this.userAuthorizationURL;
    }

}

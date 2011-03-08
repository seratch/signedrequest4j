package com.github.seratch.signedrequest4j;

/**
 * OAuth Token
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://oauth.net/core/1.0/">OAuth Core 1.0</a>
 */
public class OAuthToken implements NotString {

    private final String token;
    private final String tokenSecret;

    public OAuthToken(String token, String tokenSecret) {
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    public String getToken() {
        return this.token;
    }

    public String getTokenSecret() {
        return this.tokenSecret;
    }

    @Override
     public String toString() {
         throw new IllegalAccessError();
     }

}

package com.github.seratch.signedrequest4j;

/**
 * OAuth Token
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://oauth.net/core/1.0/">OAuth Core 1.0</a>
 */
public class OAuthToken {

    private final String token;

    public OAuthToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return this.token;
    }

}

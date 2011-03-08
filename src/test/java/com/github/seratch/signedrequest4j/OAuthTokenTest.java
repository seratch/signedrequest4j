package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class OAuthTokenTest {

    @Test
    public void type() throws Exception {
        assertNotNull(OAuthToken.class);
    }

    @Test
    public void instantiation() throws Exception {
        String token = null;
        String tokenSecret = null;
        OAuthToken target = new OAuthToken(token, tokenSecret);
        assertNotNull(target);
    }

    @Test
    public void toString_A$() throws Exception {
        String token = "tokenvalue";
        String tokenSecret = "secret";
        OAuthToken target = new OAuthToken(token, tokenSecret);
        // given
        // when
        try {
            target.toString();
            fail();
        } catch (IllegalAccessError e) {

        }
    }

}

package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SingedRequestBaseImplTest {

    /**
     * https://github.com/seratch/signedrequest4j/issues/1
     */
    @Test
    public void readQueryStringAndAddToSignatureBaseString_Sharp() throws Exception {
        OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
        SignedRequestBaseImpl request = (SignedRequestBaseImpl) SignedRequestFactory.create(consumer);
        request.readQueryStringAndAddToSignatureBaseString("?keyword=%");
        assertThat(request.getParameters.get("keyword").toString(), is(equalTo("%")));
    }

    @Test
    public void readQueryStringAndAddToSignatureBaseString_ASCII() throws Exception {
        OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
        SignedRequestBaseImpl request = (SignedRequestBaseImpl) SignedRequestFactory.create(consumer);
        request.readQueryStringAndAddToSignatureBaseString("?keyword=1234abcde");
        assertThat(request.getParameters.get("keyword").toString(), is(equalTo("1234abcde")));
    }


    @Test
    public void readQueryStringAndAddToSignatureBaseString_日本語() throws Exception {
        OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
        SignedRequestBaseImpl request = (SignedRequestBaseImpl) SignedRequestFactory.create(consumer);
        request.readQueryStringAndAddToSignatureBaseString("?keyword=日本語");
        assertThat(request.getParameters.get("keyword").toString(), is(equalTo("日本語")));
    }

    @Test
    public void readQueryStringAndAddToSignatureBaseString_URLEncoded日本語() throws Exception {
        OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
        SignedRequestBaseImpl request = (SignedRequestBaseImpl) SignedRequestFactory.create(consumer);
        request.readQueryStringAndAddToSignatureBaseString("?keyword=%E6%97%A5%E6%9C%AC%E8%AA%9E");
        assertThat(request.getParameters.get("keyword").toString(), is(equalTo("日本語")));
    }

}

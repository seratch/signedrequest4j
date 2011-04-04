package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.mock;


public class SignedRequestVerifierTest {

	@Test
	public void type() throws Exception {
		assertNotNull(SignedRequestVerifier.class);
	}

	@Test
	public void instantiation() throws Exception {
		SignedRequestVerifier target = new SignedRequestVerifier();
		assertNotNull(target);
	}

	@Test
	public void verify_A$String$OAuthConsumer$HttpMethod$SignatureMethod() throws Exception {
		// given
		String authorizationHeader = "OAuth oauth_consumer_key=\"admin\"" +
				",oauth_signature_method=\"HMAC-SHA1\"" +
				",oauth_signature=\"iok6KjvmOQStq1y4SyovV1%2FghNI%3D\"" +
				",oauth_timestamp=\"1301921304\"" +
				",oauth_nonce=\"-1425143696163906497\"" +
				",oauth_version=\"1.0\"";
		OAuthConsumer consumer = new OAuthConsumer("admin", "test");
		boolean actual = SignedRequestVerifier.verify("http://localhost:8080/topics/?keywords=test",
				authorizationHeader, consumer, HttpMethod.GET, SignatureMethod.HMAC_SHA1);
		boolean expected = true;
		assertEquals(expected, actual);
	}

	@Test
	public void verify_A$String$OAuthConsumer$OAuthToken$HttpMethod$SignatureMethod() throws Exception {
		// given
		String authorizationHeader = "OAuth oauth_token=\"t\"" +
				",oauth_consumer_key=\"admin\"" +
				",oauth_signature_method=\"HMAC-SHA1\"" +
				",oauth_signature=\"%2FiyrxzxDTh2FgMlJi0D9VONg4rM%3D\"" +
				",oauth_timestamp=\"1301923063\"" +
				",oauth_nonce=\"-5989898319858371717\"" +
				",oauth_version=\"1.0\"";
		OAuthConsumer consumer = new OAuthConsumer("admin", "test");
		OAuthToken token = new OAuthToken("t", "ts");
		boolean actual = SignedRequestVerifier.verify("http://localhost:8080/topics/?keywords=test",
				authorizationHeader, consumer, token, HttpMethod.GET, SignatureMethod.HMAC_SHA1);
		boolean expected = true;
		assertEquals(expected, actual);
	}

}

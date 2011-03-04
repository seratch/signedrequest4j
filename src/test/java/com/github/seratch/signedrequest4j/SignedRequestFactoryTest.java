package com.github.seratch.signedrequest4j;

import static org.junit.Assert.*;

import com.github.seratch.signedrequest4j.OAuthConsumer;
import com.github.seratch.signedrequest4j.SignatureMethod;
import com.github.seratch.signedrequest4j.SignedRequest;
import com.github.seratch.signedrequest4j.SignedRequestFactory;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SignedRequestFactoryTest {

	@Test
	public void type() throws Exception {
		assertNotNull(SignedRequestFactory.class);
	}

	@Test
	public void instantiation() throws Exception {
		SignedRequestFactory target = new SignedRequestFactory();
		assertNotNull(target);
	}

	@Test
	public void get2LeggedOAuthRequest_A$String$OAuthConsumer$SignatureMethod()
			throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		// when
		SignedRequest actual = SignedRequestFactory.get2LeggedOAuthRequest(
				realm, consumer, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void get2LeggedOAuthRequest_A$String$OAuthConsumer$SignatureMethod$Map()
			throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.get2LeggedOAuthRequest(
				realm, consumer, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void get2LeggedOAuthRequest_A$String$OAuthConsumer()
			throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("consumer_key",
				"consumer_secret");
		// when
		SignedRequest actual = SignedRequestFactory.get2LeggedOAuthRequest(
				realm, consumer);
		// then
		// e.g. : verify(mocked).called();
		assertNotNull(actual);
	}

	@Test
	public void get2LeggedOAuthRequest_A$String$OAuthConsumer$Map()
			throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("consumer_key",
				"consumer_secret");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.get2LeggedOAuthRequest(
				realm, consumer, additionalParameters);
		// then
		assertNotNull(actual);
	}

}

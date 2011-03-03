package com.github.signedrequest;

import static org.junit.Assert.*;

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
	public void getInstance_A$String$OAuthConsumer$SignatureMethod()
			throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		consumer.setConsumerKey("sdfsa");
		consumer.setConsumerSecret("sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		// when
		SignedRequest actual = SignedRequestFactory.getInstance(realm,
				consumer, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void getInstance_A$String$OAuthConsumer$SignatureMethod$Map()
			throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		consumer.setConsumerKey("sdfsa");
		consumer.setConsumerSecret("sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		Map<String, String> additionalParameters = new HashMap<String, String>();
		// when
		SignedRequest actual = SignedRequestFactory.getInstance(realm,
				consumer, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void getInstance_A$String$OAuthConsumer() throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("consumer_key",
				"consumer_secret");
		// when
		SignedRequest actual = SignedRequestFactory
				.getInstance(realm, consumer);
		// then
		// e.g. : verify(mocked).called();
		assertNotNull(actual);
	}

	@Test
	public void getInstance_A$String$OAuthConsumer$Map() throws Exception {
		// given
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("consumer_key",
				"consumer_secret");
		Map<String, String> additionalParameters = new HashMap<String, String>();
		// when
		SignedRequest actual = SignedRequestFactory.getInstance(realm,
				consumer, additionalParameters);
		// then
		assertNotNull(actual);
	}

}

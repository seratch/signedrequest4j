package com.github.seratch.signedrequest4j;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.mock;

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
	public void create_A$String$OAuthConsumer$SignatureMethod()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$String$OAuthConsumer$SignatureMethod$Map()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$String$OAuthConsumer()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("consumer_key", "consumer_secret");
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer);
		// then
		// e.g. : verify(mocked).called();
		assertNotNull(actual);
	}

	@Test
	public void create_A$String$OAuthConsumer$Map()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("consumer_key", "consumer_secret");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$String$OAuthConsumer$OAuthAccessToken()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("accessToken", "token_secret");
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$String$OAuthConsumer$OAuthAccessToken$Map()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("accessToken", "token_secret");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$String$OAuthConsumer$OAuthAccessToken$SignatureMethod()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("accessToken", "token_secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$String$OAuthConsumer$OAuthAccessToken$SignatureMethod$Map()
			throws Exception {
		// given
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("accessToken", "token_secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer() throws Exception {
		OAuthConsumer consumer = new OAuthConsumer(null, null);
		SignedRequest actual = SignedRequestFactory.create(consumer);
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer() throws Exception {
		OAuthRealm realm = mock(OAuthRealm.class);
		OAuthConsumer consumer = mock(OAuthConsumer.class);
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer$Map() throws Exception {
		// given
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		SignedRequest actual = SignedRequestFactory.create(consumer, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer$Map() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer$SignatureMethod() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		// when
		SignedRequest actual = SignedRequestFactory.create(consumer, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer$SignatureMethod() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer$SignatureMethod$Map() throws Exception {
		// given
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		SignedRequest actual = SignedRequestFactory.create(consumer, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer$SignatureMethod$Map() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer$OAuthAccessToken() throws Exception {
		// given
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		// when
		SignedRequest actual = SignedRequestFactory.create(consumer, accessToken);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer$OAuthAccessToken() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer$OAuthAccessToken$Map() throws Exception {
		// given
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(consumer, accessToken, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer$OAuthAccessToken$Map() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer$OAuthAccessToken$SignatureMethod() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		// when
		SignedRequest actual = SignedRequestFactory.create(consumer, accessToken, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer$OAuthAccessToken$SignatureMethod() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken, signatureMethod);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthConsumer$OAuthAccessToken$SignatureMethod$Map() throws Exception {
		// given
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// when
		SignedRequest actual = SignedRequestFactory.create(consumer, accessToken, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

	@Test
	public void create_A$OAuthRealm$OAuthConsumer$OAuthAccessToken$SignatureMethod$Map() throws Exception {
		// given
		OAuthRealm realm = new OAuthRealm("aaa");
		OAuthConsumer consumer = new OAuthConsumer("key", "secret");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		OAuthAccessToken accessToken = new OAuthAccessToken("asda", "sdfsf");
		Map<String, Object> additionalParameters = new HashMap<String, Object>();
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		SignedRequest actual = SignedRequestFactory.create(realm, consumer, accessToken, signatureMethod, additionalParameters);
		// then
		assertNotNull(actual);
	}

}

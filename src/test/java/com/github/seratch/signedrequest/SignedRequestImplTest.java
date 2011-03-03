package com.github.seratch.signedrequest;

import static org.junit.Assert.*;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SignedRequestImplTest {

	@Test
	public void type() throws Exception {
		assertNotNull(SignedRequestImpl.class);
	}

	@Test
	public void instantiation() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		consumer.setConsumerKey("sdfsa");
		consumer.setConsumerSecret("sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		assertNotNull(target);
	}

	@Test
	public void getHttpURLConnection_A$String$HttpMethod() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		consumer.setConsumerKey("sdfsa");
		consumer.setConsumerSecret("sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://localhost:8080/";
		HttpMethod method = HttpMethod.GET;
		// when
		HttpURLConnection actual = target.getHttpURLConnection(url, method);
		// then
		assertNotNull(actual);
	}

	@Test
	public void getGeneratedSignature_A$String$HttpMethod$String$Long_HMAC()
			throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		consumer.setConsumerKey("sdfsa");
		consumer.setConsumerSecret("sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://localhost:8080/";
		HttpMethod method = HttpMethod.GET;
		String oAuthNonce = "sdfaaaa";
		Long oAuthTimestamp = 12345L;
		// when
		String actual = target.getSignature(url, method, oAuthNonce,
				oAuthTimestamp);
		// then
		// e.g. : verify(mocked).called();
		String expected = "I/ci60MwaIR2x2mWvCZPFaWjaEI=";
		assertEquals(expected, actual);
	}

	@Test
	public void getSignature_A$String$HttpMethod$String$Long_PLAINTEXT()
			throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		consumer.setConsumerKey("sdfsa");
		consumer.setConsumerSecret("sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.PLAINTEXT;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://localhost:8080/";
		HttpMethod method = HttpMethod.GET;
		String oAuthNonce = "sdfaaaa";
		Long oAuthTimestamp = 123456L;
		// when
		String actual = target.getSignature(url, method, oAuthNonce,
				oAuthTimestamp);
		// then
		// e.g. : verify(mocked).called();
		String expected = "GET&http%3A%2F%2Flocalhost%3A8080%2F&oauth_consumer_key%3Dsdfsa%26oauth_nonce%3Dsdfaaaa%26oauth_signature_method%3DPLAINTEXT%26oauth_timestamp%3D123456%26oauth_version%3D1.0";
		assertEquals(expected, actual);
	}

	@Test
	public void getAuthorizationHeader_A$String$String$Long() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		consumer.setConsumerKey("sdfada");
		consumer.setConsumerSecret("sdfafdsafsa");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String signature = "signatureXXX";
		String oAuthNonce = "noncenonce";
		Long oAuthTimestamp = 12345L;
		// when
		String actual = target.getAuthorizationHeader(signature, oAuthNonce,
				oAuthTimestamp);
		// then
		String expected = "OAuth oauth_consumer_key=\"sdfada\",oauth_signature_method=\"HMAC-SHA1\",oauth_signature=\"signatureXXX\",oauth_timestamp=\"12345\",oauth_nonce=\"noncenonce\",oauth_version=\"1.0\",";
		assertEquals(expected, actual);
	}

	@Test
	public void setOAuthNonceAndOAuthTimestamp_A$String$Long() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String oAuthNonce = null;
		Long oAuthTimestamp = null;
		// when
		target.setOAuthNonceAndOAuthTimestamp(oAuthNonce, oAuthTimestamp);
		// then
	}

	@Test
	public void doRequest_A$String$HttpMethod$Map$String() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		HttpMethod method = HttpMethod.GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		String charset = "UTF-8";
		// when
		HttpResponse actual = target.doRequest(url, method, requestParameters,
				charset);
		// then
		assertNotNull(actual);
	}

	@Test
	public void getResponseCotent_A$HttpURLConnection$String() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer();
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		HttpMethod method = HttpMethod.GET;
		HttpURLConnection conn = target.getHttpURLConnection(url, method);
		String charset = "UTF-8";
		// when
		String actual = target.getResponseCotent(conn, charset);
		// then
		assertNotNull(actual);
	}

}

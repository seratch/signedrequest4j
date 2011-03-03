package com.github.seratch.signedrequest;

import static org.junit.Assert.*;

import com.github.seratch.signedrequest.HttpMethod;
import com.github.seratch.signedrequest.OAuthConsumer;
import com.github.seratch.signedrequest.SignatureMethod;
import com.github.seratch.signedrequest.SignedRequestImpl;

import java.net.HttpURLConnection;

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
	public void getHttpURLConnection_A$String$HttpMethod$String$Long()
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
		String oAuthNonce = null;
		Long oAuthTimestamp = null;
		// when
		HttpURLConnection actual = target.getHttpURLConnection(url, method,
				oAuthNonce, oAuthTimestamp);
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
	public void getContent_A$String$HttpMethod() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfafa", "sdfa22");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		HttpMethod method = HttpMethod.GET;
		// when
		String actual = target.getContent(url, method);
		// then
		assertNotNull(actual);
	}

	@Test
	public void getContent_A$String$HttpMethod$String$Long() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfafa", "sdfa22");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		HttpMethod method = HttpMethod.GET;
		String oAuthNonce = "sdfaa";
		Long oAuthTimestamp = 12345L;
		// when
		String actual = target.getContent(url, method, oAuthNonce,
				oAuthTimestamp);
		// then
		assertNotNull(actual);
	}

}

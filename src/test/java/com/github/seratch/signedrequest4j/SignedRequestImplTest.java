package com.github.seratch.signedrequest4j;

import static org.junit.Assert.*;

import com.github.seratch.signedrequest4j.HttpMethod;
import com.github.seratch.signedrequest4j.HttpResponse;
import com.github.seratch.signedrequest4j.OAuthConsumer;
import com.github.seratch.signedrequest4j.SignatureMethod;
import com.github.seratch.signedrequest4j.SignedRequestImpl;

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
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		assertNotNull(target);
	}

	@Test
	public void getHttpURLConnection_A$String$HttpMethod() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
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
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
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
	public void getSignature_A$String$HttpMethod$String$Long_HMAC_AdditionalParams()
			throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		Map<String, Object> additionalparams = new HashMap<String, Object>();
		additionalparams.put("partnerId", "p1234");
		additionalparams.put("administrator", "false");
		additionalparams.put("number", "12345");
		additionalparams.put("xoauth_requestor_id", "323sdfa");
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod, additionalparams);
		// given
		String url = "http://localhost:8080/";
		HttpMethod method = HttpMethod.GET;
		String oAuthNonce = "sdfaaaa";
		Long oAuthTimestamp = 12345L;
		String baseString = target.getSignatureBaseString(url, method,
				oAuthNonce, oAuthTimestamp);
		assertEquals(
				"GET&http%3A%2F%2Flocalhost%3A8080%2F&administrator%3Dfalse%26number%3D12345%26oauth_consumer_key%3Dsdfsa%26oauth_nonce%3Dsdfaaaa%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D12345%26oauth_version%3D1.0%26partnerId%3Dp1234%26xoauth_requestor_id%3D323sdfa",
				baseString);
		// when
		String actual = target.getSignature(url, method, oAuthNonce,
				oAuthTimestamp);
		// then
		// e.g. : verify(mocked).called();
		String expected = "UGerPB3op7qtQrhRCO87ssDbruA=";
		assertEquals(expected, actual);
	}

	@Test
	public void getSignature_A$String$HttpMethod$String$Long_PLAINTEXT()
			throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
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
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
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
		String expected = "OAuth oauth_consumer_key=\"sdfsa\",oauth_signature_method=\"HMAC-SHA1\",oauth_signature=\"signatureXXX\",oauth_timestamp=\"12345\",oauth_nonce=\"noncenonce\",oauth_version=\"1.0\",";
		assertEquals(expected, actual);
	}

	@Test
	public void doRequest_A$String$HttpMethod$Map$String() throws Exception {
		String realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		HttpMethod method = HttpMethod.GET;
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("hoge", "foo");
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
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
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

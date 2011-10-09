package com.github.seratch.signedrequest4j;

import com.github.seratch.signedrequest4j.SignedRequestImpl.Parameter;
import org.junit.Test;
import server.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.*;

public class SignedRequestImplTest {

	@Test
	public void type() throws Exception {
		assertNotNull(SignedRequestImpl.class);
	}

	@Test
	public void instantiation() throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		assertNotNull(target);
	}

	@Test
	public void getGeneratedSignature_A$String$HttpMethod$String$Long_HMAC()
			throws Exception {
		OAuthRealm realm = null;
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
	public void getGeneratedSignature_A$String$HttpMethod$String$Long_RSA()
			throws Exception {
		SignedRequestImpl signedRequest = null;
		String url = "https://www.google.com/calendar/feeds/default/allcalendars/full";
		HttpMethod method = HttpMethod.GET;
		String oAuthNonce = String.valueOf(System.currentTimeMillis());
		Long oAuthTimestamp = System.currentTimeMillis();
		try {
			Properties props = new Properties();
			props.load(this.getClass().getClassLoader().getResourceAsStream("TwitterOAuth.properties"));
			String consumerKey = (String) props.get("consumer_key");
			String consumerSecret = (String) props.get("consumer_secret");
			String token = (String) props.get("token");
			String tokenSecret = (String) props.get("token_secret");
			signedRequest = new SignedRequestImpl(null,
					new OAuthConsumer(consumerKey, consumerSecret),
					new OAuthAccessToken(token, tokenSecret),
					SignatureMethod.RSA_SHA1);
			try {
				signedRequest.getSignature(url, method, oAuthNonce,
						oAuthTimestamp);
				fail("SignedRequestClientException must be occurred.");
			} catch (SignedRequestClientException e) {
			}
		} catch (NullPointerException e) {
			System.out.println("TwitterOAuth.properties not found, test skipped.");
		}

		StringBuilder rsaPrivateKey = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					this.getClass().getClassLoader().getResourceAsStream("RSA_PrivateKey.txt")));
			String line = null;
			while ((line = br.readLine()) != null) {
				rsaPrivateKey.append(line);
				rsaPrivateKey.append("\n");
			}
			String actual = signedRequest.setRsaPrivateKeyValue(rsaPrivateKey.toString()).getSignature(url, method, oAuthNonce,
					oAuthTimestamp);
			assertTrue(actual.length() == 172);
			System.out.println(signedRequest.getAuthorizationHeader(actual, "hogehoge", 12345L));
			HttpResponse response = signedRequest.doGet(url, "UTF-8");
			System.out.println(response.getHeaders());
			System.out.println(response.getContent());
		} catch (NullPointerException e) {
			System.out.println("RSA_PrivateKey.txt not found, test skipped.");
		}
	}

	@Test
	public void getSignature_A$String$HttpMethod$String$Long_HMAC_AdditionalParams()
			throws Exception {
		OAuthRealm realm = null;
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
	public void getSignature_A$String$HttpMethod$String$Long_HMAC_TwitterOAuthExample()
			throws Exception {
		Map<String, Object> additionalparams = new HashMap<String, Object>();
		additionalparams.put("file", "vacation.jpg");
		additionalparams.put("size", "original");
		SignedRequestImpl target = new SignedRequestImpl(
				null,
				new OAuthConsumer("dpf43f3p2l4k3l03", "kd94hf93k423kf44"),
				new OAuthAccessToken("nnch734d00sl2jdk", "pfkkdhi9sl3r4s00"),
				SignatureMethod.HMAC_SHA1,
				additionalparams);
		// given
		String url = "http://photos.example.net/photos";
		HttpMethod method = HttpMethod.GET;
		String oAuthNonce = "kllo9940pd9333jh";
		Long oAuthTimestamp = 1191242096L;
		String baseString = target.getSignatureBaseString(url, method,
				oAuthNonce, oAuthTimestamp);
		assertEquals(
				"GET&http%3A%2F%2Fphotos.example.net%2Fphotos&file%3Dvacation.jpg%26oauth_consumer_key%3Ddpf43f3p2l4k3l03%26oauth_nonce%3Dkllo9940pd9333jh%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1191242096%26oauth_token%3Dnnch734d00sl2jdk%26oauth_version%3D1.0%26size%3Doriginal",
				baseString);
		assertEquals(
				"tR3+Ty81lMeYAr/Fid0kMTYa/WM=",
				target.getSignature(url, method, oAuthNonce, oAuthTimestamp));
	}

	@Test
	public void getSignature_A$String$HttpMethod$String$Long_PLAINTEXT()
			throws Exception {
		OAuthRealm realm = null;
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
		OAuthRealm realm = null;
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
		String expected = "OAuth oauth_consumer_key=\"sdfsa\",oauth_signature_method=\"HMAC-SHA1\",oauth_signature=\"signatureXXX\",oauth_timestamp=\"12345\",oauth_nonce=\"noncenonce\",oauth_version=\"1.0\"";
		assertEquals(expected, actual);
	}

	@Test
	public void doRequest_A$String$HttpMethod$Map$String() throws Exception {
		OAuthRealm realm = null;
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
	public void doRequest_A$String$HttpMethod$Map$String_TwitterOAuth() throws Exception {
		try {
			OAuthRealm realm = new OAuthRealm("http://api.twitter.com");
			Properties props = new Properties();
			props.load(this.getClass().getClassLoader().getResourceAsStream("TwitterOAuth.properties"));
			String consumerKey = (String) props.get("consumer_key");
			String consumerSecret = (String) props.get("consumer_secret");
			OAuthConsumer consumer = new OAuthConsumer(consumerKey, consumerSecret);
			String token = (String) props.get("token");
			String tokenSecret = (String) props.get("token_secret");
			OAuthAccessToken accessToken = new OAuthAccessToken(token, tokenSecret);
			SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
			SignedRequestImpl target = new SignedRequestImpl(realm, consumer, accessToken,
					signatureMethod);
			// given
			String url = "http://api.twitter.com/1/statuses/home_timeline.xml";
			HttpMethod method = HttpMethod.GET;
			String charset = "UTF-8";
			// when
			HttpResponse actual = target.doRequest(url, method, new HashMap<String, Object>(), charset);
			// then
			assertNotNull(actual);
			System.out.println(actual.getHeaders());
			System.out.println(actual.getContent());
		} catch (NullPointerException e) {
			System.out.println("TwitterOAuth.properties not found, test skipped.");
		}
	}

	@Test
	public void doGet_A$String$String() throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		String charset = "UTF-8";
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		HttpResponse actual = target.doGet(url, charset);
		// then
		// e.g. : verify(mocked).called();
		assertNotNull(actual);
		assertTrue(200 == actual.getStatusCode());
		assertTrue(actual.getContent().length() > 0);
	}

	@Test
	public void doHead_A$String() throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		HttpResponse actual = target.doHead(url);
		// then
		// e.g. : verify(mocked).called();
		assertNotNull(actual);
		assertTrue(200 == actual.getStatusCode());
	}

	@Test
	public void doOptions_A$String() throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		// when
		HttpResponse actual = target.doOptions(url);
		// then
		assertNotNull(actual);
		assertTrue(200 == actual.getStatusCode());
		assertTrue(actual.getHeaders().get("Allow").size() > 0);
	}

	@Test
	public void doPost_A$String$Map$String() throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("v", "日本語");
		String charset = "UTF-8";
		// when
		HttpResponse actual = target.doPost(url, requestParameters, charset);
		// then
		assertNotNull(actual);
		assertTrue(200 == actual.getStatusCode());
	}

	@Test
	public void getSignatureBaseString_A$String$HttpMethod$String$Long()
			throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		HttpMethod method = HttpMethod.GET;
		String oAuthNonce = "sfdafs";
		Long oAuthTimestamp = 12345L;
		// when
		String actual = target.getSignatureBaseString(url, method, oAuthNonce,
				oAuthTimestamp);
		// then
		assertNotNull(actual);
	}

	@Test
	public void getNormalizedParameters_A$String$Long() throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String oAuthNonce = "nonce";
		Long oAuthTimestamp = 123L;
		// when
		List<Parameter> actual = target.getNormalizedParameters(oAuthNonce,
				oAuthTimestamp);
		// then
		assertNotNull(actual);
	}

	@Test
	public void doDelete_A$String$Map$String() throws Exception {
		final HttpServer server = new HttpServer(new DeleteHandler());
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable).start();
			Thread.sleep(100L);
			OAuthRealm realm = null;
			SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
			SignedRequest request = new SignedRequestImpl(realm, HttpServerSpec.SINGLETON_CONSUMER, signatureMethod);
			request.doDelete("http://localhost:8888/", new HashMap<String, Object>(), "UTF-8");
		} finally {
			server.stop();
			Thread.sleep(100L);
		}
	}

	@Test
	public void doPut_A$String$Map$String() throws Exception {
		final HttpServer server = new HttpServer(new PutHandler());
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable).start();
			Thread.sleep(100L);
			OAuthRealm realm = null;
			SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
			SignedRequest request = new SignedRequestImpl(realm, HttpServerSpec.SINGLETON_CONSUMER, signatureMethod);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("k", "v");
			request.doPut("http://localhost:8888/", params, "UTF-8");
		} finally {
			server.stop();
			Thread.sleep(100L);
		}
	}

	@Test
	public void doTrace_A$String() throws Exception {
		final HttpServer server = new HttpServer(new TraceHandler());
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable).start();
			Thread.sleep(100L);
			OAuthRealm realm = null;
			SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
			SignedRequest request = new SignedRequestImpl(realm, HttpServerSpec.SINGLETON_CONSUMER, signatureMethod);
			request.doTrace("http://localhost:8888/");
		} finally {
			server.stop();
			Thread.sleep(100L);
		}
	}

	@Test
	public void doPost_A$String$RequestBody$String() throws Exception {
		final HttpServer server = new HttpServer(new PostHandler());
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable).start();
			Thread.sleep(100L);
			OAuthRealm realm = null;
			OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
			SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
			SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
					signatureMethod);
			// given
			String url = "http://localhost:8888/";
			RequestBody body = new RequestBody("abc".getBytes(), "text/plain");
			String charset = "UTF-8";
			// when
			HttpResponse actual = target.doPost(url, body, charset);
			// then
			assertNotNull(actual);
			assertTrue(200 == actual.getStatusCode());
		} finally {
			server.stop();
			Thread.sleep(100L);
		}
	}

	@Test
	public void doDelete_A$String$RequestBody$String() throws Exception {
		final HttpServer server = new HttpServer(new DeleteHandler());
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable).start();
			Thread.sleep(100L);
			OAuthRealm realm = null;
			OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
			SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
			SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
					signatureMethod);
			// given
			String url = "http://localhost:8888/";
			RequestBody body = new RequestBody(null, "text/plain");
			String charset = "UTF-8";
			// when
			HttpResponse actual = target.doDelete(url, body, charset);
			// then
			assertNotNull(actual);
			assertTrue(200 == actual.getStatusCode());
		} finally {
			server.stop();
			Thread.sleep(100L);
		}
	}

	@Test
	public void doPut_A$String$RequestBody$String() throws Exception {
		final HttpServer server = new HttpServer(new PutHandler());
		try {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					try {
						server.start();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			new Thread(runnable).start();
			Thread.sleep(100L);
			OAuthRealm realm = null;
			OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
			SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
			SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
					signatureMethod);
			// given
			String url = "http://localhost:8888/";
			RequestBody body = new RequestBody("abc".getBytes(), "text/plain");
			String charset = "UTF-8";
			// when
			HttpResponse actual = target.doPut(url, body, charset);
			// then
			assertNotNull(actual);
			assertTrue(200 == actual.getStatusCode());
		} finally {
			server.stop();
			Thread.sleep(100L);
		}
	}

	@Test
	public void doRequest_A$String$HttpMethod$RequestBody$String() throws Exception {
		OAuthRealm realm = null;
		OAuthConsumer consumer = new OAuthConsumer("sdfsa", "sdfafa33333");
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		SignedRequestImpl target = new SignedRequestImpl(realm, consumer,
				signatureMethod);
		// given
		String url = "http://seratch.net/";
		HttpMethod method = HttpMethod.POST;
		RequestBody body = new RequestBody("abc".getBytes(), "text/plain");
		String charset = "UTF-8";
		// when
		HttpResponse actual = target.doRequest(url, method, body, charset);
		// then
		assertNotNull(actual);
		assertTrue(200 == actual.getStatusCode());
	}

}

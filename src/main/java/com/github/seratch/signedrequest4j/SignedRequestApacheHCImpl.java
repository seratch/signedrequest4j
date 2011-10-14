/* 
 * Copyright 2011 Kazuhiro Sera
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language 
 * governing permissions and limitations under the License. 
 */
package com.github.seratch.signedrequest4j;

import com.github.seratch.signedrequest4j.pem.PEMReader;
import com.github.seratch.signedrequest4j.pem.PKCS1EncodedKeySpec;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link com.github.seratch.signedrequest4j.SignedRequest}
 */
public class SignedRequestApacheHCImpl implements SignedRequest {

	private static final String USER_AGENT = "SignedRequest4J HTTP Fetcher (+https://github.com/seratch/signedrequest4j)";

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestApacheHCImpl(
			OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod) {
		this(realm, consumer, null, signatureMethod);
	}

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestApacheHCImpl(
			OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod,
			Map<String, Object> additionalParameters) {
		this(realm, consumer, null, signatureMethod, additionalParameters);
	}

	public SignedRequestApacheHCImpl(
			OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken, SignatureMethod signatureMethod) {
		this.realm = realm;
		this.consumer = consumer;
		this.accessToken = accessToken;
		this.signatureMethod = signatureMethod;
	}

	public SignedRequestApacheHCImpl(
			OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken, SignatureMethod signatureMethod,
			Map<String, Object> additionalParameters) {
		this.realm = realm;
		this.consumer = consumer;
		this.accessToken = accessToken;
		this.signatureMethod = signatureMethod;
		this.additionalParameters = additionalParameters;
	}

	private OAuthRealm realm;

	private OAuthConsumer consumer;

	private OAuthAccessToken accessToken;

	private SignatureMethod signatureMethod;

	private String oAuthVersion = "1.0";

	private Map<String, Object> additionalParameters;

	private String rsaPrivateKeyValue;

	private int connectTimeoutMillis = 3000;

	private int readTimeoutMillis = 10000;

	private Map<String, String> headersToOverwrite = new HashMap<String, String>();

	/**
	 * {inheritDoc}
	 */
	@Override
	public void setHeader(String name, String value) {
		headersToOverwrite.put(name, value);
	}

	/**
	 * {inheritDoc}
	 */
	@Override
	public SignedRequest setConnectTimeoutMillis(int millis) {
		this.connectTimeoutMillis = millis;
		return this;
	}

	/**
	 * {inheritDoc}
	 */
	@Override
	public SignedRequest setReadTimeoutMillis(int millis) {
		this.readTimeoutMillis = millis;
		return this;
	}

	/**
	 * {inheritDoc}
	 */
	@Override
	public SignedRequest setRsaPrivateKeyValue(String rsaPrivateKeyValue) {
		this.rsaPrivateKeyValue = rsaPrivateKeyValue;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doGet(String url, String charset) throws IOException {
		return doRequest(url, HttpMethod.GET, new HashMap<String, Object>(), charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doHead(String url) throws IOException {
		return doRequest(url, HttpMethod.HEAD, new HashMap<String, Object>(), null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doOptions(String url) throws IOException {
		return doRequest(url, HttpMethod.OPTIONS, new HashMap<String, Object>(), null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doPost(String url, Map<String, Object> requestParameters, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.POST, requestParameters, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doPost(String url, RequestBody body, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.POST, body, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doDelete(String url, Map<String, Object> requestParameters, String charset) throws IOException {
		return doRequest(url, HttpMethod.DELETE, requestParameters, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doDelete(String url, RequestBody body, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.DELETE, body, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doPut(String url, Map<String, Object> requestParameters, String charset) throws IOException {
		return doRequest(url, HttpMethod.PUT, requestParameters, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doPut(String url, RequestBody body, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.PUT, body, charset);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doTrace(String url) throws IOException {
		return doRequest(url, HttpMethod.TRACE, new HashMap<String, Object>(), null);
	}


	@Override
	public HttpResponse doRequest(String url, HttpMethod method, RequestBody body, String charset) throws IOException {

		HttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = getRequest(method, url);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeoutMillis);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeoutMillis);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);

		for (String name : headersToOverwrite.keySet()) {
			request.setHeader(name, headersToOverwrite.get(name));
		}

		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis() / 1000;
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
		request.setHeader("Authorization", authorizationHeader);

		if (method == HttpMethod.POST) {
			HttpPost postRequest = (HttpPost) request;
			BasicHttpEntity entity = new BasicHttpEntity();
			entity.setContent(new ByteArrayInputStream(body.getBody()));
			entity.setContentType(body.getContentType());
			postRequest.setEntity(entity);
		} else if (method == HttpMethod.PUT) {
			HttpPut putRequest = (HttpPut) request;
			BasicHttpEntity entity = new BasicHttpEntity();
			entity.setContent(new ByteArrayInputStream(body.getBody()));
			entity.setContentType(body.getContentType());
			putRequest.setEntity(entity);
		}

		return toReturnValue(httpClient.execute(request), charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doRequest(String url, HttpMethod method, Map<String, Object> requestParameters, String charset)
			throws IOException {

		HttpEntity entity = null;
		if (method == HttpMethod.GET) {
			List<NameValuePair> params = toNameValuePairList(requestParameters);
			String queryString = URLEncodedUtils.format(params, "UTF-8");
			url = url.contains("?") ? url + "&" + queryString : url + "?" + queryString;
		} else {
			List<NameValuePair> params = toNameValuePairList(requestParameters);
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = getRequest(method, url);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeoutMillis);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeoutMillis);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);

		for (String name : headersToOverwrite.keySet()) {
			request.setHeader(name, headersToOverwrite.get(name));
		}

		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis() / 1000;
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
		request.setHeader("Authorization", authorizationHeader);

		if (entity != null) {
			if (method == HttpMethod.POST) {
				HttpPost postRequest = (HttpPost) request;
				postRequest.setEntity(entity);
			} else if (method == HttpMethod.PUT) {
				HttpPut putRequest = (HttpPut) request;
				putRequest.setEntity(entity);
			}
		}

		return toReturnValue(httpClient.execute(request), charset);

	}

	static List<NameValuePair> toNameValuePairList(Map<String, Object> params) {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value != null) {
				nameValuePairList.add(new BasicNameValuePair(key, value.toString()));
			}
		}
		return nameValuePairList;
	}

	static HttpUriRequest getRequest(HttpMethod method, String url) {
		if (method == HttpMethod.GET) {
			return new HttpGet(url);
		} else if (method == HttpMethod.POST) {
			return new HttpPost(url);
		} else if (method == HttpMethod.PUT) {
			return new HttpPut(url);
		} else if (method == HttpMethod.DELETE) {
			return new HttpDelete(url);
		} else if (method == HttpMethod.HEAD) {
			return new HttpHead(url);
		} else if (method == HttpMethod.OPTIONS) {
			return new HttpOptions(url);
		} else if (method == HttpMethod.TRACE) {
			return new HttpTrace(url);
		} else {
			return null;
		}
	}

	static HttpResponse toReturnValue(org.apache.http.HttpResponse response, String charset) throws IOException {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setStatusCode(response.getStatusLine().getStatusCode());
		Map<String, String> responseHeaders = new HashMap<String, String>();
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			responseHeaders.put(header.getName(), header.getValue());
		}
		httpResponse.setHeaders(responseHeaders);
		if (response.getEntity() != null) {
			httpResponse.setBody(EntityUtils.toByteArray(response.getEntity()));
		}
		httpResponse.setCharset(charset);
		return httpResponse;
	}

	static class Parameter {

		private final String key;
		private final Object value;

		public Parameter(String key, Object value) {
			this.key = key;
			if (value instanceof NotString) {
				throw new IllegalArgumentException("Invalid parameter value");
			}
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSignatureBaseString(String url, HttpMethod method, String oAuthNonce, Long oAuthTimestamp) {
		StringBuilder baseStringBuf = new StringBuilder();
		StringBuilder normalizedParamsBuf = new StringBuilder();
		for (Parameter param : getNormalizedParameters(oAuthNonce,
				oAuthTimestamp)) {
			if (normalizedParamsBuf.length() > 0) {
				normalizedParamsBuf.append("&");
			}
			normalizedParamsBuf.append(param.getKey());
			normalizedParamsBuf.append("=");
			normalizedParamsBuf.append(param.getValue());
		}
		baseStringBuf.append(OAuthEncoding.encode(method.toString().toUpperCase()));
		baseStringBuf.append("&");
		baseStringBuf.append(OAuthEncoding.encode(OAuthEncoding.normalizeURL(url)));
		baseStringBuf.append("&");
		baseStringBuf.append(OAuthEncoding.encode(normalizedParamsBuf.toString()));
		return baseStringBuf.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSignature(String url, HttpMethod method, String oAuthNonce, Long oAuthTimestamp) {
		String baseString = getSignatureBaseString(url, method, oAuthNonce,
				oAuthTimestamp);
		if (signatureMethod == SignatureMethod.HMAC_SHA1) {
			String algorithm = "HmacSHA1";
			String key = consumer.getConsumerSecret() + "&" +
					((accessToken != null && accessToken.getTokenSecret() != null)
							? accessToken.getTokenSecret() : "");
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
			try {
				Mac mac = Mac.getInstance(algorithm);
				mac.init(keySpec);
				byte[] rawValue = mac.doFinal(baseString.getBytes());
				return Base64.encode(rawValue);
			} catch (NoSuchAlgorithmException e) {
				throw new SignedRequestClientException("Invalid Alogrithm : "
						+ e.getLocalizedMessage());
			} catch (InvalidKeyException e) {
				throw new SignedRequestClientException("Invalid key : "
						+ e.getLocalizedMessage());
			}
		} else if (signatureMethod == SignatureMethod.RSA_SHA1) {
			if (rsaPrivateKeyValue == null || rsaPrivateKeyValue.length() == 0) {
				throw new SignedRequestClientException("RSA Private Key value is required.");
			}
			try {
				PEMReader reader = new PEMReader(new ByteArrayInputStream(
						this.rsaPrivateKeyValue.getBytes("UTF-8")));
				byte[] bytes = reader.getDerBytes();
				// PEM Reader's native string constructor is for filename.
				KeySpec keySpec = null;
				if (PEMReader.PRIVATE_PKCS1_MARKER.equals(reader.getBeginMarker())) {
					keySpec = (new PKCS1EncodedKeySpec(bytes)).getKeySpec();
				} else if (PEMReader.PRIVATE_PKCS8_MARKER.equals(reader.getBeginMarker())) {
					keySpec = new PKCS8EncodedKeySpec(bytes);
				} else {
					throw new SignedRequestClientException("Invalid PEM file: Unknown marker " +
							"for private key " + reader.getBeginMarker());
				}
				KeyFactory fac = KeyFactory.getInstance("RSA");
				PrivateKey privateKey = fac.generatePrivate(keySpec);
				Signature signer = Signature.getInstance("SHA1withRSA");
				signer.initSign(privateKey);
				signer.update(baseString.getBytes());
				return Base64.encode(signer.sign());
			} catch (Exception e) {
				throw new SignedRequestClientException("Cannot make a signature(RSA)", e);
			}
		} else if (signatureMethod == SignatureMethod.PLAINTEXT) {
			return baseString;
		} else {
			throw new SignedRequestClientException(
					"Invalid Signature Method (oauth_signature_method) : "
							+ signatureMethod.toString());
		}
	}

	@Override
	public String getAuthorizationHeader(String signature, String oAuthNonce, Long oAuthTimestamp) {
		StringBuilder buf = new StringBuilder();
		buf.append("OAuth ");
		if (realm != null) {
			buf.append("realm=\"");
			buf.append(OAuthEncoding.encode(realm));
			buf.append("\",");
		}
		if (accessToken != null) {
			buf.append("oauth_token=\"");
			buf.append(OAuthEncoding.encode(accessToken.getToken()));
			buf.append("\",");
		}
		buf.append("oauth_consumer_key=\"");
		buf.append(OAuthEncoding.encode(consumer.getConsumerKey()));
		buf.append("\",");
		buf.append("oauth_signature_method=\"");
		buf.append(OAuthEncoding.encode(signatureMethod));
		buf.append("\",");
		buf.append("oauth_signature=\"");
		buf.append(OAuthEncoding.encode(signature));
		buf.append("\",");
		buf.append("oauth_timestamp=\"");
		buf.append(OAuthEncoding.encode(oAuthTimestamp));
		buf.append("\",");
		buf.append("oauth_nonce=\"");
		buf.append(OAuthEncoding.encode(oAuthNonce));
		buf.append("\",");
		buf.append("oauth_version=\"");
		buf.append(OAuthEncoding.encode(oAuthVersion));
		buf.append("\"");
		if (additionalParameters != null && additionalParameters.size() > 0) {
			for (String key : additionalParameters.keySet()) {
				buf.append(",");
				buf.append(OAuthEncoding.encode(key));
				buf.append("=\"");
				buf.append(OAuthEncoding.encode(additionalParameters.get(key)));
				buf.append("\"");
			}
		}
		return buf.toString();
	}

	List<Parameter> getNormalizedParameters(String oAuthNonce, Long oAuthTimestamp) {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("oauth_consumer_key", consumer.getConsumerKey()));
		if (accessToken != null) {
			// 2 Legged OAuth does not need
			params.add(new Parameter("oauth_token", accessToken.getToken()));
		}
		params.add(new Parameter("oauth_nonce", oAuthNonce));
		params.add(new Parameter("oauth_signature_method", signatureMethod));
		params.add(new Parameter("oauth_timestamp", oAuthTimestamp));
		params.add(new Parameter("oauth_version", oAuthVersion));
		if (additionalParameters != null && additionalParameters.size() > 0) {
			for (String key : additionalParameters.keySet()) {
				params.add(new Parameter(key, OAuthEncoding.encode(additionalParameters.get(key))));
			}
		}
		Collections.sort(params, new Comparator<Parameter>() {
			public int compare(Parameter p1, Parameter p2) {
				return p1.getKey().compareTo(p2.getKey());
			}
		});
		return params;
	}

}

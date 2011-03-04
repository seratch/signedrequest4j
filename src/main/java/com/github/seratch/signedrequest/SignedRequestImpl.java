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
package com.github.seratch.signedrequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Default implementation of {@link SignedRequest}
 */
class SignedRequestImpl implements SignedRequest {

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestImpl(String realm, OAuthConsumer consumer,
			SignatureMethod signatureMethod) {
		this(realm, consumer, null, signatureMethod);
	}

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestImpl(String realm, OAuthConsumer consumer,
			SignatureMethod signatureMethod,
			Map<String, Object> additionalParameters) {
		this(realm, consumer, null, signatureMethod, additionalParameters);
	}

	public SignedRequestImpl(String realm, OAuthConsumer consumer,
			OAuthToken token, SignatureMethod signatureMethod) {
		this.realm = realm;
		this.token = token;
		this.consumerKey = consumer.getConsumerKey();
		this.consumerSecret = consumer.getConsumerSecret();
		this.signatureMethod = signatureMethod;
	}

	public SignedRequestImpl(String realm, OAuthConsumer consumer,
			OAuthToken token, SignatureMethod signatureMethod,
			Map<String, Object> additionalParameters) {
		this.realm = realm;
		this.token = token;
		this.consumerKey = consumer.getConsumerKey();
		this.consumerSecret = consumer.getConsumerSecret();
		this.signatureMethod = signatureMethod;
		this.additionalParameters = additionalParameters;
	}

	private String realm;

	private String consumerKey;

	private String consumerSecret;

	private OAuthToken token;

	private SignatureMethod signatureMethod;

	private String oAuthVersion = "1.0";

	private Map<String, Object> additionalParameters;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doDelete(String url) throws IOException {
		return doRequest(url, HttpMethod.DELETE, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doGet(String url, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.GET, null, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doHead(String url) throws IOException {
		return doRequest(url, HttpMethod.HEAD, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doOptions(String url) throws IOException {
		return doRequest(url, HttpMethod.OPTIONS, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doPost(String url,
			Map<String, Object> requestParameters, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.POST, requestParameters, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doPut(String url) throws IOException {
		return doRequest(url, HttpMethod.PUT, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doTrace(String url) throws IOException {
		return doRequest(url, HttpMethod.DELETE, null, null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doRequest(String url, HttpMethod method,
			Map<String, Object> requestParameters, String charset)
			throws IOException {
		if (method == HttpMethod.GET && requestParameters != null
				&& requestParameters.size() > 0) {
			for (String key : requestParameters.keySet()) {
				String param = key + "=" + requestParameters.get(key);
				url += (url.contains("?") ? "&" : "?") + param;
			}
		}
		HttpURLConnection conn = getHttpURLConnection(url, method);
		if (method == HttpMethod.POST && requestParameters != null
				&& requestParameters.size() > 0) {
			OutputStream os = null;
			OutputStreamWriter writer = null;
			try {
				conn.setDoOutput(true);
				os = conn.getOutputStream();
				writer = new OutputStreamWriter(os);
				for (String key : requestParameters.keySet()) {
					writer.append(key);
					writer.append("=");
					writer.append(requestParameters.get(key).toString());
				}
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (Exception e) {
					}
				}
				if (os != null) {
					try {
						os.close();
					} catch (Exception e) {
					}
				}
			}
		}
		conn.connect();
		HttpResponse response = new HttpResponse();
		response.setStatusCode(conn.getResponseCode());
		response.setHeaders(conn.getHeaderFields());
		response.setContent(getResponseCotent(conn, charset));
		return response;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpURLConnection getHttpURLConnection(String url, HttpMethod method)
			throws IOException {
		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis();
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature,
				oAuthNonce, oAuthTimestamp);
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(10000);
		conn.setRequestProperty("User-Agent",
				"Signed Request Client (+https://github.com/seratch/signed-request-for-java)");
		conn.setRequestMethod(method.toString());
		conn.setRequestProperty("Authorization", authorizationHeader);
		return conn;
	}

	static class Parameter {

		private final String key;
		private final Object value;

		public Parameter(String key, Object value) {
			this.key = key;
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
	public String getSignatureBaseString(String url, HttpMethod method,
			String oAuthNonce, Long oAuthTimestamp) {
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
		StringBuilder baseStringBuf = new StringBuilder();
		try {
			baseStringBuf.append(method.toString());
			baseStringBuf.append("&");
			baseStringBuf.append(URLEncoder.encode(url, "UTF-8"));
			baseStringBuf.append("&");
			baseStringBuf.append(URLEncoder.encode(
					normalizedParamsBuf.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new SignedRequestClientException("Invalid URL Encoding");
		}
		return baseStringBuf.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSignature(String url, HttpMethod method,
			String oAuthNonce, Long oAuthTimestamp) {
		String baseString = getSignatureBaseString(url, method, oAuthNonce,
				oAuthTimestamp);
		if (signatureMethod == SignatureMethod.HMAC_SHA1) {
			String algorithm = "HmacSHA1";
			String key = consumerSecret + "&";
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
		} else if (signatureMethod == SignatureMethod.PLAINTEXT) {
			return baseString;
		} else {
			throw new SignedRequestClientException(
					"Invalid Signature Method (oauth_signature_method) : "
							+ signatureMethod.toString());
		}
	}

	List<Parameter> getNormalizedParameters(String oAuthNonce,
			Long oAuthTimestamp) {
		List<Parameter> params = new ArrayList<Parameter>();
		params.add(new Parameter("oauth_consumer_key", consumerKey));
		if (token != null) {
			// 2 Legged OAuth does not need
			params.add(new Parameter("oauth_token", token));
		}
		params.add(new Parameter("oauth_nonce", oAuthNonce));
		params.add(new Parameter("oauth_signature_method", signatureMethod));
		params.add(new Parameter("oauth_timestamp", oAuthTimestamp));
		params.add(new Parameter("oauth_version", oAuthVersion));
		if (additionalParameters != null && additionalParameters.size() > 0) {
			for (String key : additionalParameters.keySet()) {
				params.add(new Parameter(key, additionalParameters.get(key)));
			}
		}
		Collections.sort(params, new Comparator<Parameter>() {
			public int compare(Parameter p1, Parameter p2) {
				return p1.getKey().compareTo(p2.getKey());
			};
		});
		return params;
	}

	String getAuthorizationHeader(String signature, String oAuthNonce,
			Long oAuthTimestamp) {
		StringBuilder buf = new StringBuilder();
		buf.append("OAuth ");
		if (realm != null) {
			buf.append("realm=\"" + realm + "\",");
		}
		buf.append("oauth_consumer_key=\"" + consumerKey + "\",");
		buf.append("oauth_signature_method=\"" + signatureMethod + "\",");
		buf.append("oauth_signature=\"" + signature + "\",");
		buf.append("oauth_timestamp=\"" + oAuthTimestamp + "\",");
		buf.append("oauth_nonce=\"" + oAuthNonce + "\",");
		buf.append("oauth_version=\"" + oAuthVersion + "\",");
		if (additionalParameters != null && additionalParameters.size() > 0) {
			for (String key : additionalParameters.keySet()) {
				buf.append(key + "=\"" + additionalParameters.get(key) + "\",");
			}
		}
		return buf.toString();
	}

	String getResponseCotent(HttpURLConnection conn, String charset)
			throws IOException {
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(is, charset));
			StringBuilder buf = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				buf.append(line);
				buf.append("\n");
			}
			return buf.toString();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e2) {
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (Exception e2) {
				}
			}
		}
	}

}

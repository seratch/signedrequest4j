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
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Default implementation of {@link SignedRequest}
 */
class SignedRequestImpl implements SignedRequest {

	/**
	 * {@inheritDoc}
	 */
	public SignedRequestImpl(String realm, OAuthConsumer consumer,
			SignatureMethod signatureMethod) {
		this.realm = realm;
		this.consumerKey = consumer.getConsumerKey();
		this.consumerSecret = consumer.getConsumerSecret();
		this.signatureMethod = signatureMethod;
	}

	/**
	 * {@inheritDoc}
	 */
	public SignedRequestImpl(String realm, OAuthConsumer consumer,
			SignatureMethod signatureMethod,
			Map<String, String> additionalParameters) {
		this.realm = realm;
		this.consumerKey = consumer.getConsumerKey();
		this.consumerSecret = consumer.getConsumerSecret();
		this.signatureMethod = signatureMethod;
		this.additionalParameters = additionalParameters;
	}

	private String realm;

	private String consumerKey;

	private String consumerSecret;

	private SignatureMethod signatureMethod;

	private String oAuthVersion = "1.0";

	static class OneTimeValues {
		String oAuthNonce;
		Long oAuthTimestamp;
	}

	private OneTimeValues oneTimeValues;

	private Map<String, String> additionalParameters;

	@Override
	public void setOAuthNonceAndOAuthTimestamp(String oAuthNonce,
			Long oAuthTimestamp) {
		oneTimeValues = new OneTimeValues();
		oneTimeValues.oAuthNonce = oAuthNonce;
		oneTimeValues.oAuthTimestamp = oAuthTimestamp;
	}

	@Override
	public HttpResponse doDeleteRequest(String url) throws IOException {
		return doRequest(url, HttpMethod.DELETE, null, null);
	}

	@Override
	public HttpResponse doGetRequest(String url, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.GET, null, charset);
	}

	@Override
	public HttpResponse doHeadRequest(String url) throws IOException {
		return doRequest(url, HttpMethod.HEAD, null, null);
	}

	@Override
	public HttpResponse doOptionsRequest(String url) throws IOException {
		return doRequest(url, HttpMethod.OPTIONS, null, null);
	}

	@Override
	public HttpResponse doPostRequest(String url,
			Map<String, Object> requestParameters, String charset)
			throws IOException {
		return doRequest(url, HttpMethod.POST, requestParameters, charset);
	}

	@Override
	public HttpResponse doPutRequest(String url) throws IOException {
		return doRequest(url, HttpMethod.PUT, null, null);
	}

	@Override
	public HttpResponse doTraceRequest(String url) throws IOException {
		return doRequest(url, HttpMethod.DELETE, null, null);
	}

	@Override
	public HttpResponse doRequest(String url, HttpMethod method,
			Map<String, Object> requestParameters, String charset)
			throws IOException {
		HttpResponse response = new HttpResponse();
		HttpURLConnection conn = getHttpURLConnection(url, method);
		if (method == HttpMethod.GET && requestParameters != null
				&& requestParameters.size() > 0) {
			for (String key : requestParameters.keySet()) {
				String param = key + requestParameters.get(key);
				url += (url.contains("?") ? "&" : "?") + param;
			}
		}
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
		if (oneTimeValues == null) {
			oneTimeValues = new OneTimeValues();
		}
		if (oneTimeValues.oAuthNonce == null) {
			oneTimeValues.oAuthNonce = String.valueOf(new SecureRandom()
					.nextLong());
		}
		if (oneTimeValues.oAuthTimestamp == null) {
			oneTimeValues.oAuthTimestamp = System.currentTimeMillis();
		}
		String signature = getSignature(url, method, oneTimeValues.oAuthNonce,
				oneTimeValues.oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature,
				oneTimeValues.oAuthNonce, oneTimeValues.oAuthTimestamp);
		oneTimeValues = null;

		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setConnectTimeout(3000);
		conn.setReadTimeout(10000);
		conn.setRequestProperty("User-Agent",
				"Signed Request Client (+https://github.com/seratch/signed-request)");
		conn.setRequestMethod(method.toString());
		conn.setRequestProperty("Authorization", authorizationHeader);
		return conn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSignature(String url, HttpMethod method,
			String oAuthNonce, Long oAuthTimestamp) {
		StringBuilder query = new StringBuilder();
		query.append("oauth_consumer_key=");
		query.append(consumerKey);
		query.append("&oauth_nonce=");
		query.append(oAuthNonce);
		query.append("&oauth_signature_method=");
		query.append(signatureMethod);
		query.append("&oauth_timestamp=");
		query.append(oAuthTimestamp);
		query.append("&oauth_version=");
		query.append(oAuthVersion);
		if (additionalParameters != null && additionalParameters.size() > 0) {
			for (String key : additionalParameters.keySet()) {
				query.append("&" + key + "=" + additionalParameters.get(key));
			}
		}
		StringBuilder buf = new StringBuilder();
		try {
			buf.append(method.toString());
			buf.append("&");
			buf.append(URLEncoder.encode(url, "UTF-8"));
			buf.append("&");
			buf.append(URLEncoder.encode(query.toString(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new SignedRequestClientException("Invalid URL Encoding");
		}
		String rawSignature = buf.toString();
		if (signatureMethod == SignatureMethod.HMAC_SHA1) {
			String algorithm = "HmacSHA1";
			String key = consumerSecret + "&";
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), algorithm);
			try {
				Mac mac = Mac.getInstance(algorithm);
				mac.init(keySpec);
				byte[] rawValue = mac.doFinal(rawSignature.getBytes());
				return Base64.encode(rawValue);
			} catch (NoSuchAlgorithmException e) {
				throw new SignedRequestClientException("Invalid Alogrithm : "
						+ e.getLocalizedMessage());
			} catch (InvalidKeyException e) {
				throw new SignedRequestClientException("Invalid key : "
						+ e.getLocalizedMessage());
			}
		} else if (signatureMethod == SignatureMethod.PLAINTEXT) {
			return rawSignature;
		} else {
			throw new SignedRequestClientException(
					"Invalid Signature Method (oauth_signature_method) : "
							+ signatureMethod.toString());
		}
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

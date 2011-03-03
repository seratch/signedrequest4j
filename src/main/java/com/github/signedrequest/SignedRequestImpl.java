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
package com.github.signedrequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import sun.misc.BASE64Encoder;

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

	private Map<String, String> additionalParameters;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpURLConnection getHttpURLConnection(String url, HttpMethod method)
			throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setRequestMethod(method.toString());
		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis();
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		conn.setRequestProperty("Authorization", getAuthorizationHeader(
				signature, oAuthNonce, oAuthTimestamp));
		return conn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpURLConnection getHttpURLConnection(String url,
			HttpMethod method, String oAuthNonce, Long oAuthTimestamp)
			throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url)
				.openConnection();
		conn.setRequestMethod(method.toString());
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		conn.setRequestProperty("Authorization", getAuthorizationHeader(
				signature, oAuthNonce, oAuthTimestamp));
		return conn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContent(String url, HttpMethod method) throws IOException {
		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis();
		return getContent(url, method, oAuthNonce, oAuthTimestamp);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getContent(String url, HttpMethod method, String oAuthNonce,
			Long oAuthTimestamp) throws IOException {
		HttpURLConnection conn = getHttpURLConnection(url, method, oAuthNonce,
				oAuthTimestamp);
		InputStream is = null;
		BufferedReader br = null;
		try {
			is = conn.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			StringBuilder buf = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				buf.append(line);
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
				return new BASE64Encoder().encode(rawValue);
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

}

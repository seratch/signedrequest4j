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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * OAuth Signed Request Verifier
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://oauth.net/core/1.0/">OAuth Core 1.0</a>
 */
public class SignedRequestVerifier {

	public static Map<String, String> parseAuthorizationHeader(String authorizationHeader) {
		Map<String, String> elements = new HashMap<String, String>();
		String[] keyAndValueArray = authorizationHeader.split(",");
		for (String keyAndValue : keyAndValueArray) {
			String[] arr = keyAndValue.split("=");
			String key = arr[0].trim().replaceAll("\"", "");
			String value = arr[1].trim().replaceAll("\"", "");
			// all the elements should be url-decoded.
			elements.put(key, OAuthEncoding.decode(value));
		}
		return elements;
	}

	public static boolean verifyHMacGetRequest(String url, String queryString, String authorizationHeader, OAuthConsumer consumer) {
		return verify(url, queryString, authorizationHeader, consumer, HttpMethod.GET, SignatureMethod.HMAC_SHA1);
	}

	public static boolean verifyHMacPostRequest(String url, String queryString, String authorizationHeader, OAuthConsumer consumer) {
		return verify(url, queryString, authorizationHeader, consumer, HttpMethod.POST, SignatureMethod.HMAC_SHA1);
	}

	private static final Set<String> oAuthElementNames = new HashSet<String>();

	static {
		oAuthElementNames.add("oauth_consumer_key");
		oAuthElementNames.add("oauth_signature_method");
		oAuthElementNames.add("oauth_signature");
		oAuthElementNames.add("oauth_timestamp");
		oAuthElementNames.add("oauth_nonce");
		oAuthElementNames.add("oauth_token");
		oAuthElementNames.add("oauth_version");
	}

	@Deprecated
	public static boolean verifyLegacyUncorrected(String url, String authorizationHeader, OAuthConsumer consumer,
	                                              HttpMethod httpMethod, SignatureMethod signatureMethod) {
		if (authorizationHeader == null) {
			return false;
		}
		Map<String, String> urlDecodedElements = parseAuthorizationHeader(authorizationHeader);
		SignedRequest req = SignedRequestFactory.create(consumer, signatureMethod);
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		for (String name : urlDecodedElements.keySet()) {
			String _name = name.replaceFirst("OAuth\\s+", "");
			if (!oAuthElementNames.contains(_name)) {
				// the element already should be url-encoded
				additionalParams.put(name, urlDecodedElements.get(name));
			}
		}
		req.setAdditionalAuthorizationHeaderParams(additionalParams);
		String nonce = urlDecodedElements.get("oauth_nonce");
		Long timestamp = Long.valueOf(urlDecodedElements.get("oauth_timestamp"));
		String signature = req.getSignature(url, httpMethod, nonce, timestamp);
		return signature.equals(urlDecodedElements.get("oauth_signature"));
	}

	public static boolean verify(String url, String queryString, String authorizationHeader, OAuthConsumer consumer,
	                             HttpMethod httpMethod, SignatureMethod signatureMethod) {
		if (authorizationHeader == null) {
			return false;
		}
		if (httpMethod.equals(HttpMethod.POST)) {
			throw new IllegalArgumentException("Please use verifyPOST instead for POST requests.");
		}
		Map<String, String> urlDecodedElements = parseAuthorizationHeader(authorizationHeader);
		SignedRequest req = SignedRequestFactory.create(consumer, signatureMethod);
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		for (String name : urlDecodedElements.keySet()) {
			String _name = name.replaceFirst("OAuth\\s+", "");
			if (!oAuthElementNames.contains(_name)) {
				// the element already should be url-encoded
				additionalParams.put(name, urlDecodedElements.get(name));
			}
		}
		req.setAdditionalAuthorizationHeaderParams(additionalParams);
		String nonce = urlDecodedElements.get("oauth_nonce");
		Long timestamp = Long.valueOf(urlDecodedElements.get("oauth_timestamp"));
		if (queryString == null) {
			queryString = "";
		}
		req.readQueryStringAndAddToSignatureBaseString(url + "?" + queryString);
		String signature = req.getSignature(url, httpMethod, nonce, timestamp);
		return signature.equals(urlDecodedElements.get("oauth_signature"));
	}

	public static boolean verifyPOST(String url, String queryString, String authorizationHeader, OAuthConsumer consumer,
	                                 SignatureMethod signatureMethod, Map<String, String> formParams) {
		if (authorizationHeader == null) {
			return false;
		}
		Map<String, String> urlDecodedElements = parseAuthorizationHeader(authorizationHeader);
		if (formParams != null) {
			for (String key : formParams.keySet()) {
				String value = formParams.get(key);
				if (value != null) {
					urlDecodedElements.put(key, value);
				}
			}
		}
		SignedRequest req = SignedRequestFactory.create(consumer, signatureMethod);
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		for (String name : urlDecodedElements.keySet()) {
			String _name = name.replaceFirst("OAuth\\s+", "");
			if (!oAuthElementNames.contains(_name)) {
				// the element already should be url-encoded
				additionalParams.put(name, urlDecodedElements.get(name));
			}
		}
		req.setAdditionalAuthorizationHeaderParams(additionalParams);
		String nonce = urlDecodedElements.get("oauth_nonce");
		Long timestamp = Long.valueOf(urlDecodedElements.get("oauth_timestamp"));
		if (queryString == null) {
			queryString = "";
		}
		req.readQueryStringAndAddToSignatureBaseString(url + "?" + queryString);
		String signature = req.getSignature(url, HttpMethod.POST, nonce, timestamp);
		return signature.equals(urlDecodedElements.get("oauth_signature"));
	}

	public static boolean verifyHMacGetRequest(
			String url, String queryString, String authorizationHeader, OAuthConsumer consumer, OAuthAccessToken accessToken) {
		return verify(url, queryString, authorizationHeader, consumer, accessToken, HttpMethod.GET, SignatureMethod.HMAC_SHA1);
	}

	public static boolean verifyHMacPostRequest(
			String url, String queryString, String authorizationHeader, OAuthConsumer consumer, OAuthAccessToken accessToken) {
		return verify(url, queryString, authorizationHeader, consumer, accessToken, HttpMethod.POST, SignatureMethod.HMAC_SHA1);
	}

	@Deprecated
	public static boolean verifyLegacyUncorrected(
			String url, String authorizationHeader, OAuthConsumer consumer, OAuthAccessToken accessToken,
			HttpMethod httpMethod, SignatureMethod signatureMethod) {
		if (authorizationHeader == null) {
			return false;
		}
		Map<String, String> urlDecodedElements = parseAuthorizationHeader(authorizationHeader);
		SignedRequest req = SignedRequestFactory.create(consumer, accessToken, signatureMethod);
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		for (String name : urlDecodedElements.keySet()) {
			String _name = name.replaceFirst("OAuth\\s+", "");
			if (!oAuthElementNames.contains(_name)) {
				// the element already should be url-encoded
				additionalParams.put(name, urlDecodedElements.get(name));
			}
		}
		req.setAdditionalAuthorizationHeaderParams(additionalParams);
		String signature = req.getSignature(url, httpMethod,
				urlDecodedElements.get("oauth_nonce"), Long.valueOf(urlDecodedElements.get("oauth_timestamp")));
		return signature.equals(urlDecodedElements.get("oauth_signature"));
	}

	public static boolean verify(
			String url, String queryString, String authorizationHeader, OAuthConsumer consumer, OAuthAccessToken accessToken,
			HttpMethod httpMethod, SignatureMethod signatureMethod) {
		if (authorizationHeader == null) {
			return false;
		}
		if (httpMethod.equals(HttpMethod.POST)) {
			throw new IllegalArgumentException("Please use verifyPOST instead for POST requests.");
		}
		Map<String, String> urlDecodedElements = parseAuthorizationHeader(authorizationHeader);
		SignedRequest req = SignedRequestFactory.create(consumer, accessToken, signatureMethod);
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		for (String name : urlDecodedElements.keySet()) {
			String _name = name.replaceFirst("OAuth\\s+", "");
			if (!oAuthElementNames.contains(_name)) {
				// the element already should be url-encoded
				additionalParams.put(name, urlDecodedElements.get(name));
			}
		}
		req.setAdditionalAuthorizationHeaderParams(additionalParams);
		if (queryString == null) {
			queryString = "";
		}
		req.readQueryStringAndAddToSignatureBaseString(url + "?" + queryString);
		String signature = req.getSignature(url, httpMethod,
				urlDecodedElements.get("oauth_nonce"), Long.valueOf(urlDecodedElements.get("oauth_timestamp")));
		return signature.equals(urlDecodedElements.get("oauth_signature"));
	}

	public static boolean verifyPOST(String url, String queryString, String authorizationHeader, OAuthConsumer consumer,
	                                 OAuthAccessToken accessToken, SignatureMethod signatureMethod, Map<String, String> formParams) {
		if (authorizationHeader == null) {
			return false;
		}
		Map<String, String> urlDecodedElements = parseAuthorizationHeader(authorizationHeader);
		if (formParams != null) {
			for (String key : formParams.keySet()) {
				String value = formParams.get(key);
				if (value != null) {
					urlDecodedElements.put(key, value);
				}
			}
		}
		SignedRequest req = SignedRequestFactory.create(consumer, accessToken, signatureMethod);
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		for (String name : urlDecodedElements.keySet()) {
			String _name = name.replaceFirst("OAuth\\s+", "");
			if (!oAuthElementNames.contains(_name)) {
				// the element already should be url-encoded
				additionalParams.put(name, urlDecodedElements.get(name));
			}
		}
		req.setAdditionalAuthorizationHeaderParams(additionalParams);
		if (queryString == null) {
			queryString = "";
		}
		req.readQueryStringAndAddToSignatureBaseString(url + "?" + queryString);
		String signature = req.getSignature(url, HttpMethod.POST,
				urlDecodedElements.get("oauth_nonce"), Long.valueOf(urlDecodedElements.get("oauth_timestamp")));
		return signature.equals(urlDecodedElements.get("oauth_signature"));
	}

}

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
import java.util.Map;

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
			String[] arr = keyAndValue.replaceAll("\"", "").split("=");
			elements.put(arr[0].trim(), arr[1].trim());
		}
		return elements;
	}

	public static boolean verifyHMacGetRequest(String url, String authorizationHeader, OAuthConsumer consumer) {
		return verify(url, authorizationHeader, consumer, HttpMethod.GET, SignatureMethod.HMAC_SHA1);
	}

	public static boolean verifyHMacPostRequest(String url, String authorizationHeader, OAuthConsumer consumer) {
		return verify(url, authorizationHeader, consumer, HttpMethod.POST, SignatureMethod.HMAC_SHA1);
	}

	public static boolean verify(
			String url, String authorizationHeader, OAuthConsumer consumer,
			HttpMethod httpMethod, SignatureMethod signatureMethod) {
		if (authorizationHeader == null) {
			return false;
		}
		Map<String, String> elements = parseAuthorizationHeader(authorizationHeader);
		SignedRequest req = SignedRequestFactory.get2LeggedOAuthRequest(consumer, signatureMethod);
		String signature = req.getSignature(url, httpMethod,
				elements.get("oauth_nonce"), Long.valueOf(elements.get("oauth_timestamp")));
		return OAuthEncoding.encode(signature).equals(elements.get("oauth_signature"));
	}

	public static boolean verifyHMacGetRequest(
			String url, String authorizationHeader, OAuthConsumer consumer, OAuthAccessToken accessToken) {
		return verify(url, authorizationHeader, consumer, accessToken, HttpMethod.GET, SignatureMethod.HMAC_SHA1);
	}

	public static boolean verifyHMacPostRequest(
			String url, String authorizationHeader, OAuthConsumer consumer, OAuthAccessToken accessToken) {
		return verify(url, authorizationHeader, consumer, accessToken, HttpMethod.POST, SignatureMethod.HMAC_SHA1);
	}

	public static boolean verify(
			String url, String authorizationHeader, OAuthConsumer consumer, OAuthAccessToken accessToken,
			HttpMethod httpMethod, SignatureMethod signatureMethod) {
		if (authorizationHeader == null) {
			return false;
		}
		Map<String, String> elements = parseAuthorizationHeader(authorizationHeader);
		SignedRequest req = SignedRequestFactory.get3LeggedOAuthRequest(consumer, accessToken, signatureMethod);
		String signature = req.getSignature(url, httpMethod,
				elements.get("oauth_nonce"), Long.valueOf(elements.get("oauth_timestamp")));
		return OAuthEncoding.encode(signature).equals(elements.get("oauth_signature"));
	}

}

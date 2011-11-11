package com.github.seratch.signedrequest4j.snippet;

import com.github.seratch.signedrequest4j.*;

import java.util.HashMap;
import java.util.Map;

public class VerifyingSignatureSnippet {
	public static void main(String[] args) throws Exception {

		OAuthConsumer consumer = new OAuthConsumer("consumer_key", "consumer_secret");

		Map<String, Object> additionalParams = new HashMap<String, Object>();
		additionalParams.put("xoauth_requestor_id", "user@example.com");

		String url = "http://sp.example.com/";
		HttpMethod httpMethod = HttpMethod.GET;
		SignatureMethod signatureMethod = SignatureMethod.HMAC_SHA1;
		String oAuthNonce = "nonce_value";
		Long oAuthTimestamp = 1272026745L;

		// 2-Legged OAuth request
		{
			SignedRequest signedRequest = SignedRequestFactory.create(consumer, additionalParams);
			String signature = signedRequest.getSignature(url, httpMethod, oAuthNonce, oAuthTimestamp);
			String authorizationHeader = signedRequest.getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
			System.out.println(authorizationHeader);
			boolean isValid = SignedRequestVerifier.verify(url, authorizationHeader, consumer, httpMethod, signatureMethod);
			if (isValid) {
				System.out.println("Signature is valid.");
			}
		}

		OAuthAccessToken accessToken = new OAuthAccessToken("token", "token_secret");

		// 3-Legged OAuth request
		{
			SignedRequest signedRequest = SignedRequestFactory.create(consumer, accessToken, additionalParams);
			String signature = signedRequest.getSignature(url, httpMethod, oAuthNonce, oAuthTimestamp);
			String authorizationHeader = signedRequest.getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
			System.out.println(authorizationHeader);
			boolean isValid = SignedRequestVerifier.verify(url, authorizationHeader, consumer, accessToken, httpMethod, signatureMethod);
			if (isValid) {
				System.out.println("Signature is valid.");
			}
		}

	}
}

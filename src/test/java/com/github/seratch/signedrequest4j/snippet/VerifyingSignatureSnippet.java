package com.github.seratch.signedrequest4j.snippet;

import com.github.seratch.signedrequest4j.*;

import java.util.HashMap;
import java.util.Map;

public class VerifyingSignatureSnippet {
	public static void main(String[] args) throws Exception {
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		additionalParams.put("xoauth_requestor_id", "user@example.com");
		SignedRequest signedRequest = SignedRequestFactory.create(
				new OAuthConsumer("consumer_key", "consumer_secret"),
				new OAuthAccessToken("token", "token_secret"),
				additionalParams);
		String signature = signedRequest.getSignature(
				"http://sp.example.com/",   // URL
				HttpMethod.GET,             // HTTP Method
				"nonce_value",              // oauth_nonce
				1272026745L                 // oauth_timestamp
		);
		if ("K7OrQ7UU+k94LnaezxFs4jBBekc=".equals(signature)) {
			System.out.println("Signature is valid.");
		}
	}
}

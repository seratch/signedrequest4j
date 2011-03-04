package com.github.seratch.signedrequest.snippet;

import com.github.seratch.signedrequest.HttpResponse;
import com.github.seratch.signedrequest.OAuthConsumer;
import com.github.seratch.signedrequest.SignedRequest;
import com.github.seratch.signedrequest.SignedRequestFactory;

import java.util.HashMap;
import java.util.Map;

public class SignatureWithAdditionalParamsSnippet {
	public static void main(String[] args) throws Exception {
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		additionalParams.put("xoauth_requestor_id", "user@example.com");
		SignedRequest signedRequest = SignedRequestFactory
				.get2LeggedOAuthRequest("http://sp.example.com/",
						new OAuthConsumer("consumer_key", "consumer_secret"),
						additionalParams);
		HttpResponse response = signedRequest.doGet(
				"http://sp.example.com/api/?aaa=bbb", "UTF-8");
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getContent());
	}
}

package com.github.seratch.signedrequest4j.snippet;

import com.github.seratch.signedrequest4j.HttpResponse;
import com.github.seratch.signedrequest4j.OAuthConsumer;
import com.github.seratch.signedrequest4j.SignedRequest;
import com.github.seratch.signedrequest4j.SignedRequestFactory;

import java.util.HashMap;
import java.util.Map;

public class TwoLeggedSignatureWithAdditionalParamsSnippet {
	public static void main(String[] args) throws Exception {
		Map<String, Object> additionalParams = new HashMap<String, Object>();
		additionalParams.put("xoauth_requestor_id", "user@example.com");
		SignedRequest signedRequest = SignedRequestFactory.get2LeggedOAuthRequest(
				new OAuthConsumer("consumer_key", "consumer_secret"),
				additionalParams);
		HttpResponse response = signedRequest.doGet(
				"http://sp.example.com/api/?aaa=bbb", "UTF-8");
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getContent());
	}
}

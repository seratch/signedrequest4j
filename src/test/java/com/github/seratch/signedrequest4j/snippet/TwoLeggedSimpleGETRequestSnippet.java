package com.github.seratch.signedrequest4j.snippet;

import com.github.seratch.signedrequest4j.HttpResponse;
import com.github.seratch.signedrequest4j.OAuthConsumer;
import com.github.seratch.signedrequest4j.SignedRequest;
import com.github.seratch.signedrequest4j.SignedRequestFactory;

public class TwoLeggedSimpleGETRequestSnippet {
	public static void main(String[] args) throws Exception {
		SignedRequest signedRequest = SignedRequestFactory.create(
				new OAuthConsumer("consumer_key", "consumer_secret"));
		HttpResponse response = signedRequest.doGet(
				"https://github.com/seratch/signedrequest4j", "UTF-8");
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getTextBody());
	}
}

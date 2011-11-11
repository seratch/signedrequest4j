package com.github.seratch.signedrequest4j.snippet;

import com.github.seratch.signedrequest4j.*;

public class ThreeLeggedSimpleGETRequestSnippet {

	public static void main(String[] args) throws Exception {

		OAuthConsumer consumer = new OAuthConsumer("consumer_key", "consumer_secret");
		OAuthAccessToken accessToken = new OAuthAccessToken("token", "token_secret");

		SignedRequest signedRequest = SignedRequestFactory.create(consumer, accessToken);

		HttpResponse response = signedRequest.doGet("https://github.com/seratch/signedrequest4j", "UTF-8");
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getTextBody());

	}

}

package com.github.seratch.signedrequest.snippet;

import com.github.seratch.signedrequest.HttpResponse;
import com.github.seratch.signedrequest.OAuthConsumer;
import com.github.seratch.signedrequest.SignedRequest;
import com.github.seratch.signedrequest.SignedRequestFactory;

import java.util.HashMap;
import java.util.Map;

public class SimplePOSTRequestSnippet {
	public static void main(String[] args) throws Exception {
		SignedRequest signedRequest = SignedRequestFactory
				.get2LeggedOAuthRequest("http://sp.example.com/",
						new OAuthConsumer("consumer_key", "consumer_secret"));
		Map<String, Object> requestParameters = new HashMap<String, Object>();
		requestParameters.put("something", "updated");
		HttpResponse response = signedRequest.doPost(
				"https://github.com/seratch/signed-request", requestParameters,
				"UTF-8");
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getContent());
	}
}

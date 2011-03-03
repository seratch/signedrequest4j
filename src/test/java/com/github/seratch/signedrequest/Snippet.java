package com.github.seratch.signedrequest;

public class Snippet {

	// GET / HTTP/1.1
	// User-Agent: Signed Request Client
	// (+https://github.com/seratch/signed-request)
	// Authorization: OAuth realm="http://sp.example.com/",
	// oauth_consumer_key="consumer_key",oauth_signature_method="HMAC-SHA1",oauth_signature="q9eXspUbaYB6NEmAXxxXXuBrk10=",oauth_timestamp="1299157615628",oauth_nonce="-4568253211378628139",oauth_version="1.0",

	public static void main(String[] args) throws Exception {
		SignedRequest signedRequest = SignedRequestFactory.getInstance(
				"http://sp.example.com/", new OAuthConsumer("consumer_key",
						"consumer_secret"));
		HttpResponse response = signedRequest.doGetRequest(
				"https://github.com/seratch/signed-request", "UTF-8");
		System.out.println(response.getStatusCode());
		System.out.println(response.getHeaders());
		System.out.println(response.getContent());
	}

}

package com.github.seratch.signedrequest4j;

import java.io.IOException;

public class HttpException extends IOException {

	private static final long serialVersionUID = 1L;

	private String message;

	private HttpResponse response;

	public HttpException(String message, HttpResponse response) {
		setMessage(message);
		setResponse(response);
	}

	public String getMessage() {
		return message;
	}

	public HttpResponse getResponse() {
		return response;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setResponse(HttpResponse response) {
		this.response = response;
	}

}

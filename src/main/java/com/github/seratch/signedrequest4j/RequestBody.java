package com.github.seratch.signedrequest4j;

public class RequestBody {

	private byte[] body;

	private String contentType;

	public RequestBody(byte[] body, String contentType) {
		this.body = body;
		this.contentType = contentType;
	}

	public byte[] getBody() {
		return body;
	}

	public String getContentType() {
		return contentType;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}

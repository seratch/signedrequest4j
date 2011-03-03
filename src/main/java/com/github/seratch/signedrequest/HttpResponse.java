package com.github.seratch.signedrequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

	public Integer statusCode;

	public Map<String, List<String>> headers = new HashMap<String, List<String>>();

	private String content;

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}

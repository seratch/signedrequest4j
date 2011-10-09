/*
 * Copyright 2011 Kazuhiro Sera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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

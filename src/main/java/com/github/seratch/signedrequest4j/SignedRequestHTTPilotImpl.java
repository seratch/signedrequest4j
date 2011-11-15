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

import com.github.seratch.signedrequest4j.pem.PEMReader;
import com.github.seratch.signedrequest4j.pem.PKCS1EncodedKeySpec;
import httpilot.HTTP;
import httpilot.HTTPIOException;
import httpilot.Method;
import httpilot.Request;
import httpilot.Response;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * implementation of {@link SignedRequest} with HTTPilot
 */
public class SignedRequestHTTPilotImpl extends SignedRequestBaseImpl implements SignedRequest {

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestHTTPilotImpl(
			OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod) {
		this(realm, consumer, null, signatureMethod);
	}

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestHTTPilotImpl(
			OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod,
			Map<String, Object> additionalParameters) {
		this(realm, consumer, null, signatureMethod, additionalParameters);
	}

	public SignedRequestHTTPilotImpl(
			OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken, SignatureMethod signatureMethod) {
		this.realm = realm;
		this.consumer = consumer;
		this.accessToken = accessToken;
		this.signatureMethod = signatureMethod;
	}

	public SignedRequestHTTPilotImpl(
			OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken, SignatureMethod signatureMethod,
			Map<String, Object> additionalParameters) {
		this.realm = realm;
		this.consumer = consumer;
		this.accessToken = accessToken;
		this.signatureMethod = signatureMethod;
		this.additionalParameters = additionalParameters;
	}

	@Override
	public HttpResponse doRequest(String url, HttpMethod method, RequestBody body, String charset) throws IOException {

		// create http request
		Request request = new Request(url);
		// read get parameters for signature base string
		readGetParameters(url);

		request.setEnableThrowingIOException(true);
		request.setUserAgent(USER_AGENT);
		request.setConnectTimeoutMillis(connectTimeoutMillis);
		request.setReadTimeoutMillis(readTimeoutMillis);
		request.setCharset(charset);
		for (String name : headersToOverwrite.keySet()) {
			request.setHeader(name, headersToOverwrite.get(name));
		}

		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis() / 1000;
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
		request.setHeader("Authorization", authorizationHeader);

		request.setBody(body.getBody(), body.getContentType());

		try {
			Response response = HTTP.request(new Method(method.name()), request);
			return toReturnValue(response);
		} catch (HTTPIOException ex) {
			HttpResponse httpResponse = toReturnValue(ex.getResponse());
			throw new HttpException(ex.getMessage(), httpResponse);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doRequest(String url, HttpMethod method, Map<String, Object> requestParameters, String charset)
			throws IOException {

		// create http request
		Request request = new Request(url);
		request.setEnableThrowingIOException(true);
		request.setUserAgent(USER_AGENT);
		request.setConnectTimeoutMillis(connectTimeoutMillis);
		request.setReadTimeoutMillis(readTimeoutMillis);
		request.setCharset(charset);
		for (String name : headersToOverwrite.keySet()) {
			request.setHeader(name, headersToOverwrite.get(name));
		}


		if (method == HttpMethod.GET) {
			request.setQueryParams(requestParameters);
		} else {
			// message body
			request.setFormParams(requestParameters);
		}
		// read get parameters for signature base string
		readGetParameters(request.getUrl());
		readGetParameters(request.getQueryParams().toString());

		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis() / 1000;
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
		request.setHeader("Authorization", authorizationHeader);

		try {
			Response response = HTTP.request(new Method(method.name()), request);
			return toReturnValue(response);
		} catch (HTTPIOException ex) {
			HttpResponse httpResponse = toReturnValue(ex.getResponse());
			throw new HttpException(ex.getMessage(), httpResponse);
		}

	}

	static HttpResponse toReturnValue(Response response) {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setStatusCode(response.getStatus());
		httpResponse.setHeaders(response.getHeaders());
		httpResponse.setBody(response.getBody());
		httpResponse.setCharset(response.getCharset());
		return httpResponse;
	}

}

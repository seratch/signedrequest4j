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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link com.github.seratch.signedrequest4j.SignedRequest}
 */
public class SignedRequestApacheHCImpl extends SignedRequestBaseImpl implements SignedRequest {

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestApacheHCImpl(
			OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod) {
		this(realm, consumer, null, signatureMethod);
	}

	/**
	 * 2 Legged OAuth Request
	 */
	public SignedRequestApacheHCImpl(
			OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod,
			Map<String, Object> additionalParameters) {
		this(realm, consumer, null, signatureMethod, additionalParameters);
	}

	public SignedRequestApacheHCImpl(
			OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken, SignatureMethod signatureMethod) {
		this.realm = realm;
		this.consumer = consumer;
		this.accessToken = accessToken;
		this.signatureMethod = signatureMethod;
	}

	public SignedRequestApacheHCImpl(
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

		HttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = getRequest(method, url);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeoutMillis);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeoutMillis);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);

		for (String name : headersToOverwrite.keySet()) {
			request.setHeader(name, headersToOverwrite.get(name));
		}

		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis() / 1000;
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
		request.setHeader("Authorization", authorizationHeader);

		if (method == HttpMethod.POST) {
			HttpPost postRequest = (HttpPost) request;
			BasicHttpEntity entity = new BasicHttpEntity();
			entity.setContent(new ByteArrayInputStream(body.getBody()));
			entity.setContentType(body.getContentType());
			postRequest.setEntity(entity);
		} else if (method == HttpMethod.PUT) {
			HttpPut putRequest = (HttpPut) request;
			BasicHttpEntity entity = new BasicHttpEntity();
			entity.setContent(new ByteArrayInputStream(body.getBody()));
			entity.setContentType(body.getContentType());
			putRequest.setEntity(entity);
		}

		org.apache.http.HttpResponse apacheHCResponse = httpClient.execute(request);
		if (apacheHCResponse.getStatusLine().getStatusCode() >= 400) {
			HttpResponse httpResponse = toReturnValue(apacheHCResponse, charset);
			throw new HttpException(apacheHCResponse.getStatusLine().getReasonPhrase(), httpResponse);
		}
		return toReturnValue(apacheHCResponse, charset);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpResponse doRequest(String url, HttpMethod method, Map<String, Object> requestParameters, String charset)
			throws IOException {

		HttpEntity entity = null;
		if (method == HttpMethod.GET) {
			List<NameValuePair> params = toNameValuePairList(requestParameters);
			String queryString = URLEncodedUtils.format(params, "UTF-8");
			if (queryString != null && !queryString.isEmpty()) {
				url = url.contains("?") ? url + "&" + queryString : url + "?" + queryString;
			}
		} else {
			List<NameValuePair> params = toNameValuePairList(requestParameters);
			entity = new UrlEncodedFormEntity(params, "UTF-8");
		}

		// read get parameters for signature base string
		readQueryStringAndAddToSignatureBaseString(url);

		HttpClient httpClient = new DefaultHttpClient();
		HttpUriRequest request = getRequest(method, url);
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, connectTimeoutMillis);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, readTimeoutMillis);
		httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, USER_AGENT);
		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, charset);

		for (String name : headersToOverwrite.keySet()) {
			request.setHeader(name, headersToOverwrite.get(name));
		}

		String oAuthNonce = String.valueOf(new SecureRandom().nextLong());
		Long oAuthTimestamp = System.currentTimeMillis() / 1000;
		String signature = getSignature(url, method, oAuthNonce, oAuthTimestamp);
		String authorizationHeader = getAuthorizationHeader(signature, oAuthNonce, oAuthTimestamp);
		request.setHeader("Authorization", authorizationHeader);

		if (entity != null) {
			if (method == HttpMethod.POST) {
				HttpPost postRequest = (HttpPost) request;
				postRequest.setEntity(entity);
			} else if (method == HttpMethod.PUT) {
				HttpPut putRequest = (HttpPut) request;
				putRequest.setEntity(entity);
			}
		}

		org.apache.http.HttpResponse apacheHCResponse = httpClient.execute(request);
		if (apacheHCResponse.getStatusLine().getStatusCode() >= 400) {
			HttpResponse httpResponse = toReturnValue(apacheHCResponse, charset);
			throw new HttpException(apacheHCResponse.getStatusLine().getReasonPhrase(), httpResponse);
		}
		return toReturnValue(apacheHCResponse, charset);

	}

	static List<NameValuePair> toNameValuePairList(Map<String, Object> params) {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		for (String key : params.keySet()) {
			Object value = params.get(key);
			if (value != null) {
				nameValuePairList.add(new BasicNameValuePair(key, value.toString()));
			}
		}
		return nameValuePairList;
	}

	static HttpUriRequest getRequest(HttpMethod method, String url) {
		if (method == HttpMethod.GET) {
			return new HttpGet(url);
		} else if (method == HttpMethod.POST) {
			return new HttpPost(url);
		} else if (method == HttpMethod.PUT) {
			return new HttpPut(url);
		} else if (method == HttpMethod.DELETE) {
			return new HttpDelete(url);
		} else if (method == HttpMethod.HEAD) {
			return new HttpHead(url);
		} else if (method == HttpMethod.OPTIONS) {
			return new HttpOptions(url);
		} else if (method == HttpMethod.TRACE) {
			return new HttpTrace(url);
		} else {
			return null;
		}
	}

	static HttpResponse toReturnValue(org.apache.http.HttpResponse response, String charset) throws IOException {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setStatusCode(response.getStatusLine().getStatusCode());
		Map<String, String> responseHeaders = new HashMap<String, String>();
		Header[] headers = response.getAllHeaders();
		for (Header header : headers) {
			responseHeaders.put(header.getName(), header.getValue());
		}
		httpResponse.setHeaders(responseHeaders);
		if (response.getEntity() != null) {
			httpResponse.setBody(EntityUtils.toByteArray(response.getEntity()));
		}
		httpResponse.setCharset(charset);
		return httpResponse;
	}

}

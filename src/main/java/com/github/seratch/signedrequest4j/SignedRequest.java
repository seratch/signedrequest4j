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

import java.io.IOException;
import java.util.Map;

/**
 * <pre>
 * Singed OAuth Request
 *
 * - 3 Legged OAuth Request
 * http://oauth.net/core/1.0/#signing_process
 *
 * - 2 Legged Oauth Request a.k.a OAuth Consumer Request, OpenSocial Signed Request
 * http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html
 * </pre>
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 */
public interface SignedRequest {

	/**
	 * Overwrite request header value
	 *
	 * @param name  Header name
	 * @param value Header value
	 */
	void setHeader(String name, String value);

	/**
	 * Returns OAuth Signature Base String.
	 *
	 * @param url            Request URL
	 * @param method         HTTP Method
	 * @param oAuthNonce     OAuth Nonce Value
	 * @param oAuthTimestamp OAuth Timestamp Value
	 * @return OAuth Signature Base String
	 */
	String getSignatureBaseString(String url, HttpMethod method, String oAuthNonce, Long oAuthTimestamp);

	/**
	 * Returns OAuth Signature.
	 *
	 * @param url            Request URL
	 * @param method         HTTP Method
	 * @param oAuthNonce     OAuth Nonce Value
	 * @param oAuthTimestamp OAuth Timestamp Value
	 * @return OAuth Signature
	 */
	String getSignature(String url, HttpMethod method, String oAuthNonce, Long oAuthTimestamp);

	/**
	 * Returns Authorization Header String
	 *
	 * @param signature      OAuth Signature
	 * @param oAuthNonce     OAuth Nonce  Value
	 * @param oAuthTimestamp OAuth Timestamp Value
	 * @return Authorization Header String
	 */
	String getAuthorizationHeader(String signature, String oAuthNonce, Long oAuthTimestamp);

	/**
	 * Do HTTP request and returns Http response
	 *
	 * @param url               Request URL
	 * @param method            HTTP Method
	 * @param requestParameters Request parameters(OPTIONAL)
	 * @param charset           Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doRequest(String url, HttpMethod method, Map<String, Object> requestParameters, String charset)
			throws IOException;

	/**
	 * Do HTTP request and returns Http response
	 *
	 * @param url     Request URL
	 * @param method  HTTP Method
	 * @param body    body(bytes)
	 * @param charset Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doRequest(String url, HttpMethod method, RequestBody body, String charset)
			throws IOException;

	/**
	 * HTTP/1.1 GET request and returns Http response
	 *
	 * @param url     Request URL
	 * @param charset Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doGet(String url, String charset) throws IOException;

	/**
	 * HTTP/1.1 POST request and returns Http response
	 *
	 * @param url               Request URL
	 * @param requestParameters Request parameters
	 * @param charset           Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doPost(String url, Map<String, Object> requestParameters, String charset) throws IOException;

	/**
	 * HTTP/1.1 POST request and returns Http response
	 *
	 * @param url     Request URL
	 * @param body    Request body
	 * @param charset Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doPost(String url, RequestBody body, String charset) throws IOException;

	/**
	 * HTTP/1.1 PUT request and returns Http response
	 *
	 * @param url               Request URL
	 * @param requestParameters Request parameters
	 * @param charset           Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doPut(String url, Map<String, Object> requestParameters, String charset) throws IOException;

	/**
	 * HTTP/1.1 PUT request and returns Http response
	 *
	 * @param url     Request URL
	 * @param body    Request body
	 * @param charset Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doPut(String url, RequestBody body, String charset) throws IOException;

	/**
	 * HTTP/1.1 DELETE request and returns Http response
	 *
	 * @param url               Request URL
	 * @param requestParameters Request parameters
	 * @param charset           Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doDelete(String url, Map<String, Object> requestParameters, String charset) throws IOException;

	/**
	 * HTTP/1.1 DELETE request and returns Http response
	 *
	 * @param url     Request URL
	 * @param body    Request body
	 * @param charset Charset
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doDelete(String url, RequestBody body, String charset) throws IOException;

	/**
	 * HTTP/1.1 HEAD request and returns Http response
	 *
	 * @param url Request URL
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doHead(String url) throws IOException;

	/**
	 * HTTP/1.1 OPTIONS request and returns Http response
	 *
	 * @param url Request URL
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doOptions(String url) throws IOException;

	/**
	 * HTTP/1.1 TRACE request and returns Http response
	 *
	 * @param url Request URL
	 * @return HTTP Response
	 * @throws IOException
	 */
	HttpResponse doTrace(String url) throws IOException;

	/**
	 * Set connect timeout millisecs
	 *
	 * @param millis
	 */
	SignedRequest setConnectTimeoutMillis(int millis);

	/**
	 * Set read timeout millisecs
	 *
	 * @param millis
	 */
	SignedRequest setReadTimeoutMillis(int millis);

	/**
	 * Set RSA Private Key Value
	 *
	 * @param rsaPrivateKeyValue RSA Private Key PEM Value
	 * @return Self
	 */
	SignedRequest setRsaPrivateKeyValue(String rsaPrivateKeyValue);

	/**
	 * Returns additional parameters that are used for signature
	 *
	 * @return additional parameters
	 */
	Map<String, Object> getAdditionalAuthorizationHeaderParams();

	/**
	 * Set additional parameters that are used for signature
	 *
	 * @param additionalParams additional parameters
	 */
	void setAdditionalAuthorizationHeaderParams(Map<String, Object> additionalParams);

}

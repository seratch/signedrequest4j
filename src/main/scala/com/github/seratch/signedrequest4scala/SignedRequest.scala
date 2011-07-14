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
package com.github.seratch.signedrequest4scala

import java.net.HttpURLConnection

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
trait SignedRequest {

  /**
   * Returns Signed {@link HttpURLConnection} instance.
   *
   * @param url    Request URL
   * @param method HTTP Method
   * @return Signed {@link HttpURLConnection} instance
   * @throws IOException
   */
  def getHttpURLConnection(url: String, method: HttpMethod): HttpURLConnection


  /**
   * Set RSA Private Key Value
   *
   * @param rsaPrivateKeyValue RSA Private Key PEM Value
   * @return Self
   */
  def setRsaPrivateKeyValue(rsaPrivateKeyValue: String): SignedRequest

  /**
   * Returns OAuth Signature Base String.
   *
   * @param url            Request URL
   * @param method         HTTP Method
   * @param oAuthNonce     OAuth Nonce Value
   * @param oAuthTimestamp OAuth Timestamp Value
   * @return OAuth Signature Base String
   */
  def getSignatureBaseString(url: String, method: HttpMethod, oAuthNonce: String, oAuthTimestamp: Long): String

  /**
   * Returns OAuth Signature.
   *
   * @param url            Request URL
   * @param method         HTTP Method
   * @param oAuthNonce     OAuth Nonce Value
   * @param oAuthTimestamp OAuth Timestamp Value
   * @return OAuth Signature
   */
  def getSignature(url: String, method: HttpMethod, oAuthNonce: String, oAuthTimestamp: Long): String

  /**
   * Returns Authorization Header String
   *
   * @param signature	  OAuth Signature
   * @param oAuthNonce	 OAuth Nonce  Value
   * @param oAuthTimestamp OAuth Timestamp Value
   * @return Authorization Header String
   */
  def getAuthorizationHeader(signature: String, oAuthNonce: String, oAuthTimestamp: Long): String

  /**
   * HTTP/1.1 HTTP request and returns Http response
   *
   * @param url               Request URL
   * @param method            HTTP Method
   * @param requestParameters Request parameters(OPTIONAL)
   * @param charset           Charset
   * @return HTTP Response
   * @throws IOException
   */
  def doRequest(url: String, method: HttpMethod, requestParameters: Map[String, Any], charset: String): HttpResponse

  /**
   * HTTP/1.1 GET request and returns Http response
   *
   * @param url     Request URL
   * @param charset Charset
   * @return HTTP Response
   * @throws IOException
   */
  def doGet(url: String, charset: String): HttpResponse

  /**
   * HTTP/1.1 POST request and returns Http response
   *
   * @param url               Request URL
   * @param requestParameters Request parameters
   * @param charset           Charset
   * @return HTTP Response
   * @throws IOException
   */
  def doPost(url: String, requestParameters: Map[String, Any], charset: String): HttpResponse

  /**
   * HTTP/1.1 PUT request and returns Http response
   *
   * @param url Request URL
   * @return HTTP Response
   * @throws IOException
   */
  def doPut(url: String): HttpResponse

  /**
   * HTTP/1.1 DELETE request and returns Http response
   *
   * @param url Request URL
   * @return HTTP Response
   * @throws IOException
   */
  def doDelete(url: String): HttpResponse

  /**
   * HTTP/1.1 HEAD request and returns Http response
   *
   * @param url Request URL
   * @return HTTP Response
   * @throws IOException
   */
  def doHead(url: String): HttpResponse

  /**
   * HTTP/1.1 OPTIONS request and returns Http response
   *
   * @param url Request URL
   * @return HTTP Response
   * @throws IOException
   */
  def doOptions(url: String): HttpResponse

  /**
   * HTTP/1.1 TRACE request and returns Http response
   *
   * @param url Request URL
   * @return HTTP Response
   * @throws IOException
   */
  def doTrace(url: String): HttpResponse

}
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

/**
 * <pre>
 * Singed OAuth Request Factory
 *
 * - 3 Legged OAuth Request
 * http://oauth.net/core/1.0/#signing_process
 *
 * SignedRequest req = SignedRequestFactory.get3LeggedOAuthRequest(
 * 			"http://sp.example.com",
 * 			new OAuthConsumer("consumer_key", "consumer_secret"),
 * 			new OAuthToken("token_value"));
 *
 * - 2 Legged Oauth Request a.k.a OAuth Consumer Request, OpenSocial Signed Request
 * http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html
 *
 * SignedRequest req = SignedRequestFactory.get2LeggedOAuthRequest(
 * 			"http://sp.example.com",
 * 			new OAuthConsumer("consumer_key", "consumer_secret"));
 * </pre>
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 */
object SignedRequestFactory {

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param consumer OAuth consumer
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(consumer: OAuthConsumer): SignedRequest = {
    new SignedRequestImpl(null, consumer, SignatureMethod.HMAC_SHA1)
  }

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param realm	realm(nullable)
   * @param consumer OAuth consumer
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer): SignedRequest = {
    new SignedRequestImpl(realm, consumer, SignatureMethod.HMAC_SHA1)
  }

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param consumer			 OAuth consumer
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(consumer: OAuthConsumer, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(null, consumer, SignatureMethod.HMAC_SHA1, additionalParameters)
  }

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param realm				realm(nullable)
   * @param consumer			 OAuth consumer
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(realm, consumer, SignatureMethod.HMAC_SHA1, additionalParameters)
  }

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param consumer		OAuth consumer
   * @param signatureMethod Signature Method
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(consumer: OAuthConsumer, signatureMethod: SignatureMethod): SignedRequest = {
    new SignedRequestImpl(null, consumer, signatureMethod)
  }

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param realm		   realm(nullable)
   * @param consumer		OAuth consumer
   * @param signatureMethod Signature Method
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer, signatureMethod: SignatureMethod): SignedRequest = {
    new SignedRequestImpl(realm, consumer, signatureMethod)
  }

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param consumer			 OAuth consumer
   * @param signatureMethod	  Signature Method
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(consumer: OAuthConsumer, signatureMethod: SignatureMethod, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(null, consumer, signatureMethod, additionalParameters)
  }

  /**
   * Returns {@link SignedRequest} instance(2 Legged OAuth).
   *
   * @param realm				realm(nullable)
   * @param consumer			 OAuth consumer
   * @param signatureMethod	  Signature Method
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get2LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer, signatureMethod: SignatureMethod, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(realm, consumer, signatureMethod, additionalParameters)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param consumer OAuth consumer
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(consumer: OAuthConsumer, token: OAuthToken): SignedRequest = {
    new SignedRequestImpl(null, consumer, token, SignatureMethod.HMAC_SHA1)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param realm	realm(nullable)
   * @param consumer OAuth consumer
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer, token: OAuthToken): SignedRequest = {
    new SignedRequestImpl(realm, consumer, token, SignatureMethod.HMAC_SHA1)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param consumer			 OAuth consumer
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(consumer: OAuthConsumer, token: OAuthToken, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(null, consumer, token, SignatureMethod.HMAC_SHA1, additionalParameters)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param realm				realm(nullable)
   * @param consumer			 OAuth consumer
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer, token: OAuthToken, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(realm, consumer, token, SignatureMethod.HMAC_SHA1, additionalParameters)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param consumer		OAuth consumer
   * @param signatureMethod Signature Method
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(consumer: OAuthConsumer, token: OAuthToken, signatureMethod: SignatureMethod): SignedRequest = {
    new SignedRequestImpl(null, consumer, token, signatureMethod)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param realm		   realm(nullable)
   * @param consumer		OAuth consumer
   * @param signatureMethod Signature Method
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer, token: OAuthToken, signatureMethod: SignatureMethod): SignedRequest = {
    new SignedRequestImpl(realm, consumer, token, signatureMethod)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param consumer			 OAuth consumer
   * @param signatureMethod	  Signature Method
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(consumer: OAuthConsumer, token: OAuthToken, signatureMethod: SignatureMethod, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(null, consumer, token, signatureMethod, additionalParameters)
  }

  /**
   * Returns {@link SignedRequest} instance(3 Legged OAuth).
   *
   * @param realm				realm(nullable)
   * @param consumer			 OAuth consumer
   * @param signatureMethod	  Signature Method
   * @param additionalParameters Additional parameters(optional)
   * @return {@link SignedRequest} instance.
   */
  def get3LeggedOAuthRequest(realm: OAuthRealm, consumer: OAuthConsumer, token: OAuthToken, signatureMethod: SignatureMethod, additionalParameters: Map[String, Any]): SignedRequest = {
    new SignedRequestImpl(realm, consumer, token, signatureMethod, additionalParameters)
  }

}

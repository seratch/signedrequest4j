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

import java.util.Map;

/**
 * <pre>
 * Singed OAuth Request Factory
 *
 * - 3 Legged OAuth Request
 * http://oauth.net/core/1.0/#signing_process
 *
 * SignedRequest req = SignedRequestFactory.create(
 * 			"http://sp.example.com",
 * 			new OAuthConsumer("consumer_key", "consumer_secret"),
 * 			new OAuthAccessToken("token_value"));
 *
 * - 2 Legged Oauth Request a.k.a OAuth Consumer Request, OpenSocial Signed Request
 * http://oauth.googlecode.com/svn/spec/ext/consumer_request/1.0/drafts/1/spec.html
 *
 * SignedRequest req = SignedRequestFactory.create(
 * 			"http://sp.example.com",
 * 			new OAuthConsumer("consumer_key", "consumer_secret"));
 * </pre>
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 */
public class SignedRequestFactory {

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param consumer OAuth consumer
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer) {
		return new SignedRequestApacheHCImpl(null, consumer, SignatureMethod.HMAC_SHA1);
	}

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param realm    realm(nullable)
	 * @param consumer OAuth consumer
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer) {
		return new SignedRequestApacheHCImpl(realm, consumer, SignatureMethod.HMAC_SHA1);
	}

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param consumer             OAuth consumer
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer, Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(null, consumer, SignatureMethod.HMAC_SHA1, additionalParameters);
	}

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param realm                realm(nullable)
	 * @param consumer             OAuth consumer
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer,
	                                   Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(realm, consumer,
				SignatureMethod.HMAC_SHA1, additionalParameters);
	}

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param consumer        OAuth consumer
	 * @param signatureMethod Signature Method
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer, SignatureMethod signatureMethod) {
		return new SignedRequestApacheHCImpl(null, consumer, signatureMethod);
	}

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param realm           realm(nullable)
	 * @param consumer        OAuth consumer
	 * @param signatureMethod Signature Method
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod) {
		return new SignedRequestApacheHCImpl(realm, consumer, signatureMethod);
	}

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param consumer             OAuth consumer
	 * @param signatureMethod      Signature Method
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer, SignatureMethod signatureMethod,
	                                   Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(null, consumer, signatureMethod, additionalParameters);
	}

	/**
	 * Returns {@link SignedRequest} instance(2 Legged OAuth).
	 *
	 * @param realm                realm(nullable)
	 * @param consumer             OAuth consumer
	 * @param signatureMethod      Signature Method
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer, SignatureMethod signatureMethod,
	                                   Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(realm, consumer, signatureMethod, additionalParameters);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param consumer OAuth consumer
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer, OAuthAccessToken accessToken) {
		return new SignedRequestApacheHCImpl(null, consumer, accessToken, SignatureMethod.HMAC_SHA1);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param realm    realm(nullable)
	 * @param consumer OAuth consumer
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken) {
		return new SignedRequestApacheHCImpl(realm, consumer, accessToken, SignatureMethod.HMAC_SHA1);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param consumer             OAuth consumer
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer, OAuthAccessToken accessToken,
	                                   Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(null, consumer, accessToken, SignatureMethod.HMAC_SHA1, additionalParameters);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param realm                realm(nullable)
	 * @param consumer             OAuth consumer
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken,
	                                   Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(realm, consumer, accessToken, SignatureMethod.HMAC_SHA1, additionalParameters);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param consumer        OAuth consumer
	 * @param signatureMethod Signature Method
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer, OAuthAccessToken accessToken,
	                                   SignatureMethod signatureMethod) {
		return new SignedRequestApacheHCImpl(null, consumer, accessToken, signatureMethod);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param realm           realm(nullable)
	 * @param consumer        OAuth consumer
	 * @param signatureMethod Signature Method
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken,
	                                   SignatureMethod signatureMethod) {
		return new SignedRequestApacheHCImpl(realm, consumer, accessToken, signatureMethod);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param consumer             OAuth consumer
	 * @param signatureMethod      Signature Method
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthConsumer consumer, OAuthAccessToken accessToken,
	                                   SignatureMethod signatureMethod, Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(null, consumer, accessToken, signatureMethod, additionalParameters);
	}

	/**
	 * Returns {@link SignedRequest} instance(3 Legged OAuth).
	 *
	 * @param realm                realm(nullable)
	 * @param consumer             OAuth consumer
	 * @param signatureMethod      Signature Method
	 * @param additionalParameters Additional parameters(optional)
	 * @return {@link SignedRequest} instance.
	 */
	public static SignedRequest create(OAuthRealm realm, OAuthConsumer consumer, OAuthAccessToken accessToken,
	                                   SignatureMethod signatureMethod, Map<String, Object> additionalParameters) {
		return new SignedRequestApacheHCImpl(realm, consumer, accessToken, signatureMethod, additionalParameters);
	}

}

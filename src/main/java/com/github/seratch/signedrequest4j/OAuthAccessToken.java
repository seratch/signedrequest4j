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

import org.apache.log4j.Logger;

/**
 * OAuth Access Token
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://oauth.net/core/1.0/">OAuth Core 1.0</a>
 */
public class OAuthAccessToken implements NotString {

	private static Logger log = Logger.getLogger(OAuthAccessToken.class);

	private final String token;
	private final String tokenSecret;

	public OAuthAccessToken(String token, String tokenSecret) {
		this.token = token;
		this.tokenSecret = tokenSecret;
		if (log.isDebugEnabled()) {
			log.debug("token: " + token + ", tokenSecret: " + tokenSecret);
		}
	}

	public String getToken() {
		return this.token;
	}

	public String getTokenSecret() {
		return this.tokenSecret;
	}

}

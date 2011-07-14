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

/**
 * Signature Method
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://tools.ietf.org/html/rfc5849#section-3.4">[RFC5849]</a>
 */
public enum SignatureMethod {

	/**
	 * HMAC-SHA1
	 */
	HMAC_SHA1("HMAC-SHA1"),
	/**
	 * RSA-SHA1
	 */
	RSA_SHA1("RSA-SHA1"),
	/**
	 * PLAINTEXT
	 */
	PLAINTEXT("PLAINTEXT");

	private String str;

	private SignatureMethod(String str) {
		this.str = str;
	}

	@Override
	public String toString() {
		return this.str;
	}

}

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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * OAuth Spec Encoding
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://oauth.net/core/1.0/">OAuth Core 1.0</a>
 */
public class OAuthEncoding {

	public static String encode(Object obj) {
		String encoded = "";
		try {
			encoded = URLEncoder.encode(obj.toString(), "UTF-8")
					.replace("+", "%20")
					.replace("*", "%2A")
					.replace("%7E", "~");
		} catch (UnsupportedEncodingException ignore) {
		}
		return encoded;
	}

	public static String decode(String encoded) {
		String decoded = "";
		try {
			decoded = URLDecoder.decode(encoded, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
		}
		return decoded;
	}

	public static String normalizeURL(String url) {
		try {
			URI uri = new URI(url);
			String scheme = uri.getScheme().toLowerCase();
			String authority = uri.getAuthority().toLowerCase();
			boolean dropPort = (scheme.equals("http") && uri.getPort() == 80)
					|| (scheme.equals("https") && uri.getPort() == 443);
			if (dropPort) {
				// find the last : in the authority
				int index = authority.lastIndexOf(":");
				if (index >= 0) {
					authority = authority.substring(0, index);
				}
			}
			String path = uri.getRawPath();
			if (path == null || path.length() <= 0) {
				path = "/"; // conforms to RFC 2616 section 3.2.2
			}
			// we know that there is no query and no fragment here.
			return scheme + "://" + authority + path;
		} catch (Exception e) {
			return url;
		}
	}

}

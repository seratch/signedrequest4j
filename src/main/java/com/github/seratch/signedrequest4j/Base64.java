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
 * An implelementation of "Base64"
 *
 * @author <a href="mailto:seratch@gmail.com">Kazuhiro Sera</a>
 * @see <a href="http://en.wikipedia.org/wiki/Base64">Base64</a>
 */
public final class Base64 {

	private final static char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	private static int[] charToInt = new int[128];

	static {
		for (int i = 0; i < chars.length; i++) {
			charToInt[chars[i]] = i;
		}
	}

	public static String encode(byte[] bytes) {
		int size = bytes.length;
		char[] ar = new char[((size + 2) / 3) * 4];
		int a = 0;
		int i = 0;
		while (i < size) {
			byte b0 = bytes[i++];
			byte b1 = (i < size) ? bytes[i++] : 0;
			byte b2 = (i < size) ? bytes[i++] : 0;

			int mask = 0x3F;
			ar[a++] = chars[(b0 >> 2) & mask];
			ar[a++] = chars[((b0 << 4) | ((b1 & 0xFF) >> 4)) & mask];
			ar[a++] = chars[((b1 << 2) | ((b2 & 0xFF) >> 6)) & mask];
			ar[a++] = chars[b2 & mask];
		}
		switch (size % 3) {
			case 1:
				ar[--a] = '=';
			case 2:
				ar[--a] = '=';
		}
		return new String(ar);
	}

	public static byte[] decode(String str) {
		int delta = str.endsWith("==") ? 2 : str.endsWith("=") ? 1 : 0;
		byte[] bytes = new byte[str.length() * 3 / 4 - delta];
		int mask = 0xFF;
		int index = 0;
		for (int i = 0; i < str.length(); i += 4) {
			int c0 = charToInt[str.charAt(i)];
			int c1 = charToInt[str.charAt(i + 1)];
			bytes[index++] = (byte) (((c0 << 2) | (c1 >> 4)) & mask);
			if (index >= bytes.length) {
				return bytes;
			}
			int c2 = charToInt[str.charAt(i + 2)];
			bytes[index++] = (byte) (((c1 << 4) | (c2 >> 2)) & mask);
			if (index >= bytes.length) {
				return bytes;
			}
			int c3 = charToInt[str.charAt(i + 3)];
			bytes[index++] = (byte) (((c2 << 6) | c3) & mask);
		}
		return bytes;
	}

}

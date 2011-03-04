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

    private static final String[] MAPPING_TABLE = {"A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e",
            "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "+", "/"};

    public static String encode(byte[] bytes) {
        StringBuilder bitPatterns = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            int b = bytes[i];
            if (b < 0) {
                b += 256;
            }
            String bitStr = Integer.toBinaryString(b);
            while (bitStr.length() < 8) {
                bitStr = "0" + bitStr;
            }
            bitPatterns.append(bitStr);
        }
        while (bitPatterns.length() % 6 != 0) {
            bitPatterns.append("0");
        }
        StringBuilder dest = new StringBuilder();
        for (int i = 0; i < bitPatterns.length(); i += 6) {
            int index = Integer.parseInt(bitPatterns.substring(i, i + 6), 2);
            dest.append(MAPPING_TABLE[index]);
        }
        while (dest.length() % 4 != 0) {
            dest.append("=");
        }
        return dest.toString();
    }

}

package com.github.seratch.signedrequest4j;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

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

package com.github.seratch.signedrequest4j;

import com.github.seratch.signedrequest4j.OAuthEncoding.*;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.BDDMockito.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class OAuthEncodingTest {

	@Test
	public void type() throws Exception {
		assertNotNull(OAuthEncoding.class);
	}

	@Test
	public void instantiation() throws Exception {
		OAuthEncoding target = new OAuthEncoding();
		assertNotNull(target);
	}

	@Test
	public void encode_A$Object() throws Exception {
		OAuthRealm obj = new OAuthRealm("ab+c~");
		String actual = OAuthEncoding.encode(obj);
		String expected = "ab%2Bc~";
		assertEquals(expected, actual);
	}

	@Test
	public void normalizeURL_A$String() throws Exception {
		String url = "HTTP://example.COM:80/";
		String actual = OAuthEncoding.normalizeURL(url);
		String expected = "http://example.com/";
		assertEquals(expected, actual);
	}

}

package com.github.seratch.signedrequest4j;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.github.seratch.signedrequest4j.OAuthEncoding.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

	@Test
	public void decode_A$String() throws Exception {
		String encoded = "ab%2Bc~";
		String actual = OAuthEncoding.decode(encoded);
		String expected = "ab+c~";
		assertThat(actual, is(equalTo(expected)));
	}

}

package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
	public void encode_A$Object_ErrorPatterns() throws Exception {
		OAuthRealm obj = new OAuthRealm("ab%&=+c~");
		String actual = OAuthEncoding.encode(obj);
		String expected = "ab%25%26%3D%2Bc~";
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

	@Test
	public void decode_A$String_Percent() throws Exception {
		String encoded = "%25";
		String actual = OAuthEncoding.decode(encoded);
		String expected = "%";
		assertEquals(expected, actual);
	}

	@Test
	public void decode_A$String_PercentAndEtc() throws Exception {
		String encoded = "abc%25aaa";
		String actual = OAuthEncoding.decode(encoded);
		String expected = "abc%aaa";
		assertEquals(expected, actual);
	}

	@Test
	public void decode_A$String_Sharp() throws Exception {
		String encoded = "%だよ";
		String actual = OAuthEncoding.decode(encoded);
		String expected = "%だよ";
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void decode_A$String_And() throws Exception {
		String encoded = "&だよ";
		String actual = OAuthEncoding.decode(encoded);
		String expected = "&だよ";
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void decode_A$String_Equal() throws Exception {
		String encoded = "=だよ";
		String actual = OAuthEncoding.decode(encoded);
		String expected = "=だよ";
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void decode_A$String_Plus() throws Exception {
		String encoded = "+だよ";
		String actual = OAuthEncoding.decode(encoded);
		String expected = " だよ";
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void decode_A$String_Mixed() throws Exception {
		String encoded = "%a&b=c+d";
		String actual = OAuthEncoding.decode(encoded);
		String expected = "%a&b=c+d";
		assertThat(actual, is(equalTo(expected)));
	}

}

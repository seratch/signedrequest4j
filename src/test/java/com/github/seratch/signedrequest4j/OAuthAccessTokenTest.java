package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class OAuthAccessTokenTest {

	@Test
	public void type() throws Exception {
		assertNotNull(OAuthAccessToken.class);
	}

	@Test
	public void instantiation() throws Exception {
		String token = null;
		String tokenSecret = null;
		OAuthAccessToken target = new OAuthAccessToken(token, tokenSecret);
		assertNotNull(target);
	}

	@Test
	public void getToken_A$() throws Exception {
		String token = "t";
		String tokenSecret = "ts";
		OAuthAccessToken target = new OAuthAccessToken(token, tokenSecret);
		String actual = target.getToken();
		String expected = "t";
		assertEquals(expected, actual);
	}

	@Test
	public void getTokenSecret_A$() throws Exception {
		String token = "t";
		String tokenSecret = "ts";
		OAuthAccessToken target = new OAuthAccessToken(token, tokenSecret);
		String actual = target.getTokenSecret();
		String expected = "ts";
		assertEquals(expected, actual);
	}

}

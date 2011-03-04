package com.github.seratch.signedrequest4j;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthTokenTest {

	@Test
	public void type() throws Exception {
		assertNotNull(OAuthToken.class);
	}

	@Test
	public void instantiation() throws Exception {
		String token = null;
		OAuthToken target = new OAuthToken(token);
		assertNotNull(target);
	}

	@Test
	public void toString_A$() throws Exception {
		String token = "tokenvalue";
		OAuthToken target = new OAuthToken(token);
		// given
		// when
		String actual = target.toString();
		// then
		String expected = "tokenvalue";
		assertEquals(expected, actual);
	}

}

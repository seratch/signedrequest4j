package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpMethodTest {

	@Test
	public void type() throws Exception {
		assertNotNull(HttpMethod.class);
	}

	@Test
	public void toString_A$() throws Exception {
		HttpMethod target = HttpMethod.GET;
		String actual = target.toString();
		String expected = "GET";
		assertEquals(expected, actual);
	}

}

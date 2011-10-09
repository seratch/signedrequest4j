package com.github.seratch.signedrequest4j;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpResponseTest {

	@Test
	public void type() throws Exception {
		assertNotNull(HttpResponse.class);
	}

	@Test
	public void instantiation() throws Exception {
		HttpResponse target = new HttpResponse();
		assertNotNull(target);
	}

	@Test
	public void getStatusCode_A$() throws Exception {
		HttpResponse target = new HttpResponse();
		target.setStatusCode(200);
		Integer actual = target.getStatusCode();
		Integer expected = 200;
		assertEquals(expected, actual);
	}

	@Test
	public void setStatusCode_A$Integer() throws Exception {
		HttpResponse target = new HttpResponse();
		Integer statusCode = 501;
		target.setStatusCode(statusCode);
	}

	@Test
	public void getHeaders_A$() throws Exception {
		HttpResponse target = new HttpResponse();
		Map<String, List<String>> actual = target.getHeaders();
		assertNotNull(actual);
	}

	@Test
	public void setHeaders_A$Map() throws Exception {
		HttpResponse target = new HttpResponse();
		Map<String, List<String>> headers = target.getHeaders();
		target.setHeaders(headers);
	}

}

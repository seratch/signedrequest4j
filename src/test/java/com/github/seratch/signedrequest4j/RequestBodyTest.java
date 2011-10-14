package com.github.seratch.signedrequest4j;

import com.github.seratch.signedrequest4j.RequestBody.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import static org.mockito.BDDMockito.*;

public class RequestBodyTest {

	@Test
	public void type() throws Exception {
		assertThat(RequestBody.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		byte[] body = null;
		String contentType = null;
		RequestBody target = new RequestBody(body, contentType);
		assertThat(target, notNullValue());
	}

}

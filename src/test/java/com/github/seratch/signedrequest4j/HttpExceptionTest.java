package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HttpExceptionTest {

	@Test
	public void type() throws Exception {
		assertThat(HttpException.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		String message = null;
		HttpResponse response = null;
		HttpException target = new HttpException(message, response);
		assertThat(target, notNullValue());
	}

}

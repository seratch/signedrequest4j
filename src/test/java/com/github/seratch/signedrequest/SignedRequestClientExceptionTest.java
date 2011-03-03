package com.github.seratch.signedrequest;

import static org.junit.Assert.*;
import com.github.seratch.signedrequest.SignedRequestClientException;
import org.junit.Test;

public class SignedRequestClientExceptionTest {

	@Test
	public void type() throws Exception {
		assertNotNull(SignedRequestClientException.class);
	}

	@Test
	public void instantiation() throws Exception {
		String message = "saaasdfsa";
		SignedRequestClientException target = new SignedRequestClientException(
				message);
		assertNotNull(target);
	}

	@Test
	public void getLocalizedMessage_A$() throws Exception {
		String message = "saaasdfsa";
		SignedRequestClientException target = new SignedRequestClientException(
				message);
		String actual = target.getLocalizedMessage();
		String expected = "saaasdfsa";
		assertEquals(expected, actual);
	}

	@Test
	public void getMessage_A$() throws Exception {
		String message = "sdfafa";
		SignedRequestClientException target = new SignedRequestClientException(
				message);
		String actual = target.getMessage();
		String expected = "sdfafa";
		assertEquals(expected, actual);
	}

}

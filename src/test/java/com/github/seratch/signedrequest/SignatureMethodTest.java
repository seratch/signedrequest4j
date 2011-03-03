package com.github.seratch.signedrequest;

import static org.junit.Assert.*;
import com.github.seratch.signedrequest.SignatureMethod;
import org.junit.Test;

public class SignatureMethodTest {

	@Test
	public void type() throws Exception {
		assertNotNull(SignatureMethod.class);
	}

	@Test
	public void toString_A$() throws Exception {
		SignatureMethod target = SignatureMethod.HMAC_SHA1;
		String actual = target.toString();
		String expected = "HMAC-SHA1";
		assertEquals(expected, actual);
	}

}

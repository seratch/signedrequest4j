package com.github.seratch.signedrequest;

import static org.junit.Assert.*;
import org.junit.Test;

public class OAuthConsumerTest {

	@Test
	public void type() throws Exception {
		assertNotNull(OAuthConsumer.class);
	}

	@Test
	public void instantiation() throws Exception {
		OAuthConsumer target = new OAuthConsumer("aa", "bb");
		assertNotNull(target);
	}

}

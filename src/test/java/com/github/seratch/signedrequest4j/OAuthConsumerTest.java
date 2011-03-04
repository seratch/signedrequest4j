package com.github.seratch.signedrequest4j;

import static org.junit.Assert.*;
import com.github.seratch.signedrequest4j.OAuthConsumer;
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

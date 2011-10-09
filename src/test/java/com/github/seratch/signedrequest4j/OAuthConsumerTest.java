package com.github.seratch.signedrequest4j;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

	@Test
	public void getConsumerKey_A$() throws Exception {
		String consumerKey = "aaaa";
		String consumerSecret = "sdfas";
		OAuthConsumer target = new OAuthConsumer(consumerKey, consumerSecret);
		// given
		// when
		String actual = target.getConsumerKey();
		// then
		String expected = "aaaa";
		assertEquals(expected, actual);
	}

	@Test
	public void getConsumerSecret_A$() throws Exception {
		String consumerKey = "aaaa";
		String consumerSecret = "sdfas";
		OAuthConsumer target = new OAuthConsumer(consumerKey, consumerSecret);
		// given
		// when
		String actual = target.getConsumerSecret();
		// then
		String expected = "sdfas";
		assertEquals(expected, actual);
	}

}

package com.github.seratch.signedrequest4j;

import com.github.seratch.signedrequest4j.OAuthRealm.*;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.BDDMockito.*;


public class OAuthRealmTest {

	@Test
	public void type() throws Exception {
		assertNotNull(OAuthRealm.class);
	}

	@Test
	public void instantiation() throws Exception {
		String realm = null;
		OAuthRealm target = new OAuthRealm(realm);
		assertNotNull(target);
	}

	@Test
	public void toString_A$() throws Exception {
		String realm = "realmrealm";
		OAuthRealm target = new OAuthRealm(realm);
		String actual = target.toString();
		String expected = realm;
		assertEquals(expected, actual);
	}

}

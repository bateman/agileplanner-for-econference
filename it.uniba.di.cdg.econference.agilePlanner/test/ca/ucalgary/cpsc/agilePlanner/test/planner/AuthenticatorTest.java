package ca.ucalgary.cpsc.agilePlanner.test.planner;

import persister.auth.AbstractAuthenticator;
import persister.auth.AuthenticatorFactory;
import persister.auth.TestAuthenticator;
import junit.framework.TestCase;


public class AuthenticatorTest extends TestCase{

	public void testAuthenticatorFactory(){
		AbstractAuthenticator authenticator = AuthenticatorFactory.getAuthenticator();
		
		assertNotNull(authenticator);
		assertEquals(TestAuthenticator.class, authenticator.getClass());
		
	}
	
}

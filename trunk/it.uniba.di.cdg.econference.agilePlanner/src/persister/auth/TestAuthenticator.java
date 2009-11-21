package persister.auth;

public class TestAuthenticator extends AbstractAuthenticator {

	@Override
	public boolean performAuth(String username, String password) {
		return true;
	}

}

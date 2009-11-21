package persister.auth;

public abstract class AbstractAuthenticator {

	
	
	public abstract boolean performAuth(String username, String password);
	
	public boolean isValidUser(String username, String password){
		return performAuth(username, password);
	}
	
	
}

package persister.auth;

public class AuthenticatorFactory {

	
	public static String AuthenticatorClass = "persister.auth.TestAuthenticator";
	private static AbstractAuthenticator instance = null;
	
	public static AbstractAuthenticator getAuthenticator(){
		try {

			Class clazz = Class.forName(AuthenticatorClass);
			if(instance == null || !(instance.getClass() == clazz)){
				instance = (AbstractAuthenticator) clazz.newInstance();
			}
			return instance;

		} catch (InstantiationException e) {
			util.Logger.singleton().error(e);
		} catch (IllegalAccessException e) {
			util.Logger.singleton().error(e);
		} catch (ClassNotFoundException e) {
			util.Logger.singleton().error(e);
		}
		return null;
	}
}

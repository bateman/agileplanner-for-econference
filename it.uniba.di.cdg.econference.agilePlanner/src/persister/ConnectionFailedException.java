package persister;

public class ConnectionFailedException extends Exception {

	private static final long serialVersionUID = 5999065406148580200L;

    public ConnectionFailedException(String text) {
        super(text);
    }
}

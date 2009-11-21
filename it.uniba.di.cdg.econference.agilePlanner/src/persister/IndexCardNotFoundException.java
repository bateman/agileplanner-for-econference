package persister;

public class IndexCardNotFoundException extends Exception {

    private static final long serialVersionUID = 3245412463429559884L;

    private long id;

    public IndexCardNotFoundException(String message) {
        super(message);
        this.id = 0;
    }

    public IndexCardNotFoundException(String message, long id) {
        super(message);
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

}

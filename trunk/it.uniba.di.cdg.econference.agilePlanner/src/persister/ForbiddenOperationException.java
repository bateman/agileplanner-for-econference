package persister;

public class ForbiddenOperationException extends Exception {

    private static final long serialVersionUID = 5768605185867579762L;

    private long id;

    public ForbiddenOperationException(String text, long id) {
        super(text);
        this.id = id;
    }

    public ForbiddenOperationException(String text) {
        super(text);
        this.id = 0;
    }

    public long getId() {
        return this.id;
    }

}

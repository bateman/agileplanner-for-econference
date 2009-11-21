package persister;

public class CouldNotLoadProjectException extends Exception {

    private static final long serialVersionUID = -8707653527120525548L;

    public CouldNotLoadProjectException(String arg0) {
        super(arg0);
    }

    public CouldNotLoadProjectException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

}

package persister.util;

public class FileSystemIDGenerator {

    private long id;

    private static FileSystemIDGenerator generator;

    private FileSystemIDGenerator() {
        this.id = 1;
    }

    public static FileSystemIDGenerator getInstance() {
        if (generator == null) {
            generator = new FileSystemIDGenerator();
            return generator;
        }
        else {
            return generator;
        }
    }

    public void init(long id) {
        this.id = id;
    }

    public long getNextID() {
        return this.id++;
    }

    public void reset() {
        this.id = 1;
    }

}

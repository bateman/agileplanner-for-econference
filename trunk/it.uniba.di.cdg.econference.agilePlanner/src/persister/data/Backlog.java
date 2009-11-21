package persister.data;

public interface Backlog extends IndexCardWithChildren {

    public static final int DEFAULT_HEIGHT = 400;
    public static final int DEFAULT_WIDTH = 400;

    // DOES NOT UPDATE PARENT-CHILD RELATIONS!
    public void update(Backlog backlog);
    
  

}

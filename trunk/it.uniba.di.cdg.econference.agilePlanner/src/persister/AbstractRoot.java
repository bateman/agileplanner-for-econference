package persister;

import java.io.Serializable;

/**
 * @author webers
 * 
 * DO NOT IMPLEMENT THIS INTERFACE!
 * 
 * FOR INTERNAL USE ONLY
 * 
 */
public interface AbstractRoot extends Serializable, Cloneable {

    public long getId();
    public String getName();
    public void setId(long id);
    public void setName(String name);
    public Object clone();
    
}

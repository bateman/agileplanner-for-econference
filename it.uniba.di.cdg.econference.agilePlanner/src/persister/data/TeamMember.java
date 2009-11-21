package persister.data;

import java.io.Serializable;

public interface TeamMember extends  Cloneable {
	
	public String getName();
	public void setName(String name); 
	public Object clone();
	public boolean equals(Object owner);
}

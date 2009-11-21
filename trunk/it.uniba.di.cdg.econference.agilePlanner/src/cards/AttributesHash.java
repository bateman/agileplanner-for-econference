package cards;

import java.util.HashMap;
import java.util.Set;

public class AttributesHash {
	
	static final long serialVersionUID = 0;
	
	private HashMap<String, String> attributes;
	
	public AttributesHash(HashMap<String, String> initial){
		attributes = initial;
	}

	public Object clone() {
		return attributes.clone();
	}

	public boolean containsAttribute(Object key) {
		return attributes.containsKey(key);
	}

	public String get(Object key) {
		if(!containsAttribute(key))util.Logger.singleton().debug("Illegal get '"+key+"' called for "+this.getClass().toString());
		return attributes.get(key);
	}

	public Set<String> keySet() {
		return attributes.keySet();
	}

	public void put(String key, String value) {
		if(containsAttribute(key)) attributes.put(key, value);
		else util.Logger.singleton().debug("illegal put '"+key+"' called for "+this.getClass().toString());
	}

	public String toString() {
		return attributes.toString();
	}
	
	
	
}

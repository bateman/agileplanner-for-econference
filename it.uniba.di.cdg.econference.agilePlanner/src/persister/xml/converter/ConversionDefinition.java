package persister.xml.converter;

import java.util.Hashtable;

public interface ConversionDefinition {
	public Hashtable<String, String> attributes();
	public String [] element();
}

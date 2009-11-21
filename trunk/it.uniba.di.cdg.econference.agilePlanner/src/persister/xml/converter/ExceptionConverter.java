package persister.xml.converter;

import java.util.Hashtable;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExceptionConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
          writer.startNode("Exception");
    	  writer.addAttribute("exception", ((Exception)value).getMessage());
          writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return new Exception(reader.getAttribute("exception"));
	}

	public boolean canConvert(Class arg0) {
		for(Class c: arg0.getInterfaces()){
			if(c.equals(Exception.class))
				return true;
		}
		return false;
	}
}

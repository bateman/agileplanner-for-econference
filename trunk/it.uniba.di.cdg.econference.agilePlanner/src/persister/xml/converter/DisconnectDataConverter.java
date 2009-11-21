package persister.xml.converter;


import java.util.Hashtable;

import persister.data.impl.DisconnectDataObject;
import persister.Disconnect;
import persister.Message;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class DisconnectDataConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		Disconnect dd = (Disconnect)value;
		writer.startNode("DisconnectData");
		writer.addAttribute("clientid", String.valueOf(dd.getClientId()));
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Disconnect dd = new DisconnectDataObject();
		dd.setClientId(Integer.valueOf(reader.getAttribute("clientid")));
		return dd;
	}

	public boolean canConvert(Class clazz) {
		for (Class c : clazz.getInterfaces()) {
			if (c.equals(Disconnect.class))
				return true;
		}
		return false;
	}
	
}

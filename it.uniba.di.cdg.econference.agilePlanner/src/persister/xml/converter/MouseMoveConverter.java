package persister.xml.converter;


import java.util.Hashtable;

import persister.Event;
import persister.data.impl.EventDataObject;
import persister.Event;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MouseMoveConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Event move = (Event)value;
		writer.startNode("Event");
		writer.addAttribute("id", String.valueOf(move.getId()));
		writer.addAttribute("name", move.getName());
		writer.addAttribute("locationX", String.valueOf(move.getLocationX()));
		writer.addAttribute("locationY", String.valueOf(move.getLocationY()));
        writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context) {
		EventDataObject move = new EventDataObject();
		move.setId(Long.valueOf(reader.getAttribute("id")));
		move.setName(reader.getAttribute("name"));
		move.setLocationX(Integer.valueOf(reader.getAttribute("locationX")));
		move.setLocationY(Integer.valueOf(reader.getAttribute("locationY")));
		return move;
	}

	public boolean canConvert(Class clazz) {
		for (Class c : clazz.getInterfaces()) {
			if (c.equals(Event.class)){
				return true;
			}
		}
		return false;
	}

}

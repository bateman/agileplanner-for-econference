package persister.xml.converter;
 

import java.util.Hashtable;

import persister.IndexCardLiveUpdate;
import persister.LiveUpdate;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class IndexCardLiveUpdateConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		LiveUpdate update = (LiveUpdate) value;
        writer.startNode("LiveUpdate");
        writer.addAttribute("text", update.getText());
        writer.addAttribute("field", String.valueOf(update.getField()));
        writer.addAttribute("id", String.valueOf(update.getId()));
	    writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		IndexCardLiveUpdate update = new IndexCardLiveUpdate();
		update.setField(Integer.valueOf(reader.getAttribute("field")));
		update.setId(Long.valueOf(reader.getAttribute("id")));
		update.setText(reader.getAttribute("text"));
		return update;
	}

	public boolean canConvert(Class clazz) {
		return clazz.equals(IndexCardLiveUpdate.class);
	}

}

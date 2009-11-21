package persister.xml.converter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import persister.Message;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ProjectNameListConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer,
		MarshallingContext context) {
		List<String> str = (List<String>)value;
		writer.startNode("ProjectNameList");
		for(String project : str){
			writer.startNode("project");
			writer.addAttribute("name", project);
			writer.endNode();
		}
		writer.endNode();

	}

	public Object unmarshal(HierarchicalStreamReader reader,
		UnmarshallingContext context) {

		List<String> str= new ArrayList<String>();
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			str.add(reader.getAttribute("name"));
			reader.moveUp();
		}
		return str;
	}

	public boolean canConvert(Class clazz) {
		for (Class c : clazz.getInterfaces()) {
			if (c.equals(List.class)){
				return true;
			}
		}
		return false;
	}
	
	

}

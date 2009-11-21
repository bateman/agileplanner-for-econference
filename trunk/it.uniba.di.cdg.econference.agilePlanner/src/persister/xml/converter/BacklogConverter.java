package persister.xml.converter;

import persister.*;
import persister.data.Backlog;
import persister.data.StoryCard;
import persister.data.impl.*;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class BacklogConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		Backlog backlog = (Backlog) value;
		writer.startNode("Backlog");
		writer.addAttribute("Height", String.valueOf(backlog.getHeight()));
		writer.addAttribute("Width", String.valueOf(backlog.getWidth()));
		writer.addAttribute("ID", String.valueOf(backlog.getId()));
		writer.addAttribute("Name", backlog.getName());
		writer.addAttribute("Parent", String.valueOf(backlog.getParent()));
		writer.addAttribute("XLocation", String.valueOf(backlog.getLocationX()));
		writer.addAttribute("YLocation", String.valueOf(backlog.getLocationY()));

		if (backlog.getStoryCardChildren() != null) {
			if (backlog.getStoryCardChildren().size() > 0) {
				for (StoryCard sc : backlog.getStoryCardChildren()) {
					context.convertAnother(sc);
				}
			}
		}
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		Backlog backlog = new BacklogDataObject();
		backlog.setHeight(Integer.valueOf(reader.getAttribute("Height")));
		backlog.setId(Long.valueOf(reader.getAttribute("ID")));
		backlog.setName(reader.getAttribute("Name"));
		backlog.setParent(Long.valueOf(reader.getAttribute("Parent")));
		backlog.setWidth(Integer.valueOf(reader.getAttribute("Width")));
		backlog.setLocationX(Integer.valueOf(reader.getAttribute("XLocation")));
		backlog.setLocationY(Integer.valueOf(reader.getAttribute("YLocation")));

		while (reader.hasMoreChildren()) {
			reader.moveDown();
			if ("StoryCard".equals(reader.getNodeName())) {
				StoryCard sc = (StoryCard) context.convertAnother(backlog,
						StoryCardDataObject.class);
				backlog.addStoryCard(sc);
			}
			reader.moveUp();
		}
		return backlog;
	}

	public boolean canConvert(Class clazz) {
		for (Class c : clazz.getInterfaces()) {
			if (c.equals(Backlog.class)){
				return true;
			}
		}
		return false;
	}

}

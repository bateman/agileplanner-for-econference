package persister.xml.converter;

import java.sql.Timestamp;

import persister.*;
import persister.data.Iteration;
import persister.data.StoryCard;
import persister.data.impl.*;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class IterationConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		Iteration iteration = (Iteration)value;
		writer.startNode("Iteration");
		writer.addAttribute("AvailableEffort", String.valueOf(iteration.getAvailableEffort()));
		writer.addAttribute("Description", iteration.getDescription());
		writer.addAttribute("EndDate",String.valueOf(iteration.getEndDate()));
		writer.addAttribute("Height", String.valueOf(iteration.getHeight()));
		writer.addAttribute("ID", String.valueOf(iteration.getId()));
		writer.addAttribute("Name", iteration.getName());
		writer.addAttribute("Parent", String.valueOf(iteration.getParent()));
		writer.addAttribute("StartDate", String.valueOf(iteration.getStartDate()));
		writer.addAttribute("Status", iteration.getStatus());
		writer.addAttribute("Width", String.valueOf(iteration.getWidth()));
		writer.addAttribute("XLocation", String.valueOf(iteration.getLocationX()));
		writer.addAttribute("YLocation", String.valueOf(iteration.getLocationY()));
		
		if(iteration.getStoryCardChildren() != null){
			if(iteration.getStoryCardChildren().size() > 0){
				for(StoryCard sc : iteration.getStoryCardChildren()){
					context.convertAnother(sc);
				}
			}
		}
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		
		Iteration iter = new IterationDataObject();
		
		iter.setAvailableEffort(Float.valueOf(reader.getAttribute("AvailableEffort")));
		iter.setDescription(reader.getAttribute("Description"));
		iter.setEndDate(Timestamp.valueOf(reader.getAttribute("EndDate")));
		iter.setHeight(Integer.valueOf(reader.getAttribute("Height")));
		iter.setId(Long.valueOf(reader.getAttribute("ID")));
		iter.setLocationX(Integer.valueOf(reader.getAttribute("XLocation")));
		iter.setLocationY(Integer.valueOf(reader.getAttribute("YLocation")));
		iter.setName(reader.getAttribute("Name"));
		iter.setParent(Long.valueOf(reader.getAttribute("Parent")));
		iter.setStartDate(Timestamp.valueOf(reader.getAttribute("StartDate")));
		iter.setStatus(reader.getAttribute("Status"));
		iter.setWidth(Integer.valueOf(reader.getAttribute("Width")));
		
        while (reader.hasMoreChildren()) {
                reader.moveDown();
                if ("StoryCard".equals(reader.getNodeName())) {
                        StoryCard sc = (StoryCard)context.convertAnother(iter, StoryCardDataObject.class);
                        iter.addStoryCard(sc);
                }
                reader.moveUp();
        }
		return iter;
	}

	public boolean canConvert(Class clazz) {
		
		for(Class c : clazz.getInterfaces()){
			if(c.equals(Iteration.class)){
				return true;
			}
		}
		return false;
	}

}

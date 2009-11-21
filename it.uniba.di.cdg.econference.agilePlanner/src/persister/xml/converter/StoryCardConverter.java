package persister.xml.converter;


import java.util.ArrayList;

import persister.data.StoryCard;
import persister.data.impl.StoryCardDataObject;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;



public class StoryCardConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		StoryCard sc = (StoryCard)value;
		writer.startNode("StoryCard");
		writer.addAttribute("Actual", String.valueOf(sc.getActualEffort()));
		writer.addAttribute("BestCase", String.valueOf(sc.getBestCaseEstimate()));
		writer.addAttribute("CardOwner", sc.getCardOwner());
		writer.addAttribute("Color", sc.getColor());
		writer.addAttribute("CurrentSideUp", String.valueOf(sc.getCurrentSideUp()));
		writer.addAttribute("Description", sc.getDescription());
		writer.addAttribute("Height", String.valueOf(sc.getHeight()));
		if(sc.getHandwritingImage()!=null&&sc.getHandwritingImage().length>0)
		{ 
			writer.addAttribute("Handwriting", new String(sc.getHandwritingImage()));
		}
		writer.addAttribute("ID", String.valueOf(sc.getId()));
		writer.addAttribute("MostLikely", String.valueOf(sc.getMostlikelyEstimate()));
		writer.addAttribute("Name", sc.getName());
		writer.addAttribute("Parent", String.valueOf(sc.getParent()));
		writer.addAttribute("RotationAngle", String.valueOf(sc.getRotationAngle()));
		writer.addAttribute("Status", sc.getStatus());
		writer.addAttribute("TestText", sc.getAcceptanceTestText());
		writer.addAttribute("TestURL", sc.getAcceptanceTestUrl());
		writer.addAttribute("Width", String.valueOf(sc.getWidth()));
		writer.addAttribute("WorstCase", String.valueOf(sc.getWorstCaseEstimate()));
		writer.addAttribute("XLocation", String.valueOf(sc.getLocationX()));
		writer.addAttribute("YLocation", String.valueOf(sc.getLocationY()));
        writer.addAttribute("RallyID", String.valueOf(sc.getRallyID()));
        writer.addAttribute("FitID", sc.getFitId());
		writer.endNode();
 
		
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		StoryCard sc = new StoryCardDataObject();

		sc.setAcceptanceTestText(reader.getAttribute("TestText"));
		sc.setAcceptanceTestUrl(reader.getAttribute("TestURL"));
		sc.setActualEffort(Float.valueOf(reader.getAttribute("Actual")));
		sc.setBestCaseEstimate(Float.valueOf(reader.getAttribute("BestCase")));
		sc.setCardOwner(reader.getAttribute("CardOwner"));
		sc.setColor(reader.getAttribute("Color"));
		sc.setCurrentSideUp(Integer.valueOf(reader.getAttribute("CurrentSideUp")));
		sc.setDescription(reader.getAttribute("Description"));
		sc.setHeight(Integer.valueOf(reader.getAttribute("Height")));
		sc.setId(Long.valueOf(reader.getAttribute("ID")));
		sc.setLocationX(Integer.valueOf(reader.getAttribute("XLocation")));
		sc.setLocationY(Integer.valueOf(reader.getAttribute("YLocation")));
		sc.setMostlikelyEstimate(Float.valueOf(reader.getAttribute("MostLikely")));
		sc.setName(reader.getAttribute("Name"));
		sc.setParent(Long.valueOf(reader.getAttribute("Parent")));
		sc.setRotationAngle(Float.valueOf(reader.getAttribute("RotationAngle")));
		sc.setStatus(reader.getAttribute("Status"));
		sc.setWidth(Integer.valueOf(reader.getAttribute("Width")));
		sc.setWorstCaseEstimate(Float.valueOf(reader.getAttribute("WorstCase")));	
        sc.setRallyID(Boolean.valueOf(reader.getAttribute("RallyID")));
        if (reader.getAttributeCount() > 20) sc.setFitId(reader.getAttribute("FitID"));
		if(reader.getAttribute("Handwriting") != null && !reader.getAttribute("Handwriting").equals(""))
        {
        	sc.setHandwritingImage(reader.getAttribute("Handwriting").getBytes());
        }
		return sc;
	}

	public boolean canConvert(Class clazz) {

		for(Class c : clazz.getInterfaces()){
			if(c.equals(StoryCard.class)){
				return true;
			}
		}
		return false;
	}

}

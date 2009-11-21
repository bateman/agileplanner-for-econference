package persister.xml.converter;

import java.util.Hashtable;

import persister.*;
import persister.data.Legend;
import persister.data.impl.*;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class LegendConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		Legend legend = (Legend)value;
        writer.startNode("Legend");
		writer.addAttribute("aqua", legend.getAqua());
		writer.addAttribute("blue", legend.getBlue());
		writer.addAttribute("gray", legend.getGrey());
		writer.addAttribute("green", legend.getGreen());
		writer.addAttribute("khaki", legend.getKhaki());
		writer.addAttribute("peach", legend.getPeach());
		writer.addAttribute("pink", legend.getPink());
		writer.addAttribute("red", legend.getRed());
		writer.addAttribute("white", legend.getWhite());
		writer.addAttribute("yellow", legend.getYellow());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		
		Legend legend = new LegendDataObject();
		
		legend.setAqua(reader.getAttribute("aqua"));
		legend.setBlue(reader.getAttribute("blue"));
		legend.setGrey(reader.getAttribute("gray"));
		legend.setGreen(reader.getAttribute("green"));
		legend.setKhaki(reader.getAttribute("khaki"));
		legend.setPeach(reader.getAttribute("peach"));
		legend.setPink(reader.getAttribute("pink"));
		legend.setRed(reader.getAttribute("red"));
		legend.setWhite(reader.getAttribute("white"));
		legend.setYellow(reader.getAttribute("yellow"));
		
		return legend;
	}

	public boolean canConvert(Class clazz) {
		
		for(Class c : clazz.getInterfaces()){
			if(c.equals(Legend.class)){
				return true;
			}
		}
		return false;
	}
	
	public Hashtable<String,String> attributes() {
		return new Hashtable<String, String>(){
			{
				put("aqua", "Aqua");
				put("blue", "Blue");
				put("red", "Red");
				put("green", "Green");
				put("khaki", "Khaki");
				put("peach", "Peach");
				put("pink", "Pink");
				put("white", "White");
				put("yellow", "Yellow");
				put("gray", "Grey");
			}
		};
	}

	public String[] element() {
		return new String [] {"Legend","Legend"};
	}

}

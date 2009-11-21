package persister.xml.converter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

import persister.data.Iteration;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.IterationDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.data.impl.TeamMemberDataObject;



import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class OwnerConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
		TeamMember teamMember = (TeamMember)value;
		writer.startNode("USERS");
		writer.addAttribute("Name", teamMember.getName());
		writer.endNode();
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
           TeamMember teamMember = new TeamMemberDataObject();
	     teamMember.setName(reader.getAttribute("Name"));
		return teamMember;
	}

	public boolean canConvert(Class clazz) {
		for(Class c : clazz.getInterfaces()){
			if(c.equals(TeamMember.class)){
				return true;
			}
		}
		return false;
	}

}

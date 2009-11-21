package persister.xml.converter;

import java.util.ArrayList;
import java.util.HashMap;

import persister.IndexCardLiveUpdate;
import persister.Message;
import persister.Event;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.DisconnectDataObject;
import persister.data.impl.EventDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.LegendDataObject;
import persister.data.impl.MessageDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.data.impl.TeamMemberDataObject;



import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MessageConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Message msg = (Message) value;

		writer.addAttribute("sender", String.valueOf(msg.getSender()));
		writer.addAttribute("mtype", String.valueOf(msg.getMessageType()));
		writer.addAttribute("etype", String.valueOf(msg.getEtype()));

		if (msg.getMessage()!= null) {
			if(msg.getMessage()instanceof EventDataObject){
				context.convertAnother(msg.getMessage(),new MouseMoveConverter());
			}else if(msg.getMessage() instanceof StoryCardDataObject){
				context.convertAnother(msg.getMessage(), new StoryCardConverter());
			}else if(msg.getMessage() instanceof TeamMemberDataObject){
				context.convertAnother(msg.getMessage(), new OwnerConverter());
			}else if(msg.getMessage() instanceof LegendDataObject){
				context.convertAnother(msg.getMessage(), new LegendConverter());
			}else if(msg.getMessage() instanceof IterationDataObject){
				context.convertAnother(msg.getMessage(), new IterationConverter());
			}else if (msg.getMessage() instanceof DisconnectDataObject) {
				context.convertAnother(msg.getMessage(), new DisconnectDataConverter());
			}else if(msg.getMessage() instanceof Long || msg.getMessage() instanceof String){
				writer.startNode("ElementTypeBody");
				writer.addAttribute("Body", String.valueOf(msg.getMessage()));
				writer.endNode();
			}else if(msg.getMessage() instanceof ProjectDataObject){
				writer.startNode("Project");
				context.convertAnother(msg.getMessage());
				writer.endNode();
			}else {
				context.convertAnother(msg.getMessage());
			}
		}

		if (msg.getData() != null) {
			writer.startNode("Data");

			for (String key : msg.getData().keySet()) {
				writer.startNode("Entry");
				writer.addAttribute("key", key);
				writer.addAttribute("value", msg.getData().get(key));
				writer.endNode();
			}

			writer.endNode();
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		MessageDataObject msg = new MessageDataObject(0);

		msg.setMessageType(Integer.valueOf(reader.getAttribute("mtype")));
		msg.setSender(Long.valueOf(reader.getAttribute("sender")));
        msg.setEtype(Integer.valueOf(reader.getAttribute("etype")));
		while (reader.hasMoreChildren()) {
			reader.moveDown();
			HashMap<String, String> data = new HashMap<String, String>();
			if ("Event".equals(reader.getNodeName())){
				msg.setMessage(context.convertAnother(msg, EventDataObject.class));
			}else if ("StoryCard".equals(reader.getNodeName())) {
				msg.setMessage(context.convertAnother(msg, StoryCardDataObject.class));
			}else if ("TeamMember".equals(reader.getNodeName()) || "USERS".equals(reader.getNodeName())) {
				msg.setMessage(context.convertAnother(msg, TeamMemberDataObject.class));
			}else if ("Iteration".equals(reader.getNodeName())) {
				msg.setMessage(context.convertAnother(msg, IterationDataObject.class));
			}else if ("Backlog".equals(reader.getNodeName())) {
				msg.setMessage(context.convertAnother(msg, BacklogDataObject.class));
			}else if("Legend".equals(reader.getNodeName())){
				msg.setMessage(context.convertAnother(msg, LegendDataObject.class));
			}else if ("Project".equals(reader.getNodeName())) {
				msg.setMessage(context.convertAnother(msg, ProjectDataObject.class));
			}else if ("DisconnectData".equals(reader.getNodeName())) {
				msg.setMessage(context.convertAnother(msg,DisconnectDataObject.class));
			} else if ("Data".equals(reader.getNodeName())) {
				while (reader.hasMoreChildren()) {
					reader.moveDown();
					data.put(reader.getAttribute("key"), reader
							.getAttribute("value"));
					reader.moveUp();
				}
				msg.setData(data);
			}else if ("ElementTypeBody".equals(reader.getNodeName())){
				msg.setMessage(reader.getAttribute("Body"));
			}else if ("Exception".equals(reader.getNodeName())){
				msg.setMessage(context.convertAnother(msg, Exception.class));
			}else if("ProjectNameList".equals(reader.getNodeName())){
				msg.setMessage(context.convertAnother(msg, ArrayList.class));
			}else if("LiveUpdate".equals(reader.getNodeName())){
				msg.setMessage(context.convertAnother(msg, IndexCardLiveUpdate.class));
			}else if("DownloadFile".equals(reader.getNodeName())){
				msg.setMessage(context.convertAnother(msg, String[].class));
			}
			reader.moveUp();
		}
 
		return msg;
	}

	public boolean canConvert(Class clazz) {
		for (Class c : clazz.getInterfaces()) {
			if (c.equals(Message.class))
				return true;
		}
		return false;
	}

}

package persister.xml.converter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import persister.IndexCardLiveUpdate;
import persister.Event;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.DisconnectDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.LegendDataObject;
import persister.data.impl.MessageDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.data.impl.TeamMemberDataObject;

import com.thoughtworks.xstream.XStream;

public class Converter {
	private static XStream xstream = null;
	
	private static void init(){
		if(xstream == null){
			xstream = new XStream();
			xstream.alias("Message", MessageDataObject.class);
			xstream.alias("Project", ProjectDataObject.class);
			xstream.alias("StoryCard", StoryCardDataObject.class);
			xstream.alias("Legend", LegendDataObject.class);
			xstream.alias("TeamMember",TeamMemberDataObject.class);
			xstream.alias("Iteration", IterationDataObject.class);
			xstream.alias("Backlog", BacklogDataObject.class);
			xstream.alias("Exception", Exception.class);
			xstream.alias("LiveUpdate", IndexCardLiveUpdate.class);
			xstream.alias("Event", Event.class);
			xstream.alias("ProjectNameList", ArrayList.class);
			xstream.alias("DisconnectData", DisconnectDataObject.class);
			xstream.registerConverter(new MessageConverter());
			xstream.registerConverter(new ProjectConverter());
			xstream.registerConverter(new IterationConverter());
			xstream.registerConverter(new BacklogConverter());
			xstream.registerConverter(new StoryCardConverter());
			xstream.registerConverter(new LegendConverter());
			xstream.registerConverter(new OwnerConverter());
			xstream.registerConverter(new ExceptionConverter());
			xstream.registerConverter(new IndexCardLiveUpdateConverter());
	    	xstream.registerConverter(new MouseMoveConverter());
			xstream.registerConverter(new ProjectNameListConverter());
			xstream.registerConverter(new DisconnectDataConverter());
		}
	}
	
	public static String toXML(Object obj){
		init();
		return xstream.toXML(obj);
	}
	
	public static void toXML(Object obj, OutputStream out){
		init();
		xstream.toXML(obj, out);
	}
	
	public static Object fromXML(String xml){
		init();
		return xstream.fromXML(xml);
	}
	
	public static Object fromXML(InputStream xml){
		init();
		return xstream.fromXML(xml);
	}
}

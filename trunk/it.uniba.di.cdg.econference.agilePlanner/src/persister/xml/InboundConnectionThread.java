package persister.xml;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;

import persister.IndexCardLiveUpdate;
import persister.Message;
import persister.Event;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.LegendDataObject;
import persister.data.impl.MessageDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.data.impl.TeamMemberDataObject;
import persister.xml.converter.BacklogConverter;
import persister.xml.converter.Converter;
import persister.xml.converter.ExceptionConverter;
import persister.xml.converter.IndexCardLiveUpdateConverter;
import persister.xml.converter.IterationConverter;
import persister.xml.converter.LegendConverter;
import persister.xml.converter.MessageConverter;
import persister.xml.converter.MouseMoveConverter;
import persister.xml.converter.OwnerConverter;
import persister.xml.converter.ProjectConverter;
import persister.xml.converter.ProjectNameListConverter;
import persister.xml.converter.StoryCardConverter;


import com.thoughtworks.xstream.XStream;

public class InboundConnectionThread extends Thread {

	private InputStream in;
	private String xml = "";
	private SocketConnectionEndpoint endpoint;
	private String preamble = String.valueOf(new char[] {((char)239), ((char)187), ((char)191)});
	private OutboundConnectionThread parentThread;
	private DataInputStream reader;
	private boolean runOver = false;

	public InboundConnectionThread(InputStream in,
			SocketConnectionEndpoint endpoint,
			OutboundConnectionThread parentThread) {

		this.in = in;
		this.endpoint = endpoint;
		reader = new DataInputStream(in);
		this.parentThread = parentThread;
		this.start();
	}

	public void run() {
		
			while (!runOver) {

				try {
                    xml = reader.readUTF();
					Object obj = Converter.fromXML(xml);
					endpoint.receive((Message) obj,this.parentThread.getConnectionID());
				} catch (SocketException e) {
					util.Logger.singleton().error(e);
					try {
						this.parentThread.dispose();
					} catch (IOException e1) {
					
						util.Logger.singleton().error(e1);
					}
					
				}catch (IOException e2)
				{
				}
				
			}

	}
    public void close(){
    }

	public void setRunOver(boolean runOver) {
		this.runOver = runOver;
	}
}
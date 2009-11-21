package persister.xml;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import persister.xml.InboundConnectionThread;
import persister.xml.SocketConnectionEndpoint;
import persister.xml.converter.BacklogConverter;
import persister.xml.converter.Converter;
import persister.xml.converter.DownLoadXMLConverter;
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


public class OutboundConnectionThread extends Thread {

	private Socket socket;
	private SocketConnectionEndpoint endpoint;
	private InputStream in;
	private OutputStream out;
	private InboundConnectionThread listener;
	private DataOutputStream writer ;
	private int connectionID=0;
	public OutboundConnectionThread(Socket socket, SocketConnectionEndpoint endpoint) {
		this(socket, 0, endpoint);
	}
 
	public OutboundConnectionThread(Socket socket, int connectionID, SocketConnectionEndpoint endpoint) {
		this.socket = socket;
		this.endpoint = endpoint;
		this.connectionID = connectionID;

		try {

			this.out = socket.getOutputStream();
			this.in = socket.getInputStream();
			this.writer = new DataOutputStream(out);

		} catch (IOException e) {
			
		}

		this.start();
	}

	public void run() {
		listener = new InboundConnectionThread(in, endpoint, this);
	}

	public void send(Message msg) throws Exception {
		String xml = Converter.toXML(msg);
		writer.writeUTF(xml);
		writer.flush();
	}

	public void dispose() throws IOException {
		this.listener.setRunOver(true);
		this.in.close();
		this.out.close();
	}
	
	
    public int getConnectionID() {
        return this.connectionID;
    }

    public String getNetworkAddress() {
        return this.socket.getInetAddress().getCanonicalHostName();
    }
}

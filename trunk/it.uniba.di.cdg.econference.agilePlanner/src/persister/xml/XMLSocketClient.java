package persister.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import persister.Message;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.LegendDataObject;
import persister.data.impl.MessageDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.distributed.ClientCallbackCommunicator;
import persister.xml.OutboundConnectionThread;
import persister.xml.SocketConnectionEndpoint;
import persister.xml.converter.BacklogConverter;
import persister.xml.converter.IterationConverter;
import persister.xml.converter.LegendConverter;
import persister.xml.converter.MessageConverter;
import persister.xml.converter.ProjectConverter;
import persister.xml.converter.StoryCardConverter;



import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.StreamException;

public class XMLSocketClient implements SocketConnectionEndpoint {

	private Socket socket;
	private OutboundConnectionThread serverConnection;
	private ClientCallbackCommunicator comm;

	private InetAddress addr;
	private int port;
	
	public XMLSocketClient(String host, int port, ClientCallbackCommunicator comm ) {
		try {
			this.comm = comm;
			host = stripProtocol(host);
			addr = InetAddress.getByName(host);
			this.port = port;
			
			connect();
		}catch(Exception e){
			util.Logger.singleton().error(e);
		}
	}
	
	public void connect() throws Exception{
		this.socket = new Socket(addr, this.port);
		serverConnection = new OutboundConnectionThread(socket, this);
	}

	public void send(Message msg)  {
		try {
			this.serverConnection.send(msg);
		} catch (Exception e) {
			util.Logger.singleton().error(e);
		}
	}

	public void receive(Message msg, int connectionID) {
		this.comm.receiveMessage(msg);
	}
	
	public void breakConnection()
	{
		try {
			serverConnection.dispose();
			this.socket.close();
		} catch (Exception e) {
			util.Logger.singleton().error(e);
		}
	}
	
	private String stripProtocol(String host){
		return host.replaceFirst("^.*:\\/\\/", "");
	}
	

}

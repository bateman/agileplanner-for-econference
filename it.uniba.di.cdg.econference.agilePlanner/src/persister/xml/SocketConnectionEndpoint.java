package persister.xml;

import persister.Message;

public interface SocketConnectionEndpoint {
	
	public void send(Message msg);
	
	public void receive(Message msg, int connectionID);

}

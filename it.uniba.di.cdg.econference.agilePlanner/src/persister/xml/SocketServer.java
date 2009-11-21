package persister.xml;

import persister.Message;

public interface SocketServer {
	
	public void kill();
	
	public void killClient(int id);
	
	public void sendToSingleClient(Message msg, int id);
	
	public void sendToAllButOneClient(Message msg, int idToExclude);

}

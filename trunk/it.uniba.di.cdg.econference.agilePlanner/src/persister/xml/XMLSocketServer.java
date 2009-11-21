package persister.xml;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import persister.Message;
import persister.data.impl.DisconnectDataObject;
import persister.data.impl.MessageDataObject;
import persister.distributed.ServerCallbackCommunicator;
import persister.xml.OutboundConnectionThread;
import persister.xml.SocketConnectionEndpoint;
import persister.xml.SocketServer;



public class XMLSocketServer extends Thread implements SocketConnectionEndpoint, SocketServer {
    private ServerCallbackCommunicator comm = null;
	private ServerSocket serverSocket;
	private int lastID = 1;
	private List<OutboundConnectionThread> clients;

	public XMLSocketServer(int port, ServerCallbackCommunicator parent_communicator) {
		try {
			this.serverSocket = new ServerSocket(port);
			clients = new ArrayList<OutboundConnectionThread>();
            this.comm = parent_communicator;

			this.start();

		} catch (UnknownHostException e) {
			util.Logger.singleton().error(e);
		} catch (IOException e) {
			util.Logger.singleton().error(e);
		}
	}

	/* Thread */

	public void run() {
		listen();
	}

	public void listen() {
		while (true) {
			try {				
				clients.add(new OutboundConnectionThread(serverSocket.accept(), genClientID(), this));
			} catch (SocketException e) {
				util.Logger.singleton().error(e);
				break;
			} catch (Exception e) {
				util.Logger.singleton().error(e);
			}
		}
	}

	/* SocketConnectionEndpoint */

	public void receive(Message msg, int connectionID) {
		this.comm.receiveMessage(msg, connectionID);
	}
	
	public void send(Message msg) {
		List<OutboundConnectionThread> badClients = new ArrayList<OutboundConnectionThread>();
		synchronized (this) {
			for (OutboundConnectionThread client : clients) {
				try {
					client.send(msg);
				} catch (Exception e) {
					util.Logger.singleton().error(e);
					try {
						client.dispose();
					} catch (Exception e1) {
						util.Logger.singleton().error(e1);
					}
					badClients.add(client);
				}
			}
		}

		purgeBadClients(badClients);
	}

	/* Server */

	public void sendToSingleClient(Message msg, int id) {

		List<OutboundConnectionThread> badClients = new ArrayList<OutboundConnectionThread>();

		synchronized (this) {

			for (OutboundConnectionThread client : clients) {
				if (client.getConnectionID() == id) {
					try {
						client.send(msg);

					} catch (Exception e) {
						try {
							client.dispose();
						} catch (Exception e1) {
							util.Logger.singleton().error(e1);
						}
						util.Logger.singleton().error(e);
						badClients.add(client);
					}
					break;
				}
			}
		}

		purgeBadClients(badClients);
	}

	public void sendToAllButOneClient(Message msg, int idToExclude) {

		List<OutboundConnectionThread> badClients = new ArrayList<OutboundConnectionThread>();
		synchronized (this) {
			for (OutboundConnectionThread client : clients) {
				if (client.getConnectionID() != idToExclude) {
					try {
						client.send(msg);
					} catch (Exception e) {
						try {
							client.dispose();
						} catch (Exception e1) {
							util.Logger.singleton().error(e);
						}
						badClients.add(client);
					}
				}
			}
		}

		purgeBadClients(badClients);
	}
	
	public void killClient(int id) {

		
		for (OutboundConnectionThread client : clients) {
			if (client.getConnectionID() == id) {
				try {
					client.dispose();

				} catch (Exception e) {
					util.Logger.singleton().error(e);
				}
				clients.remove(client);
				
				
			}
		}
	}
	
	public void kill() {
		try {
			for (Iterator<OutboundConnectionThread> iter = clients.iterator(); iter
					.hasNext();) {
				OutboundConnectionThread element = iter.next();
				
					element.dispose();
				
			}
			clients.clear();
			this.serverSocket.close();
		} catch (IOException e) {
			util.Logger.singleton().error(e);
		}
	}

	/* Private Helper */

	private void purgeBadClients(List<OutboundConnectionThread> badClients) {

		for (OutboundConnectionThread client : badClients) {
			System.out
					.println("A closed client connection has been removed...["
							+ client.getNetworkAddress() + ": id="
							+ client.getConnectionID());
			
			
			this.clients.remove(client);
			sendDisconnectMessageForClient(client.getConnectionID());
		}
	}

	private int genClientID() {
		return ++lastID;
	}

	private void sendDisconnectMessageForClient(int id) {
		Message msg = new MessageDataObject(Message.DISCONNECT);
		DisconnectDataObject data = new DisconnectDataObject();
		data.setClientId(id);
		msg.setMessage(data);
		msg.setSender(id);
		send(msg);
	}



}

package persister;

//public interface AgilePlannerClient extends Remote {
public interface AgilePlannerClient {

    /***************************************************************************
     * CONNECTION *
     **************************************************************************/
    public void receiveMessage(Message message);// throws RemoteException;
    public long getClientId();// throws RemoteException;
    public void setClientId(long id);// throws RemoteException;
    public boolean disconnect();// throws RemoteException;

}

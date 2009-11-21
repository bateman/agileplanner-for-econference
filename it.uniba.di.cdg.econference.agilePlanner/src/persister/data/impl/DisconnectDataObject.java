package persister.data.impl;


import persister.Disconnect;
/**
 * These objects are included in the transfer network object when a client is
 * found to have disconnected.
 * 
 * @author patrickwilson
 * 
 */
public class DisconnectDataObject implements Disconnect {
    private int clientId;

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

}

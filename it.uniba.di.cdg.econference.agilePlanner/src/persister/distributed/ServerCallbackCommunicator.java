package persister.distributed;

import persister.Message;

public interface ServerCallbackCommunicator {
    public void receiveMessage(Message message, int clientId);
}

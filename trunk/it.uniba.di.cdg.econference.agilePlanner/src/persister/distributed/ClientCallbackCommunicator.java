package persister.distributed;

import persister.Message;

public interface ClientCallbackCommunicator {
    public void receiveMessage(Message message);
}

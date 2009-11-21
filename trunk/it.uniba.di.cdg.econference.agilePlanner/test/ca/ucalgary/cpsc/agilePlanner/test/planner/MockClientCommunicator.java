package ca.ucalgary.cpsc.agilePlanner.test.planner;

import persister.Message;
import persister.distributed.ClientCallbackCommunicator;

public class MockClientCommunicator implements ClientCallbackCommunicator {

    private Message msg;

    public void receiveMessage(Message msg) {
        this.msg = msg;
    }

    public Message messageReceived() {
        return msg;
    }

}

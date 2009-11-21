package ca.ucalgary.cpsc.agilePlanner.test.planner;

import persister.Message;
import persister.distributed.ServerCommunicator;

public class MockServerCommunicator extends ServerCommunicator {

    private Message msg;

    public MockServerCommunicator(String projectDirectory, String projectName, int port) throws Exception {
        super(projectDirectory, projectName, port, "","");
    }

    @Override
    public void receiveMessage(Message msg, int id) {
        this.msg = msg;
    }
    
    public Message messageReceived() {
        return msg;
    }

}

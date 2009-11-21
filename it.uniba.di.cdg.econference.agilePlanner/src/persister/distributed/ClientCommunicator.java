package persister.distributed;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import persister.AsynchronousPersister;
import persister.ConnectionFailedException;
import persister.ForbiddenOperationException;
import persister.IndexCardLiveUpdate;
import persister.IndexCardNotFoundException;
import persister.Keystroke;
import persister.Message;
import persister.NotConnectedException;
import persister.PlannerDataChangeListener;
import persister.PlannerUIChangeListener;
import persister.UIEventPropagator;
import persister.data.Backlog;
import persister.data.IndexCardWithChildren;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.EventDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.MessageDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.distributed.CallbackPropagator;
import persister.distributed.ClientCommunicator;
import persister.factory.Settings;
import persister.xml.XMLSocketClient;



public class ClientCommunicator implements AsynchronousPersister, UIEventPropagator {

    private static final long serialVersionUID = -6487839317162366027L;
    private static final int NUM_SKIP_MOUSE_MOVES = 5;
    private CallbackPropagator callbackPropagator;
    private long clientid = 0;
    private long uiEventClientID = 0;
    private int mmcounter = 0;
    private Timestamp start = new Timestamp(0);
    private Timestamp end = new Timestamp(Long.MAX_VALUE);
    private XMLSocketClient socket;
    public static boolean isFirstTimeConnect = false;
    private static String currentProjectName;
    
    public ClientCommunicator(String url, int port) throws IOException {
        callbackPropagator = new CallbackPropagator(clientid);
        assert (callbackPropagator != null);
        if (url != null){
            socket = new XMLSocketClient(url, port, callbackPropagator);
        }
    }

    public CallbackPropagator getCallbackPropogator() {
        return this.callbackPropagator;
    }

    /***************************************************************************
     * CONNECTION *
     **************************************************************************/

    public void connect() throws ConnectionFailedException {
    	MessageDataObject msg;
    	if(isFirstTimeConnect){
    		 msg = new MessageDataObject(Message.CONNECT);
    	}else{
    		String projectName = Settings.getProjectName();
    		msg = new MessageDataObject(Message.CONNECT, projectName);
    	}
    	isFirstTimeConnect = false;
    	try{
   			socket.send(msg);
    	}catch(Throwable e){
    	}
    }

    public boolean connected() {
        if (this.socket == null) {
            return false;
        }
        else {
            return true;
        }

    }

    public boolean disconnect() {
        try {
            socket.breakConnection();
            return true;
        }
        catch (Exception e) {
        	util.Logger.singleton().error(e);
            return false;
        }

    }

    /***************************************************************************
     * LOAD *
     **************************************************************************/

    public void getProjectNames() throws NotConnectedException {
        if (connected()) {
            callbackPropagator.setGetProjectNamesRequested(true);
            Message message = new MessageDataObject(this.clientid, Message.GET_PROJECT_NAMES);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    //Seems unused
    public void load(String projectName) throws NotConnectedException {
        if (connected()) {
            callbackPropagator.setLoadRequested(true);
            MessageDataObject message = new MessageDataObject(this.clientid, Message.LOAD, projectName);
            
            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

	public void load(String projectName, Timestamp start, Timestamp end) throws NotConnectedException {
        if (connected()) {
            Object[] msg = { projectName, start, end };
            MessageDataObject message = new MessageDataObject(this.clientid, Message.TIMEBOX_LOAD, msg);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    /***************************************************************************
     * UPLOAD XML *
     **************************************************************************/

    public void uploadFile(String fileName,int recognizeID) throws NotConnectedException {
        if (connected()) {
            callbackPropagator.setUploadRequested(true);
            MessageDataObject message = new MessageDataObject(this.clientid, Message.UPLOAD, readFileToUpload(fileName,recognizeID));

            sendMessage(message);

        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    private  Object[] readFileToUpload(String fileName,int storycardID) {
        Object[] fileStr = new Object[3];
        fileStr[0] = null;
        fileStr[1] = null;
        fileStr[2] = storycardID;

        File file = new File(fileName);
        String currentFileName = file.getName();
        try {
        	FileInputStream readFile = new FileInputStream(file);
            byte[] originalSpace = new byte[readFile.available()];
            readFile.read(originalSpace);
            fileStr[0] = currentFileName;
            fileStr[1] = originalSpace;
            }catch (IOException e) {
        }
       
 
        
        return fileStr;
    }

    /***************************************************************************
     * DOWNLOAD XML *
     **************************************************************************/

    public void downloadFile(String path, int recognizeID) throws NotConnectedException {
        if (connected()) {
            callbackPropagator.setDownloadRequested(true);
            String[] sentFile = new String[2];
            sentFile[0] = path;
            sentFile[1] = String.valueOf(recognizeID);
            MessageDataObject message = new MessageDataObject(this.clientid, Message.DOWNLOAD, sentFile);

            sendMessage(message);

        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    /***************************************************************************
     * CREATE *
     **************************************************************************/

    public void createBacklog(int width, int height, int locationX, int locationY) throws NotConnectedException, ForbiddenOperationException {

        if (connected()) {
            BacklogDataObject backlog = new BacklogDataObject(width, height, locationX, locationY);

            MessageDataObject message = new MessageDataObject(this.clientid, Message.CREATE_BACKLOG, backlog);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }

    }

    public void createIteration(String name, String description, int width, int height, int locationX, int locationY, float availableEffort,
            Timestamp startDate, Timestamp endDate, float rotationAngle,boolean rallyID) throws NotConnectedException {

        if (connected()) {
            IndexCardWithChildren iteration = new IterationDataObject(name, description, width, height, locationX, locationY, availableEffort,
                    startDate, endDate, rotationAngle,rallyID);

            MessageDataObject message = new MessageDataObject(this.clientid, Message.CREATE_ITERATION, iteration);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }

    }

	public void changeProject(String name) throws NotConnectedException {

        if (connected()) {
            ProjectDataObject project = new ProjectDataObject(name);

            MessageDataObject message = new MessageDataObject(this.clientid, Message.CREATE_PROJECT, project);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }

    }
    
    public void createProject(String name)throws  NotConnectedException {

        if (connected()) {
            ProjectDataObject project = new ProjectDataObject(name);

            MessageDataObject message = new MessageDataObject(this.clientid, Message.CREATE_PROJECT, project);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }

    }

    public void createStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, String cardOwner, float rotationAngle,boolean rallyID, String fitId)
            throws NotConnectedException, IndexCardNotFoundException {

        if (connected()) {
            StoryCardDataObject storycard = new StoryCardDataObject(parentid, name, description, width, height, locationX, locationY,
                    bestCaseEstimate, mostlikelyEstimate, worstCaseEstimate, actualEffort, status,color,  cardOwner, rotationAngle,rallyID, fitId);

            MessageDataObject message = new MessageDataObject(this.clientid, Message.CREATE_STORYCARD, storycard);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }
    

    /***************************************************************************
     * DELETE *
     **************************************************************************/

    public void deleteBacklog(long id) throws NotConnectedException, ForbiddenOperationException {
        throw new ForbiddenOperationException("You cannot delete the Backlog without deleting the Project!");
    }

    public void deleteIteration(long id) throws NotConnectedException, IndexCardNotFoundException {
        if (connected()) {

            MessageDataObject message = new MessageDataObject(this.clientid, Message.DELETE_CARD, id);
            sendMessage(message);
            System.out.println(message.toString());
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    public void deleteStoryCard(long id) throws NotConnectedException, IndexCardNotFoundException {
        if (connected()) {

            MessageDataObject message = new MessageDataObject(this.clientid, Message.DELETE_CARD, id);
            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }

    }
    public void deleteOwner(TeamMember teamMember) throws NotConnectedException{
        if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.DELETE_OWNER, teamMember);
            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }

    }

    /***************************************************************************
     * UNDELETE *
     **************************************************************************/

    public void undeleteIteration(Iteration iteration) throws NotConnectedException, IndexCardNotFoundException {

        if (connected()) {

            MessageDataObject message = new MessageDataObject(this.clientid, Message.UNDELETE_CARD, iteration);
            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    public void undeleteStoryCard(StoryCard storyCard) throws NotConnectedException, IndexCardNotFoundException {

        if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.UNDELETE_CARD, storyCard);
            sendMessage(message);

        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    /***************************************************************************
     * USER INTERFACE EVENT PROPGATION *
     **************************************************************************/

    public void moveMouse(String name, int x, int y) throws NotConnectedException {
        if (this.mmcounter == NUM_SKIP_MOUSE_MOVES) {
            this.mmcounter = 0;
            if (connected()) {
                EventDataObject mm = new EventDataObject(this.clientid, name, x, y);
                MessageDataObject message = new MessageDataObject(this.uiEventClientID, Message.MOUSE_MOVE, mm);
                sendMessage(message);
            }
            else {
                throw new NotConnectedException("Server unreachable!");
            }
        }
        else {
            this.mmcounter++;
        }
    }

    public void bringToFront(long id) throws NotConnectedException {
        if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.BRING_TO_FRONT, id);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    public void deleteRemoteMouse(long id) throws NotConnectedException {
        if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.DELETE_REMOTE_MOUSE, id);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    /***************************************************************************
     * UPDATE BACKLOG *
     **************************************************************************/

    public void updateBacklog(Backlog backlog) throws NotConnectedException, IndexCardNotFoundException {
        if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.UPDATE_CARD, backlog);
            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }
    
	public void updateLegend(Legend legend)throws NotConnectedException {
		if(connected()){
			MessageDataObject message = new MessageDataObject(this.clientid, Message.UPDATE_LEGEND, legend);
			sendMessage(message);
		}
		else {
            throw new NotConnectedException("Server unreachable!");
        }
	}
	public void updateOwner(TeamMember teamMember) throws NotConnectedException {
		if(connected()){
			MessageDataObject message = new MessageDataObject(this.clientid, Message.UPDATE_OWNER, teamMember);
			sendMessage(message);
		}
		else {
            throw new NotConnectedException("Server unreachable!");
        }
	}

    /***************************************************************************
     * UPDATE ITERATION *
     **************************************************************************/

    public void updateIteration(Iteration iteration) throws NotConnectedException, IndexCardNotFoundException {
        if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.UPDATE_CARD, iteration);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    /***************************************************************************
     * UPDATE STORYCARD *
     **************************************************************************/

    public void updateStoryCard(StoryCard sc) throws NotConnectedException, IndexCardNotFoundException {
        if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.UPDATE_CARD, sc);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    /***************************************************************************
     * MOVE STORYCARD BETWEEN PARENTS *
     **************************************************************************/

    public void moveStoryCardToNewParent(StoryCard sc, long newparentid, int locationX, int locationY,float rotation) throws IndexCardNotFoundException,
            NotConnectedException {
        if (connected()) {

            HashMap<String, String> additionalData = new HashMap();
            additionalData.put("newparentid", String.valueOf(newparentid));
            additionalData.put("locationX", String.valueOf(locationX));
            additionalData.put("locationY", String.valueOf(locationY));
            additionalData.put("rotation", String.valueOf(rotation));

            MessageDataObject message = new MessageDataObject(this.clientid, Message.MOVE_STORYCARD_TO_NEW_PARENT, sc, additionalData);

            sendMessage(message);
        }
        else {
            throw new NotConnectedException("Server unreachable!");
        }
    }

    /***************************************************************************
     * PLANNERDATACHANGELISTENER *
     **************************************************************************/

    public void addPlannerDataChangeListener(PlannerDataChangeListener listener) {
        this.callbackPropagator.addPlannerDataChangeListener(listener);
    }

    public void removePlannerDataChangeListener(PlannerDataChangeListener listener) {
        this.callbackPropagator.removePlannerDataChangeListener(listener);
    }

    /***************************************************************************
     * PLANNERUICHANGELISTENER *
     **************************************************************************/

    public void addPlannerUIChangeListener(PlannerUIChangeListener listener) {
        this.callbackPropagator.addPlannerUIChangeListener(listener);
    }

    public void removePlannerUIChangeListener(PlannerUIChangeListener listener) {
        this.callbackPropagator.removePlannerUIChangeListener(listener);
    }

    /***************************************************************************
     * SETTER & GETTER *
     **************************************************************************/

    public long getClientId() {
        return this.clientid;
    }

    public void setClientId(long id) {
        this.clientid = id;
    }

    public void sendMessage(Message msg) {
            socket.send(msg);
    }

    public void sendLiveTextUpdate(IndexCardLiveUpdate data) {
        Message msg = new MessageDataObject(Message.KEYSTROKE);
        msg.setMessage(data);
            socket.send(msg);
    }

	public void updateProject(Project project) {
	}

	public void arrangeProject(Project project) {
		 if (connected()) {
	            MessageDataObject message = new MessageDataObject(this.clientid, Message.ARRANGE_PROJECT, project);

	            sendMessage(message);
	            System.out.println("arrenge message has been sent");
	        }
	        else {
	        	util.Logger.singleton().error(new NotConnectedException("Server unreachable!"));
	        }
		
	}

	public void synchronizedProject() {
		if (connected()) {
            MessageDataObject message = new MessageDataObject(this.clientid, Message.SYNCH_PROJECT);

            sendMessage(message);
        }
        else {
        	util.Logger.singleton().error(new NotConnectedException("Server unreachable!"));
		}	
	}

	public static String getCurrentProjectName() {
		return currentProjectName;
	}

	public static void setCurrentProjectName(String currentProjectName) {
		ClientCommunicator.currentProjectName = currentProjectName;
	}

	public void login(String userName, String password, String url) throws NotConnectedException {
		if (connected()) {
			MessageDataObject msg = new MessageDataObject(this.clientid, Message.LOGIN);
	  		msg.addData("username", userName);
	  		msg.addData("password", password);
	  		msg.addData("url",url);
            sendMessage(msg);
        }
        else {
        	util.Logger.singleton().error(new NotConnectedException("Server unreachable!"));
		}	
	}

	public XMLSocketClient getNc() {
		return socket;
	}

	
	public void connectToRally(String rallyProjectName) {
		MessageDataObject msg = new MessageDataObject(this.clientid, Message.CONNECT_TO_RALLY, rallyProjectName );
		 sendMessage(msg);
		
	}		

}
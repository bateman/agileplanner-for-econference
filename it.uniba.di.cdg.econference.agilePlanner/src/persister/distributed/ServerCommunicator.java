package persister.distributed;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;

import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.Message;
import persister.Event;
import persister.SynchronousPersister;
import persister.auth.AuthenticatorFactory;
import persister.data.Backlog;
import persister.data.IndexCard;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.MessageDataObject;
import persister.data.impl.ProjectDataObject;
import persister.distributed.Arguments;
import persister.distributed.ServerCallbackCommunicator;
import persister.distributed.ServerCommunicator;
import persister.local.PersisterToXML;
import persister.local.RallyConnection;
import persister.xml.XMLSocketServer;
import persister.xml.converter.Converter;




/**
 * @author dhillonh
 * 
 * 
 */

@SuppressWarnings("unused")
public class ServerCommunicator implements ServerCallbackCommunicator {

	private static final long serialVersionUID = 6221398165337964512L;

	protected static ServerCallbackCommunicator comm = null;
	public static final Integer SERVER_CLIENT_VERSION = 1;

	private SynchronousPersister synchronousPersister;
	private long clientid = 1;
	private long uiClientIDGenerator = 1;
	private XMLSocketServer ns;
	private String currentProjectDirectory;
	private String uname;
	private String upass;

	public static final String NOT_CONNECTED_MESSAGE = "Not connected yet";
	/**
	 * Starts the server for AgilepPlanner
	 *  
	 * @param args
	 *            port portnumber directory project directory project project
	 */
	public static void main(String[] args) {
		Arguments arguments = new Arguments(args);
		try {

			String pd = arguments.getValue("directory");
			if (pd == null) {
				File getPath = new File("ProjectDirectory");
				pd = getPath.getAbsolutePath();
			}

			String project = arguments.getValue("project");
			if (project == null)
				project = "ProjectFile";

			String portString = arguments.getValue("port");
			int port;
			if (portString == null) {
				port = 5555;
			} else
				port = Integer.valueOf(portString);

			String webserviceportString = arguments.getValue("wsport");
			int webserviceport;
			if (webserviceportString == null) {
				webserviceport = 8080;
			} else
				webserviceport = Integer.valueOf(webserviceportString);

			String webserviceHostString = arguments.getValue("wshost");
			if (webserviceHostString == null)
				webserviceHostString = "localhost";
			String username = arguments.getValue("username");
			String password = arguments.getValue("password");

			if (arguments.getValue("auth-class") != null)
				AuthenticatorFactory.AuthenticatorClass = arguments
						.getValue("auth-class");

			comm = new ServerCommunicator(pd, project, port, null, null);
		

		} catch (Exception e) {
			util.Logger.singleton().error(e);
			System.exit(-1);
		}
	}


	// used for testing webservice startup only
	public ServerCommunicator(String projectDirectory, String projectName,
			int port, boolean startWebService) throws Exception {
		this(projectDirectory, projectName, port);

	}

	// used by PersisterConnectDialog box to start server on localhost without
	// rally connection.
	public ServerCommunicator(String projectDirectory, String projectName,
			int port) throws Exception {
		this(projectDirectory, projectName, port, null, null);

	}

	
	public ServerCommunicator(String projectDirectory, String projectName,
			int port, String uname, String upass) throws Exception {
		this.uname = uname;
		this.upass = upass;
//		this.projectName = projectName;

			if (projectDirectory != null) {
				System.out.println("projectDirectory: " + projectDirectory
						+ " projectname= " + projectName);
				System.out
						.println("Connected to AgilePlanner server without connection to Rally.");
				this.synchronousPersister = new PersisterToXML(
						projectDirectory, projectName);
			}
         //convert normal to Xstream
		this.ns = new XMLSocketServer(port,this);
		this.currentProjectDirectory = projectDirectory;

	}


	public void shutDown() {
		
		ns.kill();
		synchronousPersister = null;
	}

	public synchronized void receiveMessage(Message message, int clientId) {

		Backlog back;
		Iteration iter;
		StoryCard sc;
		Project prj;
		TeamMember own;
		Message returnMessage;
		long id;

		try {
			switch (message.getMessageType()) {
			case Message.BRING_TO_FRONT:
				break;

			case Message.ARRANGE_PROJECT:
				arrangeProject(message);
				break;
			case Message.CONNECT:
				connectToProject(message, clientId);
				sendUIEventPropagatorID();
				sendVersionCallBack(clientId);
				break;
			case Message.CONNECT_TO_RALLY:
				connectToRally(message);
				break;
			case Message.CREATE_BACKLOG:
				createdBacklog(message);
				break;
			case Message.CREATE_ITERATION:
				createdIteration(message);
				break;
			case Message.CREATE_OWNER:
			    createdOwner(message);
			    break;
			case Message.CREATE_PROJECT:
				createdProject(message);
				break;
			case Message.CREATE_STORYCARD:
				createdStoryCard(message);
				break;

			case Message.DELETE_CARD:
				deletedCard(message);
				break;
			case Message.DELETE_OWNER:
				deletedOwner(message);
				break;
			case Message.DELETE_REMOTE_MOUSE:
				break;

			case Message.GET_PROJECT_NAMES:
				System.out.println("Servergot the request");
				getProjectNames();
				break;

			case Message.LOGIN:
				login(message);
				break;

			case Message.LOAD:
				String loadProjectName = (String) message.getMessage();
				Project proj = this.synchronousPersister.load(loadProjectName);
				this.loadedProject(proj);
				break;

			case Message.MOVE_STORYCARD_TO_NEW_PARENT:
				movedStoryCardToNewParent(message);
				break;

			case Message.MOUSE_MOVE:
				// use the id to send the message to the correct client!!!
				((Event) message.getMessage()).setId(clientId);
				ns.sendToAllButOneClient(message, clientId);
				break;

			case Message.DOWNLOAD:
				String[] downloadFile = (String[]) message.getMessage();
				byte[] str = this.synchronousPersister.readFile(downloadFile[0], Integer.parseInt(downloadFile[1]));
				this.downloadFile(downloadFile, str);
				break;

			case Message.SYNCH_PROJECT:
				synchronizeProject(message);
				break;

			case Message.TIMEBOX_LOAD:
				Object[] msg = (Object[]) message.getMessage();
				String projectName = (String) msg[0];
				Timestamp start = (Timestamp) msg[1];
				Timestamp end = (Timestamp) msg[2];
				this.synchronousPersister.load(projectName, start, end);

			case Message.UNDELETE_CARD:
				undeletedCard(message);
				break;
			case Message.UPDATE_CARD:
				updatedCard(message);
				break;
				
			case Message.UPDATE_LEGEND:
				updatedLegend(message);
				break;
			case Message.UPDATE_OWNER:
				updatedOwner(message);
				break;
			case Message.UPLOAD:
				Object[] returnedData = (Object[]) message.getMessage();
				boolean bool = synchronousPersister.writeFile(
						(String) returnedData[0], (byte[]) returnedData[1],
						Integer.parseInt((String.valueOf(returnedData[2]))));
				uploadFile(bool);
				break;
			case Message.KEYSTROKE:
				ns.sendToAllButOneClient(message, clientId);
				break;

			case Message.GET_ACCEPTANCE_TEST_REQ:
				this.getAcceptanceTestForStoryCard(message, clientId);
				break;
				
				
			default:
				System.out.println("Message = " + message.getMessageType()
						+ " was not handled");
				break;
			}
		} catch (Exception e) {
			
			util.Logger.singleton().error(e);
		}

	}
 
	private void connectToRally(Message message) throws Exception {
		
		if(!(synchronousPersister instanceof RallyConnection))
			synchronousPersister = new RallyConnection(this.currentProjectDirectory, message);
		Project pdo = synchronousPersister.synchronizeProject((String)message.getMessage());
		MessageDataObject returnMessage = new MessageDataObject(this.clientid,
				Message.CONNECT_TO_RALLY, pdo.clone());
		
		sendMessage(returnMessage);
	}


	private void login(Message msg) throws Exception{
		List<String> str = null;
		str = RallyConnection.login(msg);
		MessageDataObject returnMessage = new MessageDataObject(this.clientid,
				Message.LOGIN, str);
		sendMessage(returnMessage);
	 }

	

	private void synchronizeProject(Message msg) throws Exception {
		Project pdo = synchronousPersister.getProject();
		if(!(synchronousPersister instanceof RallyConnection))
			synchronousPersister = new RallyConnection(this.currentProjectDirectory, pdo, msg);
		
		Project pdo1 = synchronousPersister.synchronizeProject((String)msg.getMessage());
		
		MessageDataObject returnMessage = new MessageDataObject(this.clientid,
				Message.SYNCH_PROJECT, pdo1.clone());
		
		sendMessage(returnMessage);
	}


	private void sendUIEventPropagatorID() {
	}

	//made it public for junit testing
	public void connectToProject(Message msg, int sendId) throws Exception {

		System.out.println("Connecting to project "
				+ msg.getMessage().toString());

		Project curentProject = null;

			String newProjectName;
			boolean projectExisted = false;
			if (msg.getMessage() == null) {
				newProjectName = this.synchronousPersister.getProject()
						.getName();
				projectExisted = true;
			} else {
				newProjectName = (String) msg.getMessage();
				List<String> currentProjects = this.synchronousPersister
						.getProjectNames();
				for (int loop = 0; loop < currentProjects.size(); loop++) {
					if (newProjectName.equals(currentProjects.get(loop))) {
						projectExisted = true;
						break;
					}
				}

			}
			if (!projectExisted){
				if(newProjectName.equals(NOT_CONNECTED_MESSAGE)){
					newProjectName = this.synchronousPersister.getProject().getName();
				} else {
					createProject((String)msg.getMessage());
				}
	
			}
			if (!newProjectName.equals(this.synchronousPersister.getProject().getName())) {
				this.synchronousPersister.setNewPersister(newProjectName);
			}
			
			curentProject = (Project) synchronousPersister.getProject().clone();

		
		MessageDataObject returnMessage = new MessageDataObject(this.clientid,
				Message.CONNECT, curentProject);
		sendMessage(returnMessage);
		
	}
	
	private void createProject(String projectName){
		synchronousPersister.createProject(projectName);
	}

	/***************************************************************************
	 * UPLOAD & DOWNLOAD *
	 **************************************************************************/

	private void downloadFile(String[] downloadedFile, byte[] content) {

		// discard what is in content content[0];
		Object[] feedback = new Object[3];
		feedback[0] = downloadedFile;
		feedback[1] = content;
		feedback[2] = this.synchronousPersister.getProject().getName();
		MessageDataObject message = new MessageDataObject(this.clientid,
				Message.DOWNLOAD, feedback);
		sendMessage(message);

	}

	private void uploadFile(boolean bool) {
		MessageDataObject message = new MessageDataObject(this.clientid,
				Message.UPLOAD, bool);
		sendMessage(message);

	}


	/***************************************************************************
	 * CREATE *
	 **************************************************************************/

	private void createdBacklog(Message message) {
		Backlog back = (Backlog) message.getMessage();
		Backlog toReturn = null;
		try {
			toReturn = synchronousPersister.createBacklog(back.getWidth(), back
					.getHeight(), back.getLocationX(), back.getLocationY());
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.CREATE_BACKLOG, toReturn);
			sendMessage(returnMessage);
		} catch (ForbiddenOperationException e) {
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.EXCEPTION, e, Message.CREATE_BACKLOG);
			sendMessage(returnMessage);
		}
	}

	private void createdIteration(Message message) {
		Iteration iter = (Iteration) message.getMessage();
		Iteration iterReturned = synchronousPersister.createIteration(iter
				.getName(), iter.getDescription(), iter.getWidth(), iter
				.getHeight(), iter.getLocationX(), iter.getLocationY(), iter
				.getAvailableEffort(), iter.getStartDate(), iter.getEndDate(), iter.getRotationAngle(),iter.getRallyID());

		MessageDataObject returnMessage = new MessageDataObject(this.clientid,
				Message.CREATE_ITERATION, iterReturned);
		sendMessage(returnMessage);
	}
	
	private void createdOwner(Message message) {
		TeamMember own = (TeamMember) message.getMessage();
		TeamMember ownerReturned = synchronousPersister.createOwner(own.getName());

		MessageDataObject returnMessage = new MessageDataObject(this.clientid,
				Message.CREATE_OWNER, ownerReturned);
		sendMessage(returnMessage);
	}


	private void createdProject(Message message) {
		Project prj = (Project) message.getMessage();
		Project returnProject = null;

		returnProject = synchronousPersister.createProject(prj.getName());
		MessageDataObject returnMessage = new MessageDataObject(this.clientid,
				Message.CREATE_PROJECT, returnProject.clone());
		sendMessage(returnMessage);
	}

	public long createdStoryCard(Message message)
			throws ForbiddenOperationException {

		StoryCard storyCard = null;
		StoryCard sc = (StoryCard) message.getMessage();
		try {
			if (sc.getHandwritingImage() == null) {
				System.out.println(sc.getName());
				storyCard = synchronousPersister.createStoryCard(sc.getName(),
						sc.getDescription(), sc.getWidth(), sc.getHeight(), sc
								.getLocationX(), sc.getLocationY(), sc
								.getParent(), sc.getBestCaseEstimate(), sc
								.getMostlikelyEstimate(), sc
								.getWorstCaseEstimate(), sc.getActualEffort(),
						sc.getStatus(), sc.getColor(), sc.getCardOwner(), sc.getRotationAngle(),sc.getRallyID(), sc.getFitId());
			} else {
				storyCard = synchronousPersister.createTabletPCStoryCard(sc
						.getName(), sc.getDescription(), sc.getWidth(), sc
						.getHeight(), sc.getLocationX(), sc.getLocationY(), sc
						.getParent(), sc.getBestCaseEstimate(), sc
						.getMostlikelyEstimate(), sc.getWorstCaseEstimate(), sc
						.getActualEffort(), sc.getStatus(), sc.getColor(), sc
						.getHandwritingImage(),sc.getRallyID());
			}
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.CREATE_STORYCARD, storyCard);
			sendMessage(returnMessage);
			return storyCard.getId();
		} catch (IndexCardNotFoundException e) {
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.EXCEPTION, e,
					Message.CREATE_STORYCARD);
			sendMessage(returnMessage);
			return -1;
		}

	}

	private void loadedProject(Project project) {
		MessageDataObject message = new MessageDataObject(this.clientid,
				Message.LOAD, project);
		sendMessage(message);

	}

	/***************************************************************************
	 * DELETE *
	 **************************************************************************/

	public void deletedCard(Message message) {

		long id = Long.valueOf((String)message.getMessage());
		IndexCard ic = null;
		try {
			ic = synchronousPersister.deleteCard(id);
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.DELETE_CARD, ic);
			sendMessage(returnMessage);
			System.out.println("delete card has been sent by server");

		} catch (IndexCardNotFoundException e) {
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.EXCEPTION, e, Message.DELETE_CARD);
	//		System.out.println("index card not found");
			sendMessage(returnMessage);
		} catch (ForbiddenOperationException e) {
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.EXCEPTION, e);
			sendMessage(returnMessage);
		}
	}
	
	public void deletedOwner(Message message) {

		TeamMember teamMember = ((TeamMember)message.getMessage());
		TeamMember ic = null;
		ic = synchronousPersister.deleteOwner(teamMember);
		MessageDataObject returnMessage = new MessageDataObject(
				this.clientid, Message.DELETE_OWNER, ic);
		sendMessage(returnMessage);
		System.out.println("delete owner has been sent by server");
	}


	/***************************************************************************
	 * UNDELETE *
	 * 
	 * @throws ForbiddenOperationException
	 **************************************************************************/

	private void undeletedCard(Message message)
			throws ForbiddenOperationException {
		IndexCard indexCard = (IndexCard) message.getMessage();
		IndexCard ic = null;
		try {
			ic = synchronousPersister.undeleteCard(indexCard);
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.UNDELETE_CARD, ic);
			sendMessage(returnMessage);
		} catch (IndexCardNotFoundException e) {
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.EXCEPTION, e, Message.UNDELETE_CARD);
			sendMessage(returnMessage);
		}
	}
	
	/***************************************************************************
	 * UPDATE Legend *
	 * 
	 * @throws ForbiddenOperationException
	 **************************************************************************/
	
	public void updatedLegend(Message message) throws ForbiddenOperationException{
		Legend legend = (Legend) message.getMessage();
		Legend ldo = null;
		
	
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.UPDATE_LEGEND, legend);
			sendMessage(returnMessage);
			ldo = synchronousPersister.updateLegend(legend);

		

		
	}
	public void updatedOwner(Message message) throws ForbiddenOperationException{
		TeamMember teamMember = (TeamMember) message.getMessage();
		TeamMember ldo = null;
		MessageDataObject returnMessage = new MessageDataObject(this.clientid, Message.UPDATE_OWNER, teamMember);
		sendMessage(returnMessage);
		ldo = synchronousPersister.updateOwner(teamMember);
	}

	/***************************************************************************
	 * UPDATE CARD *
	 * 
	 * @throws ForbiddenOperationException
	 **************************************************************************/

	public void updatedCard(Message message) throws ForbiddenOperationException {
		IndexCard indexCard = (IndexCard) message.getMessage();
		IndexCard ic = null;
		try {
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.UPDATE_CARD, indexCard);
			sendMessage(returnMessage);
			ic = synchronousPersister.updateCard(indexCard);

		} catch (IndexCardNotFoundException e) {
			MessageDataObject returnMessage = new MessageDataObject(
					this.clientid, Message.EXCEPTION, e, Message.UPDATE_CARD);
			sendMessage(returnMessage);
		}

	}

	/***************************************************************************
	 * MOVE STORYCARD BETWEEN PARENTS *
	 * 
	 * @throws ForbiddenOperationException
	 **************************************************************************/

	private void movedStoryCardToNewParent(Message message)
			throws ForbiddenOperationException {

		StoryCard sc = (StoryCard) message.getMessage();
		System.out.println("I am server"+sc.getId());
		long id = sc.getId();
		long oldparentid = sc.getParent();

		long newParentId = Long.parseLong(((MessageDataObject) message).getData().get("newparentid"));
		int locationX = Integer.parseInt(((MessageDataObject) message).getData().get("locationX"));
		int locationY = Integer.parseInt(((MessageDataObject) message).getData().get("locationY"));
		float rotation = Float.parseFloat(((MessageDataObject)message).getData().get("rotation"));

		StoryCard returnStoryCard = null;
		try {
			returnStoryCard = synchronousPersister.moveStoryCardToNewParent(id, oldparentid, newParentId, locationX, locationY,rotation);
			MessageDataObject returnMessage = new MessageDataObject(this.clientid, Message.MOVE_STORYCARD_TO_NEW_PARENT, returnStoryCard);
			sendMessage(returnMessage);
			System.out.println("new stroy card has been sent");
		} catch (IndexCardNotFoundException e) {
			MessageDataObject returnMessage = new MessageDataObject(this.clientid, Message.EXCEPTION, e, Message.MOVE_STORYCARD_TO_NEW_PARENT);
			sendMessage(returnMessage);
		}
	}

	/***************************************************************************
	 * GET ACCEPTANCE TEST FOR STORY CARD
	 * 
	 * 
	 * this is where the server is instructed to retrieve test history.
	 **************************************************************************/

	private void getAcceptanceTestForStoryCard(Message msg, int senderId) {
		MessageDataObject data = (MessageDataObject) msg;
		String qName = data.getData().get("qName");

		MessageDataObject resp = new MessageDataObject(Message.GET_ACCEPTANCE_TEST_RESP);
		resp.addData("num-runs", "1");
		resp.addData("num-pass", "4");
		resp.addData("num-fail", "1");

		ns.sendToSingleClient(resp, senderId);
	}

	/***************************************************************************
	 * GETTERS & SETTERS *
	 **************************************************************************/

	public long getClientId() {
		return this.clientid;
	}

	
	private void getProjectNames() {
		List<String> str = null;
		str = ((PersisterToXML) synchronousPersister).getProjectNames();
		System.out.println("System got names");
		MessageDataObject returnMessage = new MessageDataObject(this.clientid, Message.GET_PROJECT_NAMES, str);
		sendMessage(returnMessage);
	}

	private void sendMessage(Message msg) {
		ns.send(msg);
	}

	public void arrangeProject(Message message) throws ForbiddenOperationException {
		Project project = (Project) message.getMessage();
		Project ic = null;
		ic = synchronousPersister.arrangeProject(project);
		MessageDataObject returnMessage = new MessageDataObject(this.clientid, Message.ARRANGE_PROJECT, project);
		sendMessage(returnMessage);
	}

	public void sendVersionCallBack(long clientId){
		Message msg = new MessageDataObject(clientId, Message.VERSION_VERIFICATION, Converter.toXML(ServerCommunicator.SERVER_CLIENT_VERSION));
		sendMessage(msg);
	}
	
	public static ServerCallbackCommunicator getComm() {
		return comm;
	}

	public SynchronousPersister getSynchronousPersister() {
		return synchronousPersister;
	}

}

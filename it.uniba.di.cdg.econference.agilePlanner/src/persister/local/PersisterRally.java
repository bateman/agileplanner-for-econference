package persister.local;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Stub;

import persister.CouldNotLoadProjectException;
import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.Backlog;
import persister.data.IndexCard;
import persister.data.Iteration;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.data.impl.TeamMemberDataObject;
import persister.factory.PersisterFactory;


import com.rallydev.webservice.v1_02.domain.CreateResult;
import com.rallydev.webservice.v1_02.domain.HierarchicalRequirement;
import com.rallydev.webservice.v1_02.domain.OperationResult;
import com.rallydev.webservice.v1_02.domain.PersistableObject;
import com.rallydev.webservice.v1_02.domain.QueryResult;
import com.rallydev.webservice.v1_02.domain.User;
import com.rallydev.webservice.v1_02.domain.Workspace;
import com.rallydev.webservice.v1_02.domain.WorkspaceDomainObject;
import com.rallydev.webservice.v1_02.service.RallyService;
import com.rallydev.webservice.v1_02.service.RallyServiceServiceLocator;

/*
 * 
 */
public class PersisterRally /* implements SynchronousPersister */{
	/**
	 * Parameters for AP
	 */
	private static final String RALLY_ROOT_URL = "https://rally1.rallydev.com:443/slm/webservice/1.02/";

	private final String RALLY_ITERATION_URL = RALLY_ROOT_URL + "iteration/";
	private final String RALLY_STORY_URL = RALLY_ROOT_URL + "hierarchicalrequirement/";
	private final String PROJECT = "Project";
	private final String USER = "User";
	private final String ITERATION = "Iteration";
	private final String USERSTORY = "HierarchicalRequirement";
	private final String TASK = "Task";
	private final int pageSize = 100;
	private final int backlogID = 2;
	private final String backlogName = "Backlog";
	private final int incStep = 5;

	private persister.data.Project APPrjObj;
	private List<StoryCard> IterStory;
	private List<StoryCard> BacklogStory;
	private int APIterLocX = 0;
	private int APIterLocY = 80;
	private int APBklgStoryLocX = 20;
	private int APBklgStoryLocY = 350;
	private List<String> users;

	/**
	 * Parameters for Rally
	 */
	private RallyService service;
	private Workspace WORKSPACE;
	private com.rallydev.webservice.v1_02.domain.Project rallyProject;
	private com.rallydev.webservice.v1_02.domain.User rallyUsers [];

	private List<com.rallydev.webservice.v1_02.domain.HierarchicalRequirement> rallyStoryList = new ArrayList();
	private List<com.rallydev.webservice.v1_02.domain.Iteration> rallyIterList = new ArrayList();
	private List<com.rallydev.webservice.v1_02.domain.User> rallyUserList = new ArrayList();
	/**
	 * Other parameters
	 */
	private boolean needIterNameOnly = false;

	private String[][] iterations;
	private String currentProjectName;
	private List<StoryCard> storyCardsListFromBacklog;
	private List<Iteration> iterationsListFromProject;
	

	/***************************************************************************
	 * Connect to Rally server, and load a project
	 * 
	 * @throws CouldNotLoadProjectException
	 * 
	 **************************************************************************/

	public PersisterRally(String user, String pass, String urlAdd) throws ServiceException,
			CouldNotLoadProjectException, RemoteException {
		/* connect to Rally */
		
		URL url = null;
		try {
			url = new URL("https://"+urlAdd+".rallydev.com:443/slm/webservice/1.02/RallyService");
		} catch (MalformedURLException e) {
			util.Logger.singleton().error(e);
		}
		service = (new RallyServiceServiceLocator()).getRallyService(url);
		Stub stub = (Stub) service;
		stub.setMaintainSession(true);
		stub.setUsername(user);
		stub.setPassword(pass);

	}

	

	public User getCurrentUser() throws RemoteException {
		if (service != null) {
			return (User) service.getCurrentUser();
		}
		return null;
	}

	/***************************************************************************
	 * LOAD AND SAVE : MAIN METHODS *
	 * 
	 **************************************************************************/

	public Project load(String projectName)
			throws CouldNotLoadProjectException, RemoteException {
		QueryResult rallyPrjResult;
		int startIndex = 1;

		/* get the named project from Rally */
		rallyPrjResult = service.query(WORKSPACE, PROJECT, "((Name = \""
				+ projectName + "\")" + " AND (State = \"Open\"))", "", false,
				startIndex, pageSize);
		if (rallyPrjResult.getTotalResultCount() <= 0) {
			System.err.println("No Project Data.");
			throw new CouldNotLoadProjectException("Wrong Project Name");
		}
		rallyProject = (com.rallydev.webservice.v1_02.domain.Project) rallyPrjResult
				.getResults()[0];

		/* specify the global parameter "APPrjObj" */
		this.convertRPrjToAPPrj(projectName,
				extractObjectIDFromRef(rallyProject.getRef()));

	
		return (Project) APPrjObj;
	}

	public List<String> getProjectNames() {
		List<String> projectNamesList = new ArrayList();

		try {
			/* get all the projects from Rally */
			long numRec = 0;
			int startIndex = 1;
			com.rallydev.webservice.v1_02.domain.Project currentRallyPrj;
			QueryResult rallyPrjResult;

			do {
				rallyPrjResult = service.query(WORKSPACE, PROJECT, "", "",
						true, startIndex, pageSize);
				numRec = rallyPrjResult.getTotalResultCount();

				for (int i = 0; i < numRec; i++) {
					currentRallyPrj = (com.rallydev.webservice.v1_02.domain.Project) rallyPrjResult
							.getResults()[i];
					projectNamesList.add(currentRallyPrj.getName());
				}
				startIndex += pageSize;

			} while (numRec == pageSize);

		} catch (RemoteException e) {

			util.Logger.singleton().error(e);
			return null;
		}
		System.out.println(projectNamesList.toString());
		return projectNamesList;
	}

	

	/***************************************************************************
	 * LOAD AND SAVE : HELPER METHODS *
	 * 
	 **************************************************************************/

	private IterationDataObject loadIteration(
			com.rallydev.webservice.v1_02.domain.Iteration rallyIterObj)
			throws RemoteException {
		Iteration APIter = this.convertRIterToAPIter(rallyIterObj);
		APIter.setStoryCardChildren(loadStories(rallyIterObj.getRef()));

		return (IterationDataObject) APIter;
	}

	/**
	 * 
	 * NOTE:
	 * 
	 * Load user stories for
	 * 
	 * 1. an iteration (stories assigned to the iteration and MUST have
	 * children) 2. the backlog (stories without children and/or any iterations)
	 * 
	 * @throws RemoteException
	 */
	private List<StoryCard> loadStories(String rallyIterRef)
			throws RemoteException {
		List<StoryCard> storyCards = new ArrayList<StoryCard>();
		HierarchicalRequirement rallyStoryObj;
		String queryStr = "";
		int startIndex = 1;
		long numRec = 0;

		if (rallyIterRef != null) {
			/* load stories under a specified iteration */
			queryStr = "(" + this.ITERATION + " = \"" + rallyIterRef + "\")";
		} else {
			/* load stories under the backlog */
			queryStr = "(" + this.ITERATION + " = null)";
		}

		/* query stories from Rally */
		do {
			QueryResult storyResult = service.query(WORKSPACE, rallyProject,
					false, false, "HierarchicalRequirement", queryStr, "",
					true, startIndex, pageSize);
			numRec = storyResult.getTotalResultCount();
			for (int i = 0; i < numRec; i++) {
				rallyStoryObj = (HierarchicalRequirement) storyResult
						.getResults()[i];
				StoryCard storycard = convertRStyToAPSty(rallyStoryObj);
				storycard.setRallyID(true);
				storyCards.add(storycard);
			}
			startIndex += pageSize;

		} while (numRec == pageSize);

		return storyCards;
	}

	private Backlog loadBacklog() throws RemoteException {
		Backlog backlog = new BacklogDataObject();

		backlog.setId(backlogID); // How to create a backlog ID???
		backlog.setParent(APPrjObj.getId());
		backlog.setName(backlogName);

		
		backlog.setStoryCardChildren(loadStories(null));

		return backlog;
	}

	/***************************************************************************
	 * CREATE: MAIN METHODS *
	 **************************************************************************/

	

	public void createIterationForRally(Iteration iter) {
		/*
		 * create a new Rally iteration
		 */

		String iterState = "Planning";
		com.rallydev.webservice.v1_02.domain.Iteration newRallyIteration;

		newRallyIteration = new com.rallydev.webservice.v1_02.domain.Iteration();
		newRallyIteration.setName(iter.getName());
		newRallyIteration.setTheme(iter.getDescription());
		newRallyIteration.setResources(Double
				.valueOf(iter.getAvailableEffort()));
		newRallyIteration.setState(iterState);
		newRallyIteration.setStartDate(this.tStampToCal(iter.getStartDate()));
		newRallyIteration.setEndDate(this.tStampToCal(iter.getEndDate()));

		String notes = "";
		notes = this.createIterLocInfo(new String[] { "X:", "Y:", "Width:",
				"Height:" }, new String[] {
				String.valueOf(iter.getLocationX()),
				String.valueOf(iter.getLocationX()),
				String.valueOf(iter.getWidth()),
				String.valueOf(iter.getHeight()) }, ";");
		newRallyIteration.setNotes(notes);

		newRallyIteration.setProject(this.rallyProject);

		/* save the new Rally iteration */
		try {
			CreateResult rallyIterCreateRlt = this.service
					.create(newRallyIteration);
			newRallyIteration = (com.rallydev.webservice.v1_02.domain.Iteration) rallyIterCreateRlt
					.getObject();

			if (rallyIterCreateRlt.getErrors().length != 0) {
				for (int i = 0; i < rallyIterCreateRlt.getErrors().length; i++) {
					System.out.println("Create ITERATION: "
							+ rallyIterCreateRlt.getErrors()[i]);
				}
				return;
			} else {
				newRallyIteration = (com.rallydev.webservice.v1_02.domain.Iteration) this.service
						.read(newRallyIteration);
			}

		} catch (RemoteException e) {

			util.Logger.singleton().error(e);
			return;
		}

		iter.setId(newRallyIteration.getObjectID());
	    iter.setRallyID(true);

		long iterNewID = iter.getId();
		List<StoryCard> storyCardsListFromIteration = new ArrayList<StoryCard>();
		storyCardsListFromIteration.addAll(iter.getStoryCardChildren());
		for (StoryCard storyCard : storyCardsListFromIteration) {
			storyCard.setParent(iterNewID);
		}

	
	}

	public void createProject(Project projectDataObject) {
		com.rallydev.webservice.v1_02.domain.Project newRallyPrj;
		this.APPrjObj = projectDataObject;
		// projectDataObject.setName("abc");
		try {
			/* create a new Rally project */
			String prjState = "Open";

			newRallyPrj = new com.rallydev.webservice.v1_02.domain.Project();
			newRallyPrj.setName(projectDataObject.getName());
			newRallyPrj.setDescription(" ");
			newRallyPrj.setNotes(" ");
			newRallyPrj.setOwner(this.getCurrentUser().getLoginName());
			newRallyPrj.setState(prjState);

			/* save the new Rally project */
			CreateResult rallyPrjCreateRlt = this.service.create(newRallyPrj);
			newRallyPrj = (com.rallydev.webservice.v1_02.domain.Project) rallyPrjCreateRlt
					.getObject();

			if (rallyPrjCreateRlt.getErrors().length != 0) {
				for (int i = 0; i < rallyPrjCreateRlt.getErrors().length; i++) {
					System.out.println("Create PROJECT: "
							+ rallyPrjCreateRlt.getErrors()[i]);
				}
				return;
			} else {
				newRallyPrj = (com.rallydev.webservice.v1_02.domain.Project) this.service
						.read(newRallyPrj);
				this.APPrjObj.setId(newRallyPrj.getObjectID());

				// update parent id of backlog
				this.APPrjObj.getBacklog().setParent(this.APPrjObj.getId());

				// update parent id for iteration
				List<Iteration> tempIterList = new ArrayList<Iteration>();
				tempIterList.addAll(this.APPrjObj.getIterationChildren());
				for (Iteration iteration : tempIterList) {
					iteration.setParent(this.APPrjObj.getId());
				}
			}
			/* reset the global parameter "rallyPrjObj" */
			rallyProject = newRallyPrj;
		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
			return;
		}

		
	}

	

	public void createStoryCardForRally(StoryCard storyCard)
			throws IndexCardNotFoundException {

		/* check if the specified parent is an iteration or the backlog */
		com.rallydev.webservice.v1_02.domain.Iteration rallyIteration;
		rallyIteration = new com.rallydev.webservice.v1_02.domain.Iteration();
		if (storyCard.getParent() != backlogID) {
			rallyIteration.setRef(this.RALLY_ITERATION_URL
					+ storyCard.getParent());
		}

		com.rallydev.webservice.v1_02.domain.HierarchicalRequirement newRallyStory;
		try {
			/* create a new Rally story */
			newRallyStory = new com.rallydev.webservice.v1_02.domain.HierarchicalRequirement();
			newRallyStory.setName(storyCard.getName());
			// newRallyStory.setOwner(storyCard.getName());
			newRallyStory.setDescription(storyCard.getDescription());
			newRallyStory.setScheduleState(storyCard.getStatus());

			if (storyCard.getParent() != backlogID) {
				newRallyStory.setIteration(rallyIteration);
			}

			Calendar cal = Calendar.getInstance();
			Date today = new Date();
			cal.setTime(today);
			newRallyStory.setCreationDate(cal);
			newRallyStory.setLastUpdateDate(cal);

			/*
			 * NOTE: the following implementation needs refactoring!!! How to
			 * convert AP estimates to Rally estimates ???
			 */
			newRallyStory.setPlanEstimate(Double.valueOf(storyCard.getMostlikelyEstimate()));

			/* set card locations and color */
			newRallyStory.setAgilePlannerXPos((long) storyCard.getLocationX());
			newRallyStory.setAgilePlannerYPos((long) storyCard.getLocationY());
			newRallyStory.setAgilePlannerWidth((long) storyCard.getWidth());
			newRallyStory.setAgilePlannerHeight((long) storyCard.getHeight());
			newRallyStory.setAgilePlannerColor(storyCard.getColor());
			newRallyStory.setProject(rallyProject);
			

			/* save the new Rally story */
			CreateResult rallyStoryCreateRlt = this.service.create(newRallyStory);
			newRallyStory = (com.rallydev.webservice.v1_02.domain.HierarchicalRequirement) rallyStoryCreateRlt.getObject();

			if (rallyStoryCreateRlt.getErrors().length != 0) {
				for (int i = 0; i < rallyStoryCreateRlt.getErrors().length; i++) {
					System.out.println("Create STORY: "+ rallyStoryCreateRlt.getErrors()[i]);
				}
				return;
			} else {
				newRallyStory = (com.rallydev.webservice.v1_02.domain.HierarchicalRequirement) this.service.read(newRallyStory);
			}

		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
			return;
		}

		storyCard.setId(newRallyStory.getObjectID());
		storyCard.setRallyID(true);
		
	}

	

	/***************************************************************************
	 * CREATE: HELPER METHODS *
	 **************************************************************************/

	/**
	 * convert a Rally project to AP project
	 * 
	 * @param
	 */
	private Project convertRPrjToAPPrj(String prjName, long prjID) {
		ProjectDataObject project = new ProjectDataObject();
		project.setName(prjName);
		project.setId(prjID);
		project.createBacklog(0, 0, 0, 0);
		this.APPrjObj = project;
		return (Project) project.clone();
	}

	/**
	 * convert a Rally iteration to AP iteration
	 * 
	 * @param rallyIterObj
	 */
	private Iteration convertRIterToAPIter(
			com.rallydev.webservice.v1_02.domain.Iteration rallyIterObj) {
		IterationDataObject iteration = new IterationDataObject();
		iteration.setName(rallyIterObj.getName());
		iteration.setId(rallyIterObj.getObjectID());
		iteration.setParent(this.APPrjObj.getId());
		iteration.setDescription(rallyIterObj.getTheme());
		iteration.setStatus(rallyIterObj.getState());
		iteration.setStartDate(this.calToTStamp(rallyIterObj.getStartDate()));
		iteration.setIdRecievedFromRally(true);
		/**
		 * ISSUE: Rally iteration enddate is displayed differently on its
		 * dashboard, and its query page in the API doc!!
		 */
		iteration.setEndDate(this.calToTStamp(rallyIterObj.getEndDate()));

		if (rallyIterObj.getNotes() != null) {
			String[] iterLocInfo = this.parseStr(rallyIterObj.getNotes(), ";");
			if (iterLocInfo != null && iterLocInfo.length == 4) {
				iteration.setLocationX(Integer.valueOf(iterLocInfo[0].substring(2)));
				iteration.setLocationY(Integer.valueOf(iterLocInfo[1].substring(2)));
				iteration.setWidth(Integer.valueOf(iterLocInfo[2].substring(6)));
				iteration.setHeight(Integer.valueOf(iterLocInfo[3].substring(7)));
				iteration.setIdRecievedFromRally(true);
			}
		}

		return iteration;
	}
	
	/**
	 * convert a Rally User to AP TeamMember
	 * 
	 * @param rallyUserObj
	 *            a Rally user object
	 */
	private TeamMember convertROwnerToAPOwner(User rallyUserObj) {
		if (rallyUserObj == null)
			return null;

		/* convert a Rally user to AP owner */
		TeamMemberDataObject owner = new TeamMemberDataObject();
		owner.setName(rallyUserObj.getDisplayName());

		return owner;
	}

	/**
	 * convert a Rally story to AP story
	 * 
	 * @param rallyStoryObj
	 *            a Rally story object
	 */
	private StoryCard convertRStyToAPSty(HierarchicalRequirement rallyStoryObj) {
		if (rallyStoryObj == null)
			return null;

		/* convert a Rally story to AP story */
		StoryCardDataObject storycard = new StoryCardDataObject();
		storycard.setId(rallyStoryObj.getObjectID());
		storycard.setIdRecievedFromRally(true);
		storycard.setName(rallyStoryObj.getName());

		/* set a story's parent to either backlog or an iteration */
		long storyParent = backlogID;
		if (rallyStoryObj.getIteration() != null) {
			storyParent = extractObjectIDFromRef(rallyStoryObj.getIteration().getRef());
		}
		storycard.setParent(storyParent);

		/* save the locataions and color */
		if (rallyStoryObj.getAgilePlannerXPos() != null)
			storycard.setLocationX(rallyStoryObj.getAgilePlannerXPos().intValue());
		if (rallyStoryObj.getAgilePlannerYPos() != null)
			storycard.setLocationY(rallyStoryObj.getAgilePlannerYPos().intValue());
		if (rallyStoryObj.getAgilePlannerWidth() != null)
			storycard.setWidth(rallyStoryObj.getAgilePlannerWidth().intValue());
		if (rallyStoryObj.getAgilePlannerHeight() != null)
			storycard.setHeight(rallyStoryObj.getAgilePlannerHeight().intValue());
		if (rallyStoryObj.getAgilePlannerColor() != null)
			storycard.setColor(rallyStoryObj.getAgilePlannerColor());

		/*
		 * IMPORTANT: The 'time estimate' implementation below NEEDS
		 * refacorring!!! * How to convert Rally estimates to AP estimates ???
		 */
		float rallyStoryPlanEst;


		rallyStoryPlanEst = (rallyStoryObj.getPlanEstimate() == null ? 0: rallyStoryObj.getPlanEstimate().floatValue());
		storycard.setMostlikelyEstimate(rallyStoryPlanEst);
		storycard.setActualEffort(rallyStoryObj.getTaskActualTotal().floatValue());

		storycard.setStatus(rallyStoryObj.getScheduleState());
		storycard.setDescription(rallyStoryObj.getDescription()+ (rallyStoryObj.getNotes().length() == 0 ? "": ("\n" + rallyStoryObj.getNotes())));
		if (rallyStoryObj.getAgilePlannerColor() != null)
			storycard.setColor(rallyStoryObj.getAgilePlannerColor());

		return storycard;
	}

	/***************************************************************************
	 * DELETE & UNDELETE : MAIN METHODS *
	 * 
	 **************************************************************************/

	public IndexCard deleteCardInRally(long id)throws IndexCardNotFoundException, ForbiddenOperationException {
		/**
		 * REMEMBER: currently, Rally iteration CANNOT be deleted! Only Rally
		 * stories can.
		 */
		IndexCard deletedCard = this.APPrjObj.deleteCard(id);
		try {
			if (deletedCard instanceof StoryCard) {
				this.deleteStoryInRally((StoryCard) deletedCard);
			}
		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
		}


		return (IndexCard) deletedCard.clone();
	}

	/**
	 * IMPORTANT: there seems no Undelete function in Rally
	 */
	public IndexCard undeleteCard(IndexCard indexCard) throws IndexCardNotFoundException, ForbiddenOperationException {
		return null;
	}

	/***************************************************************************
	 * DELETE & UNDELETE : HELPER METHODS *
	 * 
	 * @throws RemoteException
	 * 
	 **************************************************************************/

	
	public void deleteStoryInRally(StoryCard story)throws IndexCardNotFoundException, RemoteException {

		/* delete the Rally story */
		com.rallydev.webservice.v1_02.domain.HierarchicalRequirement rallyStoryObj;
		rallyStoryObj = new com.rallydev.webservice.v1_02.domain.HierarchicalRequirement();
		rallyStoryObj.setRef(this.RALLY_STORY_URL + story.getId());

		OperationResult delRlt = this.service.delete(rallyStoryObj);
		if (delRlt.getErrors().length != 0) {
			for (int i = 0; i < delRlt.getErrors().length; i++) {
				System.out.println("Delete STORY: " + delRlt.getErrors()[i]);
			}
			return;
		}

		deleteStoryFromCashedList(story);
	}

	/***************************************************************************
	 * UPDATE: MAIN METHODS
	 * 
	 * 
	 **************************************************************************/

	public IndexCard updateCard(IndexCard indexCard)throws IndexCardNotFoundException {
		IndexCard ic = this.APPrjObj.updateCard(indexCard);
		boolean success = true;

		/* update Rally objects */
		try {
			if (indexCard instanceof IterationDataObject) {
				success = this.updateRallyIter(indexCard);
			} else if (indexCard instanceof StoryCardDataObject) {
				success = this.updateRallyStory(indexCard);
			}
		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
		}

		if (success) {
			return (IndexCard) ic.clone();
		} else {
			return null;
		}
	}

	private boolean updateRallyIter(IndexCard APIterObj) throws RemoteException {
		
		com.rallydev.webservice.v1_02.domain.Iteration rallyIterObj = convertAPIterToRIter((Iteration) APIterObj);
		if (rallyIterObj == null)
			return false;

		return updateRallyObject(rallyIterObj, this.ITERATION);
	}

	private boolean updateRallyStory(IndexCard APStoryObj)throws RemoteException {
		
		HierarchicalRequirement rallyStoryObj = convertAPStyToRSty((StoryCard) APStoryObj);
		if (rallyStoryObj == null)
			return false;

		return updateRallyObject(rallyStoryObj, this.USERSTORY);
	}

	private boolean updateRallyObject(PersistableObject rallyObj, String objType) throws RemoteException {
		
		OperationResult rallyStoryUpdateRlt = this.service.update(rallyObj);

		if (rallyStoryUpdateRlt.getErrors().length != 0) {
			for (int i = 0; i < rallyStoryUpdateRlt.getErrors().length; i++) {
				util.Logger.singleton().info("Update " + objType + ": "
						+ rallyStoryUpdateRlt.getErrors()[i]);
			}
			return false;
		}
		return true;
	}

	/***************************************************************************
	 * UPDATE: HELPER METHODS
	 * 
	 * 
	 **************************************************************************/

	/**
	 * convert an AP iteration to Rally iteration
	 * 
	 * @throws RemoteException
	 * 
	 */
	private com.rallydev.webservice.v1_02.domain.Iteration convertAPIterToRIter(Iteration APIterObj) throws RemoteException {
		
		com.rallydev.webservice.v1_02.domain.Iteration rallyIterObj = new com.rallydev.webservice.v1_02.domain.Iteration();

		rallyIterObj.setName(APIterObj.getName());
		rallyIterObj.setObjectID(APIterObj.getId());
		rallyIterObj.setRef(this.RALLY_ITERATION_URL + APIterObj.getId());
		rallyIterObj.setTheme(APIterObj.getDescription());
		rallyIterObj.setState(APIterObj.getStatus());
		rallyIterObj.setResources((double) APIterObj.getAvailableEffort());
		rallyIterObj.setStartDate(this.tStampToCal(APIterObj.getStartDate()));
		rallyIterObj.setEndDate(this.tStampToCal(APIterObj.getEndDate()));
		rallyIterObj.setState("Planning");

		String notes = "";
		notes = this.createIterLocInfo(new String[] { "X:", "Y:", "Width:","Height:" }, new String[] {
				String.valueOf(APIterObj.getLocationX()),
				String.valueOf(APIterObj.getLocationY()),
				String.valueOf(APIterObj.getWidth()),
				String.valueOf(APIterObj.getHeight()) }, ";");
		rallyIterObj.setNotes(notes);

		com.rallydev.webservice.v1_02.domain.Project rallyPrjObj = null;
		if (APIterObj.getParent() != 0) {
			rallyPrjObj = (com.rallydev.webservice.v1_02.domain.Project) this.findRallyObject(APIterObj.getParent(), this.PROJECT);
		}
		rallyIterObj.setProject(rallyPrjObj);
		return rallyIterObj;
	}

	/**
	 * convert an AP story to Rally story
	 * 
	 * @throws RemoteException
	 */
	private HierarchicalRequirement convertAPStyToRSty(StoryCard APStoryObj)throws RemoteException {
		if (APStoryObj == null)
			return null;

		/* convert an AP story to Rally story */
		HierarchicalRequirement rallyStoryObj = new HierarchicalRequirement();
		rallyStoryObj.setName(APStoryObj.getName());
		rallyStoryObj.setRef(RALLY_STORY_URL + APStoryObj.getId());
		rallyStoryObj.setObjectID(APStoryObj.getId());

		/* set a story's parent to either backlog or an iteration */
		com.rallydev.webservice.v1_02.domain.Iteration rallyIterObj = null;
		if (APStoryObj.getParent() != backlogID) /*
		 * 2 is the id used by AP
		 * backlog
		 */
		{
			rallyIterObj = (com.rallydev.webservice.v1_02.domain.Iteration) this.findRallyObject(APStoryObj.getParent(), this.ITERATION);
			rallyStoryObj.setIteration(rallyIterObj);
		} else {
			com.rallydev.webservice.v1_02.domain.Iteration nullIteration = new com.rallydev.webservice.v1_02.domain.Iteration();
			nullIteration.setRef("null");
			rallyStoryObj.setIteration(nullIteration);
		}

		/*
		 * IMPORTANT: The 'time estimate' implementation below NEEDS
		 * refacorring!!! * How to convert Rally estimates to AP estimates ???
		 */
		rallyStoryObj.setPlanEstimate((double) APStoryObj.getMostlikelyEstimate());
		rallyStoryObj.setTaskActualTotal((double) APStoryObj.getActualEffort());

		/* save the locataions */
		rallyStoryObj.setAgilePlannerXPos((long) APStoryObj.getLocationX());
		rallyStoryObj.setAgilePlannerYPos((long) APStoryObj.getLocationY());
		rallyStoryObj.setAgilePlannerWidth((long) APStoryObj.getWidth());
		rallyStoryObj.setAgilePlannerHeight((long) APStoryObj.getHeight());

		rallyStoryObj.setScheduleState(APStoryObj.getStatus());
		rallyStoryObj.setDescription(APStoryObj.getDescription());
		rallyStoryObj.setAgilePlannerColor(APStoryObj.getColor());

		return rallyStoryObj;
	}

	/***************************************************************************
	 * MOVE STORYCARD BETWEEN PARENTS *
	 * 
	 * @throws
	 * 
	 **************************************************************************/

	public StoryCard moveStoryCardToNewParent(long id, long oldparentid,long newparentid, int locationX, int locationY, float rotation)throws IndexCardNotFoundException {
		StoryCard movedStoryCard = this.APPrjObj.moveStoryCardToNewParent(id,oldparentid, newparentid, locationX, locationY, rotation);

		try {
			this.updateRallyStory(movedStoryCard);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			util.Logger.singleton().error(e);
		}

		return (StoryCard) movedStoryCard.clone();
	}

	/***************************************************************************
	 * Find objects
	 **************************************************************************/

	
	/**
	 * find a Rally object by its id
	 */
	private WorkspaceDomainObject findRallyObject(long objID, String objType)throws RemoteException {

		QueryResult rallyObjResult = service.query(WORKSPACE, rallyProject,
				false, false, objType, "(ObjectID = \"" + objID + "\")", "",
				false, 0, pageSize);

		long numRec = rallyObjResult.getTotalResultCount();
		WorkspaceDomainObject rallyObj;
		if (numRec != 0) {
			return (WorkspaceDomainObject) rallyObjResult.getResults()[0];
		}

		return null;
	}

	/***************************************************************************
	 * Utility funtions
	 **************************************************************************/

	/**
	 * get ObjectID of a Rally Object
	 * 
	 * @param ref
	 * url that represents a specific Rally Object
	 * 
	 */
	private Long extractObjectIDFromRef(String ref) {
		if (ref == null) {
			return Long.valueOf(backlogID);
		}

		int index = ref.lastIndexOf("/");
		return Long.valueOf(ref.substring(index + 1));
	}

	/**
	 * 
	 * convert a java.util.Calendar instance to a java.sql.Timestamp instance
	 * 
	 * @param cal
	 *            a java.util.Calendar instance
	 * @return a java.sql.Timestamp instance
	 */
	private Timestamp calToTStamp(Calendar cal) {
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * NOTE: this method needs a test
	 * 
	 * convert a a java.sql.Timestamp instance to java.util.Calendar instance
	 * 
	 * @param cal
	 *            a java.util.Calendar instance
	 * @return a java.sql.Timestamp instance
	 */
	private Calendar tStampToCal(Timestamp tStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(tStamp.getTime());
		return cal;
	}

	/**
	 * Seperate a string based on a seperator
	 */
	private String[] parseStr(String strToParse, String seperator) {
		if (strToParse != null && seperator != null) {
			return strToParse.split(seperator);
		}
		return null;
	}

	/**
	 * Create a String.
	 */
	private String createIterLocInfo(String[] fldNames, String[] fldValues,String seperator) {

		if (fldNames != null && fldValues != null && seperator != null&& fldNames.length == fldValues.length) {

			String result = "";
			for (int i = 0; i < fldNames.length; i++) {
				result += fldNames[i] + fldValues[i];
				if (i != fldNames.length - 1) {
					result += seperator;
				}
			}
			return result;
		}
		return null;
	}


	public void synchronizeProject(Project project) throws RemoteException, IndexCardNotFoundException, CouldNotLoadProjectException {
		QueryResult rallyPrjResult;
		int startIndex = 1;

		/* get the named project from Rally */
		rallyPrjResult = service.query(WORKSPACE, PROJECT, "((Name = \""
						+ (project.getName()) + "\")" + " AND (State = \"Open\"))", "", false,
						startIndex, pageSize);
		if (rallyPrjResult.getTotalResultCount() <= 0) {
			System.err.println("No Project Data.");
			throw new CouldNotLoadProjectException("Wrong Project Name");
		}
		rallyProject = (com.rallydev.webservice.v1_02.domain.Project) rallyPrjResult.getResults()[0];

		/* specify the global parameter "APPrjObj" */
		this.convertRPrjToAPPrj((rallyProject.getName()),extractObjectIDFromRef(rallyProject.getRef()));

		Backlog backlog = loadBacklog();
		project.setBacklog(backlog);

		/* get the members of project from Rally */
		this.loadUsersFromRallyProject(rallyProject);

		/* get iterations under the named project */
		this.loadIterationsFromRallyProject(rallyProject);

		/* get storyCards under the named project */
		this.loadStoriesFromRallyProject(rallyProject);	


		this.currentProjectName = project.getName();



		// Set TeamMember List in the project
		if(project.getTeamMembers().size()!=0)
			project.getTeamMembers().clear();

		for(com.rallydev.webservice.v1_02.domain.User rStoryObj : rallyUserList){
			TeamMember convertedRallyOwner = convertROwnerToAPOwner(rStoryObj);
			project.addTeamMember(convertedRallyOwner);
		}

		this.handleOrphanCards(project);
		storyCardsListFromBacklog = new ArrayList<StoryCard>((project.getBacklog().getStoryCardChildren()));

		for (StoryCard storyCard : storyCardsListFromBacklog) {
			if (storyCard.getRallyID() && isPresentAtRally(storyCard)) {
				this.updateRallyStory(storyCard);
			} else if (storyCard.getRallyID() && (!isPresentAtRally(storyCard))) {
				project.getBacklog().removeStoryCard(storyCard);
			} else if (!storyCard.getRallyID() ){
				this.createStoryCardForRally(storyCard);
			}
		}

		List<IndexCard> deletedCardsList = new Vector<IndexCard>();
		if (!project.getDeletedRallyCardsList().isEmpty()) {
			deletedCardsList.addAll(project.getDeletedRallyCardsList());
			for (IndexCard ic : deletedCardsList) {
				if (ic instanceof StoryCard){
					deleteStoryInRally((StoryCard) ic);	
				}
			}
			project.getDeletedRallyCardsList().removeAll(deletedCardsList);
		}

		////////////////////for new rally SC in backlog///////////////////////////////

		for(com.rallydev.webservice.v1_02.domain.HierarchicalRequirement rStoryObj : rallyStoryList){

			StoryCard convertedRallyStory = convertRStyToAPSty(rStoryObj);
			if(!isStoryPresentApBl(convertedRallyStory) && (rStoryObj.getIteration()==null))
			{
				convertedRallyStory.setRallyID(true);
				project.getBacklog().addStoryCard(convertedRallyStory);
			}		
		}

		////////////////////for Iteration new in Rally///////////////////////////////

		iterationsListFromProject = new ArrayList<Iteration>();
		iterationsListFromProject.addAll(project.getIterationChildren());


		for(com.rallydev.webservice.v1_02.domain.Iteration rItrObj : rallyIterList){
			
			Iteration convertedRallyIteration = convertRIterToAPIter(rItrObj);

			if(!isItrPresentAp(convertedRallyIteration))
			{
				convertedRallyIteration.setRallyID(true);
				project.addIteration(convertedRallyIteration);
			}					
		}

		iterationsListFromProject.clear();
		iterationsListFromProject.addAll(project.getIterationChildren());

		for (Iteration iteration : iterationsListFromProject) {
			if (iteration.getRallyID() &&  isItrPresentAtRally(iteration))
				this.updateRallyIter(iteration);
			else 
				this.createIterationForRally(iteration);

			List<StoryCard> storyCardsListFromIteration = new ArrayList<StoryCard>();
			storyCardsListFromIteration.addAll(iteration.getStoryCardChildren());
			for (StoryCard storyCard : storyCardsListFromIteration) {
				if (storyCard.getRallyID() && isPresentAtRally(storyCard)) {
					this.updateRallyStory(storyCard);
				} else if (storyCard.getRallyID() && (!isPresentAtRally(storyCard))) {

					try {
						project.deleteCard(storyCard.getId());
					} catch (ForbiddenOperationException e) {
						util.Logger.singleton().error(e);
					}

				} else if (!storyCard.getRallyID() ){
					this.createStoryCardForRally(storyCard);
				}

			}

			////////////////////for new rally SC in iteration///////////////////////////////
			rallyStoryList.clear();
			this.loadStoriesFromRallyProject(rallyProject);

			for(com.rallydev.webservice.v1_02.domain.HierarchicalRequirement rStoryObj : rallyStoryList){

				StoryCard convertedRallyStory = convertRStyToAPSty(rStoryObj);
				if(!isStoryPresentAp(convertedRallyStory,iteration) )
				{
					convertedRallyStory.setRallyID(true);

					if(convertedRallyStory.getParent()== iteration.getId()){
						convertedRallyStory.setLocationX(iteration.getLocationX()+10);
						convertedRallyStory.setLocationY(iteration.getLocationY()+10);
						iteration.addStoryCard(convertedRallyStory);
						project.updateCard(iteration);
					}
				}
			}
		}



		rallyStoryList.clear();
		rallyIterList.clear();
	}
 
	public void handleOrphanCards(Project project){
		boolean found = false;
		for(StoryCard sc: project.getBacklog().getStoryCardChildren()){
			for(TeamMember teamMember: project.getTeamMembers()){
				if(teamMember.getName().equalsIgnoreCase(sc.getCardOwner()))
					found=true;
			}
			if(found==false)
				sc.setCardOwner("TeamMember");
		}
	}
	public void loadUsersFromRallyProject(com.rallydev.webservice.v1_02.domain.Project rProj){
		QueryResult rallyUserResult= null;
		int startIndex = 1;
		long numRec=0;
		com.rallydev.webservice.v1_02.domain.User rallyUserObject;

		try {
			rallyUserResult = service.query(WORKSPACE, rProj, false, false, USER, "", "", true, startIndex, pageSize);
		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
		}
		numRec = rallyUserResult.getTotalResultCount();

		for (int i = 0; i < numRec; i++) {
			rallyUserObject = (com.rallydev.webservice.v1_02.domain.User) rallyUserResult.getResults()[i];
			rallyUserList.add(rallyUserObject);
		}


	}
	public void loadIterationsFromRallyProject(com.rallydev.webservice.v1_02.domain.Project rProj){
		long numRec = 0;
		int startIndex = 1;
		com.rallydev.webservice.v1_02.domain.Iteration rallyIterObj;

		do {
			QueryResult rallyItersResult = null;
			try {
				rallyItersResult = service.query(WORKSPACE, rProj, false, false, ITERATION, "", "", true, startIndex, pageSize);
			} catch (RemoteException e) {
				util.Logger.singleton().error(e);
			}
			numRec = rallyItersResult.getTotalResultCount();
			for (int i = 0; i < numRec; i++) {
				rallyIterObj = (com.rallydev.webservice.v1_02.domain.Iteration) rallyItersResult.getResults()[i];
				rallyIterList.add(rallyIterObj);
			}
			startIndex += pageSize;
		} while (numRec == pageSize);

	}
   
	public void loadStoriesFromRallyProject(com.rallydev.webservice.v1_02.domain.Project rProj){

		long numRec = 0;
		int startIndex = 1;
		com.rallydev.webservice.v1_02.domain.HierarchicalRequirement rallyStoryObj;

		do {
			QueryResult rallyStoryResult = null;
			try {
				rallyStoryResult = service.query(WORKSPACE,
						rProj, false, false, USERSTORY, "", "", true,
						startIndex, pageSize);
			} catch (RemoteException e) {
				util.Logger.singleton().error(e);
			}

			numRec = rallyStoryResult.getTotalResultCount();
			for (int i = 0; i < numRec; i++) {
				rallyStoryObj = (com.rallydev.webservice.v1_02.domain.HierarchicalRequirement) rallyStoryResult.getResults()[i];
				rallyStoryList.add(rallyStoryObj);
			}
			startIndex += pageSize;
		} while (numRec == pageSize);
	}
   
   

	
	public void deleteStoryFromCashedList(StoryCard sc){
		com.rallydev.webservice.v1_02.domain.HierarchicalRequirement rallyStoryObj;
		StoryCard convertedrs = null;
		for(int i=0; i<rallyStoryList.size();i++){
			if ( sc.getId() == rallyStoryList.get(i).getObjectID() )
				rallyStoryList.remove(i);
		}
	}
	
	public boolean isStoryPresentApBl(StoryCard sc){
		for(StoryCard scAP: storyCardsListFromBacklog ){
			if(scAP.getId() == sc.getId()){
				return true;
			}
		}
		return false;
	}
	
	public boolean isStoryPresentAp(StoryCard sc,Iteration iter){

		List<StoryCard> storyCardsListFromiteration = new ArrayList();
		storyCardsListFromiteration = iter.getStoryCardChildren();

		for(StoryCard scAP: storyCardsListFromiteration )
		{
			if(scAP.getId() == sc.getId())
				return true;
		}
		return false;
	}
	
	public boolean isItrPresentAp(Iteration itr){
		for(Iteration scItr: iterationsListFromProject){
			if(scItr.getId() == itr.getId())
				return true;
		}
		return false;	
	}
	
	public boolean isItrPresentAtRally(Iteration itr){
		for(com.rallydev.webservice.v1_02.domain.Iteration rallyIterationObj : rallyIterList){
			if(itr.getId() == rallyIterationObj.getObjectID()){
				return true;
			}
		}
		return false;
	}

	public boolean isPresentAtRally(StoryCard sc){

		for(com.rallydev.webservice.v1_02.domain.HierarchicalRequirement rallyStoryObj : rallyStoryList){
			if(sc.getId() == rallyStoryObj.getObjectID()){
				return true;
			}
		}
		return false;
	}
	public persister.data.Project getAPPrjObj() {
		return APPrjObj;
	}

	public void setAPPrjObj(persister.data.Project prjObj) {
		APPrjObj = prjObj;
	}

}

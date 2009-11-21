package persister;

import java.sql.Timestamp;
import java.util.List;

import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;

/**
 * @author webers
 * 
 * NOTICE: IDs of all PlanningObjects (StoryCards, Iterations, Backlog etc.)
 * have to be globally UNIQUE as they are used to identify these objects in
 * AgilePlanner.
 * 
 */
public interface AsynchronousPersister {

    /***************************************************************************
     * CONNECTION *
     **************************************************************************/
    /**
     * returns if a connection already exists or not
     */
    public boolean connected() throws ConnectionFailedException;

    /**
     * Connects to a project The implementation class decides if the project
     * resides on the local machine or on a server Expected result: asynchronous
     * callback PlannerDataChangeListener>createdProject
     * 
     * @throws ConnectionFailedException
     */
    public void connect() throws ConnectionFailedException;

    /**
     * Disconnect from persister
     * 
     * @return true is connection was broken
     */
    public boolean disconnect();

    /***************************************************************************
     * LOAD AND SAVE *
     **************************************************************************/
    /**
     * send a message to the persister to load a project with name projectName
     * if the project does not yet exist, the persister needs to create it
     * Expected result: asynchronous callback
     * PlannerDataChangeListener>createdProject
     */
    public void load(String projectName) throws NotConnectedException, CouldNotLoadProjectException;

    /**
     * send a message to the persister to load a project with name projectName
     * only load iterations that end after start time and end before end time
     * Expected result: asynchronous callback
     * PlannerDataChangeListener>createdProject
     */
    public void load(String projectName, Timestamp start, Timestamp end) throws NotConnectedException, CouldNotLoadProjectException;

    /*
     * sends a message to get file from the path specified Expected result:
     * asynchronous callback PlannerDataChangeListener>
     */
    public void downloadFile(String path, int recognizeID) throws NotConnectedException;

    /*
     * sends a message to send file from the path specified Expected result:
     * asynchronous callback PlannerDataChangeListener>
     */
    public void uploadFile(String path,int recognizeID) throws NotConnectedException;

    /**
     * send a message to the persister get all project names in the default
     * location Expected result: asynchronous callback
     * PlannerDataChangeListener>gotProjectNames
     */
    public void getProjectNames() throws NotConnectedException;

    /***************************************************************************
     * CREATE *
     **************************************************************************/

    /**
     * Expected result: asynchronous callback
     * PlannerDataChangeListener>createdBacklog
     * 
     * @throws ForbiddenOperationException
     */
    public void createBacklog(int width, int height, int locationX, int locationY) throws NotConnectedException, ForbiddenOperationException;

    /**
     * Expected result: asynchronous callback
     * PlannerDataChangeListener>createdIteration
     */
    public void createIteration(String name, String description, int width, int height, int locationX, int locationY, float availableEffort,
            Timestamp startDate, Timestamp endDate,float rotationAngle,boolean rallyID) throws NotConnectedException;

    /**
     * Expected result: asynchronous callback
     * PlannerDataChangeListener>createdStoryCard
     * 
     * @throws
     */
    public void createStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String str, String color, String cardOwner, float rotationAngle, boolean rallyID, String fitId) throws NotConnectedException,
            IndexCardNotFoundException;

    /***************************************************************************
     * DELETE *
     **************************************************************************/

    /**
     * BAcklog and all stories in it need to be deleted Expected result:
     * asynchronous callback PlannerDataChangeListener>deletedBacklog
     */
    public void deleteBacklog(long id) throws NotConnectedException, ForbiddenOperationException;

    /**
     * Iteration and included story cards need to be deleted Expected result:
     * asynchronous callback PlannerDataChangeListener>deletedIteration
     */
    public void deleteIteration(long id) throws NotConnectedException, IndexCardNotFoundException;

    /**
     * StoryCard needs to be deleted Expected result: asynchronous callback
     * PlannerDataChangeListener>deletedStoryCard
     */
    public void deleteStoryCard(long id) throws NotConnectedException, IndexCardNotFoundException;
    public void deleteOwner(TeamMember teamMember) throws NotConnectedException;

    /***************************************************************************
     * UNDELETE *
     **************************************************************************/

    /**
     * Iteration object and all its story cards are added to the persister
     * Expected result: asynchronous callback
     * PlannerDataChangeListener>undeletedIteration
     * 
     * @param iteration
     *            object with all included story cards
     * @throws ForbiddenOperationException
     * @throws
     */
    public void undeleteIteration(Iteration iteration) throws NotConnectedException, IndexCardNotFoundException, ForbiddenOperationException;

    /**
     * story card is added to the persister Expected result: asynchronous
     * callback PlannerDataChangeListener>undeletedStoryCard
     * 
     * @param story
     *            card object with all included story cards
     * @throws ForbiddenOperationException
     */
    public void undeleteStoryCard(StoryCard sc) throws NotConnectedException, IndexCardNotFoundException, ForbiddenOperationException;

    /***************************************************************************
     * UPDATE BACKLOG *
     * 
     * @throws
     **************************************************************************/
    public void updateBacklog(Backlog b) throws NotConnectedException, IndexCardNotFoundException;
    public void updateLegend(Legend legend) throws NotConnectedException;
    public void updateOwner(TeamMember teamMember) throws NotConnectedException;

    /***************************************************************************
     * UPDATE STORYCARD *
     * 
     * @throws
     **************************************************************************/
    public void updateStoryCard(StoryCard sc) throws NotConnectedException, IndexCardNotFoundException;

    /***************************************************************************
     * MOVE STORYCARD BETWEEN PARENTS *
     * 
     * @throws
     **************************************************************************/
    public void moveStoryCardToNewParent(StoryCard sc, long newparentid, int locationX, int locationY, float rotation) throws NotConnectedException,
            IndexCardNotFoundException;

    /***************************************************************************
     * UPDATE ITERATION *
     * 
     * @throws
     **************************************************************************/
    public void updateIteration(Iteration iteration) throws NotConnectedException, IndexCardNotFoundException;

    /***************************************************************************
     * UPDATE PROJECT *
     **************************************************************************/

    // public void updateProjectName(long id, String name) throws
    // NotConnectedException, IndexCardNotFoundException;
    /***************************************************************************
     * LIVE TEXT UPDATE *
     **************************************************************************/
    public void sendLiveTextUpdate(IndexCardLiveUpdate data);

    /***************************************************************************
     * LISTENER *
     **************************************************************************/
    public void addPlannerDataChangeListener(PlannerDataChangeListener listener);
    public void removePlannerDataChangeListener(PlannerDataChangeListener listener);
 
    /**************************************************************************
     * REARRANGE PROJECT *
     ***************************************************************************/
    public void updateProject(Project project);
	public void arrangeProject(Project project);
	public void createProject(String ProjectName) throws NotConnectedException,ForbiddenOperationException;
	public void synchronizedProject();
	public void login(String userName, String password, String url) throws NotConnectedException;
	public void connectToRally(String string);
    
         
}

package persister;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import persister.data.Backlog;
import persister.data.IndexCard;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;

public interface SynchronousPersister {

    /***************************************************************************
     * LOAD AND SAVE *
     * 
     **************************************************************************/

    public Project load(String projectName) throws CouldNotLoadProjectException,RemoteException;
    public Project load(String projectName, Timestamp start, Timestamp end) throws CouldNotLoadProjectException,RemoteException;
    public Project getProject();
    public List<String> getProjectNames();
    public boolean writeFile(String fileName, byte[] fileContents,int recognizeID);
    public byte[] readFile(String fileName,int recognizeID);
    public String[][] getIterationNames(String projectName);

    /***************************************************************************
     * CREATE *
     **************************************************************************/

    public Project createProject(String name);
    public Backlog createBacklog(int width, int height, int locationX, int locationY) throws ForbiddenOperationException;
    public Iteration createIteration(String name, String description, int width, int height, int locationX, int locationY, float availableEffort,
            Timestamp startDate, Timestamp endDate, float rotationAngle,boolean rallyID);
    public TeamMember createOwner(String name);
    public StoryCard createStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, String cardOwner, float rotationAngle,boolean rallyID,String fitId)
            throws IndexCardNotFoundException;
    public StoryCard createTabletPCStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, byte[] image,boolean rallyID)
            throws IndexCardNotFoundException;
    /***************************************************************************
     * DELETE & UNDELETE *
     * 
     **************************************************************************/
    public IndexCard deleteCard(long id) throws IndexCardNotFoundException, ForbiddenOperationException;
    public TeamMember deleteOwner(TeamMember teamMember);
    public IndexCard undeleteCard(IndexCard indexCard) throws IndexCardNotFoundException, ForbiddenOperationException;

    /***************************************************************************
     * UPDATE *
     * 
     * @throws
     * 
     **************************************************************************/
    public IndexCard updateCard(IndexCard indexCard) throws IndexCardNotFoundException;
    public Legend updateLegend(Legend legend);
    public TeamMember updateOwner(TeamMember teamMember);

    /***************************************************************************
     * MOVE STORYCARD BETWEEN PARENTS *
     * 
     * @throws
     * 
     **************************************************************************/
    public StoryCard moveStoryCardToNewParent(long id, long oldparentid, long newparentid, int locationX, int locationY, float rotation)
            throws IndexCardNotFoundException;

    /***************************************************************************
     * Find objects by id
     **************************************************************************/
    public IndexCard findCard(long id) throws IndexCardNotFoundException;

    /**
     * Arrange Project
     */
    public Project arrangeProject(Project project2);
    public void setNewPersister(String projectName) throws RemoteException;
    public Project synchronizeProject(String projectName) throws CouldNotLoadProjectException;
    public boolean deleteProject(String projectName)throws ProjectNotFoundException;

}

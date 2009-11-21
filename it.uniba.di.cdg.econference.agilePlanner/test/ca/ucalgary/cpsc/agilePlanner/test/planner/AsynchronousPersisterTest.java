package ca.ucalgary.cpsc.agilePlanner.test.planner;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import persister.AsynchronousPersister;
import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.PlannerDataChangeListener;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.LegendDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.local.AsynchronousLocalPersister;



public class AsynchronousPersisterTest extends TestCase implements PlannerDataChangeListener {

	private AsynchronousLocalPersister asynchPers;

    private Object callbackReceived;

    private String testMethod;

    /***************************************************************************
     * SETUP *
     **************************************************************************/

    
    String projectDir =  System.getProperty("user.dir") + File.separator + "TestDirectory";

    //Get's ignored, cause TestCase doesn't support it
    @BeforeClass
    public void setUpBeforeClass(){
      File file = new File( projectDir + File.separator + "ThisProjectShouldNotExist.xml");
      if(file.exists())
      {
          file.delete();
      }
    }
    
    @Before
    public void setUp() {
        try {
            ResetFiles.resetProjectFiles();
            asynchPers = new AsynchronousLocalPersister(projectDir, "ProjectFile");
        }
        catch (Exception e) {
            Assert.fail("Could not create an instance of AsynchronousLocalPersister");
        }
        asynchPers.addPlannerDataChangeListener(this);
    }

    @After
    public void tearDown() {
        asynchPers.removePlannerDataChangeListener(this);
    }

    /***************************************************************************
     * CONNECTION *
     **************************************************************************/

    @org.junit.Test
    public void testConnect() {
        callbackReceived = null;
        testMethod = "testConnect";
        asynchPers.connect();
        Assert.assertEquals(callbackReceived, "createdProject");
        testMethod = null;
    }

    @org.junit.Test
    public void testGetProjectNames() {
        callbackReceived = null;
        asynchPers.getProjectNames();
        Assert.assertEquals(callbackReceived, "gotProjectNames");
    }

    public void gotProjectNames(List<String> str) {
        callbackReceived = "gotProjectNames";
        List<String> testList = new ArrayList<String>();
        testList.add("EmptyProject");
        testList.add("ProjectFile");
        testList.add("ProjectWithBacklogIterationAndStoryCards");
        testList.add("ThisProjectShouldNotExist");
        for (String projName:str){
        	assertTrue("The obtained project name is not in List.",testList.contains(projName));
        }
//        assertEquals("Line 94: List(0) contains: " + str.get(0),"EmptyProject",str.get(0));
//        assertEquals("Line 95: List(1) contains: " + str.get(1),"ProjectFile",str.get(1));
//        assertEquals("Line 96: List(2) contains: " + str.get(2),"ProjectWithBacklogIterationAndStoryCards",str.get(2));
//        if (str.size() == 4) assertEquals("ThisProjectShouldNotExist",str.get(3));
        //Assert.assertTrue("List of project names has unexpected length", str.size() == 3);
    }

    @org.junit.Test
    public void testLoad() {
        testMethod = "testLoad";
        callbackReceived = null;
        try {
            asynchPers.load("ProjectWithBacklogIterationAndStoryCards");
        }
        catch (CouldNotLoadProjectException e) {
            fail("Failed in AsynchronousPersisterTest>testLoad");
        }
        assertEquals(callbackReceived, "createdProject");
        testMethod = null;
    }

    
    @org.junit.Test
    public void testLoadCreateNotExistingProject() {
        testMethod = "testLoadCreateNotExistingProject";
        callbackReceived = null;
        
        try {
            String notExistingProjectName = "ThisProjectShouldNotExist";
                       
            File file = new File( projectDir + File.separator + notExistingProjectName + ".xml");
            if(file.exists()){
                file.delete();
            }
            
            asynchPers.load(notExistingProjectName);
                                    
            assertEquals(callbackReceived, "createdProject");
            
            asynchPers = new AsynchronousLocalPersister(projectDir, "ProjectFile");
            
            Thread.sleep(100);
            //file = new File( projectDir + File.separator + notExistingProjectName + ".xml");
            assertTrue(file.exists());
            //assertTrue("Couldn't delete file " + file.getCanonicalFile() + "\nProbably another handle exists.", file.delete());
            
        }
        catch (Exception e) {
            fail("Failed in AsynchronousPersisterTest>testLoad");
        }

    }

    public void createdProjectOnInitialLoadFromServer(Project p) {
        callbackReceived = "createdProject";
        if (testMethod.equals("testConnect"))
            assertEquals(testMethod, "testConnect");
        else {
            // here testMethod should be testLoadProject
            // assertEquals(testMethod, "testLoad");
            if (testMethod.equals("testLoad")) {
                assertEquals(testMethod, "testLoad");
                List<StoryCard> backlogStories = p.getBacklog().getStoryCardChildren();
                List<Iteration> iterations = p.getIterationChildren();
                assertTrue("Incorrect number os stories in backlog", backlogStories.size() == 1);
                assertTrue("Incorrect number os stories in backlog", iterations.size() == 1);
                List<StoryCard> iterationStories = p.getIterationChildren().iterator().next().getStoryCardChildren();
                assertTrue("Incorrect number os stories in backlog", iterationStories.size() == 1);    
            }

            
        }
    }

    /***************************************************************************
     * StoryCard Methods *
     **************************************************************************/

    @org.junit.Test
    public void testUndeleteIteration() {
        Iteration iteration = new IterationDataObject();
        iteration.setId(905);
        iteration.setParent(1);
        iteration.setName("undeletedIteration");

        callbackReceived = null;
        testMethod = "testUndeleteIteration";
        try {
            asynchPers.undeleteIteration(iteration);
        }
        catch (ForbiddenOperationException e) {
            fail("Failed in AsynchronousPersieterTest>testUndeleteIteration");
        }
        assertEquals("undeletedIteration", callbackReceived);
        testMethod = null;
    }

    public void undeletedIteration(Iteration iteration) {
        callbackReceived = "undeletedIteration";
        assertEquals(iteration.getId(), (long) 905);
        assertEquals(iteration.getName(), "undeletedIteration");
    }

    /** ****************************************************************** */
    @org.junit.Test
    public void testCreateBacklog() {
        AsynchronousPersister asynchPers1 = null;
        try {
            asynchPers1 = new AsynchronousLocalPersister(System.getProperty("user.dir") + File.separator + "TestDirectory", "EmptyProject");
            asynchPers1.addPlannerDataChangeListener(this);

            callbackReceived = null;
            testMethod = "testCreateBacklog";

            asynchPers1.createBacklog(25, 25, 25, 25);
        }catch (CouldNotLoadProjectException e) {
            fail("Failed in AsynchronousPersisterTest>testCreateBacklog - CouldNotLoadProjectException");
        }catch (ConnectionFailedException e) {
            fail("Failed in AsynchronousPersisterTest>testCreateBacklog - ConnectionFailedException");
        }catch (NotConnectedException e) {
            fail("Failed in AsynchronousPersisterTest>testCreateBacklog - NotConnectedException");
        }catch (ForbiddenOperationException e) {
            fail("Failed in AsynchronousPersisterTest>testCreateBacklog - ForbiddenOperationException");
        }
        assertEquals("createdBacklog", callbackReceived);
        testMethod = null;

    }

    public void createdBacklog(Backlog backlog) {
        callbackReceived = "createdBacklog";
        assertEquals((long) 3, backlog.getId());
        assertEquals("Project Backlog", backlog.getName());
    }

    /** ******************************************************************* */

    @org.junit.Test
    public void testMoveStoryCardToNewParent() {
        AsynchronousLocalPersister asynchPers1 = null;

        try {
            asynchPers1 = new AsynchronousLocalPersister(System.getProperty("user.dir") + File.separator + "TestDirectory",
                    "ProjectWithBacklogIterationAndStoryCards");
            asynchPers1.addPlannerDataChangeListener(this);
            callbackReceived = null;
            testMethod = "testMoveStoryCardToNewParent";
            StoryCard sc = (StoryCard) asynchPers1.getSynPer().findCard(6);
            asynchPers1.moveStoryCardToNewParent(sc, 4, 50, 50,0);
            assertTrue("Card moved sucessfully", (((StoryCard) (asynchPers1.getSynPer().findCard(6))).getParent() == 4));
        }
        catch (Exception e) {
            fail("Failed in AsynchronousPersisterTest>testMoveStoryCardToNewParent");
        }
        assertEquals("movedStoryCardToNewParent", callbackReceived);
        testMethod = null;
    }

    public void movedStoryCardToNewParent(StoryCard storycard) {
        callbackReceived = "movedStoryCardToNewParent";
        assertEquals(storycard.getId(), (long) 6);
        assertEquals(storycard.getParent(), (long) 4);
    }

    /** ********************************************************************************** */

    @org.junit.Test
    public void testCreateStoryCard() {
        callbackReceived = null;
        testMethod = "testCreateStoryCard";

        asynchPers.createIteration("iter", "testCreateIterationAsyn", 44, 55, 66, 77, 3, new Timestamp(0), new Timestamp(500000),0,false);
        try {
            asynchPers.createStoryCard("sc", "testCreateStoryCardAsyn", 22, 33, 44, 55, 2, 3, 4, 7, 3, "P","", "",0,false,StoryCard.DEFAULT_FITID);

        }
        catch (IndexCardNotFoundException e) {
            fail("Failed in AsynchronousPersisterTest>testCreateStoryCard");
        }
        assertEquals("createdStoryCard", callbackReceived);
        testMethod = null;
    }

    public void createdStoryCard(StoryCard storycard) {
        callbackReceived = "createdStoryCard";
        assertEquals(storycard.getName(), "sc");
    }

    /** ********************************************************************* */

    @org.junit.Test
    public void testCreateIteration() {
        callbackReceived = null;
        testMethod = "testCreateIteration";

        asynchPers.createIteration("iter", "testCreateIterationAsyn", 44, 55, 66, 77, 3, new Timestamp(0), new Timestamp(500000),0,false);

        assertEquals("createdIteration", callbackReceived);
        testMethod = null;
    }

    public void createdIteration(Iteration iteration) {
        callbackReceived = "createdIteration";
 
        if (testMethod.equals("testloadProjectAndCreateIteration")) {
            //assertTrue(iteration.getId() == 6);
            assertEquals(11L, iteration.getId());
            assertTrue(iteration.getParent() == 1);
        }
        else {
            assertEquals(iteration.getName(), "iter");
        }
    }

    /** *********************************************************************************** */

    @org.junit.Test
    public void testDeleteBacklog() {
        callbackReceived = null;
        testMethod = "testDeleteBacklog";

        try {
            asynchPers.deleteBacklog(2);
        }
        catch (ForbiddenOperationException e) {
            assertTrue("Backlog cannot be deleted", true);
            // e.printStackTrace();
        }
        testMethod = null;
    }

    public void deletedBacklog(long id) {
        assertTrue("Deleteing a backlog is forbidden but happened", false);
    }

    /** *************************************************************************** */

    @org.junit.Test
    public void testDeleteIteration() {
        AsynchronousLocalPersister asynchPers1 = null;
        try {
            asynchPers1 = new AsynchronousLocalPersister(System.getProperty("user.dir") + File.separator + "TestDirectory",
                    "ProjectWithBacklogIterationAndStoryCards");
            asynchPers1.addPlannerDataChangeListener(this);
            callbackReceived = null;
            testMethod = "testDeleteIteration";
            asynchPers1.deleteIteration(4);
        }
        catch (Exception e) {
            fail("Failed in AsynchronousPersisterTest>testDeleteIteration");
        }

        assertEquals("deletedIteration", callbackReceived);
        testMethod = null;
    }

    public void deletedIteration(long id) {
        callbackReceived = "deletedIteration";
        assertEquals(4, 4);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testDeleteStoryCard() {

        AsynchronousLocalPersister asynchPers1 = null;
        try {
            asynchPers1 = new AsynchronousLocalPersister(System.getProperty("user.dir") + File.separator + "TestDirectory",
                    "ProjectWithBacklogIterationAndStoryCards");

            asynchPers1.addPlannerDataChangeListener(this);
            callbackReceived = null;
            testMethod = "testDeleteStoryCard";

            asynchPers1.deleteStoryCard(6);
        }
        catch (Exception e) {
            fail("Failed in AsynchronousPersisterTest>testDeleteStoryCard");
        }
        assertEquals("deletedStoryCard", callbackReceived);
        testMethod = null;
    }

    public void deletedStoryCard(long id) {

        callbackReceived = "deletedStoryCard";
        assertEquals(6, 6);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testUndeleteStoryCard() {
        callbackReceived = null;
        testMethod = "testUndeleteStoryCard";

        StoryCard sc = new StoryCardDataObject();
        sc.setId(1000);
        sc.setParent(2);
        sc.setName("undeletedStoryCard");

        try {
            asynchPers.undeleteStoryCard(sc);
        }
        catch (Exception e) {
            fail("Failed in AsynchronousPersisterTest>testUndeleteStoryCard");
        }
        assertEquals("undeletedStoryCard", callbackReceived);
        testMethod = null;
    }

    public void undeletedStoryCard(StoryCard storyCard) {
        callbackReceived = "undeletedStoryCard";
        assertEquals(storyCard.getId(), (long) 1000);
        assertEquals(storyCard.getParent(), (long) 2);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testUpdateBacklog() {
        callbackReceived = null;
        testMethod = "testUpdateBacklog";

        Backlog b = new BacklogDataObject(11, 11, 11, 11);
        // b.setParent(1);
        try {
            asynchPers.updateBacklog(b);
        }
        catch (IndexCardNotFoundException e) {
            fail("Failed in AsynchronousPersisterTest>testUpdateBacklog");
        }

        assertEquals("updatedBacklog", callbackReceived);
        testMethod = null;
    }

    public void updatedBacklog(Backlog backlog) {
        callbackReceived = "updatedBacklog";
        assertEquals(backlog.getLocationX(), 11);
    }

    /** *************************************************************** */
    @org.junit.Test
    public void testUpdateIteration() {

        AsynchronousLocalPersister asynchPers1 = null;
        try {
            asynchPers1 = new AsynchronousLocalPersister(System.getProperty("user.dir") + File.separator + "TestDirectory",
                    "ProjectWithBacklogIterationAndStoryCards");

            asynchPers1.addPlannerDataChangeListener(this);
            callbackReceived = null;
            testMethod = "testUpdateStoryCard";

            Iteration i = new IterationDataObject("updateIteration", "description empty", 200, 100, 401, 500, (float) 55.0, new Timestamp(0),
                    new Timestamp(500000),0,false);
            i.setId(4);
            i.setParent(1);
            asynchPers1.updateIteration(i);
        }
        catch (Exception e) {
            fail("Failed in AsynchronousPersisterTest>testUpdateIteration");
        }
        assertEquals("updatedIteration", callbackReceived);
        testMethod = null;
    }

    public void updatedIteration(Iteration iteration) {
        callbackReceived = "updatedIteration";
        assertEquals(iteration.getWidth(), 200);
    }

    /** *********************************************************************** */

    @org.junit.Test
    public void testUpdateStoryCard() {
        AsynchronousLocalPersister asynchPers1 = null;
        try {
            asynchPers1 = new AsynchronousLocalPersister(System.getProperty("user.dir") + File.separator + "TestDirectory",
                    "ProjectWithBacklogIterationAndStoryCards");

            asynchPers1.addPlannerDataChangeListener(this);
            callbackReceived = null;
            testMethod = "testUpdateStoryCard";

            StoryCard sc = new StoryCardDataObject(2, "testUpdateStoryCard", "", 43, 22, 22, 22, 3, 3, 3, 3, "P","","",0,false, StoryCard.DEFAULT_FITID);
            sc.setId(6);
            asynchPers1.updateStoryCard(sc);
        }
        catch (Exception e) {
            fail("Failed in AsynchronousPersisterTest>testUpdateStoryCard");
        }
        assertEquals("updatedStoryCard", callbackReceived);
        testMethod = null;
    }

    public void updatedStoryCard(StoryCard storyCard) {

        callbackReceived = "updatedStoryCard";
        assertEquals(storyCard.getHeight(), 22);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testloadProjectAndCreateIteration() {
        callbackReceived = null;
        testMethod = "testloadProjectAndCreateIteration";

        try {
            asynchPers.load("ProjectWithBacklogIterationAndStoryCards");
        }
        catch (CouldNotLoadProjectException e) {
            fail("Failed in AsynchronousPersisterTest>testloadProjectAndCreatIteration");
        }

        asynchPers.createIteration("iter", "testCreateIterationAsyn", 44, 55, 66, 77, 3, new Timestamp(0), new Timestamp(600000),0,false);
        assertEquals("createdIteration", callbackReceived);
        testMethod = null;
    }

    /** ********************************************************************* */

    /** ********************************************************************* */
    public void asynchronousException(Exception exception, int messageType) {
        fail("Unexpected exception was raised: " + exception.getMessage() + "\n" + "in message type " + messageType);
    }

    public void downloadedFile(boolean bool) {}
    public void uploadedFile(boolean bool) {}
	public void arrangeProject(Project project) {}
	public void createProjectOnSubsequentLoadsFromServer(Project project) {}
	public void gotProjectNamesForLoginEvent(List<String> list) {}
	public void lostConnectionEvent() {}
	public void updatedLegendEvent() {}
	public void updatedLegend(LegendDataObject leg) {}
	public void updatedLegend(Legend leg) {}
	public void updatedOwner(TeamMember teamMember) {}
	public void createdOwner(TeamMember teamMember) {}
    public void deletedOwner(TeamMember teamMember) {}

	
}

package ca.ucalgary.cpsc.agilePlanner.test.planner;

import java.io.File;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;


import org.junit.AfterClass;
import org.junit.Assert;

import org.junit.BeforeClass;

import persister.ConnectionFailedException;
import persister.ForbiddenOperationException;
import persister.IndexCardLiveUpdate;
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
import persister.distributed.ClientCommunicator;
import persister.distributed.ServerCommunicator;







public class AsynchronousDistributedPersisterTest extends TestCase implements PlannerDataChangeListener 
{


public void deletedOwner(TeamMember teamMember) {
		// TODO Auto-generated method stub
		
	}

	//
    private ServerCommunicator server, server5051, server5052;

    private ClientCommunicator asynchPers, asynchPers5051EmptyFile, asynchPers5052CompleteFile;

    private Object callbackReceived;

    private String testMethod;
//
    public static Test suite() {
        return new JUnit4TestAdapter(AsynchronousDistributedPersisterTest.class);
    }
//
//    /***************************************************************************
//     * SETUP *
//     **************************************************************************/
//
    public void init() {
        try {
            ResetFiles.resetProjectFiles();
            server = new ServerCommunicator("." + File.separator + "TestDirectory", "ProjectFile", 5054,"","");
            server5051 = new ServerCommunicator("." + File.separator + "TestDirectory", "EmptyProject", 5055,"","");
            server5052 = new ServerCommunicator("." + File.separator + "TestDirectory", "ProjectWithBacklogIterationAndStoryCards", 5056,"","");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
//
    public void shutdownServer() {
        server.shutDown();
        server5051.shutDown();
        server5052.shutDown();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
    }
//
    @BeforeClass
    public void setUp() {
        try {
            init();
            Thread.sleep(1000);
            asynchPers = new ClientCommunicator("localhost", 5054);
            asynchPers.addPlannerDataChangeListener(this);
     //       Thread.sleep(2000);

            asynchPers5051EmptyFile = new ClientCommunicator("localhost", 5055);
            asynchPers5051EmptyFile.addPlannerDataChangeListener(this);
     //       Thread.sleep(2000);
            asynchPers5052CompleteFile = new ClientCommunicator("localhost", 5056);
            asynchPers5052CompleteFile.addPlannerDataChangeListener(this);
     //       Thread.sleep(2000);
        }
        catch (Exception e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>setup");
        }
    }
//
    @AfterClass
    public void tearDown() {
        asynchPers.removePlannerDataChangeListener(this);
        asynchPers5051EmptyFile.removePlannerDataChangeListener(this);
        asynchPers5052CompleteFile.removePlannerDataChangeListener(this);

        shutdownServer();
    }
//
//    /***************************************************************************
//     * CONNECTION *
//     **************************************************************************/
//
//    @org.junit.Test
//    public void testConnect() {
//        callbackReceived = null;
//        testMethod = "testConnect";
//        try {
//            asynchPers.connect();
//
//            try {
//                Thread.sleep(1500);
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            Assert.assertEquals("createdProject", callbackReceived);
//            testMethod = null;
//        }
//        catch (ConnectionFailedException e) {
//            Assert.fail("Connection Failed");
//        }
//    }
    
    

    public void createdProjectOnInitialLoadFromServer(Project p) {
        // project = p;
        callbackReceived = "createdProject";
        if (testMethod.equals("testConnect")) {
            Assert.assertEquals(testMethod, "testConnect");
            Assert.assertEquals(p.getName(), "ProjectFile");
        }
        else
            if (testMethod.equals("testMoveStoryCardToNewParent")) {
                Assert.assertEquals(testMethod, "testMoveStoryCardToNewParent");
            }
            else {
                if (testMethod.equals("testLoad")) {
                    Assert.assertEquals(testMethod, "testLoad");
                    List<StoryCard> backlogStories = p.getBacklog().getStoryCardChildren();
                    List<Iteration> iterations = p.getIterationChildren();
                    Assert.assertTrue("Incorrect number os stories in backlog", backlogStories.size() == 1);
                    Assert.assertTrue("Incorrect number os stories in backlog", iterations.size() == 1);
                    List<StoryCard> iterationStories = p.getIterationChildren().iterator().next().getStoryCardChildren();
                    Assert.assertTrue("Incorrect number os stories in backlog", iterationStories.size() == 1);
                }
            }
    }

    /** ************************************************************************* */

//   @org.junit.Test
//    public void testLoad() {
//        testMethod = "testLoad";
//        callbackReceived = null;
//        try {
//            asynchPers.load("ProjectWithBacklogIterationAndStoryCards");
//            try {
//                Thread.sleep(25000);
//            }
//            catch (InterruptedException e) {
//                Assert.fail("Failed in AsynchronousDistributedPersisterTest>testLoad");
//            }
//        }
//        catch (NotConnectedException e) {
//            Assert.fail("Not Connected ");
//        }
//        Assert.assertEquals("createdProject", callbackReceived);
//        testMethod = null;
//    }

    @org.junit.Test
    public void testLoadCreateNotExistingProject() {
        testMethod = "testLoadCreateNotExistingProject";
        callbackReceived = null;

        try {
            String notExistingProjectName = "ThisProjectShouldNotExist";

            File file = new File("." + File.separator + "TestDirectory" + File.separator + notExistingProjectName + ".xml");

            if (file.exists()) {
                file.delete();
            }

            asynchPers.load(notExistingProjectName);

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Assert.fail("Failed in AsynchronousDistributedPersisterTest>testLoad");
            }
            Assert.assertEquals("createdProject", callbackReceived);

            file = new File("." + File.separator + "TestDirectory" + File.separator + notExistingProjectName + ".xml");
            Assert.assertTrue(file.exists());
            assertTrue(file.delete());
            

        }
        catch (Exception e) {
            Assert.fail("Failed in AsynchronousPersisterTest>testLoad");
        }

    }

    /** ********************************************************************** */

    @org.junit.Test
    public void testGetProjectNames() throws Exception {
        testMethod = "testGetProjectNames";
        callbackReceived = null;
        try {
            asynchPers.getProjectNames();
            try {
                Thread.sleep(1500);
            }
            catch (InterruptedException e) {
                Assert.fail("Failed in AsynchronousDistributedPersisterTest>stestGetProjectNames");
            }

        }
        catch (NotConnectedException e) {
            Assert.fail("testProjectnames failed due to NotConnectedException");
        }
        Assert.assertEquals("gotProjectNames", callbackReceived);
        testMethod = null;
    }

    public void gotProjectNames(List<String> str) {
    	System.out.println("reach get project names");
        callbackReceived = "gotProjectNames";
        List<String> testList = new ArrayList<String>();
        testList.add("EmptyProject");
        testList.add("ProjectFile");
        testList.add("ProjectWithBacklogIterationAndStoryCards");
        testList.add("ThisProjectShouldNotExist");
        for (String projName:str){
        	assertTrue("The obtained project name is not in List.",testList.contains(projName));
        }
//        
//        assertEquals("EmptyProject",str.get(0));
//        assertEquals("ProjectFile",str.get(1));
//        assertEquals("ProjectWithBacklogIterationAndStoryCards",str.get(2));
//        if (str.size() == 4) assertEquals("ThisProjectShouldNotExist",str.get(3));
        //Assert.assertTrue("List of project names has unexpected length", str.size() == 3);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testUndeleteIteration() throws Exception {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e2) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testUndeleteIteration");
        }
        Iteration iteration = new IterationDataObject();
        iteration.setId(905);
        iteration.setParent(1);
        iteration.setName("undeletedIteration");

        callbackReceived = null;
        testMethod = "testUndeleteIteration";
        try {
            asynchPers.undeleteIteration(iteration);
            // helper.testUndeleteIteration();
        }
        catch (NotConnectedException e1) {
            Assert.fail("Not Connected ");
        }
        catch (IndexCardNotFoundException e1) {
            Assert.fail("IndexCard Not Found");
        }

        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testUndeleteIteration");
        }

        Assert.assertEquals("undeletedIteration", callbackReceived);
        testMethod = null;
    }

    public void undeletedIteration(Iteration iteration) {
        callbackReceived = "undeletedIteration";
        Assert.assertEquals(iteration.getId(), (long) 905);
        Assert.assertEquals(iteration.getName(), "undeletedIteration");
    }

    //
    //	
    // /*********************************************************************/
    @org.junit.Test
    public void testCreateBacklog() throws Exception {

        callbackReceived = null;
        testMethod = "testCreateBacklog";

        try {
            asynchPers5051EmptyFile.createBacklog(25, 25, 25, 25);
        }
        catch (NotConnectedException e) {
            Assert.fail("Not Connected ");
        }
        catch (ForbiddenOperationException e) {
            Assert.fail("Forbidden Operation ");
        }

        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testCreateBacklog");
        }

        Assert.assertEquals("createdBacklog", callbackReceived);
        testMethod = null;

    }

    public void createdBacklog(Backlog backlog) {
        callbackReceived = "createdBacklog";
        Assert.assertEquals(backlog.getHeight(), 25);
        Assert.assertEquals(backlog.getWidth(), 25);
        Assert.assertEquals(backlog.getLocationX(), 25);
        Assert.assertEquals(backlog.getLocationY(), 25);

    }

    //	
    // /**********************************************************************/
    //	
    @org.junit.Test
    public void testMoveStoryCardToNewParent() throws Exception {

        callbackReceived = null;
        testMethod = "testMoveStoryCardToNewParent";

        try {
            try {
                StoryCard sc = new StoryCardDataObject();
                sc.setParent(2);
                sc.setId(6);

                asynchPers5052CompleteFile.moveStoryCardToNewParent(sc, 4, 50, 50,0);
            }
            catch (IndexCardNotFoundException e) {
                Assert.fail();
            }
        }
        catch (NotConnectedException e) {
            Assert.fail();
        }

        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testMoveStoryCardToNewParent");
        }
        Assert.assertEquals("movedStoryCardToNewParent", callbackReceived);
        testMethod = null;

    }

    
    public void movedStoryCardToNewParent(StoryCard storycard) {
        callbackReceived = "movedStoryCardToNewParent";
        Assert.assertEquals(storycard.getId(), (long) 6);
        Assert.assertEquals(storycard.getParent(), (long) 4);
    }

    // /*************************************************************************************/
    //	
    //	
    @org.junit.Test
    public void testCreateStoryCard() throws Exception {
        callbackReceived = null;
        testMethod = "testCreateStoryCard";

        try {
            try {
                asynchPers.createStoryCard("sc", "testCreateStoryCardAsyn", 22, 33, 44, 55, 2, 3, 4, 7, 3, "P", "", "",0,false,"");
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }

        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testCreateStoryCard");
        }

        Assert.assertEquals("createdStoryCard", callbackReceived);
        testMethod = null;
    }

    public void createdStoryCard(StoryCard storycard) {
        callbackReceived = "createdStoryCard";
        Assert.assertEquals(storycard.getName(), "sc");
    }

    //	
    // /************************************************************************/
    //	
    @org.junit.Test
    public void testCreateIteration() throws Exception {
    	System.out.println("test create story card");
        callbackReceived = null;
        testMethod = "testCreateIteration";

        try {
            asynchPers.createIteration("iter", "testCreateIterationAsyn", 44, 55, 66, 77, 3, new Timestamp(0), new Timestamp(500000),0,false);
        }
        catch (NotConnectedException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testCreateIteration");
        }
        Assert.assertEquals("createdIteration", callbackReceived);
        testMethod = null;
    }

    public void createdIteration(Iteration iteration) {
        callbackReceived = "createdIteration";
        Assert.assertEquals(iteration.getName(), "iter");
    }

    //
    // /**************************************************************************************/
    //	
    @org.junit.Test
    public void testDeleteBacklog() throws Exception {
        callbackReceived = null;
        testMethod = "testDeleteBacklog";

        try {
            try {
                asynchPers.deleteBacklog(2);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (ForbiddenOperationException e) {
            Assert.assertTrue("Backlog cannot be deleted", true);

        }
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testDeleteBacklog");
        }
        testMethod = null;
    }

    public void deletedBacklog(long id) {
        Assert.assertTrue("Deleteing a backlog is forbidden but happened", false);
    }

    // /******************************************************************************/

    @org.junit.Test
    public void testDeleteIteration() throws Exception {
     
        callbackReceived = null;
        testMethod = "testDeleteIteration";

        try {
            try {
                asynchPers5052CompleteFile.deleteIteration(4);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testDeleteIteration");
        }
        Assert.assertEquals("deletedIteration", callbackReceived);
        testMethod = null;
    }

    public void deletedIteration(long id) {

        callbackReceived = "deletedIteration";
        Assert.assertEquals(4, 4);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testDeleteStoryCard() throws Exception {
        callbackReceived = null;
        testMethod = "testDeleteStoryCard";

        try {
            try {
                asynchPers5052CompleteFile.deleteStoryCard(6);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testDeleteStoryCard");
        }
        Assert.assertEquals("deletedStoryCard", callbackReceived);
        testMethod = null;
    }

    public void deletedStoryCard(long id) {

        callbackReceived = "deletedStoryCard";
        Assert.assertEquals(id, (long) 6);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testUndeleteStoryCard() throws Exception {
        callbackReceived = null;
        testMethod = "testUndeleteStoryCard";

        StoryCard sc = new StoryCardDataObject();
        sc.setId(1000);
        sc.setParent(2);
        sc.setName("undeletedStoryCard");

        try {
            asynchPers.undeleteStoryCard(sc);
        }
        catch (NotConnectedException e) {
            Assert.fail();
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testUndeleteStoryCard");
        }
        Assert.assertEquals("undeletedStoryCard", callbackReceived);
        testMethod = null;
    }

    public void undeletedStoryCard(StoryCard storyCard) {
        callbackReceived = "undeletedStoryCard";
        Assert.assertEquals(storyCard.getId(), (long) 1000);
        Assert.assertEquals(storyCard.getParent(), (long) 2);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testUpdateBacklog() throws Exception {
        callbackReceived = null;
        testMethod = "testUpdateBacklog";

        Backlog b = new BacklogDataObject(11, 11, 11, 11);

        try {
            try {
                asynchPers.updateBacklog(b);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(5000);
        }
        catch (InterruptedException e) {
            Assert.fail();
        }

        Assert.assertEquals("updatedBacklog", callbackReceived);
        testMethod = null;
    }

    public void updatedBacklog(Backlog backlog) {
        callbackReceived = "updatedBacklog";
        Assert.assertEquals(backlog.getLocationX(), 11);
    }

    /** *************************************************************** */
    @org.junit.Test
    public void testUpdateIteration() throws Exception {
        callbackReceived = null;
        testMethod = "testUpdateStoryCard";

        Iteration i = new IterationDataObject("updateIteration", "description empty", 200, 100, 401, 500, (float) 55.0, new Timestamp(0),
                new Timestamp(500000),0,false);
        i.setId(4);
        i.setParent(1);
        try {
            try {
                asynchPers5052CompleteFile.updateIteration(i);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(8000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testUpdateIteration");
        }

        Assert.assertEquals("updatedIteration", callbackReceived);
        testMethod = null;
    }

    public void updatedIteration(Iteration iteration) {

        callbackReceived = "updatedIteration";
        Assert.assertEquals(iteration.getWidth(), 200);
        Assert.assertEquals(iteration.getHeight(), 100);
    }

    /** *********************************************************************** */

    @org.junit.Test
    public void testUpdateStoryCard() throws Exception {
        callbackReceived = null;
        testMethod = "testUpdateStoryCard";

        StoryCard sc = new StoryCardDataObject(2, "testUpdateStoryCard", "", 43, 22, 22, 22, 3, 3, 3, 3, "P","", "",0,false,"");
        sc.setId(6);
        try {
            try {
                asynchPers5052CompleteFile.updateStoryCard(sc);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(8000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testUpdateStoryCard");
        }
        Assert.assertEquals("updatedStoryCard", callbackReceived);
        testMethod = null;
    }

    public void updatedStoryCard(StoryCard storyCard) {

        callbackReceived = "updatedStoryCard";
        Assert.assertEquals(storyCard.getHeight(), 22);
        Assert.assertEquals(storyCard.getWidth(), 43);
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testTwoClientUpdateStoryCard() throws Exception {
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5056);
        callbackReceived = null;
        testMethod = "testTwoClientUpdateStoryCard";

        StoryCard sc = new StoryCardDataObject(2, "testTwoClientUpdateStoryCard", "", 43, 22, 22, 22, 3, 3, 3, 3, "P","", "",0,false,"");
        sc.setId(6);
        try {
            try {
                asynchPers5052CompleteFile.updateStoryCard(sc);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(25000);
        }
        catch (InterruptedException e) {
            Assert.fail();
        }
        Assert.assertEquals("updatedStoryCard", callbackReceived);
        Assert.assertEquals("updatedStoryCard", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("updatedStoryCard");
        Assert.assertTrue("Second client did not receive updateStoryCard", callbackOk);
        secondClient.processedCallback();
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testTwoClientCreateStoryCard() throws Exception {
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5054);

        callbackReceived = null;
        testMethod = "testTwoClientCreateStoryCard";

        try {

            try {
                asynchPers.createStoryCard("sc", "testTwoClientCreateStoryCard", 22, 33, 44, 55, 2, 3, 4, 7, 3, "P","", "",0,false,"");
            }
            catch (IndexCardNotFoundException e) {
                Assert.fail();
            }
        }
        catch (NotConnectedException e1) {
            Assert.fail();
        }

        try {
            Thread.sleep(25000);
        }
        catch (InterruptedException e) {
            Assert.fail();
        }

        Assert.assertEquals("createdStoryCard", callbackReceived);
        Assert.assertEquals("createdStoryCard", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("createdStoryCard");
        Assert.assertTrue("Second client did not receive createdStoryCard", callbackOk);
        secondClient.processedCallback();

    }

    /** ********************************************************************* */

    @org.junit.Test
    public void testTwoClientDeleteStoryCard() throws Exception {
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5056);
        callbackReceived = null;
        testMethod = "testTwoClientDeleteStoryCard";

        try {
            try {
                asynchPers5052CompleteFile.deleteStoryCard(6);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testTwoClientDeleteStoryCard");
        }
        Assert.assertEquals("deletedStoryCard", callbackReceived);
        Assert.assertEquals("deletedStoryCard", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("deletedStoryCard");
        Assert.assertTrue("Second client did not receive deletedStoryCard", callbackOk);
        secondClient.processedCallback();
    }

    /** ********************************************************************************************** */

    @org.junit.Test
    public void testTwoClientUndeleteStoryCard() throws Exception {
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5054);
        callbackReceived = null;
        testMethod = "testTwoClientUndeleteStoryCard";

        StoryCard sc = new StoryCardDataObject();
        sc.setId(1000);
        sc.setParent(2);
        sc.setName("testTwoClientUndeleteStoryCard");

        try {
            asynchPers.undeleteStoryCard(sc);
        }
        catch (NotConnectedException e) {
            Assert.fail();
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(20000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testTwoClientUndeleteStoryCard");
        }
        Assert.assertEquals("undeletedStoryCard", callbackReceived);
        Assert.assertEquals("undeletedStoryCard", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("undeletedStoryCard");
        Assert.assertTrue("Second client did not receive undeletedStoryCard", callbackOk);
        secondClient.processedCallback();

    }

    /** ****************************************************************************************** */
    @org.junit.Test
    public void testMoveStoryCardToNewParentWithTwoClients() throws Exception {
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5056);
        callbackReceived = null;
        testMethod = "testMoveStoryCardToNewParentWithTwoClients";

        try {
            try {
                StoryCard sc = new StoryCardDataObject();
                sc.setParent(2);
                sc.setId(6);

                asynchPers5052CompleteFile.moveStoryCardToNewParent(sc, 4, 50, 50,0);
            }
            catch (IndexCardNotFoundException e) {
                Assert.fail();
            }
        }
        catch (NotConnectedException e) {
            Assert.fail();
        }

        try {
            Thread.sleep(15000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testMoveStoryCardToNewParentWithTwoClients");
        }

        Assert.assertEquals("movedStoryCardToNewParent", callbackReceived);
        Assert.assertEquals("movedStoryCardToNewParent", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("movedStoryCardToNewParent");
        Assert.assertTrue("Second client did not movedStoryCardToNewParent", callbackOk);
        secondClient.processedCallback();

    }

    //	
    // /************************************************************************/
    //	
    @org.junit.Test
    public void testTwoClientCreateIteration() throws Exception {
        callbackReceived = null;
        testMethod = "testTwoClientCreateIteration";
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5054);
        try {
            asynchPers.createIteration("iter", "testTwoClientCreateIteration", 44, 55, 66, 77, 3, new Timestamp(0), new Timestamp(500000),0,false);
        }
        catch (NotConnectedException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testTwoClientCreateIteration");
        }
        Assert.assertEquals("createdIteration", callbackReceived);
        Assert.assertEquals("createdIteration", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("createdIteration");
        Assert.assertTrue("Second client did not createIteration", callbackOk);
        secondClient.processedCallback();
    }

    // /******************************************************************************/

    @org.junit.Test
    public void testTwoClientDeleteIteration() throws Exception {
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5056);
        callbackReceived = null;
        testMethod = "testTwoClientDeleteIteration";

        try {
            try {
                asynchPers5052CompleteFile.deleteIteration(4);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(15000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testTwoClientDeleteIteration");
        }
        Assert.assertEquals("deletedIteration", callbackReceived);
        Assert.assertEquals("deletedIteration", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("deletedIteration");
        Assert.assertTrue("Second client did not deletedIteration", callbackOk);
        secondClient.processedCallback();
    }

    /** **************************************************************** */

    @org.junit.Test
    public void testTwoClientUndeleteIteration() throws Exception {
        Iteration iteration = new IterationDataObject();
        iteration.setId(905);
        iteration.setParent(1);
        iteration.setName("undeletedIteration");

        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5054);

        callbackReceived = null;
        testMethod = "testTwoClientUndeleteIteration";
        try {
            asynchPers.undeleteIteration(iteration);
            // helper.testUndeleteIteration();
        }
        catch (NotConnectedException e1) {
            Assert.fail();
        }
        catch (IndexCardNotFoundException e1) {
            Assert.fail();
        }

        try {
            Thread.sleep(25000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testTwoClientUndeleteIteration");
        }

        Assert.assertEquals("undeletedIteration", callbackReceived);
        Assert.assertEquals("undeletedIteration", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("undeletedIteration");
        Assert.assertTrue("Second client did not undeletedIteration", callbackOk);
        secondClient.processedCallback();
    }

    /** *************************************************************** */
    @org.junit.Test
    public void testTwoClientUpdateIteration() throws Exception {
        callbackReceived = null;
        testMethod = "testTwoClientUpdateIteration";
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5056);
        Iteration i = new IterationDataObject("updateIteration", "description empty", 200, 100, 401, 500, (float) 55.0, new Timestamp(0),
                new Timestamp(500000),0,false);
        i.setId(4);
        i.setParent(1);
        try {
            try {
                asynchPers5052CompleteFile.updateIteration(i);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(12000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testTwoClientUpdateIteration");
        }

        Assert.assertEquals("updatedIteration", callbackReceived);
        Assert.assertEquals("updatedIteration", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("updatedIteration");
        Assert.assertTrue("Second client did not updatedIteration", callbackOk);
        secondClient.processedCallback();
    }

    /** **************************************************************** */
    
    
    public void testArrangeProject() {
    	  callbackReceived = null;
          testMethod = "testArrangeProject";

          Project project1 = server5052.getSynchronousPersister().getProject();
		
		  asynchPers5052CompleteFile.arrangeProject(project1);
          try {
              Thread.sleep(60000);
          }
          catch (InterruptedException e) {
              Assert.fail("Failed in AsynchronousDistributedPersisterTest>testArrangeProject");
          }
          Assert.assertEquals("arrangeProject", callbackReceived);
          testMethod = null;
      }

    public void arrangeProject(Project project) { {

          callbackReceived = "arrangeProject";
          Assert.assertTrue(project != null);
      }
		
	}

    @org.junit.Test
    public void testCreateProjectOnSubsequentLoadsFromServer(Project project) throws Exception {

  	  callbackReceived = null;
        testMethod = "testArrangeProject";

        Project project1 = server5052.getSynchronousPersister().getProject();
		
		  asynchPers5052CompleteFile.arrangeProject(project1);
        try {
            Thread.sleep(100);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testArrangeProject");
        }
        Assert.assertEquals("createProjectOnSubsequentLoadsFromServer", callbackReceived);
        testMethod = null;
    
		
	}
//    
	public void createProjectOnSubsequentLoadsFromServer(Project project) {
		callbackReceived = "createProjectOnSubsequentLoadsFromServer";
		
	}

	 @org.junit.Test
	public void testGotProjectNamesForLoginEvent(List<String> list) throws Exception {

        testMethod = "testGotProjectNamesForLoginEvent";
        callbackReceived = null;
        try {
            asynchPers.login("maurer@cpsc.ucalgary.ca", "p@ssw0rd","rally1");
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Assert.fail("Failed in AsynchronousDistributedPersisterTest>testGotProjectNamesForLoginEvent");
            }

        }
        catch (NotConnectedException e) {
            Assert.fail("testProjectnames failed due to NotConnectedException");
        }
        Assert.assertEquals("gotProjectNamesForLoginEvent", callbackReceived);
        testMethod = null;
    
		
	}
	
	public void gotProjectNamesForLoginEvent(List<String> list) {
		callbackReceived = "gotProjectNamesForLoginEvent";
		
	}

	
    @org.junit.Test
    public void testTwoClientUpdateBacklog() throws Exception {
        callbackReceived = null;
        testMethod = "testTwoClientUpdateBacklog";
        AsynchronousDistributedPersisterHelper secondClient = new AsynchronousDistributedPersisterHelper("localhost", 5054);
        Backlog b = new BacklogDataObject(11, 11, 11, 11);
        // b.setParent(1);
        try {
            try {
                asynchPers.updateBacklog(b);
            }
            catch (NotConnectedException e) {
                Assert.fail();
            }
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail();
        }
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testTwoClientUpdateBacklog");
        }

        Assert.assertEquals("updatedBacklog", callbackReceived);
        Assert.assertEquals("updatedBacklog", secondClient.getCallbackReceived());
        testMethod = null;

        // test if the helper object received a callback
        boolean callbackOk = secondClient.callbackReceived("updatedBacklog");
        Assert.assertTrue("Second client did not updatedBacklog", callbackOk);
        secondClient.processedCallback();
    }

    /**
     * @throws IOException
     *             ******************************************************************************
     * @throws NotConnectedException
     * @throws InterruptedException
     */
    @org.junit.Test
    public void testMouseMove() throws Exception {
        try {
            ServerCommunicator comm = new ServerCommunicator("." + File.separator + "TestDirectory", "ProjectFile", 5048,"","");

            ClientCommunicator client1 = new ClientCommunicator("localhost", 5048);
            ClientCommunicator client2 = new ClientCommunicator("localhost", 5048);

            MockUIListener listener1 = new MockUIListener();
            client1.getCallbackPropogator().addPlannerUIChangeListener(listener1);
            // /////////////////////////////////
            MockUIListener listener2 = new MockUIListener();
            client2.getCallbackPropogator().addPlannerUIChangeListener(listener2);

            for (int i = 0; i < 5; i++) {
                client1.moveMouse("test", 3, 4);
            }
            Thread.sleep(1000);
            Assert.assertFalse(listener1.didMouseMoveGetFired); // did it
            // properly
            // filter out
            // the mouse
            // moves?
            Assert.assertFalse(listener2.didMouseMoveGetFired);

            client1.moveMouse("test", 3, 4);

            Thread.sleep(5000);

            Assert.assertFalse(listener1.didMouseMoveGetFired);
            Assert.assertTrue(listener2.didMouseMoveGetFired);

            comm.shutDown();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @org.junit.Test
    public void testLiveUpdate() throws Exception {
        try {
            ServerCommunicator comm = new ServerCommunicator("." + File.separator + "TestDirectory", "ProjectFile", 5049,"","");

            ClientCommunicator client1 = new ClientCommunicator("localhost", 5049);
            ClientCommunicator client2 = new ClientCommunicator("localhost", 5049);

            MockUIListener listener1 = new MockUIListener();
            client1.getCallbackPropogator().addPlannerUIChangeListener(listener1);
            // /////////////////////////////////
            MockUIListener listener2 = new MockUIListener();
            client2.getCallbackPropogator().addPlannerUIChangeListener(listener2);

            IndexCardLiveUpdate data = new IndexCardLiveUpdate();
            data.setField(IndexCardLiveUpdate.FIELD_DESCRIPTION);
            data.setId(0);
            data.setText("This is a ne");
            client1.sendLiveTextUpdate(data);

            Thread.sleep(15000);

            Assert.assertFalse(listener1.didLiveTextGetFired);
            Assert.assertTrue(listener2.didLiveTextGetFired);

            comm.shutDown();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    /** ****************************************************************************** */

//    @org.junit.Test
//    public void testDownloadXML() throws Exception {
//        callbackReceived = null;
//        testMethod = "testDownloadXML";
//
//        try {
//            asynchPers.downloadFile("ProjectFile.xml",0);
//        }
//        catch (NotConnectedException e) {
//            Assert.fail();
//        }
//        try {
//            Thread.sleep(5500);
//        }
//        catch (InterruptedException e) {
//            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testDownloadXML");
//        }
//        Assert.assertEquals("downloadedFile", callbackReceived);
//        testMethod = null;
//    }

    public void downloadedFile(boolean bool) {
        callbackReceived = "downloadedFile";
        Assert.assertTrue(bool);

    }

    /** ****************************************************************************** */
//    @org.junit.Test
//    public void testUploadXML() {
//        callbackReceived = null;
//        testMethod = "testUploadXML";
//
//        try {
//            asynchPers.uploadFile("ProjectFile.xml",0);
//        }
//        catch (NotConnectedException e) {
//            Assert.fail();
//        }
//        try {
//            Thread.sleep(500);
//        }
//        catch (InterruptedException e) {
//            Assert.fail("Failed in AsynchronousDistributedPersisterTest>testUploadXML");
//        }
//        Assert.assertEquals("uploadedFile", callbackReceived);
//        testMethod = null;
//    }

    public void uploadedFile(boolean bool) {
        callbackReceived = "uploadedFile";
        Assert.assertTrue(bool);
    }

    public void updatedProjectName(Project project) {
    }

    /** ****************************************************************************** */
    public void asynchronousException(Exception exception, int messageType) {
        Assert.fail("Unexpected exception was raised: " + exception.getMessage() + "\n" + "in message type " + messageType);
    }

	

//	@org.junit.Test
//    public void testLostConnectionEvent() {
//        callbackReceived = null;
//        testMethod = "testLostConnectionEvent";
//
//     
// //       	asynchPers.getNc().setOutputStream(null);
//            try {
//				asynchPers.downloadFile("ProjectFile.xml",0);
//			} catch (Exception e1) {
//			
//				e1.printStackTrace();
//			}
//          
//			 Assert.assertEquals("lostConnectionEvent", callbackReceived);
//		        testMethod = null;
//    }


	
	public void lostConnectionEvent() {
		 callbackReceived = "lostConnectionEvent";
		
	}

	public void updatedLegendEvent() {
		// TODO Auto-generated method stub
		
	}

	public void updatedLegend(LegendDataObject leg) {
		// TODO Auto-generated method stub
		
	}

	public void updatedLegend(Legend leg) {
		// TODO Auto-generated method stub
		
	}
	public void updatedOwner(TeamMember teamMember) {
		// TODO Auto-generated method stub
		
	}

	public void createdOwner(TeamMember teamMember) {
		// TODO Auto-generated method stub
		
	}

	


	


}

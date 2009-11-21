package ca.ucalgary.cpsc.agilePlanner.test.planner;

import java.io.File;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.ProjectNotFoundException;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.impl.IterationDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.local.PersisterToXML;



public class SynchronousPersisterTest extends TestCase {
    private static PersisterToXML ps;

    public static Test suite() {
        return new JUnit4TestAdapter(SynchronousPersisterTest.class);
    }

    @Before
    public void setUp() {
        try {
            ResetFiles.resetProjectFiles();
            try {
                try {
					ps = new PersisterToXML(System.getProperty("user.dir") + File.separator + "TestDirectory", "ProjectFile");
				} catch (RemoteException e) {
					// will never get here, written just for consistency with distributed mode
				}
            }
            catch (CouldNotLoadProjectException e) {
                Assert.fail("Could not load project in setup of SynchronousPersisterTest");
            }
        }
        catch (ConnectionFailedException e) {
            Assert.fail("Could not create an instance of PersisterToXML");
        }
    }

    @After
    public void tearDown() {
    }

    /***************************************************************************
     * INITIALIZATION *
     **************************************************************************/

    @org.junit.Test
    public void testInitialize() {
        File projectDirectory = ps.getProjectDirectory();
        String filename = projectDirectory.getAbsolutePath() + File.separator + "ProjectFile.xml";
        File projectFile = ps.getFile();
        Assert.assertTrue("Project directory is not a directory", projectDirectory.isDirectory());
        Assert.assertTrue("Project directory is not the user.dir", projectDirectory.getAbsolutePath().equals(
                System.getProperty("user.dir") + File.separator + "TestDirectory"));
        Assert.assertTrue("Project file not available", projectFile.getAbsolutePath().equals(filename));
    }

    @org.junit.Test
    public void testGetProjectNames() {
        List<String> names = ps.getProjectNames();
        List<String> testList = new ArrayList<String>();
        testList.add("EmptyProject");
        testList.add("ProjectFile");
        testList.add("ProjectWithBacklogIterationAndStoryCards");
        testList.add("ThisProjectShouldNotExist");
        for (String projName:names){
        	assertTrue("The obtained project name is not in List.",testList.contains(projName));
        }
        //Assert.assertTrue("The project directory contains an incorrect number of projects", names.size() == 3);
    }

    @org.junit.Test
    public void testLoad() {
        Project p = null;
        try {
            try {
				p = ps.load("ProjectWithBacklogIterationAndStoryCards");
			} catch (RemoteException e) {
				// will never get here, written just for consistency with distributed mode
			}
        }
        catch (CouldNotLoadProjectException e) {
            Assert.fail();
        }
        List<StoryCard> backlogStories = p.getBacklog().getStoryCardChildren();
        List<Iteration> iterations = p.getIterationChildren();
        Assert.assertTrue("Incorrect number os stories in backlog", backlogStories.size() == 1);
        Assert.assertTrue("Incorrect number os stories in backlog", iterations.size() == 1);
        List<StoryCard> iterationStories = p.getIterationChildren().iterator().next().getStoryCardChildren();
        Assert.assertTrue("Incorrect number os stories in backlog", iterationStories.size() == 1);
    }

    @org.junit.Test
    public void testLoadAndCreateCard() {
        try {
            try {
				ps.load("ProjectWithBacklogIterationAndStoryCards");
			} catch (RemoteException e) {
				// will never get here, written just for consistency with distributed mode
			}
        }
        catch (CouldNotLoadProjectException e) {
            Assert.fail("Could not load project in SynchronousPersisterTest>testLoadAndCreate");
        }

        Iteration iter;
        iter = ps.createIteration("", "", 22, 33, 44, 55, 2, new Timestamp(0), new Timestamp(1000),0,false);

        long id = iter.getId();

       // Assert.assertTrue(id == 7);
        Assert.assertTrue(iter.getParent() == 1);

        try {
            StoryCard sc = ps.createStoryCard("testcard", "test description", 200, 100, 70, 70, id, 0, 0, 0, 0, "P","", "",0,false, StoryCard.DEFAULT_FITID);
            Assert.assertTrue("Parent id of story card incorrect", sc.getParent() == id);
            Assert.assertTrue("StoryCard was not added to iteration", ((Iteration) ps.findCard(id)).getStoryCardChildren().size() == 1);
            Assert.assertTrue("StoryCard not found in iteration", ((Iteration) ps.findCard(id)).getStoryCardChildren().contains(sc));
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("Tried to create story card but in iteration but iteration is missing");
        }
    }

    /***************************************************************************
     * StoryCard Methods *
     **************************************************************************/

    @org.junit.Test
    public void testCreatAndDeleteIteration() {
        try {
            try {
				ps.load("ProjectWithBacklogIterationAndStoryCards");
			} catch (RemoteException e) {
				// will never get here, written just for consistency with distributed mode
			}
        }
        catch (CouldNotLoadProjectException e2) {
            Assert.fail("Could not load project in SynchronousPersisterTest>testCreatAndDeleteIteration");
        }
        try {
            // try to delete an iteration that does not exist
            Iteration iteration = new IterationDataObject();
            iteration.setId(-1);
            try {
                iteration = (Iteration) ps.deleteCard(iteration.getId());
            }
            catch (ForbiddenOperationException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }
            Assert.fail("Expected IndexCardNotFoundException was NOT thrown");
        }
        catch (IndexCardNotFoundException e) {
            Assert.assertTrue("Iteration was correctly not found", true);
        }
        Iteration iteration = new IterationDataObject();
        iteration.setId(4);
        try {
            try {
                iteration = (Iteration) ps.deleteCard(iteration.getId());
            }
            catch (ForbiddenOperationException e1) {
                e1.printStackTrace();
                Assert.fail(e1.getMessage());
            }
            try {// check if iteration really was deleted
                ps.findCard(iteration.getId());
                Assert.fail("Iteration was not deleted from SynchronousPersister");
            }
            catch (IndexCardNotFoundException e) {
                // this should happen - everything is ok
            }
            try {// check if the story card inside the iteration is deleted,
                    // too
                ps.findCard(5);
                Assert.fail("Story was not deleted from SynchronousPersister when iteration was deleted");
            }
            catch (IndexCardNotFoundException e) {
                // this should happen - everything is ok
            }

        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("Iteration was not deleted correctly");
        }
    }

    @org.junit.Test
    public void testUndeleteIteration() throws ForbiddenOperationException {
        long parentid = ps.getProject().getId();
        try {

            Iteration iteration = new IterationDataObject();
            iteration.setId(999);
            iteration.setParent(parentid);
            iteration.setName("undeletedIteration");

            StoryCard sc = new StoryCardDataObject();
            sc.setId(1000);
            iteration.addStoryCard(sc);

            iteration = (Iteration) ps.undeleteCard(iteration);

            iteration = (Iteration) ps.findCard(999);
            sc = (StoryCard) ps.findCard(1000);
            Assert.assertEquals(iteration.getId(), (long) 999);
            Assert.assertEquals(iteration.getName(), "undeletedIteration");
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("Could not undelete StoryCard included in iteration");
        }

    }

    @org.junit.Test
    public void testCreateAndDeleteBacklog() {
        PersisterToXML ps1 = null;
        try {
            try {
                try {
					ps1 = new PersisterToXML(System.getProperty("user.dir") + File.separator + "TestDirectory", "EmptyProject");
				} catch (RemoteException e) {
					// will never get here, written just for consistency with distributed mode
				}
            }
            catch (ConnectionFailedException e) {
                Assert.fail("Could not load project in SynchronousPersisterTest>testCreatAndDeleteBacklog");
            }
        }
        catch (CouldNotLoadProjectException e1) {
            Assert.fail("Could not load project in SynchronousPersisterTest>testCreatAndDeleteBacklog");
        }
        try {
            Backlog b = ps1.createBacklog(22, 33, 44, 55);
            try {
                StoryCard sc = ps1.createStoryCard("testcard", "test description", 200, 100, 70, 70, b.getId(), 0, 0, 0, 0, "P","", "",0,false, StoryCard.DEFAULT_FITID);
                Assert.assertTrue("Parent id of story card incorrect", sc.getParent() == b.getId());
                Assert.assertTrue("StoryCard was not added to Backlog", ps1.getProject().getBacklog().getStoryCardChildren().size() == 1);
                Assert.assertTrue("StoryCard not found in Backlog", ps1.getProject().getBacklog().getStoryCardChildren().contains(sc));
            }
            catch (IndexCardNotFoundException e) {
                Assert.fail("Tried to create story card but in iteration but iteration is missing");
            }

            try {
                ps1.deleteCard(b.getId());
            }
            catch (IndexCardNotFoundException e) {
                Assert.fail("IndexCardNotFound in SynchronousPersisterTest>testCreatAndDeleteBacklog");
            }
        }
        catch (ForbiddenOperationException e) {
            Assert.assertTrue("You cannot delete the Backlog without deleting the Project!", true);
        }
    }

    @org.junit.Test
    public void testDeleteStoryCard() {
        try {
            try {
				ps.load("ProjectWithBacklogIterationAndStoryCards");
			} catch (RemoteException e) {
				// will never get here, written just for consistency with distributed mode
			}
        }
        catch (CouldNotLoadProjectException e2) {
            Assert.fail("Could not load project in SynchronousPersisterTest>testDeleteStoryCard");
        }
        // long parentid = ps.getProject().getId();
        try {
            // try to delete StoryCard that does not exist
            StoryCard sc = new StoryCardDataObject();
            sc.setId(-1);
            try {
                sc = (StoryCard) ps.deleteCard(sc.getId());
            }
            catch (ForbiddenOperationException e) {
                e.printStackTrace();
                Assert.fail(e.getMessage());
            }
            Assert.fail("Expected IndexCardNotFoundException was NOT thrown");
        }
        catch (IndexCardNotFoundException e) {
            Assert.assertTrue("StoryCard was correctly not found", true);
        }
        StoryCard sc = new StoryCardDataObject();
        sc.setId(6);
        try {
            try {
                sc = (StoryCard) ps.deleteCard(6);
            }
            catch (ForbiddenOperationException e1) {
                e1.printStackTrace();
                Assert.fail(e1.getMessage());
            }
            try {// check if StoryCard really was deleted
                ps.findCard(6);
                Assert.fail("StoryCard was not deleted from SynchronousPersister");
            }
            catch (IndexCardNotFoundException e) {
                // this should happen - everything is ok
            }

        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("StoryCard was not deleted correctly");
        }
    }

    @org.junit.Test
    public void testUndeleteStoryCard() throws ForbiddenOperationException {
        try {
            Iteration i = ps.createIteration("undeletedStoryCard", "description empty", 200, 100, 399, 500, (float) 55.0, new Timestamp(0),
                    new Timestamp(500000),0,false);

            StoryCard sc = new StoryCardDataObject();
            sc.setId(1000);
            sc.setParent(i.getId());
            sc.setName("undeletedStoryCard");
            long id = sc.getParent();

            sc = (StoryCard) ps.undeleteCard(sc);

            // sc = ps.findStoryCardById(1000);
            sc = (StoryCard) ps.findCard(1000);

            Assert.assertEquals(sc.getId(), (long) 1000);
            Assert.assertEquals(i.getId(), id);
            Assert.assertEquals(sc.getName(), "undeletedStoryCard");
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("Could not undelete StoryCard");
        }

    }

    @org.junit.Test
    public void testCreateAndUpdateStoryCard() throws ForbiddenOperationException {

        try {
            Iteration iteration = ps.createIteration("undeletedStoryCard", "description empty", 200, 100, 399, 500, (float) 55.0, new Timestamp(0), new Timestamp(500000),0,false);

            StoryCard sc = ps.createStoryCard("", "", 22, 33, 44, 55, iteration.getId(), 2, 3, 4, 5, "P","", "",0,false, StoryCard.DEFAULT_FITID);
            StoryCard sc1;

            try {
				sc1 = (StoryCard) ps.updateCard(sc);
				try {// check if StoryCard really was updated
				    ps.findCard(sc1.getId());
				    try {
						ps.findCard(sc1.getParent());
					} catch (RuntimeException e) {
						Assert.fail("StoryCard was not updated from SynchronousPersister");
					}
				}catch (IndexCardNotFoundException e) {
				    Assert.fail("StoryCard was not updated from SynchronousPersister");
				}
			} catch (IndexCardNotFoundException e) {
				Assert.fail("StoryCard was not updated correctly");
			}
        }catch (IndexCardNotFoundException e) {
            Assert.fail("StoryCard was not updated correctly");
        }
    }
    
    @org.junit.Test
    public void testCreateAndUpdateTabletPCStoryCard() throws ForbiddenOperationException {

        try {
            Iteration iteration = ps.createIteration("undeletedStoryCard", "description empty", 200, 100, 399, 500, (float) 55.0, new Timestamp(0), new Timestamp(500000),0,false);

            StoryCard sc = ps.createTabletPCStoryCard("", "", 22, 33, 44, 55, iteration.getId(), 2, 3, 4, 5, "P","",null,false);
            StoryCard sc1;

            sc1 = (StoryCard) ps.updateCard(sc);
            try {// check if StoryCard really was updated
                ps.findCard(sc1.getId());
                ps.findCard(sc1.getParent());

            }
            catch (IndexCardNotFoundException e) {

                Assert.fail("StoryCard was not updated from SynchronousPersister");

            }

        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("StoryCard was not updated correctly");
        }
    }

    @org.junit.Test
    public void testUpdateIteration() throws ForbiddenOperationException {

        try {
            Iteration i = ps.createIteration("updateIteration", "description empty", 200, 100, 401, 500, (float) 55.0, new Timestamp(0),
                    new Timestamp(500000),0,false);

            Iteration i1;

            i1 = (Iteration) ps.updateCard(i);
            try {// check if Iteration really was updated
                ps.getProject().getId();
                ps.findCard(i1.getId());
            }
            catch (IndexCardNotFoundException e) {
                Assert.fail("Iteration was not updated from SynchronousPersister");
            }

        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("Iteration was not updated correctly");
        }
    }

    @org.junit.Test
    public void testUpdateBacklog() throws ForbiddenOperationException {
        try {
            Backlog b = (Backlog) ps.updateCard(ps.getProject().getBacklog());

            Assert.assertTrue("Backlog updated successfully", b.getParent() == 1);
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("IndexCardNotFound in SynchronousPersisterTest>TestUpdateBacklog");
        }
    }

    @org.junit.Test
    public void testMoveStoryCardToNewParent() throws ForbiddenOperationException {
        PersisterToXML ps1 = null;
        try {
            try {
                try {
					ps1 = new PersisterToXML(System.getProperty("user.dir") + File.separator + "TestDirectory",
					        "ProjectWithBacklogIterationAndStoryCards");
				} catch (RemoteException e) {
					// will never get here, written just for consistency with distributed mode
				}
            }
            catch (ConnectionFailedException e) {
                Assert.fail("Could not load project in SynchronousPersisterTest>testMoveStoryCradToNewParent");
            }
        }
        catch (CouldNotLoadProjectException e1) {
            Assert.fail("Could not load project in SynchronousPersisterTest>testMoveStoryCradToNewParent");
        }
        try {
            // move from Backlog to Iteration
            ps1.moveStoryCardToNewParent(6, 2, 4, 50, 50,0);
            Assert.assertTrue("Card moved unsucessfully", (((StoryCard) (ps1.findCard(6))).getParent() == 4));
            Assert.assertTrue("StoryCard appeared twice after move", ((Iteration) ps1.findCard(4)).getStoryCardChildren().size() == 2);
            Assert.assertTrue("StoryCard not removed from Backog", ((Backlog) ps1.findCard(2)).getStoryCardChildren().size() == 0);
            // move from Iteration to Backlog
            ps1.moveStoryCardToNewParent(8, 4, 2, 50, 50,0);
            Assert.assertTrue("Card moved unsucessfully", (((StoryCard) (ps1.findCard(8))).getParent() == 2));
            Assert.assertTrue("StoryCard appeared twice after move", ((Backlog) ps1.findCard(2)).getStoryCardChildren().size() == 1);
            Assert.assertTrue("StoryCard not removed from Iteration", ((Iteration) ps1.findCard(4)).getStoryCardChildren().size() == 1);
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("Unexpected IndexCardNotFound Exception was caught in testMoveStoryCardToNewParent");
        }
    }

    @org.junit.Test
    public void testDuplicateCardInProject() throws ForbiddenOperationException {
        PersisterToXML ps1 = null;
        try {
            try {
                try {
					ps1 = new PersisterToXML(System.getProperty("user.dir") + File.separator + "TestDirectory",
					        "ProjectWithBacklogIterationAndStoryCards");
				} catch (RemoteException e) {
					// will never get here, written just for consistency with distributed mode
				}
            }
            catch (ConnectionFailedException e) {
                Assert.fail("Could not load project in SynchronousPersisterTest>testDuplicateCardInProject");
            }
        }
        catch (CouldNotLoadProjectException e1) {
            Assert.fail("Could not load project in SynchronousPersisterTest>testDuplicateCardInProject");
        }
        try {
            StoryCard sc = ps1.moveStoryCardToNewParent(6, 2, 4, 50, 50,0);
            Assert.assertTrue("Card moved sucessfully", (sc.getParent() == 4));
            // for(((ps1.getProject()).g:){}
        }
        catch (IndexCardNotFoundException e) {
            Assert.fail("IndexCardNotFound in SynchronousPersisterTest>testDuplicateCardInProject");
        }
    }

    /**
     * this test tried to reproduce a bug that duplicated a story card in an iteration 
     * when the iteration was moved on the screen
     */
    @org.junit.Test
    public void testUpdateIterationBug() {

		try {
			Iteration i = ps.createIteration("IterationToUpdate", "description empty", 200, 100, 401, 500, (float) 55.0, new Timestamp(0),
                    new Timestamp(500000),0,false);
			StoryCard sc;
			sc = ps.createStoryCard("example card", "", 22, 33, 44, 55, i.getId(), 2, 3, 4, 5, "P","","",0,false, StoryCard.DEFAULT_FITID);
			i = (Iteration) ps.findCard(i.getId());
	        
	        // ensure that the current state of the back end is correct
	        Assert.assertTrue(i.getStoryCardChildren().size() == 1); 
	        Assert.assertTrue(i.getStoryCardChildren().iterator().next().getName().equals(sc.getName()));
	        
	        // now move the iteration to a new location
	        int x = i.getLocationX();
	        int y = i.getLocationY();
	        i.setLocationX(x+20);
	        i.setLocationY(y+20);
	        i = (Iteration) ps.updateCard(i);

	        Assert.assertTrue(i.getStoryCardChildren().size() == 1); 
	        Assert.assertTrue(i.getStoryCardChildren().iterator().next().getName().equals(sc.getName()));
	        Assert.assertTrue(i.getLocationX() == x+20);
	        Assert.assertTrue(i.getLocationY() == y+20);
		} catch (IndexCardNotFoundException e) {
			Assert.fail("Unexpected IndexCardNotFound exception in SynchronousPersisterTest>testUpdateIterationBug");
		}
        
     }

    @org.junit.Test
    public void testClone() {
          try {
            Project pdo1 = null;
			try {
				pdo1 = ps.load("ProjectWithBacklogIterationAndStoryCards");
			} catch (RemoteException e) {
				// will never get here, written just for consistency with distributed mode
			}
            Project pdo2 = ps.getProject();
            
			//Test if ProjectDataObject can be cloned (tests for shallow copy here)
			Assert.assertTrue("Clone did not work", pdo1 != pdo2);
			
			//Test for direct pointers into backend data structure
			Assert.assertTrue("Clone did not work the backlog", pdo1.getBacklog() != pdo2.getBacklog());
			Assert.assertTrue("Clone did not work the iteration", pdo1.getIterationChildren().get(0) != pdo2.getIterationChildren().get(0));
			Assert.assertTrue("Clone did not work the story card in the backlog", 
					pdo1.getBacklog().getStoryCardChildren().get(0) != pdo2.getBacklog().getStoryCardChildren().get(0));
			Assert.assertTrue("Clone did not work the story card in the iteration", 
					pdo1.getIterationChildren().get(0).getStoryCardChildren().get(0) != pdo2.getIterationChildren().get(0).getStoryCardChildren().get(0));
			
			long id = pdo1.getBacklog().getId();
			Assert.assertTrue("Clone did not work in findCard", ps.findCard(id) != ps.findCard(id) );
			
          } catch (CouldNotLoadProjectException e) {
			Assert.fail("Failed in testClone");
		    }catch (IndexCardNotFoundException e) {
			Assert.fail("Failed in testClone");
		    }

    }
    @org.junit.Test
    public void testDeleteProject(){
    	try {
			boolean aa = ps.deleteProject("ProjectFile");
			Assert.assertTrue(aa);
			
		} catch (ProjectNotFoundException e) {
			
			e.printStackTrace();
		}
    	
    	
    }
    
}// // end class

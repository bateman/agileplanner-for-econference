/**
 * 
 */
package ca.ucalgary.cpsc.agilePlanner.test.planner;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.TeamMemberDataObject;



/**
 * @author dhillonh
 * 
 */
public class ProjectDataObjectTest extends TestCase {

	Project project;
	
	@Before
	public void setUp(){
		project = new ProjectDataObject();
	}
	
	@org.junit.Test
    public void testCreateAndDeleteBacklog() {
        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        Backlog backlog = null;
        try {
            backlog = pdo.createBacklog(10, 10, 10, 10);
            Assert.assertEquals("Invalid Backlog name:", backlog.getName(), "Project Backlog");
            Assert.assertTrue("Invalid Backlog Height: " + backlog.getHeight(), backlog.getHeight() == 10);
            try {
                pdo.deleteCard(backlog.getId());
                fail("Backlog can't be deleted");
            }
            catch (ForbiddenOperationException e) {
                // this exception should happen here - do nothing
            }
            backlog = pdo.createBacklog(20, 20, 20, 20);
        }
        catch (Exception e) {
            fail("Failed in ProjectDataObjectTest>testCreateAndDeleteBacklog");
        }

    }

    @org.junit.Test
    public void testCreateDeleteUndeleteStoryCardAndUpdateBacklog() {
        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        ProjectDataObject pdo1 = new ProjectDataObject("TestProject1");
        try {
            Backlog backlog = pdo.createBacklog(10, 10, 10, 10);

            Backlog backlog1 = pdo1.createBacklog(20, 20, 20, 20);
            backlog1.setId(backlog.getId());
            StoryCard sc1 = pdo1.createStoryCard("sc1", "", 11, 11, 11, 11, pdo1.getBacklog().getId(), 0, 0, 0, 0, "P","","",0,false, StoryCard.DEFAULT_FITID);
            assertTrue(sc1 != null);
            assertTrue(pdo1.getBacklog().getStoryCardChildren().size() == 1);

            StoryCard sc2 = pdo1.createStoryCard("sc2", "", 33, 33, 33, 33, pdo1.getBacklog().getId(), 0, 0, 0, 0, "P","","",0,false, StoryCard.DEFAULT_FITID);
            assertTrue(pdo1.getBacklog().getStoryCardChildren().size() == 2);
            assertTrue(sc1.getId() + 2 == sc2.getId());

            pdo.updateCard(backlog1);
            assertTrue(pdo.getBacklog().getStoryCardChildren().size() == 2);

            pdo.deleteCard(sc2.getId());
            assertTrue(pdo.getBacklog().getStoryCardChildren().size() == 1);

            pdo.undeleteCard(sc2);
            assertTrue(pdo.getBacklog().getStoryCardChildren().size() == 2);

            assertTrue(pdo.getBacklog().getHeight() == 20);
            assertTrue(pdo.getBacklog().getStoryCardChildren().size() == 2);

        }
        catch (Exception e) {
            fail("Failed in ProjectDataObjectTest>testCreateDeleteUndeleteStoryCardAndUpdateBacklog");
        }
    }

    @org.junit.Test
    public void testUpdateStoryCard() {
        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        Backlog backlog = null;
        try {
            backlog = pdo.createBacklog(10, 10, 10, 10);
            StoryCard sc1 = null;
            sc1 = pdo.createStoryCard("sc1", "", 11, 11, 11, 11, pdo.getBacklog().getId(), 0, 0, 0, 0, "P","","",0,false, StoryCard.DEFAULT_FITID);
            backlog.addStoryCard(sc1);

            StoryCard sc2 = (StoryCard) pdo.updateCard(sc1);

            assertTrue(pdo.getBacklog().getId() == sc2.getParent());
            assertTrue(sc2.getName().equals("sc1"));
        }
        catch (Exception e) {
            fail("failed in ProjectDataPbjectTest>testUpdateStoryCard");
        }

    }

    @org.junit.Test
    public void testMoveStoryCardToNewParent() {
        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        Iteration iteration = pdo.createIteration("iter", "", 55, 55, 55, 55, 1, new Timestamp(0), new Timestamp(1000),0,false);
        StoryCard sc1 = null;

        try {
            pdo.createBacklog(10, 10, 10, 10);
            sc1 = pdo.createStoryCard("sc1", "", 11, 11, 11, 11, pdo.getBacklog().getId(), 0, 0, 0, 0, "P","","",0,false, StoryCard.DEFAULT_FITID);

            StoryCard sc2 = pdo.moveStoryCardToNewParent(sc1.getId(), sc1.getParent(), iteration.getId(), 99, 99,0);

            assertTrue(sc2.getParent() == iteration.getId());
            assertTrue(pdo.getBacklog().getStoryCardChildren().size() == 0);

        }
        catch (Exception e) {
            fail("failed in ProjectDataPbjectTest>testMoveStoryCardToNewParent");
        }
    }

    @org.junit.Test
    public void testCreateDeleteUndeleteAndUpdateIteration() {
        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        try {
            Iteration iteration = pdo.createIteration("iter", "", 55, 55, 55, 55, 1, new Timestamp(0), new Timestamp(1000),0,false);

            Iteration iteration1 = null;
            Iteration iteration2 = null;

            iteration1 = (Iteration) pdo.updateCard(iteration);

            assertTrue(iteration1.getLocationX() == 55);

            iteration2 = (Iteration) pdo.deleteCard(iteration1.getId());
            assertTrue(pdo.getIterationChildren().size() == 0);
            pdo.undeleteCard(iteration2);
            assertTrue(pdo.getIterationChildren().size() == 1);
        }
        catch (Exception e) {
            fail("failed in ProjectDataPbjectTest>testCreateDeleteUndeleteAndUpdateIteration");
        }

    }

    
    @org.junit.Test
    public void testClone() {
        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        Iteration iteration = pdo.createIteration("iter", "", 55, 55, 55, 55, 1, new Timestamp(0), new Timestamp(1000),0,false);
        StoryCard sc1 = null;
        StoryCard sc2 = null;
        try {
			Backlog backlog = pdo.createBacklog(10, 10, 10, 10);
			sc1 = pdo.createStoryCard("sc1", "", 11, 11, 11, 11, pdo.getBacklog().getId(), 0, 0, 0, 0, "P","","",0,false, StoryCard.DEFAULT_FITID);
			sc2 = pdo.createStoryCard("sc2", "", 22, 22, 22, 22, iteration.getId(), 0, 0, 0, 0, "P","","",0,false, StoryCard.DEFAULT_FITID);
			
			//Test if ProjectDataObject can be cloned (tests for shallow copy here)
			assertTrue("ProjectDataObject>clone did not work", pdo != pdo.clone());
			//Test for direct pointers into backend data structure
			assertTrue("ProjectDataObject>clone did clone the backlog", pdo.getBacklog() == backlog);
			assertTrue("ProjectDataObject>clone did clone the iteration", pdo.getIterationChildren().get(0) == iteration);
			assertTrue("ProjectDataObject>clone did clone the story card in the backlog", 
					pdo.getBacklog().getStoryCardChildren().get(0) == sc1);
			assertTrue("ProjectDataObject>clone did clone the story card in the iteration", 
					pdo.getIterationChildren().get(0).getStoryCardChildren().get(0) == sc2);
			
			
		} catch (IndexCardNotFoundException e) {
			fail("Unexpected IndexCardNotFoundException in testClone");
		}

    }
    
    @Test
	public void shouldRemoveOwnerFromList(){
		TeamMember teamMember = new TeamMemberDataObject("Jimmy");
		project.addTeamMember(teamMember);
		assertEquals("TeamMember was not added", 1, project.getTeamMembers().size());
		project.removeOwner(teamMember);
		assertEquals("TeamMember was not removed", 0, project.getTeamMembers().size());
	}
	
	@Test
	public void shouldRemoveOwnerOfNameJimmyFromList(){
		project.addTeamMember(new TeamMemberDataObject("Jimmy"));
		assertEquals("TeamMember was not added", 1, project.getTeamMembers().size());
		project.removeOwner(new TeamMemberDataObject("Jimmy"));
		assertEquals("TeamMember was not removed", 0, project.getTeamMembers().size());
	}
	
	@Test
	public void shouldRemoveOwnersFromList(){
		project.addTeamMember(new TeamMemberDataObject("Jimmy"));
		assertEquals("TeamMember was not added", 1, project.getTeamMembers().size());
		project.removeOwners(new String [] {"Jimmy"});
		assertEquals("TeamMember was not removed", 0, project.getTeamMembers().size());
	}
}

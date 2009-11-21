package ca.ucalgary.cpsc.agilePlanner.test.planner;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;

import persister.PlannerDataChangeListener;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.LegendDataObject;
import persister.distributed.ClientCommunicator;



public class AsynchronousDistributedPersisterHelper implements PlannerDataChangeListener {
    public void deletedOwner(TeamMember teamMember) {
		// TODO Auto-generated method stub
		
	}

	private Object callbackReceived;

    private String testMethod;

    public AsynchronousDistributedPersisterHelper(String host, int port) {
        ClientCommunicator asynchPers;
        try {
            asynchPers = new ClientCommunicator(host, port);
            asynchPers.addPlannerDataChangeListener(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * CONNECTION *
     **************************************************************************/

    public void createdProjectOnInitialLoadFromServer(Project p) {
        callbackReceived = "createdProject";
        if (testMethod.equals("testConnect")) {
            Assert.assertEquals(testMethod, "testConnect");
            Assert.assertEquals(p.getName(), "ProjectFile");
        }
        else {
            Assert.assertEquals(testMethod, "testLoad");
            List<StoryCard> backlogStories = p.getBacklog().getStoryCardChildren();
            List<Iteration> iterations = p.getIterationChildren();
            Assert.assertTrue("Incorrect number os stories in backlog", backlogStories.size() == 1);
            Assert.assertTrue("Incorrect number os stories in backlog", iterations.size() == 1);
            List<StoryCard> iterationStories = p.getIterationChildren().iterator().next().getStoryCardChildren();
            Assert.assertTrue("Incorrect number os stories in backlog", iterationStories.size() == 1);
        }
    }

    /** ************************************************************************* */

    public void gotProjectNames(List<String> str) {
        callbackReceived = "gotProjectNames";
        Assert.assertTrue("List of project names has unexpected length", str.size() == 3);
    }

    /** **************************************************************** */

    public void undeletedIteration(Iteration iteration) {
        callbackReceived = "undeletedIteration";
        Assert.assertEquals(iteration.getId(), (long) 905);
        Assert.assertEquals(iteration.getName(), "undeletedIteration");
    }

    // /*********************************************************************/

    public void createdBacklog(Backlog backlog) {
        callbackReceived = "createdBacklog";
        Assert.assertEquals(backlog.getHeight(), 25);
        Assert.assertEquals(backlog.getWidth(), 25);
        Assert.assertEquals(backlog.getLocationX(), 25);
        Assert.assertEquals(backlog.getLocationY(), 25);

    }

    /** ******************************************************************* */

    public void movedStoryCardToNewParent(StoryCard storycard) {
        callbackReceived = "movedStoryCardToNewParent";
        Assert.assertEquals(storycard.getId(), (long)6);// 6);
        Assert.assertEquals(storycard.getParent(), (long) 4);
    }

    /** ********************************************************************************** */

    public void createdStoryCard(StoryCard storycard) {
        callbackReceived = "createdStoryCard";
        Assert.assertEquals(storycard.getName(), "sc");
    }

    /** ********************************************************************* */

    public void createdIteration(Iteration iteration) {
        callbackReceived = "createdIteration";
        Assert.assertEquals(iteration.getName(), "iter");
    }

    //
    // /**************************************************************************************/
    //	

    public void deletedBacklog(long id) {
        Assert.assertTrue("Deleteing a backlog is forbidden but happened", false);
    }

    // /******************************************************************************/

    public void deletedIteration(long id) {

        callbackReceived = "deletedIteration";
        Assert.assertEquals(4, 4);
    }

    /** **************************************************************** */

    public void deletedStoryCard(long id) {

        callbackReceived = "deletedStoryCard";
        Assert.assertEquals(id, (long) 6);// 6);
    }

    /** **************************************************************** */

    public void undeletedStoryCard(StoryCard storyCard) {
        callbackReceived = "undeletedStoryCard";
        Assert.assertEquals(storyCard.getId(), (long) 1000);
        Assert.assertEquals(storyCard.getParent(), (long) 2);
    }

    /** **************************************************************** */

    public void updatedBacklog(Backlog backlog) {
        callbackReceived = "updatedBacklog";
        Assert.assertEquals(backlog.getLocationX(), 11);
    }

    /** *************************************************************** */

    public void updatedIteration(Iteration iteration) {

        callbackReceived = "updatedIteration";
        Assert.assertEquals(iteration.getWidth(), 200);
        Assert.assertEquals(iteration.getHeight(), 100);
    }

    /** *********************************************************************** */

    public void updatedStoryCard(StoryCard sc) {
        callbackReceived = "updatedStoryCard";
    }

    /** **************************************************************** */

    /** ********************************************************************* */
    public void updatedProjectName(Project project) {
    }

    public void projectInXML(String xmlFileContents) {
    }

    public void asynchronousException(Exception exception, int messageType) {
        Assert.fail("Unexpected exception was raised: " + exception.getMessage() + "\n" + "in message type " + messageType);
    }

    public boolean callbackReceived(String methodName) {
        return (callbackReceived.equals(methodName));
    }

    public void processedCallback() {
        callbackReceived = null;
    }

    public Object getCallbackReceived() {
        return callbackReceived;
    }

    public void setCallbackReceived(Object callbackReceived) {
        this.callbackReceived = callbackReceived;
    }

    public void downloadedFile(boolean bool) {
    }

    public void uploadedFile(boolean bool) {
    }

	public void arrangeProject(Project project) {
		// TODO Auto-generated method stub
		
	}

	public void createProjectOnSubsequentLoadsFromServer(Project project) {
		// TODO Auto-generated method stub
		
	}

	public void gotProjectNamesForLoginEvent(List<String> list) {
		// TODO Auto-generated method stub
		
	}

	
	public void lostConnectionEvent() {
		// TODO Auto-generated method stub
		
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

package persister.local;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.event.EventListenerList;

import persister.AsynchronousPersister;
import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.ForbiddenOperationException;
import persister.IndexCardLiveUpdate;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.PlannerDataChangeListener;
import persister.SynchronousPersister;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;



public class AsynchronousLocalPersister implements AsynchronousPersister {

    private EventListenerList plannerDataChangeListeners;

    private SynchronousPersister synPer;

    public AsynchronousLocalPersister(String localProjectDirPath, String defaultFilename) throws ConnectionFailedException,
            CouldNotLoadProjectException {
        this.plannerDataChangeListeners = new EventListenerList();
        try {
			synPer = new PersisterToXML(localProjectDirPath, defaultFilename);
		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
		}
    }
    
    

    private void fireCreatedBacklogEvent(Backlog backlog) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).createdBacklog(backlog);
            }
        }
    }

    private void fireCreatedIterationEvent(Iteration iteration) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).createdIteration(iteration);
            }
        }
    }

    private void fireCreatedProjectEvent(Project project) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).createdProjectOnInitialLoadFromServer(project);
            }
        }
    }

    private void fireCreatedStoryCardEvent(StoryCard storycard) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).createdStoryCard(storycard);
            }
        }
    }

    private void fireDeletedIterationEvent(Iteration iteration) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).deletedIteration(iteration.getId());
            }
        }
    }

    private void fireDeletedStoryCardEvent(StoryCard storycard) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).deletedStoryCard(storycard.getId());
            }
        }
    }
    
    private void fireDeletedOwnerEvent(TeamMember teamMember) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).deletedOwner(teamMember);
            }
        }
    }

    private void fireGotProjectNamesEvent(List<String> projects) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).gotProjectNames(projects);
            }
        }
    }

    private void fireMovedStoryCardToNewParentEvent(StoryCard storycard) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).movedStoryCardToNewParent(storycard);
            }
        }
    }

    private void fireUndeletedIterationEvent(Iteration iteration) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).undeletedIteration(iteration);
            }
        }
    }

    private void fireUndeletedStorycCardEvent(StoryCard storycard) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).undeletedStoryCard(storycard);
            }
        }
    }

    private void fireUpdatedBacklogEvent(Backlog backlog) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).updatedBacklog(backlog);
            }
        }
    }
    
    private void fireUpdatedIterationEvent(Iteration iteration) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).updatedIteration(iteration);
            }
        }
    }

    private void fireUpdatedStoryCardEvent(StoryCard storycard) {
        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).updatedStoryCard(storycard);
            }
        }
    }

    /***************************************************************************
     * LISTENER *
     **************************************************************************/
    public synchronized void addPlannerDataChangeListener(PlannerDataChangeListener listener) {

        this.plannerDataChangeListeners.add(PlannerDataChangeListener.class, listener);
    }

    public void connect() {
        // reload the current project to ensure that all clients are informed
        // about it
        Project p = (Project)synPer.getProject().clone();
        fireCreatedProjectEvent(p);
    }

    // For local mode this method returns false
    public boolean connected() {
        return false;
    }

    /***************************************************************************
     * CREATE *
     * 
     * @throws ForbiddenOperationException
     **************************************************************************/
    public synchronized void createBacklog(int width, int height, int locationX, int locationY) throws ForbiddenOperationException {

        this.fireCreatedBacklogEvent(synPer.createBacklog(width, height, locationX, locationY));

    }

    public synchronized void createIteration(String name, String description, int width, int height, int locationX, int locationY,
            float availableEffort, Timestamp startDate, Timestamp endDate, float rotationAngle, boolean rallyID) {

        this.fireCreatedIterationEvent(synPer.createIteration(name, description, width, height, locationX, locationY, availableEffort, startDate,
                endDate,rotationAngle,rallyID));
    }

    public synchronized void createProject(String name) throws ForbiddenOperationException, NotConnectedException{

        this.fireCreatedProjectEvent(synPer.createProject(name));

    }

    public synchronized void createStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String str, String color, String cardOwner,float rotationAngle,boolean rallyID,String fitId)
            throws IndexCardNotFoundException {

        this.fireCreatedStoryCardEvent(synPer.createStoryCard(name, description, width, height, locationX, locationY, parentid, bestCaseEstimate,
                mostlikelyEstimate, worstCaseEstimate, actualEffort, str, color,cardOwner, rotationAngle,rallyID,fitId));

    }

    /***************************************************************************
     * DELETE *
     **************************************************************************/
    public synchronized void deleteBacklog(long id) throws ForbiddenOperationException {
        throw new ForbiddenOperationException("You cannot delete the Backlog without deleting the Project!");
    }

    public synchronized void deleteIteration(long id) throws IndexCardNotFoundException {

        Iteration i = null;
        try {
            i = (Iteration) synPer.deleteCard(id);
            this.fireDeletedIterationEvent(i);
        }
        catch (ForbiddenOperationException e) {
            throw new IndexCardNotFoundException("Card with id=" + id + " is not an Iteration", id);
        }
    }

    public synchronized void deleteStoryCard(long id) throws IndexCardNotFoundException {
        StoryCard sc;
        try {
            sc = (StoryCard) synPer.deleteCard(id);
            this.fireDeletedStoryCardEvent(sc);
        }
        catch (ForbiddenOperationException e) {
            throw new IndexCardNotFoundException("Card with id=" + id + " is not a StoryCard",id);
        }

    }
    
    public synchronized void deleteOwner(TeamMember teamMember) {
        synPer.deleteOwner(teamMember);
        this.fireDeletedOwnerEvent(teamMember);
    }

    public boolean disconnect() {
        return true;
    }

    // For local mode this method does nothing
    public void downloadFile(String path, int recognizeID) throws NotConnectedException {

    }

    public synchronized void getProjectNames() {
        this.fireGotProjectNamesEvent(synPer.getProjectNames());
    }

    public PersisterToXML getSynPer() {
        return (PersisterToXML)synPer;
    }

    public synchronized void load(String projectName) throws CouldNotLoadProjectException {

    	try{
    		try {
				synPer.load(projectName);
			} catch (RemoteException e) {
				// will never get here, written just for consistency with distributed mode
			}
    	}catch(CouldNotLoadProjectException e){
    		try {
				this.fireCreatedProjectEvent(synPer.load("ProjectFile"));
			} catch (RemoteException e1) {
				util.Logger.singleton().error(e);
			}
    		throw new CouldNotLoadProjectException("", e); 
    	}
    	try {
			this.fireCreatedProjectEvent(synPer.load(projectName));
		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
		}

    }

    public synchronized void load(String projectName, Timestamp start, Timestamp end) throws CouldNotLoadProjectException {

        try {
			this.fireCreatedProjectEvent(synPer.load(projectName, start, end));
		} catch (RemoteException e) {
			util.Logger.singleton().error(e);
		}

    }

    /***************************************************************************
     * MOVE STORYCARD BETWEEN PARENTS *
     * 
     * @throws ForbiddenOperationException
     **************************************************************************/
    public synchronized void moveStoryCardToNewParent(StoryCard sc, long newparentid, int locationX, int locationY,float rotation) throws IndexCardNotFoundException {
        this.fireMovedStoryCardToNewParentEvent(synPer.moveStoryCardToNewParent(sc.getId(), sc.getParent(), newparentid, locationX, locationY,rotation));

    }

    public synchronized void removePlannerDataChangeListener(PlannerDataChangeListener listener) {

        this.plannerDataChangeListeners.remove(PlannerDataChangeListener.class, listener);
    }

    public void sendLiveTextUpdate(IndexCardLiveUpdate data) {
    }

    public void setSynPer(PersisterToXML synPer) {
        this.synPer = synPer;
    }

    public void undeleteBacklog(Backlog backlog) throws NotConnectedException, ForbiddenOperationException {
    }

    public void undeleteIteration(Iteration iteration) throws ForbiddenOperationException {
        try {
            this.fireUndeletedIterationEvent((Iteration) synPer.undeleteCard(iteration));
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }

    public void undeleteProject(Project project) throws NotConnectedException, ForbiddenOperationException {
    }

    public void undeleteStoryCard(StoryCard sc) throws NotConnectedException, IndexCardNotFoundException, ForbiddenOperationException {
        this.fireUndeletedStorycCardEvent((StoryCard) synPer.undeleteCard(sc));
    }

    /***************************************************************************
     * UPDATE BACKLOG *
     * 
     * @throws
     **************************************************************************/
    public synchronized void updateBacklog(Backlog backlog) throws IndexCardNotFoundException {

        this.fireUpdatedBacklogEvent((Backlog) synPer.updateCard(backlog));

    }
    
    /***************************************************************************
     * UPDATE Legend *
     * 
     * 
     **************************************************************************/
     public synchronized void updateLegend(Legend legend){
    	 synPer.updateLegend(legend);
     }
     public synchronized void updateOwner(TeamMember teamMember){
    	 synPer.updateOwner(teamMember);
     }
     
    /***************************************************************************
     * UPDATE ITERATION *
     * 
     * @throws
     **************************************************************************/
    public synchronized void updateIteration(Iteration iteration) throws IndexCardNotFoundException {
        this.fireUpdatedIterationEvent((Iteration) synPer.updateCard(iteration));

    }

    /***************************************************************************
     * UPDATE STORYCARD *
     * 
     * @throws ForbiddenOperationException
     **************************************************************************/
    public synchronized void updateStoryCard(StoryCard sc) throws IndexCardNotFoundException {
        this.fireUpdatedStoryCardEvent((StoryCard) synPer.updateCard(sc));
    }

    // For local mode this method does nothing
    public void uploadFile(String path,int recognizeID) throws NotConnectedException {

    }

	public void updateProject(Project project) {
	}

	public void arrangeProject(Project project) {
		this.fireArrangedProjectEvent(synPer.arrangeProject(project));		
	}



	private void fireArrangedProjectEvent(Project project) {

        Object[] listeners = plannerDataChangeListeners.getListenerList();
        // loop through each listener and pass on the event if needed
        int numListeners = listeners.length;
        for (int i = 0; i < numListeners; i += 2) {
            if (listeners[i] == PlannerDataChangeListener.class) {
                // pass the event to the listeners event dispatch method
                ((PlannerDataChangeListener) listeners[i + 1]).arrangeProject(project);
            }
        }
    
		
	}



	public void synchronizedProject() {
	}



	public void login(String userName, String password, String url) throws NotConnectedException {
	}

	public void connectToRally(String string) {
	}

}

package cards.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


import mouse.remote.RemoteMouseModel;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import applicationWorkbench.uielements.ProgressBarThread;

import persister.IndexCardNotFoundException;
import persister.data.Iteration;
import persister.data.Project;
import persister.xml.OutboundConnectionThread;


import cards.CardConstants;

public class ProjectModel extends AbstractRootModel {

    private static final long serialVersionUID = 1L;

    /** Property ID's to use when a Child is added & removed from the root */
    public static final String CHILD_ADDED_PROP = "TableModel.ChildAdded";

    public static final String CHILD_REMOVED_PROP = "TableModel.ChildRemoved";

    public static final String UPDATED_PROJECT = "TableModel.UpdatedProject";

    private BacklogModel backlogModel;

    private List<IterationCardModel> iterationModelList = new Vector<IterationCardModel>();
    private List<StoryCardModel> storyCardModelList = new Vector<StoryCardModel>();
    private List<StoryCardYCoordinate> storyCardsYCoord = new ArrayList<StoryCardYCoordinate>();
    private List<DrawingAreaModel> drawingAreaModelList = new Vector<DrawingAreaModel>();
    private Project projectDataObject;
    private static ProjectModel currentProject;
    private Dimension dragMoveDelta = new Dimension();
    private List<IndexCardModel> remoteMouseList = new ArrayList<IndexCardModel>();

    
    public ProjectModel(){
    	setCurrentProject(this);
    }
    
    @SuppressWarnings("unchecked")
    public void addNewIteration(IterationCardModel iterationCardModel) {

    	Calendar endTime = Calendar.getInstance();
    	endTime.add(Calendar.WEEK_OF_MONTH, 2);
        Timestamp endDate = new Timestamp(endTime.getTimeInMillis());
        String newEndDate = endDate.toString();

        iterationCardModel.setIterationEndDateIncomming(newEndDate);

        iterationCardModel.setProjectModel(this);
        
        this.getProjectDataObject().addIteration(iterationCardModel.getDataObject());
        iterationModelList.add(iterationCardModel);
        
        this.firePropertyChange(CHILD_ADDED_PROP, null, iterationCardModel);
    }
    
    @SuppressWarnings("unchecked")
    public void createIterationForInitialLoad(IterationCardModel iterationCardModel) {

        iterationCardModel.setProjectModel(this);
        
        
        iterationModelList.add(iterationCardModel);
        
        this.firePropertyChange(CHILD_ADDED_PROP, null, iterationCardModel);
    }
    
    public void addDrawingArea(DrawingAreaModel model){
    	model.setProjectModel(this);
    	drawingAreaModelList.add(model);
    	this.firePropertyChange(CHILD_ADDED_PROP, null, model);
    }
    
    

    @SuppressWarnings("unchecked")
    public void addRemoteMouseIncomming(RemoteMouseModel remoteMouseModel) {

        remoteMouseList.add(remoteMouseModel);
        this.firePropertyChange(CHILD_ADDED_PROP, null, remoteMouseModel);

    }

    public void cleanUpPlanningArea() {

    }

    public IndexCardModel findCard(long id2) throws IndexCardNotFoundException {
    	for (Iterator<StoryCardModel> iterator = this.getStoryCardModelList().iterator(); iterator.hasNext();) {
            StoryCardModel scm = iterator.next();
            if (scm.getStoryCard().getId() == id2) {
                return scm;
            }
        }
    	if (backlogModel != null) {
            if (backlogModel.getBacklog().getId() == id2)
                return backlogModel;

            for (Iterator<StoryCardModel> iterator = backlogModel.getChildrenList().iterator(); iterator.hasNext();) {
                StoryCardModel scm = iterator.next();
                if (scm.getStoryCard().getId() == id2) {
                    return scm;
                }
            }
        }// end if for backlog

        for (Iterator<IterationCardModel> iter = iterationModelList.iterator(); iter.hasNext();) {
            IterationCardModel element = iter.next();
            if (element.getIterationDataObject().getId() == id2) {
                return element;
            }
            List<StoryCardModel> storyCardModelList = element.getChildrenList();
            for (Iterator<StoryCardModel> iter2 = storyCardModelList.iterator(); iter2.hasNext();) {
                StoryCardModel scm = iter2.next();
                if (scm.getStoryCard().getId() == id2) {
                    return scm;
                }
            }
        }
        // no card was found
        throw new IndexCardNotFoundException("Could not find index card with id=" + id2, id2);
    }

    public BacklogModel getBacklogModel() {
        return backlogModel;
    }

    public List<ContainerModel> getContainers() {
        List<ContainerModel> containerCardsList = new ArrayList<ContainerModel>();
        containerCardsList.addAll(this.iterationModelList);
        return containerCardsList;
    }

    public List<IterationCardModel> getIterations() {
        return iterationModelList;
    }

    public Project getProjectDataObject() {
        return projectDataObject;
    }

    public List<IndexCardModel> getRemoteMice() {
        return remoteMouseList;
    }

    /** ******************************************************************************** */

    public void organizeBacklogs() {

        int backlogNewY = 20;

        int width = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacBacklogWidth : CardConstants.WindowsBacklogWidth;
        int height = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacBacklogHeight
                : CardConstants.WindowsBacklogHeight;
        backlogModel.setSize(new Dimension(width, height));
        backlogModel.setLocation(new Point(20, backlogNewY));
        backlogNewY += (20 + CardConstants.WindowsBacklogHeight);

    }

    public void organizeIterationsByEndDate() {
        ArrayList<IterationCardModel> iterations = new ArrayList<IterationCardModel>();

        Comparator<IterationCardModel> dateCompare = new Comparator<IterationCardModel>() {
            public int compare(IterationCardModel o1, IterationCardModel o2) {
                // Year
                return o1.compareEndDateTo(o2);
            }
        };

        for (int i = 0; i < this.iterationModelList.size(); i++) {
            if (this.iterationModelList.get(i) instanceof IterationCardModel) {
                iterations.add((IterationCardModel) this.iterationModelList.get(i));
            }
        }

        Collections.sort(iterations, dateCompare);

        int iterationNewY = 20;
        for (IterationCardModel iter : iterations) {
            int width = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationCardWidth
                    : CardConstants.WindowsIterationCardWidth;
            int height = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationCardHeight
                    : CardConstants.WindowsIterationCardHeight;
            iter.setSize(new Dimension(width, height));
            iter.setLocation(new Point(40 + width, iterationNewY));
            iterationNewY += (20 + height);
        }
    }

    public void removeAllMiceIncoming() {
        for (Object card : this.remoteMouseList) {
            if (card instanceof RemoteMouseModel) {
                this.removeRemoteMouseIncoming((RemoteMouseModel) card);
            }
        }
    }

    public void removeIteration(IterationCardModel iterationCardModel) {
        this.iterationModelList.remove(iterationCardModel);
        this.projectDataObject.getIterationChildren().remove(iterationCardModel.getIterationDataObject());
        this.firePropertyChange(CHILD_REMOVED_PROP, null, iterationCardModel);
    }

    public void removeDrawingArea(DrawingAreaModel model) {
        this.drawingAreaModelList.remove(model);
      //  this.projectDataObject.getIterationChildren().remove(iterationCardModel.getIterationDataObject());
        this.firePropertyChange(CHILD_REMOVED_PROP, null, model);
    }

    public boolean removeRemoteMouseIncoming(RemoteMouseModel mouse) {
        if ((mouse != null) && this.remoteMouseList.remove(mouse)) {
            this.firePropertyChange(CHILD_REMOVED_PROP, null, mouse);
            return true;
        }
        return false;
    }
   
    public boolean removeRemoteMouseIncoming(OutboundConnectionThread client) {
    	return removeRemoteMouseIncoming(client.getId());
    }
    
    public boolean removeRemoteMouseIncoming(long client) {
    	for(IndexCardModel card : getRemoteMice()){
    		RemoteMouseModel mouse = (RemoteMouseModel) card;
    		//System.out.println("MouseID: " + mouse.getClientId() + " vs ClientID: " + client);
    		if(mouse.getClientId() == client){
    			return removeRemoteMouseIncoming(mouse);
    		}
    	}
    	return false;
    }
   

    public void setNewBacklogModel(BacklogModel backlogModel) {
        this.backlogModel = backlogModel;
        this.firePropertyChange(CHILD_ADDED_PROP, null, backlogModel);
    }

    public void setIterationModelList(List<IterationCardModel> iterationModelList) {
        this.iterationModelList = iterationModelList;
    }

    public void setProjectDataObject(Project project) {
        this.projectDataObject = project;
    }

    public void setRemoteMouseList(List<IndexCardModel> remoteMouseList, RemoteMouseModel toRemove) {
        this.remoteMouseList = remoteMouseList;
        this.firePropertyChange(CHILD_REMOVED_PROP, toRemove, null);
    }

    public void updateChildrenFigure() {
        this.firePropertyChange(CHILD_ADDED_PROP, null, null);
    }

    public void updateProject() {
        this.firePropertyChange(UPDATED_PROJECT, null, this);
        
        if(ProgressBarThread.getPgth()!= null){
			ProgressBarThread.getInstatnce().stop();
			ProgressBarThread.setPgth(null);
        }
    }
    
    public List<DrawingAreaModel> getDrawingAreas(){
    	return this.drawingAreaModelList;
    }

	public void addStoryCardAsChild(StoryCardModel storyCardModel) {
		storyCardModelList.add(storyCardModel);			
		//this.updateChildPriorities();
		this.firePropertyChange(CHILD_ADDED_PROP, null, storyCardModel);
		
	}

	public List<StoryCardModel> getStoryCardModelList() {
		return storyCardModelList;
	}

	public void setStoryCardModelList(List<StoryCardModel> storyCardModelList) {
		this.storyCardModelList = storyCardModelList;
	}

	public void addChildIncommingOnLoad(StoryCardModel storyCardModel) {
		if (storyCardModel.getId() != 0) {
			this.storyCardModelList.add(storyCardModel);
		}	
		this.updateChildPriorities();
		this.firePropertyChange(CHILD_ADDED_PROP, null, storyCardModel);
		
	}
	
	public void updateStorycard(StoryCardModel storyCardModel) {
//		if (storyCardModel.getId() != 0) {
//			this.storyCardModelList.add(storyCardModel);
//		}	
		this.updateChildPriorities();
		this.firePropertyChange(CHILD_ADDED_PROP, null, storyCardModel);
		
	}

	public void removeChildStoryCard(StoryCardModel child) {
		this.storyCardModelList.remove(child);		
		//this.updateChildPriorities();
		this.firePropertyChange(CHILD_REMOVED_PROP, null, child);	
	}

	public long getId() {
		
		return this.getId();
	}

	public void setDragMoveDelta(Dimension d) {
		 this.dragMoveDelta = d;
		
	}
	
	public void updateCardLocation() {
        Point oldLocation = new Point();
        Point newLocation = new Point();
        Dimension pointChange = new Dimension();

        oldLocation = this.getBacklogModel().getLocation();
        pointChange = this.dragMoveDelta;
        newLocation.x = oldLocation.x + pointChange.width;
        newLocation.y = oldLocation.y + pointChange.height;
        this.getBacklogModel().setLocation(newLocation);
    }

	public synchronized void updateChildPriorities() {
		for (StoryCardYCoordinate scy : this.storyCardsYCoord) {
			this.storyCardModelList.get(scy.getIndex()).setPriority(scy.getPriority());
			
		}
		
	}

	public void updateIteration(IterationCardModel iterationCardModel, Iteration iteration) {
		if (iterationCardModel != null) {
			Iteration oldIterationDataObject = null;
			try {
				oldIterationDataObject = (Iteration) this.getProjectDataObject().findCard(iterationCardModel.getIterationDataObject().getId());
				this.getProjectDataObject().removeIteration(oldIterationDataObject);
				this.getProjectDataObject().addIteration(iteration);
				iterationCardModel.setIterationDataObject(iteration);
			} catch (IndexCardNotFoundException e) {
				
				util.Logger.singleton().error(e);
			}
			
		}
		this.firePropertyChange(CHILD_ADDED_PROP, null, this);
		
	}

	public static void clearProjectModel(ProjectModel project){
		
		ArrayList<StoryCardModel> toRemoveStoryCards = new ArrayList<StoryCardModel>();
		
		for(StoryCardModel card: project.storyCardModelList){
			toRemoveStoryCards.add(card);
		}
		for(StoryCardModel card: toRemoveStoryCards)
			project.removeChildStoryCard(card);
		
		
		ArrayList<IterationCardModel> toRemoveIterations = new ArrayList<IterationCardModel>();
		for(IterationCardModel card: project.iterationModelList)
			toRemoveIterations.add(card);
		
		for(IterationCardModel card: toRemoveIterations)
			project.removeIteration(card);
	}

	public static ProjectModel getCurrentProject() {
		return currentProject;
	}

	public static void setCurrentProject(ProjectModel currentProject) {
		ProjectModel.currentProject = currentProject;
	}
}

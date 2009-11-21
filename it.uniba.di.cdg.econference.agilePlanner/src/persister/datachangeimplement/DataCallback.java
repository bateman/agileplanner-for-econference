
package persister.datachangeimplement;

import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import applicationWorkbench.Editor;
import applicationWorkbench.uielements.ChangePersisterConnectDialogComoBox;
import applicationWorkbench.uielements.ConfigurationDialog;
import applicationWorkbench.uielements.PersisterConnectDialog;
import applicationWorkbench.uielements.ProgressBarThread;
import cards.model.BacklogModel;
import cards.model.ContainerModel;
import cards.model.IndexCardModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;
import filesystemaccess.FileSystemUtility;
import mouse.remote.RemoteMouseModel;
import persister.IndexCardLiveUpdate;
import persister.IndexCardNotFoundException;
import persister.Event;
import persister.PlannerDataChangeListener;
import persister.PlannerUIChangeListener;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.DisconnectDataObject;
import persister.factory.Settings;

public class DataCallback implements PlannerDataChangeListener, PlannerUIChangeListener {

    private CombinedTemplateCreationEntry combinedCreateEntry = null;

    private long ktime = 0;

    private long ltime = 0;

    private HashMap<Long, RemoteMouseModel> miceList = new HashMap<Long, RemoteMouseModel>();

    private ProjectModel projectModel;

    private Timestamp start, end;

    public char ch;

    public Editor editor;

    public Point keyPoint = new Point(0, 0);

    public Point mouseMovePoint = new Point(0, 0);

    public DataCallback() {
      
        this.start = new Timestamp(Date.valueOf("1000-1-1").getTime());
        this.end = new Timestamp(Date.valueOf("3000-1-1").getTime());
    }

    //used for UI models testing only
    public DataCallback(ProjectModel projectModel) {
        this.projectModel = projectModel;
        this.start = new Timestamp(Date.valueOf("1000-1-1").getTime());
        this.end = new Timestamp(Date.valueOf("3000-1-1").getTime());
    }

    public void asynchronousException(Exception exception, int messageType) {
    }

    public void broughtToFront(long id) {
    }



    public void createdBacklog(Backlog backlog) {
        BacklogModel backlogModel = new BacklogModel(backlog);

        if (projectModel.getBacklogModel() == null) {
            projectModel.setNewBacklogModel(backlogModel);

        }
    }

    public void createdIteration(Iteration iteration) {
        IterationCardModel iterationCardModel = new IterationCardModel(iteration);

        projectModel.addNewIteration(iterationCardModel);

    }

    // create new models for the data objects in project
    public void createdProjectOnInitialLoadFromServer(Project project) {    	
    	
    	if(projectModel != null){
    		//we are in the wrong place!
    		createProjectOnSubsequentLoadsFromServer(project);
    	
    		return;
    	}
        projectModel = new ProjectModel();
        ProjectModel.clearProjectModel(projectModel);
        projectModel.setProjectDataObject(project);

        Backlog backlog = project.getBacklog();

        if (backlog != null) {

            BacklogModel backlogModel = new BacklogModel(backlog);
            backlogModel.setLocation(new Point(0,0));
            backlogModel.setSize(new Dimension(0,0));
            backlogModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
            backlogModel.setProjectModel(projectModel);

            List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(backlog.getStoryCardChildren());

            if (!(listStoryCardChildren.isEmpty())) {

                for (StoryCard storycard : listStoryCardChildren) {

                    StoryCardModel storyCardModel = new StoryCardModel(storycard);
                    
                    projectModel.addChildIncommingOnLoad(storyCardModel);
                    

                    storyCardModel = null;
                }
            }

        }

        if (project.getIterationChildren() != null) {

            for (Iteration iteration : project.getIterationChildren()) {

                IterationCardModel iterationCardModel = new IterationCardModel(iteration);
                iterationCardModel.setIterationEndDateIncomming(iteration.getEndDate().toString());
                iterationCardModel.setIterationStartDateIncomming(iteration.getStartDate().toString());
                
                iterationCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
                List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(iteration.getStoryCardChildren());

                if (!(listStoryCardChildren.isEmpty())) {
                    for (StoryCard storycard : listStoryCardChildren) {
                        StoryCardModel storyCardModel = new StoryCardModel(storycard);
                        iterationCardModel.addChildIncommingOnLoad(storyCardModel);
                    }
                }
                this.projectModel.createIterationForInitialLoad(iterationCardModel);
            }

        }
        editor.setProjectModel(projectModel);
	}

		
    public void createProjectOnSubsequentLoadsFromServer(Project project) {
		ProjectModel.clearProjectModel(projectModel);
		
		projectModel.updateProject();
		
		projectModel.setProjectDataObject(project);

		Backlog backlog = project.getBacklog();

		if (backlog != null) {

		    BacklogModel backlogModel = new BacklogModel(backlog);
		    backlogModel.setLocation(new Point(0,0));
            backlogModel.setSize(new Dimension(0,0));
		    backlogModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
		    backlogModel.setProjectModel(projectModel);

		    List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(backlog.getStoryCardChildren());

		    if (!(listStoryCardChildren.isEmpty())) {
		        for (StoryCard storycard : listStoryCardChildren) {
		            StoryCardModel storyCardModel = new StoryCardModel(storycard);
		            projectModel.addChildIncommingOnLoad(storyCardModel);
		            storyCardModel = null;
		        }
		    }
		}
		
		if (project.getIterationChildren() != null) {

		    for (Iteration iteration : project.getIterationChildren()) {

		        IterationCardModel iterationCardModel = new IterationCardModel(iteration);
		        iterationCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
		        List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(iteration.getStoryCardChildren());

		        if (!(listStoryCardChildren.isEmpty())) {
		            for (StoryCard storycard : listStoryCardChildren) {
		            	
		                StoryCardModel storyCardModel = new StoryCardModel(storycard);
		                iterationCardModel.addChildIncommingOnLoad(storyCardModel);

		            }
		        }
		        this.projectModel.createIterationForInitialLoad(iterationCardModel);

		    }
		}
		
		editor.updateTitleTab(this.projectModel.getProjectDataObject().getName()+ ":" + Settings.getInitialDis());
		
		
		  
		
		projectModel.updateProject();
    }
    
//  create new models for the data objects in project
    public void arrangeProject(Project project) {    	
        projectModel.setProjectDataObject(project);    
        Backlog backlog = project.getBacklog();

        if (backlog != null) {
        	projectModel.getBacklogModel().setBacklogDataObject(backlog);            
        	projectModel.getBacklogModel().setTimestamp(new Timestamp(System.currentTimeMillis()));
        	
            List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(backlog.getStoryCardChildren());
            if (!(listStoryCardChildren.isEmpty())) {
                for (StoryCard storyCard : listStoryCardChildren) {
                	 StoryCardModel storyCardModel = null;
                     try {
                         storyCardModel = (StoryCardModel) this.projectModel.findCard(storyCard.getId());
                         storyCardModel.setStoryCardDataObject(storyCard);
                         storyCardModel.setParent(projectModel.getBacklogModel());
                         storyCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
                     }
                     catch (IndexCardNotFoundException e) {
                        util.Logger.singleton().error(e);
                     }
                }
            }
        }
        if (project.getIterationChildren() != null) {
            for (Iteration iteration : project.getIterationChildren()) {
            	IterationCardModel iterationCardModel = null;
                try {
                    iterationCardModel = (IterationCardModel) projectModel.findCard(iteration.getId());
                    iterationCardModel.setIterationDataObject(iteration);
                    iterationCardModel.setIterationEndDateIncomming(iteration.getEndDate().toString());
                    iterationCardModel.setIterationStartDateIncomming(iteration.getStartDate().toString());
                    
                    iterationCardModel.setLocationIncomming(new Point(iteration.getLocationX(), iteration.getLocationY()));
		             // update the children
	                if (!(iteration.getStoryCardChildren().isEmpty())) {
	                    for (StoryCard storyCard : iteration.getStoryCardChildren()) {
	                      // this.updatedStoryCardForContainerCard(storycard);
	                    	StoryCardModel storyCardModel = null;
	                        try {
	                            storyCardModel = (StoryCardModel) this.projectModel.findCard(storyCard.getId());
	                            storyCardModel.setStoryCardDataObject(storyCard);
	                        }
	                        catch (IndexCardNotFoundException e) {
	                            util.Logger.singleton().error(e);
	                        }
	                    }
	                    iterationCardModel.updateChildPriorities();
	                    iterationCardModel.updateValuesFromChildren();
	                }
                }catch (IndexCardNotFoundException e) {
                    util.Logger.singleton().error(e);
                }
            }
        }
        projectModel.updateChildrenFigure();
    }

    public void createdStoryCard(StoryCard storycard) {		
    	
    	 if ( storycard.getHandwritingImage()!=null)
   	  {
    		 storycard.setCurrentSideUp(storycard.HANDWRITING_SIDE);
   	  }
        StoryCardModel storyCardModel = new StoryCardModel(storycard);
        IndexCardModel parentCardModel = null;
        try {
            parentCardModel = projectModel.findCard(storycard.getParent());
        }
        catch (IndexCardNotFoundException e) {
           util.Logger.singleton().error(e);
        }        
        if (parentCardModel instanceof IterationCardModel) {
        	((IterationCardModel)parentCardModel).addNewChild(storyCardModel);
        }
        if (parentCardModel instanceof BacklogModel){
        	storyCardModel.setParent(this.projectModel.getBacklogModel());
        	projectModel.addStoryCardAsChild(storyCardModel);
        }
    }



    public void deletedIteration(long id) {
        
        IterationCardModel iterationCardModel = null;
        try {
            iterationCardModel = (IterationCardModel) projectModel.findCard(id);
            this.projectModel.removeIteration(iterationCardModel);
        } catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }

    public void deletedStoryCard(long id) {
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) this.projectModel.findCard(id);
        }
        catch (IndexCardNotFoundException e) {

            util.Logger.singleton().error(e);
        }
        if((storyCardModel.getParent()) instanceof IterationCardModel){
        	((IterationCardModel)storyCardModel.getParent()).removeChild(storyCardModel);
        }
        else
        	projectModel.removeChildStoryCard(storyCardModel);
        
       
    }
    
    public void deletedOwner(TeamMember teamMember) {
    }

   

    public void disconnectMouse(DisconnectDataObject data) {
    	try{
        List<IndexCardModel> children = projectModel.getRemoteMice();
        IndexCardModel toRemove = null;
        for (IndexCardModel model : children) {
            if (model instanceof RemoteMouseModel && ((RemoteMouseModel) model).getClientId() == data.getClientId()) {
                toRemove = model;
                break;
            }
        }
        children.remove(toRemove);
        projectModel.setRemoteMouseList(children, (RemoteMouseModel)toRemove);
    	}catch(Exception e){
    		util.Logger.singleton().error(e);
    	}

    }

    public void downloadedFile(boolean bool) {
    }

    public Timestamp getEnd() {
        return end;
    }

    public long getKtime() {
        return ktime;
    }

    public long getLtime() {
        return ltime;
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public Timestamp getStart() {
        return start;
    }

    public void gotProjectNames(List<String> str) {
         String[] temp = new String[str.size()];
    	for(int loop = 0; loop<str.size();loop++)
    	{
    		temp[loop] = str.get(loop);
    	}
    	ChangePersisterConnectDialogComoBox.gotProjectsNames(temp) ;

    }

    public void gotProjectNamesForLoginEvent(List<String> str) {

        String[] temp = new String[str.size()];
   	for(int loop = 0; loop<str.size();loop++){
   		temp[loop] = str.get(loop);
   	}
   	ChangePersisterConnectDialogComoBox.gotProjectsNamesForLoginEvent(temp) ;

   
		
	}

   

  

    public void liveTextUpdate(IndexCardLiveUpdate data) {
        IndexCardModel model = null;
        try {
            model = projectModel.findCard(data.getId());
        }
        catch (IndexCardNotFoundException e) {

           util.Logger.singleton().error(e);
        }
        if (model instanceof StoryCardModel) {
            if (data.getField() == IndexCardLiveUpdate.FIELD_TITLE)
                ((StoryCardModel) model).setNameIncomming(data.getText());
            else if (data.getField() == IndexCardLiveUpdate.FIELD_DESCRIPTION) {
                
                ((StoryCardModel) model).setDescriptionIncomming(data.getText());
            }else if (data.getField() == IndexCardLiveUpdate.FIELD_TESTTEXT){
            	
            	 ((StoryCardModel) model).setTestTextIncoming(data.getText());
            }
        }

        else
            if (model instanceof IterationCardModel) {
                if (data.getField() == IndexCardLiveUpdate.FIELD_TITLE)
                    ((IterationCardModel) model).setNameIncomming(data.getText());
                if (data.getField() == IndexCardLiveUpdate.FIELD_DESCRIPTION)
                    ((IterationCardModel) model).setDescriptionIncomming(data.getText());
            }
       

    }

    

    public void movedMouse(Event mm) {
    	try{
    		this.mouseMovePoint = new Point(mm.getLocationX(), mm.getLocationY());

    		RemoteMouseModel mouse;
    		if (miceList.containsKey(mm.getId())) {
    			mouse = miceList.get(mm.getId());
    			mouse.setLocation(new Point(mm.getLocationX(), mm.getLocationY()));
    		}
    		else {
    			mouse = new RemoteMouseModel(mm);
    			miceList.put(mm.getId(), mouse);
    			projectModel.addRemoteMouseIncomming(mouse);
    		}

    		List<IterationCardModel> children = projectModel.getIterations();
    		LinkedList<IndexCardModel> cards = new LinkedList<IndexCardModel>();
    		LinkedList<IndexCardModel> mice = new LinkedList<IndexCardModel>();
    		for (IndexCardModel model : children) {
    			if (model instanceof RemoteMouseModel) {
    				mice.add(model);
    			}
    			else {
    				cards.add(model);
    			}
    		}

    		ltime = System.currentTimeMillis();
    	}catch(Exception e){
    		System.out.println("Ignoring Mouse Move Event.");
    	}

    }// end of method

 

    public void movedStoryCardToNewParent(StoryCard movedStorycard) {
    	
    	
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) projectModel.findCard(movedStorycard.getId());
            ContainerModel storyCardModelOldParent = storyCardModel.getParent();
            storyCardModel.setStoryCardDataObject(movedStorycard);
            
            if (storyCardModelOldParent instanceof IterationCardModel){
            // removes both model and data it contained
            	((IterationCardModel)storyCardModelOldParent).removeChild(storyCardModel);           	
            }
            else{
            	
            	this.projectModel.removeChildStoryCard(storyCardModel);        	        	
            }
            
            IndexCardModel storyCardModelNewParent = null;
            try {
                storyCardModelNewParent = projectModel.findCard(movedStorycard.getParent());
            }
            catch (IndexCardNotFoundException e) {           
              util.Logger.singleton().error(e);
            }           
            
            // adds both model and data
            if (storyCardModelNewParent instanceof IterationCardModel) {
                ((IterationCardModel) storyCardModelNewParent).addChildSetParent(storyCardModel);
            }else
            {
            	storyCardModel.setParent(this.projectModel.getBacklogModel());
            	 this.projectModel.addStoryCardAsChild(storyCardModel);
            }
        }catch (IndexCardNotFoundException e) {            
            util.Logger.singleton().error(e);
        }
    }

    public void projectInXML(String xmlFileContents) {
        // TODO Auto-generated method stub

    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public void setProjectModel(ProjectModel table) {
        this.projectModel = table;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public void undeletedIteration(Iteration iteration) {
        
        IterationCardModel iterationCardModel = new IterationCardModel(iteration);
        iterationCardModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        List<StoryCard> listStoryCardChildren = new ArrayList<StoryCard>(iteration.getStoryCardChildren());

        if (!(listStoryCardChildren.isEmpty())) {
            for (StoryCard storycard : listStoryCardChildren) {

                StoryCardModel storyCardModel = new StoryCardModel(storycard);
                iterationCardModel.addChildIncommingOnLoad(storyCardModel);

            }// end of storycard for
        }// end of
        this.projectModel.addNewIteration(iterationCardModel);

    }

    public void undeletedStoryCard(StoryCard storycard) {
    	this.createdStoryCard(storycard);

    }

    public void updatedBacklog(Backlog backlog) {
        BacklogModel backlogModel = projectModel.getBacklogModel();
        backlogModel.setBacklogDataObject(backlog);

        backlogModel.setLocationIncomming(new Point(backlog.getLocationX(), backlog.getLocationY()));
        // update the children
        if (!(backlog.getStoryCardChildren().isEmpty())) {
            for (StoryCard storycard : backlog.getStoryCardChildren()) {
                this.updatedStoryCard(storycard);

            }
        }

        backlogModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        this.projectModel.updateChildrenFigure();
    }
    


    public void updatedIteration(Iteration iteration) {
        IterationCardModel iterationCardModel = null;
        try {
            iterationCardModel = (IterationCardModel) projectModel.findCard(iteration.getId());
        }
        catch (IndexCardNotFoundException e) {

           util.Logger.singleton().error(e);
        }
        this.projectModel.updateIteration(iterationCardModel, iteration);
       iterationCardModel.setIterationEndDateIncomming(iteration.getEndDate().toString());
       iterationCardModel.setIterationStartDateIncomming(iteration.getStartDate().toString());
        
        // update the children
        if (!(iteration.getStoryCardChildren().isEmpty())) {
            for (StoryCard storycard : iteration.getStoryCardChildren()) {
               this.updatedStoryCardForContainerCard(storycard);
            	
            }
        }
    }

    public void updatedProjectName(Project project) {
        // TODO Auto-generated method stub

    }

    public void updatedStoryCard(StoryCard storyCard) {
    	
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) this.projectModel.findCard(storyCard.getId());
        }
        catch (IndexCardNotFoundException e) {

        	util.Logger.singleton().error(e);
        }        
     
        ContainerModel cm = storyCardModel.getParent();
        if( cm instanceof IterationCardModel){
        	
        	((IterationCardModel)cm).updateChild(storyCardModel);
        	
        }else{ 	
        
        }
        
        storyCardModel.setStoryCardDataObject(storyCard);
 }
        

    public void updatedStoryCardForContainerCard(StoryCard storycard) {
    	
        StoryCardModel storyCardModel = null;
        try {
            storyCardModel = (StoryCardModel) this.projectModel.findCard(storycard.getId());
        }
        catch (IndexCardNotFoundException e) {
          util.Logger.singleton().error(e);
        }
        storyCardModel.setLocationIntern(new Point(storycard.getLocationX(), storycard.getLocationY()));
    }
    
    public void uploadedFile(boolean bool) {
    }



	
	public void lostConnectionEvent() {
		
		String path = Settings.getProjectLocationLocalMode();
		String fileName = this.projectModel.getProjectDataObject().getName();
        File file = new File(path +File.separator+ fileName + ".xml");		
		FileSystemUtility.getFileSystemUtility().saveFile(this.projectModel, file);
		String absoluteFileName = file.getAbsolutePath();
		this.editor.resetPersisterToLocalMode("dummy");
		
		MessageBox mbox = new MessageBox(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR);
		mbox.setText("Connection problem.");
		mbox.setMessage("Could not retrieve project model data from server. Switching to local mode now. File will be saved as "
						+ absoluteFileName);
		mbox.open();
		
		this.projectModel.removeAllMiceIncoming();
	}


	

	public void updatedLegend(Legend leg) {
		
		this.projectModel.getProjectDataObject().setLegend(leg);	
		
		
		 Display.getDefault().asyncExec(new Runnable() {
             public void run() {
            	 
            	 IWorkbenchWindow window = DataCallback.this.editor.getSite().getWorkbenchWindow();
            	 Editor ed = (Editor) ConfigurationDialog.resetEditor(window,DataCallback.this.projectModel);
            	 DataCallback.this.editor = ed;
            	 DataCallback.this.editor.setProjectModel(DataCallback.this.projectModel);
             }
		 });
		
		
		
	}
	public void updatedOwner(TeamMember teamMember) {
	
		this.projectModel.getProjectDataObject().removeOwner(teamMember);	
		this.projectModel.getProjectDataObject().addTeamMember(teamMember);	
			
		
		 Display.getDefault().asyncExec(new Runnable() {
	         public void run() {
	        	 
	        	 IWorkbenchWindow window = DataCallback.this.editor.getSite().getWorkbenchWindow();
	        	 Editor ed = (Editor) ConfigurationDialog.resetEditor(window,DataCallback.this.projectModel);
	        	 DataCallback.this.editor = ed;
	        	 DataCallback.this.editor.setProjectModel(DataCallback.this.projectModel);
	         }
		 });
		
		
		
	}

	public void createdOwner(TeamMember teamMember) {
	}

}

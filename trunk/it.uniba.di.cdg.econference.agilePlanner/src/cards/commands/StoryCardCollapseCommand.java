package cards.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.Iteration;
import persister.data.StoryCard;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.ContainerModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class StoryCardCollapseCommand extends Command {
    HashMap<Long, java.lang.Integer> oldStoryCardsHeight = new HashMap<Long, java.lang.Integer>();

    ArrayList<StoryCardModel> oldStoryCards = new ArrayList<StoryCardModel>();

    private ProjectModel projectModel = null;

    public StoryCardCollapseCommand(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    public void initialize() {

        ProjectModel table = projectModel;
       
        ArrayList child = (ArrayList) table.getContainers();
        for (int i = 0; i < child.size(); i++) {
            oldStoryCards.addAll(((ContainerModel) child.get(i)).getChildrenList());
        }
        oldStoryCards.addAll(projectModel.getStoryCardModelList());
        
        for (StoryCardModel sc : oldStoryCards) {
            oldStoryCardsHeight.put(sc.getId(), sc.getHeight());
        }
        
    }

    @Override
    public void execute() {
        ArrayList<StoryCardModel> storyCardsOnCanvass = new ArrayList<StoryCardModel>();
        
        storyCardsOnCanvass.addAll(projectModel.getStoryCardModelList());
        
        ArrayList<StoryCard> collapsedStoryCardsOnCanvass = new ArrayList<StoryCard>();
        
        for (StoryCardModel card : storyCardsOnCanvass) {
        	
        	if(!card.getColor().equalsIgnoreCase("transparent")){
        	card.setHeight(CardConstants.WindowsStoryCardSmallHeight);
        	card.setHeightClientArea(card.getSize().height);
        	collapsedStoryCardsOnCanvass.add(card.getStoryCard());
        	}
        }
        this.projectModel.getProjectDataObject().getBacklog().setStoryCardChildren(collapsedStoryCardsOnCanvass);
     
//      Arrange Iterations

        ArrayList<Iteration> collapsedStoryCardIterations = new ArrayList<Iteration>();
        ArrayList<IterationCardModel> iterations = new ArrayList<IterationCardModel>();
        iterations.addAll(this.projectModel.getIterations());
      
        for (IterationCardModel iter : iterations) {            
			// StoryCards for iterations
			
			 ArrayList<StoryCard> collapsedStoryCardsOnIteration = new ArrayList<StoryCard>();        
	        for (StoryCardModel card : iter.getChildrenList()) {
	        	card.setHeight(CardConstants.WindowsStoryCardSmallHeight);
	        	card.setHeightClientArea(card.getSize().height);
	        	collapsedStoryCardsOnIteration.add(card.getStoryCard());
	        }
		 
		iter.getIterationDataObject().setStoryCardChildren(collapsedStoryCardsOnIteration);
		collapsedStoryCardIterations.add(iter.getIterationDataObject());		
   }

        this.projectModel.getProjectDataObject().setIterationChildren(collapsedStoryCardIterations);
   PersisterFactory.getPersister().arrangeProject(this.projectModel.getProjectDataObject());
       
   }

    @Override
    public void redo() {
        
        ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();
        ProjectModel table = projectModel;

        storyCards.addAll(projectModel.getStoryCardModelList());
        ArrayList child = (ArrayList) table.getContainers();
        for (int i = 0; i < child.size(); i++) {
            storyCards.addAll(((ContainerModel) child.get(i)).getChildrenList());
        }
        for (StoryCardModel card : storyCards) {
            try {                
                card.getStoryCard().setHeight(CardConstants.WindowsStoryCardSmallHeight);
                PersisterFactory.getPersister().updateStoryCard(card.getStoryCard());
            }
            catch (NotConnectedException e) {
                
                util.Logger.singleton().error(e);
            }
            catch (IndexCardNotFoundException e) {
                
                util.Logger.singleton().error(e);
            }
        }
    }

    @Override
    public void undo() {
        
        for (StoryCardModel card : oldStoryCards) {
            try {
                
                card.getStoryCard().setHeight(oldStoryCardsHeight.get(card.getId()));
                PersisterFactory.getPersister().updateStoryCard(card.getStoryCard());
            }
            catch (NotConnectedException e) {
               
                util.Logger.singleton().error(e);
            }
            catch (IndexCardNotFoundException e) {
               
                util.Logger.singleton().error(e);
            }
        }
    }
}

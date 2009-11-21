/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This class is the triger point from eclipse to the editor for adding a story card  to the project.(iter or backlog)
 *
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.BacklogModel;
import cards.model.IndexCardModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;
import cards.model.StoryCardModelAqua;
import cards.model.StoryCardModelBlue;
import cards.model.StoryCardModelGreen;
import cards.model.StoryCardModelGrey;
import cards.model.StoryCardModelKhaki;
import cards.model.StoryCardModelPeach;
import cards.model.StoryCardModelPink;
import cards.model.StoryCardModelRed;
import cards.model.StoryCardModelTransparent;
import cards.model.StoryCardModelWhite;
import cards.model.StoryCardModelYellow;

public class StoryCardCreateCommand extends Command {

    private StoryCardModel newCard;

    private ProjectModel parent_T;

    private IterationCardModel parent_I;

    private Rectangle bounds;

    private BacklogModel parent_B;

    private String parentType;

    private IndexCardModel parentCard = null;
    
    private static String initialStoryCardColor = "blue"; 

    public IndexCardModel getParentModel() {
        if ((this.parent_T == null) && (this.parent_B == null)) {
            this.parentCard = this.parent_I;
        }
        else
            if ((this.parent_T == null) && (this.parent_I == null)) {
                this.parentCard = this.parent_B;
            }
        return this.parentCard;
    }

    public StoryCardModel getStoryCardModel() {
        return this.newCard;
    }

    public StoryCardCreateCommand(StoryCardModel newCard, ProjectModel parent, Rectangle bounds) {
        this.newCard = newCard;
        this.parent_B = parent.getBacklogModel();
        this.bounds = bounds;
        this.setLabel("IndexCard creation");
    }

    public StoryCardCreateCommand(StoryCardModel newCard, IterationCardModel parent, Rectangle bounds) {
        this.newCard = newCard;
        this.parent_I = parent;
        this.bounds = bounds;
        this.setLabel("IndexCard creation");
    }

    @Override
    public void execute() {
    
        this.newCard.setLocation(this.bounds.getLocation());
        Dimension size = this.bounds.getSize();
        // Forces the cards to be the predefined size.
        if ((size.width > 10) && (size.height > 10)) {
            this.newCard.setSize(size);
        }

        if ((this.parent_T == null) && (this.parent_B == null)) {
            this.parentType = "iteration";
        }else{
            if ((this.parent_T == null) && (this.parent_I == null)) {
                this.parentType = "backlog";
            }
            else if ((this.parent_I == null) && (this.parent_B == null)) {
                this.parentType = "table";
            }
        }
        if ((this.parent_T == null) && (this.parent_B == null)) {
            this.parentCard = this.parent_I;
        } else if ((this.parent_T == null) && (this.parent_I == null)) {
            this.parentCard = this.parent_B;
        }

        
        try {
        	if(newCard instanceof StoryCardModelBlue ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelBlue)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId()
                    );
        }
        	if(newCard instanceof StoryCardModelTransparent){

                PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                        this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                        Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                        Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelTransparent)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId()
                        );
            
        	}
        if(newCard instanceof StoryCardModelRed ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelRed)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }
        if(newCard instanceof StoryCardModelGreen ){
    		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelGreen)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }if(newCard instanceof StoryCardModelGrey ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelGrey)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }if(newCard instanceof StoryCardModelKhaki ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelKhaki)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }if(newCard instanceof StoryCardModelYellow ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelYellow)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }if(newCard instanceof StoryCardModelWhite ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelWhite)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }if(newCard instanceof StoryCardModelAqua ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelAqua)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }if(newCard instanceof StoryCardModelPink ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelPink)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }if(newCard instanceof StoryCardModelPeach ){
        		
            PersisterFactory.getPersister().createStoryCard(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y, this.parentCard.getId(),
                    Float.valueOf(this.newCard.getBestCaseEstimate()), Float.valueOf(this.newCard.getMostLikelyEstimate()),
                    Float.valueOf(this.newCard.getWorstCaseEstimate()), Float.valueOf(this.newCard.getActualEffort()), this.newCard.getStatus(),((StoryCardModelPeach)newCard).getColor(), this.newCard.getCardOwner(), 0,false,this.newCard.getFitId());
        }
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
        this.newCard.setLocation(this.bounds.getLocation());
        this.bounds.getSize();
        // Forces the cards to be the predefined size.
        if ((size.width > 10) && (size.height > 10)) {
            this.newCard.setSize(size);
        }

    }

    @Override
    public void redo() {
    	this.newCard.undeleteStoryCard();
    }

    @Override
    public void undo() {
        this.bounds = new Rectangle(this.newCard.getLocation(), this.newCard.getSize());// billy
          if (this.parentType.equals("iteration")) {
            try {
                PersisterFactory.getPersister().deleteStoryCard(this.newCard.getId());
            }
            catch (NotConnectedException e) {
                util.Logger.singleton().error(e);
            }
            catch (IndexCardNotFoundException e) {
                util.Logger.singleton().error(e);
            }
        }
        else
            if (this.parentType.equals("backlog")) {
                try {
                    PersisterFactory.getPersister().deleteStoryCard(this.newCard.getId());
                }
                catch (NotConnectedException e) {
                    util.Logger.singleton().error(e);
                }
                catch (IndexCardNotFoundException e) {
                    util.Logger.singleton().error(e);
                }
            }
            else {
                
            }

    }

	public static void setInitialStoryCardColor(String initialStoryCardColor) {
		StoryCardCreateCommand.initialStoryCardColor = initialStoryCardColor;
	}

}

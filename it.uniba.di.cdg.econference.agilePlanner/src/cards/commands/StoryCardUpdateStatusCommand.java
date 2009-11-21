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

import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.IndexCard;
import persister.factory.PersisterFactory;


import cards.model.StoryCardModel;

public class StoryCardUpdateStatusCommand extends Command {

    private StoryCardModel storyCardModel = null;

    private String newStatus = IndexCard.STATUS_DEFINED;

    private String oldStatus = IndexCard.STATUS_DEFINED;

    public StoryCardModel getStoryCardModel() {
        return this.storyCardModel;
    }

    public StoryCardUpdateStatusCommand(StoryCardModel storyCard, String status) {
        this.storyCardModel = storyCard;
        newStatus = status;
        oldStatus = storyCardModel.getStatus();
        this.setLabel("Set State of Story Card");
    }

    @Override
    public void execute() {
        this.storyCardModel.setComplete(newStatus);

        try {
            storyCardModel.getStoryCard().setStatus(newStatus);
            PersisterFactory.getPersister().updateStoryCard(storyCardModel.getStoryCard());
        }
        catch (NotConnectedException e) {
            e.printStackTrace();
        }
        catch (IndexCardNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void redo() {
        execute();
    }

    @Override
    public boolean canExecute() {
        return (oldStatus != newStatus);
    }

    @Override
    public boolean canUndo() {
        return (oldStatus != newStatus);
    }

    @Override
    public void undo() {
        try {
            storyCardModel.setComplete(oldStatus);
            storyCardModel.getStoryCard().setStatus(oldStatus);
            PersisterFactory.getPersister().updateStoryCard(storyCardModel.getStoryCard());
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }
}

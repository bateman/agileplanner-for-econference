/*
 * author Billy 06-9-20
 */

package cards.commands;

import org.eclipse.gef.commands.Command;

import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.StoryCardModel;

public class StoryCardDeleteCommand extends Command {
    private StoryCardModel storyCard;

//    private Rectangle bounds;

    public StoryCardDeleteCommand(){}
    public void setStoryCard(StoryCardModel storyCardModel) {
        this.storyCard = storyCardModel;
    }

    @Override
    public void execute() {
        try {
            PersisterFactory.getPersister().deleteStoryCard(this.storyCard.getId());
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }

    @Override
    public void redo() {
        this.execute();
    }

    @Override
    public void undo() {
        try {
           
            try {
                PersisterFactory.getPersister().undeleteStoryCard(this.storyCard.getStoryCard());
            }
            catch (ForbiddenOperationException e) {
                util.Logger.singleton().error(e);
            }

        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }

        
    }

}
/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This class is the triger point from eclipse to the editor for changing the dimension and the location of
 *								the backlog object. 
 *
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.commands;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.StoryCard;
import persister.factory.PersisterFactory;


import cards.model.BacklogModel;
import cards.model.StoryCardModel;

public class BacklogUpdateSizeLocationCommand extends Command {

    private final Rectangle newBounds;

    private Rectangle oldBounds;

    @SuppressWarnings("unused")
    private final ChangeBoundsRequest request;

    private final BacklogModel card;

    private ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();

   
    public BacklogUpdateSizeLocationCommand(BacklogModel card, ChangeBoundsRequest request, Rectangle newBounds) {
        if ((card == null) || (request == null) || (newBounds == null)) {
            throw new IllegalArgumentException();
        }
        this.card = card;
        this.request = request;
        this.newBounds = newBounds;
        this.setLabel("Move");
        this.oldBounds = new Rectangle(this.card.getLocation(), this.card.getSize());
        this.storyCards.addAll(card.getChildrenList());
    }

    @Override
    public void execute() {

        this.redo();
    }

    @Override
    public void redo() {

        updateBacklog();

    }

    @Override
    public void undo() {
        undoUpdateBacklog();

    }

    private void updateBacklog() {
        try {
            this.card.getBacklog().setWidth(this.newBounds.getSize().width);
            this.card.getBacklog().setHeight(this.newBounds.getSize().height);

            if (this.newBounds.x < 0)
                this.newBounds.setLocation(0, this.newBounds.y);
            if (this.newBounds.y < 0)
                this.newBounds.setLocation(this.newBounds.x, 0);

            this.card.getBacklog().setLocationX(this.newBounds.x);
            this.card.getBacklog().setLocationY(this.newBounds.y);

            for (StoryCard sc : this.card.getBacklog().getStoryCardChildren()) {
                sc.setLocationX(sc.getLocationX() + this.newBounds.getLocation().x - this.oldBounds.getLocation().x);
                sc.setLocationY(sc.getLocationY() + this.newBounds.getLocation().y - this.oldBounds.getLocation().y);
            }

            PersisterFactory.getPersister().updateBacklog(this.card.getBacklog());
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }

    private void undoUpdateBacklog() {
        try {

            this.card.getBacklog().setWidth(this.oldBounds.getSize().width);
            this.card.getBacklog().setHeight(this.oldBounds.getSize().height);

            this.card.getBacklog().setLocationX(this.oldBounds.x);
            this.card.getBacklog().setLocationY(this.oldBounds.y);

            for (StoryCard sc : this.card.getBacklog().getStoryCardChildren()) {
                sc.setLocationX(sc.getLocationX() + this.oldBounds.getLocation().x - this.newBounds.getLocation().x);
                sc.setLocationY(sc.getLocationY() + this.oldBounds.getLocation().y - this.newBounds.getLocation().y);
            }
            PersisterFactory.getPersister().updateBacklog(this.card.getBacklog());
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }
}

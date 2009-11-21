/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This class is the triger point from eclipse to the editor for changing the location and dimensions of
 *								the iteration card
 *
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.StoryCard;
import persister.factory.PersisterFactory;


import cards.model.IterationCardModel;
import cards.model.StoryCardModel;

public class IterationUpdateSizeLocationCommand extends Command {

    private Rectangle newBounds;

    private Rectangle oldBounds;

    @SuppressWarnings("unused")
    private final ChangeBoundsRequest request;

    private IterationCardModel card;

    private ArrayList<StoryCardModel> storyCards;

    public IterationUpdateSizeLocationCommand(IterationCardModel card, ChangeBoundsRequest request, Rectangle newBounds) {
        if ((card == null) || (request == null) || (newBounds == null)) {
            throw new IllegalArgumentException();
        }
        this.card = card;
        this.request = request;
        this.newBounds = newBounds;
        this.setLabel("Move");
        this.oldBounds = new Rectangle(card.getLocation(), card.getSize());
//        this.storyCards.addAll(card.getChildrenList());
    }

    @Override
    public void execute() {

        this.redo();
    }

    @Override
    public void redo() {

        updateIteration();

    }

    @Override
    public void undo() {

        undoUpdateIteration();

    }

    private void updateIteration() {
        try {
            this.card.getIterationDataObject().setWidth(this.newBounds.getSize().width);
            this.card.getIterationDataObject().setHeight(this.newBounds.getSize().height);

            if (this.newBounds.x < 0)
                this.newBounds.setLocation(0, this.newBounds.y);
            if (this.newBounds.y < 0)
                this.newBounds.setLocation(this.newBounds.x, 0);
		
            this.card.getIterationDataObject().setLocationX(this.newBounds.x);
            this.card.getIterationDataObject().setLocationY(this.newBounds.y);
            
            List<StoryCard> storyCards = this.card.getIterationDataObject().getStoryCardChildren();
            for (StoryCard sc : storyCards) {
                sc.setLocationX(sc.getLocationX() + this.newBounds.getLocation().x - this.oldBounds.getLocation().x);
                sc.setLocationY(sc.getLocationY() + this.newBounds.getLocation().y - this.oldBounds.getLocation().y);
            }

                PersisterFactory.getPersister().updateIteration(this.card.getIterationDataObject());

        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }

    private void undoUpdateIteration() {
        try {
            this.card.getIterationDataObject().setWidth(this.oldBounds.getSize().width);
            this.card.getIterationDataObject().setHeight(this.oldBounds.getSize().height);

            this.card.getIterationDataObject().setLocationX(this.oldBounds.getLocation().x);
            this.card.getIterationDataObject().setLocationY(this.oldBounds.getLocation().y);

            for (StoryCard sc : this.card.getIterationDataObject().getStoryCardChildren()) {
                sc.setLocationX(sc.getLocationX() + this.oldBounds.getLocation().x - this.newBounds.getLocation().x);
                sc.setLocationY(sc.getLocationY() + this.oldBounds.getLocation().y - this.newBounds.getLocation().y);
            }

            PersisterFactory.getPersister().updateIteration(this.card.getIterationDataObject());

        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }
}

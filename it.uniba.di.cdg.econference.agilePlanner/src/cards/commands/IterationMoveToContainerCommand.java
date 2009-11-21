package cards.commands;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.IterationCardModel;
import cards.model.StoryCardModel;

public class IterationMoveToContainerCommand extends Command {
    private Rectangle newBounds;

    private Rectangle oldBounds;

    @SuppressWarnings("unused")
    private final ChangeBoundsRequest request;

    private IterationCardModel card;

    private ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();

    public IterationMoveToContainerCommand(IterationCardModel card, ChangeBoundsRequest

    request, Rectangle newBounds) {
        if ((card == null) || (request == null) || (newBounds == null)) {
            throw new IllegalArgumentException();
        }
        this.card = card;
        this.request = request;
        this.newBounds = new Rectangle(10, 10, 10, 10);
        this.setLabel("Move");
        this.oldBounds = new Rectangle(card.getLocation(), card.getSize());
        
        this.storyCards.addAll(card.getChildrenList());
    }

    public IterationMoveToContainerCommand(IterationCardModel card, Rectangle newBounds) {
        this.card = card;
        this.request = null;
        this.newBounds = new Rectangle(10, 10, 10, 10);
        this.setLabel("Move");
        this.oldBounds = new Rectangle(card.getLocation(), card.getSize());
        this.storyCards.addAll(card.getChildrenList());
    }

    @Override
    public void execute() {
        this.oldBounds = new Rectangle(card.getLocation(), card.getSize());
        this.newBounds = new Rectangle(card.getLocation(), card.getSize());

        this.redo();
    }

    @Override
    public void redo() {

        try {
            this.card.getIterationDataObject().setLocationX(this.newBounds.getLocation().x);
            this.card.getIterationDataObject().setLocationY(this.newBounds.getLocation().y);
            PersisterFactory.getPersister().updateIteration(this.card.getIterationDataObject());
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }

    }

    @Override
    public void undo() {
        try {
            this.card.getIterationDataObject().setLocationX(this.oldBounds.getLocation().x);
            this.card.getIterationDataObject().setLocationY(this.oldBounds.getLocation().y);
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

package cards.commands;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import cards.model.ProjectModel;
import cards.model.StoryCardModel;



public class BacklogMoveToContainerCommand extends Command {
    private Rectangle newBounds;

    private Rectangle oldBounds;

    @SuppressWarnings("unused")
    private final ChangeBoundsRequest request;

    private final ProjectModel card;

    private ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();

    public BacklogMoveToContainerCommand(ProjectModel card, ChangeBoundsRequest request, Rectangle newBounds) {
        if ((card == null) || (request == null) || (newBounds == null)) {
            throw new IllegalArgumentException();
        }

        this.card = card;
        this.request = request;
        this.newBounds = new Rectangle(10, 10, 10, 10);
        this.oldBounds = new Rectangle(new Point(0,0),new Dimension(50000, 50000));
        this.setLabel("Move");
    }

}

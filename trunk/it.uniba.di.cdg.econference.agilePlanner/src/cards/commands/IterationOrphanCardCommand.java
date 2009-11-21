package cards.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import cards.model.BacklogModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;



public class IterationOrphanCardCommand extends Command {
    private Rectangle oldBound;

    private Rectangle newBound;

    private String parentType;

    private IterationCardModel child;

    private IterationCardModel parentI;

    private BacklogModel parentB;

    private ProjectModel parent;

    public void setChild(IterationCardModel child) {
        this.child = child;
        this.oldBound = new Rectangle(child.getLocation(), child.getSize());
        
    }

    @Override
    public void execute() {
    }

    @Override
    public void undo() {
    }

    @Override
    public void redo() {
    }

}

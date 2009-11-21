package cards.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import cards.model.BacklogModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;



public class StoryCardOrphanCommand extends Command {
    private Rectangle oldBound;

    private Rectangle newBound;

    private String parentType;

    private StoryCardModel child;

    private IterationCardModel parentI;
    
    private ProjectModel parentP;

    private BacklogModel parentB;

    public void setChild(StoryCardModel child) {
        this.child = child;
        this.oldBound = new Rectangle(child.getLocation(), child.getSize());
        
    }

    public void setParentI(IterationCardModel parentI) {
        this.parentI = parentI;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public void setParentB(BacklogModel parentB) {
        this.parentB = parentB;
    }

    public void setParentP(ProjectModel model) {
		this.parentP = model;
		
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

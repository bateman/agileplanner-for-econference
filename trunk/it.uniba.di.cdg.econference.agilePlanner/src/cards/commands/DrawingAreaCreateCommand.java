package cards.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import cards.model.DrawingAreaModel;
import cards.model.ProjectModel;



public class DrawingAreaCreateCommand extends Command{

	private DrawingAreaModel newCard;

    private final ProjectModel parent;

    private Rectangle bounds;

    public DrawingAreaCreateCommand(DrawingAreaModel newCard, ProjectModel parent, Rectangle bounds) {
        this.newCard = newCard;
        this.parent = parent;
        this.bounds = bounds;
        this.setLabel("Drawing creation");
        
    }

    @Override
    public boolean canExecute() {
        return (this.newCard != null) & (this.parent != null) && (this.bounds != null);
    }

    @Override
    public void execute() {
    	
    	try{
        this.newCard.setLocation(this.bounds.getLocation());
    	
    	
        Dimension size = this.bounds.getSize();
        if ((size.width > 0) && (size.height > 0)) {
            this.newCard.setSize(size);
        }
        
    	this.parent.addDrawingArea(this.newCard);
    	}catch(Exception e){
    		util.Logger.singleton().error(e);
    	}
        
    }

    @Override
    public void redo() {

    }

    @Override
    public void undo() {
     
    }

    public DrawingAreaModel getDrawingAreaModel() {
        return this.newCard;
    }
}

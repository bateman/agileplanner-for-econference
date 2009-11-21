package cards.commands;

import org.eclipse.gef.commands.Command;

import cards.model.DrawingAreaModel;
import cards.model.ProjectModel;



public class DrawingAreaDeleteCommand extends Command {

	
	  private ProjectModel parent;

	    private DrawingAreaModel drawingAreaModel;

	    public void setParent(ProjectModel tableModel) {
	        this.parent = tableModel;
	    }

	    public void setDrawingArea(DrawingAreaModel model) {
	        this.drawingAreaModel = model;
	    }

	    @Override
	    public void execute() {
	      
	        this.redo();

	    }

	    @Override
	    public void redo() {
	    	this.parent.removeDrawingArea(this.drawingAreaModel);
	    }

	    @Override
	    public void undo() {
	    }
}

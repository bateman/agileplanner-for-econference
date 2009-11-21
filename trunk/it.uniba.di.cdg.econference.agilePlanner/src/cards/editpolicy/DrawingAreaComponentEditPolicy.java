package cards.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cards.commands.DrawingAreaDeleteCommand;
import cards.model.DrawingAreaModel;
import cards.model.ProjectModel;



public class DrawingAreaComponentEditPolicy extends ComponentEditPolicy {

	
	  @Override
	    protected Command createDeleteCommand(GroupRequest deleteRequest) {
	        DrawingAreaDeleteCommand deleteCommand = new DrawingAreaDeleteCommand();

	        if (this.getHost().getParent().getModel() instanceof ProjectModel) {
	            deleteCommand.setParent((ProjectModel) (this.getHost().getParent().getModel()));
	        }
	        deleteCommand.setDrawingArea((DrawingAreaModel) (this.getHost().getModel()));
	        return deleteCommand;
	    }
}

/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required editpolicy for IterationCard  objects. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cards.commands.IterationDeleteCommand;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;



public class IterationCardComponentEditPolicy extends ComponentEditPolicy {
    public IterationCardComponentEditPolicy() {
        super();
    }

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        IterationDeleteCommand deleteCommand = new IterationDeleteCommand();

        if (this.getHost().getParent().getModel() instanceof ProjectModel) {
            deleteCommand.setParent((ProjectModel) (this.getHost().getParent().getModel()));
        }
        deleteCommand.setIterationCard((IterationCardModel) (this.getHost().getModel()));
        return deleteCommand;
    }

}

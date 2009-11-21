/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required editpolicy for backlog objects. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cards.commands.BacklogDeleteCommand;
import cards.model.BacklogModel;
import cards.model.ProjectModel;



public class BacklogComponentEditPolicy extends ComponentEditPolicy {
    public BacklogComponentEditPolicy() {
        super();
    }

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        BacklogDeleteCommand deleteCommand = new BacklogDeleteCommand();

        if (this.getHost().getParent().getModel() instanceof ProjectModel) {
            deleteCommand.setParent((ProjectModel) this.getHost().getParent().getModel());
        }
        deleteCommand.setBacklog((BacklogModel) this.getHost().getModel());
        return deleteCommand;
    }

}
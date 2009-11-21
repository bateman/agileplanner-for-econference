/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the editpolicy that allows for direct edit feature.
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import cards.commands.IterationSetValuesCommand;
import cards.editpart.IterationCardEditPart;



public class IterationDirectEditPolicy extends DirectEditPolicy {

    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        String labelText = (String) request.getCellEditor().getValue();
        IterationCardEditPart iteration = (IterationCardEditPart) this.getHost();
        IterationSetValuesCommand command = new IterationSetValuesCommand(iteration.getCastedModel(), labelText);
        return command;
    }

    @Override
    protected void showCurrentEditValue(DirectEditRequest request) {
        this.getHostFigure().getUpdateManager().performUpdate();
    }
}

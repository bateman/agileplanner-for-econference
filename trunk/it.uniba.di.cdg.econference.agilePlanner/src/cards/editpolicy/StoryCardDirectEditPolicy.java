/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required edit policy for the StoryCard object. 
 *								This class uses a Hack that was recomended by the eclipse GEF forum.  A better solution should be found. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import cards.commands.StoryCardSetValuesCommand;
import cards.editpart.StoryCardEditPart;



public class StoryCardDirectEditPolicy extends DirectEditPolicy {

    @Override
    protected Command getDirectEditCommand(DirectEditRequest request) {
        String labelText = (String) request.getCellEditor().getValue();

        StoryCardEditPart storyCard = (StoryCardEditPart) this.getHost();
        StoryCardSetValuesCommand command = new StoryCardSetValuesCommand(storyCard.getCastedModel(), labelText);
        return command;
    }

    /**
     * This is a HACK that prevents async layout from placing the cell editor
     * twice. This HACK can be found in the GEF examples online.
     */
    @Override
    protected void showCurrentEditValue(DirectEditRequest request) {
        this.getHostFigure().getUpdateManager().performUpdate();
    }
}

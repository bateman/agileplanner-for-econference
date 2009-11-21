/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required edit policy for the StoryCard Object. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cards.commands.StoryCardDeleteCommand;
import cards.model.StoryCardModel;



public class StoryCardComponentEditPolicy extends ComponentEditPolicy {

	public  StoryCardComponentEditPolicy(){} 
	
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        StoryCardDeleteCommand deleteCommand = new StoryCardDeleteCommand();
        deleteCommand.setStoryCard((StoryCardModel) this.getHost().getModel());
        return deleteCommand;
    }
}

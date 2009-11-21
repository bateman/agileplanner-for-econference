/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required edit policy for the backlog object. Handeling containmnet of children. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpolicy;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ContainerEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.GroupRequest;

import cards.commands.StoryCardOrphanCommand;
import cards.editpart.BacklogEditPart;
import cards.editpart.StoryCardEditPart;
import cards.model.BacklogModel;
import cards.model.StoryCardModel;



public class BacklogContainerEditPolicy extends ContainerEditPolicy {

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }

    @Override
    protected Command getOrphanChildrenCommand(GroupRequest request) {
        List parts = request.getEditParts();
        CompoundCommand result = new CompoundCommand();
        for (int i = 0; i < parts.size(); i++) {
            StoryCardOrphanCommand orphan = new StoryCardOrphanCommand();
            orphan.setChild((StoryCardModel) (((StoryCardEditPart) ((EditPart) parts.get(i))).getModel()));
            orphan.setParentB((BacklogModel) (((BacklogEditPart) this.getHost()).getModel()));
            orphan.setParentType("backlog");
            result.add(orphan);
        }
        return result.unwrap();
    }
}

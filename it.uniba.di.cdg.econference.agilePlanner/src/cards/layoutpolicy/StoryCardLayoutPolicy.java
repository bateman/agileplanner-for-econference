package cards.layoutpolicy;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cards.commands.IterationMoveToContainerCommand;
import cards.editpart.IterationCardEditPart;
import cards.editpart.ProjectEditPart;
import cards.editpart.StoryCardEditPart;
import cards.model.IterationCardModel;



public class StoryCardLayoutPolicy extends XYLayoutEditPolicy {

    protected Command createAddCommand(EditPart child) {
        if (child instanceof StoryCardEditPart) {
            return null;
        }else{
            if (child instanceof IterationCardEditPart) {
                IterationMoveToContainerCommand command = new IterationMoveToContainerCommand((IterationCardModel) child.getModel(), new Rectangle(
                        10, 10, 10, 10));
                return command;
            }
        }
        return null;
    }

    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        return null;
    }

    @Override
    protected Command getAddCommand(Request request) {
    
        ChangeBoundsRequest req = ((ChangeBoundsRequest) request);
        List editParts = req.getEditParts();
        CompoundCommand command = new CompoundCommand();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart child = (EditPart) editParts.get(i);
            command.add(this.createAddCommand(child));
        }
        return command.unwrap();

    }

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }
}

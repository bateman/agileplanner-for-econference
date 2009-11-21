package cards.layoutpolicy;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class DrawingAreaLayoutPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command createChangeConstraintCommand(EditPart arg0, Object arg1) {
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest arg0) {
		return null;
	}

}

package mouse.remote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Display;

import cards.model.AbstractRootModel;




public class RemoteMouseEditPart extends AbstractGraphicalEditPart implements EditPart, PropertyChangeListener {

    public final Runnable refresher = new Runnable() {
        public void run() {
            refreshVisuals();
        }
    };

    protected IFigure createFigure() {
        IFigure f = createFigureForModel();
        return f;
    }

    private IFigure createFigureForModel() {
        if (getModel() instanceof RemoteMouseModel)
            return new RemoteMouseFigure();
        else
            throw new IllegalArgumentException();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (RemoteMouseModel.LOCATION_PROP.equals(prop) || RemoteMouseModel.SIZE_PROP.equals(prop)) {
            try {
                Display d = getViewer().getControl().getDisplay();
                d.syncExec(refresher);
            }
            catch (Exception e) {
            	util.Logger.singleton().error(e);
            }
        }
    }

    
    @Override
	public boolean isSelectable() {
		return false;
	}

	public void createEditPolicies() {
    }

    public void activate() {
        if (!isActive()) {
            super.activate();
            ((AbstractRootModel) getModel()).addPropertyChangeListener(this);
        }
    }

    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((AbstractRootModel) getModel()).removePropertyChangeListener(this);
        }
    }

    public RemoteMouseModel getCastedModel() {
        return (RemoteMouseModel) getModel();
    }

    public void refreshVisuals() {
        RemoteMouseFigure figure = (RemoteMouseFigure) getFigure();
        Rectangle bounds = new Rectangle(getCastedModel().getLocation(), getCastedModel().getSize());
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), bounds);
        figure.setName(String.valueOf(getCastedModel().getClientName()));
        figure.requestFocus();
        figure.repaint();
    }

}

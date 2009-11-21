package cards.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Display;

import cards.editpolicy.DrawingAreaComponentEditPolicy;
import cards.figure.DrawingAreaFigure;
import cards.model.AbstractRootModel;
import cards.model.DrawingAreaModel;



public class DrawingAreaEditPart extends AbstractGraphicalEditPart implements EditPart, PropertyChangeListener {

	@Override
	protected IFigure createFigure() {
		return new DrawingAreaFigure();
	}

	@Override
	protected void createEditPolicies() {
		 this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new DrawingAreaComponentEditPolicy());  
	}

	 @Override
	    public void activate() {
	        if (!this.isActive()) {
	            super.activate();
	            ((AbstractRootModel) this.getModel()).addPropertyChangeListener(this);
	        }
	    }
	 
	public DrawingAreaModel getCastedModel(){
		return (DrawingAreaModel) this.getModel();
	}
	
	
	public void refreshVisuals() {
        DrawingAreaFigure figure = (DrawingAreaFigure) this.getFigure();
       
        Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(), this.getCastedModel().getSize());
        ((GraphicalEditPart) this.getParent()).setLayoutConstraint(this, this.getFigure(), bounds);
        
        figure.setImage(this.getCastedModel().getDrawing());
        figure.repaint();
    }
	
	/*************************************Property Change Listener************************/
    public final Runnable refresher = new Runnable() {
        public void run() {
            DrawingAreaEditPart.this.refreshVisuals();
            DrawingAreaEditPart.this.refreshChildren();
        }
    };
	
	
	public void propertyChange(PropertyChangeEvent evt) {
		 String prop = evt.getPropertyName();
	        if (DrawingAreaModel.CHILD_ADDED_PROP.equals(prop) || DrawingAreaModel.LOCATION_PROP.equals(prop) || DrawingAreaModel.SIZE_PROP.equals(prop)
	                || DrawingAreaModel.CHILD_REMOVED_PROP.equals(prop) || DrawingAreaModel.NAME_PROP.equals(prop)) {
	            try {
	                Display d = this.getViewer().getControl().getDisplay();
	                d.syncExec(this.refresher);
	            }
	            catch (Exception e) {
	            	util.Logger.singleton().error(e);
	            }
	        }
	}
	
	
	public void plotPoint(int x, int y){
		this.getCastedModel().plotPoint(x,y);
	}

}

/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the controler object for the Backlog object. This class handels the communication between 
 *								the backlog model and its corresponding figure. 
 *								If a mouse listener is necessary this is where it should be added as a drag tracker!
 *								
 ***************************************************************************************************/
package cards.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import cards.editpolicy.BacklogComponentEditPolicy;
import cards.editpolicy.BacklogContainerEditPolicy;
import cards.figure.BacklogFigure;
import cards.layoutpolicy.BacklogLayoutPolicy;
import cards.model.AbstractRootModel;
import cards.model.BacklogModel;
import cards.model.IndexCardModel;
import cards.model.StoryCardModel;



public class BacklogEditPart extends AbstractGraphicalEditPart implements EditPart, PropertyChangeListener {
    /**
     * Runnable object to allow for updating of the figure when changes to the
     * model occure outside of the UI thread
     */
    public final Runnable refresher = new Runnable() {
        public void run() {
            BacklogEditPart.this.refreshVisuals();
            BacklogEditPart.this.refreshChildren();
        }
    };

    private IFigure createFigureForModel() {
        if (this.getModel() instanceof BacklogModel) {
            return new BacklogFigure();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * List of all edit policies that this controler requires
     */
    @Override
    protected void createEditPolicies() {
        this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new BacklogComponentEditPolicy());
        this.installEditPolicy(EditPolicy.CONTAINER_ROLE, new BacklogContainerEditPolicy());
        this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new BacklogLayoutPolicy());

    }

    @Override
    protected IFigure createFigure() {
        IFigure f = this.createFigureForModel();
        return f;
    }

    @Override
    public void activate() {
        if (!this.isActive()) {
            super.activate();
            ((AbstractRootModel) this.getModel()).addPropertyChangeListener(this);
        }
    }

    /** ****************************************************************************************** */
    /** ****************************************************************************************** */
    @Override
    public void deactivate() {
        if (this.isActive()) {
            super.deactivate();
            ((AbstractRootModel) this.getModel()).removePropertyChangeListener(this);
        }
    }

    public BacklogModel getCastedModel() {
        return (BacklogModel) this.getModel();
    }

    /** DragTracker*********************************************************************** */
    /** ******************************************************************************** */
    /**
     * This is the mouse listener object. use this to trigger events based on
     * mouse interactions.
     */
    @Override
    public DragTracker getDragTracker(Request req) {
        return new DragEditPartsTracker(this) {

            private void updateDelta(Dimension d) {
                for (Object o : super.getOperationSet()) {
                    if (o instanceof BacklogEditPart) {
                        ((BacklogEditPart) o).getCastedModel().setDragMoveDelta(d);
                    }
                }
            }

            @Override
            protected boolean handleButtonDown(int Button) {
                boolean returnVal = super.handleButtonDown(Button);
                return returnVal;
            }

            @Override
            protected boolean handleButtonUp(int Button) {
                boolean returnVal = false;
                returnVal = super.handleButtonUp(Button);
                BacklogEditPart.this.refreshVisuals();
                BacklogEditPart.this.refreshChildren();

                return returnVal;
            }

            @Override
            protected boolean handleDoubleClick(int Button) {
                return false;
            }

            protected boolean handleDragInProgress() {
                boolean returnVal = super.handleDragInProgress();
                this.updateDelta(this.getDragMoveDelta());
                return returnVal;
            }
        };
    }

    @Override
    public List getModelChildren() {
    	
    	 List<IndexCardModel> storyChildren = new ArrayList<IndexCardModel>(); 
        storyChildren.addAll( this.getCastedModel().getChildrenList());
    	storyChildren.addAll(getCastedModel().getProjectModel().getRemoteMice());
    	Collections.sort(storyChildren);
    	
        if (storyChildren.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        else {
            return Collections.unmodifiableList(storyChildren);
        }
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (BacklogModel.CHILD_ADDED_PROP.equals(prop) || BacklogModel.LOCATION_PROP.equals(prop) || BacklogModel.SIZE_PROP.equals(prop)
                || BacklogModel.CHILD_REMOVED_PROP.equals(prop) || BacklogModel.NAME_PROP.equals(prop)) {
            try {
            	EditPartViewer e = getViewer();
            	Control c = e.getControl();
                Display d = c.getDisplay();
                d.syncExec(this.refresher);
            } catch (NullPointerException e) {
				// TODO: handle exception
			} catch (Exception e) {
            	util.Logger.singleton().error(e);
            }
        }
    }

    @Override
    public void refreshChildren() {
        super.refreshChildren();
    }

    /**
     * Required method to refresh the figure when changes to the model occure.
     * Any UI elements that this object needs should be maintained here!
     */
    @Override
    public void refreshVisuals() {
        BacklogFigure figure = (BacklogFigure) this.getFigure();
        figure.setNameText(this.getCastedModel().getName());
        Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(), this.getCastedModel().getSize());
        ((GraphicalEditPart) this.getParent()).setLayoutConstraint(this, this.getFigure(), bounds);
        figure.repaint();
    }

    public void removeChild(StoryCardEditPart child) {
        this.getCastedModel().removeChild((StoryCardModel) child.getModel());
        super.removeChild(child);
    }
}

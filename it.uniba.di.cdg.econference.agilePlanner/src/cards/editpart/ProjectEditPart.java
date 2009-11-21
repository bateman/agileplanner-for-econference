package cards.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;
import org.eclipse.swt.widgets.Display;

import cards.editpolicy.ProjectContainerEditPolicy;
import cards.layoutpolicy.ProjectLayoutEditPolicy;
import cards.model.AbstractRootModel;
import cards.model.IndexCardModel;
import cards.model.ProjectModel;



public class ProjectEditPart extends AbstractGraphicalEditPart implements
		EditPart, PropertyChangeListener, LayerConstants {


	
	public final Runnable refresher = new Runnable() {
		public void run() {
			ProjectEditPart.this.refreshVisuals();
			ProjectEditPart.this.refreshChildren();
		}
	};

	private ProjectModel getCastedModel() {
		return (ProjectModel) this.getModel();
	}

	@Override
	protected void createEditPolicies() {
		
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new RootComponentEditPolicy());
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new ProjectLayoutEditPolicy());
		this.installEditPolicy(EditPolicy.CONTAINER_ROLE,
				new ProjectContainerEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		Figure f = new FreeformLayer();
		f.setBorder(new MarginBorder(3));

		f.setLayoutManager(new FreeformLayout());
		f.repaint();

		return f;
	}

	@Override
	protected void refreshVisuals() {
		IFigure f = this.getFigure();
		f.repaint();
	}

	@Override
	public void activate() {
		if (!this.isActive()) {
			super.activate();
			((AbstractRootModel) this.getModel())
					.addPropertyChangeListener(this);
		}

	}

	

	@Override
	public void deactivate() {
		if (this.isActive()) {
			super.deactivate();
			((AbstractRootModel) this.getModel())
					.removePropertyChangeListener(this);
		}
	}



	@Override
	@SuppressWarnings("unchecked")
	public List getModelChildren() {
		List<IndexCardModel> modelChildren = new ArrayList<IndexCardModel>();
		
		if (getCastedModel().getBacklogModel() != null)
			modelChildren.add(getCastedModel().getBacklogModel());
		modelChildren.addAll(getCastedModel().getStoryCardModelList());
		modelChildren.addAll(getCastedModel().getIterations());
		modelChildren.addAll(getCastedModel().getDrawingAreas());
		modelChildren.addAll(getCastedModel().getRemoteMice());
		Collections.sort(modelChildren);
		if (modelChildren.isEmpty()) {
			return Collections.EMPTY_LIST;
		} else {

			return Collections.unmodifiableList(modelChildren);
		}
		
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (ProjectModel.CHILD_ADDED_PROP.equals(prop)
				|| ProjectModel.CHILD_REMOVED_PROP.equals(prop)
				|| ProjectModel.UPDATED_PROJECT.equals(prop)) {
			Display d = this.getViewer().getControl().getDisplay();
			d.syncExec(this.refresher);
		}

	}

}

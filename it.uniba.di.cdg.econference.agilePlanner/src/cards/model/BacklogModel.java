/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the data model for the backlog object. only data in this object does not run the risk 
 *								of being disposed by the framework at a given time.  
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import persister.data.Backlog;
import persister.data.impl.BacklogDataObject;


import cards.CardConstants;
import cards.editpart.BacklogEditPart;
import fitintegration.PluginInformation;

public class BacklogModel extends ContainerModel {
	private static IPropertyDescriptor[] descriptors;

	private static final long serialVersionUID = 1;

	public static final String CHILD_ADDED_PROP = "BacklogModel.ChildAdded";

	public static final String CHILD_REMOVED_PROP = "BacklogModel.ChildRemoved";

	public static final String LOCATION_PROP = "BacklogModel.location";

	public static final String NAME_PROP = "BacklogModel.name";

	public static final String SIZE_PROP = "BacklogModel.size";

	public static final String X_PROP = "BacklogModel.x";

	public static final String Y_PROP = "BacklogModel.y";

	static {
		descriptors = new IPropertyDescriptor[] { new TextPropertyDescriptor(
				NAME_PROP, "Name") };
		for (int i = 0; i < descriptors.length; i++) {
			((PropertyDescriptor) descriptors[i])
					.setValidator(new ICellEditorValidator() {
						public String isValid(Object value) {
							return "";
						}
					});
		}
	}// END STATIC!

	private Dimension dragMoveDelta = new Dimension();

	/** ************************************************************************************************ */
	/** ************************************************************************************************ */
	public BacklogModel() {
		this(new BacklogDataObject());
	}

		/** ************************************************************************************************ */
	/** ************************************************************************************************ */
	public BacklogModel(Backlog bklog) {
		setBacklogDataObject(bklog);
		this.addPropertyChangeListener(new BacklogEditPart());
		this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
	}

	@Override
	protected Backlog getDataObject() {
		return (Backlog) super.getDataObject();
	}

	@Override
	public void addNewChild(StoryCardModel child) {
		super.addNewChild(child);
		this.firePropertyChange(CHILD_ADDED_PROP, null, child);
	}

	@Override
	public void updateChild(StoryCardModel child) {
		super.updateChild(child);
		this.firePropertyChange(CHILD_ADDED_PROP, null, child);
	}

	@Override
	public void addChildIncommingOnLoad(StoryCardModel child) {
		super.addChildIncommingOnLoad(child);
		this.firePropertyChange(CHILD_ADDED_PROP, null, child);
	}

	
	
	@Override
	public void addChildSetParent(StoryCardModel child) {
		super.addChildSetParent(child);
		//this.firePropertyChange(CHILD_ADDED_PROP, null, child);
	}

	public void addUpdatedChild(StoryCardModel storyCardModel)
	{
		if (storyCardModel.getId() != 0) {
			this.children.add(storyCardModel);
			storyCardModel.setParent(this);
		}
		getDataObject().addStoryCard(storyCardModel.getDataObject());

	}
	/**
	 * Getters & Setters
	 * **********************************************************************************
	 */
	/** ************************************************************************************************ */

	public Backlog getBacklog() {
		return getDataObject();
	}

	public Dimension getDragMoveDelta() {
		return dragMoveDelta;
	}

	@Override
	public Image getIcon() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				PluginInformation.getPLUGIN_ID(), CardConstants.NewBacklogIcon)
				.createImage();
	}

	public String getName() {
		return getBacklog().getName();
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeChild(StoryCardModel child) {
		super.removeChild(child);
		this.firePropertyChange(CHILD_REMOVED_PROP, child, null);
	}



	public void setBacklogDataObject(Backlog backlog) {
		this.setCardDataObject(backlog);
	}

	public void setDragMoveDelta(Dimension dragMoveDelta) {
		this.dragMoveDelta = dragMoveDelta;
	}

	public void setLocationIncomming(Point newLocation) {
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}
		getBacklog().setLocationX(newLocation.x);
		getBacklog().setLocationY(newLocation.y);
		this.updateChildrenFigure();
		this.firePropertyChange(LOCATION_PROP, null, new Point(getBacklog()
				.getLocationX(), getBacklog().getLocationY()));
	}

	public void setName(String newName) {
		if (newName == null) {
			throw new IllegalArgumentException();
		}
		getBacklog().setName(newName);
		this.firePropertyChange(NAME_PROP, null, newName);
	}

	/**
	 * This will update the child location based on the change in location. This
	 * is needed because the childs locations in the model are not updated when
	 * the figures locations are changed.
	 * 
	 * @param newIterationLocation
	 * @param oldIterationLocation
	 */
	public void setProjectModel(ProjectModel model) {
		super.setProjectModel(model);
		getProjectModel().setNewBacklogModel(this);
	}

	public void setSize(Dimension newSize) {
		if (newSize == null) {
			throw new IllegalArgumentException();
		}
		getBacklog().setWidth(newSize.width);
		getBacklog().setHeight(newSize.height);
		this.firePropertyChange(SIZE_PROP, null, new Dimension(getBacklog()
				.getWidth(), getBacklog().getHeight()));
	}

	public void setSizeIncomming(Dimension newSize) {
		if (newSize == null) {
			throw new IllegalArgumentException();
		}
		getBacklog().setWidth(newSize.width);
		getBacklog().setHeight(newSize.height);
		this.firePropertyChange(SIZE_PROP, null, new Dimension(getBacklog()
				.getWidth(), getBacklog().getHeight()));
	}

	@Override
	public void setComplete(String status) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "Backlog " + this.hashCode();
	}

	@Override
	public void unDeleteChildIncomming(StoryCardModel child) {
		super.unDeleteChildIncomming(child);
		this.firePropertyChange(CHILD_ADDED_PROP, null, child);
	}

	public void updateCardLocation() {
		Point oldLocation = new Point();
		Point newLocation = new Point();
		Dimension pointChange = new Dimension();

		oldLocation = this.getLocation();
		pointChange = this.dragMoveDelta;
		newLocation.x = oldLocation.x + pointChange.width;
		newLocation.y = oldLocation.y + pointChange.height;
		this.setLocation(newLocation);
	}

	@Override
	public void updateChildrenFigure() {
		super.updateChildPriorities();
		this.firePropertyChange(CHILD_ADDED_PROP, null, null);
	}

	@Override
	public void setLocation(Point newLocation) {
		super.setLocation(newLocation);
		this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
		updateChildrenFigure();

	}

	public void colorChanged(StoryCardModel child) {
		this.updateChildrenFigure();
		this.firePropertyChange(CHILD_ADDED_PROP, null, child);
		
	}
}

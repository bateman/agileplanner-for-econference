/**
 * 
 */
package cards.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;

import persister.IndexCardNotFoundException;
import persister.data.IndexCardWithChildren;
import persister.data.StoryCard;



/**
 * @author hkolenda
 * 
 */
public abstract class ContainerModel extends IndexCardModel {

	private List<StoryCardYCoordinate> storyCardsYCoord = new ArrayList<StoryCardYCoordinate>();

	protected List<StoryCardModel> children = new ArrayList<StoryCardModel>();

	@Override
	protected IndexCardWithChildren getDataObject() {
		return (IndexCardWithChildren) super.getDataObject();
	}

	public void addNewChild(StoryCardModel child) {
		if (child.getId() != 0) {
			this.children.add(child);
			this.getDataObject().addStoryCard(child.getStoryCard());
			child.setParent(this);
		}
		this.updateChildPriorities();
	}

	public void updateChild(StoryCardModel child) {
		if (child != null) {
			try {
				//System.out.println((Iteration)this.getProjectModel().getProjectDataObject().findCard(this.getDataObject().getId()));
				StoryCard oldStoryCard = (StoryCard)this.getProjectModel().getProjectDataObject().findCard(child.getId());
				this.getDataObject().removeStoryCard(oldStoryCard);
				this.getDataObject().addStoryCard(child.getDataObject());
			} catch (IndexCardNotFoundException e) {
				util.Logger.singleton().error(e);
			}
		}
	}
	// do not add storycard here
	public void addChildIncommingOnLoad(StoryCardModel child) {
		if (child.getId() != 0) {
			this.children.add(child);
			child.setParent(this);
		}
		this.updateChildPriorities();
	}

	public void addChildSetParent(StoryCardModel child) {
		if (child.getId() != 0) {
			this.children.add(child);
			child.setParent(this);
		}
		getDataObject().addStoryCard(child.getDataObject());

		this.updateChildPriorities();
	}

	public List<StoryCardModel> getChildrenList() {
		return this.children;
	}

	public void removeChild(StoryCardModel child) {
		
		if (child != null) {			
			this.children.remove(child);
			getDataObject().removeStoryCard(child.getDataObject());
			if(child.getParent().equals(this)){
				child.setParent(null);
			}
		}
		this.updateChildPriorities();

	}

	public void unDeleteChildIncomming(StoryCardModel child) {
		this.children.add(child);
		this.updateChildPriorities();
	}

	public synchronized void updateChildPriorities() {
		int idx = 0;
		this.storyCardsYCoord.clear();
		for (StoryCardModel child : getChildrenList()) {
			this.storyCardsYCoord.add(new StoryCardYCoordinate(child
					.getLocation().y, idx));
			
			idx++;
		}
		Collections.sort(this.storyCardsYCoord);
		int priorityValue = 1;
		for (StoryCardYCoordinate scy : this.storyCardsYCoord) {
			scy.setPriority(priorityValue);
			priorityValue++;
		}

		for (StoryCardYCoordinate scy : this.storyCardsYCoord) {
			this.children.get(scy.getIndex()).setPriority(scy.getPriority());
			
		}
	}

	public void updateChildrenFigure() {
		this.updateChildPriorities();
	}

	public void setLocation(Point newLocation) {
		if (newLocation != null) {
			getDataObject().setLocationX(newLocation.x);
			getDataObject().setLocationY(newLocation.y);
		}
	}

	private ProjectModel projectModel;

	public ProjectModel getProjectModel() {
		return projectModel;
	}

	public void setProjectModel(ProjectModel model) {
		this.projectModel = model;
	}

	

	public void setChildren(List<StoryCardModel> children) {
		this.children = children;
	}

}

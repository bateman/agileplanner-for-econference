package persister.data.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import persister.data.Backlog;
import persister.data.IndexCardWithChildren;
import persister.data.StoryCard;



public class BacklogDataObject implements Backlog, Serializable {

    private static final long serialVersionUID = 5914729035527543019L;
    private IndexCardWithChildren cardWithKids = new IndexCardWithChildrenDataObject();
    
    public BacklogDataObject() {
        setId(0);
        setParent(0);
        setHeight(DEFAULT_HEIGHT);
        setWidth(DEFAULT_WIDTH);
        setLocationX(DEFAULT_LOCATION_X);
        setLocationY(DEFAULT_LOCATION_Y);
        setStatus(STATUS_DEFINED);
        setName("Project Backlog");
        setDescription("Project Backlog");
    }
    
    public BacklogDataObject(int width, int height, int locationX, int locationY) {
        this();
        setHeight(height);
        setWidth(width);
        setLocationX(locationX);
        setLocationY(locationY);
    }

    public BacklogDataObject clone() {
        BacklogDataObject clone = new BacklogDataObject();
        clone.setCardWithKids((IndexCardWithChildren)getCardWithKids().clone());
        clone.setParent(getParent());
        clone.setStatus(getStatus());
        clone.setIdRecievedFromRally(isIdRecievedFromRally());
        clone.setDescription(getDescription());
        return clone;
    }

    public float getAvailableEffort() {
        return 0;
    }

    public Timestamp getEndDate() {
        return null;
    }

    public Timestamp getStartDate() {

        return null;
    }

    /***************************************************************************
     * @return Following accesser methods must return null, false, or zero.
     * 
     **************************************************************************/

    public boolean isCompleted() {
        return false;
    }

    public boolean isStarted() {
        return false;
    }

    public String toString() {
        String backlog = "###################################\n" + "#            Backlog              #\n"
                + "###################################\n\n" + "ID:\t\t" + getId() + "\n" + "Name:\t\t" + getName() + "\n" + "Parent:\t\t"
                + getParent() + "\n" + "Size:\t\t" + getWidth() + " x " + getHeight() + "\n" + "Location:\t(" + getLocationX() + " , "
                + getLocationY() + ")\n\n";
        return backlog;
    }

    // DOES NOT UPDATE PARENT-CHILD RELATIONS!
    public void update(Backlog backlog) {
        setStatus(backlog.getStatus());
        setDescription(backlog.getDescription());
        setHeight(backlog.getHeight());
        setLocationX(backlog.getLocationX());
        setLocationY(backlog.getLocationY());
        setName(backlog.getName());
        setWidth(backlog.getWidth());
    }
    
    public boolean equals(Object other){
    	Backlog backlog = (Backlog) other;
    	return
    	   getHeight() == backlog.getHeight()
    	&& getWidth() == backlog.getWidth()
    	&& getEndDate() == backlog.getEndDate()
    	&& getLocationX() == backlog.getLocationX()
    	&& getLocationY() == backlog.getLocationY()
    	&& getStartDate() == backlog.getStartDate()
//    	&& getStatus() == backlog.getStatus()
    	&& getStoryCardChildren().equals(backlog.getStoryCardChildren())
    	;
    }

	public void addStoryCard(StoryCard storycard) {
		cardWithKids.addStoryCard(storycard);
	}

	public float getActualEffort() {
		return cardWithKids.getActualEffort();
	}

	public float getBestCaseEstimate() {
		return cardWithKids.getBestCaseEstimate();
	}

	public String getDescription() {
		return cardWithKids.getDescription();
	}

	public int getHeight() {
		return cardWithKids.getHeight();
	}

	public long getId() {
		return cardWithKids.getId();
	}

	public int getLocationX() {
		return cardWithKids.getLocationX();
	}

	public int getLocationY() {
		return cardWithKids.getLocationY();
	}

	public float getMostlikelyEstimate() {
		return cardWithKids.getMostlikelyEstimate();
	}

	public String getName() {
		return cardWithKids.getName();
	}

	public long getParent() {
		return cardWithKids.getParent();
	}

	public float getRemainingEffort() {
		return cardWithKids.getRemainingEffort();
	}

	public float getRotationAngle() {
		return cardWithKids.getRotationAngle();
	}

	public String getStatus() {
		return cardWithKids.getStatus();
	}

	public StoryCard getStoryCard(int i) {
		return cardWithKids.getStoryCard(i);
	}

	public List<StoryCard> getStoryCardChildren() {
		return cardWithKids.getStoryCardChildren();
	}

	public int getWidth() {
		return cardWithKids.getWidth();
	}

	public float getWorstCaseEstimate() {
		return cardWithKids.getWorstCaseEstimate();
	}

	public boolean isIdRecievedFromRally() {
		return cardWithKids.isIdRecievedFromRally();
	}

	public void removeStoryCard(StoryCard storycard) {
		cardWithKids.removeStoryCard(storycard);
	}

	public void setActualEffort(float actual) {
		cardWithKids.setActualEffort(actual);
	}

	public void setAvailableEffort(float availableEffort) {
		cardWithKids.setAvailableEffort(availableEffort);
	}

	public void setBestCaseEstimate(float bestcase) {
		cardWithKids.setBestCaseEstimate(bestcase);
	}

	public void setDescription(String description) {
		cardWithKids.setDescription(description);
	}

	public void setEndDate(Timestamp enddate) {
		cardWithKids.setEndDate(enddate);
	}

	public void setHeight(int height) {
		cardWithKids.setHeight(height);
	}

	public void setId(long id) {
		cardWithKids.setId(id);
	}

	public void setIdRecievedFromRally(boolean idRecievedFromRally) {
		cardWithKids.setIdRecievedFromRally(idRecievedFromRally);
	}

	public void setLocationX(int locationX) {
		cardWithKids.setLocationX(locationX);
	}

	public void setLocationY(int locationY) {
		cardWithKids.setLocationY(locationY);
	}

	public void setMostlikelyEstimate(float mostlikely) {
		cardWithKids.setMostlikelyEstimate(mostlikely);
	}

	public void setName(String name) {
		cardWithKids.setName(name);
	}

	public void setParent(long id) {
		cardWithKids.setParent(id);
	}

	public void setRotationAngle(float angle) {
		cardWithKids.setRotationAngle(angle);
	}

	public void setStartDate(Timestamp startdate) {
		cardWithKids.setStartDate(startdate);
	}

	public void setStatus(String status) {
		cardWithKids.setStatus(status);
	}

	public void setStoryCardChildren(List<StoryCard> storycards) {
		cardWithKids.setStoryCardChildren(storycards);
	}

	public void setWidth(int width) {
		cardWithKids.setWidth(width);
	}

	public void setWorstCaseEstimate(float worstcase) {
		cardWithKids.setWorstCaseEstimate(worstcase);
	}

	public int storyCardSize() {
		return cardWithKids.storyCardSize();
	}

	protected IndexCardWithChildren getCardWithKids() {
		return cardWithKids;
	}

	protected void setCardWithKids(IndexCardWithChildren cardWithKids) {
		this.cardWithKids = cardWithKids;
	}

}

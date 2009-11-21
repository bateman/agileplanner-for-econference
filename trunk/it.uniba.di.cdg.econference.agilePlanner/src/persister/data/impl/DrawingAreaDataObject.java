package persister.data.impl;

import java.sql.Timestamp;
import java.util.List;

import persister.DrawingArea;
import persister.data.IndexCardWithChildren;
import persister.data.StoryCard;


public class DrawingAreaDataObject implements DrawingArea{

	private IndexCardWithChildren cardWithKidz = new IndexCardWithChildrenDataObject();
	
	public DrawingAreaDataObject() {
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

	public DrawingAreaDataObject(int width, int height, int locationX, int locationY) {
	    this();
	    setHeight(height);
	    setWidth(width);
	    setLocationX(locationX);
	    setLocationY(locationY);
    }

	public void addStoryCard(StoryCard storycard) {
		cardWithKidz.addStoryCard(storycard);
	}

	public IndexCardWithChildren clone() {
		return cardWithKidz.clone();
	}

	public float getActualEffort() {
		return cardWithKidz.getActualEffort();
	}

	public float getAvailableEffort() {
		return cardWithKidz.getAvailableEffort();
	}

	public float getBestCaseEstimate() {
		return cardWithKidz.getBestCaseEstimate();
	}

	public String getDescription() {
		return cardWithKidz.getDescription();
	}

	public Timestamp getEndDate() {
		return cardWithKidz.getEndDate();
	}

	public int getHeight() {
		return cardWithKidz.getHeight();
	}

	public long getId() {
		return cardWithKidz.getId();
	}

	public int getLocationX() {
		return cardWithKidz.getLocationX();
	}

	public int getLocationY() {
		return cardWithKidz.getLocationY();
	}

	public float getMostlikelyEstimate() {
		return cardWithKidz.getMostlikelyEstimate();
	}

	public String getName() {
		return cardWithKidz.getName();
	}

	public long getParent() {
		return cardWithKidz.getParent();
	}

	public float getRemainingEffort() {
		return cardWithKidz.getRemainingEffort();
	}

	public float getRotationAngle() {
		return cardWithKidz.getRotationAngle();
	}

	public Timestamp getStartDate() {
		return cardWithKidz.getStartDate();
	}

	public String getStatus() {
		return cardWithKidz.getStatus();
	}

	public StoryCard getStoryCard(int i) {
		return cardWithKidz.getStoryCard(i);
	}

	public List<StoryCard> getStoryCardChildren() {
		return cardWithKidz.getStoryCardChildren();
	}

	public int getWidth() {
		return cardWithKidz.getWidth();
	}

	public float getWorstCaseEstimate() {
		return cardWithKidz.getWorstCaseEstimate();
	}

	public boolean isCompleted() {
		return cardWithKidz.isCompleted();
	}

	public boolean isIdRecievedFromRally() {
		return cardWithKidz.isIdRecievedFromRally();
	}

	public boolean isStarted() {
		return cardWithKidz.isStarted();
	}

	public void removeStoryCard(StoryCard storycard) {
		cardWithKidz.removeStoryCard(storycard);
	}

	public void setActualEffort(float actual) {
		cardWithKidz.setActualEffort(actual);
	}

	public void setAvailableEffort(float availableEffort) {
		cardWithKidz.setAvailableEffort(availableEffort);
	}

	public void setBestCaseEstimate(float bestcase) {
		cardWithKidz.setBestCaseEstimate(bestcase);
	}

	public void setDescription(String description) {
		cardWithKidz.setDescription(description);
	}

	public void setEndDate(Timestamp enddate) {
		cardWithKidz.setEndDate(enddate);
	}

	public void setHeight(int height) {
		cardWithKidz.setHeight(height);
	}

	public void setId(long id) {
		cardWithKidz.setId(id);
	}

	public void setIdRecievedFromRally(boolean idRecievedFromRally) {
		cardWithKidz.setIdRecievedFromRally(idRecievedFromRally);
	}

	public void setLocationX(int locationX) {
		cardWithKidz.setLocationX(locationX);
	}

	public void setLocationY(int locationY) {
		cardWithKidz.setLocationY(locationY);
	}

	public void setMostlikelyEstimate(float mostlikely) {
		cardWithKidz.setMostlikelyEstimate(mostlikely);
	}

	public void setName(String name) {
		cardWithKidz.setName(name);
	}

	public void setParent(long id) {
		cardWithKidz.setParent(id);
	}

	public void setRotationAngle(float angle) {
		cardWithKidz.setRotationAngle(angle);
	}

	public void setStartDate(Timestamp startdate) {
		cardWithKidz.setStartDate(startdate);
	}

	public void setStatus(String status) {
		cardWithKidz.setStatus(status);
	}

	public void setStoryCardChildren(List<StoryCard> storycards) {
		cardWithKidz.setStoryCardChildren(storycards);
	}

	public void setWidth(int width) {
		cardWithKidz.setWidth(width);
	}

	public void setWorstCaseEstimate(float worstcase) {
		cardWithKidz.setWorstCaseEstimate(worstcase);
	}

	public int storyCardSize() {
		return cardWithKidz.storyCardSize();
	}
}

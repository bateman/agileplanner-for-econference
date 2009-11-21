package persister.data.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import persister.data.EditorElement;
import persister.data.IndexCard;
import persister.data.IndexCardWithChildren;
import persister.data.StoryCard;
import persister.data.StoryCardList;



public class IndexCardWithChildrenDataObject implements IndexCardWithChildren {

    private static final long serialVersionUID = 1384807442340718616L;

    protected Timestamp startdate, enddate;

    protected float availableEffort;
    private StoryCardList storyCardList = new StoryCardListDataObject();
    private IndexCard indexCard = new IndexCardDataObject();
    
    public IndexCardWithChildrenDataObject() {
        super();
        setAvailableEffort(0f);
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        setStartDate(ts);
        setEndDate(ts);
    }

    public void addStoryCard(StoryCard storycard) {
        storycard.setParent(this.getId());
        storyCardList.addStoryCard(storycard);
    }

    public List<StoryCard> getStoryCardChildren() {
        return storyCardList.getStoryCardChildren();
    }

    public void removeStoryCard(StoryCard storycard) {
    	storyCardList.removeStoryCard(storycard);
    }

    public void setStoryCardChildren(List<StoryCard> storycards) {
    	storyCardList.setStoryCardChildren(storycards);
    }

    public Timestamp getStartDate() {
        return this.startdate;
    }

    public boolean isCompleted() {
        return getStatus().equals(STATUS_COMPLETED);
    }

    public boolean isStarted() {
        return getStatus().equals(STATUS_IN_PROGRESS);
    }

    public void setAvailableEffort(float availableEffort) {
        this.availableEffort = availableEffort;
    }

    public void setEndDate(Timestamp enddate) {
        this.enddate = enddate;

    }

    public void setStartDate(Timestamp startdate) {
        this.startdate = startdate;

    }

    public float getAvailableEffort() {
        return this.availableEffort;
    }

    public Timestamp getEndDate() {
        return this.enddate;
    }
    
    public float getRemainingEffort() {
        float sumStoryCard = 0;

        for (StoryCard storycard : getStoryCardChildren()) {
            sumStoryCard += storycard.getMostlikelyEstimate();
        }

        return (getAvailableEffort() - sumStoryCard);
    }

	public IndexCardWithChildren clone() {
		IndexCardWithChildrenDataObject card = new IndexCardWithChildrenDataObject();
		card.setIndexCard((IndexCard)getIndexCard().clone());
		card.setStoryCardList((StoryCardList)getStoryCardList().clone());
		return card;
	}

	public float getActualEffort() {
		return indexCard.getActualEffort();
	}

	public float getBestCaseEstimate() {
		return indexCard.getBestCaseEstimate();
	}

	public String getDescription() {
		return indexCard.getDescription();
	}

	public int getHeight() {
		return indexCard.getHeight();
	}

	public long getId() {
		return indexCard.getId();
	}

	public int getLocationX() {
		return indexCard.getLocationX();
	}

	public int getLocationY() {
		return indexCard.getLocationY();
	}

	public float getMostlikelyEstimate() {
		return indexCard.getMostlikelyEstimate();
	}

	public String getName() {
		return indexCard.getName();
	}

	public long getParent() {
		return indexCard.getParent();
	}

	public float getRotationAngle() {
		return indexCard.getRotationAngle();
	}

	public String getStatus() {
		return indexCard.getStatus();
	}

	public int getWidth() {
		return indexCard.getWidth();
	}

	public float getWorstCaseEstimate() {
		return indexCard.getWorstCaseEstimate();
	}

	public boolean isIdRecievedFromRally() {
		return indexCard.isIdRecievedFromRally();
	}

	public void setActualEffort(float actual) {
		indexCard.setActualEffort(actual);
	}

	public void setBestCaseEstimate(float bestcase) {
		indexCard.setBestCaseEstimate(bestcase);
	}

	public void setDescription(String description) {
		indexCard.setDescription(description);
	}

	public void setHeight(int height) {
		indexCard.setHeight(height);
	}

	public void setId(long id) {
		indexCard.setId(id);
	}

	public void setIdRecievedFromRally(boolean idRecievedFromRally) {
		indexCard.setIdRecievedFromRally(idRecievedFromRally);
	}

	public void setLocationX(int locationX) {
		indexCard.setLocationX(locationX);
	}

	public void setLocationY(int locationY) {
		indexCard.setLocationY(locationY);
	}

	public void setMostlikelyEstimate(float mostlikely) {
		indexCard.setMostlikelyEstimate(mostlikely);
	}

	public void setName(String name) {
		indexCard.setName(name);
	}

	public void setParent(long id) {
		indexCard.setParent(id);
	}

	public void setRotationAngle(float angle) {
		indexCard.setRotationAngle(angle);
	}

	public void setStatus(String status) {
		indexCard.setStatus(status);
	}

	public void setWidth(int width) {
		indexCard.setWidth(width);
	}

	public void setWorstCaseEstimate(float worstcase) {
		indexCard.setWorstCaseEstimate(worstcase);
	}

	public int storyCardSize() {
		return storyCardList.storyCardSize();
	}

	public StoryCard getStoryCard(int i) {
		return storyCardList.getStoryCard(i);
	}

	protected StoryCardList getStoryCardList() {
		return storyCardList;
	}

	protected void setStoryCardList(StoryCardList storyCardList) {
		this.storyCardList = storyCardList;
	}

	protected IndexCard getIndexCard() {
		return indexCard;
	}

	protected void setIndexCard(IndexCard indexCard) {
		this.indexCard = indexCard;
	}
}
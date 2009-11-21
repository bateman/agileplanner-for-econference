package persister.data.impl;

import java.io.Serializable;

import persister.data.Backlog;
import persister.data.IndexCard;
import persister.data.StoryCard;

import java.util.ArrayList;

//import com.sun.xml.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;


public class StoryCardDataObject implements StoryCard, Serializable {

    private static final long serialVersionUID = -2801250845252360635L;
	
    private String acceptTestUrl = "http://localhost/?something_else_is_new";
    private String acceptTestText = "";
    private int currentSideUp ;
	private String color;
	private String cardOwner;
	private boolean rallyID;
	private byte[] handWritingImage;
	private String [] prototype = null;
	private String fitId;
	private IndexCard card = new IndexCardDataObject();

    public StoryCardDataObject() {
        setId(0);
        setParent(0);
        setName("Default StoryCard");
        setDescription("description");
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);
        setLocationX(DEFAULT_LOCATION_X);
        setLocationY(DEFAULT_LOCATION_Y);
        setBestCaseEstimate(0f);
        setMostlikelyEstimate(0f);
        setWorstCaseEstimate(0f);
        setActualEffort(0f);
        setStatus(STATUS_DEFINED);
        setColor("white");
        setRotationAngle(0);
        setCardOwner("TeamMember");
        setRallyID(false);
        setFitId("TestStorage.DefaultTest");
        
        
        if (getHandwritingImage()!= null)
        	setCurrentSideUp(HANDWRITING_SIDE);
        else
        	setCurrentSideUp(FRONT_SIDE);
    }

    public StoryCardDataObject(long parentid, String name, String description, int width, int height, int locationX, int locationY,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String cardColor, String cardOwner,float rotationAngle,boolean rallyID, String fitId) {

        setId(0);
        setParent(parentid);
        setName(name);
        setDescription(description);
        setWidth(width);
        setHeight(height);
        setLocationX(locationX);
        setLocationY(locationY);
        setBestCaseEstimate(bestCaseEstimate);
        setMostlikelyEstimate(mostlikelyEstimate);
        setWorstCaseEstimate(worstCaseEstimate);
        setActualEffort(actualEffort);
        setStatus(STATUS_DEFINED);
        setColor(cardColor);
        setRotationAngle(rotationAngle);
        setCardOwner(cardOwner);
       	setPrototype(prototype);
       	setRallyID(rallyID);
       	setFitId(fitId);
       	
        if (getHandwritingImage()!= null)
        	setCurrentSideUp(HANDWRITING_SIDE);
        else
        	setCurrentSideUp(FRONT_SIDE);
        
    }
    
    public StoryCardDataObject(long parentid, long cardID, String name, String description, int width, int height, int locationX, int locationY,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String cardColor,byte[] image, String cardOwner,float rotationAngle, String [] prototype, boolean rallyID, String fitId) {

        setId(cardID);
        setParent(parentid);
        setName(name);
        setDescription(description);
        setWidth(width);
        setHeight(height);
        setLocationX(locationX);
        setLocationY(locationY);
        setBestCaseEstimate(bestCaseEstimate);
        setMostlikelyEstimate(mostlikelyEstimate);
        setWorstCaseEstimate(worstCaseEstimate);
        setActualEffort(actualEffort);
        setStatus(status);
        setColor(cardColor);
        setRotationAngle(rotationAngle);
        setCardOwner(cardOwner);
       	setPrototype(prototype);
       	setRallyID(rallyID);
       	setFitId(fitId);
      
        if(image.length>0&&image!=null){//deep copy
        setHandWritingImage(new byte[image.length]);
        System.arraycopy(image, 0, handWritingImage, 0, image.length);
        setCurrentSideUp(HANDWRITING_SIDE);
        }else{
        	setCurrentSideUp(FRONT_SIDE);
        	setHandWritingImage(null);
        }
    }
    
    public StoryCardDataObject(long parentid, long cardID, String name, String description, int width, int height, int locationX, int locationY,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String cardColor,String cardOwner, float rotationAngle, String [] prototype,boolean rallyID, String fitId) {

        setId(cardID);
        setParent(parentid);
        setName(name);
        setDescription(description);
        setWidth(width);
        setHeight(height);
        setLocationX(locationX);
        setLocationY(locationY);
        setBestCaseEstimate(bestCaseEstimate);
        setMostlikelyEstimate(mostlikelyEstimate);
        setWorstCaseEstimate(worstCaseEstimate);
        setActualEffort(actualEffort);
        setStatus(STATUS_DEFINED);
        setColor(cardColor);
        setRotationAngle(rotationAngle);
        setCardOwner(cardOwner);
       	setPrototype(prototype);
       	setRallyID(rallyID);
       	setFitId(fitId);
       	
        if (getHandwritingImage()!= null)
        	setCurrentSideUp(HANDWRITING_SIDE);
        else
        	setCurrentSideUp(FRONT_SIDE);
        
    }

    public IndexCard clone() {
    	StoryCardDataObject clone = new StoryCardDataObject();
        clone.setCard(getCard());
    	clone.setParent(getParent());
        clone.setStatus(getStatus());
        clone.setDescription(getDescription());
        clone.setActualEffort(getActualEffort());
        clone.setBestCaseEstimate(getBestCaseEstimate());
        clone.setMostlikelyEstimate(getMostlikelyEstimate());
        clone.setWorstCaseEstimate(getWorstCaseEstimate());
        clone.acceptTestUrl = getAcceptTestUrl();
        clone.currentSideUp = getCurrentSideUp();
        clone.color = getColor();
        clone.cardOwner=getCardOwner();
        clone.acceptTestText = getAcceptTestText();
       	clone.prototype = getPrototype();
       	clone.rallyID=getRallyID();
       	clone.fitId=getFitId();
        
        if(getHandWritingImage()!=null){
            clone.handWritingImage = new byte[getHandWritingImage().length];
            System.arraycopy(getHandWritingImage(), 0, clone.handWritingImage, 0, getHandWritingImage().length);
            setCurrentSideUp(HANDWRITING_SIDE);
            }
        else{
        	 setCurrentSideUp(FRONT_SIDE);
        }
        return clone;
    }

    

    public boolean isCompleted() {
        return getStatus().equals(STATUS_COMPLETED);
    }

    public boolean isStarted() {
        return getStatus().equals(STATUS_IN_PROGRESS);
    }

    public String toString() {
        String storycard = "###################################\n" + "#           StoryCard             #\n"
                + "###################################\n\n" + "ID:\t\t" + getId() + "\n" + "Name:\t\t" + getName() + "\n" + "Parent:\t\t"
                + getParent() + "\n" + "Size:\t\t" + getWidth() + " x " + getHeight() + "\n" + "Location:\t(" + getLocationX() + " , "
                + getLocationY() + ")\n" + "Best case:\t" + getBestCaseEstimate() + "\n" + "Most likely:\t" + getMostlikelyEstimate() + "\n"
                + "Worst case:\t" + getWorstCaseEstimate() + "\n" + "Actual:\t\t" + getActualEffort() + "\n" + "Status:\t" + getStatus() + "\n\n";
        return storycard;
    }

    // DOES NOT UPDATE PARENT-CHILD RELATIONS!
    public void update(StoryCard storycard) {
        setActualEffort(storycard.getActualEffort());
        setBestCaseEstimate(storycard.getBestCaseEstimate());
        setStatus(storycard.getStatus());
        setDescription(storycard.getDescription());
        setHeight(storycard.getHeight());
        setLocationX(storycard.getLocationX());
        setLocationY(storycard.getLocationY());
        setMostlikelyEstimate(storycard.getMostlikelyEstimate());
        setName(storycard.getName());
        setWidth(storycard.getWidth());
        setWorstCaseEstimate(storycard.getWorstCaseEstimate());
        setAcceptTestUrl(storycard.getAcceptanceTestUrl());
        setCurrentSideUp(storycard.getCurrentSideUp());
        setCardOwner(storycard.getCardOwner());
       	setPrototype(storycard.getPrototype());
       	setRotationAngle(storycard.getRotationAngle());
        setRallyID(storycard.getRallyID());
        setFitId(storycard.getFitId());
        setAcceptTestText(storycard.getAcceptanceTestText());
        if(storycard.getHandwritingImage()!=null) {
        	setHandWritingImage(new byte[storycard.getHandwritingImage().length]); 
        	System.arraycopy(getHandWritingImage(), 0, storycard.getHandwritingImage(), 0, getHandWritingImage().length);
        	setCurrentSideUp(HANDWRITING_SIDE);
        }
    }
    

 
    
    public void setAcceptanceTestUrl(String url){
    	this.acceptTestUrl = url;
    }
    public String getAcceptanceTestUrl(){
    	return this.acceptTestUrl;
    }
    
    public int getCurrentSideUp() {
    	
        return this.currentSideUp;
    }

    public void setCurrentSideUp(int currentSideUp) {
    	
        this.currentSideUp = currentSideUp;
        
    }

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		
		this.color=color;
	}

	public String getAcceptanceTestText() {
		return this.acceptTestText;
	}

	public void setAcceptanceTestText(String text) {
		
		this.acceptTestText = text;
	}
	
	public byte[] getHandwritingImage()
	{
		return this.handWritingImage;
	}
	
	public void setHandwritingImage(byte[] image)
	{
		if(image!=null && image.length!=0)
		{//deep copy
	        this.handWritingImage = new byte[image.length];
	        System.arraycopy(image, 0, handWritingImage, 0, image.length);
	    }
		else
			this.handWritingImage = null;
	}

	public String getCardOwner() {
		return cardOwner;
	}

	public void setCardOwner(String cardOwner) {
		this.cardOwner = cardOwner;
	}

	public String []getPrototype() {
		
		return prototype;
	}
	
	public void setPrototype(String[] prototype) {
		
		this.prototype = prototype;
	}

	public boolean getRallyID() {
		return rallyID;
	}

	public void setRallyID(boolean rallyID) {
		this.rallyID = rallyID;
	}

	public String getFitId() {
		return fitId;
	}

	public void setFitId(String fitId) {
		this.fitId = fitId;
	}
	public ArrayList<String> attributes(){
		ArrayList<String> a = new ArrayList<String>();
		a.add("FitId");
		return a;
	}

	public String getAcceptTestUrl() {
		return acceptTestUrl;
	}

	public void setAcceptTestUrl(String acceptTestUrl) {
		this.acceptTestUrl = acceptTestUrl;
	}

	public String getAcceptTestText() {
		return acceptTestText;
	}

	public void setAcceptTestText(String acceptTestText) {
		this.acceptTestText = acceptTestText;
	}

	public byte[] getHandWritingImage() {
		return handWritingImage;
	}

	public void setHandWritingImage(byte[] handWritingImage) {
		this.handWritingImage = handWritingImage;
	}
	
	 public boolean equals(Object other){
	    	StoryCard storyCard = (StoryCard) other;
	    	return
    	   getHeight() == storyCard.getHeight()
    	&& getWidth() == storyCard.getWidth()
//    	&& getLocationX() == storyCard.getLocationX()
//    	&& getLocationY() == storyCard.getLocationY()
//    	&& getStatus() == storyCard.getStatus()
    	&& getId() == storyCard.getId()
    	&& getDescription().equals(storyCard.getDescription())
    	&& getName().equals(storyCard.getName())
    	;
    }

	public float getActualEffort() {
		return card.getActualEffort();
	}

	public float getBestCaseEstimate() {
		return card.getBestCaseEstimate();
	}

	public String getDescription() {
		return card.getDescription();
	}

	public int getHeight() {
		return card.getHeight();
	}

	public long getId() {
		return card.getId();
	}

	public int getLocationX() {
		return card.getLocationX();
	}

	public int getLocationY() {
		return card.getLocationY();
	}

	public float getMostlikelyEstimate() {
		return card.getMostlikelyEstimate();
	}

	public String getName() {
		return card.getName();
	}

	public long getParent() {
		return card.getParent();
	}

	public float getRemainingEffort() {
		return card.getRemainingEffort();
	}

	public float getRotationAngle() {
		return card.getRotationAngle();
	}

	public String getStatus() {
		return card.getStatus();
	}

	public int getWidth() {
		return card.getWidth();
	}

	public float getWorstCaseEstimate() {
		return card.getWorstCaseEstimate();
	}

	public boolean isIdRecievedFromRally() {
		return card.isIdRecievedFromRally();
	}

	public void setActualEffort(float actual) {
		card.setActualEffort(actual);
	}

	public void setBestCaseEstimate(float bestcase) {
		card.setBestCaseEstimate(bestcase);
	}

	public void setDescription(String description) {
		card.setDescription(description);
	}

	public void setHeight(int height) {
		card.setHeight(height);
	}

	public void setId(long id) {
		card.setId(id);
	}

	public void setIdRecievedFromRally(boolean idRecievedFromRally) {
		card.setIdRecievedFromRally(idRecievedFromRally);
	}

	public void setLocationX(int locationX) {
		card.setLocationX(locationX);
	}

	public void setLocationY(int locationY) {
		card.setLocationY(locationY);
	}

	public void setMostlikelyEstimate(float mostlikely) {
		card.setMostlikelyEstimate(mostlikely);
	}

	public void setName(String name) {
		card.setName(name);
	}

	public void setParent(long id) {
		card.setParent(id);
	}

	public void setRotationAngle(float angle) {
		card.setRotationAngle(angle);
	}

	public void setStatus(String status) {
		card.setStatus(status);
	}

	public void setWidth(int width) {
		card.setWidth(width);
	}

	public void setWorstCaseEstimate(float worstcase) {
		card.setWorstCaseEstimate(worstcase);
	}

	protected IndexCard getCard() {
		return card;
	}

	protected void setCard(IndexCard card) {
		this.card = card;
	}
}

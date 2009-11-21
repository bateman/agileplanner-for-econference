package persister.data;

import persister.AbstractRoot;

/**
 * @author webers
 * 
 */
public interface IndexCard extends AbstractRoot {

    public static final int DEFAULT_LOCATION_X = 0;
    public static final int DEFAULT_LOCATION_Y = 0;
    public static final String STATUS_ACCEPTED = "Accepted";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_DEFINED = "Defined";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_TODO = "ToDo";   
    
    public float getActualEffort();
    public float getBestCaseEstimate();
    public String getDescription();
    public int getHeight();
    public int getLocationX();
    public int getLocationY();
    public float getMostlikelyEstimate();
    public long getParent();
    public float getRemainingEffort();
    public float getRotationAngle();
    public String getStatus();
    public int getWidth();
    public float getWorstCaseEstimate();
    public boolean isCompleted();
	public boolean isIdRecievedFromRally();
    public boolean isStarted();
    public void setActualEffort(float actual);
    public void setBestCaseEstimate(float bestcase);
    public void setDescription(String description);
    public void setHeight(int height);
    public void setIdRecievedFromRally(boolean idRecievedFromRally);
    public void setLocationX(int locationX);
    public void setLocationY(int locationY);
    public void setMostlikelyEstimate(float mostlikely);
    public void setParent(long id);
    public void setRotationAngle(float angle); 
    public void setStatus(String status);
    public void setWidth(int width);
    public void setWorstCaseEstimate(float worstcase);
    

}

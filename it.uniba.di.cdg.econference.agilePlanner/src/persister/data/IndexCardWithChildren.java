package persister.data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author dhillonh
 * 
 */
public interface IndexCardWithChildren extends IndexCard, StoryCardList {

    public float getAvailableEffort();
    public void setAvailableEffort(float availableEffort);
    public float getRemainingEffort(); 

    public Timestamp getEndDate();
    public Timestamp getStartDate();
    public void setEndDate(Timestamp enddate);
    public void setStartDate(Timestamp startdate);
    public IndexCardWithChildren clone();
}

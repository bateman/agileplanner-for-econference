/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the data model for the Iteration object. only data in this object does not run the risk 
 *								of being disposed by the framework at a given time.  
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import persister.data.IndexCard;
import persister.data.Iteration;
import persister.data.StoryCard;
import persister.data.impl.IterationDataObject;

import cards.CardConstants;
import fitintegration.PluginInformation;
import static util.Comparer.compare;

public class IterationCardModel extends ContainerModel {

    private static IPropertyDescriptor[] descriptors;

    private static final long serialVersionUID = 1;

    public static final String AVAILABLE_EFFORT_PROP = "IterationCardModel.availableEffort";

    public static final String CHILD_ADDED_PROP = "IterationCardModel.ChildAdded";

    public static final String CHILD_REMOVED_PROP = "IterationCardModenmhl.ChildRemoved";

    public static final String COMPLETE_PROP = "IterationCardModel.complete";

    public static final String DESCRIPTION_PROP = "IterationCardModel.description";

    public static final String END_DATE_PROP = "IterationCardModel.endDate";
    
    public static final String START_DATE_PROP = "IterationCardModel.endDate";
    
    public static final String LOCATION_PROP = "IterationCardModel.location";

    public static final String NAME_PROP = "IterationCardModel.name";

    public static final String REMAINING_EFFORT_PROP = "IterationCardModel.remainingEffort";

    public static final String SEARCH_PROP = "IterationCardModel.search";

    public static final String SIZE_PROP = "IterationCardModel.size";

    public static final String X_PROP = "IterationCardModel.x";

    public static final String Y_PROP = "IterationCardModel.y";

    static {
        descriptors = new IPropertyDescriptor[] { new TextPropertyDescriptor(NAME_PROP, "Name"),
                new TextPropertyDescriptor(DESCRIPTION_PROP, "Description"), new TextPropertyDescriptor(END_DATE_PROP, "End Date"),
                new TextPropertyDescriptor(AVAILABLE_EFFORT_PROP, "Available Effort"),
                new TextPropertyDescriptor(REMAINING_EFFORT_PROP, "Remaining Effort") };

        for (int i = 0; i < descriptors.length; i++) {
            ((PropertyDescriptor) descriptors[i]).setValidator(new ICellEditorValidator() {
                public String isValid(Object value) {
                    return "";
                }
            });
        }
    }// END STATIC!

    private float bestCaseEstimate, worstCaseEstimate, mostLikelyEstimate, actualEffort, remainingEffort = 0;

    private Dimension dragMoveDelta = new Dimension();

    private String endDate = "2007/12/30" /*, oldDate = "2007/12/30"*/, status = IndexCard.STATUS_DEFINED;

    private String startDate="2007/12/30";
    private int endYear, endMonth, endDay;
    private int startYear, startMonth, startDay;

    private boolean hasHiddenChildren;

    private int labelEditor;// keeps track of what label is being edited

    private ProjectModel projectModel;

    /** ************************************************************************************************ */
    /** ************************************************************************************************ */
    public IterationCardModel() {
        this(new IterationDataObject());
    }

    public IterationCardModel(Iteration iteration) {

    	
        setIterationDataObject(iteration);

        setDefaultIterationDates();
        this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
    }

    public IterationCardModel(ProjectModel aPM) {
        super();
        projectModel = aPM;
        this.getIterationDataObject().setWidth(
                (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationCardWidth
                        : CardConstants.WindowsIterationCardWidth);
        this.getIterationDataObject().setHeight(
                (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationCardHeight
                        : CardConstants.WindowsIterationCardHeight);
        setDefaultIterationDates();
        this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
    }

    private boolean checkyearformat(String date) {
        Pattern pat = Pattern.compile("[0-9][0-9][0-9][0-9]/[0-1][0-9]/[0-3][0-9]");
        Matcher mat = pat.matcher(date);
        if (mat.find()) {
            return true;
        }
        else {
            MessageDialog.openWarning(null, "Incorrect Date Format",
                    "The date format was incorrect. \nThe correct format is: YYYY/MM/DD.\n Please try again!");
            return false;
        }
    }

    /**
     * Utility Methods
     * ************************************************************************************
     */
    /** ************************************************************************************************ */
    
    private void setDefaultIterationDates()
    {
    	SimpleDateFormat iterationTimeFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Calendar time = Calendar.getInstance();
    	   	
    	System.out.println("Time preroll: " + time.getTime().toString());
    	System.out.println("Format: " + iterationTimeFormat.format(time.getTime()));
    	//this.setStartDate("2010/10/20");
    	this.setStartDate(iterationTimeFormat.format(time.getTime()));
    	
    	time.add(Calendar.WEEK_OF_MONTH, 2);
    	System.out.println("Time postroll: " + time.getTime().toString());
    	System.out.println("Format: " + iterationTimeFormat.format(time.getTime()));
    	this.setEndDate(iterationTimeFormat.format(time.getTime()));
    	//this.endDate = "2010/10/20";//= iterationTimeFormat.format(time.getTime());
    	
    	    	
    }
    
    private void parseEndDate() {
        String date = this.endDate;
        if (date.lastIndexOf(" ") != -1) {
            date = date.substring(0, date.lastIndexOf(" "));
        }
        date = date.replace('-', '/');

        if (this.checkyearformat(date)) {
            this.endDate = date;
            int index = date.lastIndexOf("/");
            int subInt;
            int newEndDay, newEndMonth, newEndYear;
            String sub = date.substring(index + 1);
            date = date.substring(0, index);
            subInt = Integer.valueOf(sub);

            if ((subInt > 0) && (subInt < 32)) {
                newEndDay = Integer.valueOf(sub);
                index = date.lastIndexOf("/");
                sub = date.substring(index + 1);
                date = date.substring(0, index);
                subInt = Integer.valueOf(sub);

                if ((subInt > 0) && (subInt < 13)) {
                    newEndMonth = Integer.valueOf(sub);
                    subInt = Integer.valueOf(date);
                    newEndYear = Integer.valueOf(date);
                    this.endDay = newEndDay;
                    this.endMonth = newEndMonth;
                    this.endYear = newEndYear;
                }
                else {
                   // this.endDate = this.oldDate;
                    MessageDialog.openWarning(null, "Invalid Date", "The date entered was not a valid date. \nPlease check the date and try again!");
                }
            }
            else {
                MessageDialog.openWarning(null, "Invalid Date", "The date entered was not a valid date. \nPlease check the date and try again!");
               // this.endDate = this.oldDate;
            }
        }
        
    }
    
    private void parseStartDate(){
        String date = this.startDate;
        if (date.lastIndexOf(" ") != -1) {
            date = date.substring(0, date.lastIndexOf(" "));
        }
        date = date.replace('-', '/');

        if (this.checkyearformat(date)) {
            this.startDate = date;
            int index = date.lastIndexOf("/");
            int subInt;
            int newStartDay, newStartMonth, newStartYear;
            String sub = date.substring(index + 1);
            date = date.substring(0, index);
            subInt = Integer.valueOf(sub);

            if ((subInt > 0) && (subInt < 32)) {
                newStartDay = Integer.valueOf(sub);
                index = date.lastIndexOf("/");
                sub = date.substring(index + 1);
                date = date.substring(0, index);
                subInt = Integer.valueOf(sub);

                if ((subInt > 0) && (subInt < 13)) {
                    newStartMonth = Integer.valueOf(sub);
                    subInt = Integer.valueOf(date);
                    newStartYear = Integer.valueOf(date);
                    this.startDay = newStartDay;
                    this.startMonth = newStartMonth;
                    this.startYear = newStartYear;
                }
                else {
                    MessageDialog.openWarning(null, "Invalid Date", "The date entered was not a valid date. \nPlease check the date and try again!");
                }
            }
            else {
                MessageDialog.openWarning(null, "Invalid Date", "The date entered was not a valid date. \nPlease check the date and try again!");
            }
        }
        
    }

    private boolean updateHiddenChild() {
        boolean hiddenChild = false;
        for (StoryCardModel child : getChildrenList()) {
            if ((this.getLocation().x < child.getLocation().x) && ((this.getLocation().x + this.getSize().width) > child.getLocation().x)
                    && (this.getLocation().y < child.getLocation().y) && ((this.getLocation().y + this.getSize().height) > child.getLocation().y)) {
                // do nothing
            }
            else {
                hiddenChild = true;
            }
        }
        return hiddenChild;
    }

    @Override
    protected Iteration getDataObject() {
        return (Iteration) super.getDataObject();
    }

    /**
     * Add Remove
     * Child**********************************************************************************
     */
    /** ************************************************************************************************ */

    
    public void updateChild(StoryCardModel child) {
        super.updateChild(child);
        this.updateValuesFromChildren();        
       // child.setLocation(child.getLocation());        
        this.firePropertyChange(CHILD_ADDED_PROP, null, child);
    }

    @Override
    public void addNewChild(StoryCardModel child) {
        super.addNewChild(child);
        this.updateValuesFromChildren();
        this.firePropertyChange(CHILD_ADDED_PROP, null, child);
    }

    @Override
    public void addChildIncommingOnLoad(StoryCardModel child) {
        super.addChildIncommingOnLoad(child);
        this.updateValuesFromChildren();
        this.firePropertyChange(CHILD_ADDED_PROP, null, child);
    }

    @Override
    public void addChildSetParent(StoryCardModel child) {
        super.addChildSetParent(child);
        this.updateValuesFromChildren();
        this.firePropertyChange(CHILD_ADDED_PROP, null, child);
    }

    public void checkThenSetComplete() {
        if (this.isComplete() == false) {
            if (MessageDialog.openQuestion(null, "Complete Iteration",
                    "Are you sure that you want to set this Iteration and all of its StoryCards to complete? This operation can not be undone!")) {
                this.setComplete(persister.data.IndexCard.STATUS_COMPLETED);
            }
        }
    }

    public String getActualEffort() {
        return Double.toString((Math.round(this.actualEffort * 100) / 100.00));
    }

    public String getAvailableEffort() {
        return String.valueOf(Double.toString((Math.round(this.getIterationDataObject().getAvailableEffort() * 100) / 100.00)));
    }

    public String getBestCaseEstimate() {
        return Double.toString((Math.round(this.bestCaseEstimate * 100) / 100.00));
    }

    public String getDescription() {
        return getIterationDataObject().getDescription();
    }

    public Timestamp getEndDate() {
        return getIterationDataObject().getEndDate();
    }

    public int getEndDay() {
        return this.endDay;
    }

    public int getEndMonth() {
        return this.endMonth;
    }

    public int getEndYear() {
        return this.endYear;
    }

    @Override
    public Image getIcon() {
        return AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.NewIterationIcon).createImage();
    }

    public Iteration getIterationDataObject() {
        return getDataObject();
    }

    public String getIterationEndDate() {
        return this.endDate;
    }

    public int getLabelEditor() {
        return this.labelEditor;
    }

    public Point getLocation() {
        return new Point(getIterationDataObject().getLocationX(), getIterationDataObject().getLocationY());
    }

    public String getMostLikelyEstimate() {
        // return Float.toString(this.mostLikelyEstimate);
        return Double.toString((Math.round(this.mostLikelyEstimate * 100) / 100.00));
    }

    public String getName() {
        return getIterationDataObject().getName();// +" Id is:
    }

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return descriptors;
    }

    public String getRemainingEffort() {
        return Double.toString((Math.round(this.remainingEffort * 100) / 100.00));
    }

    @Override
    public String getStatus() {
        return getIterationDataObject().getStatus();
    }

    public String getWorstCaseEstimate() {
        return Double.toString((Math.round(this.worstCaseEstimate * 100) / 100.00));
    }

    public boolean hasHiddenChildren() {
        return this.hasHiddenChildren;
    }

    public boolean isComplete() {
        if (getIterationDataObject().getStatus().equals(persister.data.IndexCard.STATUS_COMPLETED))
            return true;
        else
            return false;
    }

    @Override
    public void removeChild(StoryCardModel child) {
        super.removeChild(child);
        this.updateValuesFromChildren();
        this.firePropertyChange(CHILD_REMOVED_PROP, child, null);
    }
    public void searchRequest() {
        this.firePropertyChange(SEARCH_PROP, null, this);
    }

    public void setAvailableEffortIncomming(float availableEffort) {
        getIterationDataObject().setAvailableEffort(availableEffort);
        this.updateValuesFromChildren();
        this.firePropertyChange(AVAILABLE_EFFORT_PROP, null, getIterationDataObject().getAvailableEffort());
    }

    public void setBestCaseEstimate(String newBestCaseEstimate) {
        if (newBestCaseEstimate != null) {
            this.bestCaseEstimate = Float.valueOf(newBestCaseEstimate);
        }
    }

    public void setDescription(String newDescription) {
        if (newDescription != null) {
            getIterationDataObject().setDescription(newDescription);
        }
        this.firePropertyChange(DESCRIPTION_PROP, null, getIterationDataObject().getDescription());
    }

    public void setDescriptionIncomming(String newDescription) {
        if (newDescription != null) {
            getIterationDataObject().setDescription(newDescription);
        }
        this.firePropertyChange(DESCRIPTION_PROP, null, getIterationDataObject().getDescription());
    }

    public void setDragMoveDelta(Dimension dragMoveDelta) {
        this.dragMoveDelta = dragMoveDelta;
    }

    public void setIterationDataObject(Iteration iteration) {
        this.setCardDataObject(iteration);
        this.firePropertyChange(LOCATION_PROP, null, iteration);
    }

    public void setIterationEndDateIncomming(String newEndDate) {
        if (newEndDate != null) {
            this.endDate = newEndDate;
            this.parseEndDate();
            String ds = this.getEndYear() + "-" + this.getEndMonth() + "-" + this.getEndDay();
            Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());
            getIterationDataObject().setEndDate(ts);
        }
        this.firePropertyChange(END_DATE_PROP, null, this.endDate);
    }
    
    public void setIterationStartDateIncomming(String newStartDate) {
        if (newStartDate != null) {
            this.startDate = newStartDate;
            this.parseStartDate();
            String ds = this.getStartYear() + "-" + this.getStartMonth() + "-" + this.getStartDay();
            Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());
            getIterationDataObject().setStartDate(ts);
        }
        this.firePropertyChange(START_DATE_PROP, null, this.startDate);
    }
   
    public void setLabelEditor(int iterationlabelEditorId) {
        this.labelEditor = iterationlabelEditorId;
    }

    public void setLocationIncomming(Point newLocation) {
        if (newLocation == null) {
            throw new IllegalArgumentException();
        }
        getIterationDataObject().setLocationX(newLocation.x);
        getIterationDataObject().setLocationY(newLocation.y);
        this.updateChildrenFigure();
        this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
    }

    public void setName(String newName) {
        if (newName != null) {
            getIterationDataObject().setName(newName);
        }
        this.firePropertyChange(NAME_PROP, null, getIterationDataObject().getName());
    }

    public void setNameIncomming(String newName) {
        if (newName != null) {
            getIterationDataObject().setName(newName);
        }
        this.firePropertyChange(NAME_PROP, null, getIterationDataObject().getName());
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
        // this.projectModel.addIterationToProject(this);
    }

    public void setSize(Dimension newSize) {
        if (newSize != null) {
            getIterationDataObject().setWidth(newSize.width);
            getIterationDataObject().setHeight(newSize.height);
        }
        this.hasHiddenChildren = this.updateHiddenChild();
        this.firePropertyChange(SIZE_PROP, null, new Dimension(getIterationDataObject().getWidth(), getIterationDataObject().getHeight()));
    }
    public void setSizeIncomming(Dimension newSize) {
        if (newSize != null) {
            getIterationDataObject().setWidth(newSize.width);
            getIterationDataObject().setHeight(newSize.height);
        }
        this.hasHiddenChildren = this.updateHiddenChild();
        this.firePropertyChange(SIZE_PROP, null, new Dimension(getIterationDataObject().getWidth(), getIterationDataObject().getHeight()));
    }

    public void setComplete(String status) {

        if (getIterationDataObject().getStatus().equals(persister.data.IndexCard.STATUS_COMPLETED)) {
            for (StoryCardModel card : getChildrenList()) {
                card.setComplete(persister.data.IndexCard.STATUS_COMPLETED);
            }
        }
        this.firePropertyChange(COMPLETE_PROP, null, status);
    }

    public void setStatusIncomming(String status) {

        this.getIterationDataObject().setStatus(status);
        if (getIterationDataObject().getStatus().equals(persister.data.IndexCard.STATUS_COMPLETED)) {
            for (StoryCardModel card : getChildrenList()) {
                card.setComplete(persister.data.IndexCard.STATUS_COMPLETED);
            }// TODO
        }
        this.firePropertyChange(COMPLETE_PROP, null, persister.data.IndexCard.STATUS_COMPLETED);
    }

    public void setWorstCaseEstimate(String newWorstCaseEstimate) {
        if (newWorstCaseEstimate != null) {
            this.worstCaseEstimate = Float.valueOf(newWorstCaseEstimate);
        }
    }

    @Override
    public String toString() {
        return "Iteration Card " + this.hashCode();
    }

    @Override
    public void unDeleteChildIncomming(StoryCardModel child) {
        super.unDeleteChildIncomming(child);
        this.updateValuesFromChildren();
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
        updateChildPriorities();
        this.updateValuesFromChildren();
        this.firePropertyChange(CHILD_ADDED_PROP, null, null);
    }

    @Override
    public void setLocation(Point newLocation) {
        super.setLocation(newLocation);
        this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
        updateChildrenFigure();

    }
    
    
    
    public void updateValuesFromChildren(){
    	
    	this.bestCaseEstimate = this.worstCaseEstimate = this.mostLikelyEstimate = this.actualEffort = (float) 0.0;
        this.remainingEffort = (float) 0.0;
        
        float actual =(float) 0.0;
        float best =(float) 0.0;
        float most =(float) 0.0;
      
        float worst=(float) 0.0;
     
        for ( StoryCardModel scm : this.getChildrenList())
     	   {
        	StoryCard sc2  = scm.getDataObject();
      	       
     	       actual+=  sc2.getActualEffort();
     	       best  +=  sc2.getBestCaseEstimate();
     	       most  +=  sc2.getMostlikelyEstimate();
     	       worst +=  sc2.getWorstCaseEstimate();
     	     
     	            this.bestCaseEstimate = Float.valueOf(best);
     	            this.worstCaseEstimate = Float.valueOf(worst);
     	            this.mostLikelyEstimate = Float.valueOf(most);
     	            this.actualEffort = Float.valueOf(actual);     	           
     	            
     	            this.remainingEffort = (this.mostLikelyEstimate - this.actualEffort);
     	        sc2=null;      	   
     	   }
         this.firePropertyChange(LOCATION_PROP, null, this.getLocation());     	         	
    }
    
    public void colorChanged(StoryCardModel child) {
		
		this.firePropertyChange(CHILD_ADDED_PROP, null, child);
	}

	public void addChildOnArrange(StoryCardModel storyCardModel) {
		if (storyCardModel.getId() != 0) {
			this.children.add(storyCardModel);			
		}		
        this.firePropertyChange(CHILD_ADDED_PROP, null, storyCardModel);
		
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}
	
	public int compareEndDateTo(IterationCardModel other){
		int result;
		result = compare(getEndYear()).to(other.getEndYear());
		if (result == 0){
			result = compare(getEndMonth()).to(other.getEndMonth());
		}
		if (result == 0){
			result = compare(getEndDay()).to(other.getEndDay());
		}
		return result;
	}
}

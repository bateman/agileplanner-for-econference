/***************************************************************************************************
 *Version 2.0
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the data model for the StoryCard object. only data in this object does not run the risk 
 *								of being disposed by the framework at a given time.  
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.model;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import java.util.Hashtable;

import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.IndexCard;
import persister.data.StoryCard;
import persister.data.impl.AcceptanceTestDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.acceptTests.TestStatisticsProvider;
import fitintegration.PluginInformation;

public class StoryCardModel extends IndexCardModel {

	public static final String ACTUAL_PROP = "StoryCardModel.actualEffort";

	public static final String BESTCASE_PROP = "StoryCardModel.bestCaseEstimate";

	public static final String COLOR_PROP = "StoryCardModel.color";

	public static final String COMPLETE_PROP = "StoryCardModel.complete";

	private static final String DEFINED_PROP = "StoryCardModel.defined";

	public static final String DESCRIPTION_PROP = "StoryCardModel.description";
	
	private static final String PROTOTYPE_PROP = "StoryCardModel.prototype";

	private static IPropertyDescriptor[] descriptors;

	private static final String INPROGRESS_PROP = "StoryCardModel.inprogress";

	public static final String LOCATION_PROP = "StoryCardModel.location";

	public static final String MOSTLIKEYL_PROP = "StoryCardModel.mostLikelyEstimate";

	public static final String NAME_PROP = "StoryCardModel.name";

	public static final String SEARCH_PROP = "StoryCardModel.search";

	private static final long serialVersionUID = 1;

	public static final String SIZE_PROP = "StoryCardModel.size";

	public static final Hashtable<String, Color> COLORS = new Hashtable<String, Color>(){
		{
			put("red",			new Color(null,255, 99, 71));
			put("blue",			new Color(null,173, 216, 230));
			put("green",		new Color(null,143, 188, 143));
			put("yellow",		new Color(null,255, 246, 143));
			put("white",		new Color(null,255, 255, 255));
			put("pink",			new Color(null,255, 192, 203));
			put("peach",		new Color(null,255, 218, 185));
			put("khaki",		new Color(null,189, 183, 107));
			put("aqua",			new Color(null,102, 205, 170));
			put("grey",			new Color(null,190, 190, 190));
			put("gray",			new Color(null,190, 190, 190));
			put("transparent",	new Color(null,123, 111, 120));
		}
	};
	// private long parentId;


	private static String storyCardStatusPre = null;

	private String storyCardFont;

	private int storyCardFontSize;
	

	public static final String WORSTCASE_PROP = "StoryCardModel.worstCaseEstimate";

	public static final String X_PROP = "StoryCardModel.x";

	public static final String Y_PROP = "StoryCardModel.y";

	// rotation attributes
	private int widthClientArea = 100;

	private int heightClientArea = 100;

	private float aRotate = 0;


	
	
	static {
		descriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(NAME_PROP, "Name"),
				new TextPropertyDescriptor(DESCRIPTION_PROP, "Description"),
				new TextPropertyDescriptor(BESTCASE_PROP, "Best case estimate"),
				new TextPropertyDescriptor(WORSTCASE_PROP,
						"Worst case extimate"),
				new TextPropertyDescriptor(MOSTLIKEYL_PROP,
						"Most likely extimate"),
				new TextPropertyDescriptor(ACTUAL_PROP, "Actual Effort"),
				new TextPropertyDescriptor(PROTOTYPE_PROP, "Prototype")};

		for (int i = 0; i < descriptors.length; i++) {
			((PropertyDescriptor) descriptors[i])
					.setValidator(new ICellEditorValidator() {
						public String isValid(Object value) {
							return "";
						}
					});
		}
	}// END STATIC!

	private Image descriptionImage;

	private Dimension dragMoveDelta = new Dimension();

	private byte[] handwritenImage;

	private boolean handwritten = false;

	private int labelEditor;

	private ContainerModel parent;

	private ProjectModel parentProject;

	public int priority;

	private Dimension size = new Dimension(0, 0);

	/**
	 * Moved from
	 * IndexCardModel*************************************************************************
	 */
	/** ************************************************************************************************ */
	private int smallHeight = 8;

	private String status = IndexCard.STATUS_DEFINED;

	private boolean rotationCommandExecuted = false;

	public static final int LINE_HEIGHT = 18;
	public static final int INITIAL_X_OFFSET = 20;
	public static final int INITIAL_Y_OFFSET = 9;
	public static final int ESTIMATE_RIGHT_SIDE_OFFSET = ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X"))
    		? CardConstants.MacEstimateRightSideOffset
            : CardConstants.WindowsEstimateRightSideOffset - 52);
    public static final int ESTIMATE_LEFT_SIDE_OFFSET = ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X"))
    		? CardConstants.MacEstimateLeftSideOffset
            : CardConstants.WindowsEstimateLeftSideOffset);
  
    
	/** ************************************************************************************************ */
	/** ************************************************************************************************ */
	public StoryCardModel() {
		this(new StoryCardDataObject());
		this.getStoryCard().setWidth(INITIAL_X_OFFSET + ((System.getProperty("os.name")
						.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardWidth
						: CardConstants.WindowsStoryCardWidth));
		this.getStoryCard().setHeight((System.getProperty("os.name")
						.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardheight
						: CardConstants.WindowsStoryCardheight);
		this.smallHeight = (System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardSmallHeight
				: CardConstants.WindowsStoryCardSmallHeight;
		this.size = new Dimension(this.getStoryCard().getWidth(), this.getStoryCard()
				.getHeight());
		//
		this.widthClientArea = getBestFitClientArea(this.size).width;
		this.heightClientArea = getBestFitClientArea(this.size).height;
		

		this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
	}

	/*
	 * required to create storycard in AP UI public StoryCardModel() { this(new
	 * StoryCardDataObject()); }
	 */

	public StoryCardModel(StoryCard storycard) {
		setStoryCardDataObject(storycard);
		this.storyCardFont = "";
		this.storyCardFontSize = 8;

		if (storyCardStatusPre != null)

		this.setStatus(storyCardStatusPre);
		this.size = new Dimension(storycard.getWidth(), storycard.getHeight());
		this.smallHeight = (System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardSmallHeight
				: CardConstants.WindowsStoryCardSmallHeight;
		this.widthClientArea = getBestFitClientArea(this.size).width;
		this.heightClientArea = getBestFitClientArea(this.size).height;

		this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
	}

	public void checkThenSetComplete() {
		if (this.isComplete() == false) {
			if (MessageDialog
					.openQuestion(
							null,
							"Complete Iteration",
							"Are you sure that you want to set this Iteration and all of its StoryCards to complete? This operation can not be undone!")) {
				this.setComplete(persister.data.IndexCard.STATUS_COMPLETED);
			}
		}
	}

	/**
	 * Utility Methods
	 * ************************************************************************************
	 */
	/** ************************************************************************************************ */

	public void collapse() {
		this
				.setSize(new Dimension(getStoryCard().getWidth(),
						this.smallHeight));
	}

	public void expand() {

		this.setSize(new Dimension(getStoryCard().getWidth(),
				CardConstants.WindowsStoryCardheight));
	}

	public String getActualEffort() {
		String converted = Float.toString(getStoryCard().getActualEffort());
		return converted;
	}

	public String getBestCaseEstimate() {
		String converted = Float.toString(getStoryCard().getBestCaseEstimate());
		return converted;
	}

	public void setStatus(String status) {
		getStoryCard().setStatus(status);
	}
	

	@Override
	protected StoryCard getDataObject() {
		return (StoryCard) super.getDataObject();
	}

	public String getDescription() {
		String currDescription = getStoryCard().getDescription();
		return currDescription.equals("0") ? "-Click to enter text-" : currDescription;
	}
	
	public String [] getPrototype(){
		return getStoryCard().getPrototype();
	}

	public Image getHandWrittenDescImage() {
		return this.descriptionImage;
	}

	public byte[] getHandwrittenImage() {
		return this.handwritenImage;
	}

	@Override
	public Image getIcon() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				PluginInformation.getPLUGIN_ID(), CardConstants.NewStoryCardIcon)
				.createImage();
	}

	public int getLabelEditor() {
		return this.labelEditor;
	}

	/**
	 * Getters & Setters
	 * **********************************************************************************
	 */
	/** ************************************************************************************************ */

	public String getMostLikelyEstimate() {
		String converted = Float.toString(getStoryCard()
				.getMostlikelyEstimate());
		return converted;
	}

	public String getName() {
		return getStoryCard().getName();
	}

	public ContainerModel getParent() {
		return this.parent;
	}

	public String getPriority() {
		String converted = Integer.toString(this.priority);
		return converted;
	}

	public String getRemainingEstimate() {
		String converted = Double.toString((Math.round(getStoryCard()
				.getRemainingEffort() * 100) / 100.00));
		return converted;
	}

	public String getStatus() {
		return getStoryCard().getStatus();

	}

	public StoryCard getStoryCard() {
		return getDataObject();
	}

	public String getWorstCaseEstimate() {
		String converted = Float
				.toString(getStoryCard().getWorstCaseEstimate());
		return converted;
	}

	public boolean isComplete() {
		if (this.getStoryCard().getStatus().equals(
				persister.data.IndexCard.STATUS_COMPLETED))
			return true;
		else
			return false;
	}

	public boolean isDefined() {
		if (this.getStoryCard().getStatus().equals(
				persister.data.IndexCard.STATUS_DEFINED))
			return true;
		else
			return false;
	}

	public boolean isHandwritten() {
		return this.handwritten;
	}

	public boolean isInProgress() {
		if (this.getStoryCard().getStatus().equals(
				persister.data.IndexCard.STATUS_IN_PROGRESS))
			return true;
		else
			return false;
	}

	public void moveStoryCardToNewParentUpdatePriorities() {

		this.updatePriorities();
		this.firePropertyChange(SIZE_PROP, null, this.getSize());
	}

	public void searchRequest() {
		this.firePropertyChange(SEARCH_PROP, null, this);
	}

	public void setBestCaseEstimate(String newBestCaseEstimate) {
		if (newBestCaseEstimate != null) {
			this.updateParentValues(this.getParent());
			this.firePropertyChange(BESTCASE_PROP, null, newBestCaseEstimate);
		}
	}

	public void setWorstCaseEstimate(String newWorstCaseEstimate) {
		if (newWorstCaseEstimate != null) {
			this.updateParentValues(this.getParent());
			this.firePropertyChange(WORSTCASE_PROP, null, newWorstCaseEstimate);
		}
	}

	public void setActualEstimate(String newActualEstimate) {
		if (newActualEstimate != null) {
			this.updateParentValues(this.getParent());
			this.firePropertyChange(ACTUAL_PROP, null, newActualEstimate);
		}
	}

	public void setRemainingEstimate(String newRemainingEstimate) {
		if (newRemainingEstimate != null) {
			this.updateParentValues(this.getParent());
			this.firePropertyChange(ACTUAL_PROP, null, newRemainingEstimate);
		}
	}

	public void setBestCaseEstimateIncomming(float newBestCaseEstimate) {
		getStoryCard().setBestCaseEstimate(newBestCaseEstimate);
		this.updateParentValues(this.getParent());
		this.firePropertyChange(BESTCASE_PROP, null, newBestCaseEstimate);
	}

	public void setComplete(String status) {
		getStoryCard().setStatus(status);

		this.firePropertyChange(COMPLETE_PROP, null, status);
	}

	public void setDefined() {
		getStoryCard().setStatus(status);
		this.firePropertyChange(DEFINED_PROP, null,
				persister.data.IndexCard.STATUS_DEFINED);
	}

	public void setDescription(String newDescription) {
		if (newDescription != null) {
			getStoryCard().setDescription(newDescription);
			this.firePropertyChange(DESCRIPTION_PROP, null, getDescription());
		}
	}
	
	public void setPrototype(String [] newPrototype) {
		if (newPrototype != null) {
			getStoryCard().setPrototype(newPrototype);
			this.firePropertyChange(PROTOTYPE_PROP, null, newPrototype);
		}
	}

	public void setDescriptionIncomming(String newDescription) {
		if (newDescription != null) {
			getStoryCard().setDescription(newDescription);
			this.firePropertyChange(DESCRIPTION_PROP, null, getDescription());
		}
	}

	public void setDragMoveDelta(Dimension dragMoveDelta) {
		this.dragMoveDelta = dragMoveDelta;
	}

	public void setHandwritten(boolean handwritten) {
		this.handwritten = handwritten;
	}

	public void setInProgress() {
		getStoryCard().setStatus(status);
		this.firePropertyChange(INPROGRESS_PROP, null,
				persister.data.IndexCard.STATUS_IN_PROGRESS);
	}

	public void setLabelEditor(int storycarynamelabel) {
		this.labelEditor = storycarynamelabel;
	}

	public void setLocation(Point newLocation) {
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}

		getStoryCard().setLocationX(newLocation.x);
		getStoryCard().setLocationY(newLocation.y);

		this.updatePriorities();
		this.firePropertyChange(LOCATION_PROP, null, new Point(getStoryCard()
				.getLocationX(), getStoryCard().getLocationY()));
	}

	public void setLocationIncomming(Point newLocation) {
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}
		// ************CODE integrating with Rob's
		if (newLocation.x < 0)
			newLocation.x = 0;
		if (newLocation.y < 0)
			newLocation.y = 0;
		// **********
		getStoryCard().setLocationX(newLocation.x);
		getStoryCard().setLocationY(newLocation.y);
		this.updatePriorities();
		this.firePropertyChange(LOCATION_PROP, null, new Point(getStoryCard()
				.getLocationX(), getStoryCard().getLocationY()));
	}

	public void setLocationIntern(Point newLocation) {
		if (newLocation == null) {
			throw new IllegalArgumentException();
		}

		getStoryCard().setLocationX(newLocation.x);
		getStoryCard().setLocationY(newLocation.y);

		this.firePropertyChange(LOCATION_PROP, null, new Point(getStoryCard()
				.getLocationX(), getStoryCard().getLocationY()));
	}

	public void setNameIncomming(String newName) {
		if (newName != null) {
			getStoryCard().setName(newName);
			this.firePropertyChange(NAME_PROP, null, newName);
		}
	}

	public void setColorChange(String newColor) {
		if (newColor != null) {
			getStoryCard().setColor(newColor);

			try {
				PersisterFactory.getPersister().updateStoryCard(
						this.getStoryCard());
			} catch (NotConnectedException e) {

				util.Logger.singleton().error(e);
			} catch (IndexCardNotFoundException e) {

				util.Logger.singleton().error(e);
			}
		}
	}

	public void setNewParent(IndexCardModel newParent) {
		IndexCardModel oldparent = this.getParent();
		if (oldparent instanceof ContainerModel) {
			ContainerModel oldContainer = (ContainerModel) oldparent;
			oldContainer.removeChild(this);

		}

		this.updatePriorities();
		if (newParent instanceof IterationCardModel) {
			getStoryCard().setParent(
					((IterationCardModel) newParent).getIterationDataObject()
							.getId());
		}

		if (newParent instanceof BacklogModel) {
			getStoryCard().setParent(
					((BacklogModel) newParent).getBacklog().getId());
		}

		this.updateParentValues(this.getParent());
		this.updatePriorities();
		this.updateCardLocation();
	}

	public void setParent(ContainerModel newParent) {
		this.parent = newParent;
		// getDataObject().setParent(newParent.getId());
	}

	public void setParentIncomming(IndexCardModel newParent) {
		setNewParent(newParent);
		this.firePropertyChange(SIZE_PROP, null, this.getSize());

	}

	public void setPriority(int priority) {
		this.priority = priority;
		this.firePropertyChange(LOCATION_PROP, null, new Point(getStoryCard()
				.getLocationX(), getStoryCard().getLocationY()));
	}

	/*
	 * If this function is overridden, the card rotates but dimensions change If
	 * disables, the card rotates but the outer client area does not change to
	 * accomodate the new rotated rectangel public Dimension getSize() {
	 * //return new Dimension(this.storycard.getWidth(),
	 * this.storycard.getHeight()); return new
	 * Dimension(getDataObject().getWidth(), getDataObject().getHeight());
	 *  }
	 */

	public Dimension getRSize() {
		Dimension dim = new Dimension(this.widthClientArea,
				this.heightClientArea);
		return dim;
	}
	
	

	public void setSize(Dimension newSize) {
		if (newSize != null) {
			this.size.setSize(newSize);
			this.setWidth(newSize.width);
			this.setHeight(newSize.height);
			this.firePropertyChange(SIZE_PROP, null, new Dimension(
					getStoryCard().getWidth(), getStoryCard().getHeight()));
		}
	}

	public void setSizeIncomming(Dimension newSize) {
		if (newSize != null) {
			this.size.setSize(newSize);// Allows for resizing of the Cards.
			getStoryCard().setWidth(newSize.width);
			getStoryCard().setHeight(newSize.height);
			this.firePropertyChange(SIZE_PROP, null, this.size);
		}
	}

	public void setStoryCardDataObject(StoryCard storycard) {
		setCardDataObject(storycard);
		this.firePropertyChange(LOCATION_PROP, null, new Point(getStoryCard()
				.getLocationX(), getStoryCard().getLocationY()));
	}

	public void setWorstCaseEstimateIncomming(float newWorstCaseEstimate) {
		getStoryCard().setWorstCaseEstimate(newWorstCaseEstimate);
		this.updateParentValues(this.getParent());
		this.firePropertyChange(WORSTCASE_PROP, null, newWorstCaseEstimate);
	}

	@Override
	public String toString() {
		return "StoryCard " + this.hashCode();
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

	/**
	 * Updating
	 * ****************************************************************************************
	 */
	/** ************************************************************************************************ */
	public void updateParentValues(IndexCardModel modelParent) {
		if (modelParent instanceof IterationCardModel) {
			IterationCardModel p = (IterationCardModel) this.getParent();
			p.updateValuesFromChildren();
		}
	}

	public int getCurrentSideUp() {
		return this.getStoryCard().getCurrentSideUp();
	}

	public void setCurrentSideUp(int currentSideUp) {

		this.getStoryCard().setCurrentSideUp(currentSideUp);
		try {
			PersisterFactory.getPersister()
					.updateStoryCard(this.getStoryCard());

		} catch (NotConnectedException e) {

			util.Logger.singleton().error(e);
		} catch (IndexCardNotFoundException e) {

			util.Logger.singleton().error(e);
		}
		// this.firePropertyChange(COMPLETE_PROP, null, status);
	}

	public void undeleteStoryCard() {
		try {
			PersisterFactory.getPersister().undeleteStoryCard(getDataObject());
		} catch (NotConnectedException e) {

			util.Logger.singleton().error(e);
		} catch (IndexCardNotFoundException e) {

			util.Logger.singleton().error(e);
		} catch (ForbiddenOperationException e) {

			util.Logger.singleton().error(e);
		}
	}

	public void moveStoryCardToNewParent(AbstractRootModel newParent,
			Point newLocation) {

		setLocation(new Point(newLocation.x, newLocation.y));

		if (newParent instanceof ContainerModel) {
			try {
				PersisterFactory.getPersister().moveStoryCardToNewParent(
						getDataObject(), ((ContainerModel) newParent).getId(),
						getDataObject().getLocationX(),
						getDataObject().getLocationY(),0);
			} catch (NotConnectedException e) {

				util.Logger.singleton().error(e);
			} catch (IndexCardNotFoundException e) {

				util.Logger.singleton().error(e);
			}
		} else {
			try {
				PersisterFactory.getPersister().moveStoryCardToNewParent(
						getDataObject(), ((ProjectModel) newParent).getId(),
						getDataObject().getLocationX(),
						getDataObject().getLocationY(),0);
			} catch (NotConnectedException e) {

				util.Logger.singleton().error(e);
			} catch (IndexCardNotFoundException e) {

				util.Logger.singleton().error(e);
			}
		}

	}

	public AcceptanceTestDataObject getAcceptanceTest() {
		String location = "fitnesse://localhost/";
		TestStatisticsProvider provider = TestStatisticsProvider
				.getTestStatisticsProvider(location, this);

		AcceptanceTestDataObject data = new AcceptanceTestDataObject();

		data.setNumFail(provider.getNumFail());
		data.setNumPass(provider.getNumPass());
		data.setNumRuns(provider.getNumRuns());
		data.setWiki(provider.getWikiText());
		return data;
	}

	private void updatePriorities() {
		if (this.getParent() != null) {
			this.getParent().updateChildPriorities();
			List<StoryCardModel> childrenList = this.getParent()
					.getChildrenList();
			if (childrenList == null)
				return;
			for (StoryCardModel child : childrenList) {
				child.firePropertyChange(LOCATION_PROP, null, new Point(
						getStoryCard().getLocationX(), getStoryCard()
								.getLocationY()));
			}
		}
	}

	public String getColor() {

		return getStoryCard().getColor();
	}



	public void setTestTextIncoming(String text) {

		if (text != null) {
			getStoryCard().setAcceptanceTestText(text);
			this.firePropertyChange(COMPLETE_PROP, null, status);
		}
	}

	public void setTestText(String text) {
		if (text != null) {
			getStoryCard().setAcceptanceTestText(text);
			this.firePropertyChange(COMPLETE_PROP, null, status);
			try {
				PersisterFactory.getPersister().updateStoryCard(
						this.getStoryCard());
			} catch (NotConnectedException e) {

				util.Logger.singleton().error(e);
			} catch (IndexCardNotFoundException e) {

				util.Logger.singleton().error(e);
			}
		}
	}

	public ProjectModel getParentProject() {
		return parentProject;
	}

	public String getStoryCardFont() {
		return storyCardFont;
	}

	public void setStoryCardFont(String storyCardFont) {
		this.storyCardFont = storyCardFont;
	}

	public int getStoryCardFontSize() {
		return storyCardFontSize;
	}

	public void setStoryCardFontSize(int storyCardSize) {
		this.storyCardFontSize = storyCardSize;
	}

	public static String getStoryCardStatus() {
		return storyCardStatusPre;
	}

	public static void setStoryCardStatus(String storyCardStatus) {
		StoryCardModel.storyCardStatusPre = storyCardStatus;
	}

	public Dimension getBestFitClientArea(Dimension cardDimension) {
		// get the best fit clientarea dimension of storycard
		double beta1, beta2, aa, bb, cc;
		Dimension result = new Dimension(0, 0);
		aa = cardDimension.width * 1.0 / 2;
		bb = cardDimension.height * 1.0 / 2;
		cc = Math.sqrt(aa * aa + bb * bb);

		if ((this.aRotate >= 0 && this.aRotate <= 90)
				|| (this.aRotate > 180 && this.aRotate <= 270)
				|| (this.aRotate < -90 && this.aRotate >= -180)
				|| (this.aRotate < -270)) {
			beta1 = Math.atan(bb / aa) + aRotate * Math.PI / 180;
			beta2 = aRotate * Math.PI / 180 - Math.atan(bb / aa);
		} else {
			beta1 = Math.atan(bb / aa) - aRotate * Math.PI / 180;
			beta2 = aRotate * Math.PI / 180 + Math.atan(bb / aa);
		}
		result.height = Math
				.abs(Math.round((float) (cc * Math.sin(beta1) * 2)));
		result.width = Math.abs(Math.round((float) (cc * Math.cos(beta2) * 2)));

		return result;
	}

	public float getARotate() {
		return aRotate;
	}

	public void setARotate(float rotate) {
		aRotate = rotate;
	}

	public int getHeightClientArea() {
		return heightClientArea;
	}

	public void setHeightClientArea(int heightClientArea) {
		this.heightClientArea = heightClientArea;
	}

	public int getWidthClientArea() {
		return widthClientArea;
	}

	public void setWidthClientArea(int widthClientArea) {
		this.widthClientArea = widthClientArea;
	}

	public void updateRotateAngle(float aRotate) {
		setRotationCommandExecuted (true);
		//to avoid big rotation angles
		if(Math.abs(aRotate) >= 90) aRotate = 1;
		//determines how smooth the rotation is
		float frictionFactor = (float) 0.45;
		aRotate /= frictionFactor ;
		this.aRotate += (aRotate);
		while (this.aRotate >= 360)
			this.aRotate -= 360;
		
		while (this.aRotate < -360)
			this.aRotate += 360;
	
		this.widthClientArea = getBestFitClientArea(this.size).width;
		this.heightClientArea = getBestFitClientArea(this.size).height;
		this.firePropertyChange(LOCATION_PROP, null, new Point(this.getStoryCard()
				.getLocationX(), this.getStoryCard().getLocationY()));
		
	}
	
	public boolean isRotationCommandExecuted () {
		return this.rotationCommandExecuted;
	}
	
	public void setRotationCommandExecuted(boolean rotationCommandExecuted) {
		this.rotationCommandExecuted = rotationCommandExecuted;
	}

	public String getCardOwner() {
		return this.getStoryCard().getCardOwner();
	}

	public String getFitId() {
		return this.getStoryCard().getFitId();
	}

	public void setFitId(String fitId) {
		this.getStoryCard().setFitId(fitId);
	}

	public static Class getChildClassOfColor(String color){

//		if(color.equals("grey") || color.equals("gray")){
//			return StoryCardModelGrey.class;
//		}
		try {
			return Class.forName("cards.model.StoryCardModel"+util.Support.capitalize(color));
		} catch (ClassNotFoundException e) {
			util.Logger.singleton().error(e);
			return null;
		}
	}
	
}

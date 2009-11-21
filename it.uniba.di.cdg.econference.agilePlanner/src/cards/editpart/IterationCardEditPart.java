/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the controler object for the IterationCard Object
 *								This is where data is passed from the figure to the model. 
 *								There is a DragTracker in this class that is used for listening for mouse events. 
 *								UI elements that are part of the Iteration Card Object are managed here and not in the figure. 
 *
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.IndexCard;
import persister.data.StoryCard;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.celleditorlocator.IterationCardAvailableEffortCellEditorLocation;
import cards.celleditorlocator.IterationCardDescriptionLabelCellEditorLocation;
import cards.celleditorlocator.IterationCardEndDateLabelCellEditorLocation;
import cards.celleditorlocator.IterationCardNameLabelCellEditorLocation;
import cards.celleditorlocator.IterationCardStartDateLabelCellEditorLocation;
import cards.editmanager.IterationCardEditManager;
import cards.editpolicy.IterationCardComponentEditPolicy;
import cards.editpolicy.IterationDirectEditPolicy;
import cards.figure.IterationCardFigure;
import cards.layoutpolicy.IterationLayoutPolicy;
import cards.model.AbstractRootModel;
import cards.model.IndexCardModel;
import cards.model.IterationCardModel;
import cards.model.StoryCardModel;
import fitintegration.PluginInformation;

public class IterationCardEditPart extends AbstractGraphicalEditPart implements EditPart, PropertyChangeListener,KeyListener {
    private Label availableEffortLabel = new Label("0");

    /**
     * UI elements that are part of the Iteration Card Object
     */
   // private Label completedButton = new Label();

    private Label descLabel = new Label("");

    private Label endDateLabel = new Label("");
    
    private Label startDateLabel = new Label("");

    private Label hiddenCardsNotifier = new Label();

    private Label nameLabel = new Label("");

    private Label remainingEffortLabel = new Label("0");
    
    private int tabNumber=0;

    /**
     * Runnable object to allow for updating of the figure when the model is
     * updated outside of the UI thread.
     */
    public final Runnable refresher = new Runnable() {
        public void run() {
            IterationCardEditPart.this.refreshVisuals();
            IterationCardEditPart.this.refreshChildren();
        }
    };

    private IFigure createFigureForModel() {
        if (this.getModel() instanceof IterationCardModel) {
            return new IterationCardFigure();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * This method return true if color is red false if black if the iterations
     * available effort - iterations mostlikely effort is less then zero. this
     * should not be confused with the calculation of the remaining effort which
     * is: iteration mostlikely - iteration actuall.
     */
    private boolean remainingColor() {
        float available, mostlikely;
        available = Float.valueOf(this.getCastedModel().getAvailableEffort());
        mostlikely = Float.valueOf(this.getCastedModel().getMostLikelyEstimate());
        if (available - mostlikely < 0.0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * All required edit policies are added here
     */
    @Override
    protected void createEditPolicies() {
        this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new IterationCardComponentEditPolicy());
        this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new IterationLayoutPolicy()); // TableXYLayoutEditPolicy());
        this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new IterationDirectEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        IFigure f = this.createFigureForModel();
        return f;
    }

    protected void performDirectEdit(Point location) {
        IterationCardFigure fig = (IterationCardFigure) this.getFigure();
        fig.translateToRelative(location);
        IFigure directEditFigure = fig.findFigureAt(location);
        if ((directEditFigure != null) && (directEditFigure instanceof Label)) {
            if (this.nameLabel.containsPoint(location)) {
                this.getCastedModel().setLabelEditor(CardConstants.ITERATIONCARDNAMELABEL);
                new IterationCardEditManager(this, CardConstants.ITERATIONCARDNAMELABEL, new IterationCardNameLabelCellEditorLocation(
                        (IterationCardFigure) this.getFigure(),this)).show();
               
            }
            else
                if (this.descLabel.containsPoint(location)) {
                    this.getCastedModel().setLabelEditor(CardConstants.ITERATIONCARDDESCRIPTIONLABEL);
                    new IterationCardEditManager(this, CardConstants.ITERATIONCARDDESCRIPTIONLABEL,
                            new IterationCardDescriptionLabelCellEditorLocation((IterationCardFigure) this.getFigure(),this)).show();
                }
                else
                    if (this.endDateLabel.containsPoint(location)) {
                        this.getCastedModel().setLabelEditor(CardConstants.ITERATIONCARDENDDATELABEL);
                        new IterationCardEditManager(this, CardConstants.ITERATIONCARDENDDATELABEL, new IterationCardEndDateLabelCellEditorLocation(
                                (IterationCardFigure) this.getFigure(),this)).show();
                    }
                    else
                        if (this.startDateLabel.containsPoint(location)) {
                            this.getCastedModel().setLabelEditor(CardConstants.ITERATIONCARDSTARTDATELABEL);
                            new IterationCardEditManager(this, CardConstants.ITERATIONCARDSTARTDATELABEL, new IterationCardStartDateLabelCellEditorLocation(
                                    (IterationCardFigure) this.getFigure(),this)).show();
                        }
            
           
                    else
                        if (this.availableEffortLabel.containsPoint(location)) {
                            this.getCastedModel().setLabelEditor(CardConstants.ITERATIONCARDAVAILABLEEFFORT);
                            new IterationCardEditManager(this, CardConstants.ITERATIONCARDAVAILABLEEFFORT,
                                    new IterationCardAvailableEffortCellEditorLocation((IterationCardFigure) this.getFigure(),this)).show();
                        }
                      //  else
//                            if (this.completedButton.containsPoint(location)) {
//                                if (!this.getCastedModel().isComplete()
//                                        && MessageDialog
//                                                .openQuestion(null, "Complete Iteration",
//                                                        "Are you sure that you want to set this iteration and all of its contents as complete? \nThis change can not be undone!")) {
//                                    // this.getCastedModel().setStatus(IndexCard.STATUS_COMPLETED);
//                                    this.getCastedModel().getIterationDataObject().setStatus(IndexCard.STATUS_COMPLETED);
//
//                                    if (!(this.getCastedModel().getIterationDataObject().getStoryCardChildren().isEmpty())) {
//                                        for (StoryCard storycard : this.getCastedModel().getIterationDataObject().getStoryCardChildren()) {
//                                            storycard.setStatus(IndexCard.STATUS_COMPLETED);
//                                        }
//                                    }
//                                    try {
//                                        PersisterFactory.getPersister().updateIteration(this.getCastedModel().getIterationDataObject());
//                                    }
//                                    catch (NotConnectedException e) {
//                                        // TODO Auto-generated catch block
//                                        util.Logger.singleton().error(e);
//                                    }
//                                    catch (IndexCardNotFoundException e) {
//                                        // TODO Auto-generated catch block
//                                        util.Logger.singleton().error(e);
//                                    }
//
//                                }
//                            }
        }
    }

    @Override
    public void activate() {
        if (!this.isActive()) {
            super.activate();
            ((AbstractRootModel) this.getModel()).addPropertyChangeListener(this);
        }
    }

    /** ******************************************************************************** */
    /** ******************************************************************************** */
    public void addChild(StoryCardEditPart child) {
        this.getCastedModel().updateChild((StoryCardModel) child.getModel());
        super.addChild(child, -1);
    }

    @Override
    public void deactivate() {
        if (this.isActive()) {
            super.deactivate();
            ((AbstractRootModel) this.getModel()).removePropertyChangeListener(this);

//            try{
//            	this.completedButton.getIcon().dispose();
//            }catch(Exception e){}
            try{
            	this.hiddenCardsNotifier.getIcon().dispose();
            }catch(Exception e){}
        }
        
        
    }

    /** ******************************************************************************** */
    /** ******************************************************************************** */
    public Label getAvailableEffortLabel() {
        return this.availableEffortLabel;
    }

    public IterationCardModel getCastedModel() {
        return (IterationCardModel) this.getModel();
    }

    public Label getDescLabel() {
        return this.descLabel;
    }

    /** DragTracker*********************************************************************** */
    /** ******************************************************************************** */
    /**
     * This is the mouse listener object. use this to trigger events based on
     * mouse interactions.
     */
    @Override
    public DragTracker getDragTracker(Request req) {
        return new DragEditPartsTracker(this) {

            private void updateDelta(Dimension d) {
                for (Object o : super.getOperationSet()) {
                    if (o instanceof IterationCardEditPart) {
                        ((IterationCardEditPart) o).getCastedModel().setDragMoveDelta(d);
                    }
                    
                }
            }

            @Override
            protected boolean handleButtonDown(int Button) {
                boolean returnVal = super.handleButtonDown(Button);
                return returnVal;
            }

            @Override
            protected boolean handleButtonUp(int Button) {
                boolean returnVal = false;

                returnVal = super.handleButtonUp(Button);
                return returnVal;
            }

            @Override
            protected boolean handleDoubleClick(int Button) {
                return false;
            }

            @Override
            protected boolean handleDragInProgress() {
                // TODO Auto-generated method stub
                // return super.handleDragInProgress();
                boolean returnVal = super.handleDragInProgress();
                this.updateDelta(this.getDragMoveDelta());
                return returnVal;
            }

            public void updateCardLocation() {
                Point oldLocation = new Point();
                Point newLocation = new Point();

                Dimension pointChange = new Dimension();
                IterationCardModel model = IterationCardEditPart.this.getCastedModel();

                oldLocation = model.getLocation();
                pointChange = this.getDragMoveDelta();
                newLocation.x = oldLocation.x + pointChange.width;
                newLocation.y = oldLocation.y + pointChange.height;
                model.setLocation(newLocation);

            }
        };
    }

    public Label getEndDateLabel() {
        return this.endDateLabel;
    }

    @Override
    public List getModelChildren() {
        List<IndexCardModel> storyChildren = new ArrayList<IndexCardModel>(); 
        	storyChildren.addAll( this.getCastedModel().getChildrenList());
        	storyChildren.addAll(getCastedModel().getProjectModel().getRemoteMice());
        	Collections.sort(storyChildren);
        if (storyChildren.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        else {
            return Collections.unmodifiableList(storyChildren);
        }
    }

    public Label getNameLabel() {
        return this.nameLabel;
    }

    public Label getRemainingEffortLabel() {
        return this.remainingEffortLabel;
    }

    @Override
    public void performRequest(Request request) {
        if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            this.performDirectEdit(((DirectEditRequest) request).getLocation().getCopy());
        }
    }

    /**
     * Any property that can be changed should be listed here so that an update
     * of teh figure can occure when the property change is fired.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (IterationCardModel.SIZE_PROP.equals(prop) || IterationCardModel.LOCATION_PROP.equals(prop) || IterationCardModel.NAME_PROP.equals(prop)
                || IterationCardModel.DESCRIPTION_PROP.equals(prop) || IterationCardModel.CHILD_ADDED_PROP.equals(prop)
                || IterationCardModel.CHILD_REMOVED_PROP.equals(prop) || IterationCardModel.END_DATE_PROP.equals(prop)
                || IterationCardModel.AVAILABLE_EFFORT_PROP.equals(prop) || IterationCardModel.COMPLETE_PROP.equals(prop)) {

            try {
                Display d = this.getViewer().getControl().getDisplay();
                d.syncExec(this.refresher);
            }
            catch (Exception e) {
            }
        }

        if (IterationCardModel.SEARCH_PROP.equals(prop)) {
            this.getViewer().reveal(this);
            this.refreshVisuals();
        }
    }

 
    public void refreshChildren() {    	
        super.refreshChildren();        
    }

    /**
     * Does all the preparation work in updating the UI elements with data from
     * the model. This is where UI elements are added to the object to be drawn
     * to the figure.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void refreshVisuals() {
        IterationCardFigure figure = (IterationCardFigure) this.getFigure();

        Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(), this.getCastedModel().getSize());
        
        ((GraphicalEditPart) this.getParent()).setLayoutConstraint(this, this.getFigure(), bounds);
        figure.setBounds(bounds);
        this.nameLabel.setText("     ");
        this.nameLabel.setLocation(new Point(figure.getClientArea().x
                + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureNamePoint
                        : CardConstants.WindowsIterationFigureNamePoint), figure.getClientArea().y + 5));
        this.nameLabel
                .setSize(
                        (200 -((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureNamePoint
                                : CardConstants.WindowsIterationFigureNamePoint)), 15);
        this.nameLabel.setLabelAlignment(PositionConstants.LEFT);
        this.nameLabel.setOpaque(false);
        
      
        
        figure.add(this.nameLabel);

        this.endDateLabel.setText("     ");
        this.endDateLabel.setLocation(new Point(figure.getClientArea().x
                + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureEndDatePoint
                        : CardConstants.WindowsIterationFigureEndDatePoint), figure.getClientArea().y + 5));
        this.endDateLabel.setSize(((figure.getClientArea().width) - CardConstants.WindowsIterationFigureEndDatePoint), 15);
        this.endDateLabel.setLabelAlignment(PositionConstants.LEFT);
        this.endDateLabel.setOpaque(false);
        figure.add(this.endDateLabel);
        
        this.startDateLabel.setText("     ");
        this.startDateLabel.setLocation(new Point(figure.getClientArea().x
                + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureStartDatePoint
                        : CardConstants.WindowsIterationFigureStartDatePoint), figure.getClientArea().y + 20));
        this.startDateLabel.setSize(((figure.getClientArea().width) - CardConstants.WindowsIterationFigureStartDatePoint), 15);
        this.startDateLabel.setLabelAlignment(PositionConstants.LEFT);
        this.startDateLabel.setOpaque(false);
        figure.add(this.startDateLabel);
        
        

        this.availableEffortLabel.setText("        ");
        this.availableEffortLabel.setLocation(new Point(figure.getClientArea().x
                + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureAvailablePoint
                        : CardConstants.WindowsIterationFigureAvailablePoint), figure.getClientArea().y + 35));
        this.availableEffortLabel
                .setSize(
                        ((figure.getClientArea().width / 2) - ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureAvailablePoint
                                : CardConstants.WindowsIterationFigureAvailablePoint)), 15);
        this.availableEffortLabel.setLabelAlignment(PositionConstants.LEFT);
        this.nameLabel.setOpaque(false);
        figure.add(this.availableEffortLabel);

        this.descLabel.setText("       ");
        this.descLabel.setLocation(new Point(figure.getClientArea().x
                + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureNamePoint
                        : CardConstants.WindowsIterationFigureNamePoint), figure.getClientArea().y + 20));
        this.descLabel.
        setSize(
                (200 -((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureNamePoint
                        : CardConstants.WindowsIterationFigureNamePoint)), 15);
        this.descLabel.setLabelAlignment(PositionConstants.LEFT);
        this.nameLabel.setOpaque(false);
        figure.add(this.descLabel);

        if (this.remainingColor()) {
            this.remainingEffortLabel.setForegroundColor(new Color(null, 150, 1, 1));
        }
        else {
            this.remainingEffortLabel.setForegroundColor(new Color(null, 1, 1, 1));
        }
        this.remainingEffortLabel.setText("       ");
        this.remainingEffortLabel.setLocation(new Point(figure.getClientArea().x
                + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureRemainingPoint
                        : CardConstants.WindowsIterationFigureRemainingPoint), figure.getClientArea().y + 35));
        this.remainingEffortLabel
                .setSize(
                        ((figure.getClientArea().width) - ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFigureRemainingPoint
                                : CardConstants.WindowsIterationFigureRemainingPoint)), 15);
        this.remainingEffortLabel.setLabelAlignment(PositionConstants.LEFT);
        figure.add(this.remainingEffortLabel);

        figure.setActText(this.getCastedModel().getActualEffort());
        figure.setBCText(this.getCastedModel().getBestCaseEstimate());
        figure.setWCText(this.getCastedModel().getWorstCaseEstimate());
        figure.setMLText(this.getCastedModel().getMostLikelyEstimate());

        figure.setNameField(this.getCastedModel().getName());
        figure.setDescField(this.getCastedModel().getDescription());
        figure.setRemainingEffortField(this.getCastedModel().getRemainingEffort());
        figure.setEndDateField(this.getCastedModel().getIterationEndDate());
        figure.setStartDateField(this.getCastedModel().getStartDate());
        figure.setAvailableEffortField(this.getCastedModel().getAvailableEffort());

        ImageDescriptor id ;
        //        if(this.completedButton.getIcon() != null)
//        	this.completedButton.getIcon().dispose();
//        this.completedButton.setIcon(id.createImage());
//       // this.completedButton.getIcon().dispose();
//        this.completedButton.setLocation(new Point(figure.getClientArea().x + figure.getClientArea().width - 22, figure.getClientArea().y
//                + figure.getClientArea().height - 22));
//        this.completedButton.setSize(new Dimension(20, 20));
//
//        if (this.getCastedModel().getStatus().equals(IndexCard.STATUS_COMPLETED)) {
//            id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.CompleteStoryAndIterationButtonChecked);
//            if(this.completedButton.getIcon() != null)
//            	this.completedButton.getIcon().dispose();
//            this.completedButton.setIcon(id.createImage());
//        }
//        figure.add(this.completedButton);

        id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.HiddenStoryCardButton);
        if(this.hiddenCardsNotifier.getIcon() != null)
        	this.hiddenCardsNotifier.getIcon().dispose();
        this.hiddenCardsNotifier.setIcon(id.createImage());
        this.hiddenCardsNotifier.setLocation(new Point(figure.getClientArea().x + 5, figure.getClientArea().y + figure.getClientArea().height - 22));
        this.hiddenCardsNotifier.setSize(20, 20);
        figure.add(this.hiddenCardsNotifier);

        if (!this.getCastedModel().hasHiddenChildren()) {
            figure.remove(this.hiddenCardsNotifier);
        }

        figure.repaint();
    }

    public void removeChild(StoryCardEditPart child) {
        this.getCastedModel().removeChild((StoryCardModel) child.getModel());
        super.removeChild(child);
    }

    public void setAvailableEffortLabel(Label availableEffortLabel) {
        this.availableEffortLabel = availableEffortLabel;
    }

    public void setDescLabel(Label descLabel) {
        this.descLabel = descLabel;
    }

    public void setEndDateLabel(Label endDateLabel) {
        this.endDateLabel = endDateLabel;
    }

    public void setNameLabel(Label nameLabel) {
        this.nameLabel = nameLabel;
    }

    public void setRemainingEffortLabel(Label remainingEffortLabel) {
        this.remainingEffortLabel = remainingEffortLabel;
    }

	public Label getStartDateLabel() {
		return startDateLabel;
	}

	public void setStartDateLabel(Label startDateLabel) {
		this.startDateLabel = startDateLabel;
	}

	public void keyPressed(org.eclipse.draw2d.KeyEvent ke) {
		if(ke.character=='a'){
		}
	}

	public void keyReleased(org.eclipse.draw2d.KeyEvent ke) {
		if(ke.character=='a'){
		}
	}
	
}

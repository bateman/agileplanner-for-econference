/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the controler for the StoryCard object. 
 *								This class manages the communication between the model and the figure. 
 *								There is a DragTracker that handels mouse events. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editpart;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.IndexCard;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.distributed.CallbackPropagator;
import persister.factory.PersisterFactory;
import persister.factory.Settings;
import persister.local.PersisterToXML;


import cards.CardConstants;
import cards.celleditorlocator.StoryCardActualLabelCellEditorLocation;
import cards.celleditorlocator.StoryCardBestCaseLabelCellEditorLocation;
import cards.celleditorlocator.StoryCardDescriptionLabelCellEditorLocation;
import cards.celleditorlocator.StoryCardMostLikeylCellEditorLocation;
import cards.celleditorlocator.StoryCardNameLabelCellEditorLocation;
import cards.celleditorlocator.StoryCardOwnerLabelCellEditorLocation;
import cards.celleditorlocator.StoryCardUrlCellEditorLocation;
import cards.celleditorlocator.StoryCardWorstCaseCellEditorLocation;
import cards.commands.CollapseExpandStoryCardCommand;
import cards.commands.StoryCardFlipCommand;
import cards.commands.StoryCardUpdateSizeLocationCommand;
import cards.editmanager.StoryCardDescriptionEditManager;
import cards.editmanager.StoryCardEditManager;
import cards.editpolicy.StoryCardComponentEditPolicy;
import cards.editpolicy.StoryCardDirectEditPolicy;
import cards.figure.StoryCardFigure;
import cards.figure.WrapLabel;
import cards.model.AbstractRootModel;
import cards.model.IndexCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;
import cards.model.StoryCardModelTransparent;
import fitintegration.PluginInformation;

public class StoryCardEditPart extends AbstractGraphicalEditPart implements
		PropertyChangeListener {

	@SuppressWarnings("unused")
	private AccessibleEditPart acc;
	
	

	private int visibleEstimates;
	private Label actualLabel = new Label("");

	private Label bestCaseLabel = new Label("");

	private WrapLabel descriptionLabel = new WrapLabel();

	private Label mostLikelyLabel = new Label("");

	protected Label nameLabel = new Label("");

	private Label ownerLabel = new Label("");
	
	private Label remainingLabel = new Label("");

	private Label statusButton = new Label();
	
	private Label statusBar = new Label();

	protected Label triangleButton = new Label();

	private Label worstCaseLabel = new Label("");
	
	private Combo combo;
	
	private String selection;
	
	private Shell shell=null;

	// back side

	private Label urlLabel = new Label("");

	private Label addPrototypeButton = new Label("");

	private String storyCardColor;

	private String storyCardFont;

	private int storyCardFontSize;
	
	private StoryCardEditPart editpart;

	private Font scFont;
	
	private Label handWriting = new Label("");

	public StoryCardEditPart() {

		this.editpart=this;
	}

	/**
	 * Runnabel object to allow for udating of the figure when the model is
	 * updated outside of the UI thread
	 */
	public final Runnable refresher = new Runnable() {
		public void run() {
			StoryCardEditPart.this.refreshVisuals();
		}
	};

	protected float rotationAngle = 0;

	int currentLabelIndex = 0;

	// where on the client's machine do you download the prototypes?
	private String prototypesFolderPath = "C:\\";

	private IFigure createFigureForModel() {
		if (this.getModel() instanceof StoryCardModel) {
			
			StoryCardFigure storyCardFigure = PluginInformation.getFactoryForPlugin().getNewStoryCardFigure();
			storyCardFigure.setStoryCardColor((((StoryCardModel) getModel())
					.getStoryCard().getColor()));
			return storyCardFigure;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new StoryCardDirectEditPolicy());
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new StoryCardComponentEditPolicy());
	}

	@Override
	protected IFigure createFigure() {
		IFigure f = this.createFigureForModel();
		return f;
	}

	protected StoryCardFigure getStoryCardFigure() {
		return (StoryCardFigure) this.getFigure();
	}

	protected void performDirectEdit(Point location) {

		StoryCardFigure fig = (StoryCardFigure) this.getFigure();
		fig.translateToRelative(location);
		IFigure directEditFigure = fig.findFigureAt(location);

		System.out.println("Casted Figure: "
				+ this.getCastedModel().getCurrentSideUp());
		if (this.getCastedModel().getCurrentSideUp() == StoryCard.FRONT_SIDE) {
			if ((directEditFigure != null)
					&& (directEditFigure instanceof Label)) {

				if (this.nameLabel.containsPoint(location)) {
					this.getCastedModel().setLabelEditor(
							CardConstants.STORYCARDNAMELABEL);
					new StoryCardEditManager(this,
							CardConstants.STORYCARDNAMELABEL,
							new StoryCardNameLabelCellEditorLocation(
									(StoryCardFigure) this.getFigure(), this
											.getCastedModel(), this)).show();

				}

				else if (this.descriptionLabel.containsPoint(location)) {
					if (!this.getCastedModel().isHandwritten()) {
						this.getCastedModel().setLabelEditor(
								CardConstants.STORYCARDDESCRIPTIONLABEL);
						new StoryCardDescriptionEditManager(
								this,
								CardConstants.STORYCARDDESCRIPTIONLABEL,
								new StoryCardDescriptionLabelCellEditorLocation(
										(StoryCardFigure) this
												.getStoryCardFigure(), this
												.getCastedModel(), this))
								.show();
					} else {
						try {
							/**
							 * Section to handle the handwrittern cards. the
							 * handwritten editor is only avaliabe on WIndows
							 * OS. This section will try and launch the editor
							 * if the card is a handwritter card only.
							 */
							if (System.getProperty("os.name").equalsIgnoreCase(
									"Mac OS X")) {
								MessageDialog
										.openInformation(
												null,
												"Handwritten Card Editor",
												"We regret that the handwritten editor is not available for the Mac OS at this time");
							} else {
								String filelocation = new File(".")
										.getCanonicalPath();
							}
						} catch (IOException e) {
							MessageDialog
									.openInformation(
											null,
											"Handwritten Card Editor",
											"We regret that the handwritten editor is not available for your OS at this time");
						}
					}
				} else if (this.actualLabel.containsPoint(location)) {
					this.getCastedModel().setLabelEditor(
							CardConstants.STORYCARDACTUALLABEL);
					new StoryCardEditManager(this,
							CardConstants.STORYCARDACTUALLABEL,
							new StoryCardActualLabelCellEditorLocation(this
									.getCastedModel(), this)).show();

				} else if (this.ownerLabel.containsPoint(location)) {
					
					shell = new Shell();
					combo = new Combo (shell, SWT.READ_ONLY);
					ImageDescriptor des=null;
					
					des=AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID()
								,CardConstants.getColorCardsIconLocation(this.getCastedModel().getColor()));
					
					ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) this.getRoot();
					double ratio = root.getZoomManager().getZoom();
					
					int xRelative = (int) ((this.getOwnerLabel().getLocation().x - this.getFigure().getBounds().x)*ratio);
			        int yRelative = (int) ((this.getOwnerLabel().getLocation().y - this.getFigure().getBounds().y)*ratio);
			        Rectangle rect = this.getFigure().getClientArea();
			        List<TeamMember> list = ProjectModel.getCurrentProject().getProjectDataObject().getTeamMembers();
			        String[] names = new String[list.size()+1];
					
					for(int i=0;i<list.size()+1;i++){
						if(i==0)
							names[i]="Unassigned";
						else{
							String temp=(list.get(i-1)).getName();
							names[i]=temp;
						}
					}
			         
					
					combo.setSize(120,200);
					combo.setItems(names);
					combo.select(0);
					shell.setImage(des.createImage());
					shell.setLocation((rect.x +this.getCastedModel().getWidth()+15), (rect.y + yRelative+55));
					shell.pack();
					shell.open();
				
					
					selection = combo.getItem(combo.getSelectionIndex());
					combo.addSelectionListener(new SelectionListener(){
						public void widgetDefaultSelected(SelectionEvent e)
						{
							
						}
						public void widgetSelected(SelectionEvent e){
							selection = combo.getItem(combo.getSelectionIndex());
							editpart.getCastedModel().getStoryCard().setCardOwner(selection);
							StoryCardUpdateSizeLocationCommand sc = new StoryCardUpdateSizeLocationCommand(editpart.getCastedModel(),new Rectangle(editpart.getCastedModel().getLocation(),new Dimension(editpart.getCastedModel().getWidth(),editpart.getCastedModel().getHeight())));
						    sc.execute();
						    shell.close();
						   
						}
					});
				} else if (this.bestCaseLabel.containsPoint(location)) {
					this.getCastedModel().setLabelEditor(
							CardConstants.STORYCARDBESTCASELABEL);
					new StoryCardEditManager(this,
							CardConstants.STORYCARDBESTCASELABEL,
							new StoryCardBestCaseLabelCellEditorLocation(this
									.getCastedModel(), this)).show();
				} else if (this.mostLikelyLabel.containsPoint(location)) {
					this.getCastedModel().setLabelEditor(
							CardConstants.STORYCARDMOSTLIKELYLABEL);
					new StoryCardEditManager(this,
							CardConstants.STORYCARDMOSTLIKELYLABEL,
							new StoryCardMostLikeylCellEditorLocation(this
									.getCastedModel(), this)).show();
				} else if (this.worstCaseLabel.containsPoint(location)) {
					this.getCastedModel().setLabelEditor(
							CardConstants.STORYCARDWORSTCASELABEL);
					new StoryCardEditManager(this,
							CardConstants.STORYCARDWORSTCASELABEL,
							new StoryCardWorstCaseCellEditorLocation(this
									.getCastedModel(), this)).show();
				} else if (this.remainingLabel.containsPoint(location)) {
				} else if (this.statusButton.containsPoint(location)) {

					if (((this.getCastedModel().getStatus())
							.equals(IndexCard.STATUS_COMPLETED)))
						this.getCastedModel().setStatus(
								IndexCard.STATUS_ACCEPTED);

					else if (((this.getCastedModel().getStatus())
							.equals(IndexCard.STATUS_IN_PROGRESS)))
						this.getCastedModel().setStatus(
								IndexCard.STATUS_COMPLETED);

					else if (((this.getCastedModel().getStatus())
							.equals(IndexCard.STATUS_DEFINED)))
						this.getCastedModel().setStatus(
								IndexCard.STATUS_IN_PROGRESS);

					else if (((this.getCastedModel().getStatus())
							.equals(IndexCard.STATUS_ACCEPTED)))
						this.getCastedModel().setStatus(
								IndexCard.STATUS_DEFINED);

					try {
						PersisterFactory.getPersister().updateStoryCard(
								this.getCastedModel().getStoryCard());
					} catch (NotConnectedException e) {
						util.Logger.singleton().error(e);
					} catch (IndexCardNotFoundException e) {
						util.Logger.singleton().error(e);
					}
				} else checkForAdditionalFrontSideEdits(location);

			}
		}
		else if (this.getCastedModel().getCurrentSideUp() == StoryCard.RICH_TEST_SIDE) {

			if (this.urlLabel.containsPoint(location)) {

				this.getCastedModel().setLabelEditor(
						CardConstants.STORYCARDURLLABEL);
				new StoryCardEditManager(this, CardConstants.STORYCARDURLLABEL,
						new StoryCardUrlCellEditorLocation(this
								.getCastedModel(), this)).show();
			}
		} else if (this.getCastedModel().getCurrentSideUp() == StoryCard.PROTOTYPE_SIDE) {
			if (this.triangleButton.containsPoint(location)) {

				StoryCardFlipCommand commend = new StoryCardFlipCommand(
						StoryCardEditPart.this.getCastedModel());

				commend.execute();
			}
		}else if (this.getCastedModel().getCurrentSideUp() == StoryCard.HANDWRITING_SIDE) {
			// System.out.println("PROTOTYPE TEST SIDE");
			if (this.triangleButton.containsPoint(location)) {

				StoryCardFlipCommand commend = new StoryCardFlipCommand(
						StoryCardEditPart.this.getCastedModel());

				commend.execute();
			}
		} else checkAdditionalSideLocations(location);

	}

	protected void checkForAdditionalFrontSideEdits(Point location) {
	}

	/**
	 * This method can be overwritten by Subclasses. In this case its intended to facilitate other Plugins
	 * to add functionality.
	 * 
	 * @param location
	 */
	protected void checkAdditionalSideLocations(Point location) {
	}

	private void downloadAndOpen(String fileName) {
		try {
			PersisterFactory.getPersister().downloadFile(fileName,
					(int) this.getCastedModel().getStoryCard().getId());
		} catch (NotConnectedException e1) {
			util.Logger.singleton().error(e1);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			util.Logger.singleton().error(e1);
		}
		File downloadedFile = CallbackPropagator.getCurrentDownloadedFile();
		String filePath = downloadedFile.getPath() + File.separatorChar
				+ fileName;
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("cmd /c   " + "\"" + filePath + "\"");
		} catch (IOException e) {
			util.Logger.singleton().error(e);
		}

	}

	private boolean uploadPrototypeFile(String fullPath, long id) {
		try {
			PersisterFactory.getPersister().uploadFile(fullPath, (int) id);
		} catch (NotConnectedException e) {
			util.Logger.singleton().error(e);
		}
		return true;
	}
	@Override
	public void activate() {
		if (!this.isActive()) {
			super.activate();
			((AbstractRootModel) this.getModel())
					.addPropertyChangeListener(this);
		}
	}

	@Override
	public void deactivate() {
		if (this.isActive()) {
			super.deactivate();
			((AbstractRootModel) this.getModel())
					.removePropertyChangeListener(this);

			// remove image handles.
			try {
				this.statusButton.getIcon().dispose();
				this.statusBar.getIcon().dispose();
				
			} catch (Exception e) {
			}
			try {
				scFont.dispose();
			} catch (Exception e) {
			}
			try {
				((StoryCardFigure) (this.getFigure())).dispose();
			} catch (Exception e) {
			}

		}
	}

	public Label getActualLabel() {
		return this.actualLabel;
	}

	public Label getBestCaseLabel() {
		return this.bestCaseLabel;
	}

	public StoryCardModel getCastedModel() {
		return (StoryCardModel) this.getModel();
	}

	public Label getDescriptionLabel() {
		return this.descriptionLabel;
	}

	/**
	 * Mouse Event listeners should be added here
	 */
	@Override
	public DragTracker getDragTracker(Request req) {

		return new DragEditPartsTracker(this) {

			Point oldMousePosition = this.getCurrentInput().getMouseLocation();

			Point tCurrentMousePosition = this.getLocation();

			private void updateDelta(Dimension d) {
				for (Object o : super.getOperationSet()) {
					if (o instanceof StoryCardEditPart) {
						((StoryCardEditPart) o).getCastedModel()
								.setDragMoveDelta(d);
					}
				}
			}

			/**
			 * Override of mouse events. The call to the super keeps the drag
			 * and drop envent and allows for additional functionality
			 */

			@Override
			protected boolean handleButtonUp(int Button) {
				boolean retval = super.handleButtonUp(Button);

				return retval;
			}

			@Override
			protected boolean handleDoubleClick(int Button) {
				boolean returnVal = super.handleDoubleClick(Button);

				CollapseExpandStoryCardCommand command = new CollapseExpandStoryCardCommand(
						getCastedModel());
				command.execute();
				return returnVal;
			}

			@Override
			protected boolean handleDragInProgress() {
				boolean returnVal = super.handleDragInProgress();

//				if (Settings.isRotating_Mode()){
//
//					StoryCardModel model = StoryCardEditPart.this
//							.getCastedModel();
//					Dimension d = this.getDragMoveDelta();
//
//					Point centerOfStoryCard = new Point(model.getLocation().x
//							+ model.getWidthClientArea() / 2, model
//							.getLocation().y
//							+ model.getHeightClientArea() / 2);
//					double transitionOnlyRadius = 50; // changable according
//					boolean transitionOnly = true;
//					tCurrentMousePosition = this.getLocation();
//					double distance = Math.abs(centerOfStoryCard
//							.getDistance(getStartLocation()));
//					if (distance > transitionOnlyRadius) {
//						transitionOnly = false;
//					}
//
//					if (!transitionOnly) {
//
//						rotationAngle = getRotationAngle(centerOfStoryCard,
//								oldMousePosition, tCurrentMousePosition);
//
//						oldMousePosition = tCurrentMousePosition;
//						if (getCastedModel().getHeight() != CardConstants.WindowsStoryCardSmallHeight) {
//							System.out.println("Will Rotate: " + rotationAngle);
//
//							{
//								if (getCastedModel().getHeight() != CardConstants.WindowsStoryCardSmallHeight)
//									model.updateRotateAngle(rotationAngle);
//							}
//						}
//					}
//					this.updateDelta(d);
//
//				}
//
//				else
				this.updateDelta(this.getDragMoveDelta());

				return returnVal;

			}

			private float getRotationAngle(Point centerOfStoryCard,
					Point oldMousePosition, Point tCurrentMousePosition) {
				// calcaulating Vector CO
				double angleCO = Math.atan2(centerOfStoryCard.y,
						centerOfStoryCard.x)
						- Math.atan2(oldMousePosition.y, oldMousePosition.x);
				double distanceCO = centerOfStoryCard
						.getDistance(oldMousePosition);
				double yComponentCO = distanceCO * Math.cos(angleCO);
				double xComponentCO = distanceCO * Math.sin(angleCO);

				// calcaulating Vector OT
				double angleOT = Math.atan2(oldMousePosition.y,
						oldMousePosition.x)
						- Math.atan2(tCurrentMousePosition.y,
								tCurrentMousePosition.x);
				double distanceOT = oldMousePosition
						.getDistance(tCurrentMousePosition);
				double yComponentOT = distanceOT * Math.cos(angleOT);
				double xComponentOT = distanceOT * Math.sin(angleOT);

				// calcaulating Vector CT
				double angleCT = Math.atan2(centerOfStoryCard.y,
						centerOfStoryCard.x)
						- Math.atan2(tCurrentMousePosition.y,
								tCurrentMousePosition.x);
				double distanceCT = centerOfStoryCard
						.getDistance(tCurrentMousePosition);
				double yComponentCT = distanceCT * Math.cos(angleCT);
				double xComponentCT = distanceCT * Math.sin(angleCT);

				// calculating the angles
				double alpha = Math.atan(yComponentCT / xComponentCT);
				double beta = Math.atan(yComponentCO / xComponentCO);
				float theta = (float) (alpha - beta);
				if (oldMousePosition.x >= figure.getBounds().getCenter().x)
					return (float) (theta * 180 / Math.PI);
				else
					return (float) (-theta * 180 / Math.PI);
			}

		};
	}

	public Label getMostLikelyLabel() {
		return this.mostLikelyLabel;
	}

	public Label getNameLabel() {
		return this.nameLabel;
	}

	public Label getRemainingLabel() {
		return remainingLabel;
	}

	public Label getWorstCaseLabel() {
		return this.worstCaseLabel;
	}

	@Override
	public void performRequest(Request request) {

		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			this.performDirectEdit(((DirectEditRequest) request).getLocation()
					.getCopy());
			return;
		}
		super.performRequest(request);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if (StoryCardModel.SIZE_PROP.equals(prop)
				|| StoryCardModel.LOCATION_PROP.equals(prop)
				|| StoryCardModel.NAME_PROP.equals(prop)
				|| StoryCardModel.DESCRIPTION_PROP.equals(prop)
				|| StoryCardModel.BESTCASE_PROP.equals(prop)
				|| StoryCardModel.WORSTCASE_PROP.equals(prop)
				|| StoryCardModel.MOSTLIKEYL_PROP.equals(prop)
				|| StoryCardModel.ACTUAL_PROP.equals(prop)
				|| StoryCardModel.COMPLETE_PROP.equals(prop)
				|| StoryCardModel.COLOR_PROP.equals(prop)) {

			try {
				Display d = this.getViewer().getControl().getDisplay();
				d.syncExec(this.refresher);
			} catch (Exception e) {
			}
		}

		if (StoryCardModel.SEARCH_PROP.equals(prop)) {
			this.getViewer().reveal(this);
			this.refreshVisuals();
		}
	}

	/**
	 * method to update the figure when chages to the model occure. UI elements
	 * are added to the figure here.
	 */
	@Override
	@SuppressWarnings("static-access")
	public void refreshVisuals() {

		StoryCardFigure figure = (StoryCardFigure) this.getFigure();
		figure.removeAll();

		int sideup = this.getCastedModel().getCurrentSideUp();
		figure.setCurrentSideUp(sideup);
		
		
		switch (sideup) {
		case StoryCard.FRONT_SIDE: 

			if(this.getCastedModel().getColor().equals("transparent"))
				paintTextAreaSide(figure);
			else
				paintFrontSide(figure);
		
			break;
		
		case StoryCard.RICH_TEST_SIDE:
			paintBackSide(figure);

			break;

		case StoryCard.PROTOTYPE_SIDE:
			paintPrototypeSide(figure);

			break;
			
		case StoryCard.HANDWRITING_SIDE:
			paintHandWritingSide(figure);
		}
		
		paintAdditionalSides(sideup);
		
		figure.repaint();

	}

	/**
	 * Intended to be overwritten by classes extending this one to inject additional card sides.
	 * @param currentSideUp TODO
	 */
	protected void paintAdditionalSides(int currentSideUp) {
		
	}
	
	private void paintTextAreaSide(StoryCardFigure figure){
		
		this.storyCardFontSize = this.getCastedModel().getStoryCardFontSize();
		this.storyCardFont = this.getCastedModel().getStoryCardFont();
		
		if (this.scFont != null) {
			scFont.dispose();
		}

		scFont = new Font(null, "Rockwell", 22, 0);

		figure.setStoryCardColor(this.getCastedModel().getColor());

		figure.setStoryCardFont(this.getCastedModel().getStoryCardFont(), 22);



		Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(),
				this.getCastedModel().getSize());

		((GraphicalEditPart) this.getParent()).setLayoutConstraint(this,
				figure, bounds);

		figure.setLocation(this.getCastedModel().getLocation());
		figure.setBounds(bounds);

		
		this.descriptionLabel.setText(this.getCastedModel().getDescription());
		this.descriptionLabel
				.setLocation(new Point(
						StoryCardModel.INITIAL_X_OFFSET + figure.getClientArea().x
								+ ((System.getProperty("os.name")
										.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
										: CardConstants.WindowsStoryCardNameLabelX),
						figure.getClientArea().y + 8));
		this.descriptionLabel
				.setSize(
						this.getCastedModel().getStoryCard().getWidth()
								- ((System.getProperty("os.name")
										.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
										: CardConstants.WindowsStoryCardNameLabelX),
						this.getCastedModel().getStoryCard().getHeight()-8);
		this.descriptionLabel.setFont(scFont);
		this.descriptionLabel.setLabelAlignment(PositionConstants.CENTER);
		this.descriptionLabel.setFocusTraversable(false);
		figure.add(this.descriptionLabel);
		
	}

	private void drawNameLabel(StoryCardFigure figure){
		this.nameLabel.setText(this.getCastedModel().getName());
		this.nameLabel
				.setLocation(new Point(
						StoryCardModel.INITIAL_X_OFFSET + figure.getClientArea().x
						+ ((System.getProperty("os.name")
							.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
							: CardConstants.WindowsStoryCardNameLabelX),
					figure.getClientArea().y + 8));
		this.nameLabel
				.setSize(
					this.getCastedModel().getStoryCard().getWidth()
						- ((System.getProperty("os.name")
							.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
							: CardConstants.WindowsStoryCardNameLabelX),
					this.storyCardFontSize + 7);
		this.nameLabel.setFont(scFont);
		this.nameLabel.setLabelAlignment(PositionConstants.LEFT);
		this.nameLabel.setFocusTraversable(false);
		figure.add(this.nameLabel);
	}
	
	private void drawOwnerLabel(StoryCardFigure figure){
		int xLocation = StoryCardModel.INITIAL_X_OFFSET + figure.getClientArea().x + ((this.storyCardFontSize) * 20) + 56;
		int yLocation = figure.getClientArea().y + StoryCardModel.INITIAL_Y_OFFSET + StoryCardModel.LINE_HEIGHT;
		this.ownerLabel.setLocation(new Point(xLocation, yLocation));
		this.ownerLabel.setText(this.getCastedModel().getCardOwner());

		// this.actualLabel.setSize(estimateLabelWidth, this.storyCardFontSize);
		this.ownerLabel.setSize((3 * this.storyCardFontSize) + 50, this.storyCardFontSize + 5);
		this.ownerLabel.setLabelAlignment(PositionConstants.LEFT);
		this.ownerLabel.setFont(scFont);
		figure.add(this.ownerLabel);
	}
	
	private void drawDescription(StoryCardFigure figure){
		int yLocation;
		if (this.getCastedModel().isHandwritten()) {
		} else {
			this.descriptionLabel.setText(this.getCastedModel()
					.getDescription());
			this.descriptionLabel
					.setTextPlacement(PositionConstants.NORTH_EAST);
		}

		// Always show the Description area!

		if (!Settings.isEstimate_bestCase() && !Settings.isEstimate_worstCase())
			yLocation = figure.getClientArea().y + 38
					+ (3 * this.storyCardFontSize);
		else
			yLocation = figure.getClientArea().y + 48
					+ (4 * this.storyCardFontSize);

		this.descriptionLabel.setLocation(new Point(
				StoryCardModel.INITIAL_X_OFFSET+figure.getClientArea().x + 5, yLocation));
		this.descriptionLabel.setFont(scFont);
		/**
		 * TODO: change the size to be the size of the card!
		 */
		this.descriptionLabel
				.setSize((figure.getClientArea().width) - 30, 2000);// new
		this.descriptionLabel.setLabelAlignment(PositionConstants.LEFT);
		figure.add(this.descriptionLabel);
	}
	private Label drawEstimate(StoryCardFigure figure, String value, int textWidth){
		Label result = new Label();
		final int xLocalation = figure.getClientArea().x + xLocationCalculator(textWidth);
		final int yLocalation = figure.getClientArea().y + yLocationCalculator();
		
		result.setText(this.getCastedModel().getMostLikelyEstimate());
		result.setLocation(new Point(xLocalation, yLocalation));
		result.setSize((3 * this.storyCardFontSize), this.storyCardFontSize + 5);
		result.setLabelAlignment((PositionConstants.LEFT));
		result.setFont(scFont);
		figure.add(result);
		visibleEstimates++;
		return result;
	}
	private int yLocationCalculator(){
		return StoryCardModel.INITIAL_Y_OFFSET + (StoryCardModel.LINE_HEIGHT * (visibleEstimates/2));
	}
	private int xLocationCalculator(int textWidth){
		int x = (visibleEstimates%2==0) ? StoryCardModel.ESTIMATE_LEFT_SIDE_OFFSET : StoryCardModel.ESTIMATE_RIGHT_SIDE_OFFSET ;
		return StoryCardModel.INITIAL_X_OFFSET + x + textWidth;
	}
	
	private void drawStatusBar(StoryCardFigure figure){
		ImageDescriptor bd;
		ImageDescriptor id;
		
		if (this.getCastedModel().getStatus().equalsIgnoreCase(IndexCard.STATUS_IN_PROGRESS)) {
			id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.CompleteStoryAndIterationButtonUnchecked);
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.BAR_INPROGRESS);
		}else if (this.getCastedModel().getStatus().equalsIgnoreCase(IndexCard.STATUS_COMPLETED)) {
			id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.CompleteStoryAndIterationButtonChecked);
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.BAR_COMPLETED);
		} else if (this.getCastedModel().getStatus().equalsIgnoreCase(IndexCard.STATUS_DEFINED)) {
			id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(),CardConstants.CompleteStoryAndIteratoinButtonDefined);
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.BAR_DEFINED);
		} else {
			id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.CompleteStoryAndIteratoinButtonAccepted);
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.BAR_ACCEPTED);
		}
		
		Point location = new Point(
			figure.getClientArea().x - 5,
			figure.getClientArea().y
		);
		Dimension size = new Dimension(24, figure.getClientArea().height);
		
		this.statusBar.setLocation(location);
		this.statusButton.setLocation(location);
		
		this.statusBar.setSize(size);
		//size.height -= 20;
		this.statusButton.setSize(size);
		/* ************************************************************* */
		if (this.statusBar.getIcon() != null) this.statusBar.getIcon().dispose();
		this.statusBar.setIcon(bd.createImage());
		figure.add(this.statusBar);
		
		if (this.statusButton.getIcon() != null) this.statusButton.getIcon().dispose();
		this.statusButton.setIcon(id.createImage());
		figure.add(this.statusButton);
	}
	
	private void paintFrontSide(StoryCardFigure figure) {
		//StoryCardEditPartBuilder builder = new StoryCardEditPartBuilder(figure);
		visibleEstimates = 2;
		this.storyCardFontSize = this.getCastedModel().getStoryCardFontSize();

		this.storyCardFont = this.getCastedModel().getStoryCardFont();
		if (this.scFont != null) {
			scFont.dispose();
		}
		scFont = new Font(null, this.storyCardFont, this.storyCardFontSize, 0);
		figure.setStoryCardColor(this.getCastedModel().getColor());
		figure.setStoryCardFont(this.getCastedModel().getStoryCardFont(), this.getCastedModel().getStoryCardFontSize());
		Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(), this.getCastedModel().getSize());
		((GraphicalEditPart) this.getParent()).setLayoutConstraint(this, figure, bounds);

		figure.setLocation(this.getCastedModel().getLocation());
		figure.setBounds(bounds);

		int lhsLabelOffset = (this.storyCardFontSize * 7) + 5;// /////////////////////////////
		
		drawNameLabel(figure);
		drawOwnerLabel(figure);
		mostLikelyLabel = drawEstimate(figure, this.getCastedModel().getMostLikelyEstimate(), 60);
		if (Settings.isEstimate_actual())		actualLabel = drawEstimate(figure, this.getCastedModel().getActualEffort(), 40);
		if (Settings.isEstimate_bestCase())		bestCaseLabel = drawEstimate(figure, this.getCastedModel().getBestCaseEstimate(), 60);
		if (Settings.isEstimate_worstCase())	worstCaseLabel = drawEstimate(figure, this.getCastedModel().getWorstCaseEstimate(), 60);
		if (Settings.isEstimate_remaining())	remainingLabel = drawEstimate(figure, this.getCastedModel().getRemainingEstimate(), 60);
		
		/***********************************************************************
		 * This section is for the handwritten cards. It will display the
		 * handwrittne values in the description area.
		 **********************************************************************/
		drawDescription(figure);
		/** *************************************************************************************************************** */

		figure.getClientArea();
		figure.setPriorityText(this.getCastedModel().getPriority());
		drawStatusBar(figure);
		
		ImageDescriptor td;
		td = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
				.getPLUGIN_ID(), CardConstants.TRIANGLEBUTTON);

		if (this.triangleButton.getIcon() != null)
			this.triangleButton.getIcon().dispose();

		this.triangleButton.setIcon(td.createImage());
		this.triangleButton.setLocation(new Point(figure.getClientArea().x
				+ figure.getClientArea().width - 20, figure.getClientArea().y

		+ figure.getClientArea().height - 31));

		this.triangleButton.setSize(new Dimension(30, 30));

		figure.add(this.triangleButton);
	}



	private void paintPrototypeSide(StoryCardFigure figure) {
		System.out.println("Prototype Side");

		int estimateLabelLeftSideOffest = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelLeftSideOffset
				: CardConstants.WindowsEstimateLabelLeftSideOffest);
		int estimateLabelRightSideOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelRightSideOffset
				: CardConstants.WindowsEstimateLabelRightSideOffest);
		int firstEstimateLineOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacFirstEstimateLineOffset
				: CardConstants.WindowsFirstEstimateLineOffset);
		int estimateLineIncrementValue = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLineIncrementValue
				: CardConstants.WindowsEstimateLineIncrementValue);
		int estimateLabelWidth = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelWidth
				: CardConstants.WindowsEstimateLabelWidth);
		int estimateLabelHeight = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelHeight
				: CardConstants.WindowsEstimateLabelHeight);

		int urlLabelOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacUrlLabelOffset
				: CardConstants.WindowsUrlLabelOffset);

		int urlLabelHeight = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacUrlLabelHeight
				: CardConstants.WindowsUrlLabelHeight);

		int urlLabelTop = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacUrlLabelTop
				: CardConstants.WindowsUrlLabelTop);

		Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(),
				this.getCastedModel().getSize());

		((GraphicalEditPart) this.getParent()).setLayoutConstraint(this,
				figure, bounds);

		figure.setLocation(this.getCastedModel().getLocation());
		figure.setBounds(bounds);

	

		this.nameLabel.setText(this.getCastedModel().getName());
		this.nameLabel
				.setLocation(new Point(
						figure.getClientArea().x
								+ ((System.getProperty("os.name")
										.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
										: CardConstants.WindowsStoryCardNameLabelX),
						figure.getClientArea().y + 5));
		this.nameLabel.setSize(125, 15);
		this.nameLabel.setSize(
				this.getCastedModel().getStoryCard().getWidth() - 80, 15);
		this.nameLabel.setLabelAlignment(PositionConstants.LEFT);
		this.nameLabel.setFocusTraversable(false);
		if (figure.getARotate() == 0)
			figure.add(this.nameLabel);
		figure.setNameLabel(this.nameLabel.getText());

		drawAddPrototypeButton();
		figure.add(this.addPrototypeButton);

		currentLabelIndex = 0;
		ImageDescriptor bd;
		if (this.getCastedModel().getColor().equalsIgnoreCase("red")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLERED);
			figure.setTriangleColor("red");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("yellow")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEBUTTON);
			figure.setTriangleColor("yellow");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("green")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEGREEN);
			figure.setTriangleColor("green");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("blue")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEBLUE);
			figure.setTriangleColor("blue");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("pink")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEPINK);
			figure.setTriangleColor("pink");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("white")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEWHITE);
			figure.setTriangleColor("white");
		}
		else {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEBUTTON);
			figure.setTriangleColor("yellow");
		}
		if (this.triangleButton.getIcon() != null)
			this.triangleButton.getIcon().dispose();

		// if(this.getCastedModel().getColor().equalsIgnoreCase("red"))
		this.triangleButton.setIcon(bd.createImage());
		this.triangleButton.setLocation(new Point(figure.getClientArea().x
				+ figure.getClientArea().width - 20, figure.getClientArea().y

		+ figure.getClientArea().height - 31));

		this.triangleButton.setSize(new Dimension(30, 30));

		figure.add(this.triangleButton);

		figure.setPriorityText(this.getCastedModel().getPriority());

		figure.setHeight(this.getCastedModel().getHeight());
		figure.setWidth(this.getCastedModel().getWidth());

		figure.setARotate(this.getCastedModel().getARotate());

		figure.repaint();

	}
	
	private void paintHandWritingSide(StoryCardFigure figure) {
		
		
		Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(),
				this.getCastedModel().getSize());

		((GraphicalEditPart) this.getParent()).setLayoutConstraint(this,
				figure, bounds);

		figure.setLocation(this.getCastedModel().getLocation());
		figure.setBounds(bounds);

		
		
         byte[] pic = this.getCastedModel().getStoryCard().getHandwritingImage();
		
		if (pic !=null){
		try{
			
			
			String picName = String.valueOf(this.getCastedModel().getStoryCard().getId());
        
        javax.imageio.stream.FileImageOutputStream fios=new javax.imageio.stream.FileImageOutputStream(new java.io.File(picName +".png"));
		fios.write(pic,0,pic.length);
        fios.flush();
        fios.close();
        
			
			
			File file = new File(picName +".png");
	        Image image = ImageIO.read(file);
			BufferedImage bi;
		      // check image format to see if it supports transparency
		        bi = new BufferedImage(this.getCastedModel().getWidth(),this.getCastedModel().getHeight(),BufferedImage.TYPE_INT_ARGB);	
		      			
		      Graphics g = bi.createGraphics();
		      // scale image				
		      Image tmp = image.getScaledInstance(this.getCastedModel().getWidth(),this.getCastedModel().getHeight(),Image.SCALE_SMOOTH);			
		      // paint scaled image to bufferedImage
		      g.drawImage(tmp,0,0,null);			
		       // save bufferedImage
		       ImageIO.write(bi,"PNG",new File("new"+picName+".png"));
		       System.out.println(file.getAbsolutePath());
		       g.dispose();
						

        if (this.handWriting.getIcon() != null)
			this.handWriting.getIcon().dispose();
		
       
        this.handWriting.setIcon(ImageDescriptor.createFromFile(null, ".\\new"+picName+".png").createImage());
   
        this.handWriting.setSize(this.getCastedModel().getSize());
        this.handWriting.setLocation(this.getCastedModel().getLocation());

		
	    figure.add(this.handWriting);
   
				
        }catch(Exception e)
        {
        
        }

		}
        


		
		
		ImageDescriptor bd;
		if (this.getCastedModel().getColor().equalsIgnoreCase("red")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLERED);
			figure.setTriangleColor("red");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("yellow")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEBUTTON);
			figure.setTriangleColor("yellow");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("green")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEGREEN);
			figure.setTriangleColor("green");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("blue")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEBLUE);
			figure.setTriangleColor("blue");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("pink")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEPINK);
			figure.setTriangleColor("pink");
		} else if (this.getCastedModel().getColor().equalsIgnoreCase("white")) {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEWHITE);
			figure.setTriangleColor("white");
		}
		else {
			bd = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
					.getPLUGIN_ID(), CardConstants.TRIANGLEBUTTON);
			figure.setTriangleColor("yellow");
		}
		if (this.triangleButton.getIcon() != null)
			this.triangleButton.getIcon().dispose();

		this.triangleButton.setIcon(bd.createImage());
		this.triangleButton.setLocation(new Point(figure.getClientArea().x
				+ figure.getClientArea().width - 20, figure.getClientArea().y

		+ figure.getClientArea().height - 31));

		this.triangleButton.setSize(new Dimension(30, 30));

		figure.add(this.triangleButton);

		figure.setPriorityText(this.getCastedModel().getPriority());

		figure.setHeight(this.getCastedModel().getHeight());
		figure.setWidth(this.getCastedModel().getWidth());

		figure.setARotate(this.getCastedModel().getARotate());

		figure.repaint();

	}

	protected void drawAdditionalOnFitTestSide(StoryCardFigure figure) {
		// TODO Auto-generated method stub
		
	}

	private void paintBackSide(StoryCardFigure figure) {
		System.out.println("Back Side");
		int estimateLabelLeftSideOffest = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelLeftSideOffset
				: CardConstants.WindowsEstimateLabelLeftSideOffest);
		int estimateLabelRightSideOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelRightSideOffset
				: CardConstants.WindowsEstimateLabelRightSideOffest);
		int firstEstimateLineOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacFirstEstimateLineOffset
				: CardConstants.WindowsFirstEstimateLineOffset);
		int estimateLineIncrementValue = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLineIncrementValue
				: CardConstants.WindowsEstimateLineIncrementValue);
		int estimateLabelWidth = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelWidth
				: CardConstants.WindowsEstimateLabelWidth);
		int estimateLabelHeight = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLabelHeight
				: CardConstants.WindowsEstimateLabelHeight);

		int urlLabelOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacUrlLabelOffset
				: CardConstants.WindowsUrlLabelOffset);

		int urlLabelHeight = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacUrlLabelHeight
				: CardConstants.WindowsUrlLabelHeight);

		int urlLabelTop = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacUrlLabelTop
				: CardConstants.WindowsUrlLabelTop);

		Rectangle bounds = new Rectangle(this.getCastedModel().getLocation(),
				this.getCastedModel().getSize());

		((GraphicalEditPart) this.getParent()).setLayoutConstraint(this,
				figure, bounds);

		figure.setLocation(this.getCastedModel().getLocation());
		figure.setBounds(bounds);

		this.nameLabel.setText(this.getCastedModel().getName());
		this.nameLabel
				.setLocation(new Point(
						figure.getClientArea().x
								+ ((System.getProperty("os.name")
										.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
										: CardConstants.WindowsStoryCardNameLabelX),
						figure.getClientArea().y + 5));
		this.nameLabel.setSize(125, 15);
		this.nameLabel.setSize(
				this.getCastedModel().getStoryCard().getWidth() - 80, 15);
		this.nameLabel.setLabelAlignment(PositionConstants.LEFT);
		this.nameLabel.setFocusTraversable(false);
		if (figure.getARotate() == 0)
			figure.add(this.nameLabel);

		this.urlLabel.setText(this.getCastedModel().getStoryCard()
				.getAcceptanceTestUrl());
		this.urlLabel.setLocation(new Point(figure.getClientArea().x
				+ urlLabelOffset, figure.getClientArea().y + urlLabelTop));
		this.urlLabel.setSize(
				(figure.getClientArea().width - urlLabelOffset - 30),
				urlLabelHeight);
		this.urlLabel.setLabelAlignment(PositionConstants.LEFT);

		if (figure.getARotate() == 0)
			figure.add(this.urlLabel);

		figure.setAcceptanceTest(this.getCastedModel().getAcceptanceTest());

		figure.setPriorityText(this.getCastedModel().getPriority());
	}

	public Label getUrlLabel() {
		return this.urlLabel;
	}

	@Override
	public List getModelChildren() {
		List<IndexCardModel> modelChildren = new ArrayList<IndexCardModel>();
		Collections.sort(modelChildren);
		if (modelChildren.isEmpty()) {
			return Collections.EMPTY_LIST;
		} else {
			return Collections.unmodifiableList(modelChildren);
		}
	}

	@Override
	protected void fireSelectionChanged() {

		super.fireSelectionChanged();
	}

	public int getSelected() {

		return super.getSelected();

	}

	@Override
	public boolean isSelectable() {

		return super.isSelectable();
	}

	@Override
	public void setSelected(int arg0) {

		super.setSelected(arg0);
	}

	public String getStoryCardColor() {
		return storyCardColor;
	}

	public void setStoryCardColor(String storyCardColor) {
		this.storyCardColor = storyCardColor;
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

	public void setStoryCardFontSize(int storyCardFontSize) {
		this.storyCardFontSize = storyCardFontSize;
	}

	private void drawAddPrototypeButton() {

		ImageDescriptor id;

		id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
				.getPLUGIN_ID(), CardConstants.PROTOTYPE_ADD_BUTTON_ICON);

		if (this.addPrototypeButton.getIcon() != null)
			this.addPrototypeButton.getIcon().dispose();

		this.addPrototypeButton.setIcon(id.createImage());

		this.addPrototypeButton.setLocation(new Point(figure.getClientArea().x
				+ figure.getClientArea().width - 45,
				figure.getClientArea().y + 25)); // + this.storyCardFontSize
		this.addPrototypeButton.setSize(new Dimension(45, 17));

	}

	public Label getOwnerLabel() {
		return this.ownerLabel;
	}

	public void setOwnerLabel(Label ownerLabel) {
		this.ownerLabel = ownerLabel;
	}

}

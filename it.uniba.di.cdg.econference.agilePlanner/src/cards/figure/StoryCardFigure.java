/***************************************************************************************************
 *Version 2.3
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: StoryCard Figure Class used to draw the figure and any non UI element. 
 *								COntiains a mouse listener that should not be used for passing data to the contorler or the Model 
 *
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.internal.presentations.util.LeftToRightTabOrder;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import persister.data.StoryCard;
import persister.data.impl.AcceptanceTestDataObject;
import persister.factory.Settings;


import cards.CardConstants;
import cards.model.StoryCardModel;
import fitintegration.PluginInformation;

public class StoryCardFigure extends Viewport {

	private String nameTitle = "Name: ";
	
	private String ownerTitle = "Owner: ";
	private boolean Handwriting ;
	
	

	protected int currentSideUp = StoryCard.RICH_TEST_SIDE;
	private Rectangle clip_Rotate;

	/** These are the UI elements for the front of the card. */

//	private StoryCardFigure figure;

	private Point figureLocation;

	private String storyCardColor;

	private String storyCardFont;


	private int actualLabelLoc;

	protected String nameLabelText;
	private String ownerLabelText;
	private String mostLikelyLabelText;
	private String actualLabelText;
	private String bestCaseLabelText;
	private String remainingLabelText;
	private String descriptionLabelText;
	
	private String bestCaseEstimateTitle = "Best Case: ";
	private String worstCaseEstimateTitle = "Worst Case: ";
	private String remainingEstimateTitle = "Remaining: ";
	private String mostLikelyEstimateTitle = "Most Likely: ";
	private String actualTitle = "Actual: ";
	private String priorityTitle = "Rank: ";

	private String priorityField;
	private String triangleColor;

	protected int storyCardFontSize;
	private Font DISPLAY_FONT;

	/** These are the UI elements for the back of the card. */

	private static String numberOfTestRunsTitle = "Total Runs: ";
	private static String numPassTitle = "Passing: ";
	private static String numFailTitle = "Failing: ";
	private static String graphTitle = "Test History: ";
	private static String urlTitle = "URL: ";
	private String prototypeText = "Prototype:";

	private Point pointDescription = new Point();
	public boolean isInTheFigure = false;

	protected Graphics graph;

	// rotate
	protected double tempCardClipWidth,
		tempCardClipHeight,
		tempCardClipDiagonal,
		actualNewCardClipWidth,
		actualNewCardClipHeight,
		newTempWidth;

	private double beta;
	float aRotate;
	private double bRotate;

	protected int storyCardWidth;
	protected int storyCardHeight;
	private float angle = 0;


	private String status;
	
	private String URLLabelText;
	private AcceptanceTestDataObject acceptTest = null;

	public StoryCardFigure() {

		/** this seems smelly */
//		this.figure = this;

		/** end of smell */
		this.setOpaque(true);
		this.isInTheFigure = false;

		this.figureLocation = new Point();
		this.addMouseListener(new MouseListener() {

			/**
			 * NOTE: The actual mouse handlers are associated with the EditPart
			 * for this figure. this is only to bring the figure to front. No
			 * other code should be placed here under any circumstance! In
			 * addition do not consume the mouse event here, it will prevent the
			 * system from performing as it should. RM.
			 */

			public void mousePressed(MouseEvent me) {
				StoryCardFigure.this.refreshFigureOrder();
			}

			public void mouseReleased(MouseEvent me) {

			}

			public void mouseDoubleClicked(MouseEvent me) {

			}
		});
	}

	public void setPriorityText(String priority) {
		this.priorityField = priority;
	}

	public void setFigureLocation(Point location) {
		this.figureLocation = location;
	}

	public Point getFigureLocation() {
		return this.figureLocation;
	}

	@Override
	@SuppressWarnings("static-access")
	protected void paintFigure(Graphics g) {
		this.graph = g;

		g.pushState();
//
		drawNameLine(g);

		 switch(this.currentSideUp){
			 case StoryCard.RICH_TEST_SIDE:
			
			 drawRichTestSide(g);
			 break;
			 case StoryCard.FRONT_SIDE:
				 if(storyCardColor.equals("transparent"))
					 drawTextArea(g,storyCardColor);
				 else
					 drawFrontSideNonRotatingMode(g, storyCardColor); 
	
			 break;
			 
			 case StoryCard.PROTOTYPE_SIDE:
				 drawPrototypeSide(g);
			 break;
			 
			 case StoryCard.HANDWRITING_SIDE:
				 drawHandWritingSide(g);
			break;
		 }

		paintAdditionalFigures();
		 
		g.popState();

	}

	/**
	 *  Intended to be overwritten by extending classes to inject new sites of cards.
	 */
	protected void paintAdditionalFigures(){
	}
	
	private void drawNameLine(Graphics g) {

	}


	private void drawTextArea(Graphics g, String color){

    	Rectangle clip = this.getClientArea();
          g.setFont(DISPLAY_FONT);
            
            g.setAlpha(0);

            g.setBackgroundColor((Color)StoryCardModel.COLORS.get(color));
            
            g.fillRectangle(clip);
	
	}
	

	
	private void drawFrontSideNonRotatingMode(Graphics g, String color){
    	final Rectangle clip = this.getClientArea();
    	final GraphicBuilder builder = new GraphicBuilder(g, clip, this.storyCardFontSize);
    	
    	
    	
        final int nameLine = CardConstants.STORYCARDFIGURENAMELINE, bcLine = CardConstants.STORYCARDFIGUREBESTCASELINE, wcLine = CardConstants.STORYCARDFIGUREWORSTCASELINE, lineHeight = CardConstants.STORYCARDFIGURELINEHEIGHT;

        if (this.isOpaque()) {
            g.setFont(DISPLAY_FONT);
            g.setAlpha(255);
            g.setBackgroundColor((Color)StoryCardModel.COLORS.get(color));
            g.fillRectangle(clip);
            
            // Draw divider lines on the Card.
            g.setForegroundColor(new Color(null, 255, 0, 0));
            g.drawLine(clip.x, clip.y + nameLine+(this.storyCardFontSize-5), clip.x + clip.width, clip.y +nameLine+(this.storyCardFontSize-5));
            int relPos=nameLine+(this.storyCardFontSize-5);
            
            g.setForegroundColor(new Color(null, 180, 180, 180));
            g.drawLine(clip.x, clip.y +relPos + bcLine + (this.storyCardFontSize-5), clip.x + clip.width, clip.y +relPos+ bcLine+(this.storyCardFontSize-5));
            
            int relPos2=relPos +bcLine+(this.storyCardFontSize-5);
            g.drawLine(clip.x, clip.y + relPos2 + wcLine+(this.storyCardFontSize-5), clip.x + clip.width, clip.y+relPos2 + wcLine+(this.storyCardFontSize-5));
        
            int relPos3=relPos2+wcLine+(this.storyCardFontSize-5);
            g.drawLine(clip.x, clip.y + relPos3+wcLine + (this.storyCardFontSize-5), clip.x + clip.width, clip.y+relPos3 + wcLine+(this.storyCardFontSize-5));
            
            int relPos4=relPos3+wcLine+(this.storyCardFontSize-5);
            g.drawLine(clip.x, clip.y + relPos4+wcLine + (this.storyCardFontSize-5), clip.x + clip.width, clip.y+relPos4 + wcLine+(this.storyCardFontSize-5));
           
            g.setForegroundColor(new Color(null, 200, 200, 200));
            
            for (int i = 2; i < (clip.height / this.storyCardFontSize); i++) {
                g.drawLine(clip.x, clip.y +(this.storyCardFontSize+10) * i + relPos4, clip.x + clip.width, clip.y + (this.storyCardFontSize+10) * i + relPos4);
            }
            g.setForegroundColor(new Color(null, 0, 0, 0));
            
            builder.drawOwner(ownerTitle, 1);
            int numberOfEstimate = 2;
            // Always Draw the MostLikely Estimate
            builder.drawEstimate(mostLikelyEstimateTitle, numberOfEstimate/2);
            numberOfEstimate++;
            // show actual Estimate!
            if (Settings.isEstimate_actual()) {
            	builder.drawEstimate(actualTitle, numberOfEstimate/2);
            	numberOfEstimate++;
            }
            // Show BestCase Estimate!
            if (Settings.isEstimate_bestCase()) {
            	builder.drawEstimate(bestCaseEstimateTitle, numberOfEstimate/2);
            	numberOfEstimate++;
            }

            // Show WorstCase Estimate!
            if (Settings.isEstimate_worstCase()) {
            	builder.drawEstimate(worstCaseEstimateTitle, numberOfEstimate/2);
            	numberOfEstimate++;
            }
            if (Settings.isEstimate_remaining()) {
            	builder.drawEstimate(remainingEstimateTitle, numberOfEstimate/2);
            	numberOfEstimate++;
            }
            
            g.setForegroundColor(new Color(null, 100, 100, 100));// old
            g.drawRectangle(clip.x, clip.y, clip.width - 1, clip.height - 1);
        }
    }
	
	private void drawRichTestSide(Graphics g) {

		Font backSideFont = new Font(null, "", 8, 0);
		Rectangle clip = this.getClientArea();
		int nameLine = CardConstants.STORYCARDFIGURENAMELINE, bcLine = CardConstants.STORYCARDFIGUREBESTCASELINE, wcLine = CardConstants.STORYCARDFIGUREWORSTCASELINE, lineHeight = CardConstants.STORYCARDFIGURELINEHEIGHT;

		int firstEstimateLineOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacFirstEstimateLineOffset
				: CardConstants.WindowsFirstEstimateLineOffset);
		int estimateLineIncrementValue = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLineIncrementValue
				: CardConstants.WindowsEstimateLineIncrementValue);

		if (this.isOpaque()) {
					g.setFont(backSideFont);

			g.setAlpha(255);
			// leave that color as is! requirement from rally!
			if (this.acceptTest.getNumFail() > 0) {
				g.setBackgroundColor(new Color(null, 254, 150, 150));
			} else if (this.acceptTest.getNumPass() > 0) {
				g.setBackgroundColor(new Color(null, 150, 255, 150));
			} else {
				g.setBackgroundColor(new Color(null, 254, 236, 138));
			}
			g.fillRectangle(clip);

			// Draw divider lines on the Card.
			g.setForegroundColor(new Color(null, 255, 0, 0));
			g.drawLine(clip.x, clip.y + nameLine, clip.x + clip.width, clip.y
					+ nameLine);
			
			
			 
			// // Always Draw the MostLikely Estimate

			g.setForegroundColor(new Color(null, 0, 0, 0));
			g.drawText(this.urlTitle, clip.x + 5, clip.y
					+ firstEstimateLineOffset);
			firstEstimateLineOffset += estimateLineIncrementValue;
			g.drawText(this.numPassTitle + this.acceptTest.getNumPass(),
					clip.x + 5, clip.y + firstEstimateLineOffset);
			g.drawText(this.numFailTitle + this.acceptTest.getNumFail(),
							clip.x + StoryCardModel.ESTIMATE_RIGHT_SIDE_OFFSET, clip.y
									+ firstEstimateLineOffset);
			firstEstimateLineOffset += estimateLineIncrementValue;
			g.drawText(this.numberOfTestRunsTitle
					+ this.acceptTest.getNumRuns(), clip.x + 5, clip.y
					+ firstEstimateLineOffset);
			firstEstimateLineOffset += estimateLineIncrementValue;
			// g.drawText(this.graphTitle, clip.x + 5, clip.y +
			// firstEstimateLineOffset);
			g.drawText(this.nameTitle, clip.x + 5, clip.y + nameLine - 15);

			// To show the priority Level
			// g.drawText(this.priorityTitle + this.priorityField, clip.x
			// + clip.width
			// - ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ?
			// CardConstants.MacStoryCardRankX
			// : CardConstants.WindowsStoryCardRankX), clip.y + 5);
			// Draw the border for the Card.
			g.drawText(this.acceptTest.getWiki(), clip.x + 5, clip.y
					+ firstEstimateLineOffset + 15);

			g.setForegroundColor(new Color(null, 100, 100, 100));// old
			g.drawRectangle(clip.x, clip.y, clip.width - 1, clip.height - 1);

		} else {

		}
	}

	
	
	private void drawPrototypeSide(Graphics g) {
		
		Font backSideFont = new Font(null, "", 8, 0);
		
		//declarations
		int xOrig, yOrig, xRotate, yRotate;
		Rectangle clip = this.getClientArea();
		Rectangle clipRotate;

		// calculate the rotated clip
		tempCardClipWidth = clip.width / 2;
		tempCardClipHeight = clip.height / 2;
		


		actualNewCardClipWidth = storyCardWidth / 2;
		actualNewCardClipHeight = storyCardHeight / 2;

		Rectangle clipTemp = new Rectangle(clip.x
				+ Math.round((float) (clip.width / 2 - actualNewCardClipWidth)), clip.y
				+ Math.round((float) (clip.height / 2 - actualNewCardClipHeight)), Math
				.round((float) actualNewCardClipWidth * 2), Math
				.round((float) actualNewCardClipHeight * 2));
		
		int nameLine = CardConstants.STORYCARDFIGURENAMELINE, bcLine = CardConstants.STORYCARDFIGUREBESTCASELINE, wcLine = CardConstants.STORYCARDFIGUREWORSTCASELINE, lineHeight = CardConstants.STORYCARDFIGURELINEHEIGHT;

		int firstEstimateLineOffset = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacFirstEstimateLineOffset
				: CardConstants.WindowsFirstEstimateLineOffset);
		int estimateLeftSideOffest = ((System.getProperty("os.name")
				.equalsIgnoreCase("Mac OS X")) ? CardConstants.MacEstimateLeftSideOffset
				: CardConstants.WindowsEstimateLeftSideOffset);
		if (this.isOpaque()) {
			g.setFont(backSideFont);

			g.setAlpha(255);
			// leave that color as is! requirement from rally!
			g.setBackgroundColor(new Color(null, 254, 236, 138));

			g.translate(clipTemp.x + clipTemp.width / 2, clipTemp.y
					+ clipTemp.height / 2);
	
			g.rotate(aRotate);

			clipRotate = new Rectangle(-clipTemp.width / 2,
					-clipTemp.height / 2, clipTemp.width, clipTemp.height);
			
			g.fillRectangle(clipRotate);

			// Draw divider lines on the Card.
			g.setForegroundColor(new Color(null, 255, 0, 0));
			g.drawLine(clipRotate.x, clipRotate.y + nameLine, clipRotate.x + clipRotate.width, clipRotate.y
					+ nameLine);
			int relPos = nameLine + (this.storyCardFontSize - 5);
			
			//Draw the StoryCard name field
			int namexloc = clipRotate.x + estimateLeftSideOffest;
			int nameyloc = clipRotate.y + relPos;
			g.setForegroundColor(new Color(null, 0, 0, 0));
			if(this.getARotate()!=0)
				g.drawText(this.nameLabelText, namexloc, nameyloc - 14);

			g.setForegroundColor(new Color(null, 0, 0, 0));
			g.drawText(this.prototypeText, clipRotate.x + 5, clipRotate.y
					+ firstEstimateLineOffset);
		
			// g.drawText(this.nameTitle, clipRotate.x + 5, clipRotate.y + nameLine - 15);

			// To show the priority Level
			// g.drawText(this.priorityTitle + this.priorityField, clipRotate.x
			// + clipRotate.width
			// - ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ?
			// CardConstants.MacStoryCardRankX
			// : CardConstants.WindowsStoryCardRankX), clipRotate.y + 5);

			// Draw the border for the Card.
			// g.drawText(this.acceptTest.getWiki(), clipRotate.x + 5, clipRotate.y +
			// firstEstimateLineOffset+ 15);

			g.setForegroundColor(new Color(null, 100, 100, 100));// old
			
			xOrig = clipTemp.x;
			yOrig = clipTemp.y;
			xRotate = xOrig - clipTemp.x - clipTemp.width / 2;
			yRotate = yOrig - clipTemp.y - clipTemp.height / 2;

			g.setForegroundColor(new Color(null, 100, 100, 100));// old
			g.drawRectangle(xRotate, yRotate, clipTemp.width, clipTemp.height);
			
			
			if(this.getARotate()!=0){
		         ImageDescriptor id=null;
		         
		         if(this.getTriangleColor().equalsIgnoreCase("white"))
				
				id = AbstractUIPlugin.imageDescriptorFromPlugin(
						PluginInformation.getPLUGIN_ID(),
						CardConstants.TRIANGLEWHITE);
		         
		         else if(this.getTriangleColor().equalsIgnoreCase("yellow"))
		        	 id = AbstractUIPlugin.imageDescriptorFromPlugin(
		 					PluginInformation.getPLUGIN_ID(),
		 					CardConstants.TRIANGLEBUTTON);
		        	 
		         else if(this.getTriangleColor().equalsIgnoreCase("green"))
		        	 id = AbstractUIPlugin.imageDescriptorFromPlugin(
		 					PluginInformation.getPLUGIN_ID(),
		 					CardConstants.TRIANGLEGREEN);
		         else if(this.getTriangleColor().equalsIgnoreCase("pink"))
		        	
		         id = AbstractUIPlugin.imageDescriptorFromPlugin(
		 					PluginInformation.getPLUGIN_ID(),
		 					CardConstants.TRIANGLEPINK);
		         
		         else if(this.getTriangleColor().equalsIgnoreCase("blue"))
			        	
			         id = AbstractUIPlugin.imageDescriptorFromPlugin(
			 					PluginInformation.getPLUGIN_ID(),
			 					CardConstants.TRIANGLEBLUE);
		         
		         else if(this.getTriangleColor().equalsIgnoreCase("yellow"))
			        	
			         id = AbstractUIPlugin.imageDescriptorFromPlugin(
			 					PluginInformation.getPLUGIN_ID(),
			 					CardConstants.TRIANGLEBUTTON);
		      

		         g.drawImage(id.createImage(), new Point(clipRotate.x
	        				+ this.storyCardWidth - 30,clipRotate.y+ this.storyCardHeight - 31));
			 }


		} else {

		}
	}
	
private void drawHandWritingSide(Graphics g) {
		
	 
		Font backSideFont = new Font(null, "", 8, 0);
		
		//declarations
		int xOrig, yOrig, xRotate, yRotate;
		Rectangle clip = this.getClientArea();
		Rectangle clipRotate;

		// calculate the rotated clip
		tempCardClipWidth = clip.width / 2;
		tempCardClipHeight = clip.height / 2;
		


		actualNewCardClipWidth = storyCardWidth / 2;
		actualNewCardClipHeight = storyCardHeight / 2;

		Rectangle clipTemp = new Rectangle(clip.x
				+ Math.round((float) (clip.width / 2 - actualNewCardClipWidth)), clip.y
				+ Math.round((float) (clip.height / 2 - actualNewCardClipHeight)), Math
				.round((float) actualNewCardClipWidth * 2), Math
				.round((float) actualNewCardClipHeight * 2));
		
		

			g.setFont(backSideFont);

			g.setAlpha(255);
			// leave that color as is! requirement from rally!
			g.setBackgroundColor(new Color(null, 254, 236, 138));

			g.translate(clipTemp.x + clipTemp.width / 2, clipTemp.y
					+ clipTemp.height / 2);
	
			g.rotate(aRotate);

			clipRotate = new Rectangle(-clipTemp.width / 2,
					-clipTemp.height / 2, clipTemp.width, clipTemp.height);
			
			g.fillRectangle(clipRotate);

			
			

			
			
			xOrig = clipTemp.x;
			yOrig = clipTemp.y;
			xRotate = xOrig - clipTemp.x - clipTemp.width / 2;
			yRotate = yOrig - clipTemp.y - clipTemp.height / 2;

			g.setForegroundColor(new Color(null, 100, 100, 100));// old
			g.drawRectangle(xRotate, yRotate, clipTemp.width, clipTemp.height);
			
			
			if(this.getARotate()!=0){
		         ImageDescriptor id=null;
		         
		         if(this.getTriangleColor().equalsIgnoreCase("white"))
				
				id = AbstractUIPlugin.imageDescriptorFromPlugin(
						PluginInformation.getPLUGIN_ID(),
						CardConstants.TRIANGLEWHITE);
		         
		         else if(this.getTriangleColor().equalsIgnoreCase("yellow"))
		        	 id = AbstractUIPlugin.imageDescriptorFromPlugin(
		 					PluginInformation.getPLUGIN_ID(),
		 					CardConstants.TRIANGLEBUTTON);
		        	 
		         else if(this.getTriangleColor().equalsIgnoreCase("green"))
		        	 id = AbstractUIPlugin.imageDescriptorFromPlugin(
		 					PluginInformation.getPLUGIN_ID(),
		 					CardConstants.TRIANGLEGREEN);
		         else if(this.getTriangleColor().equalsIgnoreCase("pink"))
		        	
		         id = AbstractUIPlugin.imageDescriptorFromPlugin(
		 					PluginInformation.getPLUGIN_ID(),
		 					CardConstants.TRIANGLEPINK);
		         
		         else if(this.getTriangleColor().equalsIgnoreCase("blue"))
			        	
			         id = AbstractUIPlugin.imageDescriptorFromPlugin(
			 					PluginInformation.getPLUGIN_ID(),
			 					CardConstants.TRIANGLEBLUE);
		         
		         else if(this.getTriangleColor().equalsIgnoreCase("yellow"))
			        	
			         id = AbstractUIPlugin.imageDescriptorFromPlugin(
			 					PluginInformation.getPLUGIN_ID(),
			 					CardConstants.TRIANGLEBUTTON);
		      

		         g.drawImage(id.createImage(), new Point(clipRotate.x
	        				+ this.storyCardWidth - 30,clipRotate.y+ this.storyCardHeight - 31));
			 }
			
			
		
			
	       

		} 	

	public void refreshFigureOrder() {
		IFigure parent = this.getParent();
		parent.remove(this);
		parent.add(this);

	}

	public Point getPointDescription() {
		return pointDescription;
	}

	public void setCurrentSideUp(int side) {
		this.currentSideUp = side;
	}

	public void setAcceptanceTest(AcceptanceTestDataObject data) {
		this.acceptTest = data;
	}

	public String getStoryCardColor() {
		return storyCardColor;
	}

	public void setStoryCardColor(String storyCardColor) {
		this.storyCardColor = storyCardColor;
	}

	public Font getStoryCardFont() {
		return this.DISPLAY_FONT;
	}

	public void setStoryCardFont(String storyCardFont, int size) {
		this.storyCardFont = storyCardFont;
		this.storyCardFontSize = size;

		if (this.DISPLAY_FONT != null)
			this.DISPLAY_FONT.dispose();
		this.DISPLAY_FONT = new Font(null, storyCardFont, size, 0);

	}

	// public void setStoryCardFont(Font font) {

	// this.sotryCardFont=storyCardFont;
	// this.storyCardFontSize=this.storyCardFontSize;
	// this.DISPLAY_FONT=new Font (null,storyCardFont,size,0);

	// }

	// clean up fonts and SWT stuff
	public void dispose() {
		this.DISPLAY_FONT.dispose();

	}

	public void setWidth(int width) {
		this.storyCardWidth = width;
	}

	public void setHeight(int height) {
		this.storyCardHeight = height;
	}

	public void setARotate(float aRotate) {
		this.aRotate = aRotate;
	}

	public float getARotate() {
		return this.aRotate;
	}

	public void setNameLabel(String nameLabelText) {
		this.nameLabelText = nameLabelText;
	}

	public void setMostLikelyLabel(String mostLikelyLabelText) {
		this.mostLikelyLabelText = mostLikelyLabelText;

	}

	public void setActualLabel(String actualLabelText) {
		this.actualLabelText = actualLabelText;
	}

	public void setRemainingLabel(String remainingLabelText) {
		this.remainingLabelText = remainingLabelText;
	}

	public void setDescriptionLabel(String descriptionLabelText) {
		this.descriptionLabelText = descriptionLabelText;
	}

	public void setURLLabel(String URLLabelText) {
		this.URLLabelText = URLLabelText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Rectangle getClip_Rotate() {
		return clip_Rotate;
	}

	public void setClip_Rotate(Rectangle clip_Rotate) {
		this.clip_Rotate = clip_Rotate;
	}

	public String getTriangleColor() {
		return triangleColor;
	}

	public void setTriangleColor(String triangleColor) {
		this.triangleColor = triangleColor;
	}

	public String getOwnerLabelText() {
		return ownerLabelText;
	}

	public void setOwnerLabelText(String ownerLabelText) {
		this.ownerLabelText = ownerLabelText;
	}



}

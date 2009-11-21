/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is the figure for the IterationCard object. 
 *								This is where all of the non UI elements are added to the figure. 
 *								This class contains a mous listener that should NOT be used for events that interact with 
 *								either the contorlers or the models. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.figure;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import persister.factory.Settings;


import cards.CardConstants;

public class IterationCardFigure extends Viewport {
   
	
	private IterationCardFigure figure;
	
	private static final Font DISPLAY_FONT = new Font(null,"",8,0); // Font for printing. RM-6/18/2007
	
   

    private String nameTitle = "Name: ";

    private String descTitle = "Description: ";

    private String endDateTitle = "End Date: ";
    
    private String startDateTitle = "Start Date: ";

    private String availableEffortTitle = "Available Effort: ";

    private String remainingEffortTitle = "Remaining Effort: ";

    private String nameField;

    private String descField;

    private String endDateField;
    
    private String startDateField;

    private String availableEffortField;

    private String remainingEffortField;

    private String bcField;

    private String bcLabel = "Best Case: ";

    private String wcField;

    private String wcLabel = "Worst Case: ";

    private String mlField;

    private String mlLabel = "Most Likely: ";

    private String aField;

    private String aLabel = "Actual: ";

    public IterationCardFigure() {
        this.figure = this;
        this.setLayoutManager(new FreeformLayout());
        this.setOpaque(true);

        this.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent e) {
                IterationCardFigure.this.refreshFigureOrder();
            }

            public void mouseReleased(MouseEvent me) {

            }

            public void mouseDoubleClicked(MouseEvent me) {

            }
        });
    }

    @Override
    @SuppressWarnings("static-access")
    protected void paintFigure(Graphics g) {
        Rectangle clip = this.getClientArea();

        int nameLine = CardConstants.ITERATIONFIGURENAMELINE, descLine = CardConstants.ITERATIONFIGUREDESCRIPTIONLINE, effortLine = CardConstants.ITERATIONFIGUREAVAILABLEEFFORTLINE, bcLine = CardConstants.ITERATIONFIGUREBESTCASELINE,
        // wcLine=CardConstants.ITERATIONFIGUREWORSTCASELINE;
        wcLine = CardConstants.ITERATIONFIGUREAVAILABLEEFFORTLINE;

        /**
         * Remember to divide the clip.width by 2 for the first label on the
         * line*
         */
        if (this.isOpaque()) {
            boolean nextEstimateLabelOnLeftSide = true;
            int numberOfEstimate = 0;
            int xLocationValue = 0;
            int yLocationValue = 0;
            
            g.setFont(DISPLAY_FONT);// Font for printing. RM-6/18/2007
            
           // g.setAlpha(255);
            g.setAlpha(155);
            g.setBackgroundColor(new Color(null, 197, 210, 231));
            
            g.setForegroundColor(new Color(null, 1, 1, 1));
            g.fillRectangle(clip);

            int firstIterationEstimateLineOffset = ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationFirstEstimateLineOffset
                    : CardConstants.WindowsIterationFirstEstimateLineOffset);
            int estimateIterationRightSideOffset = ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationEstimateRightSideOffset
                    : CardConstants.WindowsIterationEstimateRightSideOffset);
            int estimateIterationLeftSideOffset = ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationEstimateLeftSideOffset
                    : CardConstants.WindowsIterationEstimateLeftSideOffset);
            int estimateIterationLineIncrementValue = ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationEstimateLineIncrementValue
                    : CardConstants.WindowsIterationEstimateLineIncrementValue);

            g.setForegroundColor(new Color(null, 1, 1, 1));
            // Draw the Text on the cards.
            g.drawText(this.nameTitle + this.nameField, clip.x+5, clip.y + (nameLine - 15));
            g.drawText(this.endDateTitle + this.endDateField, clip.x + 200, clip.y + (nameLine - 15));
            g.drawText(this.startDateTitle + this.startDateField, clip.x + 200, clip.y + (nameLine)); 
            g.drawText(this.descTitle + this.descField, clip.x + 5, clip.y + (descLine - 15));
            g.drawText(this.availableEffortTitle + this.availableEffortField, clip.x + estimateIterationLeftSideOffset, clip.y + (effortLine - 15));
            g.drawText(this.mlLabel + this.mlField, clip.x + estimateIterationRightSideOffset, clip.y + (effortLine - 15));

            
            // show actual Estimate!
            if (Settings.isEstimate_actual()) {
                if (nextEstimateLabelOnLeftSide == false) {
                    xLocationValue = clip.x + estimateIterationRightSideOffset;
                    nextEstimateLabelOnLeftSide = true;
                }
                else {
                    // if this estimate gets drawn then it only appears on the
                    // right side of the card!
                    nextEstimateLabelOnLeftSide = false;
                    xLocationValue = clip.x + estimateIterationLeftSideOffset;
                }
                yLocationValue = clip.y + firstIterationEstimateLineOffset;
                g.drawText(this.aLabel + this.aField, xLocationValue, yLocationValue);
                numberOfEstimate++;
            }

            // Show BestCase Estimate!
            if (Settings.isEstimate_bestCase()) {
                if (nextEstimateLabelOnLeftSide == true) {
                    xLocationValue = clip.x + estimateIterationLeftSideOffset;
                    nextEstimateLabelOnLeftSide = false;
                }
                else {
                    xLocationValue = clip.x + estimateIterationRightSideOffset;
                    nextEstimateLabelOnLeftSide = true;
                }
                yLocationValue = clip.y + (firstIterationEstimateLineOffset + ((numberOfEstimate / 2) * estimateIterationLineIncrementValue));
                g.drawText(this.bcLabel + this.bcField, xLocationValue, yLocationValue);
                numberOfEstimate++;
            }

            // Show WorstCase Estimate!
            if (Settings.isEstimate_worstCase()) {
                if (nextEstimateLabelOnLeftSide == false) {
                    xLocationValue = clip.x + estimateIterationRightSideOffset;
                    nextEstimateLabelOnLeftSide = true;
                }
                else {
                    xLocationValue = clip.x + estimateIterationLeftSideOffset;
                    nextEstimateLabelOnLeftSide = false;
                }
                yLocationValue = clip.y + (firstIterationEstimateLineOffset + ((numberOfEstimate / 2) * estimateIterationLineIncrementValue));
                g.drawText(this.wcLabel + this.wcField, xLocationValue, yLocationValue);
                numberOfEstimate++;
            }

            // Show Remaining!
            if (Settings.isEstimate_remaining()) {
                if (nextEstimateLabelOnLeftSide == true) {
                    xLocationValue = clip.x + estimateIterationLeftSideOffset;
                    nextEstimateLabelOnLeftSide = false;
                }
                else {
                    xLocationValue = clip.x + estimateIterationRightSideOffset;
                    nextEstimateLabelOnLeftSide = true;
                }
                yLocationValue = clip.y + (firstIterationEstimateLineOffset + ((numberOfEstimate / 2) * estimateIterationLineIncrementValue));
                g.drawText(this.remainingEffortTitle + this.remainingEffortField, xLocationValue, yLocationValue);
                numberOfEstimate++;
            }

            // Always Draw the following lines for iteration
            g.setForegroundColor(new Color(null, 100, 100, 100));
            g.setLineDash(new int []{3,4});
            g.drawLine(clip.x, clip.y + nameLine, clip.x + clip.width, clip.y + nameLine);
            g.drawLine(clip.x, clip.y + descLine, clip.x + clip.width, clip.y + descLine);
            g.drawLine(clip.x, clip.y + effortLine, clip.x + clip.width, clip.y + effortLine);
            // Decide to draw the following lines according to the configuration
            if (numberOfEstimate > 0)
                g.drawLine(clip.x, clip.y + firstIterationEstimateLineOffset + estimateIterationLineIncrementValue, clip.x + clip.width, clip.y
                        + firstIterationEstimateLineOffset + estimateIterationLineIncrementValue);
            if (numberOfEstimate > 2)
                g.drawLine(clip.x, clip.y + firstIterationEstimateLineOffset + 2 * estimateIterationLineIncrementValue, clip.x + clip.width, clip.y
                        + firstIterationEstimateLineOffset + 2 * estimateIterationLineIncrementValue);

            // Draw the border for the Card.
            g.setForegroundColor(new Color(null, 100, 100, 100));
            g.drawRectangle(clip.x, clip.y, clip.width - 1, clip.height - 1);
        }
        else {

        }
    }

    /** ************************************************************************************************ */
    /** BestCase* */
    public void setBCText(String bestCase) {
        this.bcField = bestCase;
    }

    /** WorstCase* */
    public void setWCText(String worstCase) {
        this.wcField = worstCase;
    }

    /** Most Likely* */
    public void setMLText(String mostLikely) {
        this.mlField = mostLikely;
    }

    /** Actual* */
    public void setActText(String Actual) {
        this.aField = Actual;
    }

    private void refreshFigureOrder() {
        IFigure parent = this.getParent();
        parent.remove(this.figure);
        parent.add(this.figure);
    }

    // billy
    public void setNameField(String nameField) {
        this.nameField = nameField;
    }

    public void setDescField(String descField) {
        this.descField = descField;
    }

    public void setEndDateField(String endDateField) {
        this.endDateField = endDateField;
    }

    public void setAvailableEffortField(String availableEffortField) {
        this.availableEffortField = availableEffortField;
    }

    public void setRemainingEffortField(String remainingEffortField) {
        this.remainingEffortField = remainingEffortField;
    }

    public String getNameField() {
        return this.nameField;
    }

    public String getDescField() {
        return this.descField;
    }

    public String getEndDateField() {
        return this.endDateField;
    }

    public String getAvailableEffortField() {
        return this.availableEffortField;
    }

    public String setRemainingEffortField() {
        return this.remainingEffortField;
    }

	public String getStartDateField() {
		return startDateField;
	}

	public void setStartDateField(String startDateField) {
		this.startDateField = startDateField;
	}

}

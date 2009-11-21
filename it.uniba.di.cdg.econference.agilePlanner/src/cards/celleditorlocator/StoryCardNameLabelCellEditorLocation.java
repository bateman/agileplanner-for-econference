/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: Helper class to locate and place the direct edit cell. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.celleditorlocator;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Text;

import cards.CardConstants;
import cards.editpart.StoryCardEditPart;
import cards.figure.StoryCardFigure;
import cards.model.StoryCardModel;



public class StoryCardNameLabelCellEditorLocation implements CellEditorLocator {

    private StoryCardFigure storyFigure;
    private StoryCardEditPart storyEditPart;
    private StoryCardModel storyModel;

    public StoryCardNameLabelCellEditorLocation(StoryCardFigure figure, StoryCardModel model, StoryCardEditPart editPart) {
        setStoryCardFigure(figure);
        this.storyModel = model;
        this.storyEditPart = editPart;
    }

    public void relocate(CellEditor celleditor) {
    	ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) storyEditPart.getRoot();
		double ratio = root.getZoomManager().getZoom();
		

		
        Text text = (Text) celleditor.getControl();
        Rectangle rect = storyFigure.getClientArea();
        
        if(storyEditPart.getCastedModel().getColor().equals("transparent"))
        	text.setFont(new Font(null,"Rockwell",(22*((int)ratio)),0));
        	else
        		text.setFont(new Font(null,storyEditPart.getStoryCardFont(),(storyEditPart.getStoryCardFontSize()*((int)ratio)),0));
        
        storyFigure.translateToAbsolute(rect);
        org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
        rect.translate(trim.x, trim.y);
        rect.width = rect.width-15;
        rect.height = 13 + trim.height;
        
        if(storyEditPart.getStoryCardFontSize()<18)
       
            text.setBounds(rect.x+5
                    + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
                            : ((int)(CardConstants.WindowsStoryCardNameLabelX*ratio))), rect.y + ((int)(7*ratio)),rect.width,((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (this.storyEditPart.getStoryCardFontSize()+6)
                                    : ((int)(((this.storyEditPart.getStoryCardFontSize())+6)*ratio))));
        
        else if((storyEditPart.getStoryCardFontSize()>16 && storyEditPart.getStoryCardFontSize()<23)  )
        	text.setBounds(rect.x+5+((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardNameLabelX
                            : CardConstants.WindowsStoryCardNameLabelX), rect.y + 7, rect.width,  ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (this.storyEditPart.getStoryCardFontSize()+6)
                                    : (this.storyEditPart.getStoryCardFontSize()))+6);
       
        else if(storyEditPart.getStoryCardFontSize()>22 && storyEditPart.getStoryCardFontSize()<29)
        	 text.setBounds(rect.x+5+((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (this.storyEditPart.getStoryCardFontSize()+6)
                             : CardConstants.WindowsStoryCardNameLabelX), rect.y + 7, rect.width,         
                             ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardEstimateTextBoxHeight
                                     : (this.storyEditPart.getStoryCardFontSize()))+6);
    
        if(storyEditPart.getCastedModel().getColor().equals("transparent")){
        	text.setBounds(rect.x+10, rect.y , rect.width+10,         
        			storyFigure.getClientArea().height);
        }
    }

    protected StoryCardFigure getFigure() {
        return storyFigure;
    }

    protected void setStoryCardFigure(StoryCardFigure figure) {
        this.storyFigure = figure;
    }
}

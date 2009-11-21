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
import cards.celleditorlocator.listener.StoryCardLiveTextEditorListener;
import cards.editpart.StoryCardEditPart;
import cards.model.StoryCardModel;



public class StoryCardBestCaseLabelCellEditorLocation implements CellEditorLocator {

    private StoryCardModel storyModel;

    private StoryCardEditPart storyEditPart;

    public StoryCardBestCaseLabelCellEditorLocation(StoryCardModel model, StoryCardEditPart editPart) {

        this.storyModel = model;
        this.storyEditPart = editPart;
    }

    public void relocate(CellEditor celleditor) {
    	
    	ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) storyEditPart.getRoot();
		double ratio = root.getZoomManager().getZoom();
		
        celleditor.addListener(new StoryCardLiveTextEditorListener(storyModel, celleditor, CardConstants.BestCaseLabel));
        Text text = (Text) celleditor.getControl();
        text.setFont(new Font(null,storyEditPart.getStoryCardFont(),(storyEditPart.getStoryCardFontSize()*((int)ratio)),0));
        int xRelative = storyEditPart.getBestCaseLabel().getLocation().x - storyEditPart.getFigure().getBounds().x;
        int yRelative = storyEditPart.getBestCaseLabel().getLocation().y - storyEditPart.getFigure().getBounds().y;
        Rectangle rect = storyEditPart.getFigure().getClientArea();
        storyEditPart.getFigure().translateToAbsolute(rect);
        org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
        rect.translate(trim.x, trim.y);
        
        if(storyEditPart.getStoryCardFontSize()<12)
        text.setBounds(rect.x + xRelative, rect.y + yRelative,
                ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardEstimateTextBoxWidth
                        : CardConstants.WindowsStoryCardEstimateTextBoxWidth),

        		
                ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ?(this.storyEditPart.getStoryCardFontSize())
                        : CardConstants.WindowsStoryCardEstimateTextBoxHeight));
        else
        	 text.setBounds(rect.x + xRelative, rect.y + yRelative,

             		   ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? storyEditPart.getStoryCardFontSize()*2
                                : storyEditPart.getStoryCardFontSize()*2),
             		
                                ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ?(this.storyEditPart.getStoryCardFontSize())
                                        : (this.storyEditPart.getStoryCardFontSize())));
        
    }
}

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



public class StoryCardOwnerLabelCellEditorLocation implements CellEditorLocator {

	
	 private StoryCardModel storyModel;

	    private StoryCardEditPart storyEditPart;

	    public StoryCardOwnerLabelCellEditorLocation(StoryCardModel model, StoryCardEditPart editPart) {
	        this.storyModel = model;
	        this.storyEditPart = editPart;
	    }
	
	public void relocate(CellEditor celleditor) {
		
		ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) storyEditPart.getRoot();
		double ratio = root.getZoomManager().getZoom();
		
		
		celleditor.addListener(new StoryCardLiveTextEditorListener(storyModel, celleditor, CardConstants.STORYCARDOWNERLABEL));
        Text text = (Text) celleditor.getControl();
        text.setFont(new Font(null,storyEditPart.getStoryCardFont(),(storyEditPart.getStoryCardFontSize()*((int)ratio)),0));
        int xRelative = (int) ((storyEditPart.getOwnerLabel().getLocation().x - storyEditPart.getFigure().getBounds().x)*ratio);
        int yRelative = (int) ((storyEditPart.getOwnerLabel().getLocation().y - storyEditPart.getFigure().getBounds().y)*ratio);
        Rectangle rect = storyEditPart.getFigure().getClientArea();
        storyEditPart.getFigure().translateToAbsolute(rect);
        org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
        rect.translate(trim.x, trim.y);
    
        if(storyEditPart.getStoryCardFontSize()<12)
        	
            text.setBounds((rect.x + xRelative), (rect.y + yRelative),
                    ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? ((int)(CardConstants.MacStoryCardEstimateTextBoxWidth*ratio)+20)
                            : ((int)(CardConstants.WindowsStoryCardEstimateTextBoxWidth*ratio)+20)),

            		
                    ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (this.storyEditPart.getStoryCardFontSize())
                            : (int)(CardConstants.WindowsStoryCardEstimateTextBoxHeight*ratio)));
            else
            	 text.setBounds(rect.x + xRelative, rect.y + yRelative,

                 		   ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (storyEditPart.getStoryCardFontSize()*2+20)
                                    :(storyEditPart.getStoryCardFontSize()*2+20)),
                 		
                                    ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? (this.storyEditPart.getStoryCardFontSize())
                                            : (this.storyEditPart.getStoryCardFontSize())));
    
    
		
	}

}

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



public class StoryCardDescriptionLabelCellEditorLocation implements CellEditorLocator {

    private StoryCardFigure storyFigure;

    private StoryCardModel storyModel;

    private StoryCardEditPart storyEditPart;

    public StoryCardDescriptionLabelCellEditorLocation(StoryCardFigure figure, StoryCardModel model, StoryCardEditPart editPart) {
        setStoryCardFigure(figure);
        this.storyModel = model;
        this.storyEditPart = editPart;
    }

    public void relocate(CellEditor celleditor) {
    	ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) storyEditPart.getRoot();
		double ratio = root.getZoomManager().getZoom();

        Text text = (Text) celleditor.getControl();
        
        if(storyEditPart.getCastedModel().getColor().equals("transparent"))
        	text.setFont(new Font(null,"Rockwell",(22*((int)ratio)),0));
        else
        	text.setFont(new Font(null,storyEditPart.getStoryCardFont(),(storyEditPart.getStoryCardFontSize()*((int)ratio)),0));
       
        
        int xRelative = (int)((storyEditPart.getDescriptionLabel().getLocation().x - storyEditPart.getFigure().getBounds().x)*ratio);
        int yRelative = (int)((storyEditPart.getDescriptionLabel().getLocation().y - storyEditPart.getFigure().getBounds().y)*ratio);
        Rectangle rect = storyEditPart.getFigure().getClientArea();
        storyEditPart.getFigure().translateToAbsolute(rect);
        org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
        rect.translate(trim.x, trim.y);
        rect.width = rect.width + trim.width - 32;
        rect.height = rect.height + trim.height - (CardConstants.STORYCARDFIGUREDESCRIPTIONLINE - 3);
        if (yRelative > 64)
            text.setBounds(rect.x + xRelative, rect.y + yRelative, (int)(rect.width*ratio), (int)(rect.height*ratio) - 15);
        else
            if (yRelative < 40)
                text.setBounds(rect.x + xRelative, rect.y + yRelative, (int)(rect.width*ratio),(int)( rect.height*ratio) + 15);
            else
                text.setBounds(rect.x + xRelative, rect.y + yRelative, (int)(rect.width*ratio),(int)( rect.height*ratio));
        
        if(storyEditPart.getCastedModel().getColor().equals("transparent")){
        	text.setBounds(rect.x, rect.y , rect.width+10,         
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

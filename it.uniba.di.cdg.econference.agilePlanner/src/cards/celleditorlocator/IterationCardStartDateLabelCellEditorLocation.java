package cards.celleditorlocator;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

import cards.CardConstants;
import cards.editpart.IterationCardEditPart;
import cards.figure.IterationCardFigure;



public class IterationCardStartDateLabelCellEditorLocation implements CellEditorLocator {

    private IterationCardFigure iterationFigure;
	private IterationCardEditPart editpart;

    public IterationCardStartDateLabelCellEditorLocation(IterationCardFigure figure,IterationCardEditPart editpart) {
        setIterationCardFigure(figure);
        
        this.editpart = editpart;
    }

    public void relocate(CellEditor celleditor) {
    	
    	ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) editpart.getRoot();
		double ratio = root.getZoomManager().getZoom();
		
        Text text = (Text) celleditor.getControl();
        Rectangle rect = iterationFigure.getClientArea();
        iterationFigure.translateToAbsolute(rect);
        org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
        rect.translate(trim.x, trim.y);
        rect.width = rect.width
                + trim.width
                - ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) 
                		? CardConstants.MacIterationFigureStartDatePoint
                        : CardConstants.WindowsIterationFigureStartDatePoint);
        rect.height = 13 + trim.height;
        text.setBounds(rect.x+8
                + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) 
                		? CardConstants.MacIterationFigureStartDatePoint
                        : (int)(CardConstants.WindowsIterationFigureStartDatePoint*ratio)) - 5, (int)((rect.y + 20)*ratio), (int)(rect.width*ratio)-100, (int)(rect.height*ratio));
    }

    protected IterationCardFigure getFigure() {
        return iterationFigure;
    }

    protected void setIterationCardFigure(IterationCardFigure figure) {
        this.iterationFigure = figure;
    }
}

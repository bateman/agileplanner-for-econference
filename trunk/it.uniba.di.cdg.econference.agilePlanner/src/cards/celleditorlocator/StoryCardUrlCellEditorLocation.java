package cards.celleditorlocator;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

import cards.CardConstants;
import cards.celleditorlocator.listener.StoryCardLiveTextEditorListener;
import cards.editpart.StoryCardEditPart;
import cards.model.StoryCardModel;



public class StoryCardUrlCellEditorLocation implements CellEditorLocator {

    private StoryCardModel storyModel;

    private StoryCardEditPart storyEditPart;

    public StoryCardUrlCellEditorLocation(StoryCardModel model, StoryCardEditPart editPart) {
        this.storyModel = model;
        this.storyEditPart = editPart;
    }

    public void relocate(CellEditor celleditor) {
        celleditor.addListener(new StoryCardLiveTextEditorListener(storyModel, celleditor, CardConstants.STORYCARDURLLABEL));
        Text text = (Text) celleditor.getControl();
        int xRelative = storyEditPart.getUrlLabel().getLocation().x - storyEditPart.getFigure().getBounds().x;
        int yRelative = storyEditPart.getUrlLabel().getLocation().y - storyEditPart.getFigure().getBounds().y;
        Rectangle rect = storyEditPart.getFigure().getClientArea();
        storyEditPart.getFigure().translateToAbsolute(rect);
        org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
        rect.translate(trim.x, trim.y);
        text.setBounds(rect.x + xRelative, rect.y + yRelative, storyEditPart.getUrlLabel().getBounds().width, storyEditPart.getUrlLabel().getBounds().height);
    }
}

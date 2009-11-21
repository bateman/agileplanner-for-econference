/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required edit manager  for the StoryCard object
 *								This allows for the direct edit feature. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.editmanager;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import cards.CardConstants;
import cards.celleditorlocator.listener.StoryCardLiveTextEditorListener;
import cards.editpart.StoryCardEditPart;
import cards.figure.WrapLabel;



public class StoryCardDescriptionEditManager extends DirectEditManager {
    private StoryCardEditPart editpart;

    private int typeOfLabel;

    public StoryCardDescriptionEditManager(GraphicalEditPart source, int labelType, CellEditorLocator locator) {
        super(source, null, locator);
        this.typeOfLabel = labelType;
        this.editpart = (StoryCardEditPart) source;

    }

    @Override
    protected CellEditor createCellEditorOn(Composite composite) {
        TextCellEditor edit = new TextCellEditor(composite, SWT.WRAP | SWT.V_SCROLL);
        edit.addListener(new StoryCardLiveTextEditorListener(editpart.getCastedModel(), edit, typeOfLabel));
        return edit;
    }

    @Override
    protected void initCellEditor() {
        StoryCardEditPart editPart = (StoryCardEditPart) this.getEditPart();
        if (this.typeOfLabel == CardConstants.STORYCARDDESCRIPTIONLABEL) {
            this.getCellEditor().setValue(((WrapLabel) editPart.getDescriptionLabel()).getTextOrig());
        }
    }
}

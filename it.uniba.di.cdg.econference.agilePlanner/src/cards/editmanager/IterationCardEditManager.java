/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required manager for IterationCard  objects.
 *								This class is used for the direct edit feature. 
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
import cards.celleditorlocator.listener.IterationCardLiveTextEditorListener;
import cards.editpart.IterationCardEditPart;
import cards.figure.IterationCardFigure;



public class IterationCardEditManager extends DirectEditManager {
    private IterationCardEditPart editPart;

    private int typeOfLabel;

    public IterationCardEditManager(GraphicalEditPart source, int labelType, CellEditorLocator locator) {
        super(source, null, locator);
        this.typeOfLabel = labelType;
        this.editPart = (IterationCardEditPart) source;
    }

    @Override
    protected CellEditor createCellEditorOn(Composite composite) {
        TextCellEditor textCellEditor = new TextCellEditor(composite, SWT.SINGLE);
        textCellEditor.addListener(new IterationCardLiveTextEditorListener(editPart.getCastedModel(), textCellEditor, typeOfLabel));
        return textCellEditor;// TODO this SWT constant needs to be set so
    }

    @Override
    protected void initCellEditor() {
        IterationCardEditPart editPart = (IterationCardEditPart) this.getEditPart();
        if (this.typeOfLabel == CardConstants.ITERATIONCARDNAMELABEL) {
            this.getCellEditor().setValue(((IterationCardFigure) (editPart.getFigure())).getNameField());
        }
        else
            if (this.typeOfLabel == CardConstants.ITERATIONCARDDESCRIPTIONLABEL) {
                this.getCellEditor().setValue(((IterationCardFigure) (editPart.getFigure())).getDescField());
            }
            else
                if (this.typeOfLabel == CardConstants.ITERATIONCARDENDDATELABEL) {
                    this.getCellEditor().setValue(((IterationCardFigure) (editPart.getFigure())).getEndDateField());
                }
                else
                    if (this.typeOfLabel == CardConstants.ITERATIONCARDAVAILABLEEFFORT) {
                        this.getCellEditor().setValue(((IterationCardFigure) (editPart.getFigure())).getAvailableEffortField());
                    }
        
                    else
                    	if(this.typeOfLabel == CardConstants.ITERATIONCARDSTARTDATELABEL)
                    		this.getCellEditor().setValue(((IterationCardFigure) (editPart.getFigure())).getStartDateField());
    }
}
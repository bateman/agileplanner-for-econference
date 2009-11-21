/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required edit Manager for the StoryCard object.   
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



public class StoryCardEditManager extends DirectEditManager {

    private StoryCardEditPart editPart;

    private int typeOfLabel;

    public StoryCardEditManager(GraphicalEditPart source, int labelType, CellEditorLocator locator) {
        super(source, null, locator);
        this.typeOfLabel = labelType;
        this.editPart = (StoryCardEditPart) source;
    }

    @Override
    protected CellEditor createCellEditorOn(Composite composite) {
        
    	TextCellEditor textCellEditor ;
//        if(this.editPart.getCastedModel().getColor().equals("transparent"))
//        	 textCellEditor = new TextCellEditor(composite, SWT.MULTI|SWT.LEFT);
//        else
        	 textCellEditor = new TextCellEditor(composite, SWT.SINGLE);
       
        textCellEditor.addListener(new StoryCardLiveTextEditorListener(editPart.getCastedModel(), textCellEditor, typeOfLabel));

        return textCellEditor;
    }

    @Override
    protected void initCellEditor() {
        StoryCardEditPart editPart = (StoryCardEditPart) this.getEditPart();
        if (this.typeOfLabel == CardConstants.STORYCARDNAMELABEL) {
        		this.getCellEditor().setValue(editPart.getNameLabel().getText());
        }else if (this.typeOfLabel == CardConstants.STORYCARDACTUALLABEL) {
        	this.getCellEditor().setValue(editPart.getActualLabel().getText());
        }else if (this.typeOfLabel == CardConstants.STORYCARDBESTCASELABEL) {
        	this.getCellEditor().setValue(editPart.getBestCaseLabel().getText());
        }else if (this.typeOfLabel == CardConstants.STORYCARDMOSTLIKELYLABEL) {
        	this.getCellEditor().setValue(editPart.getMostLikelyLabel().getText());
        }else if (this.typeOfLabel == CardConstants.STORYCARDWORSTCASELABEL) {
        	this.getCellEditor().setValue(editPart.getWorstCaseLabel().getText());
        }else if (this.typeOfLabel == CardConstants.STORYCARDREMAININGLABEL) {
        	this.getCellEditor().setValue(editPart.getRemainingLabel().getText());
        }else if(this.typeOfLabel == CardConstants.STORYCARDURLLABEL){
        	this.getCellEditor().setValue(editPart.getUrlLabel().getText());
        }else if(this.typeOfLabel == CardConstants.STORYCARDOWNERLABEL ){
        	this.getCellEditor().setValue(editPart.getOwnerLabel().getText());
        }

    }
}

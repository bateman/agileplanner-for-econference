package cards.editmanager;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import cards.editpart.StoryCardEditPart;



public class StoryCardAcceptanceTestEditManager extends DirectEditManager {

	
    private StoryCardEditPart editpart;

    private int typeOfLabel;

    public StoryCardAcceptanceTestEditManager(GraphicalEditPart source, int labelType, CellEditorLocator locator) {
        super(source, null, locator);
        this.typeOfLabel = labelType;
        this.editpart = (StoryCardEditPart) source;

    }

    @Override
    protected CellEditor createCellEditorOn(Composite composite) {
        TextCellEditor edit = new TextCellEditor(composite, SWT.H_SCROLL | SWT.V_SCROLL);
        return edit;
    }

    @Override
    protected void initCellEditor() {
        StoryCardEditPart editPart = (StoryCardEditPart) this.getEditPart();
        this.getCellEditor().setValue("Wiki Text");
    }

}

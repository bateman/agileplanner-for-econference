package applicationWorkbench.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;


import cards.CardConstants;
import cards.commands.StoryCardCreateCommand;
import cards.editpart.StoryCardEditPart;
import fitintegration.PluginInformation;

public abstract class AbstractColorAction extends Action implements
		ISelectionListener, IWorkbenchAction {

	private IWorkbenchWindow window;
	private String color;
	private boolean isStoryCardSelected = false;
	private List currentSelection;

	public IWorkbenchWindow getWindow() {
		return window;
	}

	public void setWindow(IWorkbenchWindow window) {
		this.window = window;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public boolean isStoryCardSelected() {
		return isStoryCardSelected;
	}

	public void setStoryCardSelected(boolean isStoryCardSelected) {
		this.isStoryCardSelected = isStoryCardSelected;
	}

	public List getCurrentSelection() {
		return currentSelection;
	}

	public void setCurrentSelection(List currentSelection) {
		this.currentSelection = currentSelection;
	}

	public AbstractColorAction(IWorkbenchWindow window, String color) {		
	        this.window = window;
	        this.color = color;
	        window.getSelectionService().addSelectionListener(this);
	        window.getSelectionService().getSelection();
	        setImageDescriptor(getColorImageDescriptor(color)); 	        
	    }
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).getFirstElement() instanceof StoryCardEditPart) {
			setCurrentSelection(((StructuredSelection)selection).toList());
            setStoryCardSelected(true);
            setEnabled(true);
            
            
        } else {
            setEnabled(true);
            setStoryCardSelected(false);
		}
	}

	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);

	}
	
    public void run() {	
    	StoryCardCreateCommand.setInitialStoryCardColor(color);
       	if(isStoryCardSelected){
    	   colorStoryCard();
       	}
    }
    
    private void colorStoryCard() {
		List<StoryCardEditPart> storyCards = new ArrayList<StoryCardEditPart>();	    	
    	
		for(Object o: this.currentSelection){
			if(o instanceof StoryCardEditPart)
				storyCards.add((StoryCardEditPart) o);
		}
		
		for(StoryCardEditPart storyCard: storyCards){
			storyCard.getCastedModel().setColorChange(color);
			storyCard.refreshVisuals();
			currentSelection=null;
		}

	}

   ImageDescriptor getColorImageDescriptor(String color){
    	ImageDescriptor result = null;
    	result = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(),CardConstants.getColorCardsIconLocation(color));
    	return result;
    }
    

}

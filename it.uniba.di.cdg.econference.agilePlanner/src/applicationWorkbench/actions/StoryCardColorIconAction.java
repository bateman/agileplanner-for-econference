package applicationWorkbench.actions;

import java.util.ArrayList;
import java.util.EventListener;
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

	

public class StoryCardColorIconAction extends AbstractColorAction {


	public static final String ID = "applicationWorkbench.actions.storyCardColorIconAction";	

	
	public StoryCardColorIconAction(IWorkbenchWindow window, String color) {		
		super(window, color);
		setId(ID);
	}	
	   
}



package applicationWorkbench.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import applicationWorkbench.uielements.PersisterConnectDialog;


import cards.CardConstants;
import cards.commands.SynchronizeProjectCommand;
import fitintegration.PluginInformation;

public class SynchronizeProjectAction extends Action implements /*PropertyChangeListener,*/
		 IWorkbenchAction{

	IWorkbenchWindow window;
	public static final String ID = "applicationWorkbench.actions.synchronizeProjectAction";

	public SynchronizeProjectAction(IWorkbenchWindow window) {
		setId(ID);
		this.window = window;
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				PluginInformation.getPLUGIN_ID(), CardConstants.SyncIcon));
		setToolTipText("Synch with Rally");
	}

	public void dispose() {				
	}

	public void run(){		
		if(PersisterConnectDialog.getRallyUserForSynch()!= null)
			new SynchronizeProjectCommand();
		else
			login();
	}

	 private boolean login() {
	        PersisterConnectDialog pcDialog = new PersisterConnectDialog(null, true, this.window, true);
	        return pcDialog.open() == Window.OK;
	    }
}



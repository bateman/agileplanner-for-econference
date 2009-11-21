package applicationWorkbench.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cards.CardConstants;
import fitintegration.PluginInformation;

import applicationWorkbench.Editor;
import applicationWorkbench.uielements.ConfigurationDialog;

public class HelpMenuAction extends Action implements PropertyChangeListener{
	 
	public static final String ID = "applicationWorkbench.actions.HelpMenuAction";

	
	public HelpMenuAction(){
		setId(ID);
		setText("Help");
	}
	
	  public void run() {
		  org.eclipse.swt.program.Program.launch("http://ase.cpsc.ucalgary.ca/ase/index.php/AgilePlanning/UserManual");  
	    }
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}

}

package applicationWorkbench.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import applicationWorkbench.Editor;
import applicationWorkbench.uielements.ConfigurationDialog;

import cards.CardConstants;
import fitintegration.PluginInformation;

public class ConfigureAction extends Action implements PropertyChangeListener{
	
    public static final String ID = "applicationWorkbench.actions.ConfigureAction";

    Editor editor;
    IWorkbenchWindow window;
    
    public ConfigureAction(IWorkbenchWindow window) {
    	IWorkbenchWindow win2 = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    	this.window = window;
    	setId(ID);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.ConfigureIcon));
        this.setToolTipText("Configure...");
    }


    public void run() {
    	this.editor = (Editor)window.getActivePage().getActiveEditor();
    	ConfigurationDialog cnfig = new ConfigurationDialog(null,this.editor, this.window);
        cnfig.open();
          
    }
    
	protected boolean calculateEnabled() {
		return true;
	}

	public void propertyChange(PropertyChangeEvent arg0) {
	}
		


}

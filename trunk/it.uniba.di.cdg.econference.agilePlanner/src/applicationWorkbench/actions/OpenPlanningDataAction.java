package applicationWorkbench.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;


import cards.CardConstants;
import fitintegration.PluginInformation;



/**
 * Currently this class is unused. Was used by ActionbarAdvisor before. Now Extensions are used for this Button.
 * 
 */
public class OpenPlanningDataAction extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = "rallydemogef.actions.openPlanningData";

    private final IWorkbenchWindow window;

    public OpenPlanningDataAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("Open");
        setToolTipText("Open Existing Planning Data");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.OpenPlanningDataIcon));
        window.getSelectionService().addSelectionListener(this);
    }

    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    public void run() {
    	window.getActivePage();
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {

            selection = (IStructuredSelection) selection;
            setEnabled(true);
        }
        else
            setEnabled(false);
    }
}

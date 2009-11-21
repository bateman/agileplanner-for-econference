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

public class CleanUpPlanningObjectsAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;

    public static final String ID = "rallydemogef.actions.cleanupPlanningObjects";

    public CleanUpPlanningObjectsAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&Clean up Planning Objects");
        setToolTipText("Organizes Iterations by End Date and organizes StoryCard by Rank.");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.CleanUpPlanningObjectsIcon));
        window.getSelectionService().addSelectionListener(this);
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {

            selection = (IStructuredSelection) selection;
            setEnabled(true);
        }
        else
            setEnabled(false);
    }

    public void run() {
    }

    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

}
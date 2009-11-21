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

public class ArangeIterationsByEndDateAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;

    public static final String ID = "rallydemogef.actions.arangeIterationsByEndDate";

    public ArangeIterationsByEndDateAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&Arange Iterations By End Date");
        setToolTipText("Arrange all the Iterations by their End Dates and organizes the Backlog(s)");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.ArrangeIterationsByEndDateIcon));
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
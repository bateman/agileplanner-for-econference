package applicationWorkbench.actions;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import applicationWorkbench.PathEditorInput;

import cards.CardConstants;
import filesystemaccess.FileSystemUtility;
import fitintegration.PluginInformation;

/**
 * Currently this class is unused. Was used by ActionbarAdvisor before. Now Extensions are used for this Button.
 *
 */
public class NewDocumentAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;

    public static final String ID = "rallydemogef.actions.openPlanningData";

    public NewDocumentAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("New");
        setToolTipText("Create new document for planning ");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.NewPlanningDataIcon));
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

    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    public void run() {
        IWorkbenchPage page;
        page = window.getActivePage();
        page.closeEditor(page.getActiveEditor(), true);

        FileSystemUtility utility = FileSystemUtility.getFileSystemUtility();

        IEditorInput input = createEditorInput(new File("C:/AgilePlanner.xml"));
        String editorId = "RallyDemoGEF.Editor";
        try {
            page.openEditor(input, editorId);
        }
        catch (PartInitException e) {
        	util.Logger.singleton().error(e);
        }

    }

    private IEditorInput createEditorInput(File file) {
        IPath path = new Path(file.getAbsolutePath());
        PathEditorInput input = new PathEditorInput(path);
        return input;
    }

}

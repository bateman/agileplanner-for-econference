package applicationWorkbench.actions;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

import persister.factory.PersisterFactory;
import persister.factory.Settings;
import applicationWorkbench.Editor;
import applicationWorkbench.PathEditorInput;
import cards.model.ProjectModel;

public class OpenProjectAction extends Action implements ISelectionListener,IWorkbenchWindowActionDelegate {
	private IWorkbenchWindow window;
    public final static String ID = "applicationWorkbench.actions.openProject";
    private String localDir;
    private ProjectModel projectModel;
    
    
    public OpenProjectAction (IWorkbenchWindow window){
    	 this.window = window;
    	    setId(ID);
    	    setText("&Open Project");
    	    setToolTipText("Open Project...");
    	    
    	    window.getSelectionService().addSelectionListener(this);

    }
    
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
        }
    
    
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void run() {
		FileDialog dialog = new FileDialog(new Shell(), SWT.NONE);

		String[] extensions = { "*.xml", "*.XML", "*.*" };
		dialog.setFilterExtensions(extensions);
		
		@SuppressWarnings("unused")
		String projectPath=dialog.open();
		
		String projectName=dialog.getFileName();
		localDir=dialog.getFilterPath();
		Settings.setProjectName(dialog.getFileName().replaceAll(".xml", ""));
		Settings.setProjectLocationLocalMode(localDir);
		if (!projectName.equals(""))
			openProject(projectName);	
		
	}
	
	
	
	private void openProject(String projectName){
		this.projectModel = ((Editor)window.getActivePage().getActiveEditor()).getModel();
		resetEditor(window, projectModel, projectName);
	}
	
	public static IEditorInput resetEditor(IWorkbenchWindow window,ProjectModel project, String projectName) {

		IWorkbenchPage page;
		page = window.getActivePage();
		((Editor) page.getActiveEditor()).setDataCallback(null);
		if (PersisterFactory.getPersister() != null)
			PersisterFactory.deletePersister();
		page.closeEditor(page.getActiveEditor(), true);

		//new editor
		IEditorInput neweditor = createEditorInput(new File(projectName));
		
		String editorId = "RallyDemoGEF.Editor";
		try {
			//page.getActiveEditor().dispose();
			page.openEditor(neweditor, editorId);
			/*String titleTab;
			titleTab = project.getProjectDataObject().getName()
					+ ":"
					+ Settings.getInitialDis();
			*/
			String title=projectName;
			((Editor) page.getActiveEditor()).updateTitleTab(title);
		} catch (PartInitException e) {

			util.Logger.singleton().error(e);
		}

		return neweditor;
	}
	
	private static IEditorInput createEditorInput(File file) {
		IPath path = new Path(file.getAbsolutePath());
		PathEditorInput input = new PathEditorInput(path);
		return input;
	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		this.window = window;
	}


	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		
	}

	public void run(IAction action) {
		// TODO Auto-generated method stub
		
	}

}

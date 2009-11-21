package applicationWorkbench.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import persister.factory.PersisterFactory;
import persister.factory.Settings;
import applicationWorkbench.Editor;
import applicationWorkbench.PathEditorInput;
import cards.model.ProjectModel;
public class NewProjectAction extends Action implements ISelectionListener {

	private IWorkbenchWindow window;
	public final static String ID = "applicationWorkbench.actions.NewProject";
	private ProjectModel projectModel;
	public NewProjectAction(IWorkbenchWindow window){
		this.window = window;

		setId(ID);
		setText("&New Project");
		setToolTipText("New Project");

		window.getSelectionService().addSelectionListener(this);
	}


	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}


	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

	public void run() {
		try {
			filesystemaccess.FileSystemUtility.createEmptyFile();
			File f=new File(".");
			Settings.setProjectName("EmptyProject");
			Settings.setProjectLocationLocalMode(f.getCanonicalPath());
			projectModel = ((Editor)window.getActivePage().getActiveEditor()).getModel();
			resetEditor(window, projectModel);

		}catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static IEditorInput resetEditor(IWorkbenchWindow window,ProjectModel project) {

		IWorkbenchPage page;
		page = window.getActivePage();
		((Editor) page.getActiveEditor()).setDataCallback(null);
		if (PersisterFactory.getPersister() != null)
			PersisterFactory.deletePersister();
		page.closeEditor(page.getActiveEditor(), true);

		//new editor
		IEditorInput neweditor = createEditorInput(new File(""));

		String editorId = "RallyDemoGEF.Editor";
		try {
			//page.getActiveEditor().dispose();
			page.openEditor(neweditor, editorId);
			//String titleTab;
			//titleTab = project.getProjectDataObject().getName();
					//+ ":"
					//+ Settings.getInitialDis();
			 

			((Editor) page.getActiveEditor()).updateTitleTab("EmptyProject.xml");
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

}

/*Only needed, if product is started as complete eclipse workbench with ide, to open a new 
 * AP-Editor. According plugin-extension is need as well.*/

package applicationWorkbench.actions;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import applicationWorkbench.PathEditorInput;

import filesystemaccess.FileSystemUtility;


public class NewEditorAction implements IWorkbenchWindowActionDelegate{

	public void setActiveEditor(IAction action, IEditorPart targetEditor) {
	}

public void run(IAction action) {
		
		createEditor();		
	}

	public void selectionChanged(IAction action, ISelection selection) {
		
	}
	
	 private void createEditor(){
    	 FileSystemUtility utility = FileSystemUtility.getFileSystemUtility();
         IWorkbenchPage page = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();;
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

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
	}

}

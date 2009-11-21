package applicationWorkbench.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import applicationWorkbench.uielements.PersisterConnectDialog;

public class ServerConnectionAction extends Action implements ISelectionListener{

	private IWorkbenchWindow window;
	private boolean existsOpenEditor = true;
	public final static String ID = "applicationWorkbench.actions.ServerConnection";
	public ServerConnectionAction(IWorkbenchWindow window){
		this.window = window;

		setId(ID);
		setText("&Server Connection");
		setToolTipText("Server Connection");

		window.getSelectionService().addSelectionListener(this);
	}


	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
	
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	public void run() {
		if (login()) {
			@SuppressWarnings("unused")
			String editorId = "RallyDemoGEF.Editor";
			@SuppressWarnings("unused")
			IEditorInput neweditor = null;

		}
	}
	
	private boolean login() {
		PersisterConnectDialog pcDialog = null;
		if (existsOpenEditor)
			pcDialog = new PersisterConnectDialog(null, window, false);
		else pcDialog = new PersisterConnectDialog(null, true, window, false);

		if (pcDialog.open() != Window.OK) {
			return false;
		}
		else {
			return true;
		}
	}

}

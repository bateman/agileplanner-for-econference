package applicationWorkbench.actions;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.WorkbenchException;

import cards.model.ProjectModel;

import persister.factory.PersisterFactory;
import persister.factory.Settings;

import applicationWorkbench.Editor;
import applicationWorkbench.PathEditorInput;
import applicationWorkbench.uielements.PersisterConnectDialog;

public class OpenProjectActionDelegate implements
IWorkbenchWindowActionDelegate {

	private IWorkbenchWindow window;
	private IWorkbench w;
	public static boolean localMode=false;
	private boolean existsOpenEditor = true;

	public void dispose() {
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void run(IAction action) {
		final IWorkbenchPage page = window.getActivePage();
		if (page.getActiveEditor() == null) existsOpenEditor = false;
		else existsOpenEditor = true;
		final Shell dialog = new Shell(window.getShell(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("How to use AgilePlanner");
		dialog.setBounds(350, 200, 300, 250);
		Label l=new Label(dialog, SWT.NULL);
		l.setText("Choose how to use AgilePlanner:");
		l.setBounds(70, 30, 180, 20);
		final Button localButton=new Button(dialog,SWT.RADIO);
		localButton.setText("Local Mode");
		localButton.setSelection(true);
		localButton.setBounds(100, 50, 100, 50);
		Button serverButton=new Button(dialog,SWT.RADIO);
		serverButton.setText("Server Mode");
		serverButton.setBounds(100, 80, 100, 70);
		Button ok=new Button(dialog, SWT.PUSH);
		ok.setText("OK");
		ok.setBounds(50, 150, 85, 30);
		Button cancel=new Button(dialog, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setBounds(155, 150, 85, 30);
		cancel.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {		
			}
			public void widgetSelected(SelectionEvent e) {
				dialog.dispose();
			}
		});
		dialog.open();
		ok.addSelectionListener(new SelectionListener(){
			public void widgetDefaultSelected(SelectionEvent e) {
			}
			public void widgetSelected(SelectionEvent e) {
				if (localButton.getSelection()){
					dialog.dispose();
					localMode=true;
					w=window.getWorkbench();
					try {
						w.showPerspective("RallyDemoGEF.perspective", window);
						filesystemaccess.FileSystemUtility.createEmptyFile();
						String editorId = "RallyDemoGEF.Editor";
						File f=new File(".");
						Settings.setProjectName("EmptyProject");
						Settings.setProjectLocationLocalMode(f.getCanonicalPath());
						IEditorInput neweditor = createEditorInput(new File(""));
						page.openEditor(neweditor, editorId);
						((Editor) page.getActiveEditor()).updateTitleTab("EmptyProject.xml");
					} catch (WorkbenchException ex) {
						util.Logger.singleton().error(ex);
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else{
					dialog.dispose();
					localMode=false;
					w=window.getWorkbench();
					try {
						w.showPerspective("RallyDemoGEF.perspective", window);
						filesystemaccess.FileSystemUtility.createEmptyFile();
						String editorId = "RallyDemoGEF.Editor";
						File f=new File(".");
						Settings.setProjectName("EmptyProject");
						Settings.setProjectLocationLocalMode(f.getCanonicalPath());
						IEditorInput neweditor = createEditorInput(new File(""));
						page.openEditor(neweditor, editorId);
						((Editor) page.getActiveEditor()).updateTitleTab("EmptyProject.xml");
					} catch (WorkbenchException ex) {
						util.Logger.singleton().error(ex);
					}catch (IOException e1) {
						e1.printStackTrace();
					}
					if (login()) {
						String editorId = "RallyDemoGEF.Editor";
						IEditorInput neweditor = null;
					}
				}
			}
		}
		);
	}

	public static IEditorInput resetEditor(IWorkbenchWindow window,ProjectModel project) {
		IWorkbenchPage page;
		page = window.getActivePage();
		((Editor) page.getActiveEditor()).setDataCallback(null);
		if (PersisterFactory.getPersister() != null)
			PersisterFactory.deletePersister();
		page.closeEditor(page.getActiveEditor(), true);
		IEditorInput neweditor = createEditorInput(new File("EmptyProject.xml"));

		String editorId = "RallyDemoGEF.Editor";
		try {
			page.openEditor(neweditor, editorId);
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

	public void selectionChanged(IAction action, ISelection selection) {
	}

}

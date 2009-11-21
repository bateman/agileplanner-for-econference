package applicationWorkbench.actions;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
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

public class SaveAsAction extends Action implements ISelectionListener {

	private IWorkbenchWindow window;
	public final static String ID = "applicationWorkbench.actions.SaveAs";
	private ProjectModel projectModel;
	private String[] extensions = { "*.xml", "*.XML" };
	private FileWriter savedFile;
	private FileReader currentFile;
	private File fileToSave;
	public SaveAsAction (IWorkbenchWindow window){
		this.window = window;
		setId(ID);
		setText("&Save as");
		setToolTipText("Save As...");

		window.getSelectionService().addSelectionListener(this);
	}

	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}


	public void run() {


		String currentFileName=Settings.getProjectName();
		String currentFileDir=Settings.getProjectLocationLocalMode();
		System.out.println(currentFileDir+"\\"+currentFileName);
		try {
			currentFile=new FileReader(currentFileDir+"\\"+currentFileName+".xml");

			openSaveDialog();

			BufferedReader reader = new BufferedReader(currentFile);
			String str;
			StringWriter w = new StringWriter();

			while ((str = reader.readLine()) != null) {
				w.write(str);
			} 
			w.close();
			reader.close();

			savedFile.write(w.toString());
			savedFile.close();

			System.out.println(fileToSave.getAbsolutePath());
			System.out.println(fileToSave.getCanonicalPath());
			System.out.println(fileToSave.getName());
			System.out.println(fileToSave.getParentFile());
			Settings.setProjectName(fileToSave.getName().replace(".xml", ""));
			Settings.setProjectLocationLocalMode(fileToSave.getParentFile().toString());

			projectModel = ((Editor)window.getActivePage().getActiveEditor()).getModel();
			resetEditor(window, projectModel,fileToSave);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void openSaveDialog(){

		String path = null;
		String fileName;

		FileDialog dialog = new FileDialog(new Shell(), SWT.SAVE);      
		dialog.setFilterPath("/");
		dialog.setFilterExtensions(this.extensions);

		if (dialog.open() != null) {
			path = dialog.getFilterPath();
			try{
				fileName = dialog.getFileName();
				String completeFilename =  path+File.separator + fileName;
				fileToSave = new File(completeFilename);
				if (fileToSave.exists()) {
					MessageBox messageBox = new MessageBox(new Shell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
					messageBox.setMessage(" Already exists, \n Do you want to replace it?");
					messageBox.setText("Save As");
					if (messageBox.open() != SWT.OK) {
						return;
					}
					else {
						fileToSave.delete();
						savedFile=new FileWriter(fileToSave);                  
					}
				}
				else {
					savedFile=new FileWriter(fileToSave);                  
				}

			}catch (IOException ex){ex.printStackTrace();}
		}
	}


	public static IEditorInput resetEditor(IWorkbenchWindow window,ProjectModel project,File f) {

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
			page.openEditor(neweditor, editorId);
			((Editor) page.getActiveEditor()).updateTitleTab(f.getName());
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



	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub

	}

}

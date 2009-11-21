package applicationWorkbench.uielements;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;

import applicationWorkbench.Editor;
import applicationWorkbench.PathEditorInput;
import cards.CardConstants;
import cards.model.ProjectModel;
import persister.Keystroke;
import persister.NotConnectedException;
import persister.data.Project;
import persister.factory.PersisterFactory;
import persister.factory.Settings;

public class ConfigurationDialog extends Dialog {

	private Shell parent;
	ProjectModel projectModel;
	private static int mselect = 2;

	private TabFolder tabFolderInDialog;
	private Editor editor;
	private final IWorkbenchWindow window;

	private TabItem legendTab;
	private TabItem userTab;
	private TabItem storyCardTab;
	//

	public ConfigurationDialog(Shell parentShell, Editor editor, IWorkbenchWindow window) {

		super(parentShell);
		parent = parentShell;
		this.editor = editor;
		this.projectModel = this.editor.getModel();
		
		this.window = window;

	}

	@Override
	protected void buttonPressed(int buttonId) {

		super.buttonPressed(buttonId);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CardConstants.APPLICATIONNAME + "Configuration");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);

		FillLayout fillLayout = new FillLayout(SWT.NULL);

		composite.setLayout(fillLayout);

		final TabFolder tabFolder = new TabFolder(composite, SWT.NONE);

		tabFolderInDialog = tabFolder;

		setStoryCardTab(new StoryCardTab().create(tabFolder, projectModel));
		setLegendTab(new LegendTab().create(tabFolder, projectModel));
		setUserTab(new UsersTab().create(tabFolder, projectModel));
		
		composite.pack();


		return composite;
	}

	@Override
	protected void okPressed() {

		super.okPressed();
		try {
			PersisterFactory.getPersister().updateLegend(projectModel.getProjectDataObject().getLegend());
//			PersisterFactory.getPersister().updateOwners(projectModel.getProjectDataObject().getOwnerChildren());
		} catch (NotConnectedException e) {
			util.Logger.singleton().error(e);
		}
		if (Settings.getPersisterType().equalsIgnoreCase("LOCAL"));
		
		
		resetEditor(window,this.projectModel);
	}

	/**
	 * use this method only when need to reset pallette, otherwise use method like resetPersisterInCurrentEditor from 
	 * PersisterConnectDialog class --- Harminder 
	 * @param window
	 * @return
	 */
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
			//System.out.println(Settings.getProjectName());
		
			//String titleTab;
			//titleTab=Settings.getProjectName().replace(".xml", "");
			//titleTab = project.getProjectDataObject().getName()
				//	+ ":"
					//+ Settings.getInitialDis();
			
			((Editor) page.getActiveEditor()).updateTitleTab(Settings.getProjectName());
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

	public void broughtToFront(long id) {
	}

	@Override
	public boolean close() {
		return super.close();
	}

	public void deletedRemoteMouse(long id) {
	}

	public Combo getCombo() {
		return null;
	}

	public Button getLoadButton() {
		return null;
	}

	public void sendKeyEventLocally(Keystroke ks) {
	}

	public void setCombo(Combo combo) {

	}

	public TabItem getLegendTab() {
		return legendTab;
	}

	public void setLegendTab(TabItem legendTab) {
		this.legendTab = legendTab;
	}

	public TabItem getUserTab() {
		return userTab;
	}

	public void setUserTab(TabItem userTab) {
		this.userTab = userTab;
	}

	public TabItem getStoryCardTab() {
		return storyCardTab;
	}

	public void setStoryCardTab(TabItem storyCardTab) {
		this.storyCardTab = storyCardTab;
	}

}//eoc

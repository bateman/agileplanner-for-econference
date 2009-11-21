package applicationWorkbench.uielements;

import java.io.File;
import java.text.SimpleDateFormat;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import persister.NotConnectedException;
import persister.distributed.ClientCommunicator;
import persister.distributed.ServerCommunicator;
import persister.factory.PersisterFactory;
import persister.factory.Settings;
import persister.local.AsynchronousLocalPersister;

import applicationWorkbench.Editor;
import applicationWorkbench.PathEditorInput;

import cards.CardConstants;
import cards.commands.SynchronizeProjectCommand;
import cards.model.ProjectModel;
import fitintegration.PluginInformation;

public class PersisterConnectDialog extends Dialog {

	private IWorkbenchWindow window;

	private static boolean isLocalServerStarted = false;
	private static boolean isRallyConnected = false;
	public static long threadId;

	private static String rallyUserForSynch;
	
	private Combo comboProject;

	private Combo comboProjectDis;

	private TabFolder tabFolderInDialog;

	private Text txtPortDis; // project location for distributed

	private String txtServerLocalDir1;

	private Text txtServerLocalPort;

	private Text txtServerLocalProjectName;

	private Text txtUrlDis; // project location for distributed

	private Text txtUserInitialDis;

	private Button buttonServerLocalStart;

	private static Text txtRallyPasswordDis;

	private static Text txtRallyUserNameDis;

	private Combo rallyComboProjectDis;
	
	private Combo rallyComboServerDis;

	final SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy");

	private Button buttonRallyLogin;
	
	private boolean synchRally;
	private ProjectModel projectModel;

	public PersisterConnectDialog(Shell parentShell, IWorkbenchWindow window, boolean synchRally) {
		super(parentShell);
		this.window = window;
		this.synchRally = synchRally;
		this.projectModel = ((Editor)window.getActivePage().getActiveEditor()).getModel();
	}

	public PersisterConnectDialog(Shell parentShell, boolean isNew,
			IWorkbenchWindow window, boolean synchRally) {
		super(parentShell);
		this.window = window;
		this.synchRally = synchRally;
		this.projectModel = ((Editor)window.getActivePage().getActiveEditor()).getModel();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		super.buttonPressed(buttonId);
	}

	@Override
	public boolean close() {
		return super.close();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell
				.setText(CardConstants.APPLICATIONNAME + "Persister Connection");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);

		FillLayout fillLayout = new FillLayout(SWT.NULL);

		composite.setLayout(fillLayout);

		final TabFolder tabFolder = new TabFolder(composite, SWT.NONE);

		tabFolderInDialog = tabFolder;

		tabFolder.setBounds(0, 0, 800, 600);

		if (!synchRally){			
		createHostMeetingTab(tabFolder);
		createJoinMeetingTab(tabFolder);
		createSwitchProjectTab(tabFolder);
		if (PersisterFactory.getPersister() instanceof AsynchronousLocalPersister)
			tabFolder.setSelection(0);
		if (PersisterFactory.getPersister() instanceof ClientCommunicator)
			tabFolder.setSelection(1);
		}
		;
		createRallyTabNew(tabFolder);	
		

		composite.pack();

		return composite;
	}

	
	
	private TabItem createSwitchProjectTab(final TabFolder tabFolder){

		TabItem switchProjectTab = new TabItem(tabFolder, SWT.NONE);
		switchProjectTab.setText("Switch Project");

		Composite switchProjectTabComposite = new Composite(tabFolder,
				SWT.NONE);

		switchProjectTabComposite.setLayout(new GridLayout(1, false));

		Group groupSwitchProject = new Group(switchProjectTabComposite,
				SWT.NONE);
		groupSwitchProject.setText("Switch Project");
		groupSwitchProject.setLayout(new GridLayout(3, false));

		GridData gridDataSwitchProject = new GridData();
		gridDataSwitchProject.grabExcessHorizontalSpace = true;
		gridDataSwitchProject.horizontalAlignment = GridData.FILL;
		gridDataSwitchProject.verticalAlignment = GridData.FILL;
		groupSwitchProject.setLayoutData(gridDataSwitchProject);

		GridData gridDataTextBoxes = new GridData(GridData.FILL, GridData.FILL,
				true, false);
		gridDataTextBoxes.widthHint = convertHeightInCharsToPixels(20);

		
		
		Label userInitialLabelDis = new Label(groupSwitchProject, SWT.NONE);
		userInitialLabelDis.setText("&User Initial:");
		userInitialLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.BEGINNING, false, false));

		Label txtUserInitialDis = new Label(groupSwitchProject, SWT.NONE);

		txtUserInitialDis.setLayoutData(gridDataTextBoxes);
		txtUserInitialDis.setText(Settings.getInitialDis());

		
		new Label(groupSwitchProject, SWT.NULL);

		Label serverLabelDis = new Label(groupSwitchProject, SWT.NONE);
		serverLabelDis.setText("             &Url:");
		serverLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		Label txtUrlDis = new Label(groupSwitchProject, SWT.NONE);
		GridData gridData1Dis = new GridData(GridData.FILL, GridData.FILL,
				true, false);

		txtUrlDis.setLayoutData(gridData1Dis);
		txtUrlDis.setText(Settings.getUrl());

		new Label(groupSwitchProject, SWT.NULL);

		Label serverPortLabelDis = new Label(groupSwitchProject, SWT.NONE);
		serverPortLabelDis.setText("             &Port:");
		serverPortLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));


		Label txtPortDis = new Label(groupSwitchProject,SWT.NONE);

		txtPortDis.setLayoutData(gridDataTextBoxes);
		txtPortDis.setText(String.valueOf(Settings.getPort()));

		new Label(groupSwitchProject, SWT.NULL);

		Label projectLabelDis = new Label(groupSwitchProject,SWT.NONE);
		projectLabelDis.setText("&Project:");

		projectLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		comboProjectDis = new Combo(groupSwitchProject, SWT.NONE);
		comboProjectDis.setItems(new String[] { "                                          " });
		comboProjectDis.setSize(200, 200);
		comboProjectDis.select(0);
	
	
		if(PersisterFactory.getPersister() instanceof ClientCommunicator){
			if(txtUrlDis.getText().equalsIgnoreCase(Settings.getUrl())){
				refreshProjectNameFromServer();
			}else{
				getDistributedCombo().removeAll();
				getDistributedCombo().setItem(0,ServerCommunicator.NOT_CONNECTED_MESSAGE);
			}
		}else{
			getDistributedCombo().setItem(0,ServerCommunicator.NOT_CONNECTED_MESSAGE);
		}
		
		switchProjectTab.setControl(switchProjectTabComposite);
		return switchProjectTab;
	}
	/**
	 * @param tabFolder
	 * @return
	 */
	private TabItem createHostMeetingTab(final TabFolder tabFolder) {

		TabItem distributedModeTab = new TabItem(tabFolder, SWT.NONE);

		distributedModeTab.setText("Host Meeting");

		Composite distributedModeTabComposite = new Composite(tabFolder,
				SWT.NONE);

		distributedModeTabComposite.setLayout(new GridLayout(1, false));

		Group groupStartServerLocal = new Group(distributedModeTabComposite,
				SWT.NONE);
		groupStartServerLocal.setText("Host Meeting");
		groupStartServerLocal.setLayout(new GridLayout(3, false));

		GridData gridDataServerLocal = new GridData();
		gridDataServerLocal.grabExcessHorizontalSpace = true;
		gridDataServerLocal.horizontalAlignment = GridData.FILL;
		gridDataServerLocal.verticalAlignment = GridData.FILL;
		groupStartServerLocal.setLayoutData(gridDataServerLocal);

		GridData gridDataTextBoxes = new GridData(GridData.FILL, GridData.FILL,
				true, false);
		gridDataTextBoxes.widthHint = convertHeightInCharsToPixels(20);

		Label userInitialLabelDis = new Label(groupStartServerLocal, SWT.NONE);
		userInitialLabelDis.setText("&User Initial:");
		userInitialLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		txtUserInitialDis = new Text(groupStartServerLocal, SWT.BORDER);
		gridDataTextBoxes.widthHint = convertHeightInCharsToPixels(20);
		txtUserInitialDis.setLayoutData(gridDataTextBoxes);
		txtUserInitialDis.setText(Settings.getInitialDis());

		new Label(groupStartServerLocal, SWT.NULL);

		Label labelServerLocalProjectName = new Label(groupStartServerLocal,
				SWT.NONE);
		labelServerLocalProjectName.setText("Project Name");
		labelServerLocalProjectName.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		txtServerLocalProjectName = new Text(groupStartServerLocal, SWT.BORDER);
		txtServerLocalProjectName.setLayoutData(gridDataTextBoxes);
		txtServerLocalProjectName.setText(Settings.getProjectName().replaceAll(
						".xml", ""));

		txtServerLocalDir1 = Settings.getProjectLocationLocalMode();

		Button buttonServerLocalDir1 = new Button(groupStartServerLocal,
				SWT.PUSH);
		buttonServerLocalDir1.setText("Browse");
		buttonServerLocalDir1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				FileDialog dialog = new FileDialog(new Shell(), SWT.NONE);

				String[] extensions = { "*.xml", "*.XML", "*.*" };
				dialog.setFilterExtensions(extensions);

				if (dialog.open() != null) {
					txtServerLocalProjectName.setText(dialog.getFileName()
							.replaceAll(".xml", ""));
					txtServerLocalDir1 = dialog.getFilterPath();
				}
				Settings.setProjectName(
								txtServerLocalProjectName.getText().replaceAll(
										".xml", ""));
			}
		});

		Label labelServerLocalPort = new Label(groupStartServerLocal, SWT.NONE);
		labelServerLocalPort.setText("Server Port");
		labelServerLocalPort.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		txtServerLocalPort = new Text(groupStartServerLocal, SWT.BORDER);
		txtServerLocalPort.setLayoutData(gridDataTextBoxes);
		txtServerLocalPort.setText(String.valueOf(Settings.getPort()));

		buttonServerLocalStart = new Button(groupStartServerLocal, SWT.PUSH);
		buttonServerLocalStart.setText("Start Server");
		if (isLocalServerStarted)
			buttonServerLocalStart.setEnabled(false);
		buttonServerLocalStart.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);

				new Thread() {

					@Override
					public void run() {
						try {
							if ((txtServerLocalDir1.equals(""))
									|| (txtServerLocalProjectName.getText().trim().equals(""))
									|| (txtServerLocalPort.getText().trim().equals("")))
								throw new NullPointerException();
							new ServerCommunicator(txtServerLocalDir1,
									txtServerLocalProjectName.getText(),
									Integer.valueOf(txtServerLocalPort.getText()));

							isLocalServerStarted = true;
							buttonServerLocalStart.setEnabled(false);

						} catch (NumberFormatException e) {
							showMessageBox("Port should be a digital number");
							util.Logger.singleton().error(e);;
							return;
						} catch (java.net.BindException e) {
							showMessageBox("The Port has been taken up");
							util.Logger.singleton().error(e);
							return;
						} catch (NullPointerException e) {
							showMessageBox("Please Input the Project Directory");
							util.Logger.singleton().error(e);
							return;
						}catch (Exception e) {
							showMessageBox("The Project File You Selected Has Been Damaged");
							util.Logger.singleton().error(e);
						}
					}

				}.run();
			}

		});
		distributedModeTab.setControl(distributedModeTabComposite);
		return distributedModeTab;

	}
	
	private void showMessageBox(String msg){
		MessageBox msgBox = new MessageBox(
				new Shell(), SWT.ICON_WARNING);
		msgBox.setMessage(msg);
		msgBox.setText("Error");
		msgBox.open();
	}

	private TabItem createJoinMeetingTab(final TabFolder tabFolder) {
		TabItem distributedModeTab = new TabItem(tabFolder, SWT.NONE);
		distributedModeTab.setText("Join Meeting");

		Composite distributedModeTabComposite = new Composite(tabFolder,
				SWT.NONE);

		distributedModeTabComposite.setLayout(new GridLayout(1, false));

		Group groupSetupServerAreaDis = new Group(distributedModeTabComposite,
				SWT.NONE);
		groupSetupServerAreaDis.setText("Server Settings");
		groupSetupServerAreaDis.setLayout(new GridLayout(3, false));

		GridData gridDataSetupServerAreaDis = new GridData();
		gridDataSetupServerAreaDis.grabExcessHorizontalSpace = true;
		gridDataSetupServerAreaDis.horizontalAlignment = GridData.FILL;
		gridDataSetupServerAreaDis.verticalAlignment = GridData.FILL;
		groupSetupServerAreaDis.setLayoutData(gridDataSetupServerAreaDis);

		GridData gridDataTextBoxes = new GridData(GridData.FILL, GridData.FILL,
				true, false);
		gridDataTextBoxes.widthHint = convertHeightInCharsToPixels(20);

		/****************************************************************************/

		Label userInitialLabelDis = new Label(groupSetupServerAreaDis, SWT.NONE);
		userInitialLabelDis.setText("&User Initial:");
		userInitialLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.BEGINNING, false, false));

		txtUserInitialDis = new Text(groupSetupServerAreaDis, SWT.BORDER);
		gridDataTextBoxes.widthHint = convertHeightInCharsToPixels(20);
		txtUserInitialDis.setLayoutData(gridDataSetupServerAreaDis);
		txtUserInitialDis.setText(Settings.getInitialDis());

		new Label(groupSetupServerAreaDis, SWT.NULL);

		/****************************************************************************/
		Label serverLabelDis = new Label(groupSetupServerAreaDis, SWT.NONE);
		serverLabelDis.setText("             &Url:");
		serverLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		txtUrlDis = new Text(groupSetupServerAreaDis, SWT.BORDER);
		GridData gridData1Dis = new GridData(GridData.FILL, GridData.FILL,
				true, false);
		gridDataTextBoxes.widthHint = convertHeightInCharsToPixels(20);
		txtUrlDis.setLayoutData(gridData1Dis);
		txtUrlDis.setText(Settings.getUrl());
		//		new Label(compositeServerAreaDis, SWT.NULL);
		new Label(groupSetupServerAreaDis, SWT.NULL);
		//		new Label(compositeServerAreaDis, SWT.NULL);
		/****************************************************************************/

		Label serverPortLabelDis = new Label(groupSetupServerAreaDis, SWT.NONE);
		serverPortLabelDis.setText("             &Port:");
		serverPortLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		txtPortDis = new Text(groupSetupServerAreaDis, SWT.BORDER);

		txtPortDis.setLayoutData(gridDataTextBoxes);
		txtPortDis.setText(String.valueOf(Settings.getPort()));

		new Label(groupSetupServerAreaDis, SWT.NULL);

		distributedModeTab.setControl(distributedModeTabComposite);
		return distributedModeTab;
	}

	/**
	 * @param tabFolder
	 */

	private void createRallyTabNew(TabFolder tabFolder) {

		TabItem localServerModeTab = new TabItem(tabFolder, SWT.NONE);
		localServerModeTab.setText("Rally Server");

		Composite localServerModeTabComposite = new Composite(tabFolder,
				SWT.NONE);

		GridLayout layoutItem4 = new GridLayout(1, false);
		localServerModeTabComposite.setLayout(layoutItem4);

		// /////////////////////////////////
		// Group Server Local
		// /////////////////////////////////		

		Group compositeSetupServerAreaDis = new Group(
				localServerModeTabComposite, SWT.NONE);
		compositeSetupServerAreaDis.setText("Rally Connection");
		GridLayout layoutSetupServerAreaDis = new GridLayout(3, false);

		GridData gridDataSetupServerAreaDis = new GridData();
		gridDataSetupServerAreaDis.grabExcessHorizontalSpace = true;
		gridDataSetupServerAreaDis.horizontalAlignment = GridData.FILL;
		gridDataSetupServerAreaDis.verticalAlignment = GridData.FILL;
		compositeSetupServerAreaDis.setLayoutData(gridDataSetupServerAreaDis);

		compositeSetupServerAreaDis.setLayout(layoutSetupServerAreaDis);

		Composite compositeServerAreaDis = new Composite(
				compositeSetupServerAreaDis, SWT.NONE);
		GridLayout layoutServerAreaDis = new GridLayout(3, false);
		compositeServerAreaDis.setLayout(layoutServerAreaDis);

		Label serverDis = new Label(compositeServerAreaDis, SWT.NONE);
		serverDis.setText("        &Rally Server:");
		serverDis.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));

		 rallyComboServerDis = new Combo(compositeServerAreaDis,
				SWT.READ_ONLY);
		rallyComboServerDis.setItems(new String[] { "rally1", "trial", "demo",
				"preview" });
		rallyComboServerDis.setSize(200, 200);
		rallyComboServerDis.select(0);
		new Label(compositeServerAreaDis, SWT.NULL);

		Label rallyServerLabelDis = new Label(compositeServerAreaDis, SWT.NONE);
		rallyServerLabelDis.setText("             &User Name:");
		rallyServerLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		txtRallyUserNameDis = new Text(compositeServerAreaDis, SWT.BORDER);
		GridData gridData1Dis = new GridData(GridData.FILL, GridData.FILL,
				true, false);
		gridData1Dis.widthHint = convertHeightInCharsToPixels(20);
		txtRallyUserNameDis.setLayoutData(gridData1Dis);
		txtRallyUserNameDis.setText(Settings.getUser());

		new Label(compositeServerAreaDis, SWT.NULL);

		Label serverPasswordLabelDis = new Label(compositeServerAreaDis,
				SWT.NONE);
		serverPasswordLabelDis.setText("            &Password:");
		serverPasswordLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		txtRallyPasswordDis = new Text(compositeServerAreaDis, SWT.BORDER);
		GridData gridDataPasswordDis = new GridData(GridData.FILL,
				GridData.FILL, true, false);
		gridDataPasswordDis.widthHint = convertHeightInCharsToPixels(20);
		txtRallyPasswordDis.setLayoutData(gridDataPasswordDis);
		txtRallyPasswordDis.setText(String.valueOf(Settings.getPass()));
		txtRallyPasswordDis.setEchoChar('*');

		buttonRallyLogin = new Button(compositeSetupServerAreaDis, SWT.PUSH);
		
		
		if(synchRally)
			buttonRallyLogin.setText("Synch with Rally");
		else
			buttonRallyLogin.setText("Login");

		
		
		buttonRallyLogin.addSelectionListener(new SelectionAdapter() {

			
			
			public void widgetDefaultSelected(SelectionEvent e) {
			}
           @Override
			public void widgetSelected(SelectionEvent e) {
				
        	   super.widgetSelected(e);
        	   
        	   new Thread() {
        		   
        		   @Override
					public void run() {
				if (PersisterFactory.getPersister() instanceof AsynchronousLocalPersister) {
					startLocalServer();
					Settings.setUrl("localhost");
					Settings.setPersisterType("DISTRIBUTED");
					resetPersisterInCurrentEditor();
				}

				// we are already connected to some remote server so do nothing
				else {
				}

				Settings.setUser(txtRallyUserNameDis.getText());
				Settings.setPass(txtRallyPasswordDis.getText());
				rallyUserForSynch = txtRallyUserNameDis.getText();
				
				//login to Rally
				login(txtRallyUserNameDis.getText(), txtRallyPasswordDis
						.getText(), rallyComboServerDis.getItem(rallyComboServerDis.getSelectionIndex()));
				
				isRallyConnected = true;
			
				//get projects from Rally if it is not synch to rally otherwise synch to Rally
				if(synchRally){
					
					new SynchronizeProjectCommand();
				}else{
					refreshProjectNameFromServerForRally();
				}
				
				  if (isRallyConnected)
      				buttonRallyLogin.setEnabled(false);
				    rallyComboProjectDis.setEnabled(true);
        		   }
        		   
        		 
        		   }.run();		
			}

			private void startLocalServer() {
				new Thread() {

					@Override
					public void run() {
						try {
							String projectDirectory = FileLocator.toFileURL(
									Platform.getBundle(PluginInformation.getPLUGIN_ID()).getEntry(
											"ProjectDirectory")).getFile().toString();
							if(projectDirectory == null)
								projectDirectory = ".";
							System.out.println("Project directory on which server is running is: " + projectDirectory);
							int port = Settings.getPort();
							if (port == 0)
								port = 5555;
							String projectFile = Settings.getProjectName();
							if(projectFile == null || projectFile.equalsIgnoreCase(""))
								projectFile = "ProjectFile";
							new ServerCommunicator(projectDirectory, projectFile , port);
							isLocalServerStarted = true;

						} catch (NumberFormatException e) {

							MessageBox portWrongFormatMSGBox = new MessageBox(
									new Shell(), SWT.ICON_WARNING);
							portWrongFormatMSGBox
									.setMessage("Port should be a digital number");
							portWrongFormatMSGBox.setText("Error");
							portWrongFormatMSGBox.open();
							util.Logger.singleton().error(e);
							return;
						} catch (java.net.BindException e) {

							MessageBox portTakenUpMSGBox = new MessageBox(
									new Shell(), SWT.ICON_WARNING);
							portTakenUpMSGBox
									.setMessage("The Port has been taken up");
							portTakenUpMSGBox.setText("Error");
							portTakenUpMSGBox.open();
							util.Logger.singleton().error(e);
							return;
						} catch (NullPointerException e) {

							MessageBox emptyInputMSGBox = new MessageBox(
									new Shell(), SWT.ICON_WARNING);
							emptyInputMSGBox
									.setMessage("Please Input the Project Directory");
							emptyInputMSGBox.setText("Error");
							emptyInputMSGBox.open();
							util.Logger.singleton().error(e);
							return;
						}

						catch (Exception e) {
							MessageBox unknownWrongMSGBox = new MessageBox(
									new Shell(), SWT.ICON_WARNING);
							unknownWrongMSGBox
									.setMessage("The Project File You Selected Has Been Damaged");
							unknownWrongMSGBox.setText("Error");
							unknownWrongMSGBox.open();
							util.Logger.singleton().error(e);
						}
					}
				}.run();
			}
		});

		new Label(compositeServerAreaDis, SWT.NULL);

		if(!synchRally){
		Label projectLabelDis = new Label(compositeServerAreaDis, SWT.NONE);
		projectLabelDis.setText("             &Project:");
		projectLabelDis.setLayoutData(new GridData(GridData.END,
				GridData.CENTER, false, false));

		rallyComboProjectDis = new Combo(compositeServerAreaDis, SWT.READ_ONLY);
		rallyComboProjectDis
				.setItems(new String[] { "Rally Integration Project" });
		rallyComboProjectDis.setSize(200, 200);
		rallyComboProjectDis.select(0);
		rallyComboProjectDis.setEnabled(false);
		}
		
		

		localServerModeTab.setControl(localServerModeTabComposite);
		
		

	}

	@Override
	protected void okPressed() {
		if(!synchRally){
						
		if (tabFolderInDialog.getSelectionIndex() == 1) {
			joinMeeting();
		}

		if (tabFolderInDialog.getSelectionIndex() == 0) {
			hostMeeting();			
		}
		
		if (tabFolderInDialog.getSelectionIndex() == 2)  {
		    switchProject();						
		}
		
		if (tabFolderInDialog.getSelectionIndex()==3){
			rallyLogin();
		}
		
		}else{}
		
		super.okPressed();
	}
	
	private void switchProject(){
		
		String projectName = comboProjectDis.getText();
		Settings.setProjectName(projectName);

		connectToServer();
		resetEditor(window, projectModel);
	}
	
	public static IEditorInput resetEditor(IWorkbenchWindow window,ProjectModel project) {

		IWorkbenchPage page;
		page = window.getActivePage();
		((Editor) page.getActiveEditor()).setDataCallback(null);
		if (PersisterFactory.getPersister() != null)
			PersisterFactory.deletePersister();
		page.closeEditor(page.getActiveEditor(), true);

		//new editor
		IEditorInput neweditor = createEditorInput(new File("temp"));
		String editorId = "RallyDemoGEF.Editor";
		try {
			//page.getActiveEditor().dispose();
			page.openEditor(neweditor, editorId);
			String titleTab;
			titleTab = project.getProjectDataObject().getName()
					+ ":"
					+ Settings.getInitialDis();
			((Editor) page.getActiveEditor()).updateTitleTab(titleTab);
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

	private void rallyLogin() {
		if (txtRallyUserNameDis.getText().trim().equals("")
										|| txtRallyPasswordDis.getText().trim().equals("")) {
			MessageBox emptyInputMSGBox = new MessageBox(new Shell(),
					SWT.ICON_WARNING);
			emptyInputMSGBox
					.setMessage("Check the items that are needed to fill in");
			emptyInputMSGBox.setText("Error");
			emptyInputMSGBox.open();
		}
		
		
		
		try {
			ProgressBarThread.getInstatnce().start();
			PersisterFactory.getPersister().connectToRally(rallyComboProjectDis.getItem(rallyComboProjectDis.getSelectionIndex()));

			System.out.println("asdfasdfasdfasdfasdfasdfasdfasdfsadfadsfadfadfasdf");
		} catch (Exception e) {
			util.Logger.singleton().error(e);
			MessageBox messageBox = new MessageBox(new Shell(),
					SWT.ICON_WARNING);
			messageBox.setText("AgilePlanner Information");
			messageBox.setMessage("Could not Load the Project File");
			messageBox.open();
			
		}
		
		
	}

	private void hostMeeting() {
		Settings.setPersisterType("DISTRIBUTED");
		Settings.setProjectName(txtServerLocalProjectName.getText().trim());
		Settings.setProjectLocationLocalMode(txtServerLocalDir1);
		Settings.setUrl("localhost");
		connectToServer();

	}

	private void joinMeeting() {
		if (this.txtUrlDis.getText().trim().equals("")
				|| this.txtPortDis.getText().trim().equals("")
				|| this.txtUserInitialDis.getText().trim().equals("")) {
			MessageBox emptyInputMSGBox = new MessageBox(new Shell(),
					SWT.ICON_WARNING);
			emptyInputMSGBox
					.setMessage("Check the items that are needed to fill in");
			emptyInputMSGBox.setText("Error");
			emptyInputMSGBox.open();
		}

		Settings.setPersisterType("DISTRIBUTED");
		Settings.setInitialDis(txtUserInitialDis.getText());
		Settings.setUrl(txtUrlDis.getText());
		Settings.setPort(Integer.valueOf(txtPortDis.getText()));
		Settings.setProjectName(comboProjectDis.getItem(comboProjectDis.getSelectionIndex()));
		connectToServer();
		resetEditor(window, projectModel);
	}

	private void connectToServer(){
		resetPersisterInCurrentEditor();
		
		try {

			PersisterFactory.getPersister().connect();
		} catch (Exception e) {
			util.Logger.singleton().error(e);
			MessageBox messageBox = new MessageBox(new Shell(),
					SWT.ICON_WARNING);
			messageBox.setText("AgilePlanner Information");
			messageBox.setMessage("Could not Load the Project File");
			messageBox.open();
			
		}
	}
	
	private void resetPersisterInCurrentEditor() {
		//IEditorPart editor = this.window.getActivePage().getActiveEditor();
		boolean editorOpen = false;
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorReference[] editorReference = page.getEditorReferences();
		for (IEditorReference er : editorReference) {
			if (er.getId().equals(Editor.ID)){
				editorOpen = true;
				Editor ed = (Editor) er.getEditor(true);
				er.getPage().activate(ed);
				PersisterFactory.getPersister().removePlannerDataChangeListener(
					ed.getDataCallback()
				);
				PersisterFactory.getUIEventPropagator().removePlannerUIChangeListener(
					ed.getDataCallback()
				);
				PersisterFactory.deletePersister();
				PersisterFactory.getPersister();
				PersisterFactory.getPersister().addPlannerDataChangeListener(
					ed.getDataCallback()
				);
				PersisterFactory.getUIEventPropagator().addPlannerUIChangeListener(
					ed.getDataCallback()
				);
			}
		}
		
		if (!editorOpen){
			File file = new File(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString() + File.separator + "tempProject");
			try{file.createNewFile();}catch (Exception e) {}
			IPath path = new Path(file.getAbsolutePath());
	        IEditorInput input = new PathEditorInput(path);
			try {
	        	page.openEditor(input, Editor.ID);
	        }catch (PartInitException e) {
	        	util.Logger.singleton().error(e);
	        }
		}

	}

	private void refreshProjectNameFromServer() {
		try {
			PersisterFactory.getPersister().getProjectNames();
			ChangePersisterConnectDialogComoBox changeCombo = new ChangePersisterConnectDialogComoBox(
					this);
			changeCombo.gotProjectsNameFromServerListener();
		} catch (NotConnectedException e) {

			MessageBox unknownWrongMSGBox = new MessageBox(new Shell(),
					SWT.ICON_WARNING);
			unknownWrongMSGBox.setMessage("Unable To Start Local Server");
			unknownWrongMSGBox.setText("Error");
			unknownWrongMSGBox.open();
			util.Logger.singleton().error(e);
		}

	}

	private void refreshProjectNameFromServerForRally() {
		try {
			ChangePersisterConnectDialogComoBox changeCombo = new ChangePersisterConnectDialogComoBox(
					this);
			PersisterFactory.getPersister().getProjectNames();

			changeCombo.gotProjectsNameFromServerForRallyListener();
		} catch (NotConnectedException e) {

			MessageBox unknownWrongMSGBox = new MessageBox(new Shell(),
					SWT.ICON_WARNING);
			unknownWrongMSGBox.setMessage("Unable To Start Local Server");
			unknownWrongMSGBox.setText("Error");
			unknownWrongMSGBox.open();
			util.Logger.singleton().error(e);
		}

	}

	public void setCombo(Combo combo) {
		this.comboProject = combo;
	}

	public Combo getDistributedRallyCombo() {

		return this.rallyComboProjectDis;
	}

	public Combo getCombo() {
		return comboProject;
	}

	public Combo getDistributedCombo() {
		return this.comboProjectDis;
	}

	protected void login(String userName, String password, String url) {
		
		
		try {
			PersisterFactory.getPersister().login(userName, password,url);

		} catch (NotConnectedException e) {

			MessageBox unknownWrongMSGBox = new MessageBox(new Shell(),
					SWT.ICON_WARNING);
			unknownWrongMSGBox
					.setMessage("Login Failure! Check username, password.");
			unknownWrongMSGBox.setText("Error");
			unknownWrongMSGBox.open();
			util.Logger.singleton().error(e);;
		}

	}

	
	public static String getRallyUserForSynch() {
		return rallyUserForSynch;
	}

}

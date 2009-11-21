package applicationWorkbench.uielements;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import cards.model.ProjectModel;

import persister.Event;
import persister.NotConnectedException;
import persister.data.TeamMember;
import persister.data.impl.TeamMemberDataObject;
import persister.factory.PersisterFactory;

public class UsersTab implements SelectionListener {

	private TabItem tab;
	private Button deleteUser;
	private Button saveUser;
	private Label userNameLabel;
	private List userList;
	private Text userName;
	private ProjectModel projectModel;
	
	
	public TabItem create(TabFolder parent, ProjectModel pm){
		setTab(new TabItem(parent, SWT.NONE));
		getTab().setText("Users");

		setProjectModel(pm);
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		
		
		Composite userTabComposite = new Composite(parent, SWT.NONE);
		userTabComposite.setLayout(new GridLayout(1, true));
		
		Group groupUsers = new Group(userTabComposite, SWT.NONE);
		groupUsers.setText("Edit Users");
		groupUsers.setLayout(gridLayout);
		groupUsers.setLayoutData(gridData);
		
		setUserList(new List(groupUsers, SWT.V_SCROLL));
		getUserList().setLayoutData(gridData);
		updateUserList();
		
		
		setUserNameLabel(new Label(groupUsers, SWT.NONE));
		getUserNameLabel().setText("User Name:");
	
		
		setUserName(new Text(groupUsers, SWT.BORDER));
		
		setSaveUser(new Button(groupUsers, SWT.NONE));
		getSaveUser().setText("Add User");
		getSaveUser().addSelectionListener(this);
		
		setDeleteUser(new Button(groupUsers, SWT.NONE));
		getDeleteUser().setText("Remove Selected User");
		getDeleteUser().addSelectionListener(this);
		
		new Label(groupUsers, SWT.NONE); // filler label
		

		getUserList().addSelectionListener(this);
		getTab().setControl(userTabComposite);
		return getTab();
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	public void widgetSelected(SelectionEvent e) {
		if(e.widget.equals(getUserList())){
			getUserName().setText(getSelectedUser());
			getUserName().setEnabled(false);
		}else{
			TeamMember teamMember = new TeamMemberDataObject(getUserName().getText());
			try {
				if(e.widget.equals(getDeleteUser())){
					PersisterFactory.getPersister().deleteOwner(teamMember);
					projectModel.getProjectDataObject().removeOwner(teamMember);
				}else if(e.widget.equals(getSaveUser())){
					PersisterFactory.getPersister().updateOwner(teamMember);
					projectModel.getProjectDataObject().addTeamMember(teamMember);
				}
				updateUserList();
				getUserName().setEnabled(true);
				getUserName().setText("");
			} catch (NotConnectedException ex) {
				util.Logger.singleton().error(ex);
			}
		}
	}

	private void updateUserList(){
		userList.removeAll();
		for(TeamMember user : getProjectModel().getProjectDataObject().getTeamMembers()){
			if(user.getClass() != null && user.getName() != ""){
				userList.add(user.getName());
			}
		}
	}

	public String getSelectedUser(){
		return getUserList().getItem(getUserList().getSelectionIndex());
	}
	
	public Button getDeleteUser() {
		return deleteUser;
	}

	public Button getSaveUser() {
		return saveUser;
	}

	public List getUserList() {
		return userList;
	}

	public Text getUserName() {
		return userName;
	}

	public void setDeleteUser(Button deleteUser) {
		this.deleteUser = deleteUser;
	}

	public void setSaveUser(Button saveUser) {
		this.saveUser = saveUser;
	}

	public void setUserList(List userList) {
		this.userList = userList;
	}

	public void setUserName(Text userName) {
		this.userName = userName;
	}

	public ProjectModel getProjectModel() {
		return projectModel;
	}

	public void setProjectModel(ProjectModel projectModel) {
		this.projectModel = projectModel;
	}

	public TabItem getTab() {
		return tab;
	}

	public void setTab(TabItem tab) {
		this.tab = tab;
	}

	public Label getUserNameLabel() {
		return userNameLabel;
	}

	public void setUserNameLabel(Label userNameLabel) {
		this.userNameLabel = userNameLabel;
	}
	
	
}

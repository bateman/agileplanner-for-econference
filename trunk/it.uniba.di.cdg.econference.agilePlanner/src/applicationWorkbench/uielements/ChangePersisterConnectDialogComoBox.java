package applicationWorkbench.uielements;

import org.eclipse.swt.widgets.Display;
import cards.model.ProjectModel;

public class ChangePersisterConnectDialogComoBox /*implements PlannerUIChangeListener*/{

	private PersisterConnectDialog pcDialog;

	private static String[] strFromServer;

	private static String[] strFromServerForRally;

	public ChangePersisterConnectDialogComoBox(PersisterConnectDialog pcDialog) {
		this.pcDialog = pcDialog;
	}


	public void gotProjectsNameFromServerListener() {
		Display d = pcDialog.getDistributedCombo().getDisplay();
		d.syncExec(new Runnable() {
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					util.Logger.singleton().error(e);
				}
				if(strFromServer == null)strFromServer = new String[]{""};
				pcDialog.getDistributedCombo().setItems(strFromServer);
				
				if(strFromServer.length == 1)
					pcDialog.getDistributedCombo().select(0);
				else
				{
					//Find the currently active project
					String currentProjectName = ProjectModel.getCurrentProject().getProjectDataObject().getName();			
					
					//Set that to the selected project in the combo box
					int selectedIndex = pcDialog.getDistributedCombo().indexOf(currentProjectName);
					
					if(selectedIndex == -1) //If the proj name isn't found then just select the first one
						selectedIndex = 0;
					
					//Make the selection
					pcDialog.getDistributedCombo().select(selectedIndex);
				}
			}
		});
	}
	
	public void gotProjectsNameFromServerForRallyListener() {
		Display d1 = pcDialog.getDistributedRallyCombo().getDisplay();
		d1.syncExec(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					util.Logger.singleton().error(e);
				}
					pcDialog.getDistributedRallyCombo().setItems(strFromServerForRally);
					pcDialog.getDistributedRallyCombo().select(0);
			}
		});
	}

	public static void gotProjectsNamesForLoginEvent(String[] str) {
		if (str.length > 0){
			strFromServerForRally = new String[str.length];
			for (int i=0; i < str.length; i++) {
				strFromServerForRally[i] = str[i];
			}
		}
	}

	public static void gotProjectsNames(String[] str) {
		if (str.length > 0){
			strFromServer = new String[str.length];
			for (int i=0; i < str.length; i++) {
				strFromServer[i] = str[i];
			}
		}
	}
}

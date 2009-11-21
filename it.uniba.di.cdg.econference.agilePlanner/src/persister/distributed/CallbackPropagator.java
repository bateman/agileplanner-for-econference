/**
 * 
 */
package persister.distributed;

import static persister.distributed.ClientCommunicator.setCurrentProjectName;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import cards.model.ProjectModel;

import persister.IndexCardLiveUpdate;
import persister.Message;
import persister.Event;
import persister.PlannerDataChangeListener;
import persister.PlannerUIChangeListener;
import persister.data.Backlog;
import persister.data.IndexCard;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.DisconnectDataObject;
import persister.data.impl.MessageDataObject;
import persister.distributed.ClientCallbackCommunicator;
import persister.distributed.ClientCommunicator;
import persister.factory.Settings;
import persister.xml.converter.Converter;



/**
 * @author dhillonh
 * 
 */
public class CallbackPropagator implements ClientCallbackCommunicator {
	private boolean loadRequested = false;

	private List<PlannerDataChangeListener> plannerDataChangeListeners;

	private List<PlannerUIChangeListener> plannerUIChangeListeners;

	private boolean connectRequested = false;

	private boolean downloadRequested = false;

	private boolean uploadRequested = false;

	private boolean getProjectNamesRequested = false;

	private static File currentDownloadedFile;
	public CallbackPropagator(long clientid) {
		this.plannerDataChangeListeners = new ArrayList<PlannerDataChangeListener>();
		this.plannerUIChangeListeners = new ArrayList<PlannerUIChangeListener>();
	}

	/***************************************************************************
	 * Register callback listeners
	 **************************************************************************/

	public void addPlannerDataChangeListener(PlannerDataChangeListener listener) {
		this.plannerDataChangeListeners.add(listener);
	}

	public void removePlannerDataChangeListener(
			PlannerDataChangeListener listener) {
		this.plannerDataChangeListeners.remove(listener);
	}

	/***************************************************************************
	 * PLANNERUICHANGELISTENER *
	 **************************************************************************/

	public void addPlannerUIChangeListener(PlannerUIChangeListener listener) {

		this.plannerUIChangeListeners.add(listener);
	}

	public void removePlannerUIChangeListener(PlannerUIChangeListener listener) {

		this.plannerUIChangeListeners.remove(listener);
	}

	/***************************************************************************
	 * CALLBACK FUNCTIONALITY *
	 **************************************************************************/

	public synchronized void receiveMessage(Message message) {

		switch (message.getMessageType()) {
		case Message.ARRANGE_PROJECT:
			this.fireArrangedProject((Project) message.getMessage());
			break;
		case Message.AUTH_FAIL:
			System.out.println("Invalid Login: "
					+ ((MessageDataObject) message).getData().get("message"));
			break;
		case Message.EXCEPTION:
			this.fireException((Exception) message.getMessage(), message
					.getEtype());
			break;
		case Message.MOUSE_MOVE:
			this.fireMovedMouseEvent((Event) message.getMessage());
			break;
		case Message.BRING_TO_FRONT:
			this.fireBroughtToFrontEvent((Long) message.getMessage());
			break;
		case Message.CREATE_BACKLOG:
			this.fireCreatedBacklogEvent((Backlog) message.getMessage());
			break;
		case Message.CREATE_ITERATION:
			this.fireCreatedIterationEvent((Iteration) message.getMessage());
			break;
		case Message.CREATE_PROJECT:
			this.fireChangedProjectEvent((Project) message.getMessage());
			break;
		case Message.SYNCH_PROJECT:
			this.fireChangedProjectEvent((Project) message.getMessage());
			break;
		case Message.CONNECT:
			this.fireCreatedProjectEvent((Project) message.getMessage());
			break;
		case Message.CONNECT_TO_RALLY:
			this.fireChangedProjectEvent((Project) message.getMessage());
			break;
		case Message.CREATE_STORYCARD:
			this.fireCreatedStoryCardEvent((StoryCard) message.getMessage());
			break;

		case Message.DELETE_CARD:
			this.fireDeletedCardEvent(message);
			break;
		case Message.DELETE_OWNER:
			this.fireDeletedOwnerEvent(message);
			break;
		case Message.DOWNLOAD:
			if (downloadRequested) {
				downloadRequested = false;
				Object[] returnedData = (Object[]) message.getMessage();

				this.fireDownloadedFileEvent(this.saveFile(
						(String[]) returnedData[0], (byte[]) returnedData[1],
						(String) returnedData[2]));
			}
			break;

		case Message.GET_PROJECT_NAMES:
			System.out.println("come to client call back"+ getProjectNamesRequested);
			if (getProjectNamesRequested) {
				this.fireGotProjectNamesEvent((List<String>) message
						.getMessage());
				getProjectNamesRequested = false;
			}
			break;

		case Message.LOGIN:
			this.fireGotProjectNamesForLoginEvent((List<String>) message
					.getMessage());
			break;
		case Message.LOST_CONNECTION:
			this.fireLostConnectionEvent();
			break;

		case Message.LOAD:
			if (this.isLoadRequested()) {
				this.fireCreatedProjectEvent((Project) message.getMessage());
				this.setLoadRequested(false);
			}
			break;
		case Message.MOVE_STORYCARD_TO_NEW_PARENT:
			this.fireMovedStoryCardToNewParentEvent((StoryCard) message
					.getMessage());
			break;

		case Message.UNDELETE_CARD:
			this.fireUndeletedCardEvent(message);
			break;

		case Message.UPDATE_LEGEND:
			this.fireUpdatedLegendEvent(message);
			break;
		
		case Message.VERSION_VERIFICATION:
			fireVersionVerification(message);
			break;
			
		case Message.UPDATE_OWNER:
			this.fireUpdatedOwnerEvent(message);
			break;

		case Message.UPDATE_CARD:
			this.fireUpdatedCardEvent(message);
			break;

		case Message.UPLOAD:
			if (uploadRequested) {
				uploadRequested = false;
				this.fireUploadedFileEvent(true);
			}
			break;

		case Message.DISCONNECT:
			this.fireDisconnectEvent(message);
			break;

		case Message.KEYSTROKE:
			this.fireIncomingKeystrokeEvent(message);
			break;
		default:
			util.Logger.singleton().info("Message with id=" + message.getMessageType()
					+ " not handled in CallbackPropagator");
			break;
		}
	}

	private void fireUpdatedLegendEvent(Message message) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).updatedLegend(
					(Legend) message.getMessage());
		}

	}
	
	private void fireUpdatedOwnerEvent(Message message) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).updatedOwner(
					(TeamMember) message.getMessage());
		}

	}

	private void fireLostConnectionEvent() {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).lostConnectionEvent();
		}

	}

	private void fireIncomingKeystrokeEvent(Message message) {

		for (PlannerUIChangeListener listener : plannerUIChangeListeners) {
			listener.liveTextUpdate((IndexCardLiveUpdate) message.getMessage());
		}

	}

	private void fireUpdatedCardEvent(Message message) {
		IndexCard indexCard = (IndexCard) message.getMessage();
		if (indexCard instanceof Backlog)
			fireUpdatedBacklogEvent((Backlog) indexCard);
		if (indexCard instanceof Iteration)
			fireUpdatedIterationEvent((Iteration) indexCard);
		if (indexCard instanceof StoryCard)
			fireUpdatedStoryCardEvent((StoryCard) indexCard);
	}

	private void fireDeletedCardEvent(Message message) {
		IndexCard indexCard = (IndexCard) message.getMessage();

		if (indexCard instanceof Iteration)
			fireDeletedIterationEvent(((Iteration) indexCard).getId());
		if (indexCard instanceof StoryCard)
			fireDeletedStoryCardEvent(((StoryCard) indexCard).getId());
	}
	
	private void fireDeletedOwnerEvent(Message message) {
		TeamMember teamMember = (TeamMember) message.getMessage();
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).deletedOwner(teamMember);
		}
	}

	private void fireUndeletedCardEvent(Message message) {
		IndexCard indexCard = (IndexCard) message.getMessage();

		if (indexCard instanceof Iteration)
			fireUndeletedIterationEvent((Iteration) indexCard);
		if (indexCard instanceof StoryCard)
			fireUndeletedStoryCardEvent((StoryCard) indexCard);
	}

	private void fireException(Exception exception, int messageType) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).asynchronousException(exception,
					messageType);
		}
	}

	private void fireGotProjectNamesEvent(List<String> list) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).gotProjectNames(list);
		}
	}

	private void fireGotProjectNamesForLoginEvent(List<String> list) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i)
					.gotProjectNamesForLoginEvent(list);
		}
	}

	private void fireUploadedFileEvent(boolean bool) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).uploadedFile(bool);
		}
	}

	private void fireDownloadedFileEvent(boolean bool) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).downloadedFile(bool);
		}
	}

	/***************************************************************************
	 * FIRE CREATE EVENTS *
	 **************************************************************************/

	private void fireCreatedBacklogEvent(Backlog backlog) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).createdBacklog(backlog);
		}
	}

	private void fireCreatedIterationEvent(Iteration iteration) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).createdIteration(iteration);
		}
	}

	private void fireCreatedProjectEvent(Project project) {
		// loop through each listener and pass on the event if needed
		Settings.setProjectName(project.getName());
		ClientCommunicator.setCurrentProjectName(project.getName());
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i)
					.createdProjectOnInitialLoadFromServer(project);
		}
	}

	private void fireChangedProjectEvent(Project project) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i)
					.createProjectOnSubsequentLoadsFromServer(project);
		}
	}

	private void fireVersionVerification(Message msg){
		Integer serverVersion = (Integer)Converter.fromXML((String)msg.getMessage());
		if(!serverVersion.equals(ServerCommunicator.SERVER_CLIENT_VERSION)){
			MessageBox mbox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR);
			mbox.setText("Connection problem.");
			mbox.setMessage("Server/Client version's are in conflict." );
			mbox.open();
		}
	}
	
	private void fireArrangedProject(Project project) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).arrangeProject(project);
		}
	}

	private void fireCreatedStoryCardEvent(StoryCard storycard) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).createdStoryCard(storycard);
		}
	}

	/***************************************************************************
	 * FIRE DELETE EVENTS *
	 **************************************************************************/

	private void fireDeletedIterationEvent(long id) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).deletedIteration(id);
		}
	}

	private void fireDeletedStoryCardEvent(long id) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).deletedStoryCard(id);
		}
	}

	private void fireMovedStoryCardToNewParentEvent(StoryCard storycard) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).movedStoryCardToNewParent(
					storycard);
		}
	}

	/***************************************************************************
	 * FIRE UNDELETE EVENTS *
	 **************************************************************************/

	private void fireUndeletedIterationEvent(Iteration iteration) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).undeletedIteration(iteration);
		}
	}

	private void fireUndeletedStoryCardEvent(StoryCard storyCard) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).undeletedStoryCard(storyCard);
		}
	}

	/***************************************************************************
	 * FIRE UPDATE BACKLOG EVENTS *
	 **************************************************************************/

	private void fireUpdatedBacklogEvent(Backlog backlog) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).updatedBacklog(backlog);
		}
	}

	/***************************************************************************
	 * FIRE UPDATE ITERATION EVENTS *
	 **************************************************************************/

	private void fireUpdatedIterationEvent(Iteration iteration) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).updatedIteration(iteration);
		}
	}

	/***************************************************************************
	 * FIRE UPDATE STORYCARD EVENTS *
	 **************************************************************************/

	private void fireUpdatedStoryCardEvent(StoryCard sc) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerDataChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerDataChangeListeners.get(i).updatedStoryCard(sc);
		}
	}

	/***************************************************************************
	 * FIRE USERINTERFACE EVENTS *
	 **************************************************************************/

	private void fireMovedMouseEvent(Event mm) {
		for (PlannerUIChangeListener listener : plannerUIChangeListeners) {
			listener.movedMouse(mm);
		}

	}

	private void fireDisconnectEvent(Message message) {
	ProjectModel.getCurrentProject().removeRemoteMouseIncoming(((DisconnectDataObject)message.getMessage()).getClientId());
	}

	private void fireBroughtToFrontEvent(long id) {
		// loop through each listener and pass on the event if needed
		int numListeners = plannerUIChangeListeners.size();
		for (int i = 0; i < numListeners; i++) {
			plannerUIChangeListeners.get(i).broughtToFront(id);
		}
	}

	/** ***************************************************************** */

	public boolean saveFile(String[] fileinform, byte[] content, String currentProjectName) {
		int recognizeID = Integer.parseInt(fileinform[1]);
		String fileName = fileinform[0];
		setCurrentProjectName(currentProjectName);
		try {
			if (recognizeID == 0) {
				FileOutputStream e = new FileOutputStream(fileName);
				e.write(content);
				e.flush();
				e.close();
				return true;
			} else {
				File uploadedFileForCard = new File(Settings.getProjectLocationDistributedMode()
						+ File.separatorChar
						+ currentProjectName
						+ File.separatorChar
						+ "Uploaded_Files"
						+ File.separatorChar + String.valueOf(recognizeID));
				if (!uploadedFileForCard.exists()) {
					uploadedFileForCard.mkdirs();
				}
				currentDownloadedFile = uploadedFileForCard;
				FileOutputStream e = new FileOutputStream(uploadedFileForCard
						.getPath()
						+ File.separatorChar + fileName);
				e.write(content);
				e.flush();
				e.close();
				return true;
			}
		} catch (IOException e) {
			util.Logger.singleton().error(e);
			return false;
		}

	}

	/***************************************************************************
	 * Setter/getter
	 **************************************************************************/
	protected boolean isConnectRequested() {
		return connectRequested;
	}

	protected void setConnectRequested(boolean connectRequested) {
		this.connectRequested = connectRequested;
	}

	protected boolean isLoadRequested() {
		return loadRequested;
	}

	protected void setLoadRequested(boolean loadRequested) {
		this.loadRequested = loadRequested;
	}

	protected boolean isGetProjectNamesRequested() {
		return getProjectNamesRequested;
	}

	protected void setGetProjectNamesRequested(boolean getProjectNamesRequested) {
		this.getProjectNamesRequested = getProjectNamesRequested;
	}

	protected boolean isDownloadRequested() {
		return downloadRequested;
	}

	protected void setDownloadRequested(boolean saveAsRequested) {
		this.downloadRequested = saveAsRequested;
	}

	protected boolean isUploadRequested() {
		return uploadRequested;
	}

	protected void setUploadRequested(boolean uploadRequested) {
		this.uploadRequested = uploadRequested;
	}

	public static File getCurrentDownloadedFile() {
		return currentDownloadedFile;
	}

}

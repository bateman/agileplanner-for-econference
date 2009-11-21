package applicationWorkbench;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import applicationWorkbench.actions.ArrangeRetargetAction;
import applicationWorkbench.actions.CollapseStoryCardRetargetAction;
import applicationWorkbench.actions.ConfigureAction;
import applicationWorkbench.actions.DropDownColorActionMenu;
import applicationWorkbench.actions.ExpandStoryCardRetargetAction;
import applicationWorkbench.actions.FontAction;
import applicationWorkbench.actions.HelpMenuAction;
import applicationWorkbench.actions.NewProjectAction;
import applicationWorkbench.actions.OpenProjectAction;
import applicationWorkbench.actions.SaveAsAction;
import applicationWorkbench.actions.ServerConnectionAction;
import applicationWorkbench.actions.StoryCardColorIconAction;
import applicationWorkbench.actions.SynchronizeProjectAction;
import applicationWorkbench.actions.TextSearchAction;

public class ApplicationActionBarContributor extends ActionBarContributor {

	private StoryCardColorIconAction storyCardColorIcon;
	private DropDownColorActionMenu dropDownColorMenuAction;
	private OpenProjectAction openAction=new OpenProjectAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
    private NewProjectAction newAction=new NewProjectAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
    private TextSearchAction searchAction;
    private IWorkbenchAction saveAsAction=ActionFactory.SAVE_AS.create(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
    private ServerConnectionAction serverAction=new ServerConnectionAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
    private SaveAsAction saveAsAction2=new SaveAsAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());

    @Override
	protected void declareGlobalActionKeys() {
		this.addGlobalActionKey(ActionFactory.PRINT.getId());
		this.addGlobalActionKey("ArrangeCards");
		this.addGlobalActionKey("ExpandStoryCards");
		this.addGlobalActionKey("ConfigureAction");
		this.addGlobalActionKey("HelpMenuAction");
	}

	@Override
	protected void buildActions() {
		this.addRetargetAction(new CollapseStoryCardRetargetAction(
				"CollapseStoryCards", "Collapse all the story cards"));
		this.addRetargetAction(new ExpandStoryCardRetargetAction(
				"ExpandStoryCards", "Expand all the story cards"));
		this.addRetargetAction(new ArrangeRetargetAction("ArrangeCards",
				"Arrange all the cards in the Editor"));
		this.addRetargetAction(new DeleteRetargetAction());
		this.addRetargetAction(new UndoRetargetAction());
		this.addRetargetAction(new RedoRetargetAction());
		this.dropDownColorMenuAction = new DropDownColorActionMenu(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		this.addAction(dropDownColorMenuAction);
		this.storyCardColorIcon = new StoryCardColorIconAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow(),"white");
		this.addAction(storyCardColorIcon);
		dropDownColorMenuAction.setIconListener(this.storyCardColorIcon);
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==true){
		newAction.setAccelerator(SWT.CTRL | 'J');
		this.addAction(newAction);
		openAction.setAccelerator(SWT.CTRL | 'O');
		this.addAction(openAction);
		saveAsAction2.setAccelerator(SWT.CTRL | 'K');
		this.addAction(saveAsAction2);
		}
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==false){
			serverAction.setAccelerator(SWT.CTRL | 'K');
			this.addAction(serverAction);
		}
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==false)
			this.addAction(saveAsAction);
		this.searchAction=new TextSearchAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		this.addAction(searchAction);
		this.addAction(new ConfigureAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow()));
		this.addAction(new FontAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow()));
		this.addAction(new HelpMenuAction());
		this.addAction(new SynchronizeProjectAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow()));
	}

	public void contributeToMenu(IMenuManager menuManager) {
		MenuManager AP = new MenuManager("&Agile Planner", "agile planner");
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==true){
		AP.add(this.newAction);
		AP.add(this.openAction);
		this.saveAsAction2.setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("ca.ucalgary.cpsc.agilePlanner", "Icons/icon_saveAs2.jpg"));
		AP.add(this.saveAsAction2);	
		}
		AP.add(new Separator());
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==false)
			AP.add(saveAsAction);
		this.getAction("applicationWorkbench.actions.HelpMenuAction").setAccelerator(SWT.F1);
		AP.add(this.getAction("applicationWorkbench.actions.HelpMenuAction"));
        menuManager.add(AP);
	}
	
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==true){
		this.getAction("applicationWorkbench.actions.NewProject").setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("ca.ucalgary.cpsc.agilePlanner", "Icons/icon_agileplanner_new.gif"));
    	toolBarManager.add(this.getAction("applicationWorkbench.actions.NewProject"));
    	this.getAction("applicationWorkbench.actions.openProject").setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("ca.ucalgary.cpsc.agilePlanner", "Icons/icon_agileplanner_open.gif"));
    	toolBarManager.add(this.getAction("applicationWorkbench.actions.openProject"));
    	this.getAction("applicationWorkbench.actions.SaveAs").setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("ca.ucalgary.cpsc.agilePlanner", "Icons/icon_saveAs2.jpg"));
    	toolBarManager.add(this.getAction("applicationWorkbench.actions.SaveAs"));
		}
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==false){
			this.getAction("applicationWorkbench.actions.ServerConnection").setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin("ca.ucalgary.cpsc.agilePlanner", "Icons/joinsession.gif"));
	    	toolBarManager.add(this.getAction("applicationWorkbench.actions.ServerConnection"));
	    	toolBarManager.add(this.getAction("applicationWorkbench.actions.synchronizeProjectAction"));
		}
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==false)
			toolBarManager.add(saveAsAction);
    	toolBarManager.add(this.getAction("applicationWorkbench.actions.fontAction"));
		toolBarManager.add(this.getAction("applicationWorkbench.actions.ConfigureAction"));
    	toolBarManager.add(this.getAction("applicationWorkbench.actions.dropDownColorMenuAction"));
    	toolBarManager.add(this.getAction("applicationWorkbench.actions.storyCardColorIconAction"));
    	toolBarManager.add(this.getAction("applicationWorkbench.actions.textSearchAction"));
        toolBarManager.add(this.getAction("ArrangeCards"));
        toolBarManager.add(this.getAction("CollapseStoryCards"));
        toolBarManager.add(this.getAction("ExpandStoryCards"));
       // toolBarManager.add(this.getAction(ActionFactory.PRINT.getId()));
        toolBarManager.add(this.getAction(ActionFactory.DELETE.getId()));
        toolBarManager.add(this.getAction(ActionFactory.UNDO.getId()));
        toolBarManager.add(this.getAction(ActionFactory.REDO.getId()));
      
		// //////////For Zooming Fascility////////////////////////////////////

		String[] zoomStrings = new String[] { ZoomManager.FIT_ALL,
				ZoomManager.FIT_HEIGHT, ZoomManager.FIT_WIDTH };

		ZoomComboContributionItem item = new ZoomComboContributionItem(
				getPage(), zoomStrings);

		toolBarManager.add(item);
		 toolBarManager.add(new Separator());
	
		toolBarManager.add(this.getAction("applicationWorkbench.actions.HelpMenuAction"));
	}
}

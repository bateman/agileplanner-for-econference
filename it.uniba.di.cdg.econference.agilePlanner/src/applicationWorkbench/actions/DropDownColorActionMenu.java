package applicationWorkbench.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;


import cards.CardConstants;
import fitintegration.PluginInformation;

public class DropDownColorActionMenu extends Action implements IMenuCreator, ISelectionListener , IWorkbenchAction{

	public static final String ID = "applicationWorkbench.actions.dropDownColorMenuAction";

	/** The menu to be populated with items*/
	private Menu fMenu;
	protected List<StoryCardColorAction> fActions;
	StoryCardColorIconAction iconListener;


	public DropDownColorActionMenu(IWorkbenchWindow window) {
		super("&Color StoryCards", IAction.AS_DROP_DOWN_MENU);
		setId(ID);
		window.getSelectionService().addSelectionListener(this);	
		setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
				PluginInformation.getPLUGIN_ID(), CardConstants.PaintBucket));
				
		fActions = new ArrayList<StoryCardColorAction>();
		fActions.add(new StoryCardColorAction(window,"green"));		
		fActions.add(new StoryCardColorAction(window,"blue"));		
		fActions.add(new StoryCardColorAction(window,"red"));
		fActions.add(new StoryCardColorAction(window, "pink"));				
		fActions.add(new StoryCardColorAction(window,"white"));		
		fActions.add(new StoryCardColorAction(window,"yellow"));		
		fActions.add(new StoryCardColorAction(window,"peach"));		
		fActions.add(new StoryCardColorAction(window,"gray"));		
		fActions.add(new StoryCardColorAction(window,"aqua"));		
		fActions.add(new StoryCardColorAction(window,"khaki"));
	}


	private void addActionToMenu(Menu parent, IAction action) {
		ActionContributionItem item = new ActionContributionItem(action);
		if(action instanceof StoryCardColorAction)
			((StoryCardColorAction)action).setIconListener(iconListener);
		item.fill(parent, -1);
	}

	private void createEntries(Menu menu) {
		for (int i = 0; i < fActions.size(); i++) {
			IAction action = (IAction) fActions.get(i);
			addActionToMenu(menu, action);
		}
	}

	public void dispose() {
		if (fMenu != null) {
			fMenu.dispose();
			fMenu = null;
		}
	}


	public StoryCardColorIconAction getIconListener() {
		return iconListener;
	}

	
	public Menu getMenu(Control parent) {
		if (fMenu != null) {
			fMenu.dispose();
		}
		fMenu = new Menu(parent);
		createEntries(fMenu);
		return fMenu;

	}

	public Menu getMenu(Menu parent) {
		return fMenu;
	}

	public IMenuCreator getMenuCreator() {
		return this;
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				
	}

	public void setIconListener(StoryCardColorIconAction iconListener) {
		this.iconListener = iconListener;
	}


}

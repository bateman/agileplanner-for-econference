package applicationWorkbench;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;




public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

    private IWorkbenchAction exitAction;
    private IWorkbenchAction printAction;
    private IWorkbenchAction saveAsAction;
    
    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    @Override
    protected void makeActions(IWorkbenchWindow window) {
        this.printAction = ActionFactory.PRINT.create(window);
        this.printAction.setAccelerator(SWT.CTRL | 'P');
        this.register(this.printAction);
        this.exitAction = ActionFactory.QUIT.create(window);
        this.register(this.exitAction);
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==false){
        this.saveAsAction = ActionFactory.SAVE_AS.create(window);
	    this.register(this.saveAsAction);
		}
    }

    @Override
    protected void fillCoolBar(ICoolBarManager coolBar) {
      
    	IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
        
        /** The order here is important!* */

        coolBar.add(new ToolBarContributionItem(toolbar, "AgilePlanner"));
    	toolbar.add(new GroupMarker("ap"));
    	toolbar.add(this.printAction);
		if (applicationWorkbench.actions.OpenProjectActionDelegate.localMode==false)
    	toolbar.add(this.saveAsAction);

    }
}

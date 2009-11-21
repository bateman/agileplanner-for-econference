package applicationWorkbench.actions;

import org.eclipse.ui.actions.RetargetAction;


public class HelpMenuRetargetAction extends RetargetAction {

    public  HelpMenuRetargetAction(String actionID, String text) {
        super(actionID, text);
        //setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.ConfigureIcon));
    }
}

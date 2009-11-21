package applicationWorkbench.actions;

import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;


import cards.CardConstants;
import fitintegration.PluginInformation;

public class ArrangeWithStoryCardsRetargetAction extends RetargetAction {

    public ArrangeWithStoryCardsRetargetAction(String actionID, String text) {
        super(actionID, text);
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.CleanUpPlanningObjectsIcon));
    }
}

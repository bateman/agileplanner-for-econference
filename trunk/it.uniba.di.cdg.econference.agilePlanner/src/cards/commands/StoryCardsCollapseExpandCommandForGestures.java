package cards.commands;

import org.eclipse.gef.commands.Command;

import cards.model.IterationCardModel;
import cards.model.StoryCardModel;



/**
 * command for `
 * 
 * 
 * 
 * 
 * @author hkolenda
 * 
 */
public class StoryCardsCollapseExpandCommandForGestures extends Command {

    private boolean newValue = false;

    private IterationCardModel iterationCardModel = null;

    /**
     * @param storyCard
     * @param collapseExpand
     *            true to collapse , false to expand
     */
    public StoryCardsCollapseExpandCommandForGestures(IterationCardModel iterationCardModel, boolean collapseExpand) {
        this.iterationCardModel = iterationCardModel;
        newValue = collapseExpand;
    }
    


    private void collapseExpandCards(boolean collapseExpand) {
        if (collapseExpand) {

            for (StoryCardModel storyCardModel : iterationCardModel.getChildrenList()) {
                storyCardModel.collapse();            
            }
        }
        else {
            for (StoryCardModel storyCardModel : iterationCardModel.getChildrenList()) {
                storyCardModel.expand();
            }
        }
    }

    @Override
    public void execute() {
        collapseExpandCards(newValue);
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public void undo() {
        collapseExpandCards(!newValue);
    }
}

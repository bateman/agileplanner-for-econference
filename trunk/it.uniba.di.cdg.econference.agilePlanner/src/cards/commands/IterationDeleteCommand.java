/*
 * author Billy 06-9-20
 */



package cards.commands;

import java.util.ArrayList;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class IterationDeleteCommand extends Command {
    private ProjectModel parent;

    private IterationCardModel iterationCard;

    private ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();

    public void setParent(ProjectModel tableModel) {
        this.parent = tableModel;
    }

    public void setIterationCard(IterationCardModel iCard) {
        this.iterationCard = iCard;       
    }

    @Override
    public void execute() {
        this.redo();
    }

    @Override
    public void redo() {

        // if (this.parent != null){
        try {
        	if(this.iterationCard.getIterationDataObject().getRallyID()){
        		MessageBox messageBox = new MessageBox(new Shell(), SWT.ERROR_ITEM_NOT_REMOVED | SWT.OK);
        	     
        	     messageBox.setText("Can not delete Rally iteration!!!             ");
        	     messageBox.open();
        	     return;
        	}
        	
            PersisterFactory.getPersister().deleteIteration(this.iterationCard.getId());
            
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }

    @Override
    public void undo() {
        try {

            PersisterFactory.getPersister().undeleteIteration(this.iterationCard.getIterationDataObject());

        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
        catch (ForbiddenOperationException e) {
            util.Logger.singleton().error(e);
        }

    }
}

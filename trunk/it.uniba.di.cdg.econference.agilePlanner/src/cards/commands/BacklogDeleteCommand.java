/**
 * Author Billy 061003
 */
package cards.commands;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import persister.ForbiddenOperationException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.BacklogModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class BacklogDeleteCommand extends Command {
    private ProjectModel parent;

    private BacklogModel backlog;

    private ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();

    public void setParent(ProjectModel tableModel) {
        this.parent = tableModel;
    }

    public void setBacklog(BacklogModel bklog) {
        this.backlog = bklog;
        if (this.backlog != null) {
            this.storyCards.addAll(this.backlog.getChildrenList());
        }
        for (StoryCardModel card : this.storyCards) {
            card.setDragMoveDelta(new Dimension(0, 0));
        }

    }

    @Override
    public void execute() {
        if (this.parent != null) {
            try {
                PersisterFactory.getPersister().deleteBacklog(this.backlog.getId());
            }
            catch (NotConnectedException e) {
                util.Logger.singleton().error(e);
            }
            catch (ForbiddenOperationException e) {
                MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
                messageBox.setMessage(e.toString());
                messageBox.setText("Error!");
                messageBox.open();
                util.Logger.singleton().error(e);
            }
        }

    }

    @Override
    public void redo() {
        this.execute();
    }

    @Override
    public void undo() {
        try {
            PersisterFactory.getPersister().createBacklog(this.backlog.getHeight(), this.backlog.getWidth(), this.backlog.getLocation().x,
                    this.backlog.getLocation().y);
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (ForbiddenOperationException e) {
            MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage(e.toString());
            messageBox.setText("Error!");
            messageBox.open();
            util.Logger.singleton().error(e);
        }
    }
}

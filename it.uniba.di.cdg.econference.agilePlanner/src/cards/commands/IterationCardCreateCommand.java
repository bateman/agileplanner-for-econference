/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This class is the triger point from eclipse to the editor for adding a iteration to the project.
 *
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.commands;

import java.sql.Date;
import java.sql.Timestamp;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.IterationCardModel;
import cards.model.ProjectModel;

public class IterationCardCreateCommand extends Command {
    private IterationCardModel newCard;

    private final ProjectModel parent;

    private Rectangle bounds;

    public IterationCardCreateCommand(IterationCardModel newCard, ProjectModel parent, Rectangle bounds) {
        this.newCard = newCard;
        this.parent = parent;
        this.bounds = bounds;
        this.setLabel("IndexCard creation");
    }

    @Override
    public boolean canExecute() {
        return (this.newCard != null) & (this.parent != null) && (this.bounds != null);
    }

    @Override
    public void execute() {
        this.newCard.setLocation(this.bounds.getLocation());
        Dimension size = this.bounds.getSize();
        if ((size.width > 0) && (size.height > 0)) {
            this.newCard.setSize(size);
        }
        try {
            String ds = this.newCard.getEndYear() + "-" + this.newCard.getEndMonth() + "-" + this.newCard.getEndDay();
            Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());

            PersisterFactory.getPersister().createIteration(this.newCard.getName(), this.newCard.getDescription(), this.newCard.getWidth(),
                    this.newCard.getHeight(), this.newCard.getLocation().x, this.newCard.getLocation().y,
                    new Float(this.newCard.getAvailableEffort()), new Timestamp(System.currentTimeMillis()), ts, 0,this.newCard.getIterationDataObject().getRallyID());
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
    }

    @Override
    public void redo() {
        try {
            String ds = this.newCard.getEndYear() + "-" + this.newCard.getEndMonth() + "-" + this.newCard.getEndDay();
            Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());
            try {
                PersisterFactory.getPersister().undeleteIteration(this.newCard.getIterationDataObject());
            }
            catch (ForbiddenOperationException e) {
                util.Logger.singleton().error(e);
            }
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }

    }

    @Override
    public void undo()  {
        try {
            PersisterFactory.getPersister().deleteIteration(this.newCard.getId());
        }
        catch (NotConnectedException e) {
            util.Logger.singleton().error(e);
        }
        catch (IndexCardNotFoundException e) {
            util.Logger.singleton().error(e);
        }
    }

    public IterationCardModel getIterationCardModel() {
        return this.newCard;
    }
}

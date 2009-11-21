/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This class is the triger point from eclipse to the editor for changing the location and dimensions 
 *								of the storyCard
 *
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.commands;



import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.IterationCardModel;
import cards.model.StoryCardModel;

public class StoryCardUpdateSizeLocationCommand extends Command {

	private Rectangle newBounds;

	private Rectangle oldBounds;

	@SuppressWarnings("unused")
	private final ChangeBoundsRequest request;

	private final StoryCardModel card;

	public StoryCardUpdateSizeLocationCommand(StoryCardModel card,
			ChangeBoundsRequest request, Rectangle newBounds) {
		if ((card == null) || (request == null) || (newBounds == null)) {
			throw new IllegalArgumentException();
		}

		this.card = card;
		this.request = request;
		this.newBounds = newBounds;
		this.setLabel("Move");
	}

	public StoryCardUpdateSizeLocationCommand(StoryCardModel card,
			Rectangle newBounds) {

		this.card = card;
		this.request = null;
		this.newBounds = newBounds;
		this.setLabel("Move");
	}

	@Override
	public void execute() {
		this.oldBounds = new Rectangle(this.card.getLocation(), this.card.getSize());
		this.redo();
	}

	@Override
	public void redo() {

		try {
			this.card.setWidth(this.newBounds.getSize().width);
			this.card.setHeight(this.newBounds.getSize().height);
			if ((this.card.getParent() instanceof IterationCardModel) && (this.newBounds.y <= card.getParent().getLocation().y+55))
				this.card.setLocation(new Point(this.oldBounds.x,this.oldBounds.y));
			else
				this.card.setLocation(new Point(newBounds.x,newBounds.y));
			PersisterFactory.getPersister().updateStoryCard(
				this.card.getStoryCard()
			);

		} catch (NotConnectedException e) {
			util.Logger.singleton().error(e);
		} catch (IndexCardNotFoundException e) {
			util.Logger.singleton().error(e);
		}
	}

	@Override
	public void undo() {
		try {

			this.card.getStoryCard().setWidth(this.oldBounds.getSize().width);
			this.card.getStoryCard().setHeight(this.oldBounds.getSize().height);
			
			this.card.getStoryCard().setLocationX(
					this.oldBounds.getLocation().x);
			this.card.getStoryCard().setLocationY(
					this.oldBounds.getLocation().y);

			PersisterFactory.getPersister().updateStoryCard(
					this.card.getStoryCard());

		} catch (NotConnectedException e) {
			util.Logger.singleton().error(e);
		} catch (IndexCardNotFoundException e) {
			util.Logger.singleton().error(e);
		}

	}
}

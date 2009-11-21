package cards.commands;

import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.StoryCardModel;

public class CollapseExpandStoryCardCommand extends Command {
	StoryCardModel storyCard;

	public CollapseExpandStoryCardCommand(StoryCardModel storyCard) {
		super();
		this.storyCard = storyCard;
	}

	@Override
	public void execute() {
		redo();
	}

	@Override
	public void redo() {
		
			if (storyCard.getStoryCard().getHeight() == CardConstants.WindowsStoryCardSmallHeight) {
				try {
					storyCard.getStoryCard().setHeight(
							CardConstants.WindowsStoryCardheight);
					storyCard.setHeightClientArea(CardConstants.WindowsStoryCardheight);
					storyCard.setHeight(CardConstants.WindowsStoryCardheight);
					PersisterFactory.getPersister().updateStoryCard(
							storyCard.getStoryCard());
				} catch (NotConnectedException e) {
					// TODO Auto-generated catch block
					util.Logger.singleton().error(e);
				} catch (IndexCardNotFoundException e) {
					// TODO Auto-generated catch block
					util.Logger.singleton().error(e);
				}
			} else {
				try {
					if(!storyCard.getColor().equalsIgnoreCase("transparent")){
					storyCard.getStoryCard().setHeight(
							CardConstants.WindowsStoryCardSmallHeight);

					storyCard.setHeightClientArea(CardConstants.WindowsStoryCardSmallHeight);
					storyCard.setHeight(CardConstants.WindowsStoryCardSmallHeight);
					}
					PersisterFactory.getPersister().updateStoryCard(
							storyCard.getStoryCard());
				} catch (NotConnectedException e) {
					util.Logger.singleton().error(e);
				} catch (IndexCardNotFoundException e) {
					util.Logger.singleton().error(e);
				}
			}

		}
	@Override
	public void undo() {
		redo();
	}
}

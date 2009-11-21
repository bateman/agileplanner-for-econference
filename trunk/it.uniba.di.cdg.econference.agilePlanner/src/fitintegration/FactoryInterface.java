package fitintegration;

import persister.distributed.ServerCommunicator;
import cards.editpart.StoryCardEditPart;
import cards.figure.StoryCardFigure;

public interface FactoryInterface {
	
	public ServerCommunicator getServerCommunicator();
	public StoryCardEditPart getNewStoryCardEditPart();
	public StoryCardFigure getNewStoryCardFigure();
}

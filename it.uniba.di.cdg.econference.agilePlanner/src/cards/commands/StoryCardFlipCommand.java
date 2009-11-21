package cards.commands;

import org.eclipse.gef.commands.Command;

import persister.data.StoryCard;



import cards.model.StoryCardModel;

public class StoryCardFlipCommand extends Command  {

	private StoryCardModel model;
	public StoryCardFlipCommand(StoryCardModel model){
		super();
		this.model = model;
	}
	@Override
	public void execute(){
		

		
		if(this.model.getCurrentSideUp() == StoryCard.FRONT_SIDE){
			this.model.setCurrentSideUp(StoryCard.FIT_TEST_SIDE);
		}else if(this.model.getCurrentSideUp() == StoryCard.FIT_TEST_SIDE){
			this.model.setCurrentSideUp(StoryCard.PROTOTYPE_SIDE);
		}else if(this.model.getCurrentSideUp() == StoryCard.PROTOTYPE_SIDE){
			if(this.model.getStoryCard().getHandwritingImage()!=null)
			this.model.setCurrentSideUp(StoryCard.HANDWRITING_SIDE);
			else
				this.model.setCurrentSideUp(StoryCard.FRONT_SIDE);
		}else if(this.model.getCurrentSideUp() == StoryCard.HANDWRITING_SIDE){
			this.model.setCurrentSideUp(StoryCard.FRONT_SIDE);
		}
		
	}
}

package cards.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;



import cards.editpart.StoryCardEditPart;
import cards.model.AbstractRootModel;
import cards.model.StoryCardModel;

public class StoryCardColorCommand extends Command {

	private StoryCardModel model;
	private String color;
	List currentSelection;
		
	public StoryCardColorCommand(String color,List currentSelection){
	
		this.color = color;
		this.currentSelection = currentSelection;
	}
	
	@Override 
	public void execute(){

		redo();
	}
	
	@Override
    public void redo() {
		
		
List<StoryCardEditPart> storyCards = new ArrayList<StoryCardEditPart>();	    	
    	
		for(Object o: this.currentSelection){
			if(o instanceof StoryCardEditPart)
				storyCards.add((StoryCardEditPart) o);
		}
		
		for(StoryCardEditPart storyCard: storyCards){
			storyCard.getCastedModel().setColorChange(color);
			storyCard.refreshVisuals();
			currentSelection=null;
		}
		
	}
	
	 @Override
	    public void undo() {}
	 
}

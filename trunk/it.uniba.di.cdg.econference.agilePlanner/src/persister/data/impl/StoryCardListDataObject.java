package persister.data.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import persister.data.StoryCard;
import persister.data.StoryCardList;

public class StoryCardListDataObject implements StoryCardList {

	private List<StoryCard> storyCards = new ArrayList<StoryCard>();
	
	public int storyCardSize() {
		return storyCards.size();
	}

	public void addStoryCard(StoryCard storycard) {
		storyCards.add(storycard);
	}

	public List<StoryCard> getStoryCardChildren() {
		return Collections.unmodifiableList(storyCards);
	}

	public void removeStoryCard(StoryCard storycard) {
		storyCards.remove(storycard);
	}

	public void setStoryCardChildren(List<StoryCard> storycards) {
		this.storyCards = storycards;
	}
	
	public StoryCard getStoryCard(int i){
		return this.storyCards.get(i);
	}
	
	public StoryCardList clone(){
		StoryCardList list = new StoryCardListDataObject();
		for(StoryCard storyCard : getStoryCardChildren()){
			list.addStoryCard((StoryCard)storyCard.clone());
		}
		return list;
	}
	

}

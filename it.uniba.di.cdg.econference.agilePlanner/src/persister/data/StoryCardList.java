package persister.data;

import java.util.List;

public interface StoryCardList {
	
	public void addStoryCard(StoryCard storycard);
    public List<StoryCard> getStoryCardChildren();
    public void removeStoryCard(StoryCard storycard);
    public void setStoryCardChildren(List<StoryCard> storycards);
    public int storyCardSize();
    public StoryCard getStoryCard(int i);
    public StoryCardList clone();
    
    
}

package cards.editpart;

import cards.figure.StoryCardFigure;

public class StoryCardEditPartBuilder {

	private StoryCardFigure figure;
	
	public StoryCardEditPartBuilder(StoryCardFigure figure){
		this.figure = figure;
	}

	public void addLabel(String text){
		
	}
	
	public StoryCardFigure getFigure() {
		return figure;
	}

	public void setFigure(StoryCardFigure figure) {
		this.figure = figure;
	}
}

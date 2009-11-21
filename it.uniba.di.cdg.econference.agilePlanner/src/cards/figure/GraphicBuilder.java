package cards.figure;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;

import cards.CardConstants;
import cards.model.StoryCardModel;


public class GraphicBuilder {

	private boolean nextEstimateLabelOnLeftSide = true;
	private Graphics graphic;
	private Rectangle clip; 
	private int fontSize;
	
	
	public GraphicBuilder(Graphics g, Rectangle clip, int fontSize) {
		this.setGraphic(g);
		this.setClip(clip);
		this.setFontSize(fontSize);
	}
	
	public void drawEstimate( String title, int writeToLine ){
		int xLocation = StoryCardModel.INITIAL_X_OFFSET;
		int yLocation = clip.y + StoryCardModel.INITIAL_Y_OFFSET + StoryCardModel.LINE_HEIGHT * writeToLine;
		
		if (nextEstimateLabelOnLeftSide == false) {
            if(getFontSize()>8){
            	xLocation += getClip().x + (getFontSize()*10);
            } else {
            	xLocation += clip.x + StoryCardModel.ESTIMATE_RIGHT_SIDE_OFFSET;	
            }
            nextEstimateLabelOnLeftSide = true;
        } else  {
            xLocation += clip.x + StoryCardModel.ESTIMATE_LEFT_SIDE_OFFSET;
            nextEstimateLabelOnLeftSide = false;
        }
		
		getGraphic().drawText(title, xLocation, yLocation);
	}
	
	public void drawOwner(String title, int writeToLine){
		int xLocation = StoryCardModel.INITIAL_X_OFFSET + getClip().x + getFontSize() * 16 + 50,
			yLocation = getClip().y + StoryCardModel.INITIAL_Y_OFFSET + StoryCardModel.LINE_HEIGHT * writeToLine;
		getGraphic().drawText(title, xLocation, yLocation);
	}

	public Graphics getGraphic() {
		return graphic;
	}

	public void setGraphic(Graphics graphic) {
		this.graphic = graphic;
	}

	public Rectangle getClip() {
		return clip;
	}

	public void setClip(Rectangle clip) {
		this.clip = clip;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
}

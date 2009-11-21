package cards.commands;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import cards.model.DrawingAreaModel;
import cards.model.StoryCardModel;



public class DrawingAreaUpdateSizeLocationCommand extends Command {

	
	 private Rectangle newBounds;

	    private Rectangle oldBounds;

	    @SuppressWarnings("unused")
	    private final ChangeBoundsRequest request;

	    private DrawingAreaModel card;

	    private ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();

	    public DrawingAreaUpdateSizeLocationCommand(DrawingAreaModel card, ChangeBoundsRequest request, Rectangle newBounds) {
	        if ((card == null) || (request == null) || (newBounds == null)) {
	            throw new IllegalArgumentException();
	        }
	        this.card = card;
	        this.request = request;
	        this.newBounds = newBounds;
	        
	        this.oldBounds = new Rectangle(card.getLocation(), card.getSize());
	        
	        
	    }

	    @Override
	    public void execute() {

	        this.redo();
	    }

	    @Override
	    public void redo() {
	    	this.card.setSize(this.newBounds.getSize());
	    	this.card.setLocation(this.newBounds.getLocation());
	    	
	    	Image drawing = this.card.getDrawing();
	    	Image newImage = new Image(Display.getCurrent(), (Math.max(this.newBounds.width, drawing.getBounds().width)), (Math.max(this.newBounds.height, drawing.getBounds().height)));
	    	this.card.setImage(newImage, drawing);
	    	
	    	drawing.dispose();
	    	
	    	this.card.refreshDrawingArea();
	        

	    }

	    @Override
	    public void undo() {

	        

	    }

	    
}

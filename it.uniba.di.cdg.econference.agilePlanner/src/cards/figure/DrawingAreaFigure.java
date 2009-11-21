package cards.figure;



import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import cards.CardConstants;





public class DrawingAreaFigure extends Viewport{

	
	 private DrawingAreaFigure figure;

	 private Image image;

	    public DrawingAreaFigure() {
	        this.figure = this;
	        this.setLayoutManager(new FreeformLayout());
	        this.setOpaque(true);
	        this.addMouseListener(new MouseListener() {

	            public void mousePressed(MouseEvent me) {
	                IFigure parent = DrawingAreaFigure.this.getParent();
	                parent.remove(DrawingAreaFigure.this.figure);
	                parent.add(DrawingAreaFigure.this.figure);
	                
	                
	            }

	            public void mouseReleased(MouseEvent me) {
	                // Auto-generated method stub
	            }

	            public void mouseDoubleClicked(MouseEvent me) {
	                // Auto-generated method stub
	            	
	            }
	            
	            
	        });
	    }


	    /**
	     * Method that paints the figure to the graphics layer. Colour and other
	     * visual aspects get added here.
	     */
	    @Override
	    protected void paintFigure(Graphics g) {
	        Rectangle clip = this.getClientArea();

	        int nameLine = CardConstants.BACKLOGFIGURENAMELINE;

	        if (this.isOpaque()) {
	            g.setAlpha(255);
	            g.setBackgroundColor(new Color(null, 255, 255, 255));
	            g.fillRectangle(clip);
	            // Draw the dividing lines on the card.
	            g.setForegroundColor(new Color(null, 100, 100, 100));
	            g.drawLine(clip.x, clip.y + nameLine, clip.x + clip.width, clip.y + nameLine);
	            // Draw the text
	            g.setForegroundColor(new Color(null, 1, 1, 1));
	            g.drawText("User Interface Scratch Pad", clip.x + 5, clip.y + 5);
	            // Draw the border around the card
	            g.drawRectangle(clip.x, clip.y, clip.width - 1, clip.height - 1);
	            g.drawImage(image, clip.x+1,clip.y + nameLine+ 1);
	            
//	            for(InkPixel point: pixels){
//	            	g.drawPoint(point.x, point.y);
//	            }
//	            for(int i = 0; i < 500; i++){
//	            	for(int j = 0; j < 500; j++){
//	            		if(pixels[i][j]){
//	            			g.drawPoint(i, j);
//	            		}
//	            		
//	            	}
//	            }
	        }
	        else {

	        }
	    }
	    
	    public void setImage(Image image){
	    	this.image  = image;
	    }
	    
	    
}

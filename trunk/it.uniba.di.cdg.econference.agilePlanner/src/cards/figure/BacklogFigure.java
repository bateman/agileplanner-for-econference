/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a figure class for the backlog object. Here is where all of the non UI elements of the figure are 
 *								controled. There is also a mouse listener in this class that should not be used for updating any of the 
 *								models or controlers information. This will only listen to the figure object and is not a good place for any 
 *								code that interacts with non figure information. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.figure;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import cards.CardConstants;



public class BacklogFigure extends Viewport {
	
	private static final Font DISPLAY_FONT = new Font(null,"",8,0); // Font for printing. RM-6/18/2007
	
    private BacklogFigure figure;

    private String nameField;

    private String nameLabel = "Name: ";

    public BacklogFigure() {
        this.figure = this;
        this.setLayoutManager(new FreeformLayout());
        this.setOpaque(true);
        this.addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent me) {
                IFigure parent = BacklogFigure.this.getParent();
                parent.remove(BacklogFigure.this.figure);
                parent.add(BacklogFigure.this.figure);
            }

            public void mouseReleased(MouseEvent me) {
                // Auto-generated method stub
            }

            public void mouseDoubleClicked(MouseEvent me) {
                // Auto-generated method stub
            }
        });
    }

    public void setNameText(String name) {
        this.nameField = name;
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
        	
        	g.setFont(DISPLAY_FONT);// Font for printing. RM-6/18/2007
        	
            g.setAlpha(255);
            g.setBackgroundColor(new Color(null, 120, 244, 120));
            g.fillRectangle(clip);
            // Draw the dividing lines on the card.
            g.setForegroundColor(new Color(null, 100, 100, 100));
            g.drawLine(clip.x, clip.y + nameLine, clip.x + clip.width, clip.y + nameLine);
            // Draw the text
            g.setForegroundColor(new Color(null, 1, 1, 1));
            g.drawText(this.nameLabel + this.nameField, clip.x + 5, clip.y + 5);
            // Draw the border around the card
            g.drawRectangle(clip.x, clip.y, clip.width - 1, clip.height - 1);
        }
        else {

        }
    }
}

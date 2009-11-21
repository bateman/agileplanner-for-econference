package cards.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import cards.model.StoryCardModel;



/**
 * this class simulates the rotation of the storycard
 * 
 * @author hkolenda
 * 
 */
public class StoryCardRotatedCommand extends Command {

    private StoryCardModel storyCardModel = null;

    private Point location = null;

    private Dimension size = null;

    private double angle = 0;

    /**
     * this is an body to see, that the roation gesture works, implementation of
     * functionalaty is needed
     * 
     * @param storyCard
     * @param location
     * @param size
     * @param angle
     */
    public StoryCardRotatedCommand(StoryCardModel storyCard, Point location, Dimension size, double angle) {

    }

    @Override
    public void execute() {
        this.setLabel("IndexCard rotated (" + Math.toDegrees(angle) + " degrees), resized(" + size + ") and moved(" + location + ")");

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    @Override
    public boolean canExecute() {
        // TODO Auto-generated method stub
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canUndo()
     */
    @Override
    public boolean canUndo() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void redo() {
        super.redo();
    }

    @Override
    public void undo() {
        super.undo();
    }

}

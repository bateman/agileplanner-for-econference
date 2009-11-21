package cards.commands;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.GC;

import cards.model.StoryCardModel;

import applicationWorkbench.Editor;



/**
 * this class simulates the rotation of the storycard this class is a helper
 * class and may should be implemented as a command
 * 
 * @author hkolenda
 * 
 */
public class StoryCardSetBestCaseCommand extends Command {

    private Point currentPoint = new Point();

    private static SWTGraphics graph = null;

    private StoryCardModel storyCard = null;

    private Editor editor = null;

    public StoryCardSetBestCaseCommand(StoryCardModel storyCard, Point point, Editor editor) {
        try {

            this.currentPoint = point;
            this.editor = editor;
            this.storyCard = storyCard;

            this.setLabel("StoryCard setting best case");

            final Editor e = editor;
            editor.getViewer().getControl().getDisplay().syncExec(new Runnable() {

                public void run() {
                    // 
                    graph = new SWTGraphics(new GC(e.getViewer().getControl()));

                }
            });

        }
        catch (Exception e) {
            util.Logger.singleton().error(e);
        }
    }

    @Override
    public void execute() {

        try {
            final Point midPoint = storyCard.getLocation();
            midPoint.x += storyCard.getSize().width / 2;
            midPoint.y += storyCard.getSize().height / 2;
            
            Double distance = currentPoint.getDistance(midPoint);
            distance = distance * 0.025;

            final String dist;

            int rnd = new Double(distance * 100).intValue() % 100;

            if (rnd < 25) {
                dist = String.valueOf(distance.longValue()) + ".0";

            }
            else
                if ((rnd >= 25) && (rnd < 75)) {
                    dist = String.valueOf(distance.longValue()) + ".5";
                }
                else {
                    dist = String.valueOf(distance.longValue() + 1) + ".0";
                }

            storyCard.setBestCaseEstimate(dist);

            editor.getViewer().getControl().getDisplay().syncExec(new Runnable() {

                public void run() {
                }

            });

        }
        catch (Exception e) {
            util.Logger.singleton().error(e);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canExecute()
     */
    @Override
    public boolean canExecute() {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.commands.Command#canUndo()
     */
    @Override
    public boolean canUndo() {
        return false;
    }

}

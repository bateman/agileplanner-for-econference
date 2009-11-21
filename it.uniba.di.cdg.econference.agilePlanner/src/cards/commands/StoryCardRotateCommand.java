package cards.commands;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Transform;
import org.eclipse.swt.graphics.Color;
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
public class StoryCardRotateCommand {

    private StoryCardModel storyCardModel = null;

    private double initialAngle = 0;

    private int storyCardInitialWidth = 0;

    private int storyCardInitialHeight = 0;

    private SWTGraphics graph = null;

    private double initialDistance = 0;

    private Transform trans = null;

    private Editor editor = null;

    public StoryCardRotatedCommand finish() {
        StoryCardRotatedCommand com = new StoryCardRotatedCommand(storyCardModel, new Point(), new Dimension(), 0);
        return com;
    }

    public void drawRotatetedStorycard(Point currentLower, Point currentUpper) {
        try {

            PointList inputPointList = new PointList(2);
            inputPointList.addPoint(currentLower);
            inputPointList.addPoint(currentUpper);
            Point midPoint = inputPointList.getMidpoint();

            double currentDistance = Math.sqrt(Math.pow(currentUpper.x - currentLower.x, 2) + Math.pow(currentUpper.y - currentLower.y, 2));

            double currentAngle = Math.atan2(currentUpper.x - currentLower.x, currentUpper.y - currentLower.y);

            trans.setRotation(initialAngle - currentAngle);
            trans.setScale(Math.log(Math.abs(currentDistance) / 100) + 1);

            final PointList pList = new PointList();
            pList.addPoint(-storyCardInitialWidth / 2, -storyCardInitialHeight / 2);
            pList.addPoint(storyCardInitialWidth / 2, -storyCardInitialHeight / 2);
            pList.addPoint(storyCardInitialWidth / 2, storyCardInitialHeight / 2);
            pList.addPoint(-storyCardInitialWidth / 2, storyCardInitialHeight / 2);

            final PointList pointList = new PointList(4);

            for (int i = 0; i < pList.size(); i++) {
                Point p = pList.getPoint(i);
                p = trans.getTransformed(p);
                p.x = p.x + midPoint.x;
                p.y = p.y + midPoint.y;
                pointList.addPoint(p);
            }

            final int offset = 100;

            final Rectangle redrawBounds = pointList.getBounds();

            editor.getViewer().getControl().getDisplay().syncExec(new Runnable() {

                public void run() {
                    editor.getViewer().getControl().redraw(redrawBounds.x - offset, redrawBounds.y - offset,
                            redrawBounds.x + redrawBounds.width + offset, redrawBounds.y + redrawBounds.height + offset, false);
                    // editor.getViewer().getControl().redraw();

                    graph.drawPolygon(pointList);
                }

            });

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            util.Logger.singleton().error(e);
        }
    }

    public void setColor(final Color color) {
        editor.getViewer().getControl().getDisplay().syncExec(new Runnable() {

            public void run() {
                graph.setForegroundColor(color);

            }
        });

    }

    public StoryCardRotateCommand(StoryCardModel storyCard, Point initLower, Point initUpper, Editor editor) {
        try {
            this.storyCardModel = storyCard;

            storyCardInitialWidth = storyCardModel.getSize().width;
            storyCardInitialHeight = storyCardModel.getSize().height;
            

            initialDistance = Math.sqrt(Math.pow(initUpper.x - initLower.x, 2) + Math.pow(initUpper.y - initLower.y, 2));

            initialAngle = Math.atan2(initUpper.x - initLower.x, initUpper.y - initLower.y);

            trans = new Transform();

            this.editor = editor;
            final Editor e = editor;

            editor.getViewer().getControl().getDisplay().syncExec(new Runnable() {

                public void run() {
                    // 
                    graph = new SWTGraphics(new GC(e.getViewer().getControl()));
                    graph.setLineWidth(3);

                }
            });

        }
        catch (Exception e) {
            util.Logger.singleton().error(e);
        }
    }

}

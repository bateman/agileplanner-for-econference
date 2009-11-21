package palette;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;

import cards.editpart.DrawingAreaEditPart;
import cards.editpart.ProjectEditPart;
import cards.model.DrawingAreaModel;
import cards.model.ProjectModel;



public class PenTool extends AbstractTool{

	
	private DrawingAreaModel model = null;
	
	public PenTool(){
		
	}
	@Override
	protected String getCommandName() {
		return "TEST";
		
	}
	
	@Override
	public void mouseDrag(MouseEvent me, EditPartViewer viewer) {
		
		Point viewLocation = ((FigureCanvas)
				getCurrentViewer().getControl()).getViewport().getViewLocation();
		
		try{
			ProjectEditPart project = (ProjectEditPart) viewer.getContents();
		
			ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) project.getRoot();
			double ratio = root.getZoomManager().getZoom();
	
			int actX = (int)(viewLocation.x/ratio + me.x/ratio);
			int actY = (int)(viewLocation.y/ratio + me.y/ratio);
			EditPart underMouse = viewer.findObjectAt(new Point(me.x, me.y));
			if(underMouse instanceof DrawingAreaEditPart){
				
				DrawingAreaEditPart drawingArea= (DrawingAreaEditPart) underMouse;
				drawingArea.plotPoint(actX, actY);
				drawingArea.refreshVisuals();
				this.model = drawingArea.getCastedModel();
			}
		}catch(Exception e){
			util.Logger.singleton().error(e);
		}
		
	}
	
	@Override
	protected boolean handleButtonUp(int button){
		if(model != null){
			model.liftPen();
		}
		
		return super.handleButtonUp(button);
		
	}
	
	private class InkPaintCallback implements PaintListener{

		private ProjectModel project;
		public InkPaintCallback(ProjectModel model){
			this.project = model;
		}
		public void paintControl(PaintEvent e) {
			try{
				e.gc.setForeground(new Color(e.display, 0,0,0));
				e.gc.drawPoint(10, 10);
			}catch(Exception ex){
				util.Logger.singleton().error(ex);
			}
			
		}
		
	}

}

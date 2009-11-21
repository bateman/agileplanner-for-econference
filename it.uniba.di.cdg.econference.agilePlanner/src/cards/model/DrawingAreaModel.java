package cards.model;



//import java.awt.Graphics2D;
//import java.awt.GraphicsConfiguration;
//import java.awt.GraphicsDevice;
//import java.awt.GraphicsEnvironment;
//import java.awt.Toolkit;
//import java.awt.Transparency;
//import java.awt.image.BufferedImage;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import persister.DrawingArea;
import persister.data.impl.DrawingAreaDataObject;


import cards.CardConstants;
import fitintegration.PluginInformation;

public class DrawingAreaModel extends ContainerModel{

	public static final String CHILD_ADDED_PROP = "DrawingAreaModel.ChildAdded";

	public static final String CHILD_REMOVED_PROP = "DrawingAreaModel.ChildRemoved";

	public static final String LOCATION_PROP = "DrawingAreaModel.location";

	public static final String NAME_PROP = "DrawingAreaModel.name";

	public static final String SIZE_PROP = "DrawingAreaModel.size";

	public static final String X_PROP = "DrawingAreaModel.x";

	public static final String Y_PROP = "DrawingAreaModel.y";
	
	Image image;
	GC graphics;

	private int oldX = -1;

	private int oldY = -1;
	
	public DrawingAreaModel(DrawingArea data){
		this.setCardDataObject(data);
	    image = new Image(Display.getCurrent(), this.getWidth() - 2, this.getHeight() - CardConstants.BACKLOGFIGURENAMELINE - 2);
	    graphics = new GC(image);
	}
	
	public DrawingAreaModel(){
		this(new DrawingAreaDataObject());
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4363604441182904111L;

	@Override
	public Image getIcon() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				PluginInformation.getPLUGIN_ID(), CardConstants.NewBacklogIcon)
				.createImage();
	}

	@Override
	public String getStatus() {
		return null;
	}

	public void setSize(Dimension size){
		this.setHeight(size.height);
		this.setWidth(size.width);
	}
	
	public void  refreshDrawingArea(){
		this.firePropertyChange(LOCATION_PROP, null, this.getLocation());
	}

	@Override
	public void setComplete(String status) {
	}

	public void plotPoint(int x, int y) {
		int relativeX = x - this.getLocation().x;
		
		
		int yOffset = CardConstants.BACKLOGFIGURENAMELINE;
		int relativeY = y - this.getLocation().y - yOffset;
		
		if(oldX != -1 && oldY != -1)
			this.graphics.drawLine(oldX, oldY, relativeX, relativeY);
		
		this.oldX = relativeX;
		this.oldY = relativeY;
	}
	
	public Image getDrawing(){
		return this.image;
	}
	
	public void liftPen(){
		this.oldX = -1;
		this.oldY = -1;
	}
	
	public void setImage(Image image, Image oldImage){
		this.image = image;
		this.graphics.dispose();
		this.graphics = new GC(this.image);
		this.graphics.drawImage(oldImage, 0, 0);
	}
		
	
}

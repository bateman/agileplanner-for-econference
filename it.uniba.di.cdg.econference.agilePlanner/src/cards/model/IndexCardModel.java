/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: 
 *											  
 ***************************************************************************************************/
package cards.model;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;

import persister.data.IndexCard;

import applicationWorkbench.RallyDemoGEFPlugin;




public abstract class IndexCardModel extends AbstractRootModel implements Comparable<IndexCardModel> {

    private final Image CARD_ICON = null; // AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(),CardConstants.IndexCardIcon).createImage();

    private IndexCard dataObject;

    private Timestamp timeStamp = new Timestamp(0);

    protected Image createImage(String name) {
        InputStream stream = RallyDemoGEFPlugin.class.getResourceAsStream(name);
        Image image = new Image(null, stream);
        try {
            stream.close();
        }
        catch (IOException e) {
        	util.Logger.singleton().error(e);
        }
        return image;
    }

    protected IndexCard getDataObject() {
        return dataObject;
    }

    protected void setCardDataObject(IndexCard dataObject) {
        this.dataObject = dataObject;
    }

    public int compareTo(IndexCardModel arg0) {
        if (this.timeStamp == arg0.getTimestamp()) {
            return 0;
        }
        else
            if (this.timeStamp.before(arg0.getTimestamp())) {
                return -1;
            }
            else {
                return 1;
            }
    }

    public int getHeight() {
        return getDataObject().getHeight();
    }

    /**
     * Children must overide this method. Should return an appropriate image
     * 
     * @return
     */
    public abstract Image getIcon();

    public long getId() {
        return getDataObject().getId();
    }

    public Point getLocation() {
        return new Point(getDataObject().getLocationX(), getDataObject().getLocationY());
    }

    public Dimension getSize() {
        return new Dimension(getDataObject().getWidth(), getDataObject().getHeight());
    }

    public abstract String getStatus();

    public Timestamp getTimestamp() {
        return this.timeStamp;
    }

    public int getWidth() {
        return getDataObject().getWidth();
    }

    public void setHeight(int height) {
        getDataObject().setHeight(height);
    }

    public abstract void setComplete(String status);

    public void setTimestamp(Timestamp ts) {
        this.timeStamp = ts;
    }

    public void setWidth(int width) {
        getDataObject().setWidth(width);
    }

}
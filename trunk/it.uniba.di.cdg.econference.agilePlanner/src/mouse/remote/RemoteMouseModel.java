package mouse.remote;

import java.sql.Timestamp;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import persister.Event;
import persister.data.impl.EventDataObject;


import cards.CardConstants;
import cards.model.IndexCardModel;
import cards.model.StoryCardModel;
import fitintegration.PluginInformation;

public class RemoteMouseModel extends IndexCardModel {
    private static final Image MOUSE_ICON = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.RemoteMouseIcon).createImage();

    private static final long serialVersionUID = 1;

    public static final String LOCATION_PROP = "RemoteMouseModel.location";

    public static final String SIZE_PROP = "RemoteMouseModel.size";

    private Event event = new EventDataObject();

    public RemoteMouseModel(Event mm) {
        super();

        event = mm;
    }

    public long getClientId() {
        return event.getId();
    }

    public String getClientName() {
        return event.getName();
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public Image getIcon() {
        return MOUSE_ICON;
    }

    public Point getLocation() {
        // return location.getCopy();
        return new Point(event.getLocationX(), event.getLocationY());
    }

    public Dimension getSize() {
        // return size.getCopy();
        return new Dimension(28, 28);
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    public void setClientId(long id) {
        event.setId(id);
    }

    public void setLocation(Point newLocation) {
        if (newLocation == null) {
            throw new IllegalArgumentException();
        }
        event.setLocationX(newLocation.x);
        event.setLocationY(newLocation.y - (StoryCardModel.LINE_HEIGHT*2));
        firePropertyChange(LOCATION_PROP, null, new Point(event.getLocationX(), event.getLocationY()));
    }

    /** THIS SHOULD BE COMMENTED OUT TO PREVENT RESIZING* */
    public void setSize(Dimension newSize) {
        if (newSize == null) {
            throw new IllegalArgumentException();
        }
        firePropertyChange(SIZE_PROP, null, newSize);
    }

    @Override
    public void setComplete(String status) {
    }

    @Override
    public String toString() {
        return "RemoteMouse " + hashCode();
    }

    
    @Override
    public Timestamp getTimestamp() {
    return new Timestamp(System.currentTimeMillis()+300000);
    }
}

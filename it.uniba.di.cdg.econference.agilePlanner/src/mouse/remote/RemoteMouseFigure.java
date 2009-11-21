package mouse.remote;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;


import cards.CardConstants;
import fitintegration.PluginInformation;

public class RemoteMouseFigure extends ImageFigure {
    private String name = "";

    public RemoteMouseFigure() {
        setLayoutManager(new XYLayout());
        setOpaque(true);
        ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.RemoteMouseIcon);
        setImage(id.createImage());
    }

    public void paintFigure(Graphics g) {
       
        if (isOpaque()) {
            g.setAlpha(255);
            g.drawImage(getImage(), getClientArea().x, getClientArea().y);
            g.drawText(this.name, getClientArea().x + 2, getClientArea().y + 12);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

}

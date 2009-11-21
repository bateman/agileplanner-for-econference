package palette;

import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;


import cards.CardConstants;
import fitintegration.PluginInformation;

public class PenToolEntry extends ToolEntry{

	public PenToolEntry(String label, String shortDesc, ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
		super(label, shortDesc, iconSmall, iconLarge);
		setToolClass(PenTool.class);
		
	}
	
	
	public PenToolEntry(){
		this("Drawing Pen", "A Drawing Pen for User Interface Drawings",AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.PENTOOL),AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.PENTOOL));
	}

}

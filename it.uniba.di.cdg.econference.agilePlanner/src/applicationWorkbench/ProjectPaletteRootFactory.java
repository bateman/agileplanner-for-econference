package applicationWorkbench;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import palette.PenToolEntry;



import cards.CardConstants;
import cards.model.IterationCardModel;
import cards.model.StoryCardModel;
import cards.model.StoryCardModelAqua;
import cards.model.StoryCardModelBlue;
import cards.model.StoryCardModelGreen;
import cards.model.StoryCardModelGrey;
import cards.model.StoryCardModelKhaki;
import cards.model.StoryCardModelPeach;
import cards.model.StoryCardModelPink;
import cards.model.StoryCardModelRed;
import cards.model.StoryCardModelTransparent;
import cards.model.StoryCardModelWhite;
import cards.model.StoryCardModelYellow;
import fitintegration.PluginInformation;

public class ProjectPaletteRootFactory {

    @SuppressWarnings("unused")
    private static final String PALETTE_DOCK_LOCATION = "TablePaletteRootFactory.Location";

    @SuppressWarnings("unused")
    private static final String PALETTE_SIZE = "TabletPaletteRootFactory.Size";

    @SuppressWarnings("unused")
    private static final String PALETTE_STATE = "TablePaletteRootFactory.State"; 

    public  static PaletteRoot createPalette(Editor editor) {
    	
        PaletteRoot palette = new PaletteRoot();
        PaletteGroup toolGroup = new PaletteGroup("Options");
        PaletteGroup emptyGroup = new PaletteGroup("");
        PaletteGroup legend = new PaletteGroup("Legend");

    	ToolEntry tool = new PanningSelectionToolEntry();
    	toolGroup.add(tool);
    	toolGroup.add(new MarqueeToolEntry());
      
    	CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry("New Iteration", "Create a new Iteration", IterationCardModel.class, new SimpleFactory(
                IterationCardModel.class), AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.NewIterationIcon),
                AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.NewIterationIcon));
        toolGroup.add(component);
        
    	 component = new CombinedTemplateCreationEntry("Text Area", "Create a new Text area", StoryCardModelTransparent.class, new SimpleFactory(
    			 StoryCardModelTransparent.class), AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.NewTextAreaIcon),
                AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.NewTextAreaIcon));
        toolGroup.add(component);

       //Legend  
        
        ImageDescriptor id;
        
        component = new CombinedTemplateCreationEntry("Create User Story",null,null,null,null);
        legend.add(component);
        component = new CombinedTemplateCreationEntry("",null,null,null,null);
        legend.add(component);
        
        String [] colors = new String [] {"red", "green", "blue", "white", "yellow", "pink", "grey", "khaki", "peach", "aqua"};
        for(int i =0; i< colors.length; i++){
        	legend.add(getColorCardComponent(
        			editor.getLegendColor(colors[i]),
        			StoryCardModel.getChildClassOfColor(colors[i]),
        			CardConstants.getColorCardsIconLocation(colors[i])
        		)
        	);
        }
        
        palette.add(toolGroup);  
        palette.add(emptyGroup);
        palette.add(legend);
    	
        return palette;
    }
    
    public static CombinedTemplateCreationEntry getColorCardComponent(String color, Object card, String constantIconColor){
    	ImageDescriptor id = AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation
				.getPLUGIN_ID(),
				constantIconColor);
    	return new CombinedTemplateCreationEntry(color, "Create StoryCard", card,new SimpleFactory((Class)card),id,id);
    }
    
    
}

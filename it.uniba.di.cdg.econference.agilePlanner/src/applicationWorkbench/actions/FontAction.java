package applicationWorkbench.actions;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;


import cards.CardConstants;
import cards.editpart.IterationCardEditPart;
import cards.editpart.StoryCardEditPart;
import fitintegration.PluginInformation;

	public class FontAction extends Action implements ISelectionListener , IWorkbenchAction{

		
		private final IWorkbenchWindow window;
		public static final String ID = "applicationWorkbench.actions.fontAction";

		
		private String fontStyle;
		private int fontSize;
		private List currentSelection = null;
		
		  public FontAction(IWorkbenchWindow window) {
			
		        this.window = window;
		        setId(ID);
		        window.getSelectionService().addSelectionListener(this);
		      setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.FontIcon));
		          
		    }
		


		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (selection instanceof IStructuredSelection && ((StructuredSelection)selection).getFirstElement() instanceof StoryCardEditPart) {
			selection = (IStructuredSelection) selection;
	            
	            if(selection instanceof IStructuredSelection && ((StructuredSelection)selection).getFirstElement().getClass() == IterationCardEditPart.class)
	            	selection = (IStructuredSelection) selection;
	            
	            this.currentSelection =  ((StructuredSelection)selection).toList();
	           	         
	            setEnabled(true);
	            
	        }
	        else
	            setEnabled(false);
	    }
		
		    
		    public void run() {
		    	
		    	FontDialog fd = new FontDialog(window.getShell(), SWT.NONE);
		        fd.setText("Font Dialog");
		        
		       
		        FontData d = fd.open();
		       
		        if(d.getName().equalsIgnoreCase("Arial Black") || d.getName().equalsIgnoreCase("Arial Rounded MT Bold"))
		        	this.fontStyle="Arial Narrow";
		        else if(d.getName().equalsIgnoreCase("Bodoni MT Black"))
		        	this.fontStyle="Bodoni";
		        else if(d.getName().equalsIgnoreCase("Bookman Old Style"))
		        	this.fontStyle="Book Antiqua";
		        else if(d.getName().equalsIgnoreCase("Castellar")||d.getName().equalsIgnoreCase("Copperplate Gothic Bold")||d.getName().equalsIgnoreCase("Copperplate Gothic Light")||d.getName().equalsIgnoreCase("Courier"))
		        	this.fontStyle="Century Schoolbook";
		        else if(d.getName().equalsIgnoreCase("Courier New")||d.getName().equalsIgnoreCase("Elephant")||d.getName().equalsIgnoreCase("Engravers MT")||d.getName().equalsIgnoreCase("Eras Bold ITC"))
		        	this.fontStyle="Eras Demi ITC";
		        else if(d.getName().equalsIgnoreCase("Felix Titling")||d.getName().equalsIgnoreCase("Fixedsys")||d.getName().equalsIgnoreCase("Gill Sans Ultra Bold Condensed")||d.getName().equalsIgnoreCase("Gloucester MT Extra Condensed")||d.getName().equalsIgnoreCase("Goudy Stout"))
		        	this.fontStyle="Impact";
		        
		        else if(d.getName().equalsIgnoreCase("Lucida Console")||d.getName().equalsIgnoreCase("Lucida Sans")||d.getName().equalsIgnoreCase("Lucida Sans Typewriter")||d.getName().equalsIgnoreCase("Lucida Sans Unicode")||d.getName().equalsIgnoreCase("Modern")||d.getName().equalsIgnoreCase("MS Reference Sans Serif"))
		        	this.fontStyle="Eras Light ITC";
		        else if(d.getName().equalsIgnoreCase("OCR A Extended")||d.getName().equalsIgnoreCase("Palatino Linotype")||d.getName().equalsIgnoreCase("Palatino Linotype")) 
		        	this.fontStyle="Eras Light ITC";
		        else if(d.getName().equalsIgnoreCase("Papyrus")||d.getName().equalsIgnoreCase("Perpetua Titling MT")||d.getName().equalsIgnoreCase("Rockwell Extra Bold")||d.getName().equalsIgnoreCase("Roman")||d.getName().equalsIgnoreCase("Script")) 
		        	this.fontStyle="Bodoni";
		        else if(d.getName().equalsIgnoreCase("Terminal")||d.getName().equalsIgnoreCase("Verdana")) 
		        	this.fontStyle="Vrinda";
		        
		        
		        	else{
		        this.fontStyle=d.getName();
		       
		        }
		        
		        if(d.getHeight()>28)
		        fontSize=28;
		        
		        else
		        this.fontSize=d.getHeight();
		        
		        
		        

		    	List<StoryCardEditPart> storyCards = new ArrayList<StoryCardEditPart>();
				for(Object o: this.currentSelection){
					if(o instanceof StoryCardEditPart)
						storyCards.add((StoryCardEditPart) o);
					
				}
				
				for(StoryCardEditPart storyCard: storyCards){
					storyCard.getCastedModel().setStoryCardFont(fontStyle);
					storyCard.getCastedModel().setStoryCardFontSize(fontSize);
					storyCard.setStoryCardFontSize(fontSize);
					storyCard.setStoryCardFont(fontStyle);
					storyCard.refreshVisuals();
					
				}
	            
		    }

		    public void dispose() {
		        window.getSelectionService().removeSelectionListener(this);
		    }

		   
	}

	
	



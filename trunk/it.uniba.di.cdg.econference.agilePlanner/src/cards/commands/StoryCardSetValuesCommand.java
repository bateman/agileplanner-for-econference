/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This class is the triger point from eclipse to the editor for changing the fields of the story card objects
 *
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.commands;

import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.StoryCardModel;

public class StoryCardSetValuesCommand extends Command{
	@SuppressWarnings("unused")
	private String newValue, oldValue;
	private StoryCardModel storyCard;
	
	private int oldlabelEditor,labelEditor; 
	
	public StoryCardSetValuesCommand(StoryCardModel storyCard, String s){
		this.storyCard = storyCard;
		if(s != null && !s.equals("")) {
			this.newValue = s;
		} else {
			this.newValue = "0";
		}
	}
	@Override
	public void execute(){
		if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDNAMELABEL){
			this.oldValue = this.storyCard.getName();
		try{
			this.storyCard.getStoryCard().setName(this.newValue);
			PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	 
			this.oldlabelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDDESCRIPTIONLABEL){
			this.oldValue =this.storyCard.getDescription();
			try{
				this.storyCard.getStoryCard().setDescription(this.newValue);
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	 
			this.oldlabelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDACTUALLABEL){
			this.oldValue =this.storyCard.getActualEffort();
			try{
				this.storyCard.getStoryCard().setActualEffort(Float.valueOf(this.newValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}			
			this.oldlabelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDBESTCASELABEL){
			this.oldValue =this.storyCard.getBestCaseEstimate();
			try{
				this.storyCard.getStoryCard().setBestCaseEstimate(Float.valueOf(this.newValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}			
			this.oldlabelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDMOSTLIKELYLABEL){
			this.oldValue =this.storyCard.getMostLikelyEstimate();
			try{
				this.storyCard.getStoryCard().setMostlikelyEstimate(Float.valueOf(this.newValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}				
			this.oldlabelEditor=this.storyCard.getLabelEditor();
		}
		
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDOWNERLABEL){
			this.oldValue =this.storyCard.getCardOwner();
			try{
				this.storyCard.getStoryCard().setCardOwner(this.newValue);
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}			
			this.oldlabelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDWORSTCASELABEL){
			this.oldValue =this.storyCard.getWorstCaseEstimate();
			try{
				this.storyCard.getStoryCard().setWorstCaseEstimate(Float.valueOf(this.newValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}				
			this.oldlabelEditor=this.storyCard.getLabelEditor();
		}else if(this.storyCard.getLabelEditor() == CardConstants.STORYCARDURLLABEL){
			this.oldValue = this.storyCard.getStoryCard().getAcceptanceTestUrl();
			try{
				this.storyCard.getStoryCard().setAcceptanceTestUrl(this.newValue);
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}				
		}else if(this.storyCard.getLabelEditor() == CardConstants.STORYCARDACCEPTTESTLABEL){
			this.oldValue = this.storyCard.getStoryCard().getAcceptanceTestText();
			try{
				this.storyCard.getStoryCard().setAcceptanceTestText(this.newValue);
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(Exception e){
				util.Logger.singleton().error(e);
			}
		}
		
		setFitUniqueID();
	}
	
	private void setFitUniqueID() {
		if(this.storyCard.getLabelEditor() == CardConstants.STORYCARD_FITID_LABEL){
			this.oldValue = this.storyCard.getStoryCard().getFitId();
			try{
				this.storyCard.getStoryCard().setFitId(this.newValue);
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(Exception e){
				util.Logger.singleton().error(e);
			}
		}
	}
	@Override
	public void undo(){
		this.storyCard.setLabelEditor(this.oldlabelEditor);
		if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDNAMELABEL){
			this.newValue = this.storyCard.getName();
			try{
				this.storyCard.getStoryCard().setName(this.oldValue);
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	
			this.storyCard.setLabelEditor(this.oldlabelEditor);
			this.labelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDDESCRIPTIONLABEL){
			this.newValue =this.storyCard.getDescription();
			try{
				this.storyCard.getStoryCard().setDescription(this.oldValue);
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	 
			this.storyCard.setLabelEditor(this.oldlabelEditor);
			this.labelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDACTUALLABEL){
			this.newValue =this.storyCard.getActualEffort();
			try{
				this.storyCard.getStoryCard().setActualEffort(Float.valueOf(this.oldValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}		
			this.storyCard.setLabelEditor(this.oldlabelEditor);
			this.labelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDBESTCASELABEL){
			this.newValue =this.storyCard.getBestCaseEstimate();
			try{
				this.storyCard.getStoryCard().setBestCaseEstimate(Float.valueOf(this.oldValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	
			this.storyCard.setLabelEditor(this.oldlabelEditor);
			this.labelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDMOSTLIKELYLABEL){
			this.newValue =this.storyCard.getMostLikelyEstimate();
			try{
				this.storyCard.getStoryCard().setMostlikelyEstimate(Float.valueOf(this.oldValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	
			this.storyCard.setLabelEditor(this.oldlabelEditor);
			this.labelEditor=this.storyCard.getLabelEditor();
		}
		else if(this.storyCard.getLabelEditor() ==CardConstants.STORYCARDWORSTCASELABEL){
			this.newValue =this.storyCard.getWorstCaseEstimate();
			try{
				this.storyCard.getStoryCard().setWorstCaseEstimate(Float.valueOf(this.oldValue));
				PersisterFactory.getPersister().updateStoryCard(this.storyCard.getStoryCard());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}
			this.storyCard.setLabelEditor(this.oldlabelEditor);
			this.labelEditor=this.storyCard.getLabelEditor();
		}	
	}
	@Override
	public void redo(){
		this.storyCard.setLabelEditor(this.labelEditor);
		this.execute();
	}
		
}

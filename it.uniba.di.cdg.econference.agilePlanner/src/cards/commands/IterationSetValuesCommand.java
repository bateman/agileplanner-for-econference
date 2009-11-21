/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This class is the triger point from eclipse to the editor for changing the values of the iteration object
 *
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.commands;

import java.sql.Date;
import java.sql.Timestamp;

import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.IterationCardModel;

public class IterationSetValuesCommand extends Command{
	@SuppressWarnings("unused")
	private String newValue,oldValue;
	private IterationCardModel iteration;
	private int labelEditor,oldlabelEditor;  
	
	public IterationSetValuesCommand(IterationCardModel iterCard, String s){
		this.iteration = iterCard;
		if(s !=null) {
			this.newValue = s;
		} else {
			this.newValue = "";
		}
	}
	
	@Override
	public void execute (){
		if(this.iteration.getLabelEditor() == CardConstants.ITERATIONCARDNAMELABEL){
			this.oldValue = this.iteration.getName();
			try{
				this.iteration.getIterationDataObject().setName(this.newValue);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	 
			this.oldlabelEditor=this.iteration.getLabelEditor(); 
		}
		else if (this.iteration.getLabelEditor() ==CardConstants.ITERATIONCARDDESCRIPTIONLABEL){
			this.oldValue = this.iteration.getDescription();
			try{
				this.iteration.getIterationDataObject().setDescription(this.newValue);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	 
			this.oldlabelEditor=this.iteration.getLabelEditor(); 
		}
		
		
		
	
		else if (this.iteration.getLabelEditor() == CardConstants.ITERATIONCARDENDDATELABEL){
			this.oldValue = this.iteration.getIterationEndDate();
			try{
				String ds = this.newValue.replace('/','-');
				Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());
				this.iteration.getIterationDataObject().setEndDate(ts);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}
			this.oldlabelEditor=this.iteration.getLabelEditor(); 
		}
		
		else if (this.iteration.getLabelEditor() == CardConstants.ITERATIONCARDSTARTDATELABEL){
			this.oldValue = this.iteration.getStartDate();
			try{
				String ds = this.newValue.replace('/','-');
				Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());
				this.iteration.getIterationDataObject().setStartDate(ts);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}
			this.oldlabelEditor=this.iteration.getLabelEditor(); 
		}

		else if(this.iteration.getLabelEditor() ==CardConstants.ITERATIONCARDAVAILABLEEFFORT){
			this.oldValue = this.iteration.getAvailableEffort();
			try{
				this.iteration.getIterationDataObject().setAvailableEffort(Float.valueOf(this.newValue));
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	 
			this.oldlabelEditor=this.iteration.getLabelEditor();
		}
	}
	@Override
	public void undo(){
		this.iteration.setLabelEditor(this.oldlabelEditor);
		
		if(this.iteration.getLabelEditor() == CardConstants.ITERATIONCARDNAMELABEL){
			this.newValue = this.iteration.getName();
			try{
				this.iteration.getIterationDataObject().setName(this.oldValue);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}				
			this.labelEditor=this.iteration.getLabelEditor();
		}
		else if (this.iteration.getLabelEditor() ==CardConstants.ITERATIONCARDDESCRIPTIONLABEL){
			this.newValue = this.iteration.getDescription();
			try{
				this.iteration.getIterationDataObject().setDescription(this.oldValue);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	
			this.labelEditor=this.iteration.getLabelEditor();
		}
		else if (this.iteration.getLabelEditor() ==CardConstants.ITERATIONCARDENDDATELABEL){
			this.newValue = this.iteration.getIterationEndDate();
			try{
				String ds = this.oldValue.replace('/', '-');
				Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());
				this.iteration.getIterationDataObject().setEndDate(ts);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());	
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}
			this.labelEditor=this.iteration.getLabelEditor();
		}
		
		else if (this.iteration.getLabelEditor() ==CardConstants.ITERATIONCARDSTARTDATELABEL){
			this.newValue = this.iteration.getStartDate();
			try{
				String ds = this.oldValue.replace('/', '-');
				Timestamp ts = new Timestamp(Date.valueOf(ds).getTime());
				this.iteration.getIterationDataObject().setStartDate(ts);
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());	
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}
			this.labelEditor=this.iteration.getLabelEditor();
		}
		else if(this.iteration.getLabelEditor() ==CardConstants.ITERATIONCARDAVAILABLEEFFORT){
			this.newValue = this.iteration.getAvailableEffort();
			try{
				this.iteration.getIterationDataObject().setAvailableEffort(Float.valueOf(this.oldValue));
				PersisterFactory.getPersister().updateIteration(this.iteration.getIterationDataObject());
			}catch(NotConnectedException e){
				util.Logger.singleton().error(e);
			}catch(IndexCardNotFoundException e){
				util.Logger.singleton().error(e);
			}	
			this.labelEditor=this.iteration.getLabelEditor();
		}
	}
	
	@Override
	public void redo(){
		this.iteration.setLabelEditor(this.labelEditor);
		this.execute();
	}
}

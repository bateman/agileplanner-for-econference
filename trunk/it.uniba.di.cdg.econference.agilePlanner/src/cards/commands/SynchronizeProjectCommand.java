
package cards.commands;

import org.eclipse.gef.commands.Command;

import applicationWorkbench.uielements.ProgressBarThread;

import persister.factory.PersisterFactory;



public class SynchronizeProjectCommand extends Command {

   

    public SynchronizeProjectCommand() {
    	ProgressBarThread.getInstatnce().start();
    	PersisterFactory.getPersister().synchronizedProject();
    }    
  
    @Override
    public void redo() {
        
       
    }

    @Override
    public void undo() {}


    @Override
    public void execute() {       

        redo();
    }

    
  }

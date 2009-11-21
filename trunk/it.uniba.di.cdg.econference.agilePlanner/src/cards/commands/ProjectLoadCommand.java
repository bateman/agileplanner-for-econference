/**
 * 
 */
package cards.commands;

import persister.ConnectionFailedException;
import persister.factory.PersisterFactory;

/**
 * @author hkolenda
 * 
 */
public class ProjectLoadCommand {
    
    public ProjectLoadCommand() {
       try {

    	   PersisterFactory.getPersister().connect();

     }
     catch (ConnectionFailedException e) {
    
         util.Logger.singleton().error(e);
     }
     catch (Exception e) {
    	
    	 util.Logger.singleton().error(e);
     }
    }
	
}

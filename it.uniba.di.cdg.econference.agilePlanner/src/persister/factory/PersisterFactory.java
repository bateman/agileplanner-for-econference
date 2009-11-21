package persister.factory;

import java.io.IOException;

import persister.AsynchronousPersister;
import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.UIEventPropagator;
import persister.distributed.ClientCommunicator;
import persister.local.AsynchronousLocalPersister;
import persister.local.DummyDistributedUI;

import applicationWorkbench.uielements.ErrorMessageDialogue;




public class PersisterFactory  {
    private static AsynchronousPersister persister;

    private static UIEventPropagator uiEventPropagator;

    private static ClientCommunicator clientCommunicator;

    public static final String LOCAL_PERSISTER = "LOCAL";
    
    private static String persisterType;

    public static AsynchronousPersister getPersister() {
        // if persister does not yet exist, then create it
        if (persister == null) {
            initializePersisterAndUIPropagator();
        }
        return persister;
    }
    
    public static UIEventPropagator getUIEventPropagator() {
        // if uieventpropagator does not yet exist, then create it
        if (uiEventPropagator == null)
            initializePersisterAndUIPropagator();
        return uiEventPropagator;
    }

    private static void initializePersisterAndUIPropagator() {
        persisterType = Settings.getPersisterType();
        if (persisterType.equalsIgnoreCase(LOCAL_PERSISTER)) {
        
                try {
					persister = new AsynchronousLocalPersister(
						Settings.getProjectLocationLocalMode(),
						Settings.getProjectName()
					);
				} catch (ConnectionFailedException e) {
                         
					util.Logger.singleton().error(e);
				} catch (CouldNotLoadProjectException e) {

				}
                uiEventPropagator = new DummyDistributedUI();
            
        }else {
            try {
                persister = new ClientCommunicator(Settings.getUrl(), Settings.getPort());
                uiEventPropagator = (UIEventPropagator) persister;
            }
            catch (IOException e) {
            	util.Logger.singleton().error(e);
            	Settings.setPersisterType("LOCAL");
            	ErrorMessageDialogue t = new ErrorMessageDialogue("Could not connect to server. Swtiching to LOCAL mode.");
	          	
            	try {
            		t.open();
	      			Thread.sleep(1000);
	      			t.close();
	      		} catch (InterruptedException d) {
	      			util.Logger.singleton().error(d);
	      		} catch (Throwable throwable){
	      		}	      		
            	
                
                try {
					persister = new AsynchronousLocalPersister(Settings.getProjectLocationLocalMode(), Settings.getProjectName());
					uiEventPropagator = new DummyDistributedUI();
				} catch (ConnectionFailedException e1) {
					util.Logger.singleton().error(e);
				} catch (CouldNotLoadProjectException e1) {
					util.Logger.singleton().error(e);
				}
            }
        }
    }

    public static void deletePersister() {
        persister.disconnect();
        persister = null;
        uiEventPropagator = null;
    }

    
	public static void setPersister(AsynchronousPersister persister2) {
		persister = persister2;
	}
}

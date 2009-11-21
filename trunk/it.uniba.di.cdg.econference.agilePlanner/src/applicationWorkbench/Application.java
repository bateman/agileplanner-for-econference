package applicationWorkbench;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IPlatformRunnable {

    public static final String PLUGIN_ID = "ca.ucalgary.cpsc.agilePlanner";

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
     */
    public Object run(Object args) throws Exception {
    	//Settings.load();
        WorkbenchAdvisor workbenchAdvisor = new ApplicationWorkbenchAdvisor();
        Display display = PlatformUI.createDisplay();
        try {
            Platform.endSplash();

            int returnCode = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);

            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IPlatformRunnable.EXIT_RESTART;
            }
            return IPlatformRunnable.EXIT_OK;
        }
        finally {
            display.dispose();
        }
    }
}

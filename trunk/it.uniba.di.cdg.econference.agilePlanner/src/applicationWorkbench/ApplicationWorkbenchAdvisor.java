package applicationWorkbench;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String PERSPECTIVE_ID = "RallyDemoGEF.perspective";

    @Override
    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    public void initialize(IWorkbenchConfigurer configurer) {
    	
        configurer.setSaveAndRestore(false);
       
    }

    public void preStartup() {

    }
}

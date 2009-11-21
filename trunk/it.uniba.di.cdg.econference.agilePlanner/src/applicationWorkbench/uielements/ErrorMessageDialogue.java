package applicationWorkbench.uielements;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This class demonstrates JFace's ErrorDialog class
 */
public class ErrorMessageDialogue extends ApplicationWindow {
  
	
	String msg = null;
	
	
	/**
   * ShowError constructor
   */
  public ErrorMessageDialogue(String message) {
    super(null);
    
    this.msg = message;
  }

  /**
   * Runs the application
   */
  public void run() {
    setBlockOnOpen(true);
    open();
    Display.getCurrent().dispose();
  }

  /**
   * Configures the shell
   * 
   * @param shell the shell
   */
  protected void configureShell(Shell shell) {
    super.configureShell(shell);

    // Set the title bar text and the size
    shell.setText(msg);
    shell.setSize(525, 100);
  }

  /**
   * Creates the main window's contents
   * 
   * @param parent the main window
   * @return Control
   */
  protected Control createContents(Composite parent) {
    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(new GridLayout(1, false));

    // Create a big text box to accept error text
    final Text text = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
    text.setLayoutData(new GridData(GridData.FILL_BOTH));
    text.setText(msg);

    return composite;
  }


}
           
       
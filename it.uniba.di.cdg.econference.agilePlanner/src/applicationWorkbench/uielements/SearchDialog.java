package applicationWorkbench.uielements;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SearchDialog extends Dialog {

    private String searchText = "";

    private Text searchTextbox;

    public SearchDialog(Shell parentShell) {
        super(parentShell);
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Search");
    }

    public boolean close() {
        return super.close();
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        Label searchLabel = new Label(composite, SWT.NONE);
        searchLabel.setText("Search:");
        searchLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

        searchTextbox = new Text(composite, SWT.BORDER);
        GridData gridData2 = new GridData(GridData.FILL, GridData.FILL, true, false);
        gridData2.widthHint = convertHeightInCharsToPixels(20);
        searchTextbox.setLayoutData(gridData2);

        return composite;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    protected void okPressed() {
        super.okPressed();
    }

    protected void buttonPressed(int buttonId) {
        searchText = searchTextbox.getText();
        super.buttonPressed(buttonId);
    }

    public String getSearchString() {
        return searchText;
    }
}

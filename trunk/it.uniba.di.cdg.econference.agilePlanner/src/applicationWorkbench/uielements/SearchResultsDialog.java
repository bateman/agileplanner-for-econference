package applicationWorkbench.uielements;

import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import cards.model.IterationCardModel;
import cards.model.StoryCardModel;



public class SearchResultsDialog extends Dialog {

    private String searchword;

    private Vector searchResults;

    private List results;

    public SearchResultsDialog(Shell parentShell, Vector results, String search) {
        super(parentShell);
        searchword = search;
        searchResults = results;
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Search Results:" + searchword);
    }

    public boolean close() {
        return super.close();
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(2, false);
        composite.setLayout(layout);

        Label iterationLabel = new Label(composite, SWT.NONE);
        iterationLabel.setText("&Search Results");
        iterationLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER, false, false));

        results = new List(parent, SWT.SINGLE);
        for (Object data : searchResults) {
            if (data instanceof StoryCardModel) {
                results.add(((StoryCardModel) data).getName(), searchResults.indexOf(data));
            }
            else
                if (data instanceof IterationCardModel) {
                    results.add(((IterationCardModel) data).getName(), searchResults.indexOf(data));
                }
        }

        GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, false);
        gridData.widthHint = convertHeightInCharsToPixels(20);

        results.setLayoutData(gridData);
        results.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                int index = results.getSelectionIndex();

                if (searchResults.elementAt(index) instanceof StoryCardModel) {
                    StoryCardModel card = (StoryCardModel) searchResults.elementAt(index);
                    card.searchRequest();
                }
                else
                    if (searchResults.elementAt(index) instanceof IterationCardModel) {
                        IterationCardModel card = (IterationCardModel) searchResults.elementAt(index);
                        card.searchRequest();
                    }
            }

            public void widgetSelected(SelectionEvent e) {

            }
        });

        return composite;
    }

    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, "&Done", true);
    }

    protected void okPressed() {
        super.okPressed();
    }

    protected void buttonPressed(int buttonId) {
        super.buttonPressed(buttonId);
    }
}

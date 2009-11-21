package applicationWorkbench.actions;

import java.util.Vector;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import applicationWorkbench.Editor;
import applicationWorkbench.uielements.SearchDialog;
import applicationWorkbench.uielements.SearchResultsDialog;

import cards.CardConstants;
import cards.model.BacklogModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;
import fitintegration.PluginInformation;

public class TextSearchAction extends Action implements ISelectionListener, IWorkbenchAction {
    private IWorkbenchWindow window;

    public static final String ID = "applicationWorkbench.actions.textSearchAction";

    public TextSearchAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&Search");
        setToolTipText("Search...");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(PluginInformation.getPLUGIN_ID(), CardConstants.SearchIcon));
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            selection = (IStructuredSelection) selection;
            setEnabled(true);
        }
        else
            setEnabled(false);
    }

    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    @SuppressWarnings("unchecked")
    public void run() {
        Editor editor = (Editor) window.getActivePage().getActiveEditor();
        ProjectModel projectModel = editor.getModel();
        String searchstring = "";
        SearchDialog dialog = new SearchDialog(null);
        if (dialog.open() == Window.OK) {

            searchstring = dialog.getSearchString();

            Vector<IterationCardModel> iterations = (Vector) projectModel.getIterations();
            BacklogModel backlog = projectModel.getBacklogModel();
            Vector<StoryCardModel> cards = new Vector<StoryCardModel>();

            Vector results = new Vector();
            for (int i = 0; i < iterations.size(); i++)
                cards.addAll(iterations.get(i).getChildrenList());

            cards.addAll(backlog.getChildrenList());

            for (IterationCardModel iter : iterations) {
                if (iter.getName().toLowerCase().contains(searchstring.toLowerCase()))
                    results.add(iter);
            }
            for (StoryCardModel story : cards){
                if (story.getName().toLowerCase().contains(searchstring.toLowerCase()))
                    results.add(story);
            }

            if (results.size() == 0)
                MessageDialog.openInformation(null, "Searh Results", "The search did not turn up any results.");
            else {
                SearchResultsDialog search = new SearchResultsDialog(null, results, searchstring);
                search.open();
            }
        }
    }

}

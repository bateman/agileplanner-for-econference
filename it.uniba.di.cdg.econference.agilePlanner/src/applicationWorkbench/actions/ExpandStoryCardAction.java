package applicationWorkbench.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.ui.IWorkbenchPart;

import cards.commands.StoryCardExpandCommand;
import cards.model.ContainerModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

import applicationWorkbench.Editor;


public class ExpandStoryCardAction extends WorkbenchPartAction implements PropertyChangeListener {

    private ProjectModel projectModel = null;
    IWorkbenchPart arg0;
    Editor editor;
    public ExpandStoryCardAction(IWorkbenchPart arg0) {
        super(arg0);
        
        if (arg0 instanceof Editor) {
        	this.arg0 = arg0;
            Editor editor = (Editor) arg0;
            projectModel = editor.getModel();
        }
    }

    private Command createCommand() {
    	this.editor = (Editor) arg0;
        StoryCardExpandCommand cmd = new StoryCardExpandCommand(editor.getModel());
        cmd.initialize();
        return cmd;
    }

    @Override
    protected boolean calculateEnabled() {

		ArrayList<ContainerModel> indexCards = new ArrayList<ContainerModel>();
		ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();
		indexCards.addAll(projectModel.getContainers());
		storyCards.addAll(projectModel.getStoryCardModelList());
		for (ContainerModel indexCard : indexCards) {
			storyCards.addAll(indexCard.getChildrenList());
		}
				if (storyCards.size() > 0)
					return true;
				else
					return false;		
    }

    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener) {
       
        super.addPropertyChangeListener(listener);
        (projectModel).addPropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        
        update();
    }

    @Override
    public void run() {
       
        this.execute(this.createCommand());
    }

    @Override
    public void update() {
        
        super.update();
    }
}

package cards.editpart;


import mouse.remote.RemoteMouseEditPart;
import mouse.remote.RemoteMouseModel;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;


import cards.model.BacklogModel;
import cards.model.DrawingAreaModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;
import fitintegration.PluginInformation;

public class ProjectEditPartFactory implements EditPartFactory {

	
	
    private EditPart getPartForElement(Object model) {
        if (model instanceof ProjectModel)
            return new ProjectEditPart();
        if (model instanceof StoryCardModel)
        	return PluginInformation.getFactoryForPlugin().getNewStoryCardEditPart();
        if (model instanceof IterationCardModel)
            return new IterationCardEditPart();
        if (model instanceof BacklogModel)
            return new BacklogEditPart();
        if (model instanceof RemoteMouseModel)
            return new RemoteMouseEditPart();
        if (model instanceof DrawingAreaModel){
        	return new DrawingAreaEditPart();
        }
        throw new RuntimeException("Can't create part for model: " + ((model != null) ? model.getClass().getName() : "null"));
    }

    public EditPart createEditPart(EditPart context, Object model) {
        EditPart part = getPartForElement(model);
        part.setModel(model);
        return part;
    }
}

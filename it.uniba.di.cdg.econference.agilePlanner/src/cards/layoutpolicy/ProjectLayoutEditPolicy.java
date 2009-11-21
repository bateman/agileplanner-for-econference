package cards.layoutpolicy;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cards.commands.DrawingAreaCreateCommand;
import cards.commands.DrawingAreaUpdateSizeLocationCommand;
import cards.commands.IterationCardCreateCommand;
import cards.commands.IterationUpdateSizeLocationCommand;
import cards.commands.StoryCardCreateCommand;
import cards.commands.StoryCardMoveToNewParentCommand;
import cards.commands.StoryCardUpdateSizeLocationCommand;
import cards.editpart.DrawingAreaEditPart;
import cards.editpart.IterationCardEditPart;
import cards.editpart.ProjectEditPart;
import cards.editpart.StoryCardEditPart;
import cards.model.AbstractRootModel;
import cards.model.BacklogModel;
import cards.model.DrawingAreaModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;
import cards.model.StoryCardModelAqua;
import cards.model.StoryCardModelBlue;
import cards.model.StoryCardModelGreen;
import cards.model.StoryCardModelGrey;
import cards.model.StoryCardModelKhaki;
import cards.model.StoryCardModelPeach;
import cards.model.StoryCardModelPink;
import cards.model.StoryCardModelRed;
import cards.model.StoryCardModelTransparent;
import cards.model.StoryCardModelWhite;
import cards.model.StoryCardModelYellow;



public class ProjectLayoutEditPolicy extends XYLayoutEditPolicy implements EditPolicy {

    protected Command createAddCommand(EditPart child) {
        return null;
    }

    @Override
    protected Command createAddCommand(EditPart child, Object constraint) {
    	 StoryCardEditPart storyCardEditPart = (StoryCardEditPart) child;
    	 
    	ProjectEditPart project = (ProjectEditPart) storyCardEditPart.getViewer().getContents();
 		ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) project.getRoot();
 		double ratio = root.getZoomManager().getZoom();
        
 		Point moveDelta = (Point) constraint;
         Point oldLocation = storyCardEditPart.getCastedModel().getLocation();

         Point newLocation = new Point(oldLocation.x + moveDelta.x/ratio, oldLocation.y + moveDelta.y/ratio);         
         
         StoryCardMoveToNewParentCommand command = new StoryCardMoveToNewParentCommand(storyCardEditPart.getCastedModel(),
                 (AbstractRootModel) getHost().getModel(), newLocation);
         return command;
    }

    /** Same as the TableXYLayoutPolicy */
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        if ((child instanceof StoryCardEditPart) && (constraint instanceof Rectangle)) {
        	return new StoryCardUpdateSizeLocationCommand((StoryCardModel) child.getModel(), request, (Rectangle) constraint);
        }
        else{
            if ((child instanceof IterationCardEditPart) && (constraint instanceof Rectangle)) {
                return new IterationUpdateSizeLocationCommand((IterationCardModel) child.getModel(), request, (Rectangle) constraint);
            }else if((child instanceof DrawingAreaEditPart) && (constraint instanceof Rectangle)){
            		return new DrawingAreaUpdateSizeLocationCommand((DrawingAreaModel)child.getModel(),request, (Rectangle)constraint);
            }
    	}
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
    	if ((child instanceof StoryCardEditPart) && (constraint instanceof Rectangle)) {
            return new StoryCardUpdateSizeLocationCommand((StoryCardModel) child.getModel(), (Rectangle) constraint);
        }
        return null;
    }

    @Override
    protected Command getAddCommand(Request request) {
    	
        ChangeBoundsRequest req = (ChangeBoundsRequest) request;
        List editParts = req.getEditParts();
        CompoundCommand command = new CompoundCommand();
        for (int i = 0; i < editParts.size(); i++) {
            EditPart child = (EditPart) editParts.get(i);
            
            command.add(this.createAddCommand(child, req.getMoveDelta()));
        }
        return command.unwrap();

    }

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        Object childClass = request.getNewObjectType();

        if (childClass == StoryCardModelBlue.class) {
            /** Add a Story Card to the TableModel */
        	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelBlue) request.getNewObject(),
                    (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
            return storyCreatedCommand;
        }else 
        	if(childClass == StoryCardModelTransparent.class){

                /** Add a Story Card to the TableModel */
            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelTransparent) request.getNewObject(),
                        (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                return storyCreatedCommand;
            
        	}else
        	if (childClass == StoryCardModelRed.class) {
                /** Add a Story Card to the TableModel */
            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelRed) request.getNewObject(),
                        (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                return storyCreatedCommand;
            }
        else
            if (childClass == IterationCardModel.class) {
                /** Add an Iteration Card to the TableModel */
                IterationCardCreateCommand iterationCreatedCommand = new IterationCardCreateCommand((IterationCardModel) request.getNewObject(),
                        (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                return iterationCreatedCommand;
            }
            else if (childClass == BacklogModel.class) {
                   

           }else
        	if (childClass == StoryCardModelGreen.class) {
                /** Add a Story Card to the TableModel */
            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelGreen) request.getNewObject(),
                        (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                return storyCreatedCommand;
            }else
            	if (childClass == StoryCardModelGrey.class) {
                    /** Add a Story Card to the TableModel */
                	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelGrey) request.getNewObject(),
                            (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                    return storyCreatedCommand;
                }else
                	if (childClass == StoryCardModelPink.class) {
                        /** Add a Story Card to the TableModel */
                    	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelPink) request.getNewObject(),
                                (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                        return storyCreatedCommand;
                    }else
                    	if (childClass == StoryCardModelYellow.class) {
                            /** Add a Story Card to the TableModel */
                        	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelYellow) request.getNewObject(),
                                    (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                            return storyCreatedCommand;
                        }else
                        	if (childClass == StoryCardModelWhite.class) {
                                /** Add a Story Card to the TableModel */
                            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelWhite) request.getNewObject(),
                                        (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                return storyCreatedCommand;
                            }else
                            	if (childClass == StoryCardModelKhaki.class) {
                                    /** Add a Story Card to the TableModel */
                                	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelKhaki) request.getNewObject(),
                                            (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                    return storyCreatedCommand;
                                }else
                                	if (childClass == StoryCardModelAqua.class) {
                                        /** Add a Story Card to the TableModel */
                                    	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelAqua) request.getNewObject(),
                                                (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                        return storyCreatedCommand;
                                    }else
                                    	if (childClass == StoryCardModelPeach.class) {
                                            /** Add a Story Card to the TableModel */
                                        	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelPeach) request.getNewObject(),
                                                    (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                            return storyCreatedCommand;
                                        }else if(childClass == DrawingAreaModel.class){
        	   DrawingAreaCreateCommand drawingAreaCreatedCommand = new DrawingAreaCreateCommand((DrawingAreaModel) request.getNewObject(),
                       (ProjectModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
               return drawingAreaCreatedCommand;
           }
       
        return null;
    }

    @Override
    protected Command getDeleteDependantCommand(Request request) {
        return null;
    }

    @Override
    protected Command getMoveChildrenCommand(Request request) {
       
        return super.getMoveChildrenCommand(request);
    }
}
/***************************************************************************************************
 *Author: Robert Morgan
 *Date: Aug 2006
 *Project: MasePlanner
 *Description: 	MasePlanner is a distributed planning tool for agile teams. 
 *Class Description: This is a required edit policy for the IterationCard Object. 
 *								
 *Modified After: This class is modified after a similar class in the "Logic Example Project" provided by the eclipse GEF project.  
 ***************************************************************************************************/
package cards.layoutpolicy;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cards.commands.IterationMoveToContainerCommand;
import cards.commands.StoryCardCreateCommand;
import cards.commands.StoryCardMoveToNewParentCommand;
import cards.commands.StoryCardUpdateSizeLocationCommand;
import cards.editpart.BacklogEditPart;
import cards.editpart.IterationCardEditPart;
import cards.editpart.ProjectEditPart;
import cards.editpart.StoryCardEditPart;
import cards.model.AbstractRootModel;
import cards.model.IterationCardModel;
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



public class IterationLayoutPolicy extends XYLayoutEditPolicy {

    StoryCardCreateCommand storyCardCreatedCommand = null;

    protected Command createAddCommand(EditPart child) {
        if (child instanceof StoryCardEditPart) {
            return null;
        }
        else
            if (child instanceof IterationCardEditPart) {
                IterationMoveToContainerCommand command = new IterationMoveToContainerCommand((IterationCardModel) child.getModel(),
                        new Rectangle(10, 10, 10, 10));
                return command;
            }
            else
                if (child instanceof BacklogEditPart) {
                }
        return null;
    }

    @Override
    protected Command createAddCommand(EditPart child, Object constraint) {
        if (child instanceof StoryCardEditPart) {

        	StoryCardEditPart storyCardEditPart = (StoryCardEditPart) child;
        	
        	ProjectEditPart project = (ProjectEditPart) storyCardEditPart.getViewer().getContents();
    		
    		ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) project.getRoot();
    		double ratio = root.getZoomManager().getZoom();
        	
            Point moveDelta = (Point) constraint;
            Point oldLocation = storyCardEditPart.getCastedModel().getLocation();

            Point newLocation = new Point(oldLocation.x + moveDelta.x / ratio, oldLocation.y + moveDelta.y / ratio);
            

          StoryCardMoveToNewParentCommand command = new StoryCardMoveToNewParentCommand(storyCardEditPart.getCastedModel(),
                    (AbstractRootModel) getHost().getModel(), newLocation);
            return command;

        }

        return null;
    }

    /**
     * Same as the TableXYLayoutPolicy Returns a command that allows for
     * resizing a geometric shape
     */

    protected Command createChangeConstrainCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        if ((child instanceof StoryCardEditPart) && (constraint instanceof Rectangle)) {
            return new StoryCardUpdateSizeLocationCommand((StoryCardModel) child.getModel(), request, (Rectangle) constraint);
        }
        return super.createChangeConstraintCommand(request, child, constraint);//
    }

    /**
     * Returns a command that allows for resizing a geometric shape
     */

    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        if ((child instanceof StoryCardEditPart) && (constraint instanceof Rectangle)) {
            return new StoryCardUpdateSizeLocationCommand((StoryCardModel) child.getModel(), (Rectangle) constraint);
        }
        return null;
    }

    /** Same as the TableXYLayoutPolicy */

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
                    (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
            return storyCreatedCommand;
        }else
        	if(childClass == StoryCardModelTransparent.class){

                /** Add a Story Card to the TableModel */
            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelTransparent) request.getNewObject(),
                        (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                return storyCreatedCommand;
            
        		
        	} else
        	if (childClass == StoryCardModelRed.class) {
                /** Add a Story Card to the TableModel */
            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelRed) request.getNewObject(),
                        (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                return storyCreatedCommand;          
        
           }else
        	if (childClass == StoryCardModelGreen.class) {
                /** Add a Story Card to the TableModel */
            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelGreen) request.getNewObject(),
                        (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                return storyCreatedCommand;
            }else
            	if (childClass == StoryCardModelGrey.class) {
                    /** Add a Story Card to the TableModel */
                	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelGrey) request.getNewObject(),
                            (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                    return storyCreatedCommand;
                }else
                	if (childClass == StoryCardModelPink.class) {
                        /** Add a Story Card to the TableModel */
                    	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelPink) request.getNewObject(),
                                (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                        return storyCreatedCommand;
                    }else
                    	if (childClass == StoryCardModelYellow.class) {
                            /** Add a Story Card to the TableModel */
                        	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelYellow) request.getNewObject(),
                                    (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                            return storyCreatedCommand;
                        }else
                        	if (childClass == StoryCardModelWhite.class) {
                                /** Add a Story Card to the TableModel */
                            	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelWhite) request.getNewObject(),
                                        (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                return storyCreatedCommand;
                            }else
                            	if (childClass == StoryCardModelKhaki.class) {
                                    /** Add a Story Card to the TableModel */
                                	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelKhaki) request.getNewObject(),
                                            (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                    return storyCreatedCommand;
                                }else
                                	if (childClass == StoryCardModelAqua.class) {
                                        /** Add a Story Card to the TableModel */
                                    	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelAqua) request.getNewObject(),
                                                (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                        return storyCreatedCommand;
                                    }else
                                    	if (childClass == StoryCardModelPeach.class) {
                                            /** Add a Story Card to the TableModel */
                                        	StoryCardCreateCommand storyCreatedCommand = new StoryCardCreateCommand((StoryCardModelPeach) request.getNewObject(),
                                                    (IterationCardModel) this.getHost().getModel(), (Rectangle) this.getConstraintFor(request));
                                            return storyCreatedCommand;
                                        }
      
            return null;
       
    }

    @Override
    protected Command getDeleteDependantCommand(Request request) {
        return null;
    }

}
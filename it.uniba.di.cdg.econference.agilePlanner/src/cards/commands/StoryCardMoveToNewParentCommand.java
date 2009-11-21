package cards.commands;

import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.factory.PersisterFactory;


import cards.model.AbstractRootModel;
import cards.model.BacklogModel;
import cards.model.ContainerModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class StoryCardMoveToNewParentCommand extends Command {

	private AbstractRootModel parent;
    
    private StoryCardModel childStoryCardModel;

    private ArrayList<StoryCardModel> childrenStoryCardModel = new ArrayList<StoryCardModel>();

    private ArrayList<StoryCardModel> oldStoryCards = new ArrayList<StoryCardModel>();

    private Rectangle oldStoryCardBounds;

    private ArrayList<Rectangle> oldStoryCardsBounds = new ArrayList<Rectangle>();

    private ArrayList<Rectangle> newStoryCardsBounds = new ArrayList<Rectangle>();

    private Point newLocation = null;

    public StoryCardMoveToNewParentCommand(StoryCardModel storyCardModel, AbstractRootModel parent, Point location) {
    	
    		
        this.parent = parent;
        this.childStoryCardModel = storyCardModel;
        this.newLocation = location;

    }

    @Override
    public void execute() {

    	if(parent instanceof ProjectModel)
    		parent = ((ProjectModel)parent).getBacklogModel();
    	if ((parent instanceof IterationCardModel)&& (newLocation.y >= ((IterationCardModel) parent).getLocation().y+55))	
    	childStoryCardModel.moveStoryCardToNewParent(parent , newLocation);
    	
    	else if (parent instanceof BacklogModel)
    		childStoryCardModel.moveStoryCardToNewParent(parent, newLocation);
    }

    @Override
    public void redo() {
    	
    	
        for (int i = 0; i < this.childrenStoryCardModel.size(); i++) {
            this.childrenStoryCardModel.get(i).setLocation(this.newStoryCardsBounds.get(i).getLocation());
            this.childrenStoryCardModel.get(i).setSize(this.newStoryCardsBounds.get(i).getSize());


            if (parent instanceof ContainerModel){
            try {
                PersisterFactory.getPersister().moveStoryCardToNewParent(this.childrenStoryCardModel.get(i).getStoryCard(), ((ContainerModel)this.parent).getId(),
                        this.childrenStoryCardModel.get(i).getLocation().x, this.childrenStoryCardModel.get(i).getLocation().y,0);
            }
            catch (NotConnectedException e) {
                util.Logger.singleton().error(e);
            }
            catch (IndexCardNotFoundException e) {
                util.Logger.singleton().error(e);
            }

        }
            else
            	try {
                    PersisterFactory.getPersister().moveStoryCardToNewParent(this.childrenStoryCardModel.get(i).getStoryCard(), ((ProjectModel)this.parent).getId(),
                            this.childrenStoryCardModel.get(i).getLocation().x, this.childrenStoryCardModel.get(i).getLocation().y,0);
                }
                catch (NotConnectedException e) {
                    util.Logger.singleton().error(e);
                }
                catch (IndexCardNotFoundException e) {
                    util.Logger.singleton().error(e);
                }
            }

    }

    @Override
    public void undo() {
        for (StoryCardModel card : this.childrenStoryCardModel) {
            this.newStoryCardsBounds.add(new Rectangle(card.getLocation(), card.getSize()));
            
        }

        for (int i = 0; i < this.childrenStoryCardModel.size(); i++) {
            (this.newStoryCardsBounds.get(i)).setLocation((this.childrenStoryCardModel.get(i)).getLocation());
            (this.newStoryCardsBounds.get(i)).setSize((this.childrenStoryCardModel.get(i)).getSize());
        }

    }
}

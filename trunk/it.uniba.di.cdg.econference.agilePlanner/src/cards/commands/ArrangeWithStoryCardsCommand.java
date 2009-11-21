package cards.commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Rectangle;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.StoryCard;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.BacklogModel;
import cards.model.ContainerModel;
import cards.model.IndexCardModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class ArrangeWithStoryCardsCommand extends Command {

    ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();;

    HashMap<Long, Rectangle> oldIndexCardsPosition = new HashMap<Long, Rectangle>();

    ArrayList<IndexCardModel> oldIndexCards = new ArrayList<IndexCardModel>();

    HashMap<Long, Rectangle> oldStoryCardsPosition = new HashMap<Long, Rectangle>();
    
    HashMap<Long, Rectangle> oldPositionProjectStoryCards = new HashMap<Long, Rectangle>();

    ArrayList<StoryCardModel> oldStoryCards = new ArrayList<StoryCardModel>();

    private ProjectModel projectModel = null;

    public ArrangeWithStoryCardsCommand(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    @Override
    public void execute() {
       
        ArrayList<IndexCardModel> tableModelCards = new ArrayList<IndexCardModel>();
        tableModelCards.addAll(projectModel.getContainers());
        oldIndexCards = tableModelCards;
        for (IndexCardModel ic : tableModelCards) {
            oldIndexCardsPosition.put(ic.getId(), new Rectangle(ic.getLocation().x, ic.getLocation().y, ic.getWidth(), ic.getHeight()));
            ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();
            storyCards.addAll(((ContainerModel) ic).getChildrenList());
            oldStoryCards.addAll(((ContainerModel) ic).getChildrenList());
            
            for (StoryCardModel child : storyCards) {
                oldStoryCardsPosition.put(child.getId(), new Rectangle(child.getLocation().x, child.getLocation().y, child.getWidth(), child
                        .getHeight()));
            }
            storyCards = null;
        } // end of ic for
        


        for(StoryCardModel child :projectModel.getStoryCardModelList())
        {
        	oldPositionProjectStoryCards.put(child.getId(), new Rectangle(child.getLocation().x, child.getLocation().y, child.getWidth(), child
                        .getHeight()));
        }
    
    }// end of execute()

    @Override
    public void redo() {
        
    	int x1 = 5;
        int y1 = 0;
             
        ArrayList<StoryCardModel> storyCardsOnCanvass = new ArrayList<StoryCardModel>();
                
        storyCardsOnCanvass.addAll(this.projectModel.getStoryCardModelList());
              
        ArrayList<StoryCard> sortedStoryCardsOnCanvass = new ArrayList<StoryCard>();
        
        for(StoryCardModel child : storyCardsOnCanvass)
          {
        	child.getStoryCard().setLocationX(x1);
        	child.getStoryCard().setLocationY(y1);
            x1 = x1 + 20;
            y1 = y1
                    + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardSmallHeight
                            : CardConstants.WindowsStoryCardSmallHeight);
            sortedStoryCardsOnCanvass.add(child.getStoryCard());
          }
       
        this.projectModel.getProjectDataObject().getBacklog().setStoryCardChildren(sortedStoryCardsOnCanvass);
        
    	
        ArrayList<IndexCardModel> tableModelCards = new ArrayList<IndexCardModel>();
        tableModelCards.addAll(projectModel.getContainers());

        int yIterLocationOffset = 20;// for the iteration
        int xIterLocationOffset = CardConstants.MacBacklogWidth + 40;
        int widthIter = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationCardWidth
                : CardConstants.WindowsIterationCardWidth;
        
        int heightIter = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacIterationCardHeight
                : CardConstants.WindowsIterationCardHeight;

        int yBackLocationOffset = 20;
        int xBackLocationOffset = 5;
        int widthBack = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacBacklogWidth
                : CardConstants.WindowsBacklogWidth;
        int heightBack = (System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacBacklogHeight
                : CardConstants.WindowsBacklogHeight;

       
        for (IndexCardModel icm : tableModelCards) {

            int y;
            
            if (icm instanceof IterationCardModel) {
                IterationCardModel iteration = (IterationCardModel) icm;

                iteration.getIterationDataObject().setLocationX(xIterLocationOffset);
                iteration.getIterationDataObject().setLocationY(yIterLocationOffset);
                iteration.getIterationDataObject().setWidth(widthIter);
                iteration.getIterationDataObject().setHeight(heightIter);
                
                int x = 5;
                y = CardConstants.ITERATIONFIGUREWORSTCASELINE + 5;
                for (StoryCardModel card : iteration.getChildrenList()) {
                    card.getStoryCard().setLocationX(iteration.getLocation().x + x);
                    card.getStoryCard().setLocationY(iteration.getLocation().y + y);
                    x = x + 20;
                    y = y
                            + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardSmallHeight
                                    : CardConstants.WindowsStoryCardSmallHeight);
                }

            }
        }
        PersisterFactory.getPersister().arrangeProject(this.projectModel.getProjectDataObject());
    }

    @Override
    public void undo() {
        
        for (IndexCardModel ic : oldIndexCards) {
            if (ic instanceof IterationCardModel) {
                try {
                    ((IterationCardModel) ic).getIterationDataObject().setWidth((oldIndexCardsPosition.get(ic.getId())).width);
                    ((IterationCardModel) ic).getIterationDataObject().setHeight((oldIndexCardsPosition.get(ic.getId())).height);
                    PersisterFactory.getPersister().updateIteration(((IterationCardModel) ic).getIterationDataObject());
                }
                catch (NotConnectedException e) {
                   
                    util.Logger.singleton().error(e);
                }
                catch (IndexCardNotFoundException e) {
                   
                    util.Logger.singleton().error(e);
                }
                try {
                    ((IterationCardModel) ic).getIterationDataObject().setLocationX((oldIndexCardsPosition.get(ic.getId())).x);
                    ((IterationCardModel) ic).getIterationDataObject().setLocationY((oldIndexCardsPosition.get(ic.getId())).y);
                    PersisterFactory.getPersister().updateIteration(((IterationCardModel) ic).getIterationDataObject());
                }
                catch (NotConnectedException e) {
                    
                    util.Logger.singleton().error(e);
                }
                catch (IndexCardNotFoundException e) {
                   
                    util.Logger.singleton().error(e);
                }
            }
            if (ic instanceof BacklogModel) {
                try {
                    ((BacklogModel) ic).getBacklog().setWidth((oldIndexCardsPosition.get(ic.getId())).width);
                    ((BacklogModel) ic).getBacklog().setHeight((oldIndexCardsPosition.get(ic.getId())).height);
                    PersisterFactory.getPersister().updateBacklog(((BacklogModel) ic).getBacklog());
                }
                catch (NotConnectedException e) {
                   
                    util.Logger.singleton().error(e);
                }
                catch (IndexCardNotFoundException e) {
                   
                    util.Logger.singleton().error(e);
                }
                try {
                    ((BacklogModel) ic).getBacklog().setLocationX((oldIndexCardsPosition.get(ic.getId())).x);
                    ((BacklogModel) ic).getBacklog().setLocationY((oldIndexCardsPosition.get(ic.getId())).y);
                    PersisterFactory.getPersister().updateBacklog(((BacklogModel) ic).getBacklog());
                }
                catch (NotConnectedException e) {
                   
                    util.Logger.singleton().error(e);
                }
                catch (IndexCardNotFoundException e) {
                   
                    util.Logger.singleton().error(e);
                }
            }
        }// end of for ic

        for (StoryCardModel sc : projectModel.getStoryCardModelList()) {
            try {
                sc.getStoryCard().setLocationX((oldPositionProjectStoryCards.get(sc.getStoryCard().getId())).x);
                sc.getStoryCard().setLocationY((oldPositionProjectStoryCards.get(sc.getStoryCard().getId())).y);
                PersisterFactory.getPersister().updateStoryCard(sc.getStoryCard());
            }
            catch (NotConnectedException e) {
             
                util.Logger.singleton().error(e);
            }
            catch (IndexCardNotFoundException e) {
               
                util.Logger.singleton().error(e);
            }
        }
    }// end of undo

}

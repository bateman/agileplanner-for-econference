package cards.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.graphics.Rectangle;

import persister.IndexCardNotFoundException;
import persister.NotConnectedException;
import persister.data.Iteration;
import persister.data.StoryCard;
import persister.factory.PersisterFactory;


import cards.CardConstants;
import cards.model.BacklogModel;
import cards.model.ContainerModel;
import cards.model.IndexCardModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

public class ArrangeCommand extends Command {

    ArrayList<ContainerModel> oldIndexCards = new ArrayList<ContainerModel>();
    HashMap<Long, Rectangle> oldIndexCardsPosition = new HashMap<Long, Rectangle>();
    
    ArrayList<StoryCardModel> oldStoryCards = new ArrayList<StoryCardModel>();
    HashMap<Long, Rectangle> oldStoryCardsPosition = new HashMap<Long, Rectangle>();
    
    
    HashMap<Long, Rectangle> oldPositionProjectStoryCards = new HashMap<Long, Rectangle>();

    private ProjectModel projectModel = null;

    public ArrangeCommand(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }    
  
    @Override
    public void redo() {
        
    	int x1 = 5;
        int y1 = 0;
             
        ArrayList<StoryCardModel> storyCardsOnCanvass = new ArrayList<StoryCardModel>();
                
        storyCardsOnCanvass.addAll(this.projectModel.getStoryCardModelList());
              
        Comparator<StoryCardModel> shuffleByYcoordinate = new Comparator<StoryCardModel>() {
            public int compare(StoryCardModel s1, StoryCardModel s2) {
                if (s1.getLocation().y >= s2.getLocation().y)
                    return 1;
                else
                    return -1;
            }
        };

        Collections.sort(storyCardsOnCanvass, shuffleByYcoordinate);
        
        ArrayList<StoryCard> sortedStoryCardsOnCanvass = new ArrayList<StoryCard>();
        
        int iterationsXcoodinate = 0;
        
        for(StoryCardModel child : storyCardsOnCanvass)
          {
        	child.getStoryCard().setLocationX(x1);
        	child.getStoryCard().setLocationY(y1);
            x1 = x1 + 20;
            y1 = y1
                    + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardSmallHeight
                            : CardConstants.WindowsStoryCardSmallHeight);
            sortedStoryCardsOnCanvass.add(child.getStoryCard());
           
            if(child.getLocation().x > iterationsXcoodinate)
            	iterationsXcoodinate = child.getLocation().x;           
          }
        iterationsXcoodinate = iterationsXcoodinate + CardConstants.WindowsStoryCardWidth;
       
        this.projectModel.getProjectDataObject().getBacklog().setStoryCardChildren(sortedStoryCardsOnCanvass);
        
        
        // Arrange Iterations

        ArrayList<Iteration> sortedIterations = new ArrayList<Iteration>();
        ArrayList<IterationCardModel> iterations = new ArrayList<IterationCardModel>();
        iterations.addAll(this.projectModel.getIterations());
        
        

        Comparator<IterationCardModel> dateCompare = new Comparator<IterationCardModel>() {
            public int compare(IterationCardModel o1, IterationCardModel o2) {
                if (o1.getEndDate().before(o2.getEndDate()))
                    return 1;
                else
                    return -1;
            }
        };

        Collections.sort(iterations, dateCompare);

        int iterationNewY = 20;
              
        for (IterationCardModel iter : iterations) {
			ArrayList<StoryCardModel> unSortedStoryCards = new ArrayList<StoryCardModel>();

			unSortedStoryCards.addAll(iter.getChildrenList());
						
	        Collections.sort(unSortedStoryCards, shuffleByYcoordinate);
			
			 ArrayList<StoryCard> sortedStoryCardsOnIteration = new ArrayList<StoryCard>();

			 int iterationHeight = 0;
			 int iterationWidth = 0;
			 int x2 = 5;
             int y2 = CardConstants.ITERATIONFIGUREWORSTCASELINE + 5;
             
             iter.getIterationDataObject().setLocationX(75 + iterationsXcoodinate);
 			iter.getIterationDataObject().setLocationY(iterationNewY);
 			
 			if(iter.getIterationDataObject().getWidth() < CardConstants.WindowsIterationCardWidth)
				iter.getIterationDataObject().setWidth(CardConstants.WindowsIterationCardWidth);
			if(iter.getIterationDataObject().getHeight() < CardConstants.WindowsIterationCardHeight)
				iter.getIterationDataObject().setHeight(CardConstants.WindowsIterationCardHeight);
			 
             int iterationX = iter.getIterationDataObject().getLocationX();
             int iterationY = iter.getIterationDataObject().getLocationY();
             
             for (StoryCardModel card : unSortedStoryCards) {
                 card.getStoryCard().setLocationX(iterationX + x2);
                 card.getStoryCard().setLocationY(iterationY + y2);
                 x2 = x2 + 20;
                 y2 = y2
                         + ((System.getProperty("os.name").equalsIgnoreCase("Mac OS X")) ? CardConstants.MacStoryCardSmallHeight
                                 : CardConstants.WindowsStoryCardSmallHeight);
                 
                 if((card.getStoryCard().getLocationY() + card.getHeight() - iterationY ) > iterationHeight)
                	 iterationHeight =iterationHeight + card.getHeight();
                 
                 if((card.getLocation().x + card.getWidth() - iterationX) > iterationWidth)
                	 iterationWidth = iterationWidth + card.getWidth();
                 
                 sortedStoryCardsOnIteration.add(card.getStoryCard());
             }
			 if(sortedStoryCardsOnIteration.size() > 0)
			iter.getIterationDataObject().setStoryCardChildren(sortedStoryCardsOnIteration);
						
			
			iter.getIterationDataObject().setWidth(iterationWidth);
			iter.getIterationDataObject().setHeight(iterationHeight);
			
			if(iter.getIterationDataObject().getWidth() < CardConstants.WindowsIterationCardWidth)
				iter.getIterationDataObject().setWidth(CardConstants.WindowsIterationCardWidth);
			if(iter.getIterationDataObject().getHeight() < CardConstants.WindowsIterationCardHeight)
				iter.getIterationDataObject().setHeight(CardConstants.WindowsIterationCardHeight);
			 
			
			sortedIterations.add(iter.getIterationDataObject());
			iterationNewY = 20 + iterationHeight + iterationNewY ;
        }
     
        this.projectModel.getProjectDataObject().setIterationChildren(sortedIterations);
        PersisterFactory.getPersister().arrangeProject(this.projectModel.getProjectDataObject());
       
    }

    @Override
    public void undo() {
    	
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
    	   
        for (IndexCardModel ic : oldIndexCards) {
            if (ic instanceof IterationCardModel) {
                try {
                    ((IterationCardModel) ic).getIterationDataObject().setWidth((oldIndexCardsPosition.get(ic.getId())).width);
                    ((IterationCardModel) ic).getIterationDataObject().setHeight((oldIndexCardsPosition.get(ic.getId())).height);
                    ((IterationCardModel) ic).getIterationDataObject().setLocationX((oldIndexCardsPosition.get(ic.getId())).x);
                    ((IterationCardModel) ic).getIterationDataObject().setLocationY((oldIndexCardsPosition.get(ic.getId())).y);

                    
                    for (StoryCardModel sc :((IterationCardModel)ic).getChildrenList()) {
                    	
                    	sc.getStoryCard().setLocationX((oldStoryCardsPosition.get(sc.getStoryCard().getId())).x);
                        sc.getStoryCard().setLocationY((oldStoryCardsPosition.get(sc.getStoryCard().getId())).y); 	
                    }
                              
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
                    ((BacklogModel) ic).getBacklog().setLocationX((oldIndexCardsPosition.get(ic.getId())).x);
                    ((BacklogModel) ic).getBacklog().setLocationY((oldIndexCardsPosition.get(ic.getId())).y);

                    for (StoryCardModel sc : ((BacklogModel)ic).getChildrenList()) {
                    	
                    	sc.getStoryCard().setLocationX((oldStoryCardsPosition.get(sc.getStoryCard().getId())).x);
                        sc.getStoryCard().setLocationY((oldStoryCardsPosition.get(sc.getStoryCard().getId())).y);  	
                    }
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
        
    }

    @Override
    public void execute() {
               
        oldIndexCards.addAll(projectModel.getContainers());
        for (ContainerModel ic : oldIndexCards) {
            oldIndexCardsPosition.put(ic.getId(), new Rectangle(ic.getLocation().x, ic.getLocation().y, ic.getWidth(), ic.getHeight()));
            ArrayList<StoryCardModel> storyCards = new ArrayList<StoryCardModel>();
            storyCards.addAll(ic.getChildrenList());
            oldStoryCards.addAll(ic.getChildrenList());
            for (StoryCardModel child : storyCards) {
                oldStoryCardsPosition.put(child.getId(), new Rectangle(child.getLocation().x, child.getLocation().y, child.getWidth(), child
                        .getHeight()));
            }
        } // end of ic for

        for(StoryCardModel child :projectModel.getStoryCardModelList())
        {
        	oldPositionProjectStoryCards.put(child.getId(), new Rectangle(child.getLocation().x, child.getLocation().y, child.getWidth(), child
                        .getHeight()));
        }
        
        redo();
    }

  }

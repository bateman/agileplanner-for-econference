package persister.data.impl;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;
import persister.data.Backlog;
import persister.data.IndexCard;
import persister.data.IndexCardWithChildren;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.util.FileSystemIDGenerator;




public class ProjectDataObject implements Project, Serializable {

   

    /**
	 * 
	 */
	private static final long serialVersionUID = 6231722126492172614L;

	private Backlog backlog;
	private Legend legend;
    private List<Iteration> iterations;
    private List<TeamMember>teamMembers;
    private List<IndexCard> deletedRallyCardsList;
    private boolean changeLegend = false;
    private long id;
    private String name;
    private transient FileSystemIDGenerator generator;
    public ProjectDataObject() {
        setId(0);
        setName("Project");
        iterations = new Vector<Iteration>();
        teamMembers = new Vector<TeamMember>();
        generator = FileSystemIDGenerator.getInstance();
        this.deletedRallyCardsList = new Vector<IndexCard>();
        setLegend(new LegendDataObject());
        createBacklog(10,10,0,0);
    }

    public ProjectDataObject(String name) {
        this();
        setName(name);
    }

    public Object clone() {
        ProjectDataObject clone = new ProjectDataObject();
        
        clone.setId(getId());
        clone.setName(getName());
        
        if (getBacklog() != null){
            clone.setBacklog((Backlog) getBacklog().clone());
        }
        
        clone.setIterations(new Vector<Iteration>());
        for (Iterator<Iteration> iter = getIterationChildren().iterator(); iter.hasNext();) {
            Iteration element = iter.next();
            clone.addIteration((Iteration)((Iteration) element).clone());
        }
        
        clone.setTeamMembers(new Vector<TeamMember>());
        for (Iterator<TeamMember> iter = getTeamMembers().iterator(); iter.hasNext();) {
            TeamMember element = iter.next();
            clone.addTeamMember((TeamMember)((TeamMember) element).clone());
        }
        
        if(getLegend() != null){
        	clone.setLegend((Legend)getLegend().clone());
        }
        
        return clone;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addIteration(IndexCardWithChildren iteration) {
        iteration.setParent(getId());
        getIterationChildren().add((Iteration) iteration);
    }
    
    public void addTeamMember(TeamMember teamMember) {
        if(!hasTeamMember((TeamMember)teamMember)){
        	getTeamMembers().add((TeamMember) teamMember);
        }
    }

    public boolean hasTeamMember(TeamMember tm){
    	return getTeamMembers().contains(tm);
    }
    
    public Backlog getBacklog() {
        if (backlog == null) {
            return null;
        } else {
            return (Backlog) backlog;
        }
    }

    public List<Iteration> getIterationChildren() {
        return iterations;
    }
    
    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }
    

    public void removeIteration(IndexCardWithChildren iteration) {
        iterations.remove(iteration);
    }
    
    public void removeOwner(TeamMember teamMember) {
    	iterations.remove(teamMember);
    }

    public void removeOwners(String [] owners){
    	for(String owner : owners){
    		removeOwner(new TeamMemberDataObject(owner));
    	}
    }
    
    public void setBacklog(Backlog backlog) {
        backlog.setParent(this.getId());
        this.backlog = backlog;
    }

    public void setIterationChildren(List<Iteration> iterations) {
        this.iterations = iterations;
    }
    
    public void setOwnerChildren(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public String toString() {
        String project = "###################################\n" + "#             Project             #\n"
                + "###################################\n\n" + "ID:\t" + getId() + "\n" + "Iterations:\t" + getIterationChildren().size() + "\n\n";
        return project;
    }

    /** ************************************************************************** */

    public synchronized Backlog createBacklog(int width, int height, int locationX, int locationY){
        if(getBacklog() == null) {
            BacklogDataObject backlog = new BacklogDataObject();
            backlog.setId(generator.getNextID());
            this.setBacklog(backlog);
        }
        getBacklog().setHeight(height);
        getBacklog().setWidth(width);
        getBacklog().setLocationX(locationX);
        getBacklog().setLocationY(locationY);
        return (Backlog) backlog;
        
    }

    public synchronized Iteration createIteration(String name, String description, int width, int height, int locationX, int locationY,
            float availableEffort, Timestamp startDate, Timestamp endDate, float rotationAngle,boolean rallyID) {
        IterationDataObject iteration = new IterationDataObject();
        if(generator.getNextID() == 2)
        	generator.getNextID();
        iteration.setId(generator.getNextID());
        iteration.setName(name);
        iteration.setDescription(description);
        iteration.setHeight(height);
        iteration.setWidth(width);
        iteration.setLocationX(locationX);
        iteration.setLocationY(locationY);
        iteration.setAvailableEffort(availableEffort);
        iteration.setRotationAngle(rotationAngle);
        iteration.setRallyID(rallyID);
        

        if (startDate != null) {
            iteration.setStartDate(startDate);
        }

        if (endDate != null) {
            iteration.setEndDate(endDate);
        }

        this.addIteration(iteration);

        return (Iteration) iteration;
    }

    public synchronized StoryCard createStoryCard(String name, String description, int width, int height, int locationX, int locationY,
            long parentid, float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, String cardOwner, float rotationAngle,boolean rallyID, String fitId)
            throws IndexCardNotFoundException {

        StoryCardDataObject storycard = new StoryCardDataObject();
        if(generator.getNextID() == 2)
        	generator.getNextID();
        storycard.setId(generator.getNextID());
        storycard.setName(name);
        storycard.setHeight(height);
        storycard.setWidth(width);
        storycard.setLocationX(locationX);
        storycard.setLocationY(locationY);
        storycard.setDescription(description);
        storycard.setBestCaseEstimate(bestCaseEstimate);
        storycard.setMostlikelyEstimate(mostlikelyEstimate);
        storycard.setWorstCaseEstimate(worstCaseEstimate);
        storycard.setActualEffort(actualEffort);
        storycard.setStatus(status);
        storycard.setColor(color);
        storycard.setRotationAngle(rotationAngle);
        storycard.setFitId(fitId);
        ((IndexCardWithChildren) this.findCard(parentid)).addStoryCard(storycard);

        return (StoryCard) storycard;
    }
    
    public synchronized StoryCard createTabletPCStoryCard(String name, String description, int width, int height, int locationX, int locationY,
            long parentid, float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color,byte[] image,boolean rallyID)
            throws IndexCardNotFoundException {

        StoryCardDataObject storycard = new StoryCardDataObject();
        if(generator.getNextID() == 2)
        	generator.getNextID();
        storycard.setId(generator.getNextID());
        storycard.setName(name);
        storycard.setHeight(height);
        storycard.setWidth(width);
        storycard.setLocationX(locationX);
        storycard.setLocationY(locationY);
        storycard.setDescription(description);
        storycard.setBestCaseEstimate(bestCaseEstimate);
        storycard.setMostlikelyEstimate(mostlikelyEstimate);
        storycard.setWorstCaseEstimate(worstCaseEstimate);
        storycard.setActualEffort(actualEffort);
        storycard.setStatus(status);
        storycard.setColor(color);
        storycard.setHandwritingImage(image);
        ((IndexCardWithChildren) this.findCard(parentid)).addStoryCard(storycard);

        return (StoryCard) storycard;
    }

    /** ************************************************************************************** */

    public synchronized IndexCard deleteCard(long id) throws IndexCardNotFoundException, ForbiddenOperationException {
        IndexCard indexCard = findCard(id);
        
        if (indexCard instanceof Backlog)
            return deleteBacklog((Backlog) indexCard);
        if (indexCard instanceof Iteration)
            return deleteIteration((Iteration) indexCard);
        if (indexCard instanceof StoryCard)
            return deleteStoryCard((StoryCard) indexCard);
        else
            throw new IndexCardNotFoundException("id=" + id + " does not refer to either StoryCard, Iteration or Backlog object!", id);
    }

    private IndexCard deleteStoryCard(StoryCard storyCard) throws IndexCardNotFoundException {
    	
        IndexCardWithChildren parent = (IndexCardWithChildren) this.findCard(storyCard.getParent());
        parent.removeStoryCard(storyCard);
        
        if(storyCard.getRallyID())
        	deletedRallyCardsList.add((StoryCard) storyCard);

        return (IndexCard) storyCard;
    }

    private IndexCard deleteBacklog(Backlog backlog) throws ForbiddenOperationException {
        throw new ForbiddenOperationException("You cannot delete the Backlog without deleting the Project!");
    }

    private IndexCard deleteIteration(Iteration iteration) throws IndexCardNotFoundException, ForbiddenOperationException {

        
        if(iteration.getRallyID())
        	throw new ForbiddenOperationException("You cannot delete the Rally Iteration!");
        else{
        	this.removeIteration(iteration);
        	return (IndexCard) iteration;
        }
    }

    /** ********************************************************************************************* */

    public synchronized IndexCard undeleteCard(IndexCard indexCard) throws IndexCardNotFoundException, ForbiddenOperationException {

        if (indexCard instanceof Iteration)
            return undeleteIteration((Iteration) indexCard);
        if (indexCard instanceof StoryCard)
            return undeleteStoryCard((StoryCard) indexCard);
        else
            throw new IndexCardNotFoundException("id does not refer to either StoryCard, Iteration or Backlog object!", indexCard.getId());

    }

    private Iteration undeleteIteration(Iteration iteration) throws IndexCardNotFoundException {

        this.addIteration(iteration);

        return (Iteration) iteration;
    }

    private StoryCard undeleteStoryCard(StoryCard storycard) throws IndexCardNotFoundException, ForbiddenOperationException {

        ((IndexCardWithChildren) this.findCard(storycard.getParent())).addStoryCard(storycard);

        return (StoryCard) storycard;
    }

    /** ************************************************************************************************************* */

    public TeamMember findOwner(String name)throws IndexCardNotFoundException{
    	for (Iterator<TeamMember> iter = getTeamMembers().iterator(); iter.hasNext();) {
            TeamMember element = iter.next();
            if (element.getName() == name) {
                return element;
            }
    	}
    	throw new IndexCardNotFoundException("Could not owner with name=" + name);
    	 
    }
    public IndexCard findCard(long id2) throws IndexCardNotFoundException {
        if (getBacklog() != null) {
            if (getBacklog().getId() == id2)
                return getBacklog();

            for (Iterator<StoryCard> iterator = backlog.getStoryCardChildren().iterator(); iterator.hasNext();) {
                StoryCard sc = iterator.next();
                if (sc.getId() == id2) {
                    return sc;
                }
            }
        }// end if for backlog

        for (Iterator<Iteration> iter = getIterationChildren().iterator(); iter.hasNext();) {
            Iteration element = iter.next();
            if (element.getId() == id2) {
                return element;
            }
            for (Iterator<StoryCard> iterator = element.getStoryCardChildren().iterator(); iterator.hasNext();) {
                StoryCard sc = iterator.next();
                if (sc.getId() == id2) {
                    return sc;
                }
            }
        }
        // no card was found
        throw new IndexCardNotFoundException("Could not find index card with id=" + id2, id2);
    }

    public IndexCard updateCard(IndexCard indexCard) throws IndexCardNotFoundException {
        if (indexCard instanceof Backlog)
            return updateBacklog((Backlog) indexCard);
        if (indexCard instanceof Iteration)
            return updateIteration((Iteration) indexCard);
        if (indexCard instanceof StoryCard)
            return updateStoryCard((StoryCard) indexCard);
      
        	
            throw new IndexCardNotFoundException("id does not refer to either StoryCard, Iteration or Backlog object!",indexCard.getId());
        
        
    }

    private Backlog updateBacklog(Backlog newBacklog) throws IndexCardNotFoundException {

        setBacklog(newBacklog);

        return (Backlog) (getBacklog());
    }
    
    public Legend updateLegend(Legend newLegend){
    	
    	setLegend(newLegend);
    	return (Legend) newLegend.clone();
    }
    public TeamMember updateOwner(TeamMember teamMember){
    	removeOwner(teamMember);
    	addTeamMember(teamMember);
    	return teamMember;
    }

    /** ************************************************************************************************ */

    private Iteration updateIteration(Iteration newIteration) throws IndexCardNotFoundException {

        Iteration oldIteration = (Iteration) findCard(newIteration.getId());

        removeIteration(oldIteration);

        if(oldIteration.getRallyID())
        	newIteration.setIdRecievedFromRally(true);
        
        // add new Iteration card
        addIteration(newIteration);

        return (Iteration) newIteration;
    }

    private StoryCard updateStoryCard(StoryCard sc) throws IndexCardNotFoundException {

    	//if an outdated object is received this should fix
    	//messed up parent-child relationships
    	StoryCard localSC = (StoryCard) findCard(sc.getId());
        IndexCardWithChildren parent = (IndexCardWithChildren) findCard(localSC.getParent());
        sc.setParent(parent.getId());

        // remove the old card
        StoryCard oldCard = (StoryCard) findCard(sc.getId());
        parent.removeStoryCard(oldCard);

        if(oldCard.getRallyID())
       	sc.setRallyID(true);
        // add new card
        parent.addStoryCard(sc);

        return (StoryCard) sc;
    }

    /** ****************************************************************************************** */

    public synchronized StoryCard moveStoryCardToNewParent(long id, long oldparentid, long newparentid, int locationX, int locationY,float rotation)
            throws IndexCardNotFoundException {

        IndexCardWithChildren oldparent = (IndexCardWithChildren) this.findCard(oldparentid);
        IndexCardWithChildren newparent = (IndexCardWithChildren) this.findCard(newparentid);

        StoryCardDataObject storycard = (StoryCardDataObject) this.findCard(id);

        oldparent.removeStoryCard(storycard);

        storycard.setLocationX(locationX);
        storycard.setLocationY(locationY);
        storycard.setRotationAngle(rotation);
        newparent.addStoryCard(storycard);
        storycard.setParent(newparentid);
        

        return (StoryCard) storycard;
    }

	public FileSystemIDGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(FileSystemIDGenerator generator) {
		this.generator = generator;
	}

	public List<IndexCard> getDeletedRallyCardsList() {
		return deletedRallyCardsList;
	}

	public void setDeletedRallyCardsList(List<IndexCard> deletedRallyCardsList) {
		this.deletedRallyCardsList = deletedRallyCardsList;
	}

	public Legend createLegend(String blue, String red, String green,
			String yellow, String white, String pink, String gray,
			String khaki, String peach, String aqua) {

		 LegendDataObject legend = new LegendDataObject();
		 legend.setBlue(blue);
		 legend.setRed(red);
		 legend.setGreen(green);
		 legend.setYellow(yellow);
		 legend.setWhite(white);
		 legend.setPink(pink);
		 legend.setGrey(gray);
		 legend.setKhaki(khaki);
		 legend.setPeach(peach);
		 legend.setAqua(aqua);

         this.setLegend(legend);

         return  (Legend)legend;
		
	}

	public Legend getLegend() {
		 return  this.legend;
	}

	public void setLegend(Legend legend) {
		 this.legend = legend;
		
	}

	public TeamMember createOwner(String name) {
		TeamMemberDataObject owner = new TeamMemberDataObject();
        owner.setName(name);
       
        this.addTeamMember(owner);

        return (TeamMember) owner;
	
	}

	public TeamMember deleteOwner(TeamMember teamMember) {
		this.removeOwner(teamMember);
    	return (TeamMember) teamMember;
	}

	public boolean equals(Object other){
		Project project = (Project) other;
		return
			getBacklog().equals(project.getBacklog())
			&& getId() == project.getId()
			&& getIterations().equals(project.getIterationChildren())
			&& getName().equals(project.getName())
			&& getTeamMembers().equals(project.getTeamMembers())
			&& getLegend().equals(project.getLegend())
		;
	}

	private List<Iteration> getIterations() {
		return iterations;
	}

	private void setIterations(List<Iteration> iterations) {
		this.iterations = iterations;
	}

	private boolean isChangeLegend() {
		return changeLegend;
	}

	private void setChangeLegend(boolean changeLegend) {
		this.changeLegend = changeLegend;
	}

	private void setTeamMembers(List<TeamMember> teamMembers) {
		this.teamMembers = teamMembers;
	}


}

package persister.data;

import java.sql.Timestamp;
import java.util.List;

import persister.AbstractRoot;
import persister.ForbiddenOperationException;
import persister.IndexCardNotFoundException;

public interface Project extends AbstractRoot {

    public void addIteration(IndexCardWithChildren iteration);
    public void addTeamMember(TeamMember teamMember);
    public Backlog getBacklog();
    public Legend getLegend();
    public List<Iteration> getIterationChildren();
    public List<TeamMember> getTeamMembers();
    public void removeIteration(IndexCardWithChildren iteration);
    public void removeOwner(TeamMember teamMember);
    public void removeOwners(String [] owners);
    public void setBacklog(Backlog backlog);
    public void setLegend(Legend legend);
    public void setIterationChildren(List<Iteration> iterations);
    public void setOwnerChildren(List<TeamMember>teamMembers);
    public IndexCard undeleteCard(IndexCard indexCard) throws IndexCardNotFoundException, ForbiddenOperationException;
    public StoryCard moveStoryCardToNewParent(long id, long oldparentid, long newparentid, int locationX, int locationY,float rotation)
            throws IndexCardNotFoundException;
    public IndexCard deleteCard(long id) throws IndexCardNotFoundException, ForbiddenOperationException;
    public TeamMember deleteOwner(TeamMember teamMember);
    public StoryCard createStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color, String cardOwner, float rotationAngle,boolean rallyID,String fitId)
            throws IndexCardNotFoundException;
    public StoryCard createTabletPCStoryCard(String name, String description, int width, int height, int locationX, int locationY, long parentid,
            float bestCaseEstimate, float mostlikelyEstimate, float worstCaseEstimate, float actualEffort, String status, String color,byte[] image,boolean rallyID)
            throws IndexCardNotFoundException;
    public Iteration createIteration(String name, String description, int width, int height, int locationX, int locationY, float availableEffort,
            Timestamp startDate, Timestamp endDate, float rotationAngle,boolean rallyID);
    public TeamMember createOwner(String name);
    public Backlog createBacklog(int width, int height, int locationX, int locationY) throws ForbiddenOperationException;
    public Legend createLegend(String blue ,String red ,String green ,String yellow ,String white ,String pink ,String gray ,String khaki ,String peach ,String aqua);
    public IndexCard updateCard(IndexCard indexCard) throws IndexCardNotFoundException;
    public Legend updateLegend (Legend legend);
    public TeamMember updateOwner(TeamMember teamMember);
    public IndexCard findCard(long id) throws IndexCardNotFoundException;
    public List<IndexCard> getDeletedRallyCardsList(); 
    public void setDeletedRallyCardsList(List<IndexCard> deletedRallyCardsList);
}
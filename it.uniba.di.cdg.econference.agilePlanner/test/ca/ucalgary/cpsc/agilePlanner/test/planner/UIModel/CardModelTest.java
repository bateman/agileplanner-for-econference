/**
 * 
 */
package ca.ucalgary.cpsc.agilePlanner.test.planner.UIModel;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;

import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.StoryCard;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.datachangeimplement.DataCallback;
import persister.factory.PersisterFactory;
import persister.local.AsynchronousLocalPersister;
import persister.local.PersisterToXML;


import cards.model.BacklogModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;
import cards.model.StoryCardModel;

/**
 * @author dhillonh
 * 
 */

public class CardModelTest extends TestCase {
    private static PersisterToXML ps;

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    /***************************************************************************
     * INITIALIZATION *
     **************************************************************************/

    @org.junit.Test
    public void testUpdatedIterationModelAndStoryCardModel() {

        // initial setup of project model
        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);
        try {
			PersisterFactory.setPersister(new AsynchronousLocalPersister("",""));
		} catch (ConnectionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CouldNotLoadProjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        ProjectModel projectModel = new ProjectModel();
        projectModel.setProjectDataObject(pdo);
        
        DataCallback dataCallback = new DataCallback(projectModel);

        Iteration idoInitial = new IterationDataObject(5, 1);
        IterationCardModel icmInitial = new IterationCardModel(idoInitial);
        pdo.addIteration(idoInitial);

        StoryCard scdoInitial = new StoryCardDataObject();
        //scdoInitial.setId(100);
        StoryCardModel scmInitial = new StoryCardModel(scdoInitial);
        scmInitial.getStoryCard().setId(100);
        scmInitial.setParent(icmInitial);
        //idoInitial.addStoryCard(scdoInitial);

        icmInitial.addNewChild(scmInitial);
        projectModel.addNewIteration(icmInitial);
       
        //idoInitial.addStoryCard(scdoInitial);

        // assuming following as incoming message from server

        StoryCard scdoServer = new StoryCardDataObject();
        scdoServer.setId(100);

        Iteration idoServer = new IterationDataObject(5, 1);
        idoServer.addStoryCard(scdoServer);

        dataCallback.updatedIteration(idoServer);

//        Assert.assertTrue(icmInitial.getChildrenList().get(0).getStoryCard() == icmInitial.getIterationDataObject().getStoryCardChildren().get(0));
       
        Assert.assertTrue(pdo.getIterationChildren().get(0).getStoryCardChildren().size() == 1);
    }

    @org.junit.Test
    public void testUpdatedBacklogModelAndStorycardModel() {

        // initial setup of project model
        ProjectModel projectModel = new ProjectModel();
        
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);

        Backlog bdoInitial = new BacklogDataObject();
        bdoInitial.setId(5);
        bdoInitial.setParent(1);

        BacklogModel bcmInitial = new BacklogModel(bdoInitial);
        pdo.setBacklog(bdoInitial);

        //projectModel.setBacklogModelIncoming(backlogModel);
        
        StoryCard scdoInitial = new StoryCardDataObject();
        scdoInitial.setId(100);
        StoryCardModel scmInitial = new StoryCardModel(scdoInitial);
        scmInitial.getStoryCard().setParent(5);
        scmInitial.setParent(bcmInitial);

        projectModel.setNewBacklogModel(bcmInitial);
        bcmInitial.addNewChild(scmInitial);
        bdoInitial.addStoryCard(scdoInitial);

        // assuming following as incoming message from server

        StoryCard scdoServer = new StoryCardDataObject();
        scdoServer.setId(100);

        Backlog bdoServer = new BacklogDataObject();
        bdoServer.setId(5);
        bdoServer.setParent(1);

        bdoServer.addStoryCard(scdoServer);

        pdci.updatedBacklog(bdoServer);

        Assert.assertTrue(bcmInitial.getChildrenList().get(0).getStoryCard() == bcmInitial.getBacklog().getStoryCardChildren().get(0));

    }

    @org.junit.Test
    public void testDeletedIterationAndStoryCard() {
        
        ProjectModel projectModel = new ProjectModel();
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);
        projectModel.setProjectDataObject(pdo);

      //  pdci.setProjectModel(projectModel);

        Iteration idoInitial = new IterationDataObject(5, 1);
        IterationCardModel icmInitial = new IterationCardModel(idoInitial);
        icmInitial.setProjectModel(projectModel);
      //  idoInitial.setParent(1);
        pdo.addIteration(idoInitial);

        StoryCard scdoInitial = new StoryCardDataObject();
        scdoInitial.setId(100);
        StoryCardModel scmInitial = new StoryCardModel(scdoInitial);
        //scmInitial.getStoryCard().setParent(5);
        scmInitial.setParent(icmInitial);

        projectModel.addNewIteration(icmInitial);
        icmInitial.addNewChild(scmInitial);
        //idoInitial.addStoryCard(scdoInitial);

        pdci.deletedStoryCard(100);

        Assert.assertTrue(projectModel.getProjectDataObject().getIterationChildren().get(0).getStoryCardChildren().size() == 0);
        Assert.assertTrue(projectModel.getIterations().get(0).getChildrenList().size() == 0);

        pdci.deletedIteration(5);

//        Assert.assertTrue(projectModel.getProjectDataObject().getIterationChildren().size() == 0);
        Assert.assertTrue(projectModel.getIterations().size() == 0);

    }

    @org.junit.Test
    public void testDeletedStoryCardFromBacklog() {

        // initial setup of project model
        ProjectModel projectModel = new ProjectModel();
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);
        projectModel.setProjectDataObject(pdo);

        pdci.setProjectModel(projectModel);

        BacklogDataObject bdoInitial = new BacklogDataObject();
        BacklogModel bmInitial = new BacklogModel(bdoInitial);
        bmInitial.setProjectModel(projectModel);
        bdoInitial.setId(400);
        bdoInitial.setParent(1);

        pdo.setBacklog(bdoInitial);
       // projectModel.setBacklogModelIncoming(bmInitial);

        StoryCard scdoInitial = new StoryCardDataObject();
        scdoInitial.setId(100);

        StoryCardModel scmInitial = new StoryCardModel(scdoInitial);
        scmInitial.setParent(bmInitial);
        //scmInitial.getStoryCard().setParent(5);

        //bdoInitial.addStoryCard(scdoInitial);
        
        bmInitial.addNewChild(scmInitial);
        pdci.deletedStoryCard(100);

        Assert.assertTrue(projectModel.getStoryCardModelList().size() == 0);

    }

//    @org.junit.Test
//    public void testCreatedProjectOnInitialLoadFromServer() {
//
//        // initial setup of project data object which comes from server.
//
//        ProjectDataObject pdo = new ProjectDataObject("TestProject");
//        pdo.setId(1);
//
//        // create and setup Backlog in project
//        BacklogDataObject bdoInitial = new BacklogDataObject();
//        bdoInitial.setId(400);
//        bdoInitial.setParent(1);
//
//        pdo.setBacklog(bdoInitial);
//
//        // create and setup story card in backlog
//        StoryCard scdoInitialInBacklog = new StoryCardDataObject();
//        scdoInitialInBacklog.setId(100);
//        scdoInitialInBacklog.setParent(400);
//
//        bdoInitial.addStoryCard(scdoInitialInBacklog);
//
//        // create and setup Iteration in project
//        Iteration idoInitial = new IterationDataObject(5, 1);
//        idoInitial.setId(200);
//        idoInitial.setParent(1);
//
//        pdo.addIteration(idoInitial);
//        // icmInitial.setIterationDataObject(idoInitial);
//
//        // create and setup story card in Iteration
//        StoryCard scdoInitialInIteration = new StoryCardDataObject();
//        scdoInitialInIteration.setId(300);
//        scdoInitialInIteration.setParent(200);
//
//        idoInitial.addStoryCard(scdoInitialInIteration);
//
////        DataCallback pdci = new DataCallback();
////        pdci.setEditor(new Editor());
//        Editor ed = new Editor();
//        DataCallback pdci = ed.getDataCallback();
//        pdci.createdProjectOnInitialLoadFromServer(pdo);
//
//        ProjectModel projectModel = pdci.getProjectModel();
//
//        // Following asserts check for correct set up of data objects
//        Assert.assertTrue(projectModel.getProjectDataObject() != null);
//
//        Assert.assertTrue(projectModel.getProjectDataObject().getBacklog() != null);
//
//        Assert.assertTrue(projectModel.getProjectDataObject().getBacklog().getStoryCardChildren().size() == 1);
//
//        Assert.assertTrue(projectModel.getProjectDataObject().getBacklog().getStoryCardChildren().get(0) != null);
//
//        Assert.assertTrue(projectModel.getProjectDataObject().getIterationChildren().get(0) != null);
//
//        Assert.assertTrue(projectModel.getProjectDataObject().getIterationChildren().get(0).getStoryCardChildren().size() == 1);
//
//        // Following asserts check for correct setup of data models for backlog
//        Assert.assertTrue(projectModel.getBacklogModel() != null);
//
//        Assert.assertTrue(projectModel.getBacklogModel().getChildrenList() != null);
//
////        Assert.assertTrue(projectModel.getBacklogModel().getChildrenList().get(0) != null);
//
//        Assert.assertTrue(projectModel.getStoryCardModelList().get(0).getStoryCard() != null);
//
//        // Following asserts check for correct setup of data models for
//        // Iteration
//        Assert.assertTrue(projectModel.getIterations() != null);
//
//        Assert.assertTrue(projectModel.getIterations().get(0) != null);
//
//        Assert.assertTrue(projectModel.getIterations().get(0).getIterationDataObject() != null);
//
//        Assert.assertTrue(projectModel.getIterations().get(0).getIterationDataObject().getStoryCardChildren() != null);
//
//        Assert.assertTrue(projectModel.getIterations().get(0).getIterationDataObject().getStoryCardChildren().get(0) != null);
//
//    }

}

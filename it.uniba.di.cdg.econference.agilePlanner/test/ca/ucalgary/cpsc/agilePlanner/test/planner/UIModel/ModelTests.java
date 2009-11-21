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
import persister.data.Iteration;
import persister.data.StoryCard;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.datachangeimplement.DataCallback;
import persister.factory.PersisterFactory;
import persister.local.AsynchronousLocalPersister;


import cards.model.BacklogModel;
import cards.model.IterationCardModel;
import cards.model.ProjectModel;

/**
 * @author dhillonh
 * 
 */
public class ModelTests extends TestCase {
    // private static PersisterToXML ps;
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
    	try {
			PersisterFactory.setPersister(new AsynchronousLocalPersister("",""));
		} catch (ConnectionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CouldNotLoadProjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    public void testCreatedStoryCardIteration() {

        ProjectModel projectModel = new ProjectModel();
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);

        projectModel.setProjectDataObject(pdo);

        // pdci.getDataChangeListener().setProjectModel(projectModel);

        Iteration idoInitial = new IterationDataObject(5, 1);
        idoInitial.setParent(1);
        idoInitial.setId(5);

        IterationCardModel icmInitial = new IterationCardModel(idoInitial);
        icmInitial.setProjectModel(projectModel);

        projectModel.addNewIteration(icmInitial);
        pdo.addIteration(idoInitial);
        icmInitial.setIterationDataObject(idoInitial);

        StoryCard scdoInitial = new StoryCardDataObject();
        scdoInitial.setId(100);
        scdoInitial.setParent(5);

        // StoryCardModel scmInitial = new StoryCardModel();
        // scmInitial.setParent(icmInitial);
        // scmInitial.setParentId(5);

        pdci.createdStoryCard(scdoInitial);

        Assert.assertTrue(projectModel.getIterations().get(0).getChildrenList().get(0).getStoryCard() != null);

    }

    @org.junit.Test
    public void testCreatedStoryCardBacklog() {

        ProjectModel projectModel = new ProjectModel();
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);

        projectModel.setProjectDataObject(pdo);

        // PersisterDataChangeImplementer.getDataChangeListener().setProjectModel(projectModel);

        BacklogDataObject bdoInitial = new BacklogDataObject();
        bdoInitial.setParent(1);
        bdoInitial.setId(5);

        BacklogModel bcmInitial = new BacklogModel(bdoInitial);
        bcmInitial.setProjectModel(projectModel);

        projectModel.setNewBacklogModel(bcmInitial);
        pdo.setBacklog(bdoInitial);
        bcmInitial.setBacklogDataObject(bdoInitial);

        StoryCard scdoInitial = new StoryCardDataObject();
        scdoInitial.setId(100);
        scdoInitial.setParent(5);

        pdci.createdStoryCard(scdoInitial);

//        Assert.assertTrue(projectModel.getBacklogModel().getChildrenList().get(0).getStoryCard() != null);
        Assert.assertTrue(projectModel.getStoryCardModelList().get(0).getStoryCard() != null);

    }

    @org.junit.Test
    public void testCreatedIteration() {

        ProjectModel projectModel = new ProjectModel();
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);

        projectModel.setProjectDataObject(pdo);

        // PersisterDataChangeImplementer.getDataChangeListener().setProjectModel(projectModel);

        IterationDataObject idoServer = new IterationDataObject(20, 1);
        idoServer.setId(20);
        idoServer.setParent(1);

        pdci.createdIteration(idoServer);

        Assert.assertTrue(projectModel.getIterations().get(0) != null);
        // Assert.assertTrue(projectModel.getIterationModelList().get(0).getIterationDataObject()!=
        // null);
    }

    @org.junit.Test
    public void testCreatedBacklog() {

        ProjectModel projectModel = new ProjectModel();
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);

        projectModel.setProjectDataObject(pdo);

        // PersisterDataChangeImplementer.getDataChangeListener().setProjectModel(projectModel);

        BacklogDataObject bdoServer = new BacklogDataObject();
        bdoServer.setId(30);
        bdoServer.setParent(1);

        pdci.createdBacklog(bdoServer);

        Assert.assertTrue(projectModel.getBacklogModel() != null);
    }

    @org.junit.Test
    public void testMovedStoryCardFromIteratoinToBacklog() {

        ProjectModel projectModel = new ProjectModel();
        DataCallback pdci = new DataCallback(projectModel);

        ProjectDataObject pdo = new ProjectDataObject("TestProject");
        pdo.setId(1);

        projectModel.setProjectDataObject(pdo);

        Iteration idoInitial = new IterationDataObject(5, 1);
        idoInitial.setParent(1);
        idoInitial.setId(5);

        IterationCardModel icmInitial = new IterationCardModel(idoInitial);
        icmInitial.setProjectModel(projectModel);

        projectModel.addNewIteration(icmInitial);
        pdo.addIteration(idoInitial);
        icmInitial.setIterationDataObject(idoInitial);

        StoryCard scdoInitial = new StoryCardDataObject();
        scdoInitial.setId(100);
        scdoInitial.setParent(5);

        pdci.createdStoryCard(scdoInitial);

        BacklogDataObject bdoServer = new BacklogDataObject();
        bdoServer.setId(30);
        bdoServer.setParent(1);

        pdci.createdBacklog(bdoServer);

        scdoInitial.setParent(30);
        // scdoInitial.s

        pdci.movedStoryCardToNewParent(scdoInitial);

//        Assert.assertTrue(bdoServer.getStoryCardChildren().get(0) != null);
        Assert.assertTrue(projectModel.getStoryCardModelList().get(0) != null);
        Assert.assertTrue(icmInitial.getChildrenList().size() == 0);
        //Assert.assertTrue(idoInitial.getStoryCardChildren().size() == 0);

    }

}

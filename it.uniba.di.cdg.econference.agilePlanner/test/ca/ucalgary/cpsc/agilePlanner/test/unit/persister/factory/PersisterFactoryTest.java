package ca.ucalgary.cpsc.agilePlanner.test.unit.persister.factory;


import persister.factory.PersisterFactory;
import persister.AsynchronousPersister;
import persister.ConnectionFailedException;
import persister.CouldNotLoadProjectException;
import persister.UIEventPropagator;
import persister.local.AsynchronousLocalPersister;

import static org.junit.Assert.*;

import org.junit.Test;

public class PersisterFactoryTest {

	@Test
	public void testGetPersisterLocalMode() {
		AsynchronousPersister testPersister = PersisterFactory.getPersister();
			
		assertNotNull(testPersister);
		assertEquals("persister.local.DummyDistributedUI", PersisterFactory.getUIEventPropagator().getClass().getName());
		assertEquals("persister.local.AsynchronousLocalPersister", testPersister.getClass().getName());
	}

	@Test
	public void testGetUIEventPropagatorLocalMode() {
		UIEventPropagator testPropagator = PersisterFactory.getUIEventPropagator();
		
		assertNotNull(testPropagator);
		assertEquals("persister.local.DummyDistributedUI", testPropagator.getClass().getName());
		assertEquals("persister.local.AsynchronousLocalPersister", PersisterFactory.getPersister().getClass().getName());
	}

	@Test
	public void testDeletePersister() {
		AsynchronousPersister testPersister = PersisterFactory.getPersister();
		UIEventPropagator testPropagator = PersisterFactory.getUIEventPropagator();
			
		PersisterFactory.deletePersister();
		
		try {
			assertFalse(testPersister.connected());
		} catch (ConnectionFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//assertNull(testPersister);
		//assertNull(testPropagator);
	}
	
	@Test
	public void testSetPersister() {
		try {
			AsynchronousLocalPersister testLocal = new AsynchronousLocalPersister("", "Testzorz");
			PersisterFactory.setPersister(testLocal);
			AsynchronousLocalPersister testPersister = (AsynchronousLocalPersister)PersisterFactory.getPersister();
			
			assertEquals(testLocal, testPersister);
			assertEquals("Testzorz", testPersister.getSynPer().getCurrentProjectName());
			
			
		} catch (ConnectionFailedException e) {
			fail(e.getMessage());
		} catch (CouldNotLoadProjectException e) {
			fail(e.getMessage());
		}
		
	}

}

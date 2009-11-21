package ca.ucalgary.cpsc.agilePlanner.test.planner.fitintegration;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fitintegration.AgilePlannerFactory;
import fitintegration.FactoryInterface;
import fitintegration.PluginInformation;


public class TestPluginInformation {

	public static FactoryInterface factoryForPlugin;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFactoryInterface(){
		assertNull("factoryForPlugin should be null",factoryForPlugin);
		factoryForPlugin = new AgilePlannerFactory();
		assertNotNull("factoryForPlugin should have been initialized, but is null",factoryForPlugin);
		PluginInformation.setFactoryForPlugin(factoryForPlugin);
		assertNotNull("factoryForPlugin should have been initialized in PluginInformation, but is null",PluginInformation.getFactoryForPlugin());
	}
	
	@Test
	public void testPluginID(){
		assertNull("factoryForPlugin should be null",PluginInformation.getPLUGIN_ID());
		PluginInformation.setPLUGIN_ID("TestID");
		assertEquals("TestID", PluginInformation.getPLUGIN_ID());
	}
	
}

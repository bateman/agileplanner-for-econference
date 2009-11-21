package ca.ucalgary.cpsc.agilePlanner.test.unit.persister.factory;

import java.io.File;

import org.junit.Test;

import persister.factory.Settings;
import static org.junit.Assert.*;

public class SettingsTest {
	
	@Test
	public void testLoad() throws Exception {
		File file = new File("./TestData/ConfigFixtures/config.xml");
		for(int i=0;i<100;i++)System.out.println("=================================");
		System.out.println(file.getAbsolutePath());
		System.out.println();
		assertTrue(file.exists());
		Settings.load("./TestData/ConfigFixtures/config.xml");
		assertEquals("ASE", Settings.getProjectName());
	}
	
	@Test
	public void testSetProjectName() throws Exception {
		util.File.copy(new File("./TestData/ConfigFixtures/config.xml"), new File("./TestData/ConfigFixtures/config2.xml"));
		Settings.load("./TestData/ConfigFixtures/config2.xml");
		Settings.setProjectName("EBE");
		Settings.load();
		assertEquals("EBE", Settings.getProjectName());
	}
}

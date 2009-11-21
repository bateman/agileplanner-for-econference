package ca.ucalgary.cpsc.agilePlanner.test.util;

import java.io.File;
import org.junit.After;
import org.junit.Before;
import org.junit.Assert;

import util.Logger;


public class LoggerTest {

	Logger logger;
	File testLog;
	
	@Before
	public void setUp() throws Exception {
		File testDir = new File("testspace/");
		testDir.mkdirs();
		testLog = new File(testDir.getAbsolutePath()+"/aLog");
		Logger.clear();
		logger = Logger.singleton(testLog);
	}

	@After
	public void tearDown() throws Exception {
		testLog.delete();
	}
	
	@org.junit.Test
	public void shouldWriteAMessageToFile() throws Exception {
		logger.debug("Testing Debug");
		Assert.assertTrue("The logger should have writen to the log file.", (new File(logger.getFile())).length() > 0);
	}

}

package ca.ucalgary.cpsc.agilePlanner.test.util;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static util.File.*;
import java.io.File;

public class FileTest {
	final File file1 = new File("TestData/FileFixtures/aFile.txt");
	final File file2 = new File("TestData/FileFixtures/aCoppiedFile.txt");
	
	@Before
	public void setUp(){
		assumeTrue(file1.exists());
		assumeTrue(!file2.exists());
	}
	
	@After
	public void tearDown(){
		if(file2.exists()){
			file2.delete();
		}
	}
	
	@Test
	public void shouldCopyAFile() throws Exception{
		copy(file1, file2);
		assertEquals(readAll(file1), readAll(file2));
	}
	
	@Test
	public void shouldReadAFile() throws Exception{
		assertEquals("Hello World",readAll(file1));
	}
	
	@Test
	public void shouldWriteAFile() throws Exception{
		String str = "Hello you sexy world you.";
		write(str, file2);
		assertEquals(str, readAll(file2));
	}
}

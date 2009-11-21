package ca.ucalgary.cpsc.agilePlanner.test.util;

import fit.ColumnFixture;
import util.File;

public class FileAcceptance extends ColumnFixture {
	public String file;

	public String readAll() throws Exception{
		java.io.File f = new java.io.File(file);
		return File.readAll(f);
	}

}

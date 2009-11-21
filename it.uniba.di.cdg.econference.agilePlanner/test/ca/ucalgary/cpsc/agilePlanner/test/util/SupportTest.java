package ca.ucalgary.cpsc.agilePlanner.test.util;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static util.Support.capitalize;

public class SupportTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCapitalize(){
		assertThat(capitalize("str"), equalTo("Str"));
		assertThat(capitalize("sTr"), equalTo("Str"));
		assertThat(capitalize("stR"), equalTo("Str"));
		assertThat(capitalize("STR"), equalTo("Str"));
	}
}

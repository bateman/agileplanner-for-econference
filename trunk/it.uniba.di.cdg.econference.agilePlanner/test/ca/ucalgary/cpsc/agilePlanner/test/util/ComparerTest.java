package ca.ucalgary.cpsc.agilePlanner.test.util;


import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static util.Comparer.compare;

public class ComparerTest {

	String str1;
	String str2;
	String str3;

	@Before
	public void setUp() throws Exception {
		str1 = "somechars";
		str2 = "somestring";
		str3 = "somestr";
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCompare(){
		assertThat(compare(str2).to(str2), equalTo(0));
	}

}

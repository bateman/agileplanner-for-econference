package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

//test package
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import persister.data.Backlog;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.StoryCardDataObject;
import persister.xml.converter.OwnerConverter;
//testing class
//dependencies
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class OwnerConverterTest {
	Mockery context = new Mockery();
	OwnerConverter oc = new OwnerConverter();
	
	@Test
	public void shouldMarsalAUserAndAddToTheUsersList(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final TeamMember teamMember = context.mock(TeamMember.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("USERS");
				one (mockWriter).addAttribute("Name", "John Smith");
				one (mockWriter).endNode();
			}
			{
				one (teamMember).getName();
				will(returnValue("John Smith"));
			}
		});
		
		//execute the code to test
		oc.marshal(teamMember, mockWriter, null);
		
		//check the result
		context.assertIsSatisfied();
	}
	
	@Test
	public void shouldUnMarshalAnOwner(){
		TeamMember teamMember;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("Name");will(returnValue("John Smith"));
			}
		});
		
		teamMember = (TeamMember)oc.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(teamMember.getName(), equalTo("John Smith"));
	}
	
}

package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import java.util.ArrayList;

import persister.data.Backlog;
import persister.data.StoryCard;
import persister.data.impl.StoryCardDataObject;
import persister.xml.converter.ProjectNameListConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class ProjectNameListConverterTest {

	Mockery context = new Mockery();
	ProjectNameListConverter converter = new ProjectNameListConverter();
	
	@Test
	public void shouldMarsalTheListOfProjects(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final ArrayList<String> nameList = new ArrayList<String>(){
			{
				add("aName");
				add("anotherName");
			}
		};
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("ProjectNameList");
				
				exactly(2).of (mockWriter).startNode("project");
				one (mockWriter).addAttribute("name", nameList.get(0));
				one (mockWriter).addAttribute("name", nameList.get(1));
				
				exactly(3).of (mockWriter).endNode();
			}
		});
		
		//execute the code to test
		converter.marshal(nameList, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnMarshalTheListOfProjects(){
		ArrayList<String> nameList;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		final Sequence hasMoreChildrenSequence = context.sequence("hasMoreChildrenSequence");
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).hasMoreChildren();	inSequence(hasMoreChildrenSequence);	will(returnValue(true));
				one (mockReader).moveDown();		inSequence(hasMoreChildrenSequence);
				one (mockReader).getAttribute("name");inSequence(hasMoreChildrenSequence);	will(returnValue("Hello"));
				one (mockReader).moveUp();			inSequence(hasMoreChildrenSequence);
			
				one (mockReader).hasMoreChildren();	inSequence(hasMoreChildrenSequence);	will(returnValue(true));
				one (mockReader).moveDown();		inSequence(hasMoreChildrenSequence);
				one (mockReader).getAttribute("name");inSequence(hasMoreChildrenSequence);	will(returnValue("World"));
				one (mockReader).moveUp();			inSequence(hasMoreChildrenSequence);
			
				one (mockReader).hasMoreChildren();	inSequence(hasMoreChildrenSequence);	will(returnValue(false));
			}
		});
		
		nameList = (ArrayList<String>)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(nameList.get(0), equalTo("Hello"));
		assertThat(nameList.get(1), equalTo("World"));
	}
	

}

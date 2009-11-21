package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import persister.xml.converter.BacklogConverter;
import persister.data.Backlog;
import persister.data.StoryCard;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.StoryCardDataObject;
//testing class
//dependencies
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;

public class BacklogConverterTest {

	Mockery context = new Mockery();
	BacklogConverter converter = new BacklogConverter();
	
	@Test
	public void shouldMarsalABacklog(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Backlog backlog = context.mock(Backlog.class);
		final StoryCard storyCard = context.mock(StoryCard.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("Backlog");
				one (mockWriter).addAttribute("Height", "5");
				one (mockWriter).addAttribute("Width", "5");
				one (mockWriter).addAttribute("ID", "435");
				one (mockWriter).addAttribute("Name", "John Smith");
				one (mockWriter).addAttribute("Parent", "500");
				one (mockWriter).addAttribute("XLocation", "0");
				one (mockWriter).addAttribute("YLocation", "1");
				one (mockWriter).endNode();
			}
			{
				one (backlog).getHeight();
				will(returnValue(5));
				one (backlog).getWidth();
				will(returnValue(5));
				one (backlog).getId();
				will(returnValue((long)435));
				one (backlog).getName();
				will(returnValue("John Smith"));
				one (backlog).getParent();
				will(returnValue((long)500));
				one (backlog).getLocationX();
				will(returnValue(0));
				one (backlog).getLocationY();
				will(returnValue(1));
				
				atLeast(1).of (backlog).getStoryCardChildren();
				will(returnValue(new java.util.ArrayList<StoryCard>(){
					{
						add(storyCard);
						add(storyCard);
					}
				}));
			}
			{
				exactly(2).of (marshalContext).convertAnother(storyCard);
			}
		});
		
		//execute the code to test
		converter.marshal(backlog, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnMarshalABacklog(){
		Backlog backlog;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		final Sequence hasMoreChildrenSequence = context.sequence("hasMoreChildrenSequence");
		final StoryCard storyCard = context.mock(StoryCard.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("Height");	will(returnValue("5"));
				one (mockReader).getAttribute("ID");		will(returnValue("67"));
				one (mockReader).getAttribute("Name");		will(returnValue("John Smith"));
				one (mockReader).getAttribute("Parent");	will(returnValue("45"));
				one (mockReader).getAttribute("Width");		will(returnValue("4"));
				one (mockReader).getAttribute("XLocation");	will(returnValue("2"));
				one (mockReader).getAttribute("YLocation");	will(returnValue("2"));
			}
			{	
				one (mockReader).hasMoreChildren();
					will(returnValue(true));
					inSequence(hasMoreChildrenSequence);
				one (mockReader).moveDown();
					inSequence(hasMoreChildrenSequence);
				one (mockReader).getNodeName();
					will(returnValue("StoryCard"));
					inSequence(hasMoreChildrenSequence);
				one (unMarshalContext).convertAnother(with(any(Backlog.class)), with(equal(StoryCardDataObject.class)));
					will(returnValue(storyCard));
					inSequence(hasMoreChildrenSequence);
				one (storyCard).setParent(67L);
					inSequence(hasMoreChildrenSequence);
				one (mockReader).moveUp();
					inSequence(hasMoreChildrenSequence);
				
				one (mockReader).hasMoreChildren();
					will(returnValue(true));
					inSequence(hasMoreChildrenSequence);
				one (mockReader).moveDown();
					inSequence(hasMoreChildrenSequence);
				one (mockReader).getNodeName();
					will(returnValue("Iteration"));
					inSequence(hasMoreChildrenSequence);
				one (mockReader).moveUp();
					inSequence(hasMoreChildrenSequence);
				
				one (mockReader).hasMoreChildren();
					will(returnValue(false));
					inSequence(hasMoreChildrenSequence);
			}
		});
		
		backlog = (Backlog)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(backlog.getName(), equalTo("John Smith"));
		assertThat(backlog.getId(), equalTo(67L));
		assertThat(backlog.getLocationX(), equalTo(2));
		assertThat(backlog.getLocationY(), equalTo(2));
		assertThat(backlog.getHeight(), equalTo(5));
		assertThat(backlog.getWidth(), equalTo(4));
		assertThat(backlog.getParent(), equalTo(45L));
	}
}

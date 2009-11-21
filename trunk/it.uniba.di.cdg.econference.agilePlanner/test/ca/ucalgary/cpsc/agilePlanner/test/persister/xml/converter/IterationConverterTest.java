package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;


import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.matchers.IsCollectionContaining.*;

import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import persister.xml.converter.IterationConverter;

import java.sql.Timestamp;
import java.util.ArrayList;

import persister.data.Iteration;
import persister.data.StoryCard;
import persister.data.impl.StoryCardDataObject;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class IterationConverterTest {

	Mockery context = new Mockery();
	IterationConverter converter = new IterationConverter();
	
	@Test
	public void shouldMarsalTheDataToDisconnectAClient(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Iteration iteration = context.mock(Iteration.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		final StoryCard storyCard = context.mock(StoryCard.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("Iteration");
				one (mockWriter).addAttribute("AvailableEffort", "1.0");
				one (mockWriter).addAttribute("Description", "Hello World");
				one (mockWriter).addAttribute("EndDate","1999-12-31 11:59:59.0");
				one (mockWriter).addAttribute("Height", "5");
				one (mockWriter).addAttribute("ID", "34");
				one (mockWriter).addAttribute("Name", "John Smith");
				one (mockWriter).addAttribute("Parent", "543");
				one (mockWriter).addAttribute("StartDate", "1999-12-31 11:59:50.0");
				one (mockWriter).addAttribute("Status", "good");
				one (mockWriter).addAttribute("Width", "8");
				one (mockWriter).addAttribute("XLocation", "6");
				one (mockWriter).addAttribute("YLocation", "7");
				one (mockWriter).endNode();
			}
			{
				one (iteration).getAvailableEffort();
				will(returnValue(1.0F));
				one (iteration).getDescription();
				will(returnValue("Hello World"));
				one (iteration).getEndDate();
				will(returnValue(java.sql.Timestamp.valueOf("1999-12-31 11:59:59")));
				one (iteration).getHeight();
				will(returnValue(5));
				one (iteration).getId();
				will(returnValue(34L));
				one (iteration).getName();
				will(returnValue("John Smith"));
				one (iteration).getParent();
				will(returnValue(543L));
				one (iteration).getStartDate();
				will(returnValue(java.sql.Timestamp.valueOf("1999-12-31 11:59:50")));
				one (iteration).getStatus();
				will(returnValue("good"));
				one (iteration).getLocationX();
				will(returnValue(6));
				one (iteration).getLocationY();
				will(returnValue(7));
				one (iteration).getWidth();
				will(returnValue(8));
				
				exactly(3).of(iteration).getStoryCardChildren();
				will(returnValue(new ArrayList<StoryCard>(){
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
		converter.marshal(iteration, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}
	
	@Test
	public void shouldUnmarshalAnIteration(){
		Iteration iteration;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		final Sequence hasMoreChildrenSequence = context.sequence("hasMoreChildren");
		final StoryCard storyCard = context.mock(StoryCard.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("ID");				will(returnValue("999"));
				one (mockReader).getAttribute("Parent");			will(returnValue("887"));
				one (mockReader).getAttribute("Name");				will(returnValue("John Smith"));
				one (mockReader).getAttribute("Status");			will(returnValue("bad"));
				one (mockReader).getAttribute("Description");		will(returnValue("Hello"));
				one (mockReader).getAttribute("AvailableEffort");	will(returnValue("43"));
				one (mockReader).getAttribute("StartDate");			will(returnValue("1999-12-31 11:59:50.0"));
				one (mockReader).getAttribute("EndDate");			will(returnValue("1999-12-31 11:59:50.1"));
				one (mockReader).getAttribute("XLocation");			will(returnValue("3"));
				one (mockReader).getAttribute("YLocation");			will(returnValue("4"));
				one (mockReader).getAttribute("Width");				will(returnValue("600"));
				one (mockReader).getAttribute("Height");			will(returnValue("700"));
			}
			{
				one (mockReader).hasMoreChildren();
					inSequence(hasMoreChildrenSequence);
					will(returnValue(true));
				one (mockReader).moveDown();
					inSequence(hasMoreChildrenSequence);
				one (mockReader).getNodeName();
					inSequence(hasMoreChildrenSequence);
					will(returnValue("StoryCard"));
				one (unMarshalContext).convertAnother(with(any(Iteration.class)), with(equal(StoryCardDataObject.class)));
					inSequence(hasMoreChildrenSequence);
					will(returnValue(storyCard));
				one (storyCard).setParent(999L);
				one (mockReader).moveUp();
					inSequence(hasMoreChildrenSequence);
			
				one (mockReader).hasMoreChildren();
					inSequence(hasMoreChildrenSequence);
					will(returnValue(true));
				one (mockReader).moveDown();
					inSequence(hasMoreChildrenSequence);
				one (mockReader).getNodeName();
					inSequence(hasMoreChildrenSequence);
					will(returnValue("StoryCard"));
				one (unMarshalContext).convertAnother(with(any(Iteration.class)), with(equal(StoryCardDataObject.class)));
					inSequence(hasMoreChildrenSequence);
					will(returnValue(storyCard));
				one (storyCard).setParent(999L);
				one (mockReader).moveUp();
					inSequence(hasMoreChildrenSequence);
			
				one (mockReader).hasMoreChildren();	inSequence(hasMoreChildrenSequence);	will(returnValue(false));
			}
		});
		
		iteration = (Iteration)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(iteration.getId(), equalTo(999L));
		assertThat(iteration.getAvailableEffort(), equalTo(43F));
		assertThat(iteration.getStoryCardChildren(), hasItem(storyCard));
		
	}

	

}

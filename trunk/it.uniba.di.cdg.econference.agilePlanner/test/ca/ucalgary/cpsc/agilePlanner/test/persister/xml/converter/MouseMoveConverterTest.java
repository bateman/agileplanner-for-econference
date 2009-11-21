package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import persister.Event;
import persister.data.Backlog;
import persister.data.StoryCard;
import persister.data.impl.EventDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.xml.converter.MouseMoveConverter;
import persister.Event;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class MouseMoveConverterTest {

	Mockery context = new Mockery();
	MouseMoveConverter converter = new MouseMoveConverter();
	
	@Test
	public void shouldMarsalTheDataToDisconnectAClient(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Event move = context.mock(Event.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("Event");
				one (mockWriter).addAttribute("id", "3");
				one (mockWriter).addAttribute("name", "John Smith");
				one (mockWriter).addAttribute("locationX", "3");
				one (mockWriter).addAttribute("locationY", "4");
		        one (mockWriter).endNode();
			}
			{
				one (move).getId();
				will(returnValue(3L));
				one (move).getName();
				will(returnValue("John Smith"));
				one (move).getLocationX();
				will(returnValue(3));
				one (move).getLocationY();
				will(returnValue(4));
			}
		});
		
		//execute the code to test
		converter.marshal(move, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnMarshalAMouseMoveEvent(){
		Event move;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("id");will(returnValue("45"));
				one (mockReader).getAttribute("name");will(returnValue("John Smith"));
				one (mockReader).getAttribute("locationX");will(returnValue("2"));
				one (mockReader).getAttribute("locationY");will(returnValue("4"));
			}
		});
		
		move = (Event)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(move.getId(), equalTo(45L));
		assertThat(move.getName(), equalTo("John Smith"));
		assertThat(move.getLocationX(), equalTo(2));
		assertThat(move.getLocationY(), equalTo(4));
	}

}

package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;

import persister.Message;
import persister.xml.converter.*;
import persister.data.Iteration;
import persister.data.StoryCard;
import persister.data.impl.IterationDataObject;
import persister.data.impl.StoryCardDataObject;

import java.util.HashMap;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class MessageConverterTest {

	Mockery context = new Mockery();
	MessageConverter converter = new MessageConverter();
	
	@Test
	public void shouldMarsalTheMessageAndIgnoreTheNullMessageData(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Message msg = context.mock(Message.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).addAttribute("sender", "1");
				one (mockWriter).addAttribute("mtype", "3");
				one (mockWriter).addAttribute("etype", "2");
			}
			{
				one (msg).getSender();
				will(returnValue(1L));
				one (msg).getMessageType();
				will(returnValue(3));
				one (msg).getEtype();
				will(returnValue(2));
				one (msg).getMessage();
				will(returnValue(null));
				one (msg).getData();
				will(returnValue(null));
			}
		});
		
		//execute the code to test
		converter.marshal(msg, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldMarsalTheMessageAndIncludeAStoryCard(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Message msg = context.mock(Message.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		final StoryCard storyCard = new StoryCardDataObject();
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).addAttribute("sender", "1");
				one (mockWriter).addAttribute("mtype", "3");
				one (mockWriter).addAttribute("etype", "2");
			}
			{
				one (msg).getSender();
				will(returnValue(1L));
				one (msg).getMessageType();
				will(returnValue(3));
				one (msg).getEtype();
				will(returnValue(2));
				atLeast(1).of (msg).getMessage();
				will(returnValue(storyCard));
				one (msg).getData();
				will(returnValue(null));
			}
			{
				one (marshalContext).convertAnother(with(equal(storyCard)), with(any(StoryCardConverter.class)));
			}
		});
		//execute the code to test
		converter.marshal(msg, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}
	
	@Test
	public void shouldMarsalTheMessageAndIncludeADataObject(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Message msg = context.mock(Message.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		final HashMap<String,String> data = new HashMap<String, String>(){
			{
				put("hey", "there");
				put("you", "spiffy");
				put("world", "you");
			}
		};
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).addAttribute("sender", "1");
				one (mockWriter).addAttribute("mtype", "3");
				one (mockWriter).addAttribute("etype", "2");
				one (mockWriter).startNode("Data");
				atLeast(1).of (mockWriter).startNode("Entry");
				atLeast(1).of (mockWriter).endNode();
				atLeast(1).of (mockWriter).addAttribute(with(any(String.class)), with(any(String.class)));
			}
			{
				one (msg).getSender();
				will(returnValue(1L));
				one (msg).getMessageType();
				will(returnValue(3));
				one (msg).getEtype();
				will(returnValue(2));
				one (msg).getMessage();
				will(returnValue(null));
				exactly(5).of (msg).getData();
				will(returnValue(data));
			}
		});
		//execute the code to test
		converter.marshal(msg, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}
	
	@Test
	public void shouldUnMarshalMessageWithAnIteration() throws Exception {
		Message msg;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("mtype");will(returnValue("54"));
				one (mockReader).getAttribute("sender");will(returnValue("465"));
				one (mockReader).getAttribute("etype");will(returnValue("43"));
				one (mockReader).hasMoreChildren();will(returnValue(true));
				one (mockReader).moveDown();
				atLeast(1).of (mockReader).getNodeName();will(returnValue("Iteration"));
				one (unMarshalContext).convertAnother(with(any(Message.class)), with(equal(IterationDataObject.class)));
				one (mockReader).moveUp();
				one (mockReader).hasMoreChildren();will(returnValue(false));
			}
		});
		
		msg = (Message)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(msg.getSender(), equalTo(465L));
		assertThat(msg.getEtype(), equalTo(43));
		assertThat(msg.getMessageType(), equalTo(54));
			
	}

}

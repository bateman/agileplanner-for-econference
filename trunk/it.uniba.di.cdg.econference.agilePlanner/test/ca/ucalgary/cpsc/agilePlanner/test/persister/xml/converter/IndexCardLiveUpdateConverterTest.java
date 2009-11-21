package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import persister.LiveUpdate;
import persister.data.Backlog;
import persister.data.StoryCard;
import persister.data.impl.StoryCardDataObject;
import persister.xml.converter.IndexCardLiveUpdateConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class IndexCardLiveUpdateConverterTest {

	Mockery context = new Mockery();
	IndexCardLiveUpdateConverter converter = new IndexCardLiveUpdateConverter();
	
	@Test
	public void shouldMarsalAnIndexCardUpdate(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final LiveUpdate indexCardLiveUpdate = context.mock(LiveUpdate.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("LiveUpdate");
				one (mockWriter).addAttribute("text", "Hello");
				one (mockWriter).addAttribute("field", "3");
				one (mockWriter).addAttribute("id", "5");
				one (mockWriter).endNode();
			}
			{
				one (indexCardLiveUpdate).getText();
				will(returnValue("Hello"));
				one (indexCardLiveUpdate).getField();
				will(returnValue(3));
				one (indexCardLiveUpdate).getId();
				will(returnValue((long)5));
			}
		});
		
		//execute the code to test
		converter.marshal(indexCardLiveUpdate, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnMarshalAnIndexCardUpdate(){
		LiveUpdate indexCardLiveUpdate;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("id"); will(returnValue("3"));
				one (mockReader).getAttribute("field"); will(returnValue("5"));
				one (mockReader).getAttribute("text"); will(returnValue("hello you sexy world you"));
			}
		});
		
		indexCardLiveUpdate = (LiveUpdate)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(indexCardLiveUpdate.getId(), equalTo(3L));
		assertThat(indexCardLiveUpdate.getField(), equalTo(5));
		assertThat(indexCardLiveUpdate.getText(), equalTo("hello you sexy world you"));
	}
}

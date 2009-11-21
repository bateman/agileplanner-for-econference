package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;



import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import persister.Disconnect;
import persister.data.Backlog;
import persister.data.StoryCard;
import persister.data.impl.StoryCardDataObject;
import persister.xml.converter.ExceptionConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class ExceptionConverterTest {

	Mockery context = new Mockery();
	ExceptionConverter converter = new ExceptionConverter();
	
	@Test
	public void shouldMarsalTheException(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("Exception");
				one (mockWriter).addAttribute("exception", "some Exception");
				one (mockWriter).endNode();
			}
		});
		
		//execute the code to test
		converter.marshal(new Exception("some Exception"), mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnmarshalAnException(){
		Exception e;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("exception");
					will(returnValue("hi world"));
			}
		});
		
		e = (Exception)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(e.getMessage(), equalTo("hi world"));
		
	}
	

}

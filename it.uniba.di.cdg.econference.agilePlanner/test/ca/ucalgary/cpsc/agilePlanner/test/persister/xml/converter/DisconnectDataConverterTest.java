package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

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
import persister.data.impl.DisconnectDataObject;
import persister.Disconnect;
import persister.xml.converter.DisconnectDataConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class DisconnectDataConverterTest {

	Mockery context = new Mockery();
	DisconnectDataConverter converter = new DisconnectDataConverter();
	
	@Test
	public void shouldMarsalTheDataToDisconnectAClient(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Disconnect disconnectData = context.mock(Disconnect.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("DisconnectData");
				one (mockWriter).addAttribute("clientid", "1");
				one (mockWriter).endNode();
			}
			{
				one (disconnectData).getClientId();
				will(returnValue(1));
			}
		});
		
		//execute the code to test
		converter.marshal(disconnectData, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnMarshalDisconnectionMessage(){
		Disconnect disconnectData;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("clientid"); will(returnValue("3"));
			}
		});
		
		disconnectData = (Disconnect)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(disconnectData.getClientId(), equalTo(3));
	}

}

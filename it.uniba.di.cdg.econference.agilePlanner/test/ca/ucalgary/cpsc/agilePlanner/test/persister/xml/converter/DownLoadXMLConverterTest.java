package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import org.junit.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import org.jmock.Mockery;
import org.jmock.Expectations;

import persister.data.Backlog;
import persister.xml.converter.DownLoadXMLConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class DownLoadXMLConverterTest {

	Mockery context = new Mockery();
	DownLoadXMLConverter converter = new DownLoadXMLConverter();
	
	@Test
	public void shouldMarsalADownload(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		String [] downloadedFile = new String [] {"hello", "world"};
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("DownloadFile");
				one (mockWriter).addAttribute("path", "hello");
				one (mockWriter).addAttribute("recognitionID", "world");
				one (mockWriter).endNode();
			}
		});
		
		//execute the code to test
		converter.marshal(downloadedFile, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnMarshalADownload(){
		String [] downloadedFile;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("path");will(returnValue("hello"));
				one (mockReader).getAttribute("recognitionID");will(returnValue("world"));
			}
		});
		
		downloadedFile = (String [])converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(downloadedFile[0], equalTo("hello"));
		assertThat(downloadedFile[1], equalTo("world"));
		
	}
	

}

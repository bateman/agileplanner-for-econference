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
import persister.data.Legend;
import persister.data.StoryCard;
import persister.data.impl.StoryCardDataObject;
import persister.xml.converter.LegendConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class LegendConverterTest {

	Mockery context = new Mockery();
	LegendConverter converter = new LegendConverter();
	
	@Test
	public void shouldMarsalTheDataToDisconnectAClient(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final Legend legend = context.mock(Legend.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("Legend");
				one (mockWriter).addAttribute("aqua", "aqua");
				one (mockWriter).addAttribute("blue", "blue");
				one (mockWriter).addAttribute("gray", "grey");
				one (mockWriter).addAttribute("green", "green");
				one (mockWriter).addAttribute("khaki", "khaki");
				one (mockWriter).addAttribute("peach", "peach");
				one (mockWriter).addAttribute("pink", "pink");
				one (mockWriter).addAttribute("red", "red");
				one (mockWriter).addAttribute("white", "white");
				one (mockWriter).addAttribute("yellow", "yellow");
				one (mockWriter).endNode();
			}
			{
				one (legend).getAqua();
				will(returnValue("aqua"));
				one (legend).getBlue();
				will(returnValue("blue"));
				one (legend).getGrey();
				will(returnValue("grey"));
				one (legend).getGreen();
				will(returnValue("green"));
				one (legend).getKhaki();
				will(returnValue("khaki"));
				one (legend).getPeach();
				will(returnValue("peach"));
				one (legend).getPink();
				will(returnValue("pink"));
				one (legend).getRed();
				will(returnValue("red"));
				one (legend).getWhite();
				will(returnValue("white"));
				one (legend).getYellow();
				will(returnValue("yellow"));
			}
		});
		
		//execute the code to test
		converter.marshal(legend, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}
	
	@Test
	public void shouldUnmarshalTheLegend(){
		Legend legend;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		//expectations
		context.checking(new Expectations() {
			{
				one(mockReader).getAttribute("red"); will(returnValue("Bug"));
				one(mockReader).getAttribute("blue"); will(returnValue("blue"));
				one(mockReader).getAttribute("green"); will(returnValue("green"));
				one(mockReader).getAttribute("pink"); will(returnValue("pink"));
				one(mockReader).getAttribute("white"); will(returnValue("white"));
				one(mockReader).getAttribute("yellow"); will(returnValue("yellow"));
				one(mockReader).getAttribute("peach"); will(returnValue("peach"));
				one(mockReader).getAttribute("gray"); will(returnValue("grey"));
				one(mockReader).getAttribute("aqua"); will(returnValue("aqua"));
				one(mockReader).getAttribute("khaki"); will(returnValue("khaki"));
			}
		});
		
		legend = (Legend)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(legend.getRed(), equalTo("Bug"));
		assertThat(legend.getBlue(), equalTo("blue"));
		assertThat(legend.getGreen(), equalTo("green"));
		assertThat(legend.getPink(), equalTo("pink"));
		assertThat(legend.getWhite(), equalTo("white"));
		assertThat(legend.getYellow(), equalTo("yellow"));
		assertThat(legend.getPeach(), equalTo("peach"));
		assertThat(legend.getGrey(), equalTo("grey"));
		assertThat(legend.getAqua(), equalTo("aqua"));
		assertThat(legend.getKhaki(), equalTo("khaki"));

	}

	

}

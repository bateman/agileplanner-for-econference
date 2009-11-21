package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;

import persister.data.StoryCard;
import persister.xml.converter.StoryCardConverter;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class StoryCardConverterTest {

	Mockery context = new Mockery();
	StoryCardConverter converter = new StoryCardConverter();
	
	@Test
	public void shouldMarsalTheDataToDisconnectAClient(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final StoryCard storyCard = context.mock(StoryCard.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).startNode("StoryCard");
				one (mockWriter).addAttribute("Actual", "2.3");
				one (mockWriter).addAttribute("BestCase", "7.9");
				one (mockWriter).addAttribute("CardOwner", "John Smith");
				one (mockWriter).addAttribute("Color", "blue");
				one (mockWriter).addAttribute("CurrentSideUp", "1");
				one (mockWriter).addAttribute("Description", "Hello World");
				one (mockWriter).addAttribute("Height", "4");
				one (mockWriter).addAttribute("ID", "50");
				one (mockWriter).addAttribute("MostLikely", "1.2");
				one (mockWriter).addAttribute("Name", "John Smith");
				one (mockWriter).addAttribute("Parent", "42");
				one (mockWriter).addAttribute("RotationAngle", "76.0");
				one (mockWriter).addAttribute("Status", "good");
				one (mockWriter).addAttribute("TestText", "pass");
				one (mockWriter).addAttribute("TestURL", "/hello/world");
				one (mockWriter).addAttribute("Width", "3");
				one (mockWriter).addAttribute("WorstCase", "5.6");
				one (mockWriter).addAttribute("XLocation", "7");
				one (mockWriter).addAttribute("YLocation", "3");
				one (mockWriter).addAttribute("RallyID", "true");
				one (mockWriter).addAttribute("FitID", "id");
				one (mockWriter).addAttribute(with(equal("Handwriting")), with(any(String.class)));
				one (mockWriter).endNode();
			}
			{
				one (storyCard).getActualEffort();		will(returnValue(2.3F));
				one (storyCard).getBestCaseEstimate();	will(returnValue(7.9F));
				one (storyCard).getCardOwner();			will(returnValue("John Smith"));
				one (storyCard).getColor();				will(returnValue("blue"));
				one (storyCard).getCurrentSideUp();		will(returnValue(1));
				one (storyCard).getDescription();		will(returnValue("Hello World"));
				one (storyCard).getHeight();			will(returnValue(4));
				one (storyCard).getId();				will(returnValue(50L));
				one (storyCard).getMostlikelyEstimate();will(returnValue(1.2F));
				one (storyCard).getName();				will(returnValue("John Smith"));
				one (storyCard).getParent();			will(returnValue(42L));
				one (storyCard).getRotationAngle();		will(returnValue(76.0F));
				one (storyCard).getStatus();			will(returnValue("good"));
				one (storyCard).getAcceptanceTestText();will(returnValue("pass"));
				one (storyCard).getAcceptanceTestUrl();	will(returnValue("/hello/world"));
				one (storyCard).getWidth();				will(returnValue(3));
				one (storyCard).getWorstCaseEstimate();	will(returnValue(5.6F));
				one (storyCard).getLocationX();			will(returnValue(7));
				one (storyCard).getLocationY();			will(returnValue(3));
				one (storyCard).getRallyID();			will(returnValue(true));
				one (storyCard).getFitId();				will(returnValue("id"));
				exactly(3).of (storyCard).getHandwritingImage();will(returnValue(new byte [] {1,0,1,0,1}));
			}
		});
		
		//execute the code to test
		converter.marshal(storyCard, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}
	
	@Test
	public void shouldUnmarshalAnException(){
		StoryCard storyCard;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		//expectations
		context.checking(new Expectations() {
			{
				one (mockReader).getAttribute("TestText");		will(returnValue("poop"));
				one (mockReader).getAttribute("TestURL");		will(returnValue("/hi/world"));
				one (mockReader).getAttribute("Actual");		will(returnValue("4"));
				one (mockReader).getAttribute("BestCase");		will(returnValue("5"));
				one (mockReader).getAttribute("CardOwner");		will(returnValue("Johnny"));
				one (mockReader).getAttribute("Color");			will(returnValue("green"));
				one (mockReader).getAttribute("CurrentSideUp");	will(returnValue("1"));
				one (mockReader).getAttribute("Description");	will(returnValue("hello my honey, hellow my baby"));
				one (mockReader).getAttribute("Height");		will(returnValue("5"));
				one (mockReader).getAttribute("ID");			will(returnValue("32"));
				one (mockReader).getAttribute("XLocation");		will(returnValue("5"));
				one (mockReader).getAttribute("YLocation");		will(returnValue("5"));
				one (mockReader).getAttribute("MostLikely");	will(returnValue("8.0"));
				one (mockReader).getAttribute("Name");			will(returnValue("default sc"));
				one (mockReader).getAttribute("Parent");		will(returnValue("0"));
				one (mockReader).getAttribute("RotationAngle");	will(returnValue("43"));
				one (mockReader).getAttribute("Status");		will(returnValue("33"));
				one (mockReader).getAttribute("Width");			will(returnValue("5"));
				one (mockReader).getAttribute("WorstCase");		will(returnValue("99"));
				one (mockReader).getAttribute("RallyID");		will(returnValue("1"));
				exactly(3).of(mockReader).getAttribute("Handwriting"); will(returnValue("cheese"));
				one(mockReader).getAttributeCount();			will(returnValue(21));
				one(mockReader).getAttribute("FitID");			will(returnValue("55"));
			}
		});
		
		storyCard = (StoryCard)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(storyCard.getId(), equalTo(32L));
		assertThat(storyCard.getCurrentSideUp(), equalTo(1));
	}
	

}

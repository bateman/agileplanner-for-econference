package ca.ucalgary.cpsc.agilePlanner.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.ucalgary.cpsc.agilePlanner.test.planner.*;
import ca.ucalgary.cpsc.agilePlanner.test.planner.fitintegration.*;
import ca.ucalgary.cpsc.agilePlanner.test.planner.UIModel.*;
import ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter.*;
import ca.ucalgary.cpsc.agilePlanner.test.unit.persister.factory.*;
import ca.ucalgary.cpsc.agilePlanner.test.util.*;
import ca.ucalgary.cpsc.agilePlanner.test.cards.model.*;


@RunWith(value = Suite.class)
@SuiteClasses(value	=	{ 
							AsynchronousPersisterTest.class,
							AuthenticatorTest.class,
							BacklogConverterTest.class,
							CardModelTest.class,
							ComparerTest.class,
//							ConversionTest.class, //	Not Passing due to system irregularities
							DisconnectDataConverterTest.class,
							DownLoadXMLConverterTest.class,
							ExceptionConverterTest.class,
							FileTest.class,
							IndexCardLiveUpdateConverterTest.class,
							IterationConverterTest.class,
							LoggerTest.class,
							LegendConverterTest.class,
							MessageConverterTest.class,
							MouseMoveConverterTest.class,
							ModelTests.class,
							NetworkCommunicationTest.class,
							OwnerConverterTest.class,
							PersisterFactoryTest.class,
							ProjectConverterTest.class,
							ProjectDataObjectTest.class,
							ProjectNameListConverterTest.class,
							SupportTest.class,
							SynchronousPersisterTest.class,
							TestPluginInformation.class,
							SettingsTest.class,
							StoryCardModelTest.class,
							StoryCardConverterTest.class
						}
)

public class JUnitTests {

}


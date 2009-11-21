package ca.ucalgary.cpsc.agilePlanner.test.unit.persister.impl.data;

import persister.Event;
import persister.data.impl.EventDataObject;

import org.junit.Test;
import static org.junit.Assert.*;

public class EventDataObjectTest {
	
	@Test
	public void shouldInitializeWithAnID() throws Exception {
		Event e = new EventDataObject();
		assertEquals(0L, e.getId());
	}
	
	@Test
	public void shouldBeAbleToSetAnID() throws Exception {
		Event e = new EventDataObject();
		e.setId(4L);
	}
	
}

package ca.ucalgary.cpsc.agilePlanner.test.cards.model;

import org.junit.Test;
import static org.junit.Assert.*;
import cards.model.*;

public class StoryCardModelTest {
	
	
	
	@Test
	public void shouldReturnCorrespondingColoredSCMClass(){
		assertEquals(StoryCardModelRed.class, StoryCardModel.getChildClassOfColor("red"));
		assertEquals(StoryCardModelBlue.class, StoryCardModel.getChildClassOfColor("blue"));
		assertEquals(StoryCardModelPink.class, StoryCardModel.getChildClassOfColor("pink"));
		assertEquals(StoryCardModelGreen.class, StoryCardModel.getChildClassOfColor("green"));
		assertEquals(StoryCardModelYellow.class, StoryCardModel.getChildClassOfColor("yellow"));
		assertEquals(StoryCardModelPeach.class, StoryCardModel.getChildClassOfColor("peach"));
		assertEquals(StoryCardModelWhite.class, StoryCardModel.getChildClassOfColor("white"));
		assertEquals(StoryCardModelAqua.class, StoryCardModel.getChildClassOfColor("aqua"));
		assertEquals(StoryCardModelKhaki.class, StoryCardModel.getChildClassOfColor("khaki"));
		//abnormal passable
		assertEquals(StoryCardModelRed.class, StoryCardModel.getChildClassOfColor("RED"));
		//false colors
		assertNull(StoryCardModel.getChildClassOfColor("purple"));
	}

}

package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;


import java.io.File;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.StoryCard;
import persister.data.TeamMember;
import persister.data.impl.BacklogDataObject;
import persister.data.impl.IterationDataObject;
import persister.data.impl.LegendDataObject;
import persister.data.impl.ProjectDataObject;
import persister.data.impl.StoryCardDataObject;
import persister.data.impl.TeamMemberDataObject;
import persister.xml.converter.Converter;

public class ConversionTest {
	private Backlog backlog;
	private String backlogXml;
	
	private TeamMember teamMember;
	private String ownerXml;
	
	private Project project;
	private String projectXml;

	private StoryCard storyCard;
	private String storyCardXml;
	
	private Legend legend;
	private String legendXml;
	
	private Iteration iteration;
	private String iterationXml;
	
	public void initBacklog()throws Exception{
		backlog = new BacklogDataObject();
		backlog.addStoryCard(new StoryCardDataObject());
		backlogXml = util.File.readAll(new File("TestData/XmlExamples/backlog.xml"));
	}
	
	public void initOwner()throws Exception{
		teamMember = new TeamMemberDataObject("John");
	
		ownerXml = util.File.readAll(new File("TestData/XmlExamples/teamMember.xml"));
	}
	public void initStoryCard()throws Exception{
		storyCard = new StoryCardDataObject();
	}
	public void initLegend()throws Exception{
		legend = new LegendDataObject();
	}
	public void initIteration()throws Exception{
		iteration = new IterationDataObject();
		initStoryCard();
		iteration.addStoryCard(storyCard);
	}
	
	public void initProject()throws Exception{
		project = new ProjectDataObject();
		project.addIteration(new IterationDataObject());
		initOwner();
		project.addTeamMember(teamMember);
		initBacklog();
		project.setBacklog(backlog);
		project.setLegend(new LegendDataObject());
		projectXml = util.File.readAll(new File("TestData/XmlExamples/project.xml"));
	}
	
	@Test
	public void testThatAllConvertersAreBiDirectional() throws Exception{
		initOwner();
		assertEquals("TeamMember conversion is not BiDirectional", teamMember, (TeamMember)Converter.fromXML(Converter.toXML(teamMember)));
		
		initStoryCard();
		assertEquals("Legend conversion is not BiDirectional", storyCard, (StoryCard)Converter.fromXML(Converter.toXML(storyCard)));
		
		initLegend();
		assertEquals("StoryCard conversion is not BiDirectional", legend, (Legend)Converter.fromXML(Converter.toXML(legend)));
		
		initBacklog();
		assertEquals("Backlog conversion is not BiDirectional", backlog, (Backlog)Converter.fromXML(Converter.toXML(backlog)));
		
		initIteration();
		assertEquals("Iteration conversion is not BiDirectional", iteration, (Iteration)Converter.fromXML(Converter.toXML(iteration)));
		
		initProject();
		assertEquals("Project conversion is not BiDirectional", project, (Project)Converter.fromXML(Converter.toXML(project)));
		
	}
	
	

}

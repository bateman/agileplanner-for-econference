package ca.ucalgary.cpsc.agilePlanner.test.persister.xml.converter;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.Matcher.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.Sequence;

import persister.xml.converter.*;
import persister.*;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.TeamMember;
import persister.data.impl.*;

import java.util.ArrayList;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class ProjectConverterTest {

	Mockery context = new Mockery();
	ProjectConverter converter = new ProjectConverter();
	
	@Test
	public void shouldMarsalTheDataToDisconnectAClient(){
		//mocks of objects we are not testing
		final HierarchicalStreamWriter mockWriter = context.mock(HierarchicalStreamWriter.class);
		final MarshallingContext marshalContext = context.mock(MarshallingContext.class);
		final Project project = context.mock(Project.class);

		//setup backlog
		final Backlog backlog = context.mock(Backlog.class);
		//setup iteration
		final Iteration iter = context.mock(Iteration.class);
		final ArrayList<Iteration> iterations = new ArrayList<Iteration>(){
			{
				add(iter);
				add(iter);
			}
		};
		//setup legend
		final Legend legend = context.mock(Legend.class);
		//setup owner children
		final TeamMember teamMember = context.mock(TeamMember.class);
		final ArrayList<TeamMember> teamMembers = new ArrayList<TeamMember>(){
			{
				add(teamMember);
				add(teamMember);
				add(teamMember);
			}
		};
		
		//expectations
		context.checking(new Expectations() {
			{
				one (mockWriter).addAttribute("ID", "4");
				one (mockWriter).addAttribute("Name", "Super Code");
			}
			{
				one (project).getId();
				will(returnValue(4L));
				one (project).getName();
				will(returnValue("Super Code"));
				exactly(2).of (project).getBacklog();
				will(returnValue(backlog));
				exactly(3).of (project).getIterationChildren();
				will(returnValue(iterations));
				exactly(2).of (project).getLegend();
				will(returnValue(legend));
				exactly(3).of (project).getTeamMembers();
				will(returnValue(teamMembers));
			}
			{
				one (marshalContext).convertAnother(with(equal(backlog)));
				exactly(2).of (marshalContext).convertAnother(with(equal(iter)));
				one (marshalContext).convertAnother(with(equal(legend)));
				exactly(3).of(marshalContext).convertAnother(with(equal(teamMember)));
			}
		});
		
		//execute the code to test
		converter.marshal(project, mockWriter, marshalContext);
		
		//check the result
		context.assertIsSatisfied();
	}

	@Test
	public void shouldUnmarshalAProject(){
		Project project;
		final HierarchicalStreamReader mockReader = context.mock(HierarchicalStreamReader.class);
		final UnmarshallingContext unMarshalContext = context.mock(UnmarshallingContext.class);
		final Sequence hasMoreChildrenSequence = context.sequence("hasMoreChildrenSequence"); 
		final Backlog backlog = context.mock(Backlog.class);
		//expectations
		context.checking(new Expectations() {
			{ 
				one (mockReader).getAttribute("Name");	will(returnValue("a Profound Project"));
				one (mockReader).getAttribute("ID");	will(returnValue("50004"));
			}
			{
				one (mockReader).hasMoreChildren();
					inSequence(hasMoreChildrenSequence);
					will(returnValue(true));
				one (mockReader).moveDown();
					inSequence(hasMoreChildrenSequence);
				atLeast(1).of(mockReader).getNodeName();
					inSequence(hasMoreChildrenSequence);
					will(returnValue("Backlog"));
				one (unMarshalContext).convertAnother(with(any(Project.class)), with(equal(BacklogDataObject.class)));
					inSequence(hasMoreChildrenSequence);
					will(returnValue(backlog));
				one (backlog).setParent(50004L);
					inSequence(hasMoreChildrenSequence);
				one (mockReader).moveUp();
					inSequence(hasMoreChildrenSequence);
				
				one (mockReader).hasMoreChildren();
					inSequence(hasMoreChildrenSequence);
					will(returnValue(false));
			}
		});
		
		project = (Project)converter.unmarshal(mockReader, unMarshalContext);
		
		context.assertIsSatisfied();
		assertThat(project.getId(), equalTo(50004L));
		
	}

}

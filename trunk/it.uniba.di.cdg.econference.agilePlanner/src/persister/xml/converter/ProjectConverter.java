package persister.xml.converter;

import persister.*;
import persister.data.Backlog;
import persister.data.Iteration;
import persister.data.Legend;
import persister.data.Project;
import persister.data.TeamMember;
import persister.data.impl.*;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


public class ProjectConverter implements Converter {

	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		Project project = (Project)value;
		
		writer.addAttribute("ID", String.valueOf(project.getId()));
		writer.addAttribute("Name", project.getName());
		
		if(project.getBacklog() != null){
			context.convertAnother(project.getBacklog());
		}
		if(project.getIterationChildren() != null){
			if(project.getIterationChildren().size() > 0){
				for(Iteration iter : project.getIterationChildren()){
					context.convertAnother(iter);
				}
			}
		}	
		if(project.getLegend() != null){
			context.convertAnother(project.getLegend());
		}
		if(project.getTeamMembers()!=null){
			if(project.getTeamMembers().size() > 0){
				for(TeamMember teamMember : project.getTeamMembers()){
					context.convertAnother(teamMember);
				}
			}
			
		}
	}

	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		ProjectDataObject project = new ProjectDataObject();
		project.setName(reader.getAttribute("Name"));
		project.setId(Long.valueOf(reader.getAttribute("ID")));
		while(reader.hasMoreChildren()){
			reader.moveDown();
			if("Backlog".equals(reader.getNodeName())){
				Backlog backlog = (Backlog)context.convertAnother(project, BacklogDataObject.class);
				project.setBacklog(backlog);
			}
			if("Iteration".equals(reader.getNodeName())){
				Iteration iteration = (Iteration)context.convertAnother(project, IterationDataObject.class);
				project.addIteration(iteration);
			}
			if("Legend".equals(reader.getNodeName())){
				Legend legend = (Legend)context.convertAnother(project, LegendDataObject.class);
				project.setLegend(legend);
			}
			if("USERS".equals(reader.getNodeName())){
				TeamMember teamMember = (TeamMember)context.convertAnother(project, TeamMemberDataObject.class);
			    project.addTeamMember(teamMember);
			}
			reader.moveUp();
		}
		
		return project;
	}

	public boolean canConvert(Class clazz) {
		
		for(Class c : clazz.getInterfaces()){
			if(c.equals(Project.class)){
				return true;
			}
		}
		return false;
	}

}

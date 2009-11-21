package persister.data.impl;

import persister.data.TeamMember;

public class TeamMemberDataObject implements TeamMember {

	private String name;
	
	public TeamMemberDataObject(){
		
	}
	
	public TeamMemberDataObject(String name){
		this.name=name;
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name=name;
	}
	
	public TeamMemberDataObject clone(){
		TeamMemberDataObject clone = new TeamMemberDataObject();
		clone.name=getName();
		return clone;
	}

	public boolean equals(Object other){
		return getName().equals(((TeamMember)other).getName());
	}
}

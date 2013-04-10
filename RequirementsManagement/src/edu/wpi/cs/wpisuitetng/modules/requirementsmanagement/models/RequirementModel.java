/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent.EventType;

/**
 *
 * The requirment model
 * @author TODO
 *
 */
public class RequirementModel extends AbstractModel {

	/** the id */
	private int id;
	/** the release number*/
	private ReleaseNumber releaseNumber;
	/** the status*/
	private RequirementStatus status;
	/** the priority*/
	private RequirementPriority priority;
	/** the name*/
	private String name;
	/** the description*/
	private String description;
	/** the estimate*/
	private int estimate;
	/** the actual effort*/
	private int actualEffort;
	/** the date of creation*/
	private Date creationDate;
	/** the date this was last modified*/
	private Date lastModifiedDate;
	/** the events that happened to this requirement*/
	private List<RequirementEvent> events;
	/** who made this requirement*/
	private User creator;
	/** who is assigned to this*/
	private List<User> assignees;
	/** the iteration*/
	private Iteration iteration;
	/** the type or requirement*/
	private RequirementType type;

	//TODO Validation Classes for Database retrieval
	/** the sub requirements*/
	private List<String> subRequirements;
	//TODO add attachments
	//TODO add tasks
	//TODO add notes
	//TODO add acceptance tests
	
	/**
	 * Constructs a new RequirementModel with default properties.
	 */
	public RequirementModel() {
		id = -1;
		name = "";
		description = "";
		status = RequirementStatus.NEW;
		creator = new User("", "", "", -1);
		assignees = new ArrayList<User>();
//		assignees.add(new User("", "", "", -1));
		estimate = 0;
		actualEffort = 0;
		creationDate = new Date();
		lastModifiedDate = new Date();
		events = new ArrayList<RequirementEvent>();
		subRequirements = new ArrayList<String>();
		iteration = null;
		setType(null);
		priority = null;
		releaseNumber = null;
	}
	
	/**
	 * Constructs a new RequirementModel with the given properties
	 * Other properties are the same as the default constructor
	 * 
	 * @param id the unique id of the RequirementModel
	 * @param releaseNumber the release number for the requirement
	 * @param status the status of the RequirementModel
	 * @param priority the priority of the requirement
	 * @param name the name of the requirement
	 * @param description a description of the requirement
	 * @param estimate an estimate of the time needed to complete
	 * @param actualEffort  how much effort the requirement takes in practice
	 * @param creator the user who created the requirement
	 * @param assignee the user who is assigning the requirement
	 * @param creationDate the date the requirement was created
	 * @param lastModifiedDate the date the requirement was last changed (should default to the creation date)
	 * @param events List of events for this requirement model
	 * @param subRequirementIDs a list of id numbers for associated sub-requirements
	 * @param iteration the iteration the requirement is assigned to
	 * @param type the type of the requirement
	 */
	public RequirementModel(int id, ReleaseNumber releaseNumber, RequirementStatus status,
			RequirementPriority priority, String name, String description,
			int estimate, int actualEffort, User creator, List<User> assignees,
			Date creationDate, Date lastModifiedDate, List<RequirementEvent> events,
			List<String> subRequirements, Iteration iteration, RequirementType type) {
		super();
		this.id = id;
		this.releaseNumber = releaseNumber;
		this.status = status;
		this.priority = priority;
		this.name = name;
		this.description = description;
		this.estimate = estimate;
		this.actualEffort = actualEffort;
		this.creator = creator;
		this.assignees = assignees;
		this.creationDate = creationDate;
		this.lastModifiedDate = lastModifiedDate;
		this.events = events;
		this.iteration = iteration;
		this.setType(type);
		
		this.subRequirements = subRequirements;
		
	}
	
	/**
	 *add a note to the requirement
	 *
	 * @param user the user that added the note
	 * @param body what the note says
	 * @param date the date the note was added
	 */
	public void addNote(User user, String body, Date date) {
		RequirementNote note = new RequirementNote(id, user, body);
		note.setDate(date);
		this.events.add(note);
		this.lastModifiedDate = date;
	}

	/**
	 *add a note to the requirement
	 *
	 * @param user the user that added the note
	 * @param body what the note says
	 */
	public void addNote(User user, String body) {
		RequirementNote note = new RequirementNote(id, user, body);
		note.setDate(new Date());
		this.events.add(note);
		this.lastModifiedDate = new Date();
	}
	
	/**
	 * get the requirement's notes
	 *
	 * @return an array of the requirement's notes
	 */
	public RequirementNote[] getNotes() {
		ArrayList<RequirementEvent> noteList = new ArrayList<RequirementEvent>();
		for (RequirementEvent event : events) {
			if (event.getEventType() == EventType.NOTE) {
				noteList.add(event);
			}
		}
		RequirementNote[] comments = new RequirementNote[1];
		return noteList.toArray(comments);
	}

	/**
	 * @return the iteration
	 */
	public Iteration getIteration() {
		return iteration;
	}

	/**
	 * @param iteration the iteration to set
	 */
	public void setIteration(Iteration iteration) {
		this.iteration = iteration;
	}

	/**
	 * get the list of events
	 *
	 * @return the list of events
	 */
	public List<RequirementEvent> getEvents() {
		return events;
	}

	/**
	 * set the list of events
	 *
	 * @param events the list of events to set to this
	 */
	public void setEvents(List<RequirementEvent> events) {
		this.events = events;
	}

	/**
	 * get the date this was made
	 *
	 * @return the date this requirement was made
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * set the creation date
	 *
	 * @param creationDate the creation date to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * get the date this was last modified
	 *
	 * @return the date this was last modified
	 */
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * set the date this was last modified
	 *
	 * @param lastModifiedDate the date this was last modified
	 */
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	/**
	 *set the creator
	 *
	 * @param creator the creator of this
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	/**
	 * get the creator
	 *
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}
	
	/**
	 * set the requirement's assignees
	 *
	 * @param assignees the assignees to set to this requirement
	 */
	public void setAssignees(List<User> assignees) {
		this.assignees = assignees;
	}
	
	/**
	 * get this requirement's assignees
	 *
	 * @return this requirement's assignees
	 */
	public List<User> getAssignees() {
		return assignees;
	}
		
	/**
	 * get the release number
	 *
	 * @return the realse number
	 */
	public ReleaseNumber getReleaseNumber() {
		return releaseNumber;
	}

	/**
	 * set the release number
	 *
	 * @param the release number
	 */
	public void setReleaseNumber(ReleaseNumber releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	/**
	 *get the id
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 *set the id
	 *
	 * @param id the id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *get the status
	 *
	 * @return the status
	 */
	public RequirementStatus getStatus() {
		return status;
	}

	/**
	 *set the status
	 *
	 * @param status the status
	 */
	public void setStatus(RequirementStatus status) {
		this.status = status;
	}

	/**
	 *get the priority
	 *
	 * @return the priority
	 */
	public RequirementPriority getPriority() {
		return priority;
	}

	/**
	 *set the priority
	 *
	 * @param priority the priority
	 */
	public void setPriority(RequirementPriority priority) {
		this.priority = priority;
	}

	/**
	 *get the name
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 *set the name
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 *get the description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 *set the description
	 *
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *get the estimate
	 *
	 * @return the estimate
	 */
	public int getEstimate() {
		return estimate;
	}

	/**
	 *set the estimate
	 *
	 * @param estimate the estimate
	 */
	public void setEstimate(int estimate) {
		this.estimate = estimate;
	}

	/**
	 *get the actual effort
	 *
	 * @return the actual effort
	 */
	public int getActualEffort() {
		return actualEffort;
	}

	/**
	 *set the actual effort
	 *
	 * @param actualEffort the actual effort
	 */
	public void setActualEffort(int actualEffort) {
		this.actualEffort = actualEffort;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}

	/**
	 * Converts this Requirement to a JSON string
	 * @return a string in JSON representing this Requirement
	 */
	@Override
	public String toJSON() {
		String json;

		//the following method works but if wee need special serializers then the second method is needed
		Gson gson = new Gson();

		//this is used is special serializers are needed
//		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
//		Gson gson = builder.create();
		
		json = gson.toJson(this, RequirementModel.class);
		return json;
	}

	//TODO finish this code...
	/**
	 * @param json Json string to parse containing Requirement
	 * @return The Requirement given by json
	 */
	public static RequirementModel fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
		addGsonDependencies(builder);
		return builder.create().fromJson(json, RequirementModel.class);
	}
	
	/**
	 * @param json Json string to parse containing Requirement array
	 * @return The Requirement array given by json
	 */
	public static RequirementModel[] fromJSONArray(String json) {
		GsonBuilder builder = new GsonBuilder();
		addGsonDependencies(builder);
		return builder.create().fromJson(json, RequirementModel[].class);
	}
	
	/**
	 * Add dependencies necessary for Gson to interact with this class
	 * @param builder Builder to modify
	 */
	public static void addGsonDependencies(GsonBuilder builder) {
		RequirementEvent.addGsonDependencies(builder);
		//TODO add dependencies for future class associations. Only needed if normal (de)serializer does not work
//		Task.addGsonDependencies(builder);
//		Note.addGsonDependencies(builder);
//		AbstractAttachment.addGsonDependencies(builder);
	}

	/**
	 *interface documentation says this is necessary for the mock database
	 *not sure if this is still needed otherwise
	 *
	 * @param o
	 * @return
	 */
	@Override
	public Boolean identify(Object o) {
		Boolean returnValue = false;
		if(o instanceof RequirementModel && id == ((RequirementModel) o).getId()) {
			returnValue = true;
		}
		if(o instanceof String && Integer.toString(id).equals(o)) {
			returnValue = true;
		}
		return returnValue;
	}

	/**
	 *toString() method
	 *
	 * @return the JSON string
	 */
	@Override
	public String toString() {
		return toJSON();
	}
	/**
	 * 
	 * @return ArrayList of the sub-requirements
	 */
	public List<String> getSubRequirements() {
		return subRequirements;
	}
	/**
	 * Changes the list of sub-requirement IDs
	 * @param subRequirements new list of requirements
	 */
	public void setSubRequirements(ArrayList<String> subRequirements) {
		this.subRequirements = subRequirements;
	}
	/**
	 * Adds a requirement ID to the list of sub-requirements
	 * @param subreq Requirement to add to list of sub-requirements
	 */
	public void addSubRequirement(String subreqID){
		if (!this.subRequirements.contains(subreqID)) {
			this.subRequirements.add(subreqID);
		}
	}
//	/**
//	 * Removes a sub requirement from this requirement
//	 * @param subreqID The ID of the requirement to be removed
//	 */
//	public void removeSubRequirement(int subreqID){
//		this.subRequirements.remove(this.subRequirements.indexOf(subreqID));
//	}

	/**
	 * @return the type
	 */
	public RequirementType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(RequirementType type) {
		this.type = type;
	}
}



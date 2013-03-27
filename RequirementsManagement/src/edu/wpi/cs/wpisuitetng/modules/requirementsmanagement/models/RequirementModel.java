package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent.EventType;

public class RequirementModel extends AbstractModel {

	private int id;
	private int releaseNumber;
	private RequirementStatus status;
	private RequirementPriority priority;
	private String name;
	private String description;
	private String estimate;
	private String actualEffort;
	private Date creationDate;
	private Date lastModifiedDate;
	private List<RequirementEvent> events;
	private User creator;
	private List<User> assignees;
	private Iteration iteration;
	private RequirementType type;

	//TODO Validation Classes for Database retrieval
	private List<RequirementModel> subRequirements;
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
		creator = null;//new User("", "", "", -1);
		assignees = new ArrayList<User>();
		//assignees.add(new User("", "", "", -1));
		estimate = "";
		actualEffort = "";
		creationDate = new Date();
		lastModifiedDate = new Date();
		events = new ArrayList<RequirementEvent>();
		subRequirements = new ArrayList<RequirementModel>();
		iteration = new Iteration();
		setType(null);
		priority = null;
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
	public RequirementModel(int id, int releaseNumber, RequirementStatus status,
			RequirementPriority priority, String name, String description,
			String estimate, String actualEffort, User creator, List<User> assignees,
			Date creationDate, Date lastModifiedDate, List<RequirementEvent> events,
			List<RequirementModel> subRequirements, Iteration iteration, RequirementType type) {
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
	
	public void addNote(User user, String body, Date date) {
		RequirementNote note = new RequirementNote(id, user, body);
		note.setDate(date);
		this.events.add(note);
		this.lastModifiedDate = date;
	}

	public void addNote(User user, String body) {
		RequirementNote note = new RequirementNote(id, user, body);
		note.setDate(new Date());
		this.events.add(note);
		this.lastModifiedDate = new Date();
	}
	
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

	public List<RequirementEvent> getEvents() {
		return events;
	}

	public void setEvents(List<RequirementEvent> events) {
		this.events = events;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public User getCreator() {
		return creator;
	}
	
	public void setAssignees(List<User> assignees) {
		this.assignees = assignees;
	}
	
	public List<User> getAssignees() {
		return assignees;
	}
		
	public int getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(int releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RequirementStatus getStatus() {
		return status;
	}

	public void setStatus(RequirementStatus status) {
		this.status = status;
	}

	public RequirementPriority getPriority() {
		return priority;
	}

	public void setPriority(RequirementPriority priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEstimate() {
		return estimate;
	}

	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	public String getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(String actualEffort) {
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
	 * @return a string in JSON representing this Defect
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
	
	// interface documentation says this is necessary for the mock database
	// not sure if this is still needed otherwise
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

	@Override
	public String toString() {
		return toJSON();
	}
	/**
	 * 
	 * @return ArrayList of the sub-requirements
	 */
	public List<RequirementModel> getSubRequirements() {
		return subRequirements;
	}
	/**
	 * Changes the list of sub-requirement IDs
	 * @param subRequirements new list of requirements
	 */
	public void setSubRequirementIDs(ArrayList<RequirementModel> subRequirements) {
		this.subRequirements = subRequirements;
	}
	/**
	 * Adds a requirement ID to the list of sub-requirements
	 * @param subreq Requirement to add to list of sub-requirements
	 */
	public void addSubRequirementID(RequirementModel subreq){
		this.subRequirements.add(subreq);
	}
	/**
	 * Removes a sub requirement from this requirement
	 * @param subreqID The ID of the requirement to be removed
	 */
	public void removeSubRequirementID(int subreqID){
		this.subRequirements.remove(this.subRequirements.indexOf(subreqID));
	}

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



package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent.EventType;

/**
 * Implementations of this class represent some kind of event in a ProjectEvent.
 * For example, the addition of a comment or the modification of fields.
 * 
 * @author Jacob Palnick
 */
public class ProjectEvent extends AbstractModel {
	
	private int id;
	protected Date date = new Date();
	protected User user = new User("", "", "", -1);
	private Map<String, FieldChange<?>> changes;
	
	//TODO figure out how to tell what model/object was changed....
	
	/**
	 * Construct a RequirementEvent with default properties.
	 */
	public ProjectEvent() {
		changes = new HashMap<String, FieldChange<?>>();
	}

	/**
	 * @return The Date when this event happened
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * @param date The Date of the Event to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * @return The User responsible for this event
	 */
	public User getUser() {
		return user;
	}
	
	/**
	 * @param user The User responsible for the event to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @return The id of this event
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id The number to set the id of this event to
	 */
	public void setId(int id) {
		this.id = id;
	}
	
//	/**
//	 * Given a builder, add anything to it that's necessary for Gson to interact with this class.
//	 * 
//	 * @param builder The builder to modify
//	 */
//	public static void addGsonDependencies(GsonBuilder builder) {
//		builder.registerTypeAdapter(ProjectEvent.class, new RequirementEventDeserializer());
//		builder.registerTypeAdapter(RequirementChangeset.class, new RequirementChangesetDeserializer());
//	}
	
	/**
	 * @return the map of field names to changes (Assignee -> (Bob, Joe))
	 */
	public Map<String, FieldChange<?>> getChanges() {
		return changes;
	}

	/**
	 * @param changes the changes to set
	 */
	public void setChanges(Map<String, FieldChange<?>> changes) {
		this.changes = changes;
	}

	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, ProjectEvent.class);
		return json;
	}

	/**
	 * @param json Json string to parse containing ProjectEvent
	 * @return The ProjectEvent given by json
	 */
	public static ProjectEvent fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
		return builder.create().fromJson(json, ProjectEvent.class);
	}
	
	/**
	 * @param json Json string to parse containing ProjectEvent array
	 * @return The ProjectEvent array given by json
	 */
	public static ProjectEvent[] fromJSONArray(String json) {
		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
		return builder.create().fromJson(json, ProjectEvent[].class);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
	}
	
	// this model will only be created server side and then retrieved as part of a ProjectEvent in the future
	// so I'm not sure if this is necessary
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

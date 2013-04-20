/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jacob Palnick
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Implementations of this class represent some kind of event in a ProjectEvent.
 * For example, the addition of a comment or the modification of fields.
 * 
 * @author Jacob Palnick
 */
public class ProjectEvent extends AbstractModel {
	
	/**
	 *
	 * The types of project events
	 *
	 */
	public enum ProjectEventType {
		CREATION,
		CHANGES
	};

	/** id number */
	protected int id;
	/** date of event */
	protected Date date = new Date();
	/** user */
	protected User user = new User("", "", "", -1);
	/** changes made */
	protected Map<String, FieldChange<?>> changes;
	/** type of object */
	protected ProjectEventObjectType objectType;
	/** object's id */
	protected String objectId;
	/** type of project event */
	protected ProjectEventType type;
	
	//TODO figure out how to tell what model/object was changed....
	
	/**
	 * @return the type
	 */
	public ProjectEventType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ProjectEventType type) {
		this.type = type;
	}

	/**
	 *create a project event of type creation
	 *
	 * @param user the user
	 * @param objectType the ype of object
	 * @param objectId the object's id
	 * @return the created project event
	 */
	public static ProjectEvent createProjectCreationEvent(User user, ProjectEventObjectType objectType, String objectId) {
		ProjectEvent tmp = new ProjectEvent(user, objectType, objectId);
		tmp.setType(ProjectEventType.CREATION);
		return tmp;
	}
	
	/**
	 * create a project event of type creation
	 *
	 * @param objectType the object's type
	 * @param objectId the object's id
	 * @return the created project event
	 */
	public static ProjectEvent createProjectCreationEvent(ProjectEventObjectType objectType, String objectId) {
		ProjectEvent tmp = new ProjectEvent(objectType, objectId);
		tmp.setType(ProjectEventType.CREATION);
		return tmp;
	}
	
	/**
	 * create a project event of type changes
	 *
	 * @param user the user
	 * @param objectType the type of object
	 * @param objectId the object's id
	 * @return the created project event
	 */
	public static ProjectEvent createProjectChangesetEvent(User user, ProjectEventObjectType objectType, String objectId) {
		ProjectEvent tmp = new ProjectEvent(user, objectType, objectId);
		tmp.setType(ProjectEventType.CHANGES);
		return tmp;
	}
	
	/**
	 * create a project event of type changed
	 *
	 * @param objectType the object's type
	 * @param objectId the object's id
	 * @return
	 */
	public static ProjectEvent createProjectChangesetEvent(ProjectEventObjectType objectType, String objectId) {
		ProjectEvent tmp = new ProjectEvent(objectType, objectId);
		tmp.setType(ProjectEventType.CHANGES);
		return tmp;
	}
	
	/**
	 * Construct a ProjectEventChangeset with the given properties.
	 * Other properties are the same as in the default constructor.
	 * 
	 * @param user the User responsible for this change
	 * @param objectType the type of the object that was changed
	 * @param objectId the id value of the object that was changed
	 */
	protected ProjectEvent(User user, ProjectEventObjectType objectType, String objectId) {
		this();
		this.changes = new HashMap<String, FieldChange<?>>();
		this.user = user;
		this.objectId = objectId;
		this.objectType = objectType;
	}

	/**
	 * Construct a ProjectEventChangeset with the given properties.
	 * Other properties are the same as in the default constructor.
	 * 
	 * @param objectType the type of the object that was changed
	 * @param objectId the id value of the object that was changed
	 */
	protected ProjectEvent(ProjectEventObjectType objectType, String objectId) {
		this();
		this.changes = new HashMap<String, FieldChange<?>>();
		this.objectId = objectId;
		this.objectType = objectType;
	}

	/**
	 * Construct a RequirementEvent with default properties.
	 */
	public ProjectEvent() {
		id = -1;
	}

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

	/**
	 * @return the objectType
	 */
	public ProjectEventObjectType getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(ProjectEventObjectType objectType) {
		this.objectType = objectType;
	}

	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
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
	
	/**
	 * Given a builder, add anything to it that's necessary for Gson to interact with this class.
	 * 
	 * @param builder The builder to modify
	 */
	public static void addGsonDependencies(GsonBuilder builder) {
		builder.registerTypeAdapter(ProjectEvent.class, new ProjectEventDeserializer());
	}
	
	/**
	 * get the JSON string
	 *
	 * @return the JSON string
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, ProjectEvent.class);
		return json;
	}

	/**
	 * the toString() method
	 *
	 * @return the JSON string
	 */
	public String toString() {
		return toJSON();
	}
	
	/**
	 * @param json Json string to parse containing ProjectEvent
	 * @return The ProjectEvent given by json
	 */
	public static ProjectEvent fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
		addGsonDependencies(builder);
		return builder.create().fromJson(json, ProjectEvent.class);
	}
	
	/**
	 * @param json Json string to parse containing ProjectEvent array
	 * @return The ProjectEvent array given by json
	 */
	public static ProjectEvent[] fromJSONArray(String json) {
		GsonBuilder builder = new GsonBuilder();
		addGsonDependencies(builder);
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

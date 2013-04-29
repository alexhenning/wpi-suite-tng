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

import java.util.Date;

import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Implementations of this class represent some kind of event in a Requirement.
 * For example, the addition of a comment or the modification of fields.
 * @author TODO
 */
public abstract class RequirementEvent extends AbstractModel {
	
	/**
	 *
	 * the kinds of events
	 */
	public enum EventType {
		NOTE,
		CHANGESET
	};
	
	/** the date */
	protected Date date = new Date();
	/** the user */
	protected User user = new User("", "", "", -1);
	
	/**
	 * The type of event this is.  Subclasses must specify this in order to be deserialized properly.
	 */
	protected EventType type;
	
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
	 *get the type of event
	 *
	 * @return the events type
	 */
	public EventType getEventType() {
		return type;
	}
	
	/**
	 * Given a builder, add anything to it that's necessary for Gson to interact with this class.
	 * 
	 * @param builder The builder to modify
	 */
	public static void addGsonDependencies(GsonBuilder builder) {
		builder.registerTypeAdapter(RequirementEvent.class, new RequirementEventDeserializer());
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
	 * this model will only be created server side and then retrieved as part of a RequirementEvent in the future
	 * so I'm not sure if this is necessary
	 *
	 * @param o
	 * @return
	 */
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

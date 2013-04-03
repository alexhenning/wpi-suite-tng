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

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Persistent Model that holds information about a set of changes to a Requirement.
 * Every time a Requirement is changed by a user, a RequirementChangeset should be created
 * containing the changes and the user responsible for making them.
 */
public class RequirementChangeset extends RequirementEvent {

	private Map<String, FieldChange<?>> changes;
	
	/**
	 * Construct a RequirementChangeset with default properties.
	 */
	public RequirementChangeset() {
		type = EventType.CHANGESET;
		changes = new HashMap<String, FieldChange<?>>();
	}
	
	/**
	 * Construct a RequirementChangeset with the given properties.
	 * Other properties are the same as in the default constructor.
	 * 
	 * @param user the User responsible for this change
	 */
	public RequirementChangeset(User user) {
		this();
		this.user = user;
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

	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, RequirementChangeset.class);
		return json;
	}

}

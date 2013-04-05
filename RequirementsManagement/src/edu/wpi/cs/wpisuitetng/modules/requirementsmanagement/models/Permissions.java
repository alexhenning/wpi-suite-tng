/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    William Terry
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * @author William Terry
 *
 */
public class Permissions extends AbstractModel {
	
	// TODO: Do we want a transaction log for the Permissions data?
	
	private PermissionLevel permissionLevel;
	private String username;

	/**
	 * Constructs an empty user permission
	 */
	public Permissions(){
		this("", PermissionLevel.NONE);
	}
	
	/**
	 * Constructs a user permission using given parameters
	 * 
	 * @param username whose's permission is to be assigned
	 * @param permissionLevel
	 */
	public Permissions(String username, PermissionLevel permissionLevel) {
		this.username = username;
		this.permissionLevel = permissionLevel;
	}

	/**
	 * returns the permissions currently granted this user
	 *
	 * @param user
	 * @return PermissionLevel
	 */
	public PermissionLevel getPermissionLevel(){
		return permissionLevel;
	}
	
	/**
	 * Change the permissions level for this user
	 *
	 * @param user
	 * @param newLevel
	 */
	public void setPermissionLevel(PermissionLevel newLevel) {
		permissionLevel = newLevel;
	}
	
	/**
	 * Gets username from the model
	 *
	 * @return model's username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets model's username
	 *
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#delete()
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, Permissions.class);
		return json;
	}
	
	@Override
	public String toString() {
		return toJSON();
	}

	/**
	 * @param json Json string to parse containing Permissions
	 * @return The Iteration given by json
	 */
	public static Permissions fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
		return builder.create().fromJson(json, Permissions.class);
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		return (o.getClass() == this.getClass());
	}

	public static Permissions[] fromJSONArray(String body) {
		GsonBuilder builder = new GsonBuilder();
		return builder.create().fromJson(body, Permissions[].class);
	}

}

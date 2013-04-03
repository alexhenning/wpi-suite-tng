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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * Validates permission models
 * 
 * @author William Terry
 * @author vpatara
 *
 */
public class PermissionsValidator {
	private Data data;
	private Permissions lastExistingPermissions;
	
	/**
	 * Create a IterationValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public PermissionsValidator(Data data) {
		//TODO: "strict" mode for returning *all* issues, rather than ignoring and overwriting?
		this.data = data;
	}
	
	/**
	 * @return the data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Return the User with the given username if they already exist in the database.
	 * 
	 * @param username the username of the User
	 * @param issues list of errors to add to if user doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The User with the given username, or null if they don't exist
	 * @throws WPISuiteException 
	 */
	User getExistingUser(String username, List<ValidationIssue> issues, String fieldName) throws WPISuiteException {
		final List<Model> existingUsers = data.retrieve(User.class, "username", username);
		
		System.out.println("Edit user permission - size : " + existingUsers.size());
		
		if(existingUsers.size() > 0 && existingUsers.get(0) != null) {
			return (User) existingUsers.get(0);
		} else {
			issues.add(new ValidationIssue("User doesn't exist", fieldName));
			return null;
		}
	}
	
	/**
	 * Return all Users in the db.
	 * 
	 * @param issues list of errors to add to if user doesn't exist
	 * @return all Permissions in the project
	 * @throws WPISuiteException 
	 */
	User[] getAllExistingUsers(List<ValidationIssue> issues)
			throws WPISuiteException {
		User sample = new User(null, null, null, 0);
		User[] users = (data.retrieveAll(sample)).toArray(new User[0]);
		return users;
	}

	/**
	 * Return the Permissions for the given user if it already exists in the database.
	 *
	 * @param username whose permission to be retrieved
	 * @param project the project this permission belongs to
	 * @param issues list of errors to add to if permission doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return an existing permission, or null if not found
	 * @throws WPISuiteException 
	 */
	Permissions getExistingPermissions(String username, Project project,
			List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldPermissions = data.retrieve(Permissions.class, "username", username, project);
		if(oldPermissions.size() < 1 || oldPermissions.get(0) == null) {
			issues.add(new ValidationIssue("This project has no permissions files", username));
			return null;
		} else {
			return (Permissions) oldPermissions.get(0);
		}
	}

	/**
	 * Return all Permissions of the specified project.
	 * 
	 * @param project the project this Iteration belongs to
	 * @return all Permissions in the project
	 * @throws WPISuiteException 
	 */
	Permissions[] getAllExistingPermissions(Project project)
			throws WPISuiteException {
		Permissions sample = new Permissions();
		Permissions[] Permissions = (data.retrieveAll(sample, project)).toArray(new Permissions[0]);
		return Permissions;
	}
		
	/**
	 * Validate the given model such that the requesting user has authority to modify
	 * 
	 * @param session The session to validate against
	 * @param permissions The permission model to validate
	 * @param mode The mode to validate for
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException
	 */
	public List<ValidationIssue> validate(Session session,
			Permissions permissions, Mode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if (permissions == null) {
			issues.add(new ValidationIssue("Permissions cannot be null"));
			return issues;
		}

		System.out.println("validate this : " + permissions.getUsername());
		Permissions oldPermissions = null;
		
		//check if user is in the db
		if (mode == Mode.EDIT) {
			oldPermissions = getExistingPermissions("" + permissions.getUsername(),
					permissions.getProject(), issues, "username");
			
			if (oldPermissions != null) {
				// TODO: If these checks are inappropriate, delete them
				if(!oldPermissions.getUsername().equals(permissions.getUsername())) {
					issues.add(new ValidationIssue("Username cannot be changed"));
				}
				if(!oldPermissions.getProject().equals(permissions.getProject())) {
					issues.add(new ValidationIssue("Project cannot be changed"));
				}
				
			} else {
				System.out.println("Cannot find an existing user ("
						+ permissions.getUsername() + ")");
			}
		}
		
		lastExistingPermissions = oldPermissions;
		Permissions[] allPermissions = getAllExistingPermissions(session.getProject());

		if(mode == Mode.CREATE) {
			// Check if the user already has a permissions profile
			for(Permissions profile : allPermissions) {
				System.out.println("get user : " + profile.getUsername());
				
				// Check if the permission pair (user, project) already exists
				// Need to check between profile and session since permissions.getProject() is null
				if(profile.getUsername().equals(permissions.getUsername()) &&
						profile.getProject().equals(session.getProject())) {
					issues.add(new ValidationIssue("Unable to create a permission profile for ("+permissions.getUsername()+") since there is already a profile for this user"));
				}
			}
			
			// Check if the username exists in the database
			if(getExistingUser(permissions.getUsername(), issues, "username") == null) {
				issues.add(new ValidationIssue("Unable to create a permission profile for non-existent user ("+permissions.getUsername()+")"));
			}
		}
		
		System.out.println("issue count : " + issues.size());
		
		if (issues.size() > 0){
			System.out.println("permissions json: "+permissions.toJSON());
		}
		
		return issues;
	}

	/**
	 * @return The last existing Iteration the validator fetched if in edit mode
	 */
	public Permissions getLastExistingPermissions() {
		return lastExistingPermissions;
	}
}

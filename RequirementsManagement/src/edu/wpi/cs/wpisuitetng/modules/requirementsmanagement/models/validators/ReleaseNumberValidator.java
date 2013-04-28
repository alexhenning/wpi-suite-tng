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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

/**
 *
 * Validator for Release Numbers
 * @author TODO
 *
 * @version $Revision: 1.0 $
 */
public class ReleaseNumberValidator {
	/** the database */
	private Data data;
	/** the last existing release number */
	private ReleaseNumber lastExistingReleaseNumber;
	
	/**
	 * Create a ReleaseNumberValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public ReleaseNumberValidator(Data data) {
		//TODO: "strict" mode for returning *all* issues, rather than ignoring and overwriting?
		this.data = data;
	}
	
	/**
	
	 * @return the data */
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
	
	
	 * @return The User with the given username, or null if they don't exist * @throws WPISuiteException  */
	User getExistingUser(String username, List<ValidationIssue> issues, String fieldName) throws WPISuiteException {
		final List<Model> existingUsers = data.retrieve(User.class, "username", username);
		if(existingUsers.size() > 0 && existingUsers.get(0) != null) {
			return (User) existingUsers.get(0);
		} else {
			issues.add(new ValidationIssue("User doesn't exist", fieldName));
			return null;
		}
	}
	
	/**
	 * Return all ReleaseNumbers of the specified project.
	 * 
	 * @param project the project this ReleaseNumber belongs to
	 * @param issues list of errors to add to if release number doesn't exist
	
	
	 * @return all ReleaseNumbers in the project * @throws WPISuiteException  */
	ReleaseNumber[] getAllExistingReleaseNumbers(Project project, List<ValidationIssue> issues)
			throws WPISuiteException {
		ReleaseNumber sample = new ReleaseNumber();
		ReleaseNumber[] releaseNumbers = (data.retrieveAll(sample, project)).toArray(new ReleaseNumber[0]);
		return releaseNumbers;
	}
		
	/**
	 * Return the ReleaseNumber with the given id if it already exists in the database.
	 * 
	
	 * @param project the project this ReleaseNumber belongs to
	 * @param issues list of errors to add to if release number doesn't exist
	 * @param fieldName name of field to use in error if necessary
	
	
	 * @param id int
	 * @return The ReleaseNumber with the given id, or null if it doesn't exist * @throws WPISuiteException  */
	ReleaseNumber getExistingReleaseNumber(int id, Project project, List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldReleases = data.retrieve(ReleaseNumber.class, "id", id, project);
		if(oldReleases.size() < 1 || oldReleases.get(0) == null) {
			issues.add(new ValidationIssue("ReleaseNumber with id does not exist in project", fieldName));
			return null;
		} else {
			return (ReleaseNumber) oldReleases.get(0);
		}
	}
		
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	
	 * @param mode The mode to validate for
	 * @param releaseNumber
	
	
	 * @return A list of ValidationIssues (possibly empty)
	 *  @throws WPISuiteException  */
	public List<ValidationIssue> validate(Session session, ReleaseNumber releaseNumber, Mode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(releaseNumber == null) {
			issues.add(new ValidationIssue("releaseNumber cannot be null"));
			return issues;
		}
		
		ReleaseNumber[] allReleaseNumbers = getAllExistingReleaseNumbers(session.getProject(), issues);
		
		if(mode == Mode.CREATE) { // make sure there are none with the same release numbers when creating
			for(ReleaseNumber rn : allReleaseNumbers) {
				if(rn.getId() == releaseNumber.getId()) {
					issues.add(new ValidationIssue("Unable to create an ReleaseNumber with the provided id ("+releaseNumber.getId()+") since there is already an ReleaseNumber with that id"));
				}
			}
		}
		
		// regardless of mode, no release numbers should have the same number
		for(ReleaseNumber rn : allReleaseNumbers) {
			if(rn.getReleaseNumber().equals(releaseNumber.getReleaseNumber())) {
				issues.add(new ValidationIssue("Unable to create an ReleaseNumber with the provided releaseNumber ("+releaseNumber.getReleaseNumber()+") since there is already an ReleaseNumber with that releaseNumber"));
			}
		}

		ReleaseNumber oldReleaseNumber = null;
		if(mode == Mode.EDIT) {
			oldReleaseNumber = getExistingReleaseNumber(releaseNumber.getId(), session.getProject(), issues, "id");
		}
		lastExistingReleaseNumber = oldReleaseNumber;
		
		return issues;
	}

	/**
	
	 * @return The last existing ReleaseNumber the validator fetched if in edit mode */
	public ReleaseNumber getLastExistingReleaseNumber() {
		return lastExistingReleaseNumber;
	}
}
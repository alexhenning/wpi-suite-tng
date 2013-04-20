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
import java.util.Date;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 * Validates RequirementModel so that they fit in with the given Data implementation.
 * 
 * Note that Data could be something used client-side (e.g. a wrapper around a local cache of
 * Users so you can check assignee usernames as-you-type).
 * @author TODO
 */
public class RequirementModelValidator {

	/** the database */
	private Data data;
	/** last existing requirement model */
	private RequirementModel lastExistingRequirement;
	
	/**
	 * Create a RequirementModelValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public RequirementModelValidator(Data data) {
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
	 * @param issues list of errors to add to if ReleaseNumber doesn't exist
	 * @return all ReleaseNumbers in the project
	 * @throws WPISuiteException 
	 */
	ReleaseNumber[] getAllExistingReleaseNumbers(Project project, List<ValidationIssue> issues)
			throws WPISuiteException {
		ReleaseNumber sample = new ReleaseNumber();
		ReleaseNumber[] releaseNumbers = (data.retrieveAll(sample, project)).toArray(new ReleaseNumber[0]);
		return releaseNumbers;
	}
		
	/**
	 * Return the ReleaseNumber with the given id if it already exists in the database.
	 * 
	 * @param id the id of the ReleaseNumber
	 * @param project the project this ReleaseNumber belongs to
	 * @param issues list of errors to add to if ReleaseNumber doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The ReleaseNumber with the given id, or null if it doesn't exist
	 * @throws WPISuiteException 
	 */
	ReleaseNumber getExistingReleaseNumber(int id, Project project, List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldReleases = data.retrieve(ReleaseNumber.class, "id", id, project);
		if(oldReleases.size() < 1 || oldReleases.get(0) == null) {
			issues.add(new ValidationIssue("ReleaseNumber with "+fieldName+" ("+id+") does not exist in project", fieldName));
			return null;
		} else {
			return (ReleaseNumber) oldReleases.get(0);
		}
	}
	
	/**
	 * Return all Iterations of the specified project.
	 * 
	 * @param project the project this Iterations belongs to
	 * @param issues list of errors to add to if Iterations doesn't exist
	 * @return all Iterations in the project
	 * @throws WPISuiteException 
	 */
	Iteration[] getAllExistingIterations(Project project, List<ValidationIssue> issues)
			throws WPISuiteException {
		Iteration sample = new Iteration();
		Iteration[] iterations = (data.retrieveAll(sample, project)).toArray(new Iteration[0]);
		return iterations;
	}
		
	/**
	 * Return the Iteration with the given id if it already exists in the database.
	 * 
	 * @param id the id of the Iteration
	 * @param project the project this Iteration belongs to
	 * @param issues list of errors to add to if Iteration doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The Iteration with the given id, or null if it doesn't exist
	 * @throws WPISuiteException 
	 */
	Iteration getExistingIteration(int id, Project project, List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldIterations = data.retrieve(Iteration.class, "id", id, project);
		if(oldIterations.size() < 1 || oldIterations.get(0) == null) {
			issues.add(new ValidationIssue("Iteration with "+fieldName+" ("+id+") does not exist in project", fieldName));
			return null;
		} else {
			return (Iteration) oldIterations.get(0);
		}
	}
	
	/**
	 * Return the RequirementModel with the given id if it already exists in the database.
	 * 
	 * @param id the id of the Requirement
	 * @param project the project this requirement belongs to
	 * @param issues list of errors to add to if requirement doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The Requirement with the given id, or null if it doesn't exist
	 * @throws WPISuiteException 
	 */
	RequirementModel getExistingRequirement(int id, Project project, List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldRequirements = data.retrieve(RequirementModel.class, "id", id, project);
		if(oldRequirements.size() < 1 || oldRequirements.get(0) == null) {
			issues.add(new ValidationIssue("Requirement with id does not exist in project", fieldName));
			return null;
		} else {
			return (RequirementModel) oldRequirements.get(0);
		}
	}
	
	//TODO modifiy the defect validation code to work with the RequirementModel
	
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	 * @param requirement The requirement model to validate
	 * @param mode The mode to validate for
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	public List<ValidationIssue> validate(Session session, RequirementModel requirement, Mode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(requirement == null) {
			issues.add(new ValidationIssue("Requirement cannot be null"));
			return issues;
		}

		RequirementModel oldRequirement = null;
		if(mode == Mode.EDIT) {
			oldRequirement = getExistingRequirement(requirement.getId(), session.getProject(), issues, "id");
		}
		lastExistingRequirement = oldRequirement;
		
		if(mode == Mode.CREATE && requirement.getCreator().getIdNum() == -1) {
			System.out.println("Validator is doing things it should not have to do....setting the creating user for the requirement");
			requirement.setCreator(session.getUser());
			System.out.println("sesion user: "+session.getUser().toString());
			System.out.println("new creator user: "+requirement.getCreator().toString());
		}

		if(mode == Mode.CREATE) {
			requirement.setStatus(RequirementStatus.NEW); // new requirements should always have new status
		} else if(requirement.getStatus() == null) {
			issues.add(new ValidationIssue("Cannot be null", "status"));
		}

		// make sure Name and description size are within constraints
		if(requirement.getName() == null || requirement.getName().length() <= 0
				|| requirement.getName().length() > 100) {
			issues.add(new ValidationIssue("Required, name must be 1-100 characters", "name"));
		}
		if(requirement.getDescription() == null || requirement.getDescription().length() <= 0) {
			// empty descriptions are okay
			issues.add(new ValidationIssue("description Cannot be empty", "description"));
		}
		
		// make sure the creator and assignee exist and aren't duplicated
		if(mode == Mode.EDIT) {
			if(oldRequirement != null) {
				requirement.setCreator(oldRequirement.getCreator());
			}
		} else if(requirement.getCreator() == null) {
			issues.add(new ValidationIssue("Required creator", "creator"));
		} else {
			User creator = getExistingUser(requirement.getCreator().getUsername(), issues, "creator");
			if(creator != null) {
				if(!creator.getUsername().equals(session.getUsername())) {
					issues.add(new ValidationIssue("creator Must match currently logged in user", "creator"));
				} else {
					requirement.setCreator(creator);
				}
			}
		}
		
		if(requirement.getAssignees() != null) { // requirements can be missing an assignee
			List<User> assignees = requirement.getAssignees();
			ArrayList<User> assignees2 = new ArrayList<User>();
			for (User assigneeSrc : assignees) {
				User assignee = getExistingUser(assigneeSrc.getUsername(), issues, "assignee");
				if(assignee != null) {
					assignees2.add(assignee);
				}
			}
			requirement.setAssignees(assignees2);
		}
		
		//check if releaseNumber is null or has a matching ReleaseNumber in the project
		if(requirement.getReleaseNumber() != null) {
			ReleaseNumber[] existingReleaseNumbers = getAllExistingReleaseNumbers(session.getProject(), issues);
			boolean isExistingReleaseNumber = false;
			for(ReleaseNumber existingReleaseNumber : existingReleaseNumbers) {
				//TODO change to .equals()
				if(requirement.getReleaseNumber().getReleaseNumber().equals(existingReleaseNumber.getReleaseNumber())) {
					isExistingReleaseNumber = true;
				}
			}
			if (!isExistingReleaseNumber) {
				issues.add(new ValidationIssue("releaseNumber must match an existing ReleaseNumber or be null", "releaseNumber"));
			} else {
				requirement.setReleaseNumber(
						getExistingReleaseNumber(requirement.getReleaseNumber().getId(), session.getProject(), issues, "id"));
			}
		}
		
		//check if iteration is null or has a matching Iteration in the project
		if(requirement.getIteration() != null) {
			Iteration[] existingIterations = getAllExistingIterations(session.getProject(), issues);
			boolean isExistingIteration = false;
			for(Iteration existingIteration : existingIterations) {
				if(requirement.getIteration().getIterationNumber().equals(existingIteration.getIterationNumber())) {
					isExistingIteration = true;
				}
			}
			if (!isExistingIteration) {
				issues.add(new ValidationIssue("iteration must match an existing Iteration or be null", "iteration"));
			} else {
				requirement.setIteration(
						getExistingIteration(requirement.getIteration().getId(), session.getProject(), issues, "id"));
			}
		}
		
		// make sure we're not being spoofed with some weird date
		final Date now = new Date();
		if(oldRequirement != null) {
			requirement.setCreationDate(oldRequirement.getCreationDate());
		} else {
			requirement.setCreationDate(now);
		}
		requirement.setLastModifiedDate((Date)now.clone());
		
		if(oldRequirement != null) {
			requirement.setEvents(oldRequirement.getEvents());
		} else {
			// new requirements should never have any events
			requirement.setEvents(new ArrayList<RequirementEvent>());
		}
		
		// Check if the iteration is still in progress
		if(requirement.getIteration() != null) {
			if(now.after(requirement.getIteration().getEndDate()) && now != requirement.getIteration().getEndDate()) {
				issues.add(new ValidationIssue("iteration must not be over", "iteration"));
			}
		}
		
		ArrayList<String> subRequirements = new ArrayList<String>();
		for(String subRequirement : requirement.getSubRequirements()) {
			RequirementModel tmp = getExistingRequirement(new Integer(subRequirement), session.getProject(), issues, "id");
			if (tmp != null) {
				subRequirements.add(subRequirement);
			}
		}
		requirement.setSubRequirements(subRequirements);
		
		return issues;
	}

	/**
	 * @return The last existing Requirement the validator fetched if in edit mode
	 */
	public RequirementModel getLastExistingRequirement() {
		return lastExistingRequirement;
	}
	

}

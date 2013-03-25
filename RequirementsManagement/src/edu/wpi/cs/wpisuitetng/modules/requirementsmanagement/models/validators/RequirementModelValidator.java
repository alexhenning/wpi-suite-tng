/**
 * 
 */
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 * Validates RequirementModel so that they fit in with the given Data implementation.
 * 
 * Note that Data could be something used client-side (e.g. a wrapper around a local cache of
 * Users so you can check assignee usernames as-you-type).
 */
public class RequirementModelValidator {

	private Data data;
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
	 * Return the RequirementModel with the given id if it already exists in the database.
	 * 
	 * @param id the id of the Requirement
	 * @param project the project this requirement belongs to
	 * @param issues list of errors to add to if defect doesn't exist
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

		/* possible fields that might still need validation.
	private int releaseNumber;
	private RequirementPriority priority;
	private String estimate;
	private String actualEffort;
	private Date lastModifiedDate;
	private List<RequirementEvent> events;
	private List<RequirementModel> subRequirements;
		 */
		
		RequirementModel oldRequirement = null;
		if(mode == Mode.EDIT) {
			oldRequirement = getExistingRequirement(requirement.getId(), session.getProject(), issues, "id");
		}
		lastExistingRequirement = oldRequirement;
		
		if(mode == Mode.CREATE) {
			requirement.setStatus(RequirementStatus.NEW); // new requirements should always have new status
		} else if(requirement.getStatus() == null) {
			issues.add(new ValidationIssue("Cannot be null", "status"));
		}

		// make sure Name and description size are within constraints
		if(requirement.getName() == null || requirement.getName().length() > 0
				|| requirement.getName().length() < 100) {
			issues.add(new ValidationIssue("Required, must be 1-100 characters", "name"));
		}
		if(requirement.getDescription() == null) {
			// empty descriptions are okay
			requirement.setDescription("");
		} else if(requirement.getDescription().length() > 5000) {
			issues.add(new ValidationIssue("Cannot be greater than 5000 characters", "description"));
		}
		
		// make sure the creator and assignee exist and aren't duplicated
		if(mode == Mode.EDIT) {
			if(oldRequirement != null) {
				requirement.setCreator(oldRequirement.getCreator());
			}
		} else if(requirement.getCreator() == null) {
			issues.add(new ValidationIssue("Required", "creator"));
		} else {
			User creator = getExistingUser(requirement.getCreator().getUsername(), issues, "creator");
			if(creator != null) {
				if(!creator.getUsername().equals(session.getUsername())) {
					issues.add(new ValidationIssue("Must match currently logged in user", "creator"));
				} else {
					requirement.setCreator(creator);
				}
			}
		}
		
		if(requirement.getAssignee() != null) { // requirements can be missing an assignee
			User assignee = getExistingUser(requirement.getAssignee().getUsername(), issues, "assignee");
			if(assignee != null) {
				requirement.setAssignee(assignee);
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
			// new defects should never have any events
			requirement.setEvents(new ArrayList<RequirementEvent>());
		}
		
		return issues;
	}

	/**
	 * @return The last existing Requirement the validator fetched if in edit mode
	 */
	public RequirementModel getLastExistingRequirement() {
		return lastExistingRequirement;
	}
	

}

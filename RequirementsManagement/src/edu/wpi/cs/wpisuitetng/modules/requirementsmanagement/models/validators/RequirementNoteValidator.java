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
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * Responsible for validating a comment model.
 */
public class RequirementNoteValidator {

	private RequirementModel lastExistingRequirement;
	private RequirementModelValidator requirementValidator;
	
	/**
	 * Create a RequirementCommentValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public RequirementNoteValidator(Data data) {
		requirementValidator = new RequirementModelValidator(data);
	}
	
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	 * @param comment The RequirementComment model to validate
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	public List<ValidationIssue> validate(Session session, RequirementNote comment) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(comment == null) {
			issues.add(new ValidationIssue("RequirementComment cannot be null"));
			return issues;
		}
		
		if(comment.getUser() == null) {
			issues.add(new ValidationIssue("Required", "user"));
		} else {
			User author = requirementValidator.getExistingUser(comment.getUser().getUsername(), issues, "user");
			if(author != null) {
				if(!author.getUsername().equals(session.getUsername())) {
					issues.add(new ValidationIssue("Must match currently logged in user", "user"));
				} else {
					comment.setUser(author);
				}
			}
		}
		
		lastExistingRequirement = requirementValidator.getExistingRequirement(comment.getRequirementId(), session.getProject(),
				issues, "defectId");
		
		String body = comment.getBody();
		if(body == null || body.length() < 1 || body.length() > 10000) {
			issues.add(new ValidationIssue("Required, must be 1-10000 characters", "body"));
		}
		
		comment.setDate(new Date());
		
		return issues;
	}
	
	/**
	 * @return The last existing defect the validator fetched
	 */
	public RequirementModel getLastExistingRequirement() {
		return lastExistingRequirement;
	}
	
}
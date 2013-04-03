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
 * 
 * @author William Terry
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
	 * Return the Permissions for the given user if it already exists in the database.
	 * 
	 * @param id the id of the permission
	 * @param project the project this permission belongs to
	 * @param issues list of errors to add to if permission doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return
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
	 * Validate the given model such that the requesting user has authority to modify and .
	 * 
	 * @param session The session to validate against
	 * @param defect The defect model to validate
	 * @param mode The mode to validate for
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	public List<ValidationIssue> validate(Session session, Permissions permissions, Mode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(permissions == null) {
			issues.add(new ValidationIssue("Permissions cannot be null"));
			return issues;
		}

		System.out.println("validate this : " + permissions.getUsername());
		lastExistingPermissions = null;
		
		//check if user is in the db
		if(mode == Mode.EDIT){
			lastExistingPermissions = getExistingPermissions(permissions.getUsername(), permissions.getProject(), issues, "");
		}
		
		Permissions[] allPermissions = getAllExistingPermissions(session.getProject());

		//check if the user already has a permissions profile
		if(mode == Mode.CREATE) {
			for(Permissions profile : allPermissions) {
				System.out.println("get user : " + profile.getUsername());
				
				// Check if the permission pair (user, project) already exists
				if(profile.getUsername().equals(permissions.getUsername()) &&
						profile.getProject().equals(permissions.getProject())) {
					issues.add(new ValidationIssue("Unable to create a permissions profile for ("+permissions.getUsername()+") since there is already a profile for this user"));
					return issues;
				}
			}
		}
		
		if (issues.size() > 0){
			System.out.println("permissions json: "+permissions.toJSON());
		}
		System.out.println("issue count : " + issues.size());
		return issues;
	}

	/**
	 * @return The last existing Iteration the validator fetched if in edit mode
	 */
	public Permissions getLastExistingPermissions() {
		return lastExistingPermissions;
	}
}

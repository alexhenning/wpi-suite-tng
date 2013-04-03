/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;

/**
 * @author jpalnick
 *
 */
public class ProjectEventValidator {

	private Data data;
	private ProjectEvent lastExistingProjectEvent;
	
	/**
	 * Create a ProjectEventValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public ProjectEventValidator(Data data) {
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
	 * Return all ProjectEvents of the specified project.
	 * 
	 * @param project the project these ProjectEvents belong to
	 * @return all ProjectEvents in the project
	 * @throws WPISuiteException 
	 */
	ProjectEvent[] getAllExistingProjectEvents(Project project)
			throws WPISuiteException {
		ProjectEvent sample = new ProjectEvent();
		ProjectEvent[] projectEvents = (data.retrieveAll(sample, project)).toArray(new ProjectEvent[0]);
		return projectEvents;
	}
		
//	/**
//	 * Return the Iteration with the given id if it already exists in the database.
//	 * 
//	 * @param id the id of the Iteration
//	 * @param project the project this Iteration belongs to
//	 * @param issues list of errors to add to if defect doesn't exist
//	 * @param fieldName name of field to use in error if necessary
//	 * @return The Iteration with the given id, or null if it doesn't exist
//	 * @throws WPISuiteException 
//	 */
//	Iteration getExistingIteration(int id, Project project, List<ValidationIssue> issues, String fieldName)
//			throws WPISuiteException {
//		List<Model> oldIterations = data.retrieve(Iteration.class, "id", id, project);
//		if(oldIterations.size() < 1 || oldIterations.get(0) == null) {
//			issues.add(new ValidationIssue("Iteration with id does not exist in project", fieldName));
//			return null;
//		} else {
//			return (Iteration) oldIterations.get(0);
//		}
//	}
		
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	 * @param projectEvent The ProjectEvent model to validate
	 * @param mode The mode to validate for
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	public List<ValidationIssue> validate(Session session, ProjectEvent projectEvent, Mode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(projectEvent == null) {
			issues.add(new ValidationIssue("projectEvent cannot be null"));
			return issues;
		}
		
		//TODO finish this validator for ProjectEvent objects
		
		ProjectEvent[] allProjectEvents = getAllExistingProjectEvents(session.getProject());

		//check if the id is unique
		if(mode == Mode.CREATE) {
			for(ProjectEvent pe : allProjectEvents) {
				if(pe.getId() == projectEvent.getId()) {
					issues.add(new ValidationIssue("Unable to create a ProjectEvent with the provided id ("+projectEvent.getId()+") since there is already a ProjectEvent with that id"));
				}
			}
		}
//
//		Iteration oldIteration = null;
//		if(mode == Mode.EDIT) {
//			oldIteration = getExistingIteration(projectEvent.getId(), session.getProject(), issues, "id");
//		}
//		lastExistingProjectEvent = oldIteration;
//		
		// make sure the user is not duplicated
		User user = getExistingUser(projectEvent.getUser().getUsername(), issues, "creator");
		if(user != null) {
			if(!user.getUsername().equals(session.getUsername())) {
				issues.add(new ValidationIssue("Must match currently logged in user", "user"));
			} else {
				projectEvent.setUser(user);
			}
		}
//
////		// make sure start/endDates are not null
////		if(requirement.getName() == null || requirement.getName().length() > 150
////				|| requirement.getName().length() < 5) {
////			issues.add(new ValidationIssue("Required, must be 5-150 characters", "name"));
////		}
////		if(requirement.getDescription() == null) {
////			// empty descriptions are okay
////			requirement.setDescription("");
////		} else if(requirement.getDescription().length() > 5000) {
////			issues.add(new ValidationIssue("Cannot be greater than 5000 characters", "description"));
////		}
//
//		//TODO make sure this works. I think it should but I've been wrong before....
//		//check if dates overlap with other iterations
//		for (Iteration i : allIterations) {
//			if(i.getId() != projectEvent.getId()) {
//				if(projectEvent.getStartDate().after(i.getStartDate()) && projectEvent.getStartDate().before(i.getEndDate())) {
//					issues.add(new ValidationIssue("startDate overlaps with Iteration "+i.getIterationNumber(), "startDate"));
//				}
//				if(projectEvent.getEndDate().after(i.getStartDate()) && projectEvent.getEndDate().before(i.getEndDate())) {
//					issues.add(new ValidationIssue("endDate overlaps with Iteration "+i.getIterationNumber(), "endDate"));
//				}
//				if(i.getStartDate().after(projectEvent.getStartDate()) && projectEvent.getStartDate().before(i.getEndDate()) ||
//						i.getEndDate().after(projectEvent.getStartDate()) && projectEvent.getEndDate().before(i.getEndDate())) {
//					issues.add(new ValidationIssue("iteration overlaps with Iteration "+i.getIterationNumber()));
//				}
//			}
//		}

		if (issues.size() > 0){
			System.out.println("ProjectEvent json: "+projectEvent.toJSON());
		}
		return issues;
	}

//	/**
//	 * @return The last existing Iteration the validator fetched if in edit mode
//	 */
//	public Iteration getLastExistingIteration() {
//		return lastExistingIteration;
//	}
	
}

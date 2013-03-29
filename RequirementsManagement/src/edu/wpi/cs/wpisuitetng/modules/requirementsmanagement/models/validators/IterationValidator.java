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

/**
 * @author jpalnick
 *
 */
public class IterationValidator {

	private Data data;
	private Iteration lastExistingIteration;
	
	/**
	 * Create a IterationValidator
	 * 
	 * @param data The Data implementation to use
	 */
	public IterationValidator(Data data) {
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
	 * Return all Iterations of the specified project.
	 * 
	 * @param project the project this Iteration belongs to
	 * @param issues list of errors to add to if defect doesn't exist
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
	 * @param issues list of errors to add to if defect doesn't exist
	 * @param fieldName name of field to use in error if necessary
	 * @return The Iteration with the given id, or null if it doesn't exist
	 * @throws WPISuiteException 
	 */
	Iteration getExistingIteration(int id, Project project, List<ValidationIssue> issues, String fieldName)
			throws WPISuiteException {
		List<Model> oldIterations = data.retrieve(Iteration.class, "id", id, project);
		if(oldIterations.size() < 1 || oldIterations.get(0) == null) {
			issues.add(new ValidationIssue("Iteration with id does not exist in project", fieldName));
			return null;
		} else {
			return (Iteration) oldIterations.get(0);
		}
	}
		
	/**
	 * Validate the given model such that any nested models point to appropriate existing models
	 * from the Data given in the constructor.
	 * 
	 * @param session The session to validate against
	 * @param defect The defect model to validate
	 * @param mode The mode to validate for
	 * @return A list of ValidationIssues (possibly empty)
	 * @throws WPISuiteException 
	 */
	public List<ValidationIssue> validate(Session session, Iteration iteration, Mode mode) throws WPISuiteException {
		List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
		if(iteration == null) {
			issues.add(new ValidationIssue("Iteration cannot be null"));
			return issues;
		}
		
		Iteration[] allIterations = getAllExistingIterations(session.getProject(), issues);

		//check if the id is unique
		if(mode == Mode.CREATE) {
			for(Iteration it : allIterations) {
				if(it.getId() == iteration.getId()) {
					issues.add(new ValidationIssue("Unable to create an Iteration with the provided id ("+iteration.getId()+") since there is already an iteration with that id"));
	//				return issues;
				}
			}
		}

		//check if the iterationNumber is unique for the project
		if(mode == Mode.CREATE) {
			for(Iteration it : allIterations) {
				if(it.getIterationNumber() == iteration.getIterationNumber()) {
					issues.add(new ValidationIssue("Unable to create an Iteration with the provided iterationNumber ("+iteration.getIterationNumber()+") since there is already an iteration with that iterationNumber"));
//				return issues;
				}
			}
		}

		Iteration oldIteration = null;
		if(mode == Mode.EDIT) {
			oldIteration = getExistingIteration(iteration.getId(), session.getProject(), issues, "id");
		}
		lastExistingIteration = oldIteration;
		
//		// make sure Name and description size are within constraints
//		if(requirement.getName() == null || requirement.getName().length() > 150
//				|| requirement.getName().length() < 5) {
//			issues.add(new ValidationIssue("Required, must be 5-150 characters", "name"));
//		}
//		if(requirement.getDescription() == null) {
//			// empty descriptions are okay
//			requirement.setDescription("");
//		} else if(requirement.getDescription().length() > 5000) {
//			issues.add(new ValidationIssue("Cannot be greater than 5000 characters", "description"));
//		}
		
//		// make sure the creator and assignee exist and aren't duplicated
//		if(mode == Mode.EDIT) {
//			if(oldRequirement != null) {
//				requirement.setCreator(oldRequirement.getCreator());
//			}
//		} else if(requirement.getCreator() == null) {
//			issues.add(new ValidationIssue("Required", "creator"));
//		} else {
//			User creator = getExistingUser(requirement.getCreator().getUsername(), issues, "creator");
//			if(creator != null) {
//				if(!creator.getUsername().equals(session.getUsername())) {
//					issues.add(new ValidationIssue("Must match currently logged in user", "creator"));
//				} else {
//					requirement.setCreator(creator);
//				}
//			}
//		}
//		
//		if(requirement.getAssignee() != null) { // requirements can be missing an assignee
//			User assignee = getExistingUser(requirement.getAssignee().getUsername(), issues, "assignee");
//			if(assignee != null) {
//				requirement.setAssignee(assignee);
//			}
//		}
		
//		// make sure start/endDates are not null
//		if(requirement.getName() == null || requirement.getName().length() > 150
//				|| requirement.getName().length() < 5) {
//			issues.add(new ValidationIssue("Required, must be 5-150 characters", "name"));
//		}
//		if(requirement.getDescription() == null) {
//			// empty descriptions are okay
//			requirement.setDescription("");
//		} else if(requirement.getDescription().length() > 5000) {
//			issues.add(new ValidationIssue("Cannot be greater than 5000 characters", "description"));
//		}

		
//		// make sure we're not being spoofed with some weird dates
//		final Date now = new Date();
//		if(oldIteration != null) {
//			iteration.setStartDate(oldIteration.getStartDate());
//			iteration.setEndDate(oldIteration.getEndDate());
//		} else {
//			iteration.setStartDate(now);
//			iteration.setEndDate(now);
//		}

		// make sure startDate is before the endDate
		if(iteration.getStartDate() == null) {
			issues.add(new ValidationIssue("Required, must not be null", "startDate"));
		}
		if(iteration.getEndDate() == null) {
			issues.add(new ValidationIssue("Required, must not be null", "endDate"));
		}
		if((iteration.getStartDate() == null || iteration.getEndDate() == null) || 
				iteration.getStartDate().after(iteration.getEndDate())) {
//			System.out.println("start: "+iteration.getStartDate().getTime());
//			System.out.println("end: "+iteration.getEndDate().getTime());
			
//			issues.add(new ValidationIssue("startDate must be before endDate", "startDate"));
			issues.add(new ValidationIssue("startDate must be before endDate", "endDate"));
		}
		
		//TODO make sure this works. I think it should but I've been wrong before....
		//check if dates overlap with other iterations
		for (Iteration i : allIterations) {
			if(i.getId() != iteration.getId()) {
				if(iteration.getStartDate().after(i.getStartDate()) && iteration.getStartDate().before(i.getEndDate())) {
					issues.add(new ValidationIssue("startDate overlaps with Iteration "+i.getIterationNumber(), "startDate"));
				}
				if(iteration.getEndDate().after(i.getStartDate()) && iteration.getEndDate().before(i.getEndDate())) {
					issues.add(new ValidationIssue("endDate overlaps with Iteration "+i.getIterationNumber(), "endDate"));
				}
				if(i.getStartDate().after(iteration.getStartDate()) && iteration.getStartDate().before(i.getEndDate()) ||
						i.getEndDate().after(iteration.getStartDate()) && iteration.getEndDate().before(i.getEndDate())) {
					issues.add(new ValidationIssue("iteration overlaps with Iteration "+i.getIterationNumber()));
				}
			}
		}

//		// make sure we're not being spoofed with some weird date
//		final Date now = new Date();
//		if(oldRequirement != null) {
//			requirement.setCreationDate(oldRequirement.getCreationDate());
//		} else {
//			requirement.setCreationDate(now);
//		}
//		requirement.setLastModifiedDate((Date)now.clone());
		
//		if(oldRequirement != null) {
//			requirement.setEvents(oldRequirement.getEvents());
//		} else {
//			// new defects should never have any events
//			requirement.setEvents(new ArrayList<RequirementEvent>());
//		}
		
		if (issues.size() > 0){
			System.out.println("iteration json: "+iteration.toJSON());
		}
		return issues;
	}

	/**
	 * @return The last existing Iteration the validator fetched if in edit mode
	 */
	public Iteration getLastExistingIteration() {
		return lastExistingIteration;
	}
	
}

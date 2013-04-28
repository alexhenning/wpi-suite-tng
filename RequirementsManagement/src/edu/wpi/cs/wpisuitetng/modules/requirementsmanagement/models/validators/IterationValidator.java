/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jacob Palnick
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;

import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * validator for iterations
 * @author jpalnick
 *
 * @version $Revision: 1.0 $
 */
public class IterationValidator {

	/** the database */
	private Data data;
	/** the last existing iteration model */
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
	 * Return all Iterations of the specified project.
	 * 
	 * @param project the project this Iteration belongs to
	 * @param issues list of errors to add to if iteration doesn't exist
	
	
	 * @return all Iterations in the project * @throws WPISuiteException  */
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
	 * @param issues list of errors to add to if iteration doesn't exist
	 * @param fieldName name of field to use in error if necessary
	
	
	 * @return The Iteration with the given id, or null if it doesn't exist * @throws WPISuiteException  */
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
	 * @param iteration The iteration model to validate
	 * @param mode The mode to validate for
	
	
	 * @return A list of ValidationIssues (possibly empty)
	 *  @throws WPISuiteException  */
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
				}
			}
		}

		//check if the iterationNumber is unique for the project
		if(mode == Mode.CREATE) {
			for(Iteration it : allIterations) {
				if(it.getIterationNumber().equals(iteration.getIterationNumber())) {
					issues.add(new ValidationIssue("Unable to create an Iteration with the provided iterationNumber ("+iteration.getIterationNumber()+") since there is already an iteration with that iterationNumber"));
				}
			}
		}

		Iteration oldIteration = null;
		if(mode == Mode.EDIT) {
			oldIteration = getExistingIteration(iteration.getId(), session.getProject(), issues, "id");
		}
		lastExistingIteration = oldIteration;
		
		// make sure Name and description size are within constraints
		if(iteration.getIterationNumber() == null || iteration.getIterationNumber().length() == 0) {
			issues.add(new ValidationIssue("Required, must not be blank", "name"));
		}
		
		// make sure startDate is before the endDate
		if(iteration.getStartDate() == null) {
			issues.add(new ValidationIssue("Required, must not be null", "startDate"));
		}
		if(iteration.getEndDate() == null) {
			issues.add(new ValidationIssue("Required, must not be null", "endDate"));
		}
		if(iteration.getStartDate() != null && iteration.getEndDate() != null && 
				iteration.getStartDate().after(iteration.getEndDate())) {
			issues.add(new ValidationIssue("startDate must be before endDate", "endDate"));
		}
		
		//TODO make sure this works. I think it should but I've been wrong before....
		//check if dates overlap with other iterations
		if(iteration.getStartDate() != null && iteration.getEndDate() != null){
			Date iterationStart = iteration.getStartDate();
			Calendar noTime = Calendar.getInstance();
			noTime.setTime(iterationStart);
			noTime.set(Calendar.HOUR_OF_DAY, 0);  
			noTime.set(Calendar.MINUTE, 0);  
			noTime.set(Calendar.SECOND, 0);  
			noTime.set(Calendar.MILLISECOND, 0);
			iterationStart = noTime.getTime();
			Date iterationEnd = iteration.getEndDate();
			noTime.setTime(iterationEnd);
			noTime.set(Calendar.HOUR_OF_DAY, 0);  
			noTime.set(Calendar.MINUTE, 0);  
			noTime.set(Calendar.SECOND, 0);  
			noTime.set(Calendar.MILLISECOND, 0);
			iterationEnd = noTime.getTime();
			for (Iteration i : allIterations) {
				Date iStart = i.getStartDate();
				noTime.setTime(iStart);
				noTime.set(Calendar.HOUR_OF_DAY, 0);  
				noTime.set(Calendar.MINUTE, 0);  
				noTime.set(Calendar.SECOND, 0);  
				noTime.set(Calendar.MILLISECOND, 0);
				iStart = noTime.getTime();
				Date iEnd = i.getEndDate();
				noTime.setTime(iEnd);
				noTime.set(Calendar.HOUR_OF_DAY, 0);  
				noTime.set(Calendar.MINUTE, 0);  
				noTime.set(Calendar.SECOND, 0);  
				noTime.set(Calendar.MILLISECOND, 0);
				iEnd = noTime.getTime();
				if(i != null && i.getId() != iteration.getId()) {
					if(iterationStart.after(iStart) && iterationStart.before(iEnd)) {
						issues.add(new ValidationIssue("startDate overlaps with Iteration "+i.getIterationNumber(), "startDate"));
					}
					if(iterationEnd.after(iStart) && iterationEnd.before(iEnd)) {
						issues.add(new ValidationIssue("endDate overlaps with Iteration "+i.getIterationNumber(), "endDate"));
					}
					if((iStart.after(iterationStart) && iStart.before(iterationEnd)) ||
							(iEnd.after(iterationStart) && iEnd.before(iterationEnd)) ||
							(iStart.equals(iterationStart) || iEnd.equals(iterationEnd))) {
						issues.add(new ValidationIssue("iteration overlaps with Iteration "+i.getIterationNumber()));
					}
				}
			}
		}
		
		if (issues.size() > 0){
			System.out.println("iteration json: "+iteration.toJSON());
		}
		return issues;
	}
	/**
	 * Method checkForOverlap.
	 * @param iteration1 Iteration
	 * @param iteration2 Iteration
	 * @param issues List<ValidationIssue>
	 */
	public void checkForOverlap(Iteration iteration1, Iteration iteration2, List<ValidationIssue> issues) {
		if(iteration1 != null && iteration1.getId() != iteration2.getId()) {
			Calendar it1Start = Calendar.getInstance();
			Calendar it1End = Calendar.getInstance();
			Calendar it2Start = Calendar.getInstance();
			Calendar it2End = Calendar.getInstance();
			
			it1Start.setTime(iteration1.getStartDate());
			it1End.setTime(iteration1.getEndDate());
			it2Start.setTime(iteration2.getStartDate());
			it2End.setTime(iteration2.getEndDate());

			it1Start.set(Calendar.HOUR_OF_DAY, 0);
			it1Start.set(Calendar.MINUTE, 0);
			it1Start.set(Calendar.SECOND, 0);

			it1End.set(Calendar.HOUR_OF_DAY, 0);
			it1End.set(Calendar.MINUTE, 0);
			it1End.set(Calendar.SECOND, 0);

			it2Start.set(Calendar.HOUR_OF_DAY, 0);
			it2Start.set(Calendar.MINUTE, 0);
			it2Start.set(Calendar.SECOND, 0);

			it2End.set(Calendar.HOUR_OF_DAY, 0);
			it2End.set(Calendar.MINUTE, 0);
			it2End.set(Calendar.SECOND, 0);
			
			if(it2Start.compareTo(it1Start) >= 0 && it2Start.compareTo(it1End) <= 0) {
				issues.add(new ValidationIssue("startDate overlaps with Iteration "+iteration1.getIterationNumber(), "startDate"));
			}
			
			
			if(iteration2.getStartDate().after(iteration1.getStartDate()) && iteration2.getStartDate().before(iteration1.getEndDate())) {
				issues.add(new ValidationIssue("startDate overlaps with Iteration "+iteration1.getIterationNumber(), "startDate"));
			}
			if(iteration2.getEndDate().after(iteration1.getStartDate()) && iteration2.getEndDate().before(iteration1.getEndDate())) {
				issues.add(new ValidationIssue("endDate overlaps with Iteration "+iteration1.getIterationNumber(), "endDate"));
			}
			if((iteration1.getStartDate().after(iteration2.getStartDate()) && iteration1.getStartDate().before(iteration2.getEndDate())) ||
					(iteration1.getEndDate().after(iteration2.getStartDate()) && iteration1.getEndDate().before(iteration2.getEndDate())) ||
					(iteration1.getStartDate().equals(iteration2.getStartDate()) || iteration1.getEndDate().equals(iteration2.getEndDate()))) {
				
				issues.add(new ValidationIssue("iteration overlaps with Iteration "+iteration1.getIterationNumber()));
			}
		}
	}

	/**
	
	 * @return The last existing Iteration the validator fetched if in edit mode */
	public Iteration getLastExistingIteration() {
		return lastExistingIteration;
	}
	
}

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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.RequirementsCallback;

/**
 * The iteration model
 * @author jpalnick
 *
 */
public class Iteration extends AbstractModel {
	
	//TODO Do we want a transaction log for the iteration data?
	
	/** The id */
	private int id;
	/** the start date */
	private Date startDate;
	/** the end date */
	private Date endDate;
	/** the name */
	private String iterationNumber;
	/** the sum of the estimates for all requirements in the iteration */
	private int estimate;

	/**
	 * Constructor
	 * @param id the id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @param iterationNumber the iteration's name
	 * @param project the project
	 */
	public Iteration(int id, Date startDate, Date endDate, String iterationNumber,
			Project project) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.endDate = setTimeToEndOfDay(endDate); // Set the time on the end date to be 11:59:59 P.M.
		this.iterationNumber = iterationNumber;
		this.setProject(project);
	}

	/**
	 * Constructor
	 */
	public Iteration() {
		super();
		id = -1;
		startDate = new Date();
		endDate = new Date(); //TODO should probably make this set to the next day...
		iterationNumber = "-1";
		
		this.setProject(new Project("", "-1"));
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Sets the time in the date to be 11:59:59 P.M. (or the end of the day)
	 * this is needed so that an a requirement can be added to an iteration on the day the iteration ends
	 * since the time is defaulted to 12:00:00 A.M. (or the beginning of the day)
	 *
	 * @param date Date to have its time set
	 */
	public Date setTimeToEndOfDay(Date date) {
		if (date != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			date = cal.getTime();
		}
		return date;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate; // The time of the start date defaults to 12:00:00 A.M.
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = setTimeToEndOfDay(endDate); // Set the time of the end date to be 11:59:59 P.M.
	}

	/**
	 * @return the iterationNumber
	 */
	public String getIterationNumber() {
		return iterationNumber;
	}

	/**
	 * @param iterationNumber the iterationNumber to set
	 */
	public void setIterationNumber(String iterationNumber) {
		this.iterationNumber = iterationNumber;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#save()
	 *
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#delete()
	 *
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 *
	 * @return
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, Iteration.class);
		return json;
	}
	
	/**
	 * The toSting method, returns the iteration's name, or iterationNumber
	 *
	 * @return the iteration's name
	 */
	@Override
	public String toString() {
		return getIterationNumber();
	}

	/**
	 * @param json Json string to parse containing Iteration
	 * @return The Iteration given by json
	 */
	public static Iteration fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
		return builder.create().fromJson(json, Iteration.class);
	}
	
	/**
	 * @param json Json string to parse containing Iteration array
	 * @return The Iteration array given by json
	 */
	public static Iteration[] fromJSONArray(String json) {
		GsonBuilder builder = new GsonBuilder();
		return builder.create().fromJson(json, Iteration[].class);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 *
	 * @param o
	 * @return
	 */
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	//TODO add new equals method

	/**
	 *
	 * Description goes here
	 * @author James
	 * @author Visit
	 *
	 */
	public class IterationEstimate implements RequirementsCallback {
		
		/** The Iteration that an estimate is needed for */
		private Iteration iteration;

		/**
		 * Constructor
		 * @param iteration the iteration
		 */
		public IterationEstimate(Iteration iteration) {
			this.iteration = iteration;
		}

		/**
		 * Get the total estimate for all of the requirements in an iteration
		 * TODO: when sub-children are implemented for requirements make sure that this still works
		 *
		 * @param reqs list of requirements
		 */
		@Override
		public void callback(List<RequirementModel> reqs) {
			estimate = 0;
			if (iteration != null) {
				for(RequirementModel req : reqs) {
					// If the iteration is the same as the requirment's iteration
					if (req.getIteration() != null &&
							req.getIteration().getIterationNumber().equals(iteration.getIterationNumber())) {
						// Add the requirment's estimate to the iteration's estimate
						estimate += req.getEstimate();
					}
				}
			}
		}
		
	}
	
}

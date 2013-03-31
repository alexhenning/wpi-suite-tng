/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.util.List;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.AbstractEditCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Walks through a list of requirements and applies a generic function to them
 * @author Tim Calvert
 *
 */
public class WorkMultipleRequirements {
	
	/** List of requirements to edit */
	private List<RequirementModel> requirements;
	// no need for a default callback, because there is no default action
	
	/**
	 * Default constructor
	 */
	public WorkMultipleRequirements() {
		
	}

	/**
	 * Useful constructor
	 */
	public WorkMultipleRequirements(List<RequirementModel> requirements) {
		this.requirements = requirements;
	}
	
	/**
	 * For every requirement to be worked with, call walk
	 *
	 * @param callback Callback that actually performs the needed actions
	 */
	public void workRequirements(AbstractEditCallback callback) {
		if(requirements != null) {
			for(RequirementModel req : requirements) {
				walk(req, callback);
			}
		}
	}
	
	/**
	 * Walks through a requirement and all of its children and performs
	 * an undefined action on it
	 *
	 * @param req The requirement to work with
	 * @param callback Callback that performs the actions
	 * @return Whatever value the call function is set up to return
	 */
	private Object walk(final RequirementModel req, AbstractEditCallback callback) {
		// do what needs to be done and then recurse
		if(req.getSubRequirements().size() >= 1 || req.getSubRequirements() != null) {
			for(RequirementModel r : req.getSubRequirements()) {
				walk(r, callback);
			}
		}
		return callback.call(req);
	}
	
	/**
	 * Getter for requirements
	 *
	 * @return List of requirements that this class will work with
	 */
	public List<RequirementModel> getRequirements() {
		return requirements;
	}

	/**
	 * Setter for requirements
	 *
	 * @param requirements List of requirements to work with
	 */
	public void setRequirements(List<RequirementModel> requirements) {
		this.requirements = requirements;
	}
}

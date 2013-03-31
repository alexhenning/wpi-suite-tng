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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.AbstractWorkCallback;
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
	 * Works through all requirements in a list and all their children
	 * and perform an action (defined by callback.call) on each
	 *
	 * @param reqs list of requirements to work with
	 * @param callback callback
	 * @return an Object as returned by call
	 */
	public Object workRequirements(List<RequirementModel> reqs, AbstractWorkCallback callback) {
		if(reqs != null && reqs.size() >= 1) {
			RequirementModel req = reqs.get(0);
			if(req.getSubRequirements().size() >=1 && req.getSubRequirements() != null) {
				workRequirements(req.getSubRequirements(), callback);
			}
			workRequirements(reqs.subList(1, reqs.size()), callback);
			return callback.call(req);	
		}
		return null;
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

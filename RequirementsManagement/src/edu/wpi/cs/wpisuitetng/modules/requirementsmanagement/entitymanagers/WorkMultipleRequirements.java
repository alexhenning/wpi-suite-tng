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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.callbacks.AbstractWorkCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Walks through a list of requirements and applies a generic function to them
 * @author Tim Calvert
 *
 */
public class WorkMultipleRequirements {
	
	// no need for a default callback, because there is no default action
	
	/**
	 * Default constructor
	 */
	public WorkMultipleRequirements() {
		
	}

	
	/**
	 * Works through all requirements in a list and all their children
	 * and perform an action (defined by callback.call) on each
	 *
	 * @param reqs list of requirements to work with
	 * @param callback callback
	 * @return an Integer as returned by call
	 */
	public Integer workRequirements(List<RequirementModel> reqs, AbstractWorkCallback callback) {
		if(reqs != null && reqs.size() >= 1) {
			//Integer[] returnValues = new Integer[1];
			workRequirements(reqs.subList(1, reqs.size()), callback);
			RequirementModel req = reqs.get(0);
			if(req.getSubRequirements().size() >=1 && req.getSubRequirements() != null) {
				return workRequirements(req.getSubRequirements(), callback) + callback.call(req);
			} else {
				//callback.setParameters(returnValues);
				return callback.call(req);	
			}
		}
		return 0;
	}

}

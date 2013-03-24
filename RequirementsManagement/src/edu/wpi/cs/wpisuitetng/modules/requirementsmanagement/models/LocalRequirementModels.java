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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * Singleton model for requirements to be stored and accessed locally
 * @author Tim
 *
 */
public class LocalRequirementModels {
	/** Singleton instance of the model */
	public static LocalRequirementModels instance = null;
	public List<RequirementModel> localRequirements;
	
	protected LocalRequirementModels() {
		localRequirements = new LinkedList<RequirementModel>();
	}
	
	public static void initModel() {
		instance = new LocalRequirementModels();
	}
	
	public void addRequirement(RequirementModel req) {
		localRequirements.add(req);
	}
	
	public void addRequirements(List<RequirementModel> reqs) {
		localRequirements.addAll(reqs);
	}
	
	public List<RequirementModel> getAllRequirements() {
		return localRequirements;
	}
	
	public RequirementModel getRequirementById(int Id) {
		for(RequirementModel req : localRequirements) {
			if(req.getId() == Id) {
				return req;
			}
		}
		
		return null;
	}
	
	public void updateRequirement(RequirementModel req) {
		for(RequirementModel r : localRequirements) {
			if(r.getId() == req.getId()) {
				// TODO copy over values using validator
			}
		}
	}
	
	public void deleteRequirement(RequirementModel req) {
		localRequirements.remove(req);
	}
}

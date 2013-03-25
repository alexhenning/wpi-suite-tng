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
	/** list containing all of the requirements currently known about */
	public List<RequirementModel> localRequirements;
	
	/**
	 * Default constructor, which is called from initModel
	 */
	protected LocalRequirementModels() {
		localRequirements = new LinkedList<RequirementModel>();
	}
	
	/**
	 * Used to instantiate a new instance of the class to be used by all callers
	 *
	 */
	public static void initModel() {
		instance = new LocalRequirementModels();
	}
	
	/**
	 * Adds a single requirement to the list, provided the list does not
	 * already contain the same requirement
	 *
	 * @param req Requriement to add to the model
	 */
	public void addRequirement(RequirementModel req) {
		if(!localRequirements.contains(req)) {
			localRequirements.add(req);
		}
	}
	
	/**
	 * Adds a list of requirements to the model
	 * Not really sure if there is actually an application for this
	 *
	 * @param reqs A list of all requirements to add
	 */
	public void addRequirements(List<RequirementModel> reqs) {
		for(RequirementModel req : reqs) {
			addRequirement(req);
		}
	}
	
	/**
	 * Resets the local model to the list supplied as a parameter
	 * Used when retrieving all requirements from the server
	 *
	 * @param reqs A list of all requirements to use as the new list
	 */
	public void resetRequirements(List<RequirementModel> reqs) {
		localRequirements.clear();
		localRequirements.addAll(reqs);
	}
	
	/**
	 * Returns all requirements currently known about by the system
	 *
	 * @return A list of all RequirementModels
	 */
	public List<RequirementModel> getAllRequirements() {
		return localRequirements;
	}
	
	/**
	 * Looks up a requirement by its Id
	 *
	 * @param Id Id of requirement to look up
	 * @return RequirementModel with matching Id, or null if none was found
	 */
	public RequirementModel getRequirementById(int Id) {
		for(RequirementModel req : localRequirements) {
			if(req.getId() == Id) {
				return req;
			}
		}
		
		return null;
	}
	
	/**
	 * Updates the value of a requirement with the values of the requirement
	 * passed in. Requirement is identified by Id.
	 *
	 * @param req Requirement with updated values to add
	 */
	public void updateRequirement(RequirementModel req) {
		/*for(RequirementModel r : localRequirements) {
			if(r.getId() == req.getId()) {
				// TODO copy over values using validator
			}
		}*/
	}
	
	/**
	 * Deletes a requirement from the local model. Requirement is identified
	 * by being equal to the requirement passed in
	 *
	 * @param req Requirement identical to the requirement to be deleted
	 */
	public void deleteRequirement(RequirementModel req) {
		localRequirements.remove(req);
	}
	
	/**
	 * Deletes a requirement from the local model. Requirement is identified
	 * by Id
	 *
	 * @param Id Id of requirement to delete
	 */
	public void deleteRequirementById(int Id) {
		for(RequirementModel req : localRequirements) {
			if(req.getId() == Id) {
				localRequirements.remove(req);
				break;  // no need to continue, Ids are unique
			}
		}
	}

}

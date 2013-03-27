package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * An interface for callbacks that return all of the requirements.
 * 
 * @author alex
 */
public interface RequirementsCallback {

	/**
	 * Implements an action to be performed on the requirement being passed back.
	 * 
	 * @param reqs
	 *            The requirements being returned
	 */
	public void callback(List<RequirementModel> reqs);
}

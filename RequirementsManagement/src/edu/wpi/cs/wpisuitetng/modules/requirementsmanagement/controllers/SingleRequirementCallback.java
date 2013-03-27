package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * An interface for callbacks that return a single requirement.
 * 
 * @author alex
 */
public interface SingleRequirementCallback {

	/**
	 * Implements an action to be performed on the requirement being passed back.
	 * 
	 * @param req
	 *            The requirement being returned
	 */
	public void callback(RequirementModel req);
}

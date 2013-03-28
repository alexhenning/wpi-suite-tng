package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * An interface for callbacks that return a single requirement.
 * 
 * @author alex
 * @author Jacob Palnick
 */
public interface SingleIterationCallback {

	/**
	 * Implements an action to be performed on the iteration being passed back.
	 * 
	 * @param iteration
	 *            The iteration being returned
	 */
	public void callback(Iteration iteration);
}

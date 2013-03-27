package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

public interface IterationCallback {

	/**
	 * Implements an action to be performed on the iteration being passed back.
	 * 
	 * @param reqs
	 *            The iterations being returned
	 */
	public void callback(List<Iteration> iterationss);
}

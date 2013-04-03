package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;

public interface ProjectEventsCallback {

	/**
	 * Implements an action to be performed on the projectEvents being passed back.
	 * 
	 * @param reqs
	 *            The projectEvents being returned
	 */
	public void callback(List<ProjectEvent> projectEvents);
}

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

public interface ReleaseNumberCallback {

	/**
	 * Implements an action to be performed on the iteration being passed back.
	 * 
	 * @param reqs
	 *            The iterations being returned
	 */
	public void callback(List<ReleaseNumber> releaseNumbers);
}

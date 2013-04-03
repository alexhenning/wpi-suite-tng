package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;


/**
 * An interface for callbacks that return a single permissions profile.
 * 
 * @author William Terry
 */
public interface SinglePermissionCallback {
	/**
	 * Implements an action to be performed on the permissions profile being passed back.
	 * 
	 * @param req
	 *            The requirement being returned
	 */
	public void callback(Permissions profile);
}

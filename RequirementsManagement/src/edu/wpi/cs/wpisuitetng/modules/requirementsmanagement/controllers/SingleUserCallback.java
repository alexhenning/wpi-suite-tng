package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;


/**
 * An interface for callbacks that return a single user.
 * 
 * @author William Terry
 */
public interface SingleUserCallback {
	/**
	 * Implements an action to be performed on the permissions profile being passed back.
	 * 
	 * @param req
	 *            The requirement being returned
	 */
	public void callback(User user);
}

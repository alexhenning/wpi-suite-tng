/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Willam Terry
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

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

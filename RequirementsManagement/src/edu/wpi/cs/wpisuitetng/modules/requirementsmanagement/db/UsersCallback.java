/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    William Terry
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * An interface for callbacks that return all of the users.
 * 
 * @author William Terry
 */
public interface UsersCallback {

	/**
	 * Implements an action to be performed on the requirement being passed back.
	 * 
	 * @param reqs
	 *            The requirements being returned
	 */
	public void callback(List<User> users);
}

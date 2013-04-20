/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alex Henning
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;


/**
 * An interface for callbacks that return all of the requirements.
 * 
 * @author alex
 */
public interface CanCloseRequirementCallback {

	/**
	 * Implements an action to be performed on the requirement being passed back.
	 * 
	 * @param result
	 *            boolean of if it can be closed
	 */
	void callback(boolean result);
}

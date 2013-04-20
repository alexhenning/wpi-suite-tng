/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   	//TODO
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

/**
 *
 * Interface that all iteration callbacks must inherit in order to use the functions in DB
 * @author TODO
 *
 */
public interface IterationCallback {

	/**
	 * Implements an action to be performed on the iteration being passed back.
	 * 
	 * @param reqs
	 *            The iterations being returned
	 */
	void callback(List<Iteration> iterations);
}

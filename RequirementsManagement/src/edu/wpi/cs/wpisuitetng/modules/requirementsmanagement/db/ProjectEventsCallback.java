/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;

/**
 *
 * Interface that all project event callbacks must inherit in order to use the functions in DB
 * @author TODO
 *
 */
public interface ProjectEventsCallback {

	/**
	 * Implements an action to be performed on the projectEvents being passed back.
	 * 
	 * @param reqs
	 *            The projectEvents being returned
	 */
	void callback(List<ProjectEvent> projectEvents);
}

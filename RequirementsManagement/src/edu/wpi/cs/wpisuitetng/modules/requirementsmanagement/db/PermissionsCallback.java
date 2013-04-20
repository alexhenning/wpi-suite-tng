/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * An interface for callbacks that return permissions profiles.
 *
 * @author vpatara
 */
public interface PermissionsCallback {
	/**
	 * Implements an action to be performed on the permissions profile being passed back.
	 *
	 * @param req The requirement being returned
	 */
	void callback(List<Permissions> profiles);
}

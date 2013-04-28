/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tim Calvert
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

/**
 *
 * Interface that release number callbacks can inherit from
 * @author Tim
 *
 * @version $Revision: 1.0 $
 */
public interface SingleReleaseNumberCallback {

	/**
	 * Implements an action to be called on the release number being passed back
	 *
	 * @param releaseNumber Release numbers returned from the database
	 */
	void callback(ReleaseNumber releaseNumber);
}

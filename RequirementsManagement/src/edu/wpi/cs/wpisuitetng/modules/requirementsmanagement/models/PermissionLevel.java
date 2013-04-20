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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

/**
 * Permission levels for different types of users
 * @author William Terry
 */
public enum PermissionLevel {
	ADMIN,		//Admin level
	UPDATE,		//Team member level
	NONE		//Customer level
	//TBI		//User not associated with project
}

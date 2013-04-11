/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sergey Zolotykh (szolotykh)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * @author Sergey Zolotykh (szolotykh)
 *
 */
public class PermissionsTest {
	Permissions goodPermissions;
	String userName;
	PermissionLevel userPermissionLevel;
	@Before
	public void setUp() throws Exception {
		userName = "Admin";
		userPermissionLevel = PermissionLevel.ADMIN;
		goodPermissions = new Permissions(userName, userPermissionLevel);
	}
	@Test
	public void testGetPermissionLevel() {
		assertEquals(PermissionLevel.ADMIN, goodPermissions.getPermissionLevel());
	}
	@Test
	public void testSetPermissionLevelUPDATE() {
		goodPermissions.setPermissionLevel(PermissionLevel.UPDATE);
		assertEquals(PermissionLevel.UPDATE, goodPermissions.getPermissionLevel());
	}
	@Test
	public void testSetPermissionLevelNONE() {
		goodPermissions.setPermissionLevel(PermissionLevel.NONE);
		assertEquals(PermissionLevel.NONE, goodPermissions.getPermissionLevel());
	}
	@Test
	public void testGetUserName() {
		assertEquals("Admin", goodPermissions.getUsername());
	}
	@Test
	public void testSetUserName() {
		goodPermissions.setUsername("User");
		assertEquals("User", goodPermissions.getUsername());
	}
}

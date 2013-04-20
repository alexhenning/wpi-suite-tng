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

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * @author Sergey Zolotykh (szolotykh)
 *
 */
public class ReleaseNumberTest {
	ReleaseNumber goodReleaseNumber;
	int id = 1;
	String releaseNumber = "First";
	Project testProject;
	
	@Before
	public void setUp() throws Exception {
		testProject = new Project("TestProject", "1");
		goodReleaseNumber = new ReleaseNumber(id, releaseNumber, testProject);
	}
	@Test
	public void testGetGoodId() {
		assertEquals(1, goodReleaseNumber.getId());
	}
	@Test
	public void testSetGoodId() {
		goodReleaseNumber.setId(100);
		assertEquals(100, goodReleaseNumber.getId());
	}
	@Test
	public void testGetGoodReleaseNumber() {
		assertEquals("First", goodReleaseNumber.getReleaseNumber());
	}
	@Test
	public void testSetGoodReleaseNumber() {
		goodReleaseNumber.setReleaseNumber("Second");
		assertEquals("Second", goodReleaseNumber.getReleaseNumber());
	}
}
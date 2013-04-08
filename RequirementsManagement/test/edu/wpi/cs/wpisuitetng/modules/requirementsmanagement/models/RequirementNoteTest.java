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

/**
 * @author Sergey Zolotykh (szolotykh)
 *
 */
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class RequirementNoteTest {
	RequirementNote goodRequirementNote;
	int requirementId;
	User currentUuser;
	String body;
	@Before
	public void setUp() throws Exception {
		requirementId = 1;
		currentUuser = new User("admin", "admin", "pass", 1);
		body = "Discription";
		goodRequirementNote = new RequirementNote(requirementId, currentUuser, body);
	}
	@Test
	public void testGetRequirementId() {
		assertEquals(1, goodRequirementNote.getRequirementId());
	}
	@Test
	public void testSetRequirementId() {
		goodRequirementNote.setRequirementId(100);
		assertEquals(100, goodRequirementNote.getRequirementId());
	}
	@Test
	public void testGetNoteBody() {
		assertEquals("Discription", goodRequirementNote.getBody());
	}
	@Test
	public void testSetNoteBody() {
		goodRequirementNote.setBody("More discription");
		assertEquals("More discription", goodRequirementNote.getBody());
	}
}

/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Visit Pataranutaporn
 *    Tim DeFreitas
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import static org.junit.Assert.*;

import org.junit.*;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import static edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus.*;
import static edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority.*;

public class RequirementModelTest {
	
	private RequirementModel rm = new RequirementModel();

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	// TODO: add a test case for non-default constructors
	// TODO: add a test case for combined assignments (sets-gets)	
	
	@Test
	public void testAssigningReleaseNumber() {
		rm.setReleaseNumber(null);
		assertEquals(null, rm.getReleaseNumber());
		ReleaseNumber tmp = new ReleaseNumber(1, 1, new Project("", "2"));
		rm.setReleaseNumber(tmp);
		assertEquals(tmp.getId(), rm.getReleaseNumber().getId());
		assertEquals(tmp.getReleaseNumber(), rm.getReleaseNumber().getReleaseNumber());
	}
	
	@Test
	public void testAssigningID() {
		rm.setId(50);
		assertEquals(50, rm.getId());
		rm.setId(100000);
		assertEquals(100000, rm.getId());
	}

	@Ignore
	@Test
	public void testNewAssignmentIsNew() {
		assertEquals(NEW, rm.getStatus());
	}
	
	@Test
	public void testAssigningName() {
		String s = "Finish the Project";
		rm.setName(s);
		assertEquals(s, rm.getName());
		rm.setName("Unnamed Requirement");
		assertEquals("Unnamed Requirement", rm.getName());
	}
	
	@Test
	public void testAssigningRequirementStatus() {
		rm.setStatus(COMPLETE);
		assertEquals(COMPLETE, rm.getStatus());
		rm.setStatus(DELETED);
		assertEquals(DELETED, rm.getStatus());
		rm.setStatus(IN_PROGRESS);
		assertEquals(IN_PROGRESS, rm.getStatus());
		rm.setStatus(NEW);
		assertEquals(NEW, rm.getStatus());
		rm.setStatus(OPEN);
		assertEquals(OPEN, rm.getStatus());
		
		// Test if reassigning the same status does not cause an error
		rm.setStatus(OPEN);
		assertEquals(OPEN, rm.getStatus());
	}
	
	@Test
	public void testAssigningPriority() {
//		rm.setPriority(CRITICAL);
//		assertEquals(CRITICAL, rm.getPriority());
		rm.setPriority(HIGH);
		assertEquals(HIGH, rm.getPriority());
		rm.setPriority(MEDIUM);
		assertEquals(MEDIUM, rm.getPriority());
		rm.setPriority(LOW);
		assertEquals(LOW, rm.getPriority());
		rm.setPriority(NONE);
		assertEquals(NONE, rm.getPriority());
		
		// Test if reassigning the same priority does not cause an error
		rm.setPriority(NONE);
		assertEquals(NONE, rm.getPriority());
	}
	
	@Test
	public void testAssigningDescription() {
		String s = "This is a test";
		rm.setDescription(s);
		assertEquals(s, rm.getDescription());
		rm.setDescription("Add this test case");
		assertEquals("Add this test case", rm.getDescription());
	}
	
	// TODO: Need to change if estimates are numbers (integers)
	@Test
	public void testAssigningEstimate() {
		int s = 2;
		rm.setEstimate(s);
		assertEquals(s, rm.getEstimate());
		rm.setEstimate(5);
		assertEquals(5, rm.getEstimate());
//		String s = "2 hours";
//		rm.setEstimate(s);
//		assertEquals(s, rm.getEstimate());
//		rm.setEstimate("5 minutes");
//		assertEquals("5 minutes", rm.getEstimate());
	}

	// TODO: Need to change if efforts are numbers (integers)
	@Test
	public void testAssigningActualEffort() {
		int s = 3;
		rm.setActualEffort(s);
		assertEquals(s, rm.getActualEffort());
		rm.setActualEffort(90);
		assertEquals(90, rm.getActualEffort());
//		String s = "3 hours";
//		rm.setActualEffort(s);
//		assertEquals(s, rm.getActualEffort());
//		rm.setActualEffort("about 90 minutes");
//		assertEquals("about 90 minutes", rm.getActualEffort());
	}
	
	@Ignore
	@Test
	public void testJSONConversionOfAnEmptyRequirementModelInstance() {
		String s = rm.toString();
		System.out.println(s);
		System.out.println(RequirementModel.fromJSON(s));
		
		assertEquals(rm, RequirementModel.fromJSON(s));
	}

	@Ignore
	@Test
	public void testSave() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testToJSON() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIdentify() {
		fail("Not yet implemented");
	}

}

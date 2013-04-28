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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.*;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import static edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus.*;
import static edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority.*;

/**
 * Test cases for RequirementModel
 *
 * @author vpatara
 * @author Tim
 * @version $Revision: 1.0 $
 */

public class RequirementModelTest {

	private RequirementModel emptyRequirement;
	private RequirementModel requirement;

	/**
	
	 * @throws Exception */
	@Before
	public void setUp() throws Exception {
		emptyRequirement = new RequirementModel();
		requirement = new RequirementModel(3, null, RequirementStatus.OPEN,
				null, "name3", "description3", 4, 3, new User("name",
						"username", "password", 1), new ArrayList<User>(),
				new Date(), new Date(), new ArrayList<RequirementEvent>(),
				new ArrayList<String>(), null, RequirementType.EPIC);
	}

	// TODO: add a test case for combined assignments (sets-gets)	

	@Test
	public void testCreateRequirementModel() {
		// Test a few fields from "requirement"
		assertEquals(RequirementStatus.OPEN, requirement.getStatus());
		assertEquals("name3", requirement.getName());
		assertEquals("description3", requirement.getDescription());
		assertEquals(3, requirement.getActualEffort());
		assertEquals(RequirementType.EPIC, requirement.getType());
	}

	@Test
	public void testAssigningReleaseNumber() {
		emptyRequirement.setReleaseNumber(null);
		assertEquals(null, emptyRequirement.getReleaseNumber());
		ReleaseNumber tmp = new ReleaseNumber(1, "1", new Project("", "2"));
		emptyRequirement.setReleaseNumber(tmp);
		assertEquals(tmp.getId(), emptyRequirement.getReleaseNumber().getId());
		assertEquals(tmp.getReleaseNumber(), emptyRequirement.getReleaseNumber().getReleaseNumber());
	}

	@Test
	public void testAssigningID() {
		emptyRequirement.setId(50);
		assertEquals(50, emptyRequirement.getId());
		emptyRequirement.setId(100000);
		assertEquals(100000, emptyRequirement.getId());
	}

	@Test
	public void testNewAssignmentIsNew() {
		assertEquals(NEW, emptyRequirement.getStatus());
	}

	@Test
	public void testAssigningName() {
		String s = "Finish the Project";
		emptyRequirement.setName(s);
		assertEquals(s, emptyRequirement.getName());
		emptyRequirement.setName("Unnamed Requirement");
		assertEquals("Unnamed Requirement", emptyRequirement.getName());
	}

	@Test
	public void testAssigningRequirementStatus() {
		emptyRequirement.setStatus(COMPLETE);
		assertEquals(COMPLETE, emptyRequirement.getStatus());
		emptyRequirement.setStatus(DELETED);
		assertEquals(DELETED, emptyRequirement.getStatus());
		emptyRequirement.setStatus(IN_PROGRESS);
		assertEquals(IN_PROGRESS, emptyRequirement.getStatus());
		emptyRequirement.setStatus(NEW);
		assertEquals(NEW, emptyRequirement.getStatus());
		emptyRequirement.setStatus(OPEN);
		assertEquals(OPEN, emptyRequirement.getStatus());

		// Test if reassigning the same status does not cause an error
		emptyRequirement.setStatus(OPEN);
		assertEquals(OPEN, emptyRequirement.getStatus());
	}
	
	@Test
	public void testAssigningPriority() {
		emptyRequirement.setPriority(HIGH);
		assertEquals(HIGH, emptyRequirement.getPriority());
		emptyRequirement.setPriority(MEDIUM);
		assertEquals(MEDIUM, emptyRequirement.getPriority());
		emptyRequirement.setPriority(LOW);
		assertEquals(LOW, emptyRequirement.getPriority());
		emptyRequirement.setPriority(NONE);
		assertEquals(NONE, emptyRequirement.getPriority());
		
		// Test if reassigning the same priority does not cause an error
		emptyRequirement.setPriority(NONE);
		assertEquals(NONE, emptyRequirement.getPriority());
	}

	@Test
	public void testAssigningType() {
		emptyRequirement.setType(RequirementType.NONE);
		assertEquals(RequirementType.NONE, emptyRequirement.getType());
		emptyRequirement.setType(RequirementType.EPIC);
		assertEquals(RequirementType.EPIC, emptyRequirement.getType());
		emptyRequirement.setType(RequirementType.THEME);
		assertEquals(RequirementType.THEME, emptyRequirement.getType());
		emptyRequirement.setType(RequirementType.USER_STORY);
		assertEquals(RequirementType.USER_STORY, emptyRequirement.getType());
		emptyRequirement.setType(RequirementType.NON_FUNCTIONAL);
		assertEquals(RequirementType.NON_FUNCTIONAL, emptyRequirement.getType());
		emptyRequirement.setType(RequirementType.SCENARIO);
		assertEquals(RequirementType.SCENARIO, emptyRequirement.getType());

		// Test reassigning with the same type
		emptyRequirement.setType(RequirementType.SCENARIO);
		assertEquals(RequirementType.SCENARIO, emptyRequirement.getType());
	}

	@Test
	public void testAssigningDescription() {
		String s = "This is a test";
		emptyRequirement.setDescription(s);
		assertEquals(s, emptyRequirement.getDescription());
		emptyRequirement.setDescription("Add this test case");
		assertEquals("Add this test case", emptyRequirement.getDescription());
	}

	@Test
	public void testAssigningEstimate() {
		emptyRequirement.setEstimate(5);
		assertEquals(5, emptyRequirement.getEstimate());
		emptyRequirement.setEstimate(0);
		assertEquals(0, emptyRequirement.getEstimate());
	}

	@Test
	public void testAssigningActualEffort() {
		emptyRequirement.setActualEffort(3);
		assertEquals(3, emptyRequirement.getActualEffort());
		emptyRequirement.setActualEffort(90);
		assertEquals(90, emptyRequirement.getActualEffort());
	}

	@Test
	public void testAssigningIterations() {
		Iteration iteration1 = new Iteration(1, new Date(50), new Date(100),
				"iteration 1", new Project("project", "1"));
		Iteration iteration2 = new Iteration(1, new Date(150), new Date(200),
				"2", new Project("project", "1"));

		// Set an iteration
		emptyRequirement.setIteration(iteration1);
		assertEquals(iteration1, emptyRequirement.getIteration());
		assertEquals("iteration 1", emptyRequirement.getIteration().getIterationNumber());

		// Set another iteration
		emptyRequirement.setIteration(iteration2);
		assertEquals("2", emptyRequirement.getIteration().getIterationNumber());
	}

	@Test
	public void testIdentifyRequirements() {
		RequirementModel requirement1 = new RequirementModel();
		RequirementModel requirement2 = new RequirementModel();
		RequirementModel otherRequirement = new RequirementModel();

		requirement1.setId(4);
		requirement2.setId(4);
		otherRequirement.setId(40);

		assertTrue(requirement1.identify(requirement1));
		assertTrue(requirement1.identify(requirement2));
		assertTrue(requirement2.identify(requirement1));
		assertTrue(requirement2.identify(requirement2));
		assertTrue(otherRequirement.identify(otherRequirement));
		assertTrue(requirement1.identify("4"));

		assertFalse(requirement1.identify(new Object()));
		assertFalse(requirement1.identify(new Integer(4)));
		assertFalse(requirement1.identify("1"));
	}

	@Test
	public void testAddAndGetNotes() {
		Date d = new Date(555);
		User user = new User("test", "testuser", "p", 2);
		emptyRequirement.addNote(user, "a note");
		emptyRequirement.addNote(user, "another note", d);

		RequirementNote[] notes = emptyRequirement.getNotes();
		assertNotNull(notes);
		assertEquals(2, notes.length);

		assertEquals(user, notes[0].getUser());
		assertEquals("a note", notes[0].getBody());
		assertEquals("another note", notes[1].getBody());
		assertEquals(d, notes[1].getDate());
	}

	@Test
	public void testAddAndGetEvents() {
		List <RequirementEvent> events = new ArrayList <RequirementEvent>();
		events.add(new RequirementNote());

		emptyRequirement.setEvents(events);
		assertEquals(events, emptyRequirement.getEvents());
	}

	@Test
	public void testAssigningCreationDate() {
		Date d1 = new Date();
		Date d2 = new Date(99999);

		emptyRequirement.setCreationDate(d1);
		assertEquals(d1, emptyRequirement.getCreationDate());
		emptyRequirement.setCreationDate(d2);
		assertEquals(new Date(99999).toString(), emptyRequirement.getCreationDate().toString());
	}

	@Test
	public void testAssigningLastModifiedDate() {
		Date d1 = new Date();
		Date d2 = new Date(9999);

		emptyRequirement.setLastModifiedDate(d1);
		assertEquals(d1, emptyRequirement.getLastModifiedDate());
		emptyRequirement.setLastModifiedDate(d2);
		assertEquals(new Date(9999).toString(), emptyRequirement.getLastModifiedDate().toString());
	}

	@Test
	public void testAssigningCreator() {
		User user1 = new User("tester1", "user1", "p", 3);
		User user2 = new User("tester2", "user2", "P", 4);

		emptyRequirement.setCreator(user1);
		assertEquals(user1, emptyRequirement.getCreator());
		emptyRequirement.setCreator(user2);
		assertEquals(user2, emptyRequirement.getCreator());
	}

	@Test
	public void testAssigningAssignees() {
		User user1 = new User("tester1", "user1", "p", 3);
		User user2 = new User("tester2", "user2", "P", 4);
		List <User> assignees = new ArrayList <User> ();
		assignees.add(user1);
		assignees.add(user2);

		emptyRequirement.setAssignees(assignees);
		assertNotNull(emptyRequirement.getAssignees());
		assertEquals(2, emptyRequirement.getAssignees().size());
		assertEquals(user1, emptyRequirement.getAssignees().get(0));
		assertEquals(user2, emptyRequirement.getAssignees().get(1));
	}

	@Test
	public void testAssigningSubRequirements() {
		assertNotNull(emptyRequirement.getSubRequirements());
		assertEquals(0, emptyRequirement.getSubRequirements().size());

		emptyRequirement.addSubRequirement("5");
		assertEquals(1, emptyRequirement.getSubRequirements().size());
		emptyRequirement.addSubRequirement("7");
		assertEquals(2, emptyRequirement.getSubRequirements().size());
		// Adding an already-existing id has no effect
		emptyRequirement.addSubRequirement("5");
		assertEquals(2, emptyRequirement.getSubRequirements().size());
	}

	@Test
	public void testAssigningSubRequirementList() {
		List <String> list1 = new ArrayList <String>();
		list1.add("1");
		list1.add("55555");
		ArrayList <String> list2 = new ArrayList <String>();
		list2.add("55");
		list2.add("283940");
		list2.add("10");

		emptyRequirement.setSubRequirements(list1);
		assertNotNull(emptyRequirement.getSubRequirements());
		assertEquals(2, emptyRequirement.getSubRequirements().size());
		assertEquals("55555", emptyRequirement.getSubRequirements().get(1));

		emptyRequirement.setSubRequirements(list2);
		assertNotNull(emptyRequirement.getSubRequirements());
		assertEquals(3, emptyRequirement.getSubRequirements().size());
		assertEquals("283940", emptyRequirement.getSubRequirements().get(1));
	}

	@Test
	public void testJSONConversionOfRequirementModelInstance() {
		emptyRequirement.setName("NAME1");
		String s = emptyRequirement.toString();
		RequirementModel convertedRequirement = RequirementModel.fromJSON(s);

		// Compare entire strings and a few more fields
		assertEquals(emptyRequirement.toString(), convertedRequirement.toString());
		assertEquals("NAME1", convertedRequirement.getName());
		assertEquals(emptyRequirement.getCreationDate().toString(), convertedRequirement.getCreationDate().toString());
	}

	@Test
	public void testJSONArrayConversion() {
		emptyRequirement.setId(500);
		String s1 = emptyRequirement.toString();
		String s2 = requirement.toString();

		String s = "[" + s1 + "," + s2 + "]";
		RequirementModel[] requirements = RequirementModel.fromJSONArray(s);
		assertNotNull(requirements);
		assertEquals(2, requirements.length);
		assertTrue(emptyRequirement.identify(requirements[0]));
		assertTrue(requirement.identify(requirements[1]));
		assertFalse(requirement.identify(requirements[0]));
	}

	// No effect
	@Test
	public void testSave() {
		emptyRequirement.save();
	}

	// No effect
	@Test
	public void testDelete() {
		emptyRequirement.delete();
	}
}

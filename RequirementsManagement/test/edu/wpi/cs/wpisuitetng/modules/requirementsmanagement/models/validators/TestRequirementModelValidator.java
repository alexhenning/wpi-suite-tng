/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tim DeFreitas
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.DefectEvent;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Tag;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.mockdata.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;


/**
 *
 * Description goes here
 * @author Tim
 *
 */
public class TestRequirementModelValidator {
	
	RequirementModel existingRequirement;
	User existingUser;
	User existingUserCopy;
	RequirementModel goodNewRequirement;
	RequirementModel goodUpdatedRequirement;
	
	User bob;
	User bobCopy;
	User invalidUser;

	List<RequirementEvent> ignoredEvents;
	Project testProject;
	Session defaultSession;
	String mockSsid;
	Data db;
	RequirementModelValidator validator;
	RequirementModel otherRequirement;
	Project otherProject;
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		invalidUser = new User("idontexist", "blah", "1234", 99);
		
		bob = new User("bob", "bob", "1234", 1);
		existingUser = new User("joe", "joe", "1234", 2);
		
		existingRequirement = new RequirementModel();
		//TODO set fields for requirement
		existingRequirement.setId(1);
		existingRequirement.setName("Existing");
		existingRequirement.setDescription("Is existing");
		existingRequirement.setCreator(bob);
		
		otherRequirement = new RequirementModel();
		//TODO set fields for other requirement (Different Project)
		otherRequirement.setId(2);
		
		otherProject = new Project("other", "2");
		
		testProject = new Project("test","1");
		mockSsid = "abcde";
		defaultSession = new Session(bob, testProject, mockSsid);
		
		//Copies that simulate db4o cross-container problem
		bobCopy = new User(null, "bob", null, -1);
		goodNewRequirement = new RequirementModel();
		//TODO set fields for new requirement
		goodNewRequirement.setCreator(bob);
		goodNewRequirement.setName("New Requirement");
		goodNewRequirement.setDescription("A new requirement");
		//Set user to bobCopy
		
		ignoredEvents = new ArrayList<RequirementEvent>();
		goodNewRequirement.setEvents(ignoredEvents);
		
		existingUserCopy = new User(null, "joe", null, -1);
		goodUpdatedRequirement = new RequirementModel();
		//TODO set fields
		goodUpdatedRequirement.setCreator(bob);
		goodUpdatedRequirement.setName("Updated Requirement");
		goodUpdatedRequirement.setDescription("It has a description");
		
		db = new MockData(new HashSet<Object>());
		db.save(bob);
		db.save(existingRequirement, testProject);
		db.save(existingUser);
		db.save(otherRequirement, otherProject);
		validator = new RequirementModelValidator(db);
	
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDBState() throws WPISuiteException{
		assertSame(bob, db.retrieve(User.class,  "username", "bob").get(0));
		assertSame(existingRequirement, db.retrieve(RequirementModel.class, "id", 1).get(0));
		assertSame(otherRequirement, db.retrieve(RequirementModel.class, "id", 2).get(0));
	}
	public List<ValidationIssue> checkNumIssues(int num, Session session, RequirementModel requirement, Mode mode) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, requirement, mode);
			assertEquals(num, issues.size());
		} catch(WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	public void checkNoIssues(Session session, RequirementModel requirement, Mode mode) {
		checkNumIssues(0, session, requirement, mode);
	}
	
	public List<ValidationIssue> checkIssue(Session session, RequirementModel requirement, Mode mode) {
		return checkNumIssues(1, session, requirement, mode);
	}
	
	public List<ValidationIssue> checkNonFieldIssue(Session session, RequirementModel requirement, Mode mode) {
		List<ValidationIssue> issues = checkIssue(session, requirement, mode);
		assertFalse(issues.get(0).hasFieldName());
		return issues;
	}
	
	public List<ValidationIssue> checkFieldIssue(Session session,  RequirementModel requirement, Mode mode, 
			String fieldName) {
		List<ValidationIssue> issues = checkNumIssues(1, session, requirement, mode);
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
	}
	
	@Test
	public void testNullNewRequirement(){
		checkNonFieldIssue(defaultSession, null, Mode.CREATE);
	}
	
	@Test
	public void testGoodNewRequirement(){
		checkNoIssues(defaultSession, goodNewRequirement, Mode.CREATE);
		assertSame(bob, goodNewRequirement.getCreator());
		assertEquals(0, goodNewRequirement.getEvents().size());
		assertNotSame(ignoredEvents, goodNewRequirement.getEvents());
		assertNotNull(goodNewRequirement.getCreationDate());
		assertNotNull(goodNewRequirement.getLastModifiedDate());
		assertEquals(RequirementStatus.NEW, goodNewRequirement.getStatus());
		
		
	}
	
	@Test
	public void testEmptyName(){
		goodNewRequirement.setName("");
		checkFieldIssue(defaultSession, goodNewRequirement, Mode.CREATE, "name");
	}
	
	@Test
	public void testEmptyDescription(){
		goodNewRequirement.setDescription("");
		checkFieldIssue(defaultSession, goodNewRequirement, Mode.CREATE, "description");
	}
	
	@Test
	public void testTooLongName(){
		String toolongname = "abc123";
		for(int i = 1; i < 20; i++){
			toolongname += "abc123";
		}
		goodNewRequirement.setName(toolongname);
		checkFieldIssue(defaultSession, goodNewRequirement, Mode.CREATE, "name");
	}
	
	
	

}

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
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;

import static org.junit.Assert.*;

import java.util.List;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.mockdata.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;

/**
 * Test cases for PermissionsValidator
 *
 * @author William Terry
 *
 * @version $Revision: 1.0 $
 */
public class PermissionsValidatorTest {

	private PermissionsValidator validator;
	
	Project testProject;
	
	Permissions profile1, profile2;
	
	Session defaultSession;
	User user1, user2;
	String mockSsid;
	List<ValidationIssue> issueList;

	Data db;
	
	/**
	 * Method setUp.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {	

		user1 = new User("john", "user1", "1234", 1);
		user2 = new User("lemon", "user2", "1234", 1);
		
		mockSsid = "abc123";
		testProject = new Project("TestProject", "1");
		defaultSession = new Session(user1, testProject, mockSsid);
		
		profile1 = new Permissions(user1.getUsername(), PermissionLevel.NONE);
		profile2 = new Permissions(user2.getUsername(), PermissionLevel.ADMIN);
		
		// Only user1 is saved
		db = new MockData(new HashSet<Object>());
		db.save(user1, testProject);

		validator = new PermissionsValidator(db);
	}

	@Test
	public void testInstantiation() {
		validator = new PermissionsValidator(null);
		assertNull(validator.getData());
		validator.setData(db);
		assertEquals(db, validator.getData());
	}

	/**
	 * Method testCatchNullPermissions.
	 * @throws WPISuiteException
	 */
	@Test
	public void testCatchNullPermissions() throws WPISuiteException {
		issueList = validator.validate(defaultSession, null, Mode.EDIT);
		assertEquals("Permissions cannot be null", issueList.get(0).getMessage());
		issueList = validator.validate(defaultSession, profile1, Mode.EDIT);
		assertNotSame("Permissions cannot be null", issueList.get(0).getMessage());
	}

	/**
	 * Method checkNumIssues.
	 * @param num int
	 * @param session Session
	 * @param profile Permissions
	 * @param mode Mode
	 * @return List<ValidationIssue>
	 */
	public List<ValidationIssue> checkNumIssues(int num, Session session, Permissions profile, Mode mode) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, profile, mode);
			System.out.println("issue size : " + issues.size());
			for(ValidationIssue i : issues) {
				System.out.println(i.getFieldName() + "--" + i.getMessage());
			}
			assertEquals(num, issues.size());
		} catch(WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	/**
	 * Method checkNoIssues.
	 * @param session Session
	 * @param profile Permissions
	 * @param mode Mode
	 */
	public void checkNoIssues(Session session, Permissions profile, Mode mode) {
		checkNumIssues(0, session, profile, mode);
	}
	
	/**
	 * Method checkIssue.
	 * @param session Session
	 * @param profile Permissions
	 * @param mode Mode
	 * @return List<ValidationIssue>
	 */
	public List<ValidationIssue> checkIssue(Session session, Permissions profile, Mode mode) {
		return checkNumIssues(1, session, profile, mode);
	}
	
	/**
	 * Method checkNonFieldIssue.
	 * @param session Session
	 * @param profile Permissions
	 * @param mode Mode
	 * @return List<ValidationIssue>
	 */
	public List<ValidationIssue> checkNonFieldIssue(Session session, Permissions profile, Mode mode) {
		List<ValidationIssue> issues = checkIssue(session, profile, mode);
		assertFalse(issues.get(0).hasFieldName());
		return issues;
	}
	
	/**
	 * Method checkFieldIssue.
	 * @param session Session
	 * @param profile Permissions
	 * @param mode Mode
	 * @param fieldName String
	 * @return List<ValidationIssue>
	 */
	public List<ValidationIssue> checkFieldIssue(Session session, Permissions profile, Mode mode, String fieldName) {
		List<ValidationIssue> issues = checkNumIssues(1, session, profile, mode);
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
	}

	@Test
	public void testGoodPermissions() {
		checkNoIssues(defaultSession, profile1, Mode.CREATE);
	}

	@Test
	public void testNullPermissions() {
		checkIssue(defaultSession, null, Mode.CREATE);
	}

	@Test
	public void testNullUsername() {
		profile1.setUsername(null);
		checkNumIssues(1, defaultSession, profile1, Mode.CREATE);
	}

	@Test
	public void testNullPermissionsLevel() {
		profile1.setPermissionLevel(null);
		checkNumIssues(1, defaultSession, profile1, Mode.CREATE);
	}

	@Test
	public void testAbsentUser() {
		checkNumIssues(2, defaultSession, profile2, Mode.CREATE);
	}

	@Test
	public void testPreexistingPermissions() {
		// Save the permission profile before redundantly creating one
		db.save(profile1, testProject);
		System.out.println((db.retrieveAll(new Permissions())).toArray(new Permissions[0]).length);
		checkNumIssues(1, defaultSession, profile1, Mode.CREATE);
	}
}

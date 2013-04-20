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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.mockdata.*;

/**
 *
 * Description goes here
 * @author Tim
 *
 */
public class TestReleaseNumberValidator {
	
	ReleaseNumber existingReleaseNum;
	
	ReleaseNumber goodNewReleaseNum;
	User existingUser;

	User bob;
	User invalidUser;

	Project testProject;
	Session defaultSession;
	String mockSsid;
	Data db;
	
	ReleaseNumberValidator validator;

	Project otherProject;
	
	
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		bob = new User("bob", "bob", "1234", 1);
		existingUser = new User("joe", "joe", "1234", 2);
		
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "ABCD";
		defaultSession = new Session(bob, testProject, mockSsid);
		
		existingReleaseNum = new ReleaseNumber(1, "1", testProject);
		goodNewReleaseNum = new ReleaseNumber(2, "34", testProject);
		
		db = new MockData(new HashSet<Object>());
		db.save(bob);
		db.save(existingUser);
		db.save(existingReleaseNum);
		validator = new ReleaseNumberValidator(db);
		
		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDBState() throws WPISuiteException{
		assertSame(bob, db.retrieve(User.class, "username", "bob").get(0));
		assertSame(existingReleaseNum, db.retrieve(ReleaseNumber.class, "id", 1).get(0));
		
	}
	
	
	public List<ValidationIssue> checkNumIssues(int num, Session session, ReleaseNumber releasenum, Mode mode){
		List<ValidationIssue> issues;
		try{
			issues = validator.validate(session, releasenum, mode);
			assertEquals(num, issues.size());
		} catch (WPISuiteException e){
			throw new RuntimeException("Unexpected WPISuiteException");	
		}
		return issues;
	}
	
	//Asserts that there are no issues
	public void checkNoIssues(Session session, ReleaseNumber releasenum, Mode mode){
		checkNumIssues(0, session, releasenum, mode);
	}
	
	//Asserts that there is one issue 
	public List<ValidationIssue> checkIssue(Session session, ReleaseNumber releasenum, Mode mode){
		return checkNumIssues(1, session, releasenum, mode);
	}
	
	//Asserts that there is a non-field validation issue
	public List<ValidationIssue> checkNonFieldIssue(Session session, ReleaseNumber releasenum, Mode mode) {
		List<ValidationIssue> issues = checkIssue(session, releasenum, mode);
		assertFalse(issues.get(0).hasFieldName());
		return issues;
	}
	
	//Asserts that there is a validation issue with a field for the given Release Number
	public List<ValidationIssue> checkFieldIssue(Session session, ReleaseNumber releasenum, Mode mode, 
			String fieldName) {
		List<ValidationIssue> issues = checkNumIssues(1, session, releasenum, mode);
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
	}
	
	
	@Test
	public void testNullNewReleaseNum(){
		checkNonFieldIssue(defaultSession, null, Mode.CREATE);
	}
	
	@Test
	public void testGoodNewReleaseNumber(){
		//TODO Fix validation issue of checking whether a new id is in the db
		//checkNoIssues(defaultSession, goodNewReleaseNum, Mode.CREATE);
		assertSame(2, goodNewReleaseNum.getId());
		assertSame(testProject, goodNewReleaseNum.getProject());
	}
}

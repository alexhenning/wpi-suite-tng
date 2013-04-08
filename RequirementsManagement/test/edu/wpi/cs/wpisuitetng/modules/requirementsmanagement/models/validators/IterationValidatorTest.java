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
/**
*
* @author Sergey Zolotykh (szolotykh) 
*
*/
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.IterationValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

public class IterationValidatorTest {
	private IterationValidator validator;
	
	Project testproject;
	Date startDate;
	Date endDate;
	
	Iteration firstIteration;
	Iteration goodIteration;
	Iteration lastIteration;
	
	
	Session defaultSession;
	User bobUser;
	String mockSsid;

	Data db;
	
	@Before
	public void setUp() throws Exception {
		// Dates
		startDate = newDate(10);
		endDate = newDate(20);		
		
		bobUser = new User("bob", "bob", "1234", 1);
		
		mockSsid = "abc123";
		testproject = new Project("TestProject", "1");
		defaultSession = new Session(bobUser, testproject, mockSsid);
		
		firstIteration = new Iteration(1, newDate(1), newDate(9), "1", testproject);
		goodIteration = new Iteration(100, startDate, endDate, "100", testproject);
		lastIteration = new Iteration(2, newDate(21), newDate(30), "2", testproject);
		
		db = new MockData(new HashSet<Object>());
		db.save(firstIteration);
		db.save(lastIteration);
		validator = new IterationValidator(db);
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	public Date newDate(int day){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	public List<ValidationIssue> checkNumIssues(int num, Session session, Iteration iteration, Mode mode) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, iteration, mode);
			assertEquals(num, issues.size());
		} catch(WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	public void checkNoIssues(Session session, Iteration iteration, Mode mode) {
		checkNumIssues(0, session, iteration, mode);
	}
	
	public List<ValidationIssue> checkIssue(Session session, Iteration iteration, Mode mode) {
		return checkNumIssues(1, session, iteration, mode);
	}
	
	public List<ValidationIssue> checkNonFieldIssue(Session session, Iteration iteration, Mode mode) {
		List<ValidationIssue> issues = checkIssue(session, iteration, mode);
		assertFalse(issues.get(0).hasFieldName());
		return issues;
	}
	
	public List<ValidationIssue> checkFieldIssue(Session session, Iteration iteration, Mode mode, String fieldName) {
		List<ValidationIssue> issues = checkNumIssues(1, session, iteration, mode);
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
	}
	
	@Test
	public void testNullIteration() {
		checkIssue(defaultSession, null, Mode.CREATE);
	}
	
	@Test
	public void testGoodInteration(){
		checkNoIssues(defaultSession, goodIteration, Mode.CREATE);
	}
	
	@Test
	public void testBadId(){
		goodIteration.setId(1);
		checkNumIssues(1, defaultSession, goodIteration, Mode.CREATE);
	}	
	@Test
	public void testNullStartDate(){
		goodIteration.setStartDate(null);
		checkNumIssues(1, defaultSession, goodIteration, Mode.CREATE);
	}
	
	@Test
	public void testNullEndDate(){
		goodIteration.setEndDate(null);
		checkNumIssues(1, defaultSession, goodIteration, Mode.CREATE);
	}
	@Test
	public void testEndDateGreaderStartDate(){
		goodIteration.setStartDate(endDate);
		goodIteration.setEndDate(startDate);
		checkNumIssues(1, defaultSession, goodIteration, Mode.CREATE);
	}
	@Test
	public void testDatesOverlapsUp(){
		goodIteration.setEndDate(newDate(25));
		checkNumIssues(2, defaultSession, goodIteration, Mode.CREATE);
	}
	@Test
	public void testDatesOverlapsDown(){
		goodIteration.setStartDate(newDate(5));
		checkNumIssues(2, defaultSession, goodIteration, Mode.CREATE);
	}
	@Test
	public void testDatesOverlaps(){
		goodIteration.setStartDate(newDate(5));
		goodIteration.setEndDate(newDate(25));
		checkNumIssues(4, defaultSession, goodIteration, Mode.CREATE);
	}
}

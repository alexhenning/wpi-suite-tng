/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 * @author milk3dfx
 *
 */
public class IterationeTest {
	Iteration goodIteration;
	int iterationId = 1;
	Calendar calendar;
	Date startDate;
	Date endDate;
	String iterationNumber;
	Project testProject;
	@Before
	public void setUp() throws Exception {

		startDate = newDate("04/09/13 9:41:10");
		endDate = newDate("04/20/13 9:41:10");
		
		iterationNumber = "First";
		testProject = new Project("TestProject", "1");
		
		goodIteration = new Iteration(iterationId, startDate, endDate, iterationNumber, testProject);
	}
	@Test
	public void testGetGoodId() {
		assertEquals(1, goodIteration.getId());
	}
	@Test
	public void testSetGoodId() {
		goodIteration.setId(100);
		assertEquals(100, goodIteration.getId());
	}
	@Test
	public void testGetStartDate() {
		assertEquals(newDate("04/09/13 9:41:10"), goodIteration.getStartDate());
	}
	@Test
	public void testSetStartDate() {
		goodIteration.setStartDate(newDate("04/10/13 9:41:10"));
		assertEquals(newDate("04/10/13 9:41:10"), goodIteration.getStartDate());
	}
	@Test
	public void testGetEndDate() {
		assertEquals(newDate("04/20/13 23:59:59"), goodIteration.getEndDate());
	}
	@Test
	public void testSetEndDate9h() {
		goodIteration.setEndDate(newDate("04/21/13 9:41:10"));
		assertEquals(newDate("04/21/13 23:59:59"), goodIteration.getEndDate());
	}
	@Test
	public void testSetEndDate20h() {
		goodIteration.setEndDate(newDate("04/10/13 20:41:10"));
		assertEquals(newDate("04/10/13 23:59:59"), goodIteration.getEndDate());
	}
	
	Date newDate(String strDate){
		Date newDate;
		try{
			newDate =  new SimpleDateFormat("MM/dd/yy HH:mm:ss").parse(strDate);
		}
		catch(ParseException e){
			throw new RuntimeException("Date parse error", e);
		}
		return newDate;
	}
}

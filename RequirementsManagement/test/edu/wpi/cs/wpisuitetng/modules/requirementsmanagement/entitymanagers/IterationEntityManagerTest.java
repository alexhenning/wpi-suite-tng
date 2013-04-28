/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.mockdata.*;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

/**
 * Test cases for IterationEntityManager
 *
 * @author vpatara
 *
 * @version $Revision: 1.0 $
 */
public class IterationEntityManagerTest {

	private MockData db;
	private String mockSsid;

	private IterationEntityManager manager;

	private User currentUser;
	private User otherUser;
	private Session testSession;
	private Session otherSession;
	private Project testProject;
	private Project otherProject;
	private DateFormat dateFormat;
	private Iteration existingIteration;
	private Iteration otherIteration;


	/**
	 * Sets up the testing environment for this test set
	 *
	
	 * @throws Exception
	 * @throws java.lang.Exception */
	@Before
	public void setUp() throws Exception {
		// Initialize the entity manager
		db = new MockData(new HashSet<Object>());
		manager = new IterationEntityManager(db);

		// Create
		mockSsid = "ABC";
		currentUser = new User("Tim", "tim", "password", 2);
		otherUser = new User("Nope", "nope", "p", 3);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		testSession = new Session(currentUser, testProject, mockSsid);
		otherSession = new Session(otherUser, otherProject, mockSsid);

		// Initialize iterations
		dateFormat = new SimpleDateFormat("mm/dd/yyyy");
		existingIteration = new Iteration(1, dateFormat.parse("01/01/2013"),
				dateFormat.parse("01/07/2013"), "1", testProject);
		otherIteration = new Iteration(2, dateFormat.parse("02/01/2013"),
				dateFormat.parse("02/07/2013"), "3", otherProject);

		// Saves the existing iteration into the mock database
		db.save(existingIteration);
		db.save(otherIteration);
	}

	/**
	 *Description goes here
	 *
	
	 * @throws Exception
	 * @throws java.lang.Exception */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Method testInitialCount.
	 * @throws WPISuiteException
	 */
	@Test
	public void testInitialCount() throws WPISuiteException {
		assertEquals(2, manager.Count());
	}

	/**
	 * Method testInitialGetAll.
	 * @throws WPISuiteException
	 */
	@Test
	public void testInitialGetAll() throws WPISuiteException {
		assertEquals(1, manager.getAll(testSession).length);
		assertEquals(1, manager.getAll(otherSession).length);
		assertEquals("1", manager.getAll(testSession)[0].getIterationNumber());
		assertEquals("3", manager.getAll(otherSession)[0].getIterationNumber());
	}

	/**
	 * Method testSavingTheSameIteration.
	 * @throws WPISuiteException
	 */
	@Test
	public void testSavingTheSameIteration() throws WPISuiteException {
		manager.save(testSession, existingIteration);
		assertEquals(2, manager.Count());
		manager.save(otherSession, otherIteration);
		assertEquals(2, manager.Count());
	}

	/**
	 * Method testSavingNewIterations.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test
	public void testSavingNewIterations() throws WPISuiteException, ParseException {
		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		// Another new iteration (non-overlapping dates)
		Iteration iteration2 = new Iteration();
		iteration2.setStartDate(dateFormat.parse("01/15/2013"));
		iteration2.setEndDate(dateFormat.parse("01/16/2013"));
		iteration2.setIterationNumber("12");

		// The same iteration (different object) for the other project/session
		Iteration iteration3 = new Iteration();
		iteration3.setStartDate(dateFormat.parse("01/15/2013"));
		iteration3.setEndDate(dateFormat.parse("01/16/2013"));
		iteration3.setIterationNumber("12");

		manager.save(testSession, iteration1);
		assertEquals(3, manager.Count());
		manager.save(testSession, iteration2);
		assertEquals(4, manager.Count());
		manager.save(otherSession, iteration3);
		assertEquals(5, manager.Count());

		assertEquals(3, manager.getAll(testSession).length);
		assertEquals(2, manager.getAll(otherSession).length);
	}

	/**
	 * Method testDeleteAll.
	 * @throws WPISuiteException
	 */
	@Test
	public void testDeleteAll() throws WPISuiteException {
		manager.deleteAll(testSession);
		assertEquals(1, manager.Count());
		manager.deleteAll(otherSession);
		assertEquals(0, manager.Count());
	}

	/**
	 * Method testDeleteAllAndWithResaving.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test
	public void testDeleteAllAndWithResaving() throws WPISuiteException, ParseException {
		manager.deleteAll(otherSession);
		assertEquals(1, manager.Count());
		manager.deleteAll(testSession);
		assertEquals(0, manager.Count());

		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		// Now, save all in the test session
		manager.save(testSession, existingIteration);
		manager.save(testSession, otherIteration);
		manager.save(testSession, iteration1);
		assertEquals(3, manager.Count());

		// There is no iteration in the other session to be deleted
		manager.deleteAll(otherSession);
		assertEquals(3, manager.Count());

		// Now, deleting iterations of this session should clear them all
		manager.deleteAll(testSession);
		assertEquals(0, manager.Count());
	}

	/**
	 * Method testGetEntityWithSaving.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test
	public void testGetEntityWithSaving() throws WPISuiteException, ParseException {
		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setId(5);
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		// Retrieve existing iterations
		assertEquals(existingIteration, manager.getEntity(testSession, "1")[0]);
		assertEquals(otherIteration, manager.getEntity(otherSession, "2")[0]);

		// Add another iteration and retrieve it back
		manager.save(testSession, iteration1);
		assertEquals(iteration1, manager.getEntity(testSession, "5")[0]);
	}

	/**
	 * Method testMakeEntity.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test
	public void testMakeEntity() throws WPISuiteException, ParseException {
		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		// Another new iteration (non-overlapping dates)
		Iteration iteration2 = new Iteration();
		iteration2.setStartDate(dateFormat.parse("01/15/2013"));
		iteration2.setEndDate(dateFormat.parse("01/16/2013"));
		iteration2.setIterationNumber("12");

		// The same iteration (different object) for the other project/session
		Iteration iteration3 = new Iteration();
		iteration3.setStartDate(dateFormat.parse("01/15/2013"));
		iteration3.setEndDate(dateFormat.parse("01/16/2013"));
		iteration3.setIterationNumber("12");

		// Make and save entities (two projects)
		manager.makeEntity(testSession, iteration1.toJSON());
		assertEquals(3, manager.Count());
		manager.makeEntity(testSession, iteration2.toJSON());
		assertEquals(4, manager.Count());
		manager.makeEntity(otherSession, iteration3.toJSON());
		assertEquals(5, manager.Count());

		// Make sure the iterations are saved in the correct projects
		assertEquals(3, manager.getAll(testSession).length);
		assertEquals(2, manager.getAll(otherSession).length);
		assertEquals("11", manager.getEntity(testSession, "3")[0].getIterationNumber());
		assertEquals("12", manager.getEntity(testSession, "4")[0].getIterationNumber());
		assertEquals("12", manager.getEntity(otherSession, "5")[0].getIterationNumber());
	}

	/**
	 * Method testMakeBadEntityWithStartDateAfterEndDate.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test (expected = BadRequestException.class)
	public void testMakeBadEntityWithStartDateAfterEndDate() throws WPISuiteException, ParseException {
		// New iteration with an invalid date interval (start after end)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2014"));
		iteration1.setEndDate(dateFormat.parse("01/09/2014"));
		iteration1.setIterationNumber("11");

		manager.makeEntity(testSession, iteration1.toJSON());
	}

	/**
	 * Method testMakeTwoEntitiesWithTheSameNumberInOneProject.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test (expected = BadRequestException.class)
	public void testMakeTwoEntitiesWithTheSameNumberInOneProject() throws WPISuiteException, ParseException {
		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		// Another new iteration for the other project/session (non-overlapping dates)
		Iteration iteration2 = new Iteration();
		iteration2.setStartDate(dateFormat.parse("01/15/2013"));
		iteration2.setEndDate(dateFormat.parse("01/16/2013"));
		iteration2.setIterationNumber("11");

		manager.makeEntity(testSession, iteration1.toJSON());
		manager.makeEntity(testSession, iteration2.toJSON());
	}

	/**
	 * Method testMakeEntityForOneProjectDoesNotAffectAnotherProject.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test (expected = NotFoundException.class)
	public void testMakeEntityForOneProjectDoesNotAffectAnotherProject() throws WPISuiteException, ParseException {
		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		manager.makeEntity(testSession, iteration1.toJSON());
		assertEquals(3, manager.Count());
		manager.getEntity(otherSession, "3");
	}

	/**
	 * Method testGetEntityWithNonNumericIdResultsInNotFoundException.
	 * @throws WPISuiteException
	 */
	@Test (expected = NotFoundException.class)
	public void testGetEntityWithNonNumericIdResultsInNotFoundException() throws WPISuiteException {
		manager.getEntity(testSession, "a");
	}

	/**
	 * Method testGetEntityWithInvalidIdResultsInNotFoundException.
	 * @throws WPISuiteException
	 */
	@Test (expected = NotFoundException.class)
	public void testGetEntityWithInvalidIdResultsInNotFoundException() throws WPISuiteException {
		manager.getEntity(testSession, "-1");
	}

	/**
	 * Method testUpdate.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test
	public void testUpdate() throws WPISuiteException, ParseException {
		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		// Another new iteration (non-overlapping dates)
		Iteration iteration2 = new Iteration();
		iteration2.setStartDate(dateFormat.parse("01/15/2013"));
		iteration2.setEndDate(dateFormat.parse("01/16/2013"));
		iteration2.setIterationNumber("12");

		// The same iteration (different object) for the other project/session
		Iteration iteration3 = new Iteration();
		iteration3.setStartDate(dateFormat.parse("01/15/2013"));
		iteration3.setEndDate(dateFormat.parse("01/16/2013"));
		iteration3.setIterationNumber("12");

		// Make and save entities (two projects)
		manager.makeEntity(testSession, iteration1.toJSON());
		assertEquals(3, manager.Count());
		manager.makeEntity(testSession, iteration2.toJSON());
		assertEquals(4, manager.Count());
		manager.makeEntity(otherSession, iteration3.toJSON());
		assertEquals(5, manager.Count());

		// See if they are well-saved
		assertEquals(3, manager.getAll(testSession).length);
		assertEquals(2, manager.getAll(otherSession).length);

		// Make some changes to the iterations and update (random order)
		iteration1 = manager.getEntity(testSession, "3")[0];
		iteration2 = manager.getEntity(testSession, "4")[0];
		iteration3 = manager.getEntity(otherSession, "5")[0];
		iteration1.setEndDate(dateFormat.parse("01/13/2013"));
		iteration2.setIterationNumber("100");
		iteration3.setStartDate(dateFormat.parse("01/10/2013"));

		manager.update(testSession, iteration1.toJSON());
		manager.update(testSession, iteration2.toJSON());
		manager.update(otherSession, iteration3.toJSON());

		// Make sure the iterations are edited properly
		assertEquals(iteration1.getEndDate(), manager.getEntity(testSession, "3")[0].getEndDate());
		assertEquals("100", manager.getEntity(testSession, "4")[0].getIterationNumber());
		assertEquals(iteration3.getStartDate(), manager.getEntity(otherSession, "5")[0].getStartDate());
	}

	/**
	 * Method testCannotFindDeletedIterations.
	 * @throws WPISuiteException
	 */
	@Test (expected = NotFoundException.class)
	public void testCannotFindDeletedIterations() throws WPISuiteException {
		manager.deleteEntity(testSession, "1");
		manager.getEntity(testSession, "1");
	}

	/**
	 * Method testCannotDeleteNonExistingIteration.
	 * @throws WPISuiteException
	 */
	@Test (expected = NotFoundException.class)
	public void testCannotDeleteNonExistingIteration() throws WPISuiteException {
		manager.deleteEntity(testSession, "-100");
	}

	/**
	 * Method testCannotUpdateNonExistingIteration.
	 * @throws WPISuiteException
	 */
	@Test (expected = BadRequestException.class)
	public void testCannotUpdateNonExistingIteration() throws WPISuiteException {
		manager.update(testSession, new Iteration().toJSON());
	}

	/**
	 * Method testDeleteEntities.
	 * @throws WPISuiteException
	 * @throws ParseException
	 */
	@Test
	public void testDeleteEntities() throws WPISuiteException, ParseException {
		// New iteration (non-overlapping dates)
		Iteration iteration1 = new Iteration();
		iteration1.setStartDate(dateFormat.parse("01/10/2013"));
		iteration1.setEndDate(dateFormat.parse("01/12/2013"));
		iteration1.setIterationNumber("11");

		// Another new iteration for the other project (non-overlapping dates)
		Iteration iteration2 = new Iteration();
		iteration2.setStartDate(dateFormat.parse("01/15/2013"));
		iteration2.setEndDate(dateFormat.parse("01/16/2013"));
		iteration2.setIterationNumber("12");

		// Save the iterations projects
		manager.makeEntity(testSession, iteration1.toJSON());
		manager.makeEntity(otherSession, iteration2.toJSON());

		// Delete the iterations in the test project
		assertEquals(2, manager.getAll(testSession).length);
		manager.deleteEntity(testSession, "1");
		assertEquals(1, manager.getAll(testSession).length);
		manager.deleteEntity(testSession, "3");
		assertEquals(0, manager.getAll(testSession).length);

		// Make sure iterations in the other project still exist
		assertEquals(2, manager.getAll(otherSession).length);
	}
}

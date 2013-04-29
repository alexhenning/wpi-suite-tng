/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sergey Zolotyhk
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementType;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.dummyserver.DummyServer;




/**
 * @author szolotykh
 *
 */

public class RequirementNoteValidatorTest {
	User currentUser;
	RequirementNote goodNote;
	String noteBody;
	RequirementNoteValidator validator;
	
	Session defaultSession;
	Project testproject;
	String mockSsid;
	
	Data db;
	
	RequirementModel mainRequirement;
	
	// Network
	DummyServer server;
	private NetworkConfiguration config;
	private static int port = 38512;
	
	@Before
	public void setUp() throws Exception {
		
		config = new NetworkConfiguration("http://localhost:" + port);
		Network.getInstance().setDefaultNetworkConfiguration(config);
		server = new DummyServer(port);
		server.start();
		
		noteBody = "Requirement note body.";
		currentUser = new User("Admin", "Admin", "pass", 1);
		mockSsid = "abc123";
		testproject = new Project("TestProject", "1");
		defaultSession = new Session(currentUser, testproject, mockSsid);

		mainRequirement = new RequirementModel(
				1, // int id
				new ReleaseNumber(1, "1000", testproject), //ReleaseNumber releaseNumber,
				RequirementStatus.IN_PROGRESS, //RequirementStatus status,
				RequirementPriority.HIGH, //RequirementPriority priority,
				"goodRequirement", //String name,
				"Discription", // String description,
				100, //int estimate,
				90, //int actualEffort,
				currentUser, // User creator,
				null, //List<User> assignees,
				new Date(), //Date creationDate,
				new Date(), //Date lastModifiedDate, 
				null, //List<RequirementEvent> events,
				null, //List<RequirementModel> subRequirements,
				null, //Iteration iteration,
				RequirementType.EPIC//RequirementType type
		);
		
		// RequirementNote(int requirementId, User user, String body)
		goodNote = new RequirementNote(1, currentUser, noteBody);
		
		db = new edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.mockdata.MockData(new HashSet<Object>());
		db.save(currentUser);
		db.save(mainRequirement);
		
		validator = new RequirementNoteValidator(db);
	}
	@After
	public void tearDown() throws Exception {
		server.stop();
	}
	public List<ValidationIssue> checkNumIssues(int num, Session session, RequirementNote comment) {
		List<ValidationIssue> issues;
		try {
			issues = validator.validate(session, comment);
			assertEquals(num, issues.size());
		} catch(WPISuiteException e) {
			throw new RuntimeException("Unexpected WPISuiteException", e);
		}
		return issues;
	}
	
	public void checkNoIssues(Session session, RequirementNote comment) {
		checkNumIssues(0, session, comment);
	}
	
	public List<ValidationIssue> checkIssue(Session session, RequirementNote comment) {
		return checkNumIssues(1, session, comment);
	}
	
	public List<ValidationIssue> checkNonFieldIssue(Session session, RequirementNote comment) {
		List<ValidationIssue> issues = checkIssue(session, comment);
		assertFalse(issues.get(0).hasFieldName());
		return issues;
	}
	
	public List<ValidationIssue> checkFieldIssue(Session session, RequirementNote comment, String fieldName) {
		List<ValidationIssue> issues = checkNumIssues(1, session, comment);
		assertEquals(fieldName, issues.get(0).getFieldName());
		return issues;
	}
	
	//TODO fix this issue
	@Ignore
	@Test
	public void testGoodNote() {
		checkNoIssues(defaultSession, goodNote);
	}
}


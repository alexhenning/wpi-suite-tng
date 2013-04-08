package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;


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

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementType;




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
	
	@Before
	public void setUp() throws Exception {
		
		noteBody = "Requirement note body.";
		currentUser = new User("Admin", "Admin", "pass", 1);
		mockSsid = "abc123";
		testproject = new Project("TestProject", "1");
		defaultSession = new Session(currentUser, testproject, mockSsid);

		mainRequirement = new RequirementModel(
				1, // int id
				new ReleaseNumber(1, 1000, testproject), //ReleaseNumber releaseNumber,
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
		
		db = new MockData(new HashSet<Object>());
		db.save(currentUser);
		db.save(mainRequirement);
		
		validator = new RequirementNoteValidator(db);
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
	@Test
	public void testGoodNote() {
		checkNoIssues(defaultSession, goodNote);
	}
}


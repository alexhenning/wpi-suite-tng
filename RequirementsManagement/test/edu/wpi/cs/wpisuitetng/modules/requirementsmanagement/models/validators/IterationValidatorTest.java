/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Date;
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.IterationValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 * @author milk3dfx
 *
 */
public class IterationValidatorTest {
	private IterationValidator validator;
	
	Project testproject;
	Date sDate;
	Date eDate;
	
	Iteration firstIteration;
	Iteration goodIteration;
	Iteration lastIteration;
	
	
	Session defaultSession;
	User bobUser;
	String mockSsid;
	
	Data db;
	
	@Before
	public void setUp() throws Exception {
		bobUser = new User("bob", "bob", "1234", 1);
		
		mockSsid = "abc123";
		//Project(String name, String idNum)
		testproject = new Project("TestProject", "1");
		defaultSession = new Session(bobUser, testproject, mockSsid);
		
		//Iteration (int id, Date startDate, Date endDate, String iterationNumber,Project project);
		firstIteration = new Iteration(1, new Date(1), new Date(1000), "1", testproject);
		goodIteration = new Iteration(100, new Date(1001), new Date(2000), "100", testproject);
		lastIteration = new Iteration(2, new Date(2001), new Date(3000), "2", testproject);
		
		db = new MockData(new HashSet<Object>());
		db.save(firstIteration);
		//db.save(goodIteration);
		db.save(lastIteration);
		validator = new IterationValidator(db);
	}
	
	@After
	public void tearDown() throws Exception {
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
		checkNumIssues(3, defaultSession, goodIteration, Mode.CREATE);
	}
	
	@Test
	public void testNullEndDate(){
		goodIteration.setEndDate(null);
		checkNumIssues(3, defaultSession, goodIteration, Mode.CREATE);
	}
	@Test
	public void testNullEndDateGreaderStartDate(){
		goodIteration.setStartDate(new Date(1900));
		goodIteration.setEndDate(new Date(1100));
		checkNumIssues(1, defaultSession, goodIteration, Mode.CREATE);
	}
}

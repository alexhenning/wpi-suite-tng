package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.*;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.mockdata.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementType;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class RequirementModelEntityManagerTest {

	MockData db;
	User existingUser;
	RequirementModel existingRequirement;
	Session defaultSession;
	String mockSsid;
	RequirementModelEntityManager manager;
	
	RequirementModel newRequirement;
	User bob;
	RequirementModel goodUpdatedRequirement;
	Session adminSession;
	Project testProject;
	Project otherProject;
	RequirementModel otherRequirement;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		User admin = new User("admin", "admin", "1234", 27);
		admin.setRole(Role.ADMIN);
		testProject = new Project("test", "1");
		otherProject = new Project("other", "2");
		mockSsid = "ABC";
		adminSession = new Session(admin, testProject, mockSsid);
		
		existingUser = new User("tim", "tim", "password", 2);
		existingRequirement = new RequirementModel(1, new ReleaseNumber(1,3, testProject), RequirementStatus.NEW,
				RequirementPriority.NONE, "existingReq", "descrip", "0","0", existingUser, null,
				new Date(), new Date(), null, null, null, RequirementType.EPIC );
		
		
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

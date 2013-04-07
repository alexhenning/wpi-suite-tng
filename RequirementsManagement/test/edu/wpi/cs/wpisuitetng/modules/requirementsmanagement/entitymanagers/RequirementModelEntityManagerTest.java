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
 *    
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import static org.junit.Assert.*;

import org.junit.*;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.mockdata.MockData;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

import edu.wpi.cs.wpisuitetng.Session;
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
		/*existingRequirement = new RequirementModel(1, new ReleaseNumber(1,3, testProject), RequirementStatus.NEW,
				RequirementPriority.NONE, "existingReq", "descrip", "0","0", existingUser, null,
				new Date(0), new Date(0), null, null, null, RequirementType.EPIC );
		*/
		otherRequirement = new RequirementModel();
		otherRequirement.setProject(otherProject);
	
		//TODO more setup for tests
		
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	//TODO test entity managers
	@Ignore
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}

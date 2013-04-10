/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * 	 Deniz Ozgoren
 *   Tim DeFreitas    
 *    
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;

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
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.Role;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.entitymanagers.CommentManager;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Comment;
import edu.wpi.cs.wpisuitetng.modules.defecttracker.models.Defect;


public class RequirementModelEntityManagerTest {

	MockData db;
	User deniz;
	Session defaultSession;
	String mockSsid;
	RequirementModelEntityManager manager;	
	Project testProject;

	RequirementModel existingRequirement;
	RequirementModel newRequirement2;
	RequirementModel newRequirement3;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//initial setup
		mockSsid = "abc123";
		deniz = new User("deniz", "dboz", "password", 1);
		testProject = new Project("test", "1");
		defaultSession = new Session(deniz, testProject, mockSsid);

		//create an initial requirement to be put to the database
		existingRequirement = new RequirementModel();
		existingRequirement.setId(1);

		//setup a mock database with current settings and intial requirement		
		db = new MockData(new HashSet<Object>());
		db.save(existingRequirement, testProject);
		db.save(deniz);

		//create a new entity manager
		manager = new RequirementModelEntityManager(db);		
	}

	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testMakeEntity() {
		//check the initially saved requirement on database
		assertNotNull(db.retrieve(RequirementModel.class, "ID", 1));
	}





	@Test(expected=BadRequestException.class)
	public void testMakeBadEntity() throws WPISuiteException {
		newRequirement2 = new RequirementModel();
		newRequirement2.setName(""); 
		manager.makeEntity(defaultSession, newRequirement2.toJSON());
	}

	
	@Test
	public void testGetEntity() throws WPISuiteException {
		RequirementModel[] gotten = manager.getEntity(defaultSession, "1");
		assertSame(existingRequirement, gotten[0]);
	}
	
	
	@Test(expected=NotFoundException.class)
	public void testGetBadId() throws WPISuiteException {
		manager.getEntity(defaultSession, "-1");
		
	}

	
	@Test(expected=NotFoundException.class)
	public void testGetMissingEntity() throws WPISuiteException {
		manager.getEntity(defaultSession, "2");
	}
	
	
	@Test
	public void testGetAll() throws WPISuiteException {
		RequirementModel[] gotten = manager.getAll(defaultSession);
		assertEquals(1, gotten.length);
		assertSame(existingRequirement, gotten[0]);
	}
	
	@Ignore
	@Test
	public void testSave() throws WPISuiteException {
		newRequirement2 = new RequirementModel();
		newRequirement2.setName("name2");
		newRequirement2.setDescription("lorem ipsilum2");
		newRequirement2.setCreator(deniz);
		
		
		manager.makeEntity(defaultSession, newRequirement2.toJSON());
		assertSame(newRequirement2, db.retrieve(RequirementModel.class, "id", 2).get(0));
		assertSame(testProject, newRequirement2.getProject());
	}
	
	




	@Test
	public void testIDincrementing() throws BadRequestException, ConflictException, WPISuiteException{
		assertEquals(1, existingRequirement.getId());

		newRequirement2 = new RequirementModel();
		newRequirement2.setName("name2");
		newRequirement2.setDescription("lorem ipsilum2");
		newRequirement2.setCreator(deniz);		
		RequirementModel created2 = manager.makeEntity(defaultSession, newRequirement2.toJSON());
		assertEquals(2, created2.getId());

		//assertNotNull(db.retrieve(RequirementModel.class, "ID", 2));

	}


	@Test
	public void testMultipleIDincrementing2() throws BadRequestException, ConflictException, WPISuiteException{
		newRequirement2 = new RequirementModel();
		newRequirement2.setName("name2");
		newRequirement2.setDescription("lorem ipsilum2");
		newRequirement2.setCreator(deniz);		
		RequirementModel created2 = manager.makeEntity(defaultSession, newRequirement2.toJSON());		
		assertEquals(2, created2.getId());


		newRequirement3 = new RequirementModel();
		newRequirement3.setName("name3");
		newRequirement3.setDescription("lorem ipsilum3");
		newRequirement3.setCreator(deniz);		
		RequirementModel created3 = manager.makeEntity(defaultSession, newRequirement3.toJSON());		
		assertEquals(3, created3.getId());
	}

	//


}
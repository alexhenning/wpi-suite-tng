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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.editmultiple;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.callbacks.EditFieldOfRequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers.EditMultipleRequirements;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Tests editing multiple requirements at once
 * @author Tim Calvert
 *
 */
public class EditMultipleRequirementsTest {
	
	List<RequirementModel> requirements;
	EditMultipleRequirements edit;
	EditFieldOfRequirementsCallback callback;	
	
	@Before
	public void setUp() {
		requirements = new ArrayList<RequirementModel>();
		edit = new EditMultipleRequirements();
	}
	
	/**
	 * Edits the name field of one requirement and tests that it changed
	 *
	 */
	@Test
	public void testEditingOneRequirementName() {
		requirements.add(new RequirementModel());
		edit.setRequirements(requirements);
		assertEquals("", requirements.get(0).getName());
		callback = new EditFieldOfRequirementsCallback("name", "test name");
		edit.editRequirements(callback);
		assertEquals("test name", requirements.get(0).getName());
	}
	
	/**
	 * Edits the name field of four requirements and test if they changed
	 * and makes sure they are all equal to each other
	 *
	 */
	@Test
	public void testEditingMultipleRequirementsName() {
		for(int i = 0; i < 4; i++) {
			requirements.add(new RequirementModel());
		}
		edit.setRequirements(requirements);
		for(int i = 0; i < 4; i++) {
			assertEquals("", requirements.get(i).getName());
		}
		callback = new EditFieldOfRequirementsCallback("name", "test name");
		edit.editRequirements(callback);
		for(int i = 0; i < 4; i++) {
			assertEquals("test name", requirements.get(i).getName());
		}
		for(int i = 0; i < 3; i++) {
			assertEquals(requirements.get(i).getName(), requirements.get(i + 1).getName());
		}
	}
	
	/**
	 * Edits the description field of four requirements and test if they changed
	 * and makes sure they are all equal to each other
	 *
	 */
	@Test
	public void testEditingMultipleRequirementsDesc() {
		for(int i = 0; i < 4; i++) {
			requirements.add(new RequirementModel());
		}
		edit.setRequirements(requirements);
		for(int i = 0; i < 4; i++) {
			assertEquals("", requirements.get(i).getDescription());
		}
		callback = new EditFieldOfRequirementsCallback("description", "test description");
		edit.editRequirements(callback);
		for(int i = 0; i < 4; i++) {
			assertEquals("test description", requirements.get(i).getDescription());
		}
		for(int i = 0; i < 3; i++) {
			assertEquals(requirements.get(i).getDescription(), requirements.get(i + 1).getDescription());
		}
	}

}

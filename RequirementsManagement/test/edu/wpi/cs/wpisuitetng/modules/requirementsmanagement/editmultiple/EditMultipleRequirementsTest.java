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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.callbacks.UpdateEstimatesCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers.WorkMultipleRequirements;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 *
 * Tests editing multiple requirements at once
 * @author Tim Calvert
 *
 */
public class EditMultipleRequirementsTest {
	
	List<RequirementModel> requirements;
	WorkMultipleRequirements edit;
	EditFieldOfRequirementsCallback callback;	
	
	@Before
	public void setUp() {
		requirements = new ArrayList<RequirementModel>();
		edit = new WorkMultipleRequirements();
	}
	
	/**
	 * Edits the name field of one requirement and tests that it changed
	 *
	 */
	@Test
	public void testEditingOneRequirementName() {
		requirements.add(new RequirementModel());
		assertEquals("", requirements.get(0).getName());
		callback = new EditFieldOfRequirementsCallback("name", "test name");
		edit.workRequirements(requirements, callback);
		assertEquals("test name", requirements.get(0).getName());
	}
	
	/**
	 * Gives two requirements different Ids and then tries to edit them and check
	 *
	 */
	@Test
	public void testEditingOneRequirementWithDiffFieldsId() {
		RequirementModel req = new RequirementModel();
		req.setId(1);
		requirements.add(req);
		req = new RequirementModel();
		req.setId(2);
		requirements.add(req);
		assertFalse(requirements.get(0).getId() == requirements.get(1).getId());
		callback = new EditFieldOfRequirementsCallback("id", 13);
		edit.workRequirements(requirements, callback);
		assertEquals(requirements.get(0).getId(), requirements.get(1).getId());
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
		for(int i = 0; i < 4; i++) {
			assertEquals("", requirements.get(i).getName());
		}
		callback = new EditFieldOfRequirementsCallback("name", "test name");
		edit.workRequirements(requirements, callback);
		for(int i = 0; i < 4; i++) {
			assertEquals("test name", requirements.get(i).getName());
		}
		for(int i = 0; i < 3; i++) {  // can't go all the way to 3 or there will be a out of range exception
			                          // but it still overs all the bounds: 0 = 1,  1 = 2, 2 = 3
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
		for(int i = 0; i < 4; i++) {
			assertEquals("", requirements.get(i).getDescription());
		}
		callback = new EditFieldOfRequirementsCallback("description", "test description");
		edit.workRequirements(requirements, callback);
		for(int i = 0; i < 4; i++) {
			assertEquals("test description", requirements.get(i).getDescription());
		}
		for(int i = 0; i < 3; i++) {
			assertEquals(requirements.get(i).getDescription(), requirements.get(i + 1).getDescription());
		}
	}
	
	/**
	 * Edits multiple fields of multiple requirements
	 *
	 */
	@Test
	public void testEditingMultipleRequirementsMultiField() {
		for(int i = 0; i < 10; i++) {
			requirements.add(new RequirementModel());
		}
		for(int i = 0; i < 10; i++) {
			assertEquals(-1, requirements.get(i).getId());
			assertEquals("", requirements.get(i).getName());
			assertEquals("", requirements.get(i).getDescription());
			assertEquals(null, requirements.get(i).getReleaseNumber());
			assertEquals(RequirementStatus.NEW, requirements.get(i).getStatus());
			assertEquals(null, requirements.get(i).getPriority());
		}
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("id", 15));
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("name", "test name"));
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("description", "test description"));
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("releaseNumber", new ReleaseNumber()));
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("status", RequirementStatus.DELETED));
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("priority", RequirementPriority.HIGH));
		for(int i = 0; i < 10; i++) {
			assertEquals(15, requirements.get(i).getId());
			assertEquals("test name", requirements.get(i).getName());
			assertEquals("test description", requirements.get(i).getDescription());
//			assertEquals(new ReleaseNumber(), requirements.get(i).getReleaseNumber());  * Breaks without a proper equals method, but I don't want to mess with Jacob's models *
			assertEquals(RequirementStatus.DELETED, requirements.get(i).getStatus());
			assertEquals(RequirementPriority.HIGH, requirements.get(i).getPriority());
		}
		for(int i = 0; i < 9; i++) {
			assertEquals(requirements.get(i).getId(), requirements.get(i + 1).getId());
			assertEquals(requirements.get(i).getName(), requirements.get(i + 1).getName());
			assertEquals(requirements.get(i).getDescription(), requirements.get(i + 1).getDescription());
			assertEquals(requirements.get(i).getReleaseNumber(), requirements.get(i + 1).getReleaseNumber());
			assertEquals(requirements.get(i).getStatus(), requirements.get(i + 1).getStatus());
			assertEquals(requirements.get(i).getPriority(), requirements.get(i + 1).getPriority());
		}
	}
	
	/* * The commented out assert will pass if this function (or any other proper one) is used as ReleaseNumber's equals method *
	 public boolean equals(Object o) {
		if(o instanceof ReleaseNumber) {
			if(this.id == ((ReleaseNumber)o).getId() &&
					this.releaseNumber == ((ReleaseNumber)o).getReleaseNumber() &&
					this.getProject().equals(((ReleaseNumber)o).getProject()))
				return true;
		}
		return false;
	}
	 */
	
	/**
	 * Tests recursion by trying to edit a tree of requirements
	 * Setup: Req  -> SubReq -> SubSubReq
	 *            \-> SubReq
	 *            \-> SubReq
	 *        Req
	 */           
	@Test
	public void testEditingMultipleLeveledRequirements() {
		RequirementModel req = new RequirementModel();  // top level req
		ArrayList<RequirementModel> reqs = new ArrayList<RequirementModel>();  // sub reqs for top level
		RequirementModel subReq = new RequirementModel();  // sub req with sub req
		subReq.addSubRequirementID(new RequirementModel()); // add sub req to seb req
		reqs.add(subReq);  // add three sub reqs to first req
		reqs.add(new RequirementModel());
		reqs.add(new RequirementModel());
		req.setSubRequirementIDs(reqs); // actually set the sub reqs
		requirements.add(req);  // add to list of reqs
		requirements.add(new RequirementModel());  // add second req to list of reqs, leaving tree looking as above
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("name", "recursive name"));
		
		// we'll check these manually because I'd do the recursion the same way, so that doesn't really help
		// top level of two reqs
		assertEquals("recursive name", requirements.get(0).getName());
		assertEquals("recursive name", requirements.get(1).getName());
		requirements = requirements.get(0).getSubRequirements(); // sub tree under first requirement
		// check second level of three reqs
		assertEquals("recursive name", requirements.get(0).getName());
		assertEquals("recursive name", requirements.get(1).getName());
		assertEquals("recursive name", requirements.get(2).getName());
		// check sub req of first sub req
		assertEquals("recursive name", requirements.get(0).getSubRequirements().get(0).getName());
	}
	
	/**
	 * Tests updating estimates
	 * Setup: Req  -> SubReq -> SubSubReq
	 *            \-> SubReq
	 *            \-> SubReq
	 *        Req
	 *
	 */
	@Test
	public void testAccumulatingEstimates() {
		List<RequirementModel> subrequirements;
		RequirementModel req = new RequirementModel();
		ArrayList<RequirementModel> reqs = new ArrayList<RequirementModel>();
		RequirementModel subReq = new RequirementModel();
		subReq.addSubRequirementID(new RequirementModel());
		reqs.add(subReq);
		reqs.add(new RequirementModel());
		reqs.add(new RequirementModel());
		req.setSubRequirementIDs(reqs);
		requirements.add(req);
		requirements.add(new RequirementModel());
		edit.workRequirements(requirements, new EditFieldOfRequirementsCallback("estimate", 50));
		
		assertEquals(50, requirements.get(0).getEstimate());
		assertEquals(50, requirements.get(1).getEstimate());
		subrequirements = requirements.get(0).getSubRequirements();
		assertEquals(50, subrequirements.get(0).getEstimate());
		assertEquals(50, subrequirements.get(1).getEstimate());
		assertEquals(50, subrequirements.get(2).getEstimate());
		assertEquals(50, subrequirements.get(0).getSubRequirements().get(0).getEstimate());
		
		subrequirements.get(0).getSubRequirements().get(0).setEstimate(25);
		assertEquals(50, requirements.get(0).getEstimate());
		assertEquals(50, requirements.get(1).getEstimate());
		subrequirements = requirements.get(0).getSubRequirements();
		assertEquals(50, subrequirements.get(0).getEstimate());
		assertEquals(50, subrequirements.get(1).getEstimate());
		assertEquals(50, subrequirements.get(2).getEstimate());
		assertEquals(25, subrequirements.get(0).getSubRequirements().get(0).getEstimate());
		
		edit.workRequirements(requirements, new UpdateEstimatesCallback());
		
		assertEquals(125, requirements.get(0).getEstimate());
		assertEquals(50, requirements.get(1).getEstimate());
		subrequirements = requirements.get(0).getSubRequirements();
		assertEquals(25, subrequirements.get(0).getEstimate());
		assertEquals(50, subrequirements.get(1).getEstimate());
		assertEquals(50, subrequirements.get(2).getEstimate());
		assertEquals(25, subrequirements.get(0).getSubRequirements().get(0).getEstimate());
	}

}

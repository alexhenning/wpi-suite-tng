/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tim Calvert
 *    David Modica
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEventObjectType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.RequirementModelValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 * Provides database interaction for the RequirementModel class
 * @author Tim Calvert
 * @author David Modica
 *
 */
public class RequirementModelEntityManager implements EntityManager<RequirementModel> {
	Queue<Integer> availableIds;  // A queue of any Ids that are available for recycling
	Data db;
	RequirementModelValidator validator;
	ModelMapper updateMapper;
	
	public RequirementModelEntityManager(Data db) {
		this.db = db;
		validator = new RequirementModelValidator(db);
		updateMapper = new ModelMapper();
		updateMapper.getBlacklist().add("project"); // don't allow project changing
		availableIds = new LinkedList<Integer>();
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#makeEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public RequirementModel makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		
		final RequirementModel newRequirementModel = RequirementModel.fromJSON(content);
		
		if(availableIds.isEmpty()) {
			newRequirementModel.setId(Count() + 1);
		} else {
			try {
			newRequirementModel.setId(availableIds.remove().intValue());
			} catch (NoSuchElementException e) {
				newRequirementModel.setId(Count() + 1);
			}
		}
		
		List<ValidationIssue> issues = validator.validate(s, newRequirementModel, Mode.CREATE);
		if(issues.size() > 0) {
//			for (ValidationIssue issue : issues) {
//				System.out.println("Validation issue: " + issue.getMessage());
//			}
			throw new BadRequestException();
		}
		
		ProjectEvent creation = ProjectEvent.createProjectCreationEvent(ProjectEventObjectType.REQUIREMENT, newRequirementModel.getId()+"");
		// make sure the user exists
		creation.setUser((User) db.retrieve(User.class, "username", s.getUsername()).get(0));
		creation.setId(CountEvents() + 1);

		if(!db.save(newRequirementModel, s.getProject()) || !db.save(creation, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return newRequirementModel;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public RequirementModel[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}
		RequirementModel[] requirements = null;
		try {
			requirements = db.retrieve(RequirementModel.class, "id", intId, s.getProject()).toArray(new RequirementModel[0]);
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}
		if(requirements.length < 1 || requirements[0] == null) {
			throw new NotFoundException();
		}
		return requirements;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#getAll(edu.wpi.cs.wpisuitetng.Session)
	 */
	@Override
	public RequirementModel[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new RequirementModel(), s.getProject()).toArray(new RequirementModel[0]);
	}


	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#update(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public RequirementModel update(Session s, String content)
			throws WPISuiteException, NotFoundException {		
		
		/* [This comment is from DefectManager.  The problem still applies, so it must be worked around]
		 * Because of the disconnected objects problem in db4o, we can't just save updatedDefect.
		 * We have to get the original defect from db4o, copy properties from updatedDefect,
		 * then save the original defect again.
		 */
		
		RequirementModel updatedRequirement = RequirementModel.fromJSON(content);
		
		List<ValidationIssue> issues = validator.validate(s, updatedRequirement, Mode.EDIT);
		if(issues.size() > 0) {
			throw new BadRequestException();
		}
		
		RequirementModel existingRequirement = validator.getLastExistingRequirement();
		Date originalLastModified = existingRequirement.getLastModifiedDate();
		
//		RequirementChangeset changeset = new RequirementChangeset();
//		// make sure the user exists
//		changeset.setUser((User) db.retrieve(User.class, "username", s.getUsername()).get(0));
//		RequirementChangesetCallback callback = new RequirementChangesetCallback(changeset);
		ProjectEvent changeset = ProjectEvent.createProjectChangesetEvent(ProjectEventObjectType.REQUIREMENT, existingRequirement.getId()+"");
		// make sure the user exists
		changeset.setUser((User) db.retrieve(User.class, "username", s.getUsername()).get(0));
		changeset.setId(CountEvents() + 1);
		RequirementModelEventCallback callback = new RequirementModelEventCallback(changeset);
		
		// copy over values
		updateMapper.map(updatedRequirement, existingRequirement, callback);
		
		if(changeset.getChanges().size() == 0) {
			// nothing changes, don't bother saving it
			existingRequirement.setLastModifiedDate(originalLastModified);
		} else {
			// add changeset to events
//			existingRequirement.getEvents().add(changeset);
//			DB.createProjectEvent(changeset, new AddProjectEventController());
			if(!db.save(existingRequirement, s.getProject()) || !db.save(changeset, s.getProject()) || !db.save(existingRequirement.getEvents())) {
				throw new WPISuiteException();
			}
		}
		
		return existingRequirement;
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#save(edu.wpi.cs.wpisuitetng.Session, edu.wpi.cs.wpisuitetng.modules.Model)
	 */
	@Override
	public void save(Session s, RequirementModel model)
			throws WPISuiteException {
		db.save(model, s.getProject());		
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteEntity(edu.wpi.cs.wpisuitetng.Session, java.lang.String)
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		availableIds.add(new Integer(id));
		return (db.delete(getEntity(s, id)[0]) != null);
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#deleteAll(edu.wpi.cs.wpisuitetng.Session)
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new RequirementModel(), s.getProject());
		availableIds.clear();
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new RequirementModel()).size();
	}
	
	public int CountEvents() throws WPISuiteException {
		return db.retrieveAll(new ProjectEvent()).size();
	}
	
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedGet(edu.wpi.cs.wpisuitetng.Session, java.lang.String[])
	 */
	@Override
	public String advancedGet(Session s, String[] args)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPut(edu.wpi.cs.wpisuitetng.Session, java.lang.String[], java.lang.String)
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.EntityManager#advancedPost(edu.wpi.cs.wpisuitetng.Session, java.lang.String, java.lang.String)
	 */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}
	
}

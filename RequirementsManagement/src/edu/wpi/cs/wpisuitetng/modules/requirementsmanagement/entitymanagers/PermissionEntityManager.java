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
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.util.List;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.RequirementNoteValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 * Entity manager for Permissions
 *
 * @author vpatara
 *
 */
public class PermissionEntityManager implements
		EntityManager<Permissions> {
	
	private final Data db;
	private final Gson gson;
	private final RequirementNoteValidator validator;
	
	public PermissionEntityManager(Data data) {
		db = data;
		gson = new Gson();
		validator = new RequirementNoteValidator(data);
	}

	@Override
	public Permissions makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		Permissions newNote = gson.fromJson(content, Permissions.class);
		
		// TODO: need to set up validation
//		List<ValidationIssue> issues = validator.validate(s, newNote);
//		if(issues.size() > 0) {
//			throw new BadRequestException();
//		}
//		
//		Permissions requirement = validator.getLastExistingRequirement();
//		requirement.getEvents().add(newNote);
//		db.save(requirement, s.getProject());
//		db.save(requirement.getEvents());
		
		return newNote;
	}

	@Override
	public Permissions[] getEntity(Session s, String id)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public Permissions[] getAll(Session s) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public Permissions update(Session s, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void save(Session s, Permissions model) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public void deleteAll(Session s) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public int Count() throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

}
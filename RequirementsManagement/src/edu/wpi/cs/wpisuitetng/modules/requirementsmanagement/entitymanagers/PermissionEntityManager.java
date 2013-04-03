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

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.PermissionsValidator;
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
	private final PermissionsValidator validator;
	private final ModelMapper updateMapper;
	
	public PermissionEntityManager(Data data) {
		db = data;
		validator = new PermissionsValidator(data);
		updateMapper = new ModelMapper();
		updateMapper.getBlacklist().add("project");
	}

	/**
	 * Creates a user permission
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public Permissions makeEntity(Session s, String content)
			throws WPISuiteException {
		Permissions newPermission = Permissions.fromJSON(content);
		
		List<ValidationIssue> issues = validator.validate(s, newPermission, Mode.CREATE);
		if(issues.size() > 0) {
			throw new BadRequestException();
		}
		
		if(!db.save(newPermission, s.getProject())) {
			throw new WPISuiteException();
		}

		return newPermission;
	}

	/**
	 * Retrieves a user permission from the database by username
	 *
	 * @param s
	 * @param username
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public Permissions[] getEntity(Session s, String username)
			throws WPISuiteException {
		Permissions[] Permissions = null;
		try {
			Permissions = db.retrieve(Permissions.class, "username", username, s.getProject()).toArray(new Permissions[0]);
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}
		
		if(Permissions.length < 1 || Permissions[0] == null) {
			throw new NotFoundException();
		}
		return Permissions;
	}

	/**
	 * Retrieves all the user permissions from the database
	 *
	 * @param s
	 * @return
	 */
	@Override
	public Permissions[] getAll(Session s) {
		return db.retrieveAll(new Permissions(), s.getProject()).toArray(new Permissions[0]);
	}

	/**
	 * Updates the user permission in the database
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public Permissions update(Session s, String content)
			throws WPISuiteException {
		Permissions updatedPermissions = Permissions.fromJSON(content);
		
		List<ValidationIssue> issues = validator.validate(s, updatedPermissions, Mode.EDIT);
		if(issues.size() > 0) {
			throw new BadRequestException();
		}
		
		Permissions existingPermissions = validator.getLastExistingPermissions();
		
		updateMapper.map(updatedPermissions, existingPermissions);
		
		if(!db.save(existingPermissions, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return existingPermissions;
	}

	/**
	 * Saves a permission into the database
	 *
	 * @param s
	 * @param model
	 * @throws NotImplementedException
	 */
	@Override
	public void save(Session s, Permissions model) {
		db.save(model, s.getProject());
	}

	/**
	 * Deletes a permission by username
	 *
	 * @param s
	 * @param username
	 * @return
	 * @throws NotImplementedException
	 */
	@Override
	public boolean deleteEntity(Session s, String username) throws WPISuiteException {
		return (db.delete(getEntity(s, username)[0]) != null);
	}

	/**
	 * Deletes all the user permissions in the database
	 *
	 * @param s
	 * @throws NotImplementedException
	 */
	@Override
	public void deleteAll(Session s) {
		db.deleteAll(new Permissions(), s.getProject());
	}

	/**
	 * Returns the number of existing user-permission pairs
	 *
	 * @return the number of existing user-permission pairs
	 * @throws NotImplementedException
	 */
	@Override
	public int Count() {
		return db.retrieveAll(new Permissions()).size();
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws NotImplementedException {
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

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
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEventObjectType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.PermissionsValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 * Entity manager for Permissions
 *
 * @author vpatara
 * @author Tim
 */
public class PermissionEntityManager implements
		EntityManager<Permissions> {
	
	/** database */
	private final Data db;
	/** validator for permissions */
	private final PermissionsValidator validator;
	/** model mapper for copying properties */
	private final ModelMapper updateMapper;
	
	/**
	 * Constructor
	 * @param data dataabase
	 */
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
		
		ProjectEvent creation = ProjectEvent.createProjectCreationEvent(ProjectEventObjectType.PERMISSIONS, newPermission.getUsername()+"");
		// make sure the user exists
		creation.setUser((User) db.retrieve(User.class, "username", s.getUsername()).get(0));
		creation.setId(CountEvents() + 1);

		if(!db.save(newPermission, s.getProject()) || !db.save(creation, s.getProject())) {
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
		
		ProjectEvent changeset = ProjectEvent.createProjectChangesetEvent(ProjectEventObjectType.PERMISSIONS, existingPermissions.getUsername()+"");
		// make sure the user exists
		changeset.setUser((User) db.retrieve(User.class, "username", s.getUsername()).get(0));
		changeset.setId(CountEvents() + 1);
		PermissionEventCallback callback = new PermissionEventCallback(changeset);

		updateMapper.map(updatedPermissions, existingPermissions, callback);
		
		if(!db.save(existingPermissions, s.getProject()) || !db.save(changeset, s.getProject())) {
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

	/**
	 * return the number of permissions in the database
	 *
	 * @return the number of permissions in the database
	 * @throws WPISuiteException
	 */
	public int CountEvents() throws WPISuiteException {
		return db.retrieveAll(new ProjectEvent()).size();
	}
	
	/**
	 * This is not implemented
	 *
	 * @param s
	 * @param args
	 * @return
	 * @throws NotImplementedException
	 */
	@Override
	public String advancedGet(Session s, String[] args)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	/**
	 * This is not implemented
	 *
	 * @param s
	 * @param args
	 * @param content
	 * @return
	 * @throws NotImplementedException
	 */
	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

	/**
	 * This is not implemented
	 *
	 * @param s
	 * @param string
	 * @param content
	 * @return
	 * @throws NotImplementedException
	 */
	@Override
	public String advancedPost(Session s, String string, String content)
			throws NotImplementedException {
		throw new NotImplementedException();
	}

}

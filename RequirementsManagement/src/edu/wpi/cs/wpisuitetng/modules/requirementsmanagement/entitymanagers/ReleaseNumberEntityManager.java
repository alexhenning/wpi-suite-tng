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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.IterationValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ReleaseNumberValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 *
 * Entity manager for release numbers
 * @author James
 *
 */
public class ReleaseNumberEntityManager implements EntityManager<ReleaseNumber> {

	/** the database */
	private final Data db;
	/** a release number validator */
	private final ReleaseNumberValidator validator;
	/** model mapper for copying properties */
	private final ModelMapper updateMapper;

	/**
	 * Constructor
	 * @param data database
	 */
	public ReleaseNumberEntityManager(Data data) {
		db = data;
		validator = new ReleaseNumberValidator(db);
		updateMapper = new ModelMapper();
		updateMapper.getBlacklist().add("project");
	}

	/**
	 * make a release number to add to the database
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws BadRequestException
	 * @throws ConflictException
	 * @throws WPISuiteException
	 */
	@Override
	public ReleaseNumber makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		
		ReleaseNumber newReleaseNumber = ReleaseNumber.fromJSON(content);

		newReleaseNumber.setId(Count() + 1);

		List<ValidationIssue> issues = validator.validate(s, newReleaseNumber, Mode.CREATE);
		if(issues.size() > 0) {
			for(ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage() + "\n\tField name: " + issue.getFieldName());
			}
			throw new BadRequestException();
		}

		if(!db.save(newReleaseNumber, s.getProject())) {
			throw new WPISuiteException();
		}

		return newReleaseNumber;
	}

	/**
	 * get release number by id
	 *
	 * @param s
	 * @param id
	 * @return
	 * @throws NotFoundException
	 * @throws WPISuiteException
	 */
	@Override
	public ReleaseNumber[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}

		ReleaseNumber[] releaseNumbers = null;
		try {
			releaseNumbers = db.retrieve(ReleaseNumber.class, "id", intId, s.getProject()).toArray(new ReleaseNumber[0]);
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}

		if(releaseNumbers.length < 1 || releaseNumbers[0] == null) {
			throw new NotFoundException();
		}
		return releaseNumbers;
	}

	/**
	 * get all release numbers in the database
	 *
	 * @param s
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public ReleaseNumber[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new ReleaseNumber(), s.getProject()).toArray(new ReleaseNumber[0]);
	}

	/**
	 * update a release number in the database
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public ReleaseNumber update(Session s, String content)
			throws WPISuiteException {
		ReleaseNumber updatedReleaseNumber = ReleaseNumber.fromJSON(content);

		List<ValidationIssue> issues = validator.validate(s, updatedReleaseNumber, Mode.EDIT);
		if(issues.size() > 0) {
			throw new BadRequestException();
		}

		ReleaseNumber existingReleaseNumber = validator.getLastExistingReleaseNumber();

		updateMapper.map(updatedReleaseNumber, existingReleaseNumber);

		if(!db.save(existingReleaseNumber, s.getProject())) {
			throw new WPISuiteException();
		}

		return existingReleaseNumber;
	}

	/**
	 * save a releases number
	 *
	 * @param s
	 * @param model
	 * @throws WPISuiteException
	 */
	@Override
	public void save(Session s, ReleaseNumber model) throws WPISuiteException {
		db.save(model, s.getProject());
	}

	/**
	 * delete a release number by id
	 *
	 * @param s
	 * @param id
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return (db.delete(getEntity(s, id)[0]) != null);
	}

	/**
	 * this is not implemented
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
	 * delete all release numbers
	 *
	 * @param s
	 * @throws WPISuiteException
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new ReleaseNumber(), s.getProject());
	}

	/**
	 * get the number of release numbers
	 *
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new ReleaseNumber()).size();
	}

	/**
	 * this is not implemented
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
	 * this is not implemented
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

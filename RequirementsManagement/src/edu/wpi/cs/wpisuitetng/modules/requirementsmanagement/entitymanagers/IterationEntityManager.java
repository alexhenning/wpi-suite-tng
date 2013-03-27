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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.IterationValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

public class IterationEntityManager implements EntityManager<Iteration> {
	Queue<Integer> availableIds;  // A queue of any Ids that are available for recycling
	Data db;
	IterationValidator validator;
	ModelMapper updateMapper;
	
	public IterationEntityManager(Data db) {
		this.db = db;
		validator = new IterationValidator(db);
		updateMapper = new ModelMapper();
		updateMapper.getBlacklist().add("project"); // don't allow project changing
		availableIds = new LinkedList<Integer>();
	}

	@Override
	public Iteration makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		final Iteration newIteration = Iteration.fromJSON(content);

		if(availableIds.isEmpty()) {
			newIteration.setId(Count() + 1);
		} else {
			try {
				newIteration.setId(availableIds.remove().intValue());
			} catch (NoSuchElementException e) {
				newIteration.setId(Count() + 1);
			}
		}

		List<ValidationIssue> issues = validator.validate(s, newIteration, Mode.CREATE);
		if(issues.size() > 0) {
			for (ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage());
			}
			throw new BadRequestException();
		}

		if(!db.save(newIteration, s.getProject())) {
			throw new WPISuiteException();
		}

		return newIteration;
	}

	@Override
	public Iteration[] getEntity(Session s, String id)
			throws NotFoundException, WPISuiteException {
		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}
		Iteration[] iteration = null;
		try {
			iteration = db.retrieve(Iteration.class, "id", intId, s.getProject()).toArray(new Iteration[0]);
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}
		if(iteration.length < 1 || iteration[0] == null) {
			throw new NotFoundException();
		}
		return iteration;
	}

	@Override
	public Iteration[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new Iteration(), s.getProject()).toArray(new Iteration[0]);

	}

	@Override
	public Iteration update(Session s, String content) throws WPISuiteException, NotFoundException {		
		
		/* [This comment is from DefectManager.  The problem still applies, so it must be worked around]
		 * Because of the disconnected objects problem in db4o, we can't just save updatedDefect.
		 * We have to get the original defect from db4o, copy properties from updatedDefect,
		 * then save the original defect again.
		 */
		
		Iteration updatedIteration = Iteration.fromJSON(content);
		
		List<ValidationIssue> issues = validator.validate(s, updatedIteration, Mode.EDIT);
		if(issues.size() > 0) {
			throw new BadRequestException();
		}
		
		Iteration existingIteration = validator.getLastExistingIteration();
		/*Date originalLastModified = existingIteration.getLastModifiedDate();
		
		IterationChangeset changeset = new IterationChangeset();
		// make sure the user exists
		changeset.setUser((User) db.retrieve(User.class, "username", s.getUsername()).get(0));
		IterationChangesetCallback callback = new IterationChangesetCallback(changeset);
		
		// copy over values
		updateMapper.map(updatedIteration, existingIteration, callback);
		
		if(changeset.getChanges().size() == 0) {
			// nothing changes, don't bother saving it
			existingIteration.setLastModifiedDate(originalLastModified);
		} else {
			// add changeset to events
			existingIteration.getEvents().add(changeset);
			if(!db.save(existingIteration, s.getProject()) || !db.save(existingIteration.getEvents())) {
				throw new WPISuiteException();
			}
		}*/
		
		return existingIteration;
	}

	@Override
	public void save(Session s, Iteration model) throws WPISuiteException {
		db.save(model, s.getProject());	
		
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		availableIds.add(new Integer(id));
		return (db.delete(getEntity(s, id)[0]) != null);
	}

	@Override
	public String advancedGet(Session s, String[] args)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Iteration(), s.getProject());
		availableIds.clear();
		
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Iteration()).size();
	}

	@Override
	public String advancedPut(Session s, String[] args, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content)
			throws WPISuiteException {
		throw new NotImplementedException();
	}
}

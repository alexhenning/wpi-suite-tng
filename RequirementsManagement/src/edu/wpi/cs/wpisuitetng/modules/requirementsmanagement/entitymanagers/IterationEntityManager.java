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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.IterationValidator;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;


/**
 *
 * Entity manager for Iterations
 * @author Tim
 *
 */
public class IterationEntityManager implements EntityManager<Iteration> {
	private final Data db;
	private final IterationValidator validator;
	private final ModelMapper updateMapper;
	
	/**
	 * Default constructor
	 * @param data database
	 */
	public IterationEntityManager(Data data) {
		db = data;
		validator = new IterationValidator(db);
		updateMapper = new ModelMapper();
		updateMapper.getBlacklist().add("project");
	}

	/**
	 * Makes an Iteration to add to the database
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws BadRequestException
	 * @throws ConflictException
	 * @throws WPISuiteException
	 */
	@Override
	public Iteration makeEntity(Session s, String content) throws BadRequestException,
			ConflictException, WPISuiteException {
		Iteration newIteration = Iteration.fromJSON(content);
		
		newIteration.setId(Count() + 1);
		
		List<ValidationIssue> issues = validator.validate(s, newIteration, Mode.CREATE);
		if(issues.size() > 0) {
			for(ValidationIssue issue : issues) {
				System.out.println("Validation issue: " + issue.getMessage());
			}
			throw new BadRequestException();
		}
		
		if(!db.save(newIteration, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return newIteration;
	}

	/**
	 * Retrieves an iteration by id
	 *
	 * @param s
	 * @param id
	 * @return
	 * @throws NotFoundException
	 * @throws WPISuiteException
	 */
	@Override
	public Iteration[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {

		final int intId = Integer.parseInt(id);
		if(intId < 1) {
			throw new NotFoundException();
		}

		Iteration[] iterations = null;
		try {
			iterations = db.retrieve(Iteration.class, "id", intId, s.getProject()).toArray(new Iteration[0]);
		} catch (WPISuiteException e) {
			e.printStackTrace();
		}
		
		if(iterations.length < 1 || iterations[0] == null) {
			throw new NotFoundException();
		}
		return iterations;
	}

	/**
	 * Retrieves all iterations in the database
	 *
	 * @param s
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public Iteration[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new Iteration(), s.getProject()).toArray(new Iteration[0]);
	}

	/**
	 * Updates an iteration in the database
	 *
	 * @param s
	 * @param content
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public Iteration update(Session s, String content) throws WPISuiteException {
		Iteration updatedIteration = Iteration.fromJSON(content);
		
		List<ValidationIssue> issues = validator.validate(s, updatedIteration, Mode.EDIT);
		if(issues.size() > 0) {
			throw new BadRequestException();
		}
		
		Iteration existingIteration = validator.getLastExistingIteration();
		
		updateMapper.map(updatedIteration, existingIteration);
		
		if(!db.save(existingIteration, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return existingIteration;
	}

	/**
	 * Saves an iteration
	 *
	 * @param s
	 * @param model
	 * @throws WPISuiteException
	 */
	@Override
	public void save(Session s, Iteration model) throws WPISuiteException {
		db.save(model, s.getProject());
	}

	/**
	 * Deletes an iteration by id
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
	 * Deletes all iterations in the db
	 *
	 * @param s
	 * @throws WPISuiteException
	 */
	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new Iteration(), s.getProject());
	}
	
	/**
	 * Returns the number of iterations in the db
	 *
	 * @return
	 * @throws WPISuiteException
	 */
	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Iteration()).size();
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

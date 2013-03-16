/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.util.List;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 * @author Tim
 *
 */
public class RequirementModelEntityManager implements EntityManager<RequirementModel>
{
	Data db;
	
	public RequirementModelEntityManager(Data db) {
		this.db = db;
	}

	@Override
	public RequirementModel makeEntity(Session s, String content)
			throws BadRequestException, ConflictException, WPISuiteException {
		
		final RequirementModel newRequirementModel = RequirementModel.fromJSON(content);
		
		if(!db.save(newRequirementModel, s.getProject())) {
			throw new WPISuiteException();
		}
		
		return newRequirementModel;
	}

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

	@Override
	public RequirementModel[] getAll(Session s) throws WPISuiteException {
		return db.retrieveAll(new RequirementModel(), s.getProject()).toArray(new RequirementModel[0]);
	}

	@Override
	public RequirementModel update(Session s, String content)
			throws WPISuiteException {
		
	}

	@Override
	public void save(Session s, RequirementModel model)
			throws WPISuiteException {
		db.save(model, s.getProject());		
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		return (db.delete(getEntity(s, id)[0]) != null);
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		db.deleteAll(new RequirementModel(), s.getProject());
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new RequirementModel()).size();
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

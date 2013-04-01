
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * @author William Terry
 *
 */
public class Permissions extends AbstractModel {
	
	//TODO Do we want a transaction log for the Permissions data?
	
	private HashMap<User, PermissionLevel> permissionMapping = new HashMap<User, PermissionLevel>();
	private Project project;

	public Permissions(Project project, User admin) {
		this.permissionMapping.put(admin, PermissionLevel.EDIT_ALL);
		this.setProject(project);
	}

	/**
	 * adds user with the appropriate permissions level for a project manager
	 *
	 * @param user
	 */
	public void addManager(User user){
		permissionMapping.put(user, PermissionLevel.EDIT_ALL);
	}
	
	/**
	 * adds a user with the appropriate permissions level for a team member
	 *
	 * @param user
	 */
	public void addTeamMember(User user){
		permissionMapping.put(user, PermissionLevel.EDIT_LIMITED);
	}
	
	/**
	 * adds a user with the appropriate permissions level for a customer
	 *
	 * @param user
	 */
	public void addCustomer(User user){
		permissionMapping.put(user, PermissionLevel.VIEW);
	}

	/**
	 * returns the permissions currently granted to a given user
	 *
	 * @param user
	 * @return PermissionLevel
	 */
	public PermissionLevel getPermissionLevel(User user){
		if(permissionMapping.containsKey(user)) return permissionMapping.get(user);
		else return PermissionLevel.NONE;
	}
	
	/**
	 * Remove the permissions for a given user
	 *
	 * @param user
	 */
	public void removePermissions(User user){
		permissionMapping.remove(user);
	}
	
	/**
	 * Change the permissions level for a given user
	 *
	 * @param user
	 * @param newLevel
	 */
	public void changePermissions(User user, PermissionLevel newLevel){
		permissionMapping.remove(user);
		permissionMapping.put(user, newLevel);
	}
	
	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#save()
	 */
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#delete()
	 */
	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, Iteration.class);
		return json;
	}
	
	@Override
	public String toString() {
		return toJSON();
	}

	/**
	 * @param json Json string to parse containing Permissions
	 * @return The Iteration given by json
	 */
	public static Permissions fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
		return builder.create().fromJson(json, Permissions.class);
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		return (o.getClass() == this.getClass());
	}

}

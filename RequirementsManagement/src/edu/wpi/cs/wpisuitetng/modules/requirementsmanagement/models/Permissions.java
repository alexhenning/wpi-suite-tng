
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.Permission;
import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * @author William Terry
 *
 */
public class Permissions extends AbstractModel {
	
	//TODO Do we want a transaction log for the Permissions data?
	
	private PermissionLevel permissions;
	private String username;
	private Project project;
	private int id;

	public Permissions(){
		
	}
	
	public Permissions(Project project, String username, PermissionLevel permissionLevel, int id) {
		this.username = username;
		this.permissions = permissionLevel;
		this.project = project;
	}

	/**
	 * returns the permissions currently granted this user
	 *
	 * @param user
	 * @return PermissionLevel
	 */
	public PermissionLevel getPermissionLevel(){
		return permissions;
	}
	
	/**
	 * Change the permissions level for this user
	 *
	 * @param user
	 * @param newLevel
	 */
	public void changePermissions(PermissionLevel newLevel){
		permissions = newLevel;
	}
	
	public String getUsername(){
		return username;
	}
	
	public int getId(){
		return id;
	}
	
	/**
	 * Project getter
	 *
	 * @return project
	 */
	public Project getProject(){
		return project;
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

	public static Permissions[] fromJSONArray(String body) {
		GsonBuilder builder = new GsonBuilder();
		return builder.create().fromJson(body, Permissions[].class);
	}

}

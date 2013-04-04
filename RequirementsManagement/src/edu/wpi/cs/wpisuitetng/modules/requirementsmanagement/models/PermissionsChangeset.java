package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent.EventType;

/**
 * A model to store the changes made to a permissions model
 * @author William Terry
 *
 */
public class PermissionsChangeset extends RequirementEvent{
private Map<String, FieldChange<?>> changes;
	
	/**
	 * Construct a RequirementChangeset with default properties.
	 */
	public PermissionsChangeset() {
		type = EventType.CHANGESET;
		changes = new HashMap<String, FieldChange<?>>();
	}
	
	/**
	 * Construct a RequirementChangeset with the given properties.
	 * Other properties are the same as in the default constructor.
	 * 
	 * @param user the User responsible for this change
	 */
	public PermissionsChangeset(User user) {
		this();
		this.user = user;
	}

	/**
	 * @return the map of field names to changes (Assignee -> (Bob, Joe))
	 */
	public Map<String, FieldChange<?>> getChanges() {
		return changes;
	}

	/**
	 * @param changes the changes to set
	 */
	public void setChanges(Map<String, FieldChange<?>> changes) {
		this.changes = changes;
	}

	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, PermissionsChangeset.class);
		return json;
	}
}

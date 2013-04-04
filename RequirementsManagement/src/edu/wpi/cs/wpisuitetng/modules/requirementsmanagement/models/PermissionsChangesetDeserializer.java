package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class PermissionsChangesetDeserializer implements JsonDeserializer<PermissionsChangeset>{
	@Override
	public PermissionsChangeset deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		// hash map to hold the deserialized FieldChange objects
		HashMap<String, FieldChange<?>> changesMap = new HashMap<String, FieldChange<?>>();
		
		JsonObject changeSet = json.getAsJsonObject();
		if (changeSet.has("changes")) {
			JsonObject changes = changeSet.get("changes").getAsJsonObject();
			if (changes.has("username")) {
				JsonObject titleObj = changes.get("username").getAsJsonObject();
				String oldName = context.deserialize(titleObj.get("oldValue"), String.class);
				String newName = context.deserialize(titleObj.get("newValue"), String.class);
				changesMap.put("username", new FieldChange<String>(oldName, newName));
			}
			if (changes.has("permissionLevel")) {
				JsonObject statusObj = changes.get("permissionLevel").getAsJsonObject();
				PermissionLevel oldStatus = context.deserialize(statusObj.get("oldValue"), PermissionLevel.class);
				PermissionLevel newStatus = context.deserialize(statusObj.get("newValue"), PermissionLevel.class);
				changesMap.put("permissionLevel", new FieldChange<PermissionLevel>(oldStatus, newStatus));
			}
			/*
	private int id;
	private int releaseNumber;
	private User creator;
	private Date creationDate, lastModifiedDate;
	private List<PermissionsEvent> events;

			 */
			// reconstruct the PermissionsChangeset
			PermissionsChangeset retVal = new PermissionsChangeset();
			retVal.setChanges(changesMap);
			retVal.setDate((Date)(context.deserialize(changeSet.get("date"), Date.class)));
			retVal.setUser((User)(context.deserialize(changeSet.get("user"), User.class)));
			
			// return the PermissionsChangeset
			return retVal;
		}
		else {
			throw new JsonParseException("PermissionsChangeset type is unrecognized");
		}
	}
}

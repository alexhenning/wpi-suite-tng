package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Custom JSON deserializer for the RequirementChangesetChangeset class
 */
public class RequirementChangesetDeserializer implements JsonDeserializer<RequirementChangeset> {

	@Override
	public RequirementChangeset deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		
		// hash map to hold the deserialized FieldChange objects
		HashMap<String, FieldChange<?>> changesMap = new HashMap<String, FieldChange<?>>();
		
		JsonObject changeSet = json.getAsJsonObject();
		if (changeSet.has("changes")) {
			JsonObject changes = changeSet.get("changes").getAsJsonObject();
			if (changes.has("name")) {
				JsonObject titleObj = changes.get("name").getAsJsonObject();
				String oldName = context.deserialize(titleObj.get("oldValue"), String.class);
				String newName = context.deserialize(titleObj.get("newValue"), String.class);
				changesMap.put("name", new FieldChange<String>(oldName, newName));
			}
			if (changes.has("description")) {
				JsonObject descriptionObj = changes.get("description").getAsJsonObject();
				String oldDesc = context.deserialize(descriptionObj.get("oldValue"), String.class);
				String newDesc = context.deserialize(descriptionObj.get("newValue"), String.class);
				changesMap.put("description", new FieldChange<String>(oldDesc, newDesc));
			}
			if (changes.has("assignee")) {
				JsonObject assigneeObj = changes.get("assignee").getAsJsonObject();
				User oldUser = context.deserialize(assigneeObj.get("oldValue"), User.class);
				User newUser = context.deserialize(assigneeObj.get("newValue"), User.class);
				changesMap.put("assignee", new FieldChange<User>(oldUser, newUser));
			}
//			if (changes.has("tags")) {
//				JsonObject tagsObj = changes.get("tags").getAsJsonObject();
//				Tag[] oldTags = context.deserialize(tagsObj.get("oldValue"), Tag[].class);
//				Tag[] newTags = context.deserialize(tagsObj.get("newValue"), Tag[].class);
//				changesMap.put("tags", new FieldChange<Set<Tag>>(new HashSet<Tag>(new ArrayList<Tag>(Arrays.asList(oldTags))), new HashSet<Tag>(new ArrayList<Tag>(Arrays.asList(newTags)))));
//			}
			if (changes.has("status")) {
				JsonObject statusObj = changes.get("status").getAsJsonObject();
				RequirementStatus oldStatus = context.deserialize(statusObj.get("oldValue"), RequirementStatus.class);
				RequirementStatus newStatus = context.deserialize(statusObj.get("newValue"), RequirementStatus.class);
				changesMap.put("status", new FieldChange<RequirementStatus>(oldStatus, newStatus));
			}
			if (changes.has("priority")) {
				JsonObject statusObj = changes.get("priority").getAsJsonObject();
				RequirementPriority oldPriority = context.deserialize(statusObj.get("oldValue"), RequirementPriority.class);
				RequirementPriority newPriority = context.deserialize(statusObj.get("newValue"), RequirementPriority.class);
				changesMap.put("priority", new FieldChange<RequirementPriority>(oldPriority, newPriority));
			}
			if (changes.has("estimate")) {
				JsonObject titleObj = changes.get("estimate").getAsJsonObject();
				String oldEstimate = context.deserialize(titleObj.get("oldValue"), String.class);
				String newEstimate = context.deserialize(titleObj.get("newValue"), String.class);
				changesMap.put("estimate", new FieldChange<String>(oldEstimate, newEstimate));
			}
			if (changes.has("actualEffort")) {
				JsonObject titleObj = changes.get("actualEffort").getAsJsonObject();
				String oldActualEffort = context.deserialize(titleObj.get("oldValue"), String.class);
				String newActualEffort = context.deserialize(titleObj.get("newValue"), String.class);
				changesMap.put("actualEffort", new FieldChange<String>(oldActualEffort, newActualEffort));
			}
			/*
	private int id;
	private int releaseNumber;
	private User creator;
	private Date creationDate, lastModifiedDate;
	private List<RequirementEvent> events;

			 */
			// reconstruct the RequirementChangeset
			RequirementChangeset retVal = new RequirementChangeset();
			retVal.setChanges(changesMap);
			retVal.setDate((Date)(context.deserialize(changeSet.get("date"), Date.class)));
			retVal.setUser((User)(context.deserialize(changeSet.get("user"), User.class)));
			
			// return the RequirementChangeset
			return retVal;
		}
		else {
			throw new JsonParseException("RequirementChangeset type is unrecognized");
		}
	}
}

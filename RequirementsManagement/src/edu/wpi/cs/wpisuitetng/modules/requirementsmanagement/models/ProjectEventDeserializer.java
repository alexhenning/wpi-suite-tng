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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent.ProjectEventType;


/**
 * Responsible for deserializing a RequirementEvent.
 * TODO: DOCUMENT THIS CLASS
 */
class ProjectEventDeserializer implements JsonDeserializer<ProjectEvent> {
	public static <T> void addFieldChange(String fieldName, Class<T> fieldClass, JsonObject changes, JsonDeserializationContext context, HashMap<String, FieldChange<?>> changesMap) {
		if (changes.has(fieldName)) {
			JsonObject titleObj = changes.get(fieldName).getAsJsonObject();
			changesMap.put(fieldName, new FieldChange<T>(fieldClass.cast(context.deserialize(titleObj.get("oldValue"), fieldClass)), fieldClass.cast(context.deserialize(titleObj.get("newValue"), fieldClass))));
		}
	}
	
	@Override
	public ProjectEvent deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		// we need to switch on the type field to figure out the concrete class to instantiate
		JsonObject object = element.getAsJsonObject();
		if(!object.has("type")) {
			throw new JsonParseException("ProjectEvent does not have type information");
		}
		if(!object.has("objectType")) {
			throw new JsonParseException("ProjectEvent does not have objectType information");
		}
		ProjectEventType eType = context.deserialize(object.get("type"), ProjectEventType.class);
		ProjectEventObjectType oType = context.deserialize(object.get("objectType"), ProjectEventObjectType.class);
		
		ProjectEvent retVal = new ProjectEvent(oType, (String)(context.deserialize(object.get("objectId"), String.class)));
		
		if(eType != null) { // type could be any garbage string, eType null if not in enum
			switch(eType) {
			case CREATION:
				retVal.setDate((Date)(context.deserialize(object.get("date"), Date.class)));
				retVal.setUser((User)(context.deserialize(object.get("user"), User.class)));
				retVal.setType(eType);
				
				return retVal;
			case CHANGES:
				// hash map to hold the deserialized FieldChange objects
				HashMap<String, FieldChange<?>> changesMap = new HashMap<String, FieldChange<?>>();
				JsonObject changeSet = object;
				if (changeSet.has("changes")) {
					JsonObject changes = changeSet.get("changes").getAsJsonObject();
					if(oType == ProjectEventObjectType.PERMISIONS) {
						addFieldChange("permissionLevel", PermissionLevel.class, changes, context, changesMap);
						addFieldChange("username", String.class, changes, context, changesMap);
					} else if (oType == ProjectEventObjectType.REQUIREMENT) {
						addFieldChange("name", String.class, changes, context, changesMap);
						addFieldChange("description", String.class, changes, context, changesMap);
						addFieldChange("assignees", User[].class, changes, context, changesMap);
						addFieldChange("status", RequirementStatus.class, changes, context, changesMap);
						addFieldChange("priority", RequirementPriority.class, changes, context, changesMap);
						addFieldChange("estimate", Integer.class, changes, context, changesMap);
						addFieldChange("actualEffort", Integer.class, changes, context, changesMap);
						addFieldChange("iteration", Iteration.class, changes, context, changesMap);
						addFieldChange("type", RequirementType.class, changes, context, changesMap);
						addFieldChange("subRequirements", String[].class, changes, context, changesMap);
						addFieldChange("releaseNumber", ReleaseNumber.class, changes, context, changesMap);
					}
					retVal.setChanges(changesMap);
					retVal.setDate((Date)(context.deserialize(changeSet.get("date"), Date.class)));
					retVal.setUser((User)(context.deserialize(changeSet.get("user"), User.class)));
					retVal.setType(eType);
				
					return retVal;
				}
				else {
					throw new JsonParseException("changes unrecognized");
				}

			}
		}
		throw new JsonParseException("ProjectEvent type is unrecognized");
	}
}
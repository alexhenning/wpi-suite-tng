package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent.EventType;

/**
 * Responsible for deserializing a RequirementEvent.
 */
class RequirementEventDeserializer implements JsonDeserializer<RequirementEvent> {
	@Override
	public RequirementEvent deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		// we need to switch on the type field to figure out the concrete class to instantiate
		JsonObject object = element.getAsJsonObject();
		if(!object.has("type")) {
			throw new JsonParseException("DefectEvent does not have type information");
		}
		EventType eType = context.deserialize(object.get("type"), EventType.class);
		if(eType != null) { // type could be any garbage string, eType null if not in enum
			switch(eType) {
			case CHANGESET:
				return context.deserialize(element, RequirementChangeset.class);
			case NOTE:
				return context.deserialize(element, RequirementNote.class);
			}
		}
		throw new JsonParseException("RequirementEvent type is unrecognized");
	}
}
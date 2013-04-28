/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO
 ******************************************************************************/

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
 * @author TODO
 * @version $Revision: 1.0 $
 */
class RequirementEventDeserializer implements JsonDeserializer<RequirementEvent> {
	/**
	 *TODO: DOCUMENT THIS
	 *
	 * @param element
	 * @param type
	 * @param context
	
	
	 * @return RequirementEvent
	 * @throws JsonParseException * @see com.google.gson.JsonDeserializer#deserialize(JsonElement, Type, JsonDeserializationContext)
	 */
	@Override
	
	public RequirementEvent deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		// we need to switch on the type field to figure out the concrete class to instantiate
		JsonObject object = element.getAsJsonObject();
		if(!object.has("type")) {
			throw new JsonParseException("RequirementEvent does not have type information");
		}
		EventType eType = context.deserialize(object.get("type"), EventType.class);
		if(eType != null) { // type could be any garbage string, eType null if not in enum
			switch(eType) {
			case NOTE:
				return context.deserialize(element, RequirementNote.class);
			}
		}
		throw new JsonParseException("RequirementEvent type is unrecognized");
	}
}
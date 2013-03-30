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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.entitymanagers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Walks through a list of requirements and applies a generic function to them
 * @author Tim Calvert
 *
 */
public class EditMultipleRequirements {
	
	/** List of requirements to edit */
	private List<RequirementModel> requirements;
	//private ModelMapper mapper;

	/**
	 * Default constructor
	 */
	public EditMultipleRequirements(List<RequirementModel> requirements) {
		this.requirements = requirements;
		//this.mapper = new ModelMapper();
		//this.mapper.getBlacklist().addAll(getFields());
	}
	
	private List<String> getFields() {
		final Method[] methods = requirements.get(1).getClass().getMethods();
		List<String> fields = new ArrayList<String>();
		
		for(Method m : methods) {
			fields.add(accessorNameToFieldName(m.getName()));
		}
		
		return fields;
	}
	
	private static String accessorNameToFieldName(String methodName) {
		methodName = methodName.substring(3);
		return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
	}
	
	public interface EditCallback {
		
		/**
		 * Called whenever a model's field needs to be changed
		 *
		 */
		Object call(RequirementModel req, String field);
	}
	
	// TODO add a public method to start all this off
	
	public void editRequirements(String field, EditCallback callback) {
		for(RequirementModel req : requirements) {
			walk(req, field,callback);
		}
	}
	
	private void walk(final RequirementModel req, String field, EditCallback callback) {
		// TODO do what needs to be done and then recurse
		callback.call(req, field);
		if(req.getSubRequirements().size() >= 1 || req.getSubRequirements() != null) {
			for(RequirementModel r : req.getSubRequirements()) {
				walk(r, field, callback);
			}
		}
	}
}

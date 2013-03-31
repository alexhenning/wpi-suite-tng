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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.callbacks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.AbstractEditCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Description goes here
 * @author Tim Calvert
 *
 */
public class EditFieldOfRequirementsCallback extends AbstractEditCallback {

	public EditFieldOfRequirementsCallback(String field, Object newValue) {
		super(field, newValue);
	}

	/**
	 * Changes the field of each requirement to a given value
	 *
	 * @param req requirement to work with
	 * @return
	 */
	@Override
	public Object call(RequirementModel req) {
		Method[] methods = RequirementModel.class.getMethods();
		Method methodToUse = null;
		for(Method method : methods) {
			if(method.getName().equals("set" + field.substring(0,1).toUpperCase() + field.substring(1))) {
				methodToUse = method;
			}
		}
		
		if(methodToUse != null) {
			try {
				methodToUse.invoke(req, newValue);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		
		return null;
	}

}

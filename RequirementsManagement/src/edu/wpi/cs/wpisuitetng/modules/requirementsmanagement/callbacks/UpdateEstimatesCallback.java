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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Description goes here
 * @author Tim Calvert
 *
 */
public class UpdateEstimatesCallback extends AbstractWorkCallback{
	
	public UpdateEstimatesCallback() {
		super("", null);
	}

	/**
	 * Updates the estimate fields of requirements, given their children
	 * * Currently assuming that if a requirement has children, then the
	 *   sum of the childrens' estimates is the entirety of the parent
	 *   requirement's estimate; it does not have any additional estimates
	 *   of its own. *
	 *
	 * @param req
	 * @return
	 */
	@Override
	public Object call(RequirementModel req) {
		Integer requirementsEstimate = 0;
		Method[] methods = RequirementModel.class.getMethods();
		Method getMethodToUse = null;
		Method setMethodToUse = null;

		for(Method method : methods) {
			if(method.getName().equals("set" + field.substring(0,1).toUpperCase() + field.substring(1))) {
				setMethodToUse = method;
			}
			if(method.getName().equals("get" + field.substring(0,1).toUpperCase() + field.substring(1))) {
				getMethodToUse = method;
			}
		}
		
		if(getMethodToUse != null && setMethodToUse != null) {
			try {
				setMethodToUse.invoke(req, parameters[0]);
				requirementsEstimate = (Integer) getMethodToUse.invoke(req);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		
		return requirementsEstimate;
	}

}

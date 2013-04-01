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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Abstract framework for applying a method to a requirement
 * @author Tim Calvert
 *
 */
public abstract class AbstractWorkCallback {

	protected String field;
	protected Object newValue;
	protected Integer[] parameters = null;
	
	public AbstractWorkCallback(String field, Object newValue) {
		this.field = field;
		this.newValue = newValue;
	}
	
	public AbstractWorkCallback(String field, Object newValue, Integer[] parameters) {
		this.field = field;
		this.newValue = newValue;
		this.parameters = parameters;
	}
	
	public abstract Integer call(RequirementModel req);

	public Integer[] getParameters() {
		return parameters;
	}

	public void setParameters(Integer[] parameters) {
		this.parameters = parameters;
	}	
}

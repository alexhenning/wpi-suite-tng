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

	/** field that might be accessed */
	protected String field;
	/** new value that the field might receive */
	protected Object newValue;
	
	/**
	 * Default constructor used by extended classes
	 * @param field Field to access
	 * @param newValue New value to set
	 */
	public AbstractWorkCallback(String field, Object newValue) {
		this.field = field;
		this.newValue = newValue;
	}
	
	/**
	 * Calls a function defined in extended classes
	 *
	 * @param req Requirement to work with
	 * @param value Value to can be used for various things
	 * @return Value that is usually aggregated in value
	 */
	public abstract Integer call(RequirementModel req, Integer value);
	
}

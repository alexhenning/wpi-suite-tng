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

/**
 *
 * Description goes here
 * @author Tim Calvert
 *
 */
public abstract class AbstractEditCallback {

	protected String field;
	protected Object newValue;
	
	public AbstractEditCallback(String field, Object newValue) {
		this.field = field;
		this.newValue = newValue;
	}
	
	public abstract Object call(RequirementModel req);
}

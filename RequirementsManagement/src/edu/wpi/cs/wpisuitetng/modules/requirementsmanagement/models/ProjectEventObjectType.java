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
 * the types of project event objects
 *
 */
public enum ProjectEventObjectType {
	REQUIREMENT ("requirementmodel", "id"),
	PERMISIONS ("permissions", "username");

	private final String modelName;
    private final String idFieldName;
    ProjectEventObjectType(String modelName, String idFieldName) {
        this.idFieldName = idFieldName;
        this.modelName = modelName;
    }
    public String modelName() { return modelName; }
    public String idFieldName() { return idFieldName; }
}
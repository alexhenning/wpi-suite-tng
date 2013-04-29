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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;


/**
 * A type to indicate how the report should display.
 * @author TODO
 */
public enum ReportType {
	PIE_CHART("Pie Chart"), BAR_CHART("Bar Chart");
	
	private String name;
	private ReportType(String name) {
		this.name = name;
	}
	
	/**
	 * Convert the data to a string.
	 */
	@Override public String toString() {
		return name;
	}
}

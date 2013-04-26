/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    alex henning
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * A combination of a data provider to plot and a report type to generate.
 * 
 * @author alex
 */
public class Report {

	ReportDataProvider dataProvider;
	ReportType type;

	/**
	 * @param dataProvider The source to get data from.
	 * @param type The type of report to generate.
	 */
	public Report(ReportDataProvider dataProvider, ReportType type) {
		this.dataProvider = dataProvider;
		this.type = type;
	}
	
	/**
	 * A type to indicate how the report should display.
	 */
	public ReportType getType() {
		return type;
	}

	/**
	 * Extracts data from a list of requirements and returns the corresponding
	 * map.
	 * 
	 * @param model List of requirements.
	 * @return some data to chart with a name and thecorresponding value
	 */
	public Map<Object, Integer> extractData(List<RequirementModel> model) {
		return dataProvider.extractData(model);
	}

	/**
	 * The name of the report.
	 */
	@Override public String toString() {
		return type.toString() + " of " + dataProvider.getName();
	}
}

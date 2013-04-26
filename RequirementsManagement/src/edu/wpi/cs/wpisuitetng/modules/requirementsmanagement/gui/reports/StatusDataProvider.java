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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
/**
 * TODO description
 * @author TODO
 */
public class StatusDataProvider implements ReportDataProvider {

	/**
	 * @return The name of this data provider
	 */
	@Override public String getName() {
		return "Requirement Status";
	}
	
	/**
	 * Extracts data from a list of requirements and returns the corresponding
	 * map.
	 * 
	 * @param model List of requirements.
	 * @return some data to chart with a name and thecorresponding value
	 */
	@Override public Map<Object, Integer> extractData(List<RequirementModel> model) {
		Map<Object, Integer> map = new HashMap<Object, Integer>();
	
		for (RequirementModel req : model) {
			RequirementStatus status = req.getStatus();
			if (map.containsKey(status)) {
				map.put(status, 1 + map.get(status));
			} else {
				map.put(status, 1);
			}
		}
		
		return map;
	}
}

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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
/**
 * TODO description
 * @return The name of this data provider
 * @author TODO
 */
public class IterationDataProvider implements ReportDataProvider {


	@Override public String getName() {
		return "Requirements in a Iteration";
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
		Iteration backlog = new Iteration();
		backlog.setIterationNumber("Backlog");
	
		for (RequirementModel req : model) {
			Iteration iteration = req.getIteration();
			String name;
			if (iteration == null) name = "Backlog";
			else name = iteration.getIterationNumber();
			if (map.containsKey(name)) {
				map.put(name, 1 + map.get(name));
			} else {
				map.put(name, 1);
			}
		}
		
		return map;
	}
}

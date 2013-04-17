package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

public class IterationDataProvider implements ReportDataProvider {
	
	@Override public Map<Object, Integer> extractData(List<RequirementModel> model) {
		Map<Object, Integer> map = new HashMap<Object, Integer>();
		Iteration backlog = new Iteration();
		backlog.setIterationNumber("Backlog");
	
		for (RequirementModel req : model) {
			Iteration iteration = req.getIteration();
			if (iteration == null) iteration = backlog;
			if (map.containsKey(iteration)) {
				map.put(iteration, 1 + map.get(iteration));
			} else {
				map.put(iteration, 1);
			}
		}
		
		return map;
	}
}

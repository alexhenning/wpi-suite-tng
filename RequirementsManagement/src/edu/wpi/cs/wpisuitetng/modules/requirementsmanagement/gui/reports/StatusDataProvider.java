package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

public class StatusDataProvider implements ReportDataProvider {

	@Override public String getName() {
		return "Requirement Status";
	}
	
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

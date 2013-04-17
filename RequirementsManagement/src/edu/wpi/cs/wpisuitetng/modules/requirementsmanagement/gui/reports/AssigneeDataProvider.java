package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

public class AssigneeDataProvider implements ReportDataProvider {

	@Override public String getName() {
		return "Requirement Assignees";
	}
	
	@Override public Map<Object, Integer> extractData(List<RequirementModel> model) {
		Map<Object, Integer> map = new HashMap<Object, Integer>();
	
		for (RequirementModel req : model) {
			List<User> users = req.getAssignees();
			for (User user: users) {
				if (map.containsKey(user.getName())) {
					map.put(user.getName(), 1 + map.get(user.getName()));
				} else {
					map.put(user.getName(), 1);
				}
			}
		}

		return map;
	}
}

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

public interface ReportDataProvider {
	Map<Object, Integer> extractData(List<RequirementModel> model);
}

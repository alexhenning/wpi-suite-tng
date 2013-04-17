package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

public class Report {
	
	ReportDataProvider dataProvider;
	ReportType type;
	
	public Report(ReportDataProvider dataProvider, ReportType type) {
		this.dataProvider = dataProvider;
		this.type = type;
	}
	
	public ReportType getType() {
		return type;
	}
	
	public Map<Object, Integer> extractData(List<RequirementModel> model) {
		return dataProvider.extractData(model);
	}
	
	@Override public String toString() {
		return type.toString() + " of " + dataProvider.getName();
	}
}

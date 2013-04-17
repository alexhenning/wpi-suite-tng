package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;


/**
 * A type to indicate how the report should display.
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

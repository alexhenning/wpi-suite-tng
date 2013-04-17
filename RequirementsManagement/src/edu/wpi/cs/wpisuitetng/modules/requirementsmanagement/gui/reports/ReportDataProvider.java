package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.reports;

import java.util.List;
import java.util.Map;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * An interface that provides some data to chart with a name and the
 * corresponding value.
 * 
 * @author alex
 */
public interface ReportDataProvider {
	/**
	 * @return The name of this data provider
	 */
	String getName();
	
	/**
	 * Extracts data from a list of requirements and returns the corresponding
	 * map.
	 * 
	 * @param model List of requirements.
	 * @return some data to chart with a name and thecorresponding value
	 */
	Map<Object, Integer> extractData(List<RequirementModel> model);
}

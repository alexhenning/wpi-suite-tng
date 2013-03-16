package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class RequirementModel extends AbstractModel {

	int id;
	int releaseNumber;
	RequirementStatus status;
	RequirementPriority priority;
	String name;
	String description;
	String estimate;
	String actualEffort;
	
	
	public int getReleaseNumber() {
		return releaseNumber;
	}

	public void setReleaseNumber(int releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RequirementStatus getStatus() {
		return status;
	}

	public void setStatus(RequirementStatus status) {
		this.status = status;
	}

	public RequirementPriority getPriority() {
		return priority;
	}

	public void setPriority(RequirementPriority priority) {
		this.priority = priority;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEstimate() {
		return estimate;
	}

	public void setEstimate(String estimate) {
		this.estimate = estimate;
	}

	public String getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(String actualEffort) {
		this.actualEffort = actualEffort;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/**
	 * Converts this Requiremnet to a JSON string
	 * @return a string in JSON representing this Defect
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, RequirementModel.class);
		return json;
	}

	//TODO finish this code...
//	/**
//	 * @param json Json string to parse containing Defect
//	 * @return The Defect given by json
//	 */
//	public static Defect fromJSON(String json) {
//		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
//		return builder.create().fromJson(json, Defect.class);
//	}
//	
//	/**
//	 * @param json Json string to parse containing Defect array
//	 * @return The Defect array given by json
//	 */
//	public static RequirementModel[] fromJSONArray(String json) {
//		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
//		return builder.create().fromJson(json, Defect[].class);
//	}
	
//	/**
//	 * Add dependencies necessary for Gson to interact with this class
//	 * @param builder Builder to modify
//	 */
//	public static void addGsonDependencies(GsonBuilder builder) {
//		DefectEvent.addGsonDependencies(builder);
//	}
//	
	// interface documentation says this is necessary for the mock database
	// not sure if this is still needed otherwise
	@Override
	public Boolean identify(Object o) {
		Boolean returnValue = false;
		if(o instanceof RequirementModel && id == ((RequirementModel) o).getId()) {
			returnValue = true;
		}
		if(o instanceof String && Integer.toString(id).equals(o)) {
			returnValue = true;
		}
		return returnValue;
	}

	@Override
	public String toString() {
		return toJSON();
	}

}

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

public class ReleaseNumber extends AbstractModel {

	private int id;
	private int releaseNumber;

	public ReleaseNumber(int id, int releaseNumber, Project project) {
		super();
		this.id = id;
		this.releaseNumber = releaseNumber;
		this.setProject(project);
	}

	public ReleaseNumber() {
		super();
		id = -1;
		releaseNumber = -1;
		
		this.setProject(new Project("", "-1"));
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the releaseNumber
	 */
	public int getReleaseNumber() {
		return releaseNumber;
	}

	/**
	 * @param releaseNumber the releaseNumber to set
	 */
	public void setReleaseNumber(int releaseNumber) {
		this.releaseNumber = releaseNumber;
	}
	
	@Override
	public void save() {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, Iteration.class);
		return json;
	}
	
	@Override
	public String toString() {
		return toJSON();
	}

	/**
	 * @param json Json string to parse containing ReleaseNumber
	 * @return The ReleaseNumber given by json
	 */
	public static ReleaseNumber fromJSON(String json) {
		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
		return builder.create().fromJson(json, ReleaseNumber.class);
	}
	
	/**
	 * @param json Json string to parse containing ReleaseNumber array
	 * @return The ReleaseNumber array given by json
	 */
	public static ReleaseNumber[] fromJSONArray(String json) {
		GsonBuilder builder = new GsonBuilder();
//		addGsonDependencies(builder);
		return builder.create().fromJson(json, ReleaseNumber[].class);
	}

	/* (non-Javadoc)
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 */
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//TODO add new equals method
}

/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

/**
 *
 * Class for release numbers
 *
 */
public class ReleaseNumber extends AbstractModel {

	/** id */
	private int id;
	/** the release number */
	private String releaseNumber;
	//TODO add any addition fields for the ReleaseNumber

	/**
	 * Constructor
	 * @param id the id number
	 * @param releaseNumber the release number
	 * @param project the project
	 */
	public ReleaseNumber(int id, String releaseNumber, Project project) {
		super();
		this.id = id;
		this.releaseNumber = releaseNumber;
		this.setProject(project);
	}

	/**
	 * Constructor
	 */
	public ReleaseNumber() {
		super();
		id = -1;
		releaseNumber = "";
		
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
	public String getReleaseNumber() {
		return releaseNumber;
	}

	/**
	 * @param releaseNumber the releaseNumber to set
	 */
	public void setReleaseNumber(String releaseNumber) {
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

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#toJSON()
	 *
	 * @return
	 */
	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, ReleaseNumber.class);
		return json;
	}
	
	/**
	 *toString() method
	 *
	 * @return the JSON string
	 */
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

	/**
	 * @see edu.wpi.cs.wpisuitetng.modules.Model#identify(java.lang.Object)
	 *
	 * @param o
	 * @return
	 */
	@Override
	public Boolean identify(Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//TODO add new equals method
}

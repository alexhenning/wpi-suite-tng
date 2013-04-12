/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO who wrote this?
 ******************************************************************************/
//TODO Documentation of this file

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveProjectEventsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrievePermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSingleIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSinglePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSingleRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSingleUserRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveUsersRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 *
 * Handles retrieving and updating data in the database
 * @author Alex Henning
 * @contributor William Terry
 *
 */
public class DB {
	
	/**
	 * Retrieves a single requirement from the database and runs the callback function in the class passed
	 *
	 * @param id the id of the requirement
	 * @param callback the class that has the callback function to be run on the requirement
	 */
	public static void getSingleRequirement(String id, SingleRequirementCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + id, HttpMethod.GET);
		request.addObserver(new RetrieveSingleRequirementRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Retrieves all the requirements from the databse and runs the callback function in the class passed
	 *
	 * @param callback the class that has the callback function to be run on the list of requirements
	 */
	public static void getAllRequirements(RequirementsCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.GET);
		request.addObserver(new RetrieveRequirementModelRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Updates a single requirement in the database and calls the callback function in the class passed on it
	 *
	 * @param req The requirement to be updated
	 * @param callback The class that the callback function will be called from
	 */
	public static void updateRequirements(RequirementModel req, SingleRequirementCallback callback) {
		System.out.println(req.toJSON());
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + req.getId(),  HttpMethod.POST);
		request.setBody(req.toJSON());
		request.addObserver(new EditRequirementModelRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Retrieves a single requirement from the database and runs the callback function in the class passed
	 *
	 * @param id the id of the requirement
	 * @param callback the class that has the callback function to be run on the requirement
	 */
	public static void getSingleIteration(String id, SingleIterationCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration/" + id, HttpMethod.GET);
		request.addObserver(new RetrieveSingleIterationRequestObserver(callback));
		request.send();
	}

	/**
	 * Retrieve all the iterations from the database and run the callback function in the class passed
	 *
	 * @param callback The class that has the callback function to be run
	 */
	public static void getAllIterations(IterationCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration",  HttpMethod.GET);
		request.addObserver(new RetrieveIterationsRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Update an iteration in the database and run the callback function in the class passed
	 *
	 * @param iteration the iteration to be updated
	 * @param callback
	 */
	public static void updateIteration(Iteration iteration, SingleIterationCallback callback) {
		System.out.println(iteration.toJSON());
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration/" + iteration.getId(),  HttpMethod.POST);
		request.setBody(iteration.toJSON());
		request.addObserver(new EditIterationRequestObserver(callback));
		request.send();
	}

	/**
	 * Get a single permission from the database run the callback function from the class passed
	 *
	 * @param username the name of the user that the permission is wanted for
	 * @param callback the class with the callback function to be run
	 */
	public static void getSinglePermission(String username, SinglePermissionCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions/" + username, HttpMethod.GET);
		request.addObserver(new RetrieveSinglePermissionRequestObserver(callback));
		request.send();
	}

//	public static void createProjectEvent(ProjectEvent projectEvent, AddProjectEventController callback) {
//		final Request request = Network.getInstance().makeRequest("requirementsmanagement/projectevent",  HttpMethod.PUT);
//		request.setBody(projectEvent.toJSON());
//		request.addObserver(new CreateProjectEventRequestObserver(callback));
//		request.send();
//	}
	
	/**
	 * retrieves all project events from the database and runs the callback function on them in the class passed
	 *
	 * @param callback the class with the callback function
	 */
	public static void getAllProjectEvents(ProjectEventsCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/projectevent",  HttpMethod.GET);
		request.addObserver(new RetrieveProjectEventsRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Retrieves all the project events from the database and runs the callback function on the in the class passed
	 *
	 * @param callback the class with the callback function to run
	 */
	public static void getAllProjectEventsFor(ProjectEventsCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/projectevent",  HttpMethod.GET);
		request.addObserver(new RetrieveProjectEventsRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Retrieve all the permissions from the database and run the callback function in the class passed
	 *
	 * @param callback the class with the callback function to run
	 */
	public static void getAllPermissions(PermissionsCallback callback) {
		System.out.println("get all permissions");
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions", HttpMethod.GET);
		request.addObserver(new RetrievePermissionsRequestObserver(callback));
		request.send();
	}

	/**
	 * Retrieves a single user from the database and runs the callback function in the class passed
	 *
	 * @param id the id of the requirement
	 * @param callback the class that has the callback function to be run on the requirement
	 */
	public static void getSingleUser(String id, SingleUserCallback callback) {
System.err.println("id requested from db: " + id);
		final Request request = Network.getInstance().makeRequest("core/user/" + id, HttpMethod.GET);
		request.addObserver(new RetrieveSingleUserRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Retrieve all the users from the database and run the callback function in the class passed
	 *
	 * @param callback the class with the callback function to run
	 */
	public static void getAllUsers(UsersCallback callback) {
		System.out.println("get all users");
		final Request request = Network.getInstance().makeRequest("core/user", HttpMethod.GET);
		request.addObserver(new RetrieveUsersRequestObserver(callback));
		request.send();
	}
}

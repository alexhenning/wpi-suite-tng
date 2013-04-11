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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveAllReleaseNumbersObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveCanCloseRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveProjectEventsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrievePermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSingleIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSinglePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSingleRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.UpdateReleaseNumberObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 *
 * Handels retriving and updating data in the database
 * @author TODO
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
	 * Retrieves all the requirements from the database and runs the callback function in the class passed
	 *
	 * @param callback the class that has the callback function to be run on the list of requirements
	 */
	public static void getAllRequirements(RequirementsCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.GET);
		request.addObserver(new RetrieveRequirementModelRequestObserver(callback));
		request.send();
	}
	
	/**
	 * Checks if a requirement has all of it's subrequirements closed
	 *
	 * @param callback the class that has the callback function to be run on the list of requirements
	 */
	public static void canCloseRequirements(CanCloseRequirementCallback callback, String id) {
		final Request request = Network.getInstance().makeRequest("Advanced/requirementsmanagement/requirementmodel/canClose/"+id,  HttpMethod.GET);
		request.addObserver(new RetrieveCanCloseRequirementModelRequestObserver(callback));
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
	 * @param updateTableCallback the class with the callback function to run
	 */
	public static void getAllPermissions(PermissionsCallback updateTableCallback) {
		System.out.println("get all permissions");
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions", HttpMethod.GET);
		request.addObserver(new RetrievePermissionsRequestObserver(updateTableCallback));
		request.send();
	}
	
	/**
	 * Retrieves all release numbers from the database
	 *
	 * @param getReleaseNumbersCallback class with the callback function to run
	 */
	public static void getAllReleaseNumbers(ReleaseNumberCallback getReleaseNumbersCallback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/releasenumber", HttpMethod.GET);
		request.addObserver(new RetrieveAllReleaseNumbersObserver(getReleaseNumbersCallback));
		request.send();
	}
	
	/**
	 * Updates the database with the given release number
	 *
	 * @param rn Release number that will be updated
	 * @param updateReleaseNumberCallback class with the callback function to run
	 */
	public static void updateReleaseNumber(ReleaseNumber rn, SingleReleaseNumberCallback updateReleaseNumberCallback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/releasenumber/" + rn.getId(), HttpMethod.POST);
		request.setBody(rn.toJSON());
		request.addObserver(new UpdateReleaseNumberObserver(updateReleaseNumberCallback));
		request.send();
	}

}

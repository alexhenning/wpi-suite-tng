package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveIterationsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSinglePermissionsRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveSingleRequirementRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class DB {
	
	public static void getSingleRequirement(String id, SingleRequirementCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + id, HttpMethod.GET);
		request.addObserver(new RetrieveSingleRequirementRequestObserver(callback));
		request.send();
	}
	
	public static void getAllRequirements(RequirementsCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.GET);
		request.addObserver(new RetrieveRequirementModelRequestObserver(callback));
		request.send();
	}
	
	public static void updateRequirements(RequirementModel req, SingleRequirementCallback callback) {
		System.out.println(req.toJSON());
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + req.getId(),  HttpMethod.POST);
		request.setBody(req.toJSON());
		request.addObserver(new EditRequirementModelRequestObserver(callback));
		request.send();
	}

	public static void getAllIterations(IterationCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration",  HttpMethod.GET);
		request.addObserver(new RetrieveIterationsRequestObserver(callback));
		request.send();
	}
	
	public static void updateIteration(Iteration iteration, SingleIterationCallback callback) {
		System.out.println(iteration.toJSON());
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration/" + iteration.getId(),  HttpMethod.POST);
		request.setBody(iteration.toJSON());
		request.addObserver(new EditIterationRequestObserver(callback));
		request.send();
	}

	public static void getSinglePermissions(String id, SinglePermissionsCallback callback) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions/" + id, HttpMethod.GET);
		request.addObserver(new RetrieveSinglePermissionsRequestObserver(callback));
		request.send();
	}

}

/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite

 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    vpatara
 *    David Modica
 *    Tim Calvert
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SplitRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * This observer is called when a response is received from a request
 * to the server to split a requirement.
 *
 * @author vpatara
 */
public class SplitRequirementModelRequestObserver implements RequestObserver {

	/** the controller that created the observer */
	private SplitRequirementController controller;

	/**
	 * Constructs a request observer for splitting a requirement
	 *
	 * @param controller the controller that created the observer
	 */
	public SplitRequirementModelRequestObserver(SplitRequirementController controller) {
		this.controller = controller;
	}

	/**
	 * Indicate a successful response and reports success to the controller
	 *
	 * @param iReq a request response containing the split child requirement
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();

		// Parse the requirement model out of the response body
		final RequirementModel requirement = RequirementModel.fromJSON(response.getBody());
		final int childId = requirement.getId();

		// Tells the controller the success and sends the child id (only if id
		// is valid, or non-negative)
		if(childId >= 0)
			controller.receivedSplitConfirmation(requirement);
		else {
			System.err.println("The request to split a requirement was successful, "
							+ "although the child id is invalid (" + childId + ").");
			controller.receivedSplitConfirmation(null); // failure
		}
	}

	/**
	 * Indicates an error in the response and reports failure to the controller
	 *
	 * @param iReq a request response
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to split a requirement had an error.");
		System.err.println("\tResponse: "+iReq.getResponse().getStatusCode() + " --- "
							+ iReq.getResponse().getBody());

		// Tells the controller that the request was unsuccessful
		controller.receivedSplitConfirmation(null);
	}

	/**
	 * Indicates the response failed and reports failure to the controller
	 *
	 * @param iReq a request response
	 * @param exception the exception
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to split a requirement has failed.");

		// Tells the controller that the request was unsuccessful
		controller.receivedSplitConfirmation(null);
	}
}

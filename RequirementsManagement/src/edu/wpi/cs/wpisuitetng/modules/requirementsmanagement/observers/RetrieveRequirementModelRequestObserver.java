/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite

 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Modica
 *    Tim Calvert
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import java.util.Arrays;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.GetRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;
/**
 * This observer is called when a response is received from a request to the server
 * to retrieve a requirement from the database.
 * 
 * @author David
 *
 */
public class RetrieveRequirementModelRequestObserver implements RequestObserver {

	private GetRequirementController controller;
	
	public RetrieveRequirementModelRequestObserver(GetRequirementController controller){
		this.controller = controller;
	}
	
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		
		// Parse the message out of the response body
		final RequirementModel[] requirements = RequirementModel.fromJSONArray(response.getBody());
		
		// Pass the messages back to the controller
		controller.receivedGetConfirmation(Arrays.asList(requirements));
	}

	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to add a message failed.");		
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to add a message failed.");
	}
	
	

}

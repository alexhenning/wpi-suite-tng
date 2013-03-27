/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 *
 * Description goes here
 * @author Tim
 *
 */
public class RetrieveSingleRequirementRequestObserver implements RequestObserver {
	
	private GetSingleRequirementController controller;
	
	/**
	 * Default constructor
	 * @param controller Controller this Observer handles requests from
	 */
	public RetrieveSingleRequirementRequestObserver(GetSingleRequirementController controller) {
		this.controller = controller;
	}

	/**
	 * Successful request
	 *
	 * @param iReq Request returned from db
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final RequirementModel[] reqs = RequirementModel.fromJSONArray(response.getBody());
		controller.receivedGetSingleConfirmation(reqs[0]);
	}

	/**
	 * Error on return
	 *
	 * @param iReq
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to get a requirement failed.");
	}

	/**
	 * Failure on return
	 *
	 * @param iReq
	 * @param exception
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to get a requirement failed.\nError: " + exception.getMessage());
	}

}

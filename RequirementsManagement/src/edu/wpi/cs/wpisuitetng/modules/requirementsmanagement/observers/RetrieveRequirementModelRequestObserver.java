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

import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;
/**
 * This observer is called when a response is received from a request to the server
 * to retrieve a requirement from the database.
 * 
 * @author David
 * @author Tim Calvert
 */
public class RetrieveRequirementModelRequestObserver implements RequestObserver {

	/** Callback paired with this */
	private RequirementsCallback callback;
	
	/**
	 * Constructor
	 * @param callback callback paired with this
	 */
	public RetrieveRequirementModelRequestObserver(RequirementsCallback callback){
		this.callback = callback;
	}
	
	/**
	 * Indicate a successful response
	 *
	 * @param iReq a request
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		
		// Parse the message out of the response body
		final RequirementModel[] requirements = RequirementModel.fromJSONArray(response.getBody());
		
		// Pass the messages back to the controller
		if(requirements == null)
			System.err.println("Null requirements in response body");
		callback.callback((requirements != null) ? Arrays.asList(requirements) : new ArrayList<RequirementModel>());
	}

	/**
	 * indicate an error in the response
	 *
	 * @param iReq a request
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to retrieve requirements failed." + iReq.getResponse().getStatusCode());		
	}

	/**
	 *indicate the response failed
	 *
	 * @param iReq a request
	 * @param exception the exception
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve requirements failed.");
	}
	
	

}

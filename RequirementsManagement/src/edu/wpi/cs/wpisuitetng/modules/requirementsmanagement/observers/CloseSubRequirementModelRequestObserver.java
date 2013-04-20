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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.CloseSubRequirementsCallback;
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
public class CloseSubRequirementModelRequestObserver implements RequestObserver {

	/** Callback paired with this */
	private CloseSubRequirementsCallback callback;
	
	/**
	 * Constructor
	 * @param callback callback paired with this
	 */
	public CloseSubRequirementModelRequestObserver(CloseSubRequirementsCallback callback){
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
		final boolean result = response.getBody().trim().equals("true");
		
		// Pass the messages back to the controller
		callback.callback(result);
	}

	/**
	 * indicate an error in the response
	 *
	 * @param iReq a request
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to close sub requirements failed.");		
	}

	/**
	 *indicate the response failed
	 *
	 * @param iReq a request
	 * @param exception the exception
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to close sub requirements failed.");
	}
	
	

}

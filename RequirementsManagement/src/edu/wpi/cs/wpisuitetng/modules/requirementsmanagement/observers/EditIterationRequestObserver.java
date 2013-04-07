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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * This observer is called when a response is received from a request to the server
 * to edit a requirement.
 * 
 * @author David
 * @author Jacob Palnick
 *
 */
public class EditIterationRequestObserver implements RequestObserver {

	/** Callback paired with this */
	private SingleIterationCallback callback;
	
	/**
	 * Default constructor
	 * @param callback Controller paired with this observer
	 */
	public EditIterationRequestObserver(SingleIterationCallback callback){
		this.callback = callback;
	}
	
	/**
	 * Server reported a successful request
	 *
	 * @param iReq Request returned from server
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();
		
		// Parse the message out of the response body
		final Iteration iteration = Iteration.fromJSON(response.getBody());
		
		// Pass the messages back to the controller
		callback.callback(iteration);
	}

	/**
	 * Server reported an error in the request
	 *
	 * @param iReq Request returned from the server
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to edit an iteration failed.");
	}

	/**
	 * Server reported an failure in the request
	 *
	 * @param iReq Request returned from the server
	 * @param exception Exception thrown on the server
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to edit an iteration failed.");
	}

}

/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    James Meggin
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.AddReleaseNumberController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 *
 * This observer is called when a response is received from a request 
 * to the server to create a release number.
 * @author James
 *
 * @version $Revision: 1.0 $
 */
public class CreateReleaseNumberRequestObserver implements RequestObserver {
	
	/** The controller that called this */
	private AddReleaseNumberController controller;
	
	/**
	 * Constructor
	 * @param controller the controller that called this
	 */
	public CreateReleaseNumberRequestObserver(AddReleaseNumberController controller) {
		this.controller = controller;
	}

	/**
	 * Indicate a successful response
	 *
	 * @param iReq a request
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		// Get the response to the given request
		final ResponseModel response = iReq.getResponse();

		// Parse the message out of the response body
		final ReleaseNumber rnumber = ReleaseNumber.fromJSON(response.getBody());

		// Pass the messages back to the controller
		controller.receivedAddConfirmation(rnumber);

	}

	/**
	 * indicate an error in the response
	 *
	 * @param iReq a request
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to create a release number had an error.");
		System.err.println("\tResponse: "+iReq.getResponse().getStatusCode()+" --- "
							+iReq.getResponse().getBody());
	}

	/**
	 * indicate a response failed
	 *
	 * @param iReq a request
	 * @param exception the exception
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest, Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to create a release number has failed.");
	}

}

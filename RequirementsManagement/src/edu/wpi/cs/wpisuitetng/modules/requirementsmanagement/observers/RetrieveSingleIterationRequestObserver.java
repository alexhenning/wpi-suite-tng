/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tim Calvert
 *    Jacob Palnick
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 *
 * Description goes here
 * @author Tim
 * @author Jacob Palnick
 *
 * @version $Revision: 1.0 $
 */
public class RetrieveSingleIterationRequestObserver implements RequestObserver {
	
	/** Callback paired with this */
	private SingleIterationCallback callback;
	
	/**
	 * Default constructor
	 * @param callback Controller this Observer handles requests from
	 */
	public RetrieveSingleIterationRequestObserver(SingleIterationCallback callback) {
		this.callback = callback;
	}

	/**
	 * Successful request
	 *
	 * @param iReq Request returned from db
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final Iteration[] reqs = Iteration.fromJSONArray(response.getBody());
		callback.callback(reqs[0]);
	}

	/**
	 * Error on return
	 *
	 * @param iReq
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to get a iteration failed.");
	}

	/**
	 * Failure on return
	 *
	 * @param iReq
	 * @param exception
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest, Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to get a iteration failed.\nError: " + exception.getMessage());
	}

}

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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleReleaseNumberCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 *
 * Request observer to monitor the db response when updating a release number
 * @author Tim
 *
 */
public class UpdateReleaseNumberObserver implements RequestObserver {
	
	private SingleReleaseNumberCallback callback;
	
	public UpdateReleaseNumberObserver(SingleReleaseNumberCallback callback) {
		this.callback = callback;
	}

	/**
	 * Successful request
	 *
	 * @param iReq Request returned from db
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		ReleaseNumber rn = ReleaseNumber.fromJSON(response.getBody());
		callback.callback(rn);
	}

	/**
	 * Error in request
	 *
	 * @param iReq Request returned from db
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to update a release number failed");
	}

	/**
	 * Failed request
	 *
	 * @param iReq Request returned from db
	 * @param exception Exception throw by the server
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to update a release number failed");
	}

}

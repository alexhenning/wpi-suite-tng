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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author William Terry
 *
 */
public class RetrieveSinglePermissionRequestObserver implements
		RequestObserver {
	
	/** Callback paired with this */
	private SinglePermissionCallback callback;
	
	/**
	 * Default constructor
	 * @param callback Controller this Observer handles requests from
	 */
	public RetrieveSinglePermissionRequestObserver(SinglePermissionCallback callback) {
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
		final Permissions[] profile = Permissions.fromJSONArray(response.getBody());
		callback.callback(profile[0]);
	}

	/**
	 * Error on return
	 *
	 * @param iReq
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to get a permissions profile failed.");
	}

	/**
	 * Failure on return
	 *
	 * @param iReq
	 * @param exception
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to get a permissions failed.\nError: " + exception.getMessage());
	}


}

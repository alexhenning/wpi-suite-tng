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
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.PermissionsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Observes responses for requests to retrieve all user permissions 
 *
 * @author vpatara
 *
 * @version $Revision: 1.0 $
 */
public class RetrievePermissionsRequestObserver implements RequestObserver {
	
	/** Callback paired with this */
	private PermissionsCallback callback;
	
	/**
	 * Constructor
	 * @param callback callback paired with this
	 */
	public RetrievePermissionsRequestObserver(PermissionsCallback callback) {
		this.callback = callback;
	}

	/**
	 * Indicate a successful response
	 *
	 * @param iReq a request
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(IRequest)
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final Permissions[] perms = Permissions.fromJSONArray(response.getBody());
		callback.callback(Arrays.asList(perms));
	}

	/**
	 * indicate an error in the response
	 *
	 * @param iReq a request
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(IRequest)
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to retrieve Permissions failed.");	
	}

	/**
	 *indicate the response failed
	 *
	 * @param iReq a request
	 * @param exception the exception
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(IRequest, Exception)
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve Permissions failed.");
	}
}

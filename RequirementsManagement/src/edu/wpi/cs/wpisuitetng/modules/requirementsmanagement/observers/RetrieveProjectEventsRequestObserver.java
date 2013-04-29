/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    TODO
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import java.util.Arrays;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.ProjectEventsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;
/**
 * This observer is called when TODO
 * 
 * @author TODO
 */

public class RetrieveProjectEventsRequestObserver implements RequestObserver {
	
	/** Callback paired with this */
	private ProjectEventsCallback callback;
	
	/**
	 * Constructor
	 * @param callback callback paired with this
	 */
	public RetrieveProjectEventsRequestObserver(ProjectEventsCallback callback) {
		this.callback = callback;
	}

	/**
	 * Indicate a successful response
	 *
	 * @param iReq a request
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final ProjectEvent[] reqs = ProjectEvent.fromJSONArray(response.getBody());
		callback.callback(Arrays.asList(reqs));
	}

	/**
	 * indicate an error in the response
	 *
	 * @param iReq a request
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to retrieve ProjectEvents failed.");	
	}

	/**
	 *indicate the response failed
	 *
	 * @param iReq a request
	 * @param exception the exception
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve ProjectEvents failed.");
	}
}

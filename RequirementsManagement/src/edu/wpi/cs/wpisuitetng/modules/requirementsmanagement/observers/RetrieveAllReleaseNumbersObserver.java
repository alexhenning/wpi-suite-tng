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

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.ReleaseNumberCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 *
 * Request observer to monitor response for retrieving release numbers
 * @author Tim
 *
 */
public class RetrieveAllReleaseNumbersObserver implements RequestObserver {
	
	private ReleaseNumberCallback callback;
	
	public RetrieveAllReleaseNumbersObserver(ReleaseNumberCallback callback) {
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
		final ReleaseNumber[] releaseNumbers = ReleaseNumber.fromJSONArray(response.getBody());
		callback.callback(Arrays.asList(releaseNumbers));
	}

	/**
	 * Error in request
	 *
	 * @param iReq Request returned from db
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to get release numbers failed");
	}

	/**
	 * Request failed
	 *
	 * @param iReq Request returned from db
	 * @param exception Exception throw by db
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The requst to get release numbers failed");
	}

}

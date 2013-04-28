/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import java.util.Arrays;
import com.google.gson.GsonBuilder;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.UsersCallback;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * TODO Description
 * @author TODO
 *
 */

public class RetrieveUsersRequestObserver implements RequestObserver {
	
	/** Callback paired with this */
	private UsersCallback callback;
	
	/**
	 * Constructor
	 * @param callback callback paired with this
	 */
	public RetrieveUsersRequestObserver(UsersCallback callback) {
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
		GsonBuilder builder = new GsonBuilder();
		final User[] users = builder.create().fromJson(response.getBody(), User[].class);
//		String s = "Users: [";
//		for(int i = 0; i < users.length; i++)
//			s += users[i].getUsername() + ", ";
//		s = s.substring(0, s.length() - 2);
//		s += "]";
//		System.out.println(s);
		callback.callback(Arrays.asList(users));
	}

	/**
	 * indicate an error in the response
	 *
	 * @param iReq a request
	 */
	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to retrieve users failed.");	
	}

	/**
	 *indicate the response failed
	 *
	 * @param iReq a request
	 * @param exception the exception
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve users failed.");
	}
}

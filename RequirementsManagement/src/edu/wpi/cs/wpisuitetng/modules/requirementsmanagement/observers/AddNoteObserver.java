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
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.AddNoteController;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 *
 * Handles responses from the server to save a note
 * @author Tim
 *
 */
public class AddNoteObserver implements RequestObserver {
	
	/** the controller that created the observer */
	private final AddNoteController controller;
	
	/**
	 * Default constructor for notes
	 * @param controller Controller that sent the request to add
	 */
	public AddNoteObserver(AddNoteController controller) {
		this.controller = controller;
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseSuccess(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 *
	 * @param iReq Request received from the server
	 */
	@Override
	public void responseSuccess(IRequest iReq) {
		controller.receivedAddConfirmation(iReq.getResponse());
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#responseError(edu.wpi.cs.wpisuitetng.network.models.IRequest)
	 *
	 * @param iReq Request received from the server
	 */
	@Override
	public void responseError(IRequest iReq) {
		controller.receivedAddFailure(iReq.getResponse());
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.network.RequestObserver#fail(edu.wpi.cs.wpisuitetng.network.models.IRequest, java.lang.Exception)
	 *
	 * @param iReq Request received from the server
	 * @param exception Exception indicating error
	 */
	@Override
	public void fail(IRequest iReq, Exception exception) {
		controller.receivedAddFailure(iReq.getResponse());
	}

}

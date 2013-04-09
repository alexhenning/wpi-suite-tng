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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ReleaseNumberPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreateIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreateReleaseNumberRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 *
 * Controller responds to a click of an add button to create a new
 * Release Number to send to the server
 * @author James
 *
 */
@SuppressWarnings("serial")
public class AddReleaseNumberController extends AbstractAction implements
		ActionListener {
	
	/** The panel for release numbers */
	private final ReleaseNumberPanel panel;
	
	/**
	 * Constructor
	 * @param panel the release number panel
	 */
	public AddReleaseNumberController(ReleaseNumberPanel panel) {
		this.panel = panel;
	}

	/**
	 * This will be called with the add button is pressed
	 *
	 * @param e the action performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(panel.validateFields()) {
			final Request request = Network.getInstance().makeRequest("requirementsmanagement/releasenumber",  HttpMethod.PUT);
			request.setBody(panel.getModel().toJSON());
			request.addObserver(new CreateReleaseNumberRequestObserver(this));
			request.send();
		}
	}
	
	/**
	 * Called by observer when it receives a confirmation
	 *
	 * @param rnum Release Number that was added
	 */
	public void receivedAddConfirmation(ReleaseNumber rnum) {
		panel.updateReleaseNumbers();
		panel.updateModel(rnum);
		panel.setStatus("Release Number saved");
	}

}

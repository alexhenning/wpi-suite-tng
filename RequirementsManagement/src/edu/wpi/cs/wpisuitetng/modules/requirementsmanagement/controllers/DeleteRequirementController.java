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
 *    Tim DeFreitas
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.DeleteRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to a click even to delete a requirement
 * @author Tim Calvert
 *
 */
@SuppressWarnings("serial")
public class DeleteRequirementController extends AbstractAction implements ActionListener {
	// TODO This system currently does not have enough information to function properly
	//      I'd suggest that we don't try and demonstrate this yet
	
	/** The requirements panel that has the requirement */
	private final RequirementsPanel panel;
	//private final JPanel buttonPanel;
	
	/**
	 * Constructor
	 * @param panel The requirements panel that has the requirement
	 */
	public DeleteRequirementController(RequirementsPanel panel) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.panel = panel;
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Delete the requirement from the server
	 *
	 * @param e the action that happened
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.DELETE);
		request.setBody(panel.getModel().toJSON());
		request.addObserver(new DeleteRequirementModelRequestObserver(this));
		request.send();
	}
	
	/**
	 * Confirm that it was deleted
	 *
	 * @param Id
	 */
	public void receivedDeleteConfirmation(int Id) {
		// TODO: mainBoard.deleteRequirement(Id);
	}

}

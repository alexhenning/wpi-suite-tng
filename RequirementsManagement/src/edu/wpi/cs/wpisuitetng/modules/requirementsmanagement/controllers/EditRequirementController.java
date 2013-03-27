/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite

 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    David Modica
 *    Tim Calvert
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.EditRequirementPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to a click event to edit a requirement
 * @author Tim Calvert
 *
 */
@SuppressWarnings("serial")
public class EditRequirementController extends AbstractAction implements ActionListener {
	
	private final RequirementsPanel panel;
	//private final JPanel buttonPanel;

	public EditRequirementController(RequirementsPanel panel) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.panel = panel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final RequirementModel newReq = panel.getModel();
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + newReq.getId(),  HttpMethod.POST);
		request.setBody(newReq.toJSON());
		request.addObserver(new EditRequirementModelRequestObserver(this));
		request.send();
	}

	/**
	 * Called by Observer when it receives a success confirmation
	 *
	 * @param req Requirement received by Observer
	 */
	public void receivedUpdateConfirmation(RequirementModel req) {
		panel.setStatus("Requirement Updated");
	}

}

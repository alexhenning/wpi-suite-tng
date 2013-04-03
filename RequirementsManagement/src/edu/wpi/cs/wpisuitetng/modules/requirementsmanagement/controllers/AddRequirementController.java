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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreateRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller responds to a click of an add button to create a new
 * Requirement to send to the server
 * @author Tim Calvert
 *
 */
@SuppressWarnings("serial")
public class AddRequirementController extends AbstractAction implements ActionListener {
	
	private final RequirementsPanel panel;
	//private final JPanel buttonPanel;

	public AddRequirementController(RequirementsPanel panel) {
		this.panel = panel;
	}
	
	/* 
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(panel.validateFields()){
			final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.PUT);
			request.setBody(panel.getModel().toJSON());
			request.addObserver(new CreateRequirementModelRequestObserver(this));
			request.send();
		}
	}
	
	public void receivedAddConfirmation(RequirementModel req) {
		DB.getSingleRequirement(""+req.getId(), new SingleRequirementCallback() {
			@Override public void callback(RequirementModel req) {
				panel.updateModel(req);
			}
		});
		panel.setStatus("Requirement saved!");
	}

}

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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;


import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement.RequirementsPanel;
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
	
	/** The requirements panel */
	private final RequirementsPanel panel;

	/**
	 * Constructor
	 * @param panel The requirement panel that contains the requirement being sent to the server
	 */
	public AddRequirementController(RequirementsPanel panel) {
		this.panel = panel;
	}
	

	/**
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * sends the requirement to the server
	 *
	 * @param e Action that called this
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
	
	/**
	 * Receives confirmation that server got the requirement
	 *
	 * @param req the requirement model sent
	 */
	public void receivedAddConfirmation(RequirementModel req) {
		DB.getSingleRequirement(""+req.getId(), new SingleRequirementCallback() {
			@Override public void callback(RequirementModel req) {
				panel.updateModel(req);
			}
		});
		panel.setStatus("Requirement saved!");
	}

}

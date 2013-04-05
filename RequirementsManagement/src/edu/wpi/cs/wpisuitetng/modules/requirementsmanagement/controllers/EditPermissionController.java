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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreatePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditPermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to an action to edit (update) a user permission
 *
 * @author vpatara
 *
 */
public class EditPermissionController implements ActionListener {

	private final PermissionsPanel panel;

	public EditPermissionController(PermissionsPanel panel) {
		this.panel = panel;
	}

	/*
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions",  HttpMethod.POST);
		request.setBody(panel.getUpdatedModel().toJSON());
		request.addObserver(new EditPermissionRequestObserver(this));
		request.send();
	}

	public void receivedEditConfirmation(Permissions profile) {
		DB.getSinglePermission(profile.getUsername(), new SinglePermissionCallback() {
			@Override
			public void callback(Permissions profile) {
				panel.updateAllPermissionsList();
			}
		});
		panel.resetUpdateFields();
	}
}

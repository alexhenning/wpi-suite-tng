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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreatePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 *
 * Handles saving permissions to the server
 * @author TODO
 *
 */
public class AddPermissionController implements ActionListener {

	/** The permissions panel */
	private final PermissionsPanel panel;

	/**
	 * constructor
	 * @param panel the permissions panel
	 */
	public AddPermissionController(PermissionsPanel panel) {
		this.panel = panel;
	}


	/**
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Saves the permissions to the server
	 *
	 * @param e the action that called this
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.setAddPermissionStatus("");
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions",  HttpMethod.PUT);
		request.setBody(panel.getNewModel().toJSON());
		request.addObserver(new CreatePermissionRequestObserver(this));
		request.send();
	}

	/**
	 * Response if everything worked
	 *
	 * @param profile
	 */
	public void receivedAddConfirmation(Permissions profile) {
		DB.getSinglePermission(profile.getUsername(), new SinglePermissionCallback() {
			@Override
			public void callback(Permissions profile) {
				panel.updateAllPermissionsList();
			}
		});
		panel.resetCreationFields();
		panel.setAddPermissionStatus("New permission added for "
				+ profile.getUsername() + " with "
				+ profile.getPermissionLevel().toString());
	}

	/**
	 * Response if there was a problem
	 *
	 */
	public void receivedAddError() {
		panel.setAddPermissionStatus("Invalid username, username already exists, or an internal error occurred");
	}
}

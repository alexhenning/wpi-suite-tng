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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
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

	/** The permissions panel */
	private final PermissionsPanel panel;

	/**
	 * Constructs the controller for updating a permission model
	 *
	 * @param panel the permissions panel where the action is taking place
	 */
	public EditPermissionController(PermissionsPanel panel) {
		this.panel = panel;
	}

	/**
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Edit the user permission in the server
	 *
	 * @param e the action that called this
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions",  HttpMethod.POST);
		request.setBody(panel.getUpdatedModel().toJSON());
		request.addObserver(new EditPermissionRequestObserver(this));
		request.send();
	}

	/**
	 * Confirm that the permission has been updated
	 *
	 * @param profile the newly updated permission
	 */
	public void receivedEditConfirmation(Permissions profile) {
		panel.updateAllPermissionsList();
		panel.resetUpdateFields();
	}
}

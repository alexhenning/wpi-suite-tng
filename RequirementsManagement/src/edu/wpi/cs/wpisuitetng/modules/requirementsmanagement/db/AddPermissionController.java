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
 *    William Terry
 *    jlmegin
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreatePermissionRequestObserver;

/**
 * Responds to an action to add a user permission to the server
 *
 * @author vpatara
 *
 * @version $Revision: 1.0 $
 */
public class AddPermissionController implements ActionListener {

	/** The permissions panel */
	private final PermissionsPanel panel;

	/**
	 * Constructs the controller for adding a new permission model
	 *
	 * @param panel the permissions panel where the action is taking place
	 */
	public AddPermissionController(PermissionsPanel panel) {
		this.panel = panel;
	}

	/**
	 * This will be called when the user clicks an add button
	
	 *
	 * @param e the action that called this
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * Saves the permissions to the server */
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.setAddPermissionStatus("");
		DB.addSinglePermission(panel.getNewModel(), new CreatePermissionRequestObserver(this));
	}

	/**
	 * Responds if the new permission has been added
	 *
	 * @param profile the newly added permission
	 */
	public void receivedAddConfirmation(Permissions profile) {
		panel.updateAllPermissionsList();
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

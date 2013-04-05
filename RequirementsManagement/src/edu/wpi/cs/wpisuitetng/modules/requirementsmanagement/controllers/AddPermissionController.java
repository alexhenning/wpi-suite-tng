package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreatePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class AddPermissionController implements ActionListener {

	private final PermissionsPanel panel;

	public AddPermissionController(PermissionsPanel panel) {
		this.panel = panel;
	}

	/*
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		panel.setAddPermissionStatus("");
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions",  HttpMethod.PUT);
		request.setBody(panel.getNewModel().toJSON());
		request.addObserver(new CreatePermissionRequestObserver(this));
		request.send();
	}

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

	public void receivedAddError() {
		panel.setAddPermissionStatus("Invalid username, username already exists, or an internal error occurred");
	}
}

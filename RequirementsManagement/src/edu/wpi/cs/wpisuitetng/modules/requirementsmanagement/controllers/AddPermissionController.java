package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreatePermissionRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class AddPermissionController implements ActionListener {

		private final PermissionsPanel panel;
		//private final JPanel buttonPanel;

		public AddPermissionController(PermissionsPanel panel) {
			this.panel = panel;
		}
		
		/* 
		 * This will be called when the user clicks an add button
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Button "+((JButton)e.getSource()).getText()+" pushed");
//			if(panel.validateFields()){
			final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions",  HttpMethod.PUT);
			request.setBody(panel.getModel().toJSON());
			request.addObserver(new CreatePermissionRequestObserver(this));
			request.send();
//			}
		}
		
		public void receivedAddConfirmation(Permissions profile) {
			DB.getSinglePermission(""+profile.getId(), new SinglePermissionCallback() {
				@Override public void callback(Permissions profile) {
//					panel.updateModel();
				}
			});
//			panel.setStatus("Profile saved!");
		}


}

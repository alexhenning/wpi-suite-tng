package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreateRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

public class AddPermissionsController implements ActionListener {

		private final PermissionsPanel panel;
		//private final JPanel buttonPanel;

		public AddPermissionsController(PermissionsPanel panel) {
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
//				final Request request = Network.getInstance().makeRequest("requirementsmanagement/permissions",  HttpMethod.PUT);
//				request.setBody(panel.getModel().toJSON());
//				request.addObserver(new CreatePermissionsModelRequestObserver(this));
//				request.send();
//			}
		}
		
//		public void receivedAddConfirmation(Permissions profile) {
//			DB.getSinglePermissions(""+profile.getId(), new SingleProfileCallback() {
//				@Override public void callback(Permissions profile) {
//					panel.updateModel();
//				}
//			});
//			panel.setStatus("Profile saved!");
//		}


}

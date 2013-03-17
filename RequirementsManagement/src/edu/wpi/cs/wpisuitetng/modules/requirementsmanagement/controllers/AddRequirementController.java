/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller responds to a click of an add button to create a new
 * Requirement to send to the server
 * @author Tim
 *
 */
public class AddRequirementController implements ActionListener {

	public AddRequirementController() {
		
	}
	/* 
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// this is still missing a lot of info, but it can't be filled in until we have some GUI info
		
		final Request request = Network.getInstance().makeRequest("requirementsmanager/requirementmodel",  HttpMethod.PUT);
		request.setBody(new RequirementModel().toJSON());
		request.addObserver(new AddRequirementRequestObserver(this));
		request.send();
	}

}

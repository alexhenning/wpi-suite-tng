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
 * responds to a click even to delete a requirement
 * @author Tim
 *
 */
public class DeleteRequirementController implements ActionListener {

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// this is still missing a lot of info, but it can't be filled in until we have some GUI info
		
		final Request request = Network.getInstance().makeRequest("requirementsmanager/requirementmodel",  HttpMethod.PUT);
		request.setBody(new RequirementModel().toJSON());
		request.addObserver(new DeleteRequirementRequestObserver(this));
		request.send();
	}

}

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
 * Controller responds to a click even to retrieve Requirements from the db
 * @author Tim
 *
 */
public class GetRequirementController implements ActionListener {

	public GetRequirementController() {
		
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO this is still missing a lot of info, but it can't be filled in until we have some GUI info
		
		final Request request = Network.getInstance().makeRequest("requirementsmanager/requirementmodel",  HttpMethod.GET);
		request.setBody(new RequirementModel().toJSON());
		request.addObserver(new RetrieveRequirementModelRequestObserver(this));
		request.send();
	}

}

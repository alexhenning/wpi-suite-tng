/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to a click event to edit a requirement
 * @author Tim Calvert
 *
 */
public class EditRequirementController implements ActionListener {
	
	private final JanewayModule mainBoard;
	//private final JPanel buttonPanel;

	public EditRequirementController(JPanel buttonPanel, JanewayModule mB) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.mainBoard = mB;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO need to get the ID somehow, but that's not possible under our current set up
		final Integer reqReleaseNumber        = Integer.parseInt(mainBoard.releasefield.getText());
		final RequirementStatus reqStatus     = RequirementStatus.valueOf(mainBoard.statusfield.getText().toUpperCase());  // might not work, we'll need a better system
		final RequirementPriority reqPriority = RequirementPriority.LOW;  // default
		final String reqName                  = mainBoard.namefield.getText();
		final String reqDescription           = mainBoard.descriptionfield.getText();
		final String reqEstimate              = "Unknown"; // aren't retrievable with our current GUI
		final String reqEffort                = "Unknown"; // so just some default stuff
		
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.POST);
		request.setBody(new RequirementModel(reqReleaseNumber, reqStatus, reqPriority,
				reqName, reqDescription, reqEstimate, reqEffort).toJSON());
		request.addObserver(new EditRequirementModelRequestObserver(this));
		request.send();
	}

}

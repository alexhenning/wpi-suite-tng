/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreateRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.JanewayModule;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller responds to a click of an add button to create a new
 * Requirement to send to the server
 * @author Tim Calvert
 *
 */
public class AddRequirementController implements ActionListener {
	
	private final JanewayModule mainBoard;
	//private final JPanel buttonPanel;

	public AddRequirementController(JPanel buttonPanel, JanewayModule mB) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.mainBoard = mB;
	}
	/* 
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// No need to add ID.  This particular instance is not being stored
		// and the one returned by the db will have ID filled in
		final Integer reqReleaseNumber        = Integer.parseInt(mainBoard.releasefield.getText());
		final RequirementStatus reqStatus     = RequirementStatus.NEW;
		final RequirementPriority reqPriority = RequirementPriority.LOW;
		final String reqName                  = mainBoard.namefield.getText();
		final String reqDescription           = mainBoard.descriptionfield.getText();
		final String reqEstimate              = "Unknown"; // aren't retrievable with our current GUI
		final String reqEffort                = "Unknown"; // so just some default stuff
		final User reqCreator                 = null;
		final User reqAssignee                = null;
		final Date reqCreationDate            = new Date();
		final Date reqLastModifiedDate        = reqCreationDate;
		
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.PUT);
		request.setBody(new RequirementModel(-1, reqReleaseNumber.intValue(), reqStatus, reqPriority,
				reqName, reqDescription, reqEstimate, reqEffort, reqCreator, reqAssignee, reqCreationDate, reqLastModifiedDate).toJSON());
		request.addObserver(new CreateRequirementModelRequestObserver(this));
		request.send();
	}
	
	public void receivedAddConfirmation(RequirementModel req) {
		mainBoard.addRequirement(req);
	}

}

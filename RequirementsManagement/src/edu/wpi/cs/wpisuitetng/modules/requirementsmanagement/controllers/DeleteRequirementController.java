/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.DeleteRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to a click even to delete a requirement
 * @author Tim Calvert
 *
 */
public class DeleteRequirementController extends AbstractAction implements ActionListener {
	// TODO This system currently does not have enough information to function properly
	//      I'd suggest that we don't try and demonstrate this yet
	
	private final RequirementsPanel panel;
	//private final JPanel buttonPanel;
	
	public DeleteRequirementController(RequirementsPanel panel) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.panel = panel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.DELETE);
		request.setBody(panel.getModel().toJSON());
		request.addObserver(new DeleteRequirementModelRequestObserver(this));
		request.send();
	}
	
	public void receivedDeleteConfirmation(int Id) {
		// TODO: mainBoard.deleteRequirement(Id);
	}

}

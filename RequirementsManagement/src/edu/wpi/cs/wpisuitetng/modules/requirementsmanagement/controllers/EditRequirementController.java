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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.EditRequirementPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.EditRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to a click event to edit a requirement
 * @author Tim Calvert
 *
 */
@SuppressWarnings("serial")
public class EditRequirementController extends AbstractAction implements ActionListener {
	
	private final RequirementsPanel panel;
	//private final JPanel buttonPanel;

	public EditRequirementController(RequirementsPanel panel) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.panel = panel;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final String idToEdit = "";
		final RequirementModel newReq = panel.getModel();
		newReq.setId(Integer.getInteger(idToEdit).intValue());
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + idToEdit,  HttpMethod.POST);
		request.setBody(newReq.toJSON());
		request.addObserver(new EditRequirementModelRequestObserver(this));
		request.send();
	}

	public void receivedUpdateConfirmation(RequirementModel req) {
		panel.setStatus("Requirement Updated");
	}

}

/**
 * 
 */
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.RetrieveRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller responds to a click even to retrieve Requirements from the db
 * @author Tim Calvert
 *
 */
public class GetRequirementController extends AbstractAction implements ActionListener {
	// TODO This currently gets all requirements.

	protected List<RequirementModel> data = null;
	
	private final ListRequirementsPanel panel;
	//private final JPanel buttonPanel;

	public GetRequirementController(ListRequirementsPanel panel) {
		//this.buttonPanel = buttonPanel;  /* not needed at the moment */
		this.panel = panel;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
				
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel",  HttpMethod.GET);
		// no need for a body
		request.addObserver(new RetrieveRequirementModelRequestObserver(this));
		request.send();
	}
	
	public void receivedGetConfirmation(List<RequirementModel> reqs) {
		receivedData(reqs);
	}
	
	public void receivedData(List<RequirementModel> reqs) {	
		if (reqs.size() > 0) {
			// save the data
			this.data = reqs;

			// put the data in the table
			Object[][] entries = new Object[reqs.size()][5];
			int i = 0;
			for(RequirementModel req : reqs) {
				entries[i][0] = String.valueOf(req.getId());
				entries[i][1] = req.getName();
				if (req.getStatus() != null) {
					entries[i][2] = req.getStatus().toString();
				}
				else {
					entries[i][2] = "Error: Status set to null";
				}
				if (req.getPriority() != null) {
					entries[i][3] = req.getPriority().toString();
				}
				else {
					entries[i][3] = "";
				}
				entries[i][4] = req.getEstimate();
				i++;
				}
			panel.getTable().setData(entries);
			panel.getTable().fireTableStructureChanged();
		}
		else {
			// do nothing, there are no requirements
		}
	}

}

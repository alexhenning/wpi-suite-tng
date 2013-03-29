package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.IterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CreateIterationRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Controller responds to a click of an add button to create a new
 * Requirement to send to the server
 * @author Tim Calvert
 * @author Jacob Palnick
 *
 */
@SuppressWarnings("serial")
public class AddIterationController extends AbstractAction implements ActionListener {
	
	private final IterationPanel panel;
	//private final JPanel buttonPanel;

	public AddIterationController(IterationPanel panel) {
		this.panel = panel;
	}
	
	/* 
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration",  HttpMethod.PUT);
		request.setBody(panel.getModel().toJSON());
		request.addObserver(new CreateIterationRequestObserver(this));
		request.send();
	}
	
	public void receivedAddConfirmation(Iteration req) {
//		DB.getSingleRequirement(""+req.getId(), new SingleIterationCallback() {
//			@Override public void callback(Iteration req) {
//				panel.updateModel(req);
//			}
//		});
		panel.close();//setStatus("Requirement saved!");
	}

}

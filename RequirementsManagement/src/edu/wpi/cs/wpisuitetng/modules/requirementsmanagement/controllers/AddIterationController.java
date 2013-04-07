/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tim Calvert
 *    Jacob Palnick
 *    Tim DeFreitas
 ******************************************************************************/

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
 * Iteartion to send to the server
 * @author Tim Calvert
 * @author Jacob Palnick
 *
 */
@SuppressWarnings("serial")
public class AddIterationController extends AbstractAction implements ActionListener {
	
	/** The panel for iterations */
	private final IterationPanel panel;
	//private final JPanel buttonPanel;

	/**
	 * Constructor
	 * @param panel the iteration panel being created
	 */
	public AddIterationController(IterationPanel panel) {
		this.panel = panel;
	}
	
	
	/**
	 * This will be called when the user clicks an add button
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 *
	 * @param e the action performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(panel.validateFields()) {
			final Request request = Network.getInstance().makeRequest("requirementsmanagement/iteration",  HttpMethod.PUT);
			request.setBody(panel.getModel().toJSON());
			request.addObserver(new CreateIterationRequestObserver(this));
			request.send();
		}
	}
	
	/**
	 * Confirm that the iteration was added, closes the panel
	 *
	 * @param req
	 */
	public void receivedAddConfirmation(Iteration iter) {
		DB.updateIteration(iter, new SingleIterationCallback() {
			@Override public void callback(Iteration iter) {
				panel.updateModel(iter);
			}
		});
		panel.setStatus("Iteration saved!");
	}
	
	/**
	 * Returns iteration panel that controller is operating on
	 */
	public IterationPanel getPanel(){
		return this.panel;
	}
}

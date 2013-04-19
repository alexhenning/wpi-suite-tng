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
 *    Tim DeFreitas
 *    vpatara
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement.SplitRequirementTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.SplitRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to a click to split a requirement to be sent to the server
 *
 * @author vpatara
 */
@SuppressWarnings("serial")
public class SplitRequirementController extends AbstractAction implements ActionListener {

	/** The requirement splitting panel */
	private final SplitRequirementTab splitPanel;

	/**
	 * Constructs the controller for splitting a requirement
	 *
	 * @param splitPanel The panel that contains the requirement being split
	 */
	public SplitRequirementController(SplitRequirementTab splitPanel) {
		this.splitPanel = splitPanel;
	}

	/**
	 * Called when the user clicks the split button, which then sends the split
	 * requirement to the server
	 *
	 * @param e Action that called this
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Make sure the fields are valid (e.g. non-empty) before sending
		if (splitPanel.validateFields()) {
			final Request request = Network.getInstance().makeRequest(
					"requirementsmanagement/requirementmodel", HttpMethod.PUT);
			request.setBody(splitPanel.getChildModel().toJSON());
			request.addObserver(new SplitRequirementModelRequestObserver(this));
			request.send();
		}
	}

	/**
	 * Receives confirmation that server whether the request was successful
	 *
	 * @param success whether the split request was successful
	 * @param childId the id of the split child requirement, or -1 if failure
	 */
	public void receivedSplitConfirmation(boolean success, int childId) {
		// Simple passes the result to the panel
		splitPanel.reportNewChild(success, childId);
	}
}

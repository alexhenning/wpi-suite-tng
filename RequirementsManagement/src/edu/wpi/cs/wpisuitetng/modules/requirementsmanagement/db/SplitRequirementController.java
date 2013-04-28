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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.SplitRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Responds to a click to split a requirement to be sent to the server
 *
 * @author vpatara
 * @author Tim Calvert
 * @author Tim Defreitas
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class SplitRequirementController extends AbstractAction implements ActionListener {

	/** The requirement splitting panel */
	private final RequirementsPanel requirementsPanel;

	/**
	 * Constructs the controller for splitting a requirement
	 *
	 * @param panel The panel that contains the requirement being split
	 */
	public SplitRequirementController(RequirementsPanel panel) {
		requirementsPanel = panel;
	}

	/**
	 * Called when the user clicks the split button, which then sends the split
	 * requirement to the server
	 *
	 * @param e Action that called this
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final Request request = Network.getInstance().makeRequest(
				"requirementsmanagement/requirementmodel", HttpMethod.PUT);
		request.setBody(requirementsPanel.getChildModel().toJSON());
		request.addObserver(new SplitRequirementModelRequestObserver(this));
		request.send();
	}

	/**
	 * Receives confirmation that server whether the request was successful
	 *
	 * @param childModel the split child requirement, or null if failure
	 */
	public void receivedSplitConfirmation(RequirementModel childModel) {
		// Simple passes the resulted child to the panel
		requirementsPanel.reportSplitChild(childModel);
	}
}

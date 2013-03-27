/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListSingleRequirementPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 *
 * Controller responds to a click event to retrieve a single requirement from the db
 * @author Tim Calvert
 *
 */
public class GetSingleRequirementController implements ActionListener {


	private final JTextField field;
	private final MainTabController tabController;
	
	/**
	 * Default constructor
	 * @param panel The panel containing all the GUI elements necessary for creation
	 */
	public GetSingleRequirementController(MainTabController tabController, JTextField field) {
		this.tabController = tabController;
		this.field = field;
	}
	
	/**
	 * Action performed upon click event
	 *
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO get id from user input
		final String idToLookup = field.getText();
		
		final Request request = Network.getInstance().makeRequest("requirementsmanagement/requirementmodel/" + idToLookup, HttpMethod.GET);
		request.addObserver(new RetrieveSingleRequirementRequestObserver(this));
		request.send();
	}
	
	public void receivedGetSingleConfirmation(RequirementModel req) {
		tabController.addEditRequirementTab(req);
	}

}

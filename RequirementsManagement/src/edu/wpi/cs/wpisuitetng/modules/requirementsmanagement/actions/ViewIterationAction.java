/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Josh
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;

/**
 * Action to bring up the list requirement tab
 * @author Josh
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class ViewIterationAction extends AbstractAction {
	
	/** the main tab controller */
	private final MainTabController controller;
	
	/**
	 * Create a ListRequirementsAction
	 * @param controller When the action is performed, controller.addListRequirementsTab() is called
	 */
	public ViewIterationAction(MainTabController controller) {
		super("View Iteration");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_L);
	}
	/**
	 * Add the list requirement tab to the main tab controller
	 *
	 * @param arg0 the action that happened
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.addViewIterationTab();
	}

}

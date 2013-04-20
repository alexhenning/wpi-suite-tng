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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;

/**
 * Action to bring up the list requirement tab
 * 
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class ListRequirementsAction extends AbstractAction {
	
	/** the main tab controller */
	private final MainTabController controller;
	
	/**
	 * Create a ListRequirementsAction
	 * @param controller When the action is performed, controller.addListRequirementsTab() is called
	 */
	public ListRequirementsAction(MainTabController controller) {
		super("List Requirements");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_L);
	}
	/**
	 * Add the list requirement tab to the main tab controller
	 *
	 * @param arg0 the action that happened
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.addListRequirementsTab();
	}

}

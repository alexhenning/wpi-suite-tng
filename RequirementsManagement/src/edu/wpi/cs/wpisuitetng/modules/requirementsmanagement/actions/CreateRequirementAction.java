/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 *    JPage
 ******************************************************************************/


package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;

/**
 *
 * Action that calls {@link MainTabController#addCreateRequirementTab()}, default mnemonic key is C
 * adds the create Requirement tab
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class CreateRequirementAction extends AbstractAction {

	/** The main tab controller */
	private final MainTabController controller;
	
	/**
	 * Create a CreateRequirementAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public CreateRequirementAction(MainTabController controller) {
		super("Create Requirement");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_C);
	}
	
	/**
	 * Add the create requirement tab to the main tab controller
	 *
	 * @param e The action event that occured
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addCreateRequirementTab();
	}

}

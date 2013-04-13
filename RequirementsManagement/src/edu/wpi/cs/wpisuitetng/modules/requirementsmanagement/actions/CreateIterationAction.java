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
 * Action for adding the create iteration tab
 *
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class CreateIterationAction extends AbstractAction{


	/**
	 * the main tab controller being used
	 */
	private final MainTabController controller;
	
	/**
	 * Create a CreateIterationAction
	 * @param controller When the action is performed, controller.addCreateIterationTab() is called
	 */
	public CreateIterationAction(MainTabController controller) {
		super("Create Iteration");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_I);
	}
	
	/**
	 * The action performed, adds a createIterationTab to the main tab controller
	 *
	 * @param e The action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addCreateIterationTab();
	}
}

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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;

/**
 *
 * Description goes here
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class ListSingleRequirementAction extends AbstractAction {

	private final MainTabController controller;
	
	/**
	 * Create a GetSiAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public ListSingleRequirementAction(MainTabController controller) {
		super("List Single Requirement");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_L);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.addListSingleRequirementTab();
	}

}

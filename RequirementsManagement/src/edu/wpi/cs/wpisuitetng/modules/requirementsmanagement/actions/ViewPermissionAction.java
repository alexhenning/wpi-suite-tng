/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;

/**
 * Action to create a view for permission management
 *
 * @author vpatara
 *
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class ViewPermissionAction extends AbstractAction {
	
	/** the main tab controller */
	private final MainTabController controller;
	
	/**
	 * Creates a ViewPermissionAction
	 * @param controller When the action is performed, controller.addCreatePermissionTab() is called
	 */
	public ViewPermissionAction(MainTabController controller) {
		super("View Permission");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_P);
	}
	
	/**
	 * Add the permissions tab to the main tab controller
	 *
	 * @param e the action performed
	 * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addPermissionTab();
	}
}

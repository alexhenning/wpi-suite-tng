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
 *    William Terry
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ViewPermissionAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;

/**
 * User-permission management toolbar view for RequirementsManagement
 *
 * @author vpatara
 *
 */
@SuppressWarnings("serial")
public class ManagementToolbarView extends ToolbarGroupView {

	private JButton permissionButton;

	/**
	 * Create a ToolbarView for management.
	 *
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public ManagementToolbarView(MainTabController tabController) {
		super("Management");

		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		content.setLayout(layout);
		content.setOpaque(false);

		// Setup button panel
		permissionButton = new JButton("User Permissions");
		permissionButton.setAction(new ViewPermissionAction(tabController)); // TODO: create permission action
		content.add(permissionButton);

		// Construct a new toolbar group to be added to the end of the toolbar
		add(content);

		// Calculate the width of the toolbar
		Double toolbarGroupWidth = permissionButton.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		setPreferredWidth(toolbarGroupWidth.intValue());
	}
}

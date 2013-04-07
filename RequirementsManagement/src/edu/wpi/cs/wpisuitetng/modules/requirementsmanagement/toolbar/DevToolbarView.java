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
 *    Chris Casola
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ListRequirementsAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;

/**
 * The Requirements Management tab's toolbar panel.
 * Always has a group of global commands (Create Requirement, Create Iteration, Search, List Requirements).
 */
@SuppressWarnings("serial")
public class DevToolbarView extends ToolbarGroupView {

	/** button to list requirements */
	private JButton listReq;
	
	/**
	 * Constructor
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public DevToolbarView(MainTabController tabController) {
		super("Development");

		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout  = new GridBagLayout();
		content.setLayout(layout);
		content.setOpaque(false);
		
		// Setup button panel
		listReq = new JButton("List Requirements");
		listReq.setAction(new ListRequirementsAction(tabController));
		content.add(listReq);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		add(content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = listReq.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		setPreferredWidth(toolbarGroupWidth.intValue());
	}

}

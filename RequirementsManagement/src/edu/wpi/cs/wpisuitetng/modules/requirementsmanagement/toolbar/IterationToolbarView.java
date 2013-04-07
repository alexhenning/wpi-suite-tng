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
	 *    Josh
	 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ViewIterationAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;

	/**
 * The Iteration tab's toolbar panel.
 */
@SuppressWarnings("serial")
public class IterationToolbarView extends ToolbarGroupView {

	/** button to list requirements */
	private JButton iterView;
	
	/**
	 * Constructor
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public IterationToolbarView(MainTabController tabController) {
		super("Iteration");

		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout  = new GridBagLayout();
		content.setLayout(layout);
		content.setOpaque(false);
		
		// Setup button panel
		iterView = new JButton("View Iterations");
		iterView.setAction(new ViewIterationAction(tabController));
		content.add(iterView);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		add(content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = iterView.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		setPreferredWidth(toolbarGroupWidth.intValue());
	}

}

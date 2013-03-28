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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.CreateIterationAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.CreateRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	private JButton createDefect;
	private JButton searchDefects;
	private JPlaceholderTextField searchField;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public ToolbarView(final MainTabController tabController) {

		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout  = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(layout);
		content.setOpaque(false);
		
		JButton Crtreq = new JButton("Create Requirement");
		Crtreq.setAction(new CreateRequirementAction(tabController));
		
		JButton CrtIteration = new JButton("Create Iteration");
		CrtIteration.setAction(new CreateIterationAction(tabController));
				
		// Construct the create defect button
		createDefect = new JButton();
		//createDefect.setAction(new CreateDefectAction(tabController));
		
		// Construct the search button
		searchDefects = new JButton("Lookup by ID");
		
		// Construct the search field
		searchField = new JPlaceholderTextField("Lookup by ID", 10);
		
		searchDefects.setAction(new AbstractAction() {
			@Override public void actionPerformed(ActionEvent event) {
				DB.getSingleRequirement(searchField.getText(), new SingleRequirementCallback() {
					@Override public void callback(RequirementModel req) {
						tabController.addEditRequirementTab(req);
					}
				});
			}
		});
		searchDefects.setText("Search Requirements");
		//searchField.addActionListener(new LookupDefectController(tabController, searchField, this));
		
		// Add buttons to the content panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		content.add(Crtreq, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		content.add(CrtIteration, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		//content.add(createDefect);
		content.add(searchField, c);
		c.gridx = 1;
		content.add(searchDefects, c);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home", content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = searchField.getPreferredSize().getWidth() + searchDefects.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);
	}

}
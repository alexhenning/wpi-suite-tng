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
 *    vpatara
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.CurrentUserPermissionManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * The Requirements Management tab's toolbar panel.
 * Always has a group of global commands (Create Requirement, Create Iteration, Search, List Requirements).
 * @author Andrew Hurle
 * @author Chris Casola
 */
@SuppressWarnings("serial")
public class ToolbarView extends DefaultToolbarView {

	/** requirement-creation button */
	private JButton createRequirement;
	/** iteration-creation button */
	private JButton createIteration;
	/** search requirements button */
	private JButton searchRequirements;
	/** search field for searching requirements */
	private JPlaceholderTextField searchField;
	
	/**
	 * Constructor
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

		createRequirement = new JButton("Create Requirement");
		createRequirement.setAction(new CreateRequirementAction(tabController));
		createRequirement.setEnabled(false);

		createIteration = new JButton("Create Iteration");
		createIteration.setAction(new CreateIterationAction(tabController));
		createIteration.setEnabled(false);

		// Construct the search button
		searchRequirements = new JButton("Lookup by ID");

		// Construct the search field
		searchField = new JPlaceholderTextField("Lookup by ID", 10);

		searchRequirements.setAction(new AbstractAction() {
			@Override public void actionPerformed(ActionEvent event) {
				DB.getSingleRequirement(searchField.getText(), new SingleRequirementCallback() {
					@Override public void callback(RequirementModel req) {
						tabController.addEditRequirementTab(req);
					}
				});
			}
		});
		searchRequirements.setText("Search Requirements");

		// Add buttons to the content panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		content.add(createRequirement, c);
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		content.add(createIteration, c);
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		content.add(searchField, c);
		c.gridx = 1;
		content.add(searchRequirements, c);

		// Construct a new toolbar group to be added to the end of the toolbar
		ToolbarGroupView toolbarGroup = new ToolbarGroupView("Home", content);

		// Calculate the width of the toolbar
		Double toolbarGroupWidth = searchField.getPreferredSize().getWidth() + searchRequirements.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		toolbarGroup.setPreferredWidth(toolbarGroupWidth.intValue());
		addGroup(toolbarGroup);

		// Allow access to users with certain permission levels
		CurrentUserPermissionManager.getInstance().addCallback(
				new SinglePermissionCallback() {
			@Override
			public void failure() {} // No need to implement

			@Override
			public void callback(Permissions profile) {
				switch (profile.getPermissionLevel()) {
				case ADMIN:
					// Administrator can do everything
					createIteration.setEnabled(true);
					createRequirement.setEnabled(true);
					break;

				case UPDATE:
					// "Update" can only create a new requirement (TODO: check this)
					createRequirement.setEnabled(true);
					break;

				default:
					// "None" can't do anything
					break;
				}
			}
		});
	}

}

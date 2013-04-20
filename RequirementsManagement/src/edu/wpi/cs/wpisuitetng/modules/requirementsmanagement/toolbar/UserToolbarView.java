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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;

/**
 * The Requirements Management tab's toolbar panel to display information about
 * the current user
 *
 * @author vpatara
 */
@SuppressWarnings("serial")
public class UserToolbarView extends ToolbarGroupView {

	/** Text fields for the current user's info */
	private JTextField usernameField;
	private JTextField permissionField;

	/**
	 * Constructs the toolbar panel displaying info about the current user
	 *
	 * @param tabController MainTabController with which this view should open tabs
	 */
	public UserToolbarView(final MainTabController tabController) {
		super("Session");

		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout  = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(layout);
		content.setOpaque(false);

		// Fields to be displayed on the toolbar view
		JLabel usernameLabel = new JLabel("Username: ");
		JLabel permissionLabel = new JLabel("Permission: ");
		usernameField = new JTextField("user?");
		usernameField.setEditable(false);
		usernameField.setOpaque(true);
		permissionField = new JTextField("permission?");
		permissionField.setEditable(false);
		permissionField.setOpaque(true);

		// Add display fields to the content panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridy = 0;
		c.gridx = 0;
		content.add(usernameLabel, c);
		c.gridx = 1;
		c.gridwidth = 2;
		content.add(usernameField, c);

		// Another line
		c.gridy = 1;
		c.gridx = 0;
		c.gridwidth = 1;
		content.add(permissionLabel, c);
		c.gridx = 1;
		content.add(permissionField, c);

		// Add the content panel into the toolbar
		add(content);

		// Calculate the width of the toolbar
		double usernameWidth = usernameLabel.getPreferredSize().getWidth()
				+ usernameField.getPreferredSize().getWidth();
		double permissionWidth = permissionLabel.getPreferredSize().getWidth()
				+ permissionField.getPreferredSize().getWidth();
		Double preferredWidth = ((usernameWidth >= permissionWidth) ? usernameWidth : permissionWidth) + 40;
		setPreferredWidth(preferredWidth.intValue());
	}
}

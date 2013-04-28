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

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.CurrentUserPermissionManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * The Requirements Management tab's toolbar panel to display information about
 * the current user
 *
 * @author vpatara
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class UserToolbarView extends ToolbarGroupView {

	/** Text fields (labels) for the current user's info */
	private JLabel usernameField;
	private JLabel permissionField;
	/** Labels for the fields above */
	private JLabel usernameLabel;
	private JLabel permissionLabel;

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
		content.setLayout(layout);
		content.setOpaque(false);

		// Fields to be displayed on the toolbar view
		usernameLabel = new JLabel("Username: ");
		permissionLabel = new JLabel("Permission: ");
		usernameField = new JLabel("?");
		permissionField = new JLabel("NONE");

		// Add display fields to the content panel
		GridBagConstraints c = new GridBagConstraints();
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

		// Displays the stub message
		setUserPermission("user?", "permission?");
		// .. until the new profile is ready
		CurrentUserPermissionManager.getInstance().addCallback(
				new SinglePermissionCallback() {
			@Override
			public void callback(Permissions profile) {
				setUserPermission(profile.getUsername(), profile.getPermissionLevel().toString());
			}
			@Override
			public void failure() {} // Never gets called
		});
	}

	/**
	 * Sets current username and permission level and display on the toolbar
	 *
	 * @param username The current user's username
	
	 * @param permission String
	 */
	private void setUserPermission(String username, String permission) {
		usernameField.setText(username);
		permissionField.setText(permission);

		// Calculate the width of the toolbar
		double usernameWidth = usernameLabel.getPreferredSize().getWidth()
				+ usernameField.getPreferredSize().getWidth();
		double permissionWidth = permissionLabel.getPreferredSize().getWidth()
				+ permissionField.getPreferredSize().getWidth();
		Double preferredWidth = ((usernameWidth >= permissionWidth) ? usernameWidth : permissionWidth) + 40;
		setPreferredWidth(preferredWidth.intValue());
	}
}

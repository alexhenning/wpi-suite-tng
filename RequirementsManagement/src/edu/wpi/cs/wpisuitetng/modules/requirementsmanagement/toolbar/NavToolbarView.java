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

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.CreateReleaseNumberAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ListRequirementsAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ViewIterationAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ViewPermissionAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;

/**
 * The Requirements Management tab's toolbar panel.
 * Always has a group of global commands (Create Requirement, Create Iteration, Search, List Requirements).
 */
@SuppressWarnings("serial")
public class NavToolbarView extends ToolbarGroupView {

	/** button to list requirements */
	private JButton showReports, iterView, permissionButton, viewReleaseNumbers;
	
	/**
	 * Constructor
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public NavToolbarView(final MainTabController tabController) {
		super("Navigation");

		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout  = new GridBagLayout();
		content.setLayout(layout);
		content.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		
		// Add button
		showReports = new JButton("Show Reports");
		showReports.setAction(new ShowReportsAction(tabController));
		content.add(showReports, c);
		
		iterView = new JButton("View Iterations");
		iterView.setAction(new ViewIterationAction(tabController));
		c.gridy = 1;
		content.add(iterView, c);
		
		permissionButton = new JButton("User Permissions");
		permissionButton.setAction(new ViewPermissionAction(tabController));
		c.gridy = 2;
		content.add(permissionButton, c);
		
		viewReleaseNumbers = new JButton("View Release Numbers");
		viewReleaseNumbers.setAction(new CreateReleaseNumberAction(tabController));
		c.gridx = 1;
		c.gridy = 0;
		content.add(viewReleaseNumbers, c);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		add(content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = permissionButton.getPreferredSize().getWidth() + 
				viewReleaseNumbers.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		setPreferredWidth(toolbarGroupWidth.intValue());
	}

	class ShowReportsAction extends AbstractAction {
		MainTabController tabController;
		public ShowReportsAction(MainTabController tabController){
			super("Show Reports");
			this.tabController = tabController;
		}
		@Override public void actionPerformed(ActionEvent arg0) {
			tabController.addShowReportsTab();
		}
		}
}

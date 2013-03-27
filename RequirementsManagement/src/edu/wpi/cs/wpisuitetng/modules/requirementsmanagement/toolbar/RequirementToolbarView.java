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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.CreateRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.EditRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ListRequirementsAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ListSingleRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 * The Defect tab's toolbar panel.
 * Always has a group of global commands (Create Defect, Search).
 */
@SuppressWarnings("serial")
public class RequirementToolbarView extends ToolbarGroupView {

	private JButton cancelButton;
	private JButton closeButton;
	private JButton deleteButton;
	
	/**
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public RequirementToolbarView(final MainTabController tabController, final RequirementsTab tab) {
		super("Requirements");

		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout  = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(layout);
		content.setOpaque(false);
		
		// Add cancel button
		cancelButton = new JButton("Cancel");
		cancelButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//if (tab.getRequirementPanel().getEditMode() == Mode.CREATE) {
					tabController.closeCurrentTab();
				//} else {
				//	tab.getRequirementPanel().refreshModel();
				//}
			}
		});
		cancelButton.setText("Cancel");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		content.add(cancelButton, c);
		
		// Add close button
		closeButton = new JButton("Complete!");
		closeButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RequirementModel model = tab.getRequirementPanel().getModel();
				model.setStatus(RequirementStatus.COMPLETE);
				DB.updateRequirements(model, new SingleRequirementCallback() {
					@Override
					public void callback(RequirementModel req) {
						tabController.closeCurrentTab();
						tabController.addListRequirementsTab();
					}
				});
			}
		});
		closeButton.setText("Complete!");
		c.gridy = 1;
		content.add(closeButton, c);
		
		// Add delete button
		deleteButton = new JButton("Delete");
		deleteButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RequirementModel model = tab.getRequirementPanel().getModel();
				model.setStatus(RequirementStatus.DELETED);
				DB.updateRequirements(model, new SingleRequirementCallback() {
					@Override
					public void callback(RequirementModel req) {
						tabController.closeCurrentTab();
					}
				});
			}
		});
		c.gridy = 2;
		deleteButton.setText("Delete");
		content.add(deleteButton, c);
		
		// Construct a new toolbar group to be added to the end of the toolbar
		add(content);
		
		// Calculate the width of the toolbar
		Double toolbarGroupWidth = closeButton.getPreferredSize().getWidth() + 40; // 40 accounts for margins between the buttons
		setPreferredWidth(toolbarGroupWidth.intValue());
	}

}

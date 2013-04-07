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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 * The Requirements Management tab's toolbar panel. Provides cancel, close, and delete buttons for creating and/or editing requirements
 * Always has a group of global commands (Create Requirement, Create Iteration, Search, List Requirements).
 */
@SuppressWarnings("serial")
public class RequirementToolbarView extends ToolbarGroupView {

	/** cancel button */
	private JButton cancelButton;
	/** close button */
	private JButton closeButton;
	/** delete button */
	private JButton deleteButton;
	/** the coresponding requirement's tab */
	private RequirementsTab tab;
	
	/**
	 * Constructor
	 * Create a ToolbarView.
	 * @param tabController The MainTabController this view should open tabs with
	 */
	public RequirementToolbarView(final MainTabController tabController, final RequirementsTab tab) {
		super("Requirements");
		this.tab = tab;

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
				tabController.closeCurrentTab();
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
				if (model.getStatus().equals(RequirementStatus.COMPLETE)) {
					model.setStatus(RequirementStatus.OPEN);
				} else {
					model.setStatus(RequirementStatus.COMPLETE);
				}
				DB.updateRequirements(model, new SingleRequirementCallback() {
					@Override
					public void callback(RequirementModel req) {
						if (req.getStatus().equals(RequirementStatus.COMPLETE)) {
							tabController.closeCurrentTab();
							tabController.addListRequirementsTab();
						} else {
							tab.getRequirementPanel().updateModel(req);
						}
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
				if (model.getStatus().equals(RequirementStatus.IN_PROGRESS)) return;
				if (model.getStatus() != RequirementStatus.DELETED) {
					model.setStatus(RequirementStatus.DELETED);
					DB.updateRequirements(model, new SingleRequirementCallback() {
						@Override
						public void callback(RequirementModel req) {
							tabController.closeCurrentTab();
							tabController.addListRequirementsTab();
						}
					});
				} else {
					model.setStatus(RequirementStatus.OPEN);
					DB.updateRequirements(model, new SingleRequirementCallback() {
						@Override
						public void callback(RequirementModel req) {
							tab.getRequirementPanel().updateModel(req);
						}
					});
				}
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
	
	/**
	 * update buttons displayed based on the mode
	 *
	 * @param mode the mode to set the buttons off of
	 * @param req the requirement being created/editied
	 */
	public void update(Mode mode, RequirementModel req) {
		if (mode.equals(Mode.EDIT)) {
			closeButton.setEnabled(true);
			deleteButton.setEnabled(true);
			if (req.getStatus().equals(RequirementStatus.IN_PROGRESS)) {
				deleteButton.setEnabled(false);
			} else {
				deleteButton.setEnabled(true);
			}
			if (req.getStatus().equals(RequirementStatus.COMPLETE)) {
				closeButton.setText("Reopen");
			} else {
				closeButton.setText("Complete!");
			}
			if (req.getStatus().equals(RequirementStatus.DELETED)) {
				deleteButton.setText("Restore");
				closeButton.setEnabled(false);
			} else {
				deleteButton.setText("Delete");
				closeButton.setEnabled(true);
			}
		} else {
			closeButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}

}

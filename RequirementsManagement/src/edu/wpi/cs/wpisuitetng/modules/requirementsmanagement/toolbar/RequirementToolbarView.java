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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.CanCloseRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.CloseSubRequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement.RequirementsTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.CloseSubRequirementModelRequestObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * The Requirements Management tab's toolbar panel. Provides cancel, close, and delete buttons for creating and/or editing requirements
 * Always has a group of global commands (Create Requirement, Create Iteration, Search, List Requirements).
 * @author Andrew Hurle
 * @author Chris Casola
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
				if (!model.getSubRequirements().isEmpty()) {
					//Check that the sub requirements are closed
					DB.canCloseRequirements(new CanCloseCallback(model, tabController), ""+model.getId());
				} else {
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
				if (!model.getSubRequirements().isEmpty()) return;
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
	
	class CanCloseCallback implements CanCloseRequirementCallback {
		RequirementModel model;
		MainTabController tabController;

		public CanCloseCallback(RequirementModel model, final MainTabController tabController) {
			super();
			this.model = model;
			this.tabController = tabController;
		}

		@Override
		public void callback(boolean result) {
			// TODO Auto-generated method stub
			if(result){
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
			} else {
				//TODO what to do if false...
				
				boolean closeSub = false;
				//TODO ask user if they want to close the sub requirements
				int input = JOptionPane.showConfirmDialog(tab,
						"Do you want to lose these changes?", 
						"Unsaved Changes", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
System.out.println("Input to close: "+input);
closeSub = (input == JOptionPane.YES_OPTION);
				if(closeSub) {
					final Request request = Network.getInstance().makeRequest("Advanced/requirementsmanagement/requirementmodel/closeSub/"+model.getId(),  HttpMethod.GET);
					request.addObserver(new CloseSubRequirementModelRequestObserver(new CloseSubRequirementsCallback() {
						
						@Override
						public void callback(boolean result) {
							// TODO Auto-generated method stub
							
						}
					}));
					request.send();

				}
			}
		}
		
	}
	
	/**
	 * update buttons displayed based on the mode
	 *
	 * @param mode the mode to set the buttons off of
	 * @param req the requirement being created/editied
	 */
	public void update(Mode mode, RequirementModel req) {
		if (mode.equals(Mode.EDIT)) {
			closeButton.setEnabled(req.getStatus().equals(RequirementStatus.IN_PROGRESS) || req.getStatus().equals(RequirementStatus.COMPLETE));
			closeButton.setText((req.getStatus().equals(RequirementStatus.COMPLETE) ? "Reopen" : "Complete!"));
			deleteButton.setEnabled(req.getSubRequirements().isEmpty());
			deleteButton.setText((req.getStatus().equals(RequirementStatus.DELETED) ? "Restore" : "Delete"));
		} else {
			closeButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}

}

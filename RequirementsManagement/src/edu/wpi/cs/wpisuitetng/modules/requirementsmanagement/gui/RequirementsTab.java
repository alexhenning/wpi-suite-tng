/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    JPage
 *    Chris Casola
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.RequirementToolbarView;

/**
 * This view is responsible for showing the form for creating or viewing a new requirements.
 */
@SuppressWarnings("serial")
public class RequirementsTab extends JPanel implements IToolbarGroupProvider {

	/** group of buttons to show */
	RequirementToolbarView buttonGroup;
	/** the save button */
	private JButton saveButton;
	/** the main panel */
	private RequirementsPanel mainPanel;
	/** the containing tab*/
	private Tab containingTab;
	/** is input enabled*/
	private boolean inputEnabled = true;

	/**
	 * Constructs a new CreateRequirementView where the user can enter the data for a new requirement.
	 */
	public RequirementsTab(MainTabController tabController) {
		this(tabController, new RequirementModel(), Mode.CREATE, null);
	}

	/**
	 * Constructs a new RequirementView where the user can view (and edit) a requirement.
	 * 
	 *TODO: Fix the documentation of this method
	 * 
	 * @param requirement	The Requirement to show.
	 * @param editMode	The editMode for editing the Requirement
	 * @param tab		The Tab holding this RequirementView (can be null)
	 */
	public RequirementsTab(MainTabController tabController, RequirementModel requirement, Mode editMode, Tab tab) {
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		
		buttonGroup = new RequirementToolbarView(tabController, this);
		
		containingTab.setIcon(new ImageIcon());
		if(editMode == Mode.CREATE) {
			containingTab.setTitle("Create Requirement");
			containingTab.setToolTipText("Create a new Requirement");
		} else {
			setEditModeDescriptors(requirement);
		}
		
		// If this is new, set the creator
		if (editMode == Mode.CREATE) {
			// TODO: requirement.setCreator(new User("", ConfigManager.getConfig().getUserName(), "", -1));
		}
		
		// Instantiate the main create panel
		mainPanel = new RequirementsPanel(this, requirement, editMode);
		this.setLayout(new BorderLayout());
		this.add(mainPanel, BorderLayout.CENTER);

		// Instantiate the save button and add it to the button panel
		saveButton = new JButton();
		// TODO: saveButton.setAction(new SaveChangesAction(controller));
		
		// Instantiate the button panel
		buttonGroup.getContent().add(saveButton);
		buttonGroup.setPreferredWidth(150);
	}
	
	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	public void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		saveButton.setEnabled(enabled);
		mainPanel.setInputEnabled(enabled);
	}
	
	/**
	 * Returns whether or not input is enabled.
	 * 
	 * @return whether or not input is enabled.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

	/**
	 * Returns the main panel with the data fields
	 * 
	 * @return the main panel with the data fields
	 */
	public RequirementsPanel getRequirementPanel() {
		return mainPanel;
	}
	
	/**
	 * get the button group
	 *
	 * @return the button group
	 */
	@Override
	public ToolbarGroupView getGroup() {
		return buttonGroup;
	}
	
	/**
	 * @param requirement Set the tab title and group name according to this Requirement
	 */
	public void setEditModeDescriptors(RequirementModel requirement) {
		containingTab.setTitle("Requirement #" + requirement.getId());
		// TODO: containingTab.setToolTipText("View Requirement " + requirement.getTitle());
		buttonGroup.setName("Edit Requirement");
	}

}

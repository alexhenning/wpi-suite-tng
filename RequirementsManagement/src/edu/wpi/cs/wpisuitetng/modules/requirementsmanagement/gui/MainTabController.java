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
 *    JPage
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

/**
 * Contributors:
 * AHurle
 * JPage
 */

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * Controls the behavior of a given MainTabView.
 * Provides convenient public methods for controlling the MainTabView.
 * Keep in mind that this controller is visible as a public field in the module.
 */
public class MainTabController {
	
	/** the main tab view */
	private final MainTabView view;
	/** a list requirements tab */
	private ListRequirementsTab listReqsView = null;
	
	/**
	 * @param view Create a controller that controls this MainTabView
	 */
	public MainTabController(MainTabView view) {
		this.view = view;
		this.view.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				MainTabController.this.onMouseClick(event);
			}
		});
	}
	
	/**
	 * Adds a tab.
	 * 
	 * @param title			The tab's title.
	 * @param icon			The icon for the tab.
	 * @param component		The component that will be displayed inside the tab.
	 * @param tip			The tooltip to display when the cursor hovers over the tab title.
	 * @return				The created Tab
	 */
	public Tab addTab(String title, Icon icon, Component component, String tip) {
		view.addTab(title, icon, component, tip);
		int index = view.getTabCount() - 1;
		view.setSelectedIndex(index);
		return new Tab(view, view.getTabComponentAt(index));
	}
	
	/**
	 * @return Same as addTab(null, null, null, null)
	 */
	public Tab addTab() {
		return addTab(null, null, null, null);
	}
	
	/**
	 * Adds a tab that displays the given requirement in the given mode
	 * @param requirement The requirement to display
	 * @param mode The Mode to use
	 */
	private Tab addRequirementTab(RequirementModel requirement, Mode mode) {
		Tab tab = addTab();
		RequirementsTab view = new RequirementsTab(this, requirement, mode, tab);
		tab.setComponent(view);
		view.requestFocus();
		return tab;
	}
	
	/**
	 * create a tab for adding an iteration
	 *
	 * @return the tab created
	 */
	public Tab addCreateIterationTab() {
		Iteration iteration = new Iteration();
		Tab tab = addTab();
		IterationTab view = new IterationTab(this, iteration, tab);
		tab.setComponent(view);
		view.requestFocus();
		return tab;
	}
	
	/**
	 * create a tab for editing permissions
	 *
	 * @return the tab created
	 */
	public Tab addPermissionTab() {
		// If the tab is already opened, switch to that tab.
		for (int i = 0; i < this.view.getTabCount(); i++) {

			// TODO: May have to refactor "Manage Permissions"
			if (view.getTitleAt(i).equals("Manage Permissions")) {
				switchToTab(i);
				// TODO: figure out what to return
				return null;
			}
		}

		// Otherwise, create a new one.
		Permissions profile = new Permissions();
		Tab tab = addTab();
		PermissionsTab view = new PermissionsTab(this, profile, tab);
		tab.setComponent(view);
		view.requestFocus();
		return tab;
	}
	
	/**
	 * Adds a tab that displays the given requirement
	 * @param requirement the requirement to display
	 * @return The created Tab 
	 */
	public Tab addEditRequirementTab(RequirementModel requirement) {
		for (int i=0; i<view.getTabCount(); i++) {
			if (("Requirement #"+(requirement.getId())).equals(view.getTitleAt(i))) {
				switchToTab(i);
				return null;//TODO figure out what to return
			}
		}
		return addRequirementTab(requirement, Mode.EDIT);
	}
	
	/**
	 * Adds a tab that allows the user to create a new requirement
	 * @return The created Tab
	 */
	public Tab addCreateRequirementTab() {
		return addRequirementTab(new RequirementModel(), Mode.CREATE);
	}
	
	/**
	 * Add a change listener to the view this is controlling.
	 * @param listener the ChangeListener that should receive ChangeEvents
	 */
	public void addChangeListener(ChangeListener listener) {
		view.addChangeListener(listener);
	}
	
	/**
	 * Changes the selected tab to the tab left of the current tab
	 */
	public void switchToLeftTab() {
		if (view.getSelectedIndex() > 0) {
			switchToTab(view.getSelectedIndex() - 1);
		}
	}
	
	/**
	 * Changes the selected tab to the tab right of the current tab
	 */
	public void switchToRightTab() {
		switchToTab(view.getSelectedIndex() + 1);
	}
	
	/**
	 * Closes the currently active tab
	 */
	public void closeCurrentTab() {
		try {
			view.removeTabAt(view.getSelectedIndex());
		}
		catch (IndexOutOfBoundsException e) {
			// do nothing, tried to close tab that does not exist
		}
	}
	
	/**
	 * Changes the selected tab to the tab with the given index
	 * @param tabIndex the index of the tab to select
	 */
	private void switchToTab(int tabIndex) {
		try {
			view.setSelectedIndex(tabIndex);
		}
		catch (IndexOutOfBoundsException e) {
			// an invalid tab was requested, do nothing
		}
	}
	
	/**
	 * Close tabs upon middle clicks.
	 * @param event MouseEvent that happened on this.view
	 */
	private void onMouseClick(MouseEvent event) {
		// only want middle mouse button
		if(event.getButton() == MouseEvent.BUTTON2) {
			final int clickedIndex = view.indexAtLocation(event.getX(), event.getY());
			if(clickedIndex > -1) {
				view.removeTabAt(clickedIndex);
			}
		}
	}

	/**
	 * add a tab that lists all the requirements
	 *
	 * @return the created tab
	 */
	public Tab addListRequirementsTab() {
		if(listReqsView != null) {
			for (int i=0; i<view.getTabCount(); i++) {
				// TODO: If the tab name changes, will need to change the string
				// to match the tab name
				if (view.getTitleAt(i).equals("All Requirements")) {
					switchToTab(i);
					view.requestFocus();
					return null;
				}
			}

			// Should not reach here, but if so, let it create another tab
		}

		listReqsView = new ListRequirementsTab(null, this);
		Tab tab = addTab("All Requirements", new ImageIcon(), listReqsView,
							"List of requirements");
		tab.setComponent(listReqsView);
		listReqsView.requestFocus();
		view.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				listReqsView.mainPanel.updateAllRequirementList();
			}
		});

		return tab;
	}

	public void addShowReportsTab() {
		for (int i=0; i<view.getTabCount(); i++) {
			// TODO: If the tab name changes, will need to change the string
			// to match the tab name
			if (view.getTitleAt(i).equals("Reports")) {
				switchToTab(i);
				view.requestFocus();
				return;
			}
		}

		Tab tab = addTab();
		final ReportsTab reportsTab = new ReportsTab(this, tab);
		reportsTab.requestFocus();
		view.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				reportsTab.mainPanel.refresh();
			}
		});
	}
}

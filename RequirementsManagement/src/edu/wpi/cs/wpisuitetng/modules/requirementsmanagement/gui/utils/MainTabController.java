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
 *    Josh
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils;

/**
 * Contributors:
 * AHurle
 * JPage
 * Josh
 */

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.IterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.PermissionsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ReleaseNumberTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ReportsTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewIterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewReqTable;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewSingleIterationTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement.RequirementsTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.listeners.UpdateViewIterationOnFocusListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * Controls the behavior of a given MainTabView.
 * Provides convenient public methods for controlling the MainTabView.
 * Keep in mind that this controller is visible as a public field in the module.
 */
public class MainTabController {
	
	/** the main tab view */
	private final MainTabView mainView;
	/** a list requirements tab */
	private ScrollableTab<ListRequirementsPanel> listReqsView = null;
	
	/**
	 * @param view Create a controller that controls this MainTabView
	 */
	public MainTabController(MainTabView view) {
		this.mainView = view;
		this.mainView.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				MainTabController.this.onMouseClick(event);
			}
		});
	}
	
	class RequirementTabChangeListener implements ChangeListener {

		private RequirementsTab tab;
		
		public RequirementTabChangeListener(final RequirementsTab view) {
			super();
			this.tab = view;
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			if (MainTabController.this.mainView.getSelectedComponent() instanceof RequirementsTab) {
				RequirementsTab tmpTab = (RequirementsTab)MainTabController.this.mainView.getSelectedComponent();
				System.out.println("is match: " + (tab == tmpTab));
				if(tab == tmpTab) {
					tmpTab.getRequirementPanel().updateLists();
				}
			}
			boolean tabExists = false;
			for(int i=0; i<MainTabController.this.mainView.getComponentCount(); i++) {
				if (MainTabController.this.mainView.getComponent(i) == tab) tabExists = true;
			}
			if(!tabExists) {
				MainTabController.this.mainView.removeChangeListener(this);
			}
		}
		
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
		mainView.addTab(title, icon, component, tip);
		int index = mainView.getTabCount() - 1;
		mainView.setSelectedIndex(index);
		return new Tab(mainView, mainView.getTabComponentAt(index));
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
		final RequirementsTab view = new RequirementsTab(this, requirement, mode, tab);
		tab.setComponent(view);
		view.requestFocus();
		this.mainView.addChangeListener(new RequirementTabChangeListener(view));

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
		ScrollableTab<IterationPanel> view =
				new ScrollableTab<IterationPanel>(this, tab, "Add Iteration", new IterationPanel(iteration));
		tab.setComponent(view);
		view.requestFocus();
		return tab;
	}
	
	public Tab addCreateReleaseNumberTab() {
		// If the tab is already opened, switch to that tab.
		for (int i = 0; i < this.mainView.getTabCount(); i++) {
			// TODO: May have to refactor "View Iteration"
			if (mainView.getTitleAt(i).equals("Release Number")) {
				switchToTab(i);
				// TODO: figure out what to return
				return null;
			}
		}
				
		ReleaseNumber rn = new ReleaseNumber();
		Tab tab = addTab();
		ReleaseNumberTab view = new ReleaseNumberTab(this, rn, tab);
		tab.setComponent(view);
		view.requestFocus();
		return tab;
	}
	
	/**
	 * Adds a tab that displays the given requirement in the given mode
	 * @param requirement The requirement to display
	 * @param mode The Mode to use
	 */
	public Tab addIterationTab(Iteration iteration, String title) {
		if (iteration == null) {
			for (int i=0; i<mainView.getTabCount(); i++) {
				if (("View Backlog").equals(mainView.getTitleAt(i))) {
					switchToTab(i);
					return null;//TODO figure out what to return
				}
			}
		} else {
			for (int i=0; i<mainView.getTabCount(); i++) {
				if (("Edit "+(iteration.getIterationNumber())).equals(mainView.getTitleAt(i))) {
					switchToTab(i);
					return null;//TODO figure out what to return
				}
			}
		}
		Tab tab = addTab();
		ViewSingleIterationTab view = new ViewSingleIterationTab(this, iteration, tab, title);
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
		for (int i = 0; i < this.mainView.getTabCount(); i++) {

			// TODO: May have to refactor "Manage Permissions"
			if (mainView.getTitleAt(i).equals("Manage Permissions")) {
				switchToTab(i);
				// TODO: figure out what to return
				return null;
			}
		}

		// Otherwise, create a new one.
		Permissions profile = new Permissions();
		Tab tab = addTab();
		ScrollableTab<PermissionsPanel> view = 
				new ScrollableTab<PermissionsPanel>(this, tab, "Manage Permissions",
						new PermissionsPanel());
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
		for (int i=0; i<mainView.getTabCount(); i++) {
			if (("Requirement #"+(requirement.getId())).equals(mainView.getTitleAt(i))) {
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
		mainView.addChangeListener(listener);
	}
	
	/**
	 * Changes the selected tab to the tab left of the current tab
	 */
	public void switchToLeftTab() {
		if (mainView.getSelectedIndex() > 0) {
			switchToTab(mainView.getSelectedIndex() - 1);
		}
	}
	
	/**
	 * Changes the selected tab to the tab right of the current tab
	 */
	public void switchToRightTab() {
		switchToTab(mainView.getSelectedIndex() + 1);
	}
	
	/**
	 * Closes the currently active tab
	 */
	public void closeCurrentTab() {
		try {
			mainView.removeTabAt(mainView.getSelectedIndex());
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
			mainView.setSelectedIndex(tabIndex);
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
			final int clickedIndex = mainView.indexAtLocation(event.getX(), event.getY());
			if(clickedIndex > -1) {
				mainView.removeTabAt(clickedIndex);
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
			for (int i=0; i<mainView.getTabCount(); i++) {
				// TODO: If the tab name changes, will need to change the string
				// to match the tab name
				if (mainView.getTitleAt(i).equals("All Requirements")) {
					switchToTab(i);
					mainView.requestFocus();
					return null;
				}
			}
		}

		listReqsView = new ScrollableTab<ListRequirementsPanel>(this, null, "All Requirements",
				new ListRequirementsPanel());
		Tab tab = addTab("All Requirements", new ImageIcon(), listReqsView,
							"List of requirements");
		tab.setComponent(listReqsView);
		listReqsView.requestFocus();
		mainView.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(listReqsView.getPanel().getTable().getMode() == ViewReqTable.Mode.VIEW) {
					listReqsView.getPanel().updateAllRequirementList();
				}
			}
		});

		return tab;
	}

	public void addShowReportsTab() {
		for (int i=0; i<mainView.getTabCount(); i++) {
			// TODO: If the tab name changes, will need to change the string
			// to match the tab name
			if (mainView.getTitleAt(i).equals("Reports")) {
				switchToTab(i);
				mainView.requestFocus();
				return;
			}
		}

		Tab tab = addTab();
		final ReportsTab reportsTab = new ReportsTab(this, tab);
		reportsTab.requestFocus();
		mainView.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				reportsTab.getPanel().refresh();
			}
		});
	}

	
	/**
	 * add a tab that views iterations
	 *
	 * @return the created tab
	 */

	public Tab addViewIterationTab() {
		// If the tab is already opened, switch to that tab.
		for (int i = 0; i < this.mainView.getTabCount(); i++) {
			// TODO: May have to refactor "View Iteration"
			if (mainView.getTitleAt(i).equals("View Iteration")) {
				switchToTab(i);
				// TODO: figure out what to return
				return null;
			}
		}

		// Otherwise, create a new one.
		Tab tab = addTab();
		ScrollableTab<ViewIterationPanel> view
			= new ScrollableTab<ViewIterationPanel>(this, tab, "Add Iteration", new ViewIterationPanel());
		addChangeListener(new UpdateViewIterationOnFocusListener(view));
		tab.setComponent(view);
		view.requestFocus();
		return tab;
	}
}

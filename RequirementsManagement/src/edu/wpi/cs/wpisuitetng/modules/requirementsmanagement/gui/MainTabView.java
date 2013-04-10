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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 * This tabbed pane will appear as the main content of the Requirements tab.
 * It starts out showing the single Dashboard tab.
 */
@SuppressWarnings("serial")
public class MainTabView extends JTabbedPane {
	
	/**
	 * Constructor
	 */
	public MainTabView() {
		setTabPlacement(TOP);
		setTabLayoutPolicy(SCROLL_TAB_LAYOUT);
		setBorder(BorderFactory.createEmptyBorder(5, 3, 3, 3));
//		addTab("Dashboard", new ImageIcon(), new DashboardView(),
//		       "Your Dashboard - notifications, etc.");
	}
	
	/**
	 * insert a tab
	 *
	 * @param title title of the tab
	 * @param icon icon of the tab
	 * @param component the tab's component
	 * @param tip the tooltip for the tab
	 * @param index the tab's index
	 */
	@Override
	public void insertTab(String title, Icon icon, Component component, String tip, int index) {
		super.insertTab(title, icon, component, tip, index);
		// the Dashboard tab cannot be closed
		if(!(component instanceof DashboardView)
				&& !(component instanceof ListRequirementsTab)) {
			setTabComponentAt(index, new ClosableTabComponent(this));
		}
	}
	
	/**
	 *remove a tab
	 *
	 * @param index the index of the tab to be removed
	 */
	@Override
	public void removeTabAt(int index) {
		// if a tab does not have the close button UI, it cannot be removed
		if(getTabComponentAt(index) instanceof ClosableTabComponent) {
			// TODO: Hacky fix for requirements panel, make an interface and generalize
			if (getComponentAt(index) instanceof RequirementsTab) {
				System.out.println("Deleting requirement tab");
				if (((RequirementsTab) getComponentAt(index)).getRequirementPanel().hasUnsavedChanges()) {
					// 0 is OK, 2 is cancel
					int input = JOptionPane.showConfirmDialog(this,
															"Do you want to lose these changes?", 
															"Unsaved Changes", 
															JOptionPane.WARNING_MESSAGE);
					System.out.println("Input to close: "+input);
					if (input == 2) return;  
				}
			}
			
			super.removeTabAt(index);
		}
	}
	
	/**
	 * set the component of a tab
	 *
	 * @param index the index of the tab
	 * @param component what the tab's component should be set to
	 */
	@Override
	public void setComponentAt(int index, Component component) {
		super.setComponentAt(index, component);
		fireStateChanged(); // hack to make sure toolbar knows if component changes
	}
	
}

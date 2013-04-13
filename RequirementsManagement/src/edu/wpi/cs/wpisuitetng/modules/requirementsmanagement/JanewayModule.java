/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Joshua Morse
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.NavToolbarView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.ToolbarController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.ToolbarView;

/**
 * Tab for requirements management
 * 
 * @author Joshua Morse
 *
 */
public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	/** The panel for Requirements management */
	public JPanel buttonPanel = new JPanel();
	
	/** The main tab controller for this tab */
	private MainTabController mainTabController;
	/** The toolbar controller for this tab */
	private ToolbarController toolbarController;
	
	JTabbedPane tabPane = new JTabbedPane();
	
	/**
	 * The Constructor
	 */
	public JanewayModule() {
		MainTabView mainTabView = new MainTabView();
		mainTabController = new MainTabController(mainTabView);
		//mainTabController.addListRequirementsTab();
		
		ToolbarView toolbarView = new ToolbarView(mainTabController);

		// Add default toolbars
		toolbarController = new ToolbarController(toolbarView, mainTabController);
		toolbarController.setRelevant(new NavToolbarView(mainTabController), true);

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Requirements Management", new ImageIcon(), toolbarView, mainTabView);
		tabs.add(tab);
		
		tab.getMainComponent().addAncestorListener(new AncestorListener() {
			@Override public void ancestorRemoved(AncestorEvent e) {}
			@Override public void ancestorMoved(AncestorEvent e) {}
			@Override public void ancestorAdded(AncestorEvent e) {
				System.out.println("Showing all requirements.");
				mainTabController.addListRequirementsTab();
			}
		});
	}

	/**
	 * returns the name of the tab
	 *
	 * @return RequirementsManagment, the name of the tab
	 */
	@Override
	public String getName() {
		return "RequirementsManagement";
	}

	/**
	 * Return a list of tabs in this tab
	 *
	 * @return a list of the tabs in this tab
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}

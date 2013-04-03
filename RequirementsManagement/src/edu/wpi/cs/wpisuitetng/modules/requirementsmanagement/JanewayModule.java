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

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.DevToolbarView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.ManagementToolbarView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.ToolbarController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.ToolbarView;

public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	public JPanel buttonPanel = new JPanel();
	
	private MainTabController mainTabController;
	private ToolbarController toolbarController;
	
	JTabbedPane tabPane = new JTabbedPane();
	
	public JanewayModule() {
		MainTabView mainTabView = new MainTabView();
		mainTabController = new MainTabController(mainTabView);
		//mainTabController.addListRequirementsTab();
		
		ToolbarView toolbarView = new ToolbarView(mainTabController);

		// Add default toolbars
		toolbarController = new ToolbarController(toolbarView, mainTabController);
		toolbarController.setRelevant(new DevToolbarView(mainTabController), true);
		toolbarController.setRelevant(new ManagementToolbarView(mainTabController), true);

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Requirements Management", new ImageIcon(), toolbarView, mainTabView);
		tabs.add(tab);
	}

	@Override
	public String getName() {
		return "RequirementsManagement";
	}

	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
	}

}

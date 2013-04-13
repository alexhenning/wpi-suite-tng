/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    William Terry
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.DummyTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * Tab for permissions
 * @author William Terry
 *
 */
@SuppressWarnings("serial")
public class PermissionsTab extends JPanel implements IToolbarGroupProvider {
	/** the main tab controller */
	MainTabController tabController;
	/** tab that contains this*/
	Tab containingTab;
	/** permissions model */
	Permissions permission;
	/** scroll pane */
	JScrollPane mainPanelScrollPane;
	/** the main panel */
	PermissionsPanel mainPanel;
	

	/**
	 * Constructor
	 * @param tabController the main tab controller
	 * @param permission a permissions model
	 * @param tab the tab that contains this
	 */
	public PermissionsTab(MainTabController tabController, Permissions permission, Tab tab) {
		this.tabController = tabController;
		this.permission = permission;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("Manage Permissions");
		
		// Instantiate the main create iteration panel
		mainPanel = new PermissionsPanel(this);
		this.setLayout(new BorderLayout());
		mainPanelScrollPane = new JScrollPane(mainPanel);
		mainPanelScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		
		// Prevent content of scroll pane from smearing (credit: https://gist.github.com/303464)
		mainPanelScrollPane.getVerticalScrollBar().addAdjustmentListener(new java.awt.event.AdjustmentListener(){
			public void adjustmentValueChanged(java.awt.event.AdjustmentEvent ae){
				//SwingUtilities.invokeLater(new Runnable(){
				//	public void run(){
						mainPanelScrollPane.repaint();
				//	}
				//});
			}
		});
		
		this.add(mainPanelScrollPane, BorderLayout.CENTER);
		
	}


	@Override
	public ToolbarGroupView getGroup() {
		// TODO Auto-generated method stub
		return null;
	}
}

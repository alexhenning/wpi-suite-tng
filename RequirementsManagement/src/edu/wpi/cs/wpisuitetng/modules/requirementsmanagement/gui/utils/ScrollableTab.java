/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //Alex Henning
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;

/**
 *Class for creating and setting scrollable tabs
 *
 * @author Alex Henning

 *
 * @version $Revision: 1.0 $
 * @param <Panel> 
 */
@SuppressWarnings("serial")
public class ScrollableTab<Panel extends JComponent & ScrollablePanel> extends JPanel implements IToolbarGroupProvider {
	
	/** the main tab controller*/
	MainTabController tabController;
	/** tab that this tab resides*/
	Tab containingTab;
	/** the main scroll pane */
	JScrollPane mainPanelScrollPane;
	/** The main panel */
	Panel mainPanel;
	ToolbarGroupView toolbar;

	/**
	 * Constructor
	 * @param tabController the main tab controller
	 * @param tab the containing tab
	 * @param name the tab name
	 * @param panel the main panel to be put in this scrollable tab
	 */
	public ScrollableTab(MainTabController tabController, Tab tab, String name, Panel panel) {
		this.tabController = tabController;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle(name);
		
		// Set the panel panel
		System.out.println(name+": "+panel.toString());
		mainPanel = panel;
		panel.setTab(this);
		this.setLayout(new BorderLayout());
		mainPanelScrollPane = new JScrollPane(mainPanel);
		mainPanelScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		
		// Prevent content of scroll pane from smearing (credit: https://gist.github.com/303464)
		mainPanelScrollPane.getVerticalScrollBar().addAdjustmentListener(new java.awt.event.AdjustmentListener(){
			public void adjustmentValueChanged(java.awt.event.AdjustmentEvent ae){
				mainPanelScrollPane.repaint();
			}
		});
		
		this.add(mainPanelScrollPane, BorderLayout.CENTER);
	}

	/**
	
	 * @return The main panel that this tab contains */
	public Panel getPanel() {
		return mainPanel;
	}
	
	/**
	
	 * @return The main tab controller. */
	public MainTabController getTabController() {
		return tabController;
	}

	/**
	
	 * @return The toolbar * @see edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider#getGroup()
	 */
	@Override
	public ToolbarGroupView getGroup() {
		return toolbar;
	}

	/**
	 * Set toolbar group
	 * @param toolbar The toolbar to be set to
	 */
	public void setGroup(ToolbarGroupView toolbar) {
		this.toolbar = toolbar;
	}

}

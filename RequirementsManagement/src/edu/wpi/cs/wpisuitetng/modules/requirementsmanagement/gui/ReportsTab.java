/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO who did this?
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.util.Enumeration;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

/**
 *
 * The tab for iterations
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class ReportsTab extends JPanel implements IToolbarGroupProvider{
	
	/** the main tab controller*/
	MainTabController tabController;
	/** tab that this tab resides*/
	Tab containingTab;
	/** the main scroll pane */
	JScrollPane mainPanelScrollPane;
	/** The main panel */
	ReportsPanel mainPanel;
	

	/**
	 * Constructor
	 * @param tabController the main tab controller
	 * @param iteration an iteration
	 * @param tab the containing tab
	 */
	public ReportsTab(MainTabController tabController, Tab tab) {
		this.tabController = tabController;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("Reports");
		
		// Instantiate the main create iteration panel
		mainPanel = new ReportsPanel(this);
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
	 * This does not appear to be implemented
	 *
	 * @return
	 */
	@Override
	public ToolbarGroupView getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

}

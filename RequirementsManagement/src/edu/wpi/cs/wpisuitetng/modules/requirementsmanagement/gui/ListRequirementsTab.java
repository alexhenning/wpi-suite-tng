/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;

/**
 *
 * tab for listing all the requirements
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class ListRequirementsTab extends JPanel implements IToolbarGroupProvider {
	
	/** tab that contains this */
	Tab containingTab;
	/** the main tab controller*/
	MainTabController tabController;
	/** the main panel*/
	ListRequirementsPanel mainPanel;
	/** a panel scroll pane*/
	JScrollPane mainPanelScrollPane;

	/** Constructor
	 * @param tab the tab that contains this
	 * @param tabController the main tab controller
	 */
	public ListRequirementsTab(Tab tab, MainTabController tabController) {
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("All Requirements");
		
		this.tabController = tabController; 
		
		// Instantiate the main create requirement panel
		mainPanel = new ListRequirementsPanel(this);
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

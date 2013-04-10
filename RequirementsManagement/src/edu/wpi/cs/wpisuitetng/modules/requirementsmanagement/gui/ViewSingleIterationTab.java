/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Josh
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

/**
 *
 * The tab for iterations
 * @author Josh
 *
 */
@SuppressWarnings("serial")
public class ViewSingleIterationTab extends JPanel implements IToolbarGroupProvider{
	
	/** the main tab controller*/
	MainTabController tabController;
	/** tab that this tab resides*/
	Tab containingTab;
	/** the iteration */
	Iteration iteration;
	/** the main scroll pane */
	JScrollPane mainPanelScrollPane;
	/** The main panel */
	ViewSingleIterationPanel mainPanel;
	

	/**
	 * Constructor
	 * @param tabController the main tab controller
	 * @param iteration an iteration
	 * @param tab the containing tab
	 */
	public ViewSingleIterationTab(MainTabController tabController, Iteration iteration, Tab tab) {
		this.tabController = tabController;
		this.iteration = iteration;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("Add Iteration");
		
		// Instantiate the main create iteration panel
		mainPanel = new ViewSingleIterationPanel(this);
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

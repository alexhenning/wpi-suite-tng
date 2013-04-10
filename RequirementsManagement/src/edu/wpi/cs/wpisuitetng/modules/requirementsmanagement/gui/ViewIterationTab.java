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
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

/**
 * Tab for permissions
 * @author Josh
 *
 */
@SuppressWarnings("serial")
public class ViewIterationTab extends JPanel implements IToolbarGroupProvider {
	/** the main tab controller */
	MainTabController tabController;
	/** tab that contains this*/
	Tab containingTab;
	/** scroll pane */
	JScrollPane mainPanelScrollPane;
	/** the main panel */
	ViewIterationPanel mainPanel;
	

	/**
	 * Constructor
	 * @param tabController the main tab controller
	 * @param permission a permissions model
	 * @param tab the tab that contains this
	 */
	public ViewIterationTab(MainTabController tabController, Tab tab){
		this.tabController = tabController;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("View Iteration");
		
		// Instantiate the main create iteration panel
		mainPanel = new ViewIterationPanel(this);
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
		
		this.tabController.addChangeListener(new TabFocusListener(this));
		
		this.add(mainPanelScrollPane, BorderLayout.CENTER);
		
	}

	@Override
	public ToolbarGroupView getGroup() {
		// TODO Auto-generated method stub
		return null;
	}
}

class TabFocusListener implements ChangeListener {
	ViewIterationTab attentiveTab;
	
	TabFocusListener(ViewIterationTab attentiveTab){
		this.attentiveTab = attentiveTab;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(((JTabbedPane) e.getSource()).getSelectedComponent() == null){
			//TODO: find a way to remove this listener if tab has been closed
			//((JTabbedPane) e.getSource()).removeChangeListener(this);
		} else if( ((JTabbedPane) e.getSource()).getSelectedComponent().equals(attentiveTab) ){
			attentiveTab.mainPanel.updateAllIterationList();
		}
	}
}
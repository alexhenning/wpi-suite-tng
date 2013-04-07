/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

/**
 *
 * The tab for Release Numbers
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class ReleaseNumberTab extends JPanel implements IToolbarGroupProvider {

	/** the main tab controller */
	MainTabController tabController;
	/** tab that this tab resides in */
	Tab containingTab;
	/** main scroll pane */
	JScrollPane mainPanelScrollPane;
	/** a list of all ReleaseNumbers */
	List<ReleaseNumber> releaseNumbers;
	/** the current ReleaseNumber */
	ReleaseNumber releaseNum;
	/** the main panel */
	ReleaseNumberPanel mainPanel;
	
	public ReleaseNumberTab(MainTabController tabController, ReleaseNumber model, Tab tab) {
		this.tabController = tabController;
		this.containingTab = tab;
		this.releaseNum = model;
		
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("Create/Edit Release Numbers");
		
		// Instantiate the main create iteration panel
		mainPanel = new ReleaseNumberPanel(this);
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

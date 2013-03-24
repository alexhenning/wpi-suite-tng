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

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.CreateRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.EditRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ListRequirementsAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.ListSingleRequirementAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.DummyTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabView;

public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	public JPanel buttonPanel = new JPanel();
	
	private MainTabController mainTabController;
	
	JTabbedPane tabPane = new JTabbedPane();
	
	public JanewayModule() {
		MainTabView mainTabView = new MainTabView();
		mainTabController = new MainTabController(mainTabView);
		
		// Setup button panel
		buttonPanel.setLayout(new FlowLayout());
		JButton Crtreq = new JButton("Create Requirement");
		JButton listReq = new JButton("List Requirements");
		JButton listSReq = new JButton("Search Requirements");
		JButton editReq = new JButton("Edit REquirement");
		Crtreq.setAction(new CreateRequirementAction(mainTabController));
		listReq.setAction(new ListRequirementsAction(mainTabController));
		listSReq.setAction(new ListSingleRequirementAction(mainTabController));
		editReq.setAction(new EditRequirementAction(mainTabController));
		buttonPanel.add(Crtreq);
		buttonPanel.add(listReq);
		buttonPanel.add(listSReq);
		buttonPanel.add(editReq);

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Requirements Management", new ImageIcon(), buttonPanel, mainTabView);
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

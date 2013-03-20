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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.DummyTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabView;

public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	public JPanel buttonPanel = new JPanel();
	
	public JTextField namefield = new JTextField(30);
	public JTextField descriptionfield = new JTextField(30);
	String[] statusStrings = { "New" };
	public JComboBox statusfield = new JComboBox(statusStrings);
	public JTextField releasefield = new JTextField(30);
	public JTextField results = new JTextField(50);
	
	private MainTabController mainTabController;
	
	JTabbedPane tabPane = new JTabbedPane();
	
	JButton submit = new JButton("Submit");
	
	public JanewayModule() {
		MainTabView mainTabView = new MainTabView();
		mainTabController = new MainTabController(mainTabView);
		
		// Setup button panel
		buttonPanel.setLayout(new FlowLayout());
		JButton Crtreq = new JButton("Create Requirement");
		JButton Edtreq = new JButton("Edit Requirement");
		buttonPanel.add(Crtreq);
		buttonPanel.add(Edtreq);

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

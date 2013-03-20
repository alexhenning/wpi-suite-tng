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
	
	JTabbedPane tabPane = new JTabbedPane();
	
	JButton submit = new JButton("Submit");
	
	public JanewayModule() {
		
		//create main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());
		
		// Setup button panel
		buttonPanel.setLayout(new FlowLayout());
		JButton Crtreq = new JButton("Create Requirement");
		JButton Edtreq = new JButton("Edit Requirement");
		buttonPanel.add(Crtreq);
		buttonPanel.add(Edtreq);
		
		//name field
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());
		JTextArea nameArea = new JTextArea(1, 10);
		nameArea.setEditable(false);
		nameArea.append("name");
		namePanel.add(nameArea);
		namePanel.add(namefield);
		
		//description field
		JPanel descPanel = new JPanel();
		descPanel.setLayout(new FlowLayout());
		JTextArea descriptionArea = new JTextArea(1, 10);
		descriptionArea.setEditable(false);
		descriptionArea.append("Description");
		descPanel.add(descriptionArea);
		descPanel.add(descriptionfield);
		
		//status field
		JPanel statPanel = new JPanel();
		statPanel.setLayout(new FlowLayout());
		JTextArea statusArea = new JTextArea(1, 10);
		statusArea.setEditable(false);
		statusArea.append("Status");
		statPanel.add(statusArea);
		statPanel.add(statusfield);
		
		//release field
		JPanel relPanel = new JPanel();
		relPanel.setLayout(new FlowLayout());
		JTextArea releaseArea = new JTextArea(1, 10);
		releaseArea.setEditable(false);
		releaseArea.append("Release");
		relPanel.add(releaseArea);
		relPanel.add(releasefield);

		//submit panel
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new FlowLayout());
		
		//submit button
		submitPanel.add(submit);
		//results field
		submitPanel.add(results);
		
		//add subpanels to main panel
		mainPanel.add(namePanel);
		mainPanel.add(descPanel);
		mainPanel.add(statPanel);
		mainPanel.add(relPanel);
		mainPanel.add(submitPanel);
		
		//tab creation
		tabPane.addTab("Create Requirement", mainPanel);
		
		//Janeway tab code
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Requirements Management", new ImageIcon(), buttonPanel, tabPane);
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

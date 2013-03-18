package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

//import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.view.RequirementPanel; unworking atm

public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	public JanewayModule() {
		
		// Setup button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		JButton Crtreq = new JButton("Create Requirement");
		//Crtreq.addActionListener(new Controller(buttonPanel, this)); //add controllers
		JButton Edtreq = new JButton("Edit Requirement");
		//Edtreq.addActionListener(new Controller(buttonPanel, this)); //add controllers
		buttonPanel.add(Crtreq);
		buttonPanel.add(Edtreq);
		
		JPanel mainPanel = new JPanel();
		
		//name field
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());
		JTextArea nameArea = new JTextArea(1, 10);
		nameArea.setEditable(false);
		nameArea.append("name");
		mainPanel.add(nameArea);
		JTextField namefield = new JTextField(30);
		mainPanel.add(namefield);
		
		//description field
		JPanel descriptionPanel = new JPanel();
		descriptionPanel.setLayout(new FlowLayout());
		JTextArea descriptionArea = new JTextArea(1, 10);
		descriptionArea.setEditable(false);
		descriptionArea.append("Description");
		mainPanel.add(descriptionArea);
		JTextField descriptionfield = new JTextField(30);
		mainPanel.add(descriptionfield);
		
		//status field
		JPanel statusPanel = new JPanel();
		statusPanel.setLayout(new FlowLayout());
		JTextArea statusArea = new JTextArea(1, 10);
		statusArea.setEditable(false);
		statusArea.append("Status");
		mainPanel.add(statusArea);
		JTextField statusfield = new JTextField(30);
		mainPanel.add(statusfield);
		
		//release field
		JPanel releasePanel = new JPanel();
		releasePanel.setLayout(new FlowLayout());
		JTextArea releaseArea = new JTextArea(1, 10);
		releaseArea.setEditable(false);
		releaseArea.append("Release");
		mainPanel.add(releaseArea);
		JTextField releasefield = new JTextField(30);
		mainPanel.add(releasefield);

		//submit button
		JPanel submitPanel = new JPanel();
		JButton submit = new JButton("Submit");
		mainPanel.add(submit);
		
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Requirements Management", new ImageIcon(), buttonPanel, mainPanel);
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

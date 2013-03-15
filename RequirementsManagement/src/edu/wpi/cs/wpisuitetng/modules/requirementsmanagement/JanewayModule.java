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

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	public JanewayModule() {
		
		// Setup button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(new JButton("Func A"));
		buttonPanel.add(new JButton("Func B"));
		
		// Setup the main panel
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(new JLabel("Dummy Module"), BorderLayout.PAGE_START);
		mainPanel.add(new JTextField(), BorderLayout.CENTER);
		mainPanel.add(new JTextField(), BorderLayout.CENTER);
		
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

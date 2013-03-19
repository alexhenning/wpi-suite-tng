package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.EditRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

//import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.view.RequirementPanel; unworking atm

public class JanewayModule implements IJanewayModule {
	
	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	/** Local model of requirements */
	private static List<RequirementModel> requirements;
	
	public JPanel mainPanel = new JPanel();
	public JPanel buttonPanel = new JPanel();
	
	public JTextField namefield = new JTextField(30);
	public JTextField descriptionfield = new JTextField(30);
	public JTextField statusfield = new JTextField(30);
	public JTextField releasefield = new JTextField(30);
	
	public JButton submit = new JButton("Submit");
	
	public JanewayModule() {
		
		// Setup button panel
		buttonPanel.setLayout(new FlowLayout());
		JButton Crtreq = new JButton("Create Requirement");
		Crtreq.addActionListener(new AddRequirementController(buttonPanel, this)); //add controllers
		JButton Edtreq = new JButton("Edit Requirement");
		Edtreq.addActionListener(new EditRequirementController(buttonPanel, this)); //add controllers
		buttonPanel.add(Crtreq);
		buttonPanel.add(Edtreq);
		
		mainPanel.setLayout(new GridLayout(0,2));
		
		//name field
		JTextArea nameArea = new JTextArea(1, 10);
		nameArea.setEditable(false);
		nameArea.append("name");
		mainPanel.add(nameArea);
		mainPanel.add(namefield);
		
		//description field
		JTextArea descriptionArea = new JTextArea(1, 10);
		descriptionArea.setEditable(false);
		descriptionArea.append("Description");
		mainPanel.add(descriptionArea);
		mainPanel.add(descriptionfield);
		
		//status field
		JTextArea statusArea = new JTextArea(1, 10);
		statusArea.setEditable(false);
		statusArea.append("Status");
		mainPanel.add(statusArea);
		mainPanel.add(statusfield);
		
		//release field
		JTextArea releaseArea = new JTextArea(1, 10);
		releaseArea.setEditable(false);
		releaseArea.append("Release");
		mainPanel.add(releaseArea);
		mainPanel.add(releasefield);

		//submit button
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
	
	public static boolean addRequirement(RequirementModel req) {
		return requirements.add(req);
	}
	
	public void updateAllRequirementList(List<RequirementModel> reqs) {
		requirements.clear();
		for(RequirementModel req : reqs) {
			requirements.add(req);
		}
	}
	
	public void deleteRequirement(int Id) {
		for(RequirementModel req : requirements) {
			if(req.getId() == Id) {
				requirements.remove(req);
			}
		}
	}
	
	public void updateSingleRequirement(RequirementModel req) {
		for(int i = 0; i < requirements.size(); i++) {
			if(requirements.get(i).getId() == req.getId()) {
				requirements.remove(i);
				requirements.add(i, req);
			}
		}
	}

}

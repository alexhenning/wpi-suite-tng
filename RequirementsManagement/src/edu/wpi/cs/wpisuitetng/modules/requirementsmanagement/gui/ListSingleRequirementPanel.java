package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.GetSingleRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

@SuppressWarnings("serial")
public class ListSingleRequirementPanel extends JPanel {
	
	ListSingleRequirementTab parent;
	boolean inputEnabled;
	TextArea ta;
	public TextArea idArea;
	JButton search;

	/**
	 * Constructs a DefectPanel for creating or editing a given Defect.
	 * 
	 * @param parent	The parent DefectView.
	 * @param defect	The Defect to edit.
	 * @param mode		Whether or not the given Defect should be treated as if it already exists 
	 * 					on the server ({@link Mode#EDIT}) or not ({@link Mode#CREATE}).
	 */
	public ListSingleRequirementPanel(ListSingleRequirementTab parent) {
		this.parent = parent;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		
		// Populate the form with the contents of the Defect model and update the TextUpdateListeners.
		// TODO: updateFields();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		ta = new TextArea();
		idArea = new TextArea();
		search = new JButton("Lookup By ID");
		search.addActionListener(new GetSingleRequirementController(this));
		
		
		//add subpanels to main panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(ta, c);
		c.gridy = 1;
		add(idArea, c);
		c.gridy = 2;
		add(search, c);
	}
	
	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	protected void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		// TODO: implement
	}

	public void updateAllRequirementList(List<RequirementModel> reqs) {
		String output = "";
		for(RequirementModel req : reqs) {
			output = output + "Name: " + req.getName() + " (Status: " + req.getStatus() + ", ID: " + 
					req.getId()  + ") " + "   Description: " + req.getDescription() + "\n";
		}
		ta.setText(output);
	}
	
	public void addSingleRequirementToList(RequirementModel req) {
		String output = "";
		output = output + "Name: " + req.getName() + " (Status: " + req.getStatus() + ", ID: " + 
				req.getId()  + ") " + "   Description: " + req.getDescription() + "\n";
		ta.setText(output);
	}
}

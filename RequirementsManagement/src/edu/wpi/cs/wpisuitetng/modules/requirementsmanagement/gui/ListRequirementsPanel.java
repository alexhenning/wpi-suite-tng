package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Dimension;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.GetRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.LocalRequirementModels;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewReqTable;

public class ListRequirementsPanel extends JPanel {
	
	ListRequirementsTab parent;
	boolean inputEnabled;
	JTable table;
	ViewReqTable tableModel;
	
	String[] columnNames = {"Name", "Status", "ID", "Description"};


	public ListRequirementsPanel(ListRequirementsTab parent) {
		this.parent = parent;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		new GetRequirementController(this).actionPerformed(null);
		
		// Populate the form with the contents of the Defect model and update the TextUpdateListeners.
		// TODO: updateFields();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		setLayout(new GridLayout());
		
		tableModel = new ViewReqTable();
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(1, 1));
		table.setFillsViewportHeight(true);
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		add(scrollPane);
		
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

	public void updateAllRequirementList() {
		;
	}
	
	public void addSingleRequirementToList(RequirementModel req) {
		;
	}
	
	public ViewReqTable getTable(){
		return this.tableModel;
	}
}

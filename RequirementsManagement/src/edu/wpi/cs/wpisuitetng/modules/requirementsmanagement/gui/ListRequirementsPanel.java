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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewReqTable;

public class ListRequirementsPanel extends JPanel {
	
	ListRequirementsTab parent;
	boolean inputEnabled;
	TextArea ta;
	JTable table;
	
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
		//GridBagConstraints c = new GridBagConstraints();
		
		table = new JTable(new ViewReqTable());
		table.setPreferredScrollableViewportSize(new Dimension(1, 1));
		table.setFillsViewportHeight(true);
		
		table.addMouseListener(new MouseAdapter() {
			   public void mouseClicked(MouseEvent e) {
			      if (e.getClickCount() == 2) {
			         JTable target = (JTable)e.getSource();
			         int row = target.getSelectedRow();
			         int column = target.getSelectedColumn();
			         // do some action
			         }
			   }
			});
		
		JScrollPane scrollPane = new JScrollPane(table);
		
		ta = new TextArea();
		ta.setText("Testing");
		
		//add subpanels to main panel
	//	c.fill = GridBagConstraints.HORIZONTAL;
	//	c.gridx = 0;
	//	c.gridy = 0;
		//add(ta, c);
		//add(table, c);
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

	public void updateAllRequirementList(List<RequirementModel> reqs) {
		String output = "";
		for(RequirementModel req : reqs) {
			output = output + "Name: " + req.getName() + " (Status: " + req.getStatus() + ", ID: " + 
					req.getId()  + ") " + "   Description: " + req.getDescription() + "\n";
		}
		ta.setText(output);
	}
}

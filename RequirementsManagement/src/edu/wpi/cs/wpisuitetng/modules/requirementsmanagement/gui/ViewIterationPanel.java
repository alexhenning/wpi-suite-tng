/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    William Terry
 *    vpatara
 *    Josh
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel.CustomCellRenderer;

/**
 * GUI for a project manager to manage user permissions 
 *
 * @author William Terry
 *
 */
@SuppressWarnings("serial")
public class ViewIterationPanel extends JPanel {
	
	public static final int NAME = 0;
	public static final int STARTDATE = 1;
	public static final int ENDDATE = 2;
	public static final int ESTIMATE = 3;
	public static final int REQUIREMENTS = 4;
	public static final int ROWS = 5;
	
	/** the tab that made this */
	ViewIterationTab parent;
	JPanel topPanel;
	JTable table;
	ViewIterTable tableModel;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	/**
	 * constructor
	 * @param permissionsTab the tab that created this
	 */
	public ViewIterationPanel(ViewIterationTab iterTab){
		this.parent = iterTab;
		
		tableModel = new ViewIterTable();
		table = new JTable(tableModel);
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
		updateTable();
		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		//updateFields();
	}

	/**
	 *add the components to the view
	 *
	 */
	private void addComponents() {
		setLayout(new BorderLayout());
		
		table.setPreferredScrollableViewportSize(new Dimension(500, 100));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		add(scrollPane, BorderLayout.CENTER);

	}
	
	/**
	 * close the tab
	 *
	 */
	public void close() {
		parent.tabController.closeCurrentTab();
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

	/**
	 * Returns a boolean representing whether or not input is enabled for the ViewIterationPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the ViewIterationPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}
	
	   /**
     * updates table
     *TODO DYNAMICALLY UPDATE
     * @return data
     */
    public void updateTable() {
    	
    	Object[][] entries = {{1,2,3,4,5},{6,7,8,9,10}};
		int i = 0;
		int temp = 0;
		for(Object iter : entries) {
			entries[i][NAME] = temp++;
			entries[i][STARTDATE] = temp++;
			entries[i][ENDDATE] = temp++;
			entries[i][ESTIMATE] = temp++;
			entries[i][REQUIREMENTS] = temp++;
			i++;
		}
		getTable().setData(entries);
		getTable().fireTableStructureChanged();


		TableColumn column = null;
		for (i = 0; i < ROWS; i++) {
			column = table.getColumnModel().getColumn(i);
			if (i == NAME) {
				column.setPreferredWidth(300); //third column is bigger
			}
			else if (i == ESTIMATE) {
				column.setPreferredWidth(500);
			}
			else if (i == ENDDATE) {
				column.setPreferredWidth(700);
			}
			else {
				column.setPreferredWidth(200);
			}
		}
    }
    
	/**
	 * the table model
	 *
	 * @return the table model
	 */
	public ViewIterTable getTable(){
		return this.tableModel;
	}

}

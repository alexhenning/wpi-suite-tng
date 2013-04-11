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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

/**
 * GUI for a project manager to view and manage iterations 
 *
 * @author Josh
 * 
 */
@SuppressWarnings("serial")
public class ViewIterationPanel extends JPanel {
	
	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int STARTDATE = 2;
	public static final int ENDDATE = 3;
	public static final int ESTIMATE = 4;
	public static final int ROWS = 5;
	
	/** the tab that made this */
	ViewIterationTab parent;
	JPanel topPanel;
	JTable table;
	ViewIterTable tableModel;
	protected Iteration model;

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
		updateAllIterationList();
		
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
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2 ) {
                	if (table.getModel().getValueAt(table.getSelectedRow(),ID).toString().equals("0")) {
                		parent.tabController.addIterationTab(null, "View Backlog");
                	}
                	else DB.getSingleIteration(table.getModel().getValueAt(table.getSelectedRow(), ID).toString(),
                			new SingleIterationCallback() {
						@Override
						public void callback(Iteration iter) {
							parent.tabController.addIterationTab(iter, "Edit " + iter.getIterationNumber());
						}
                	});
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		add(scrollPane, BorderLayout.CENTER);

	}
	
	public void updateAllIterationList() {
		DB.getAllIterations(new UpdateTableCallback());
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
	
	class UpdateTableCallback implements IterationCallback {
		/**
		 * Callback function to populate the table with all the requirements
		 *
		 * @param reqs a list of all requirements
		 */
		@Override
		public void callback(List<Iteration> iterations) {
			if (iterations.size() > 0) {
				// put the data in the table
				Object[][] entries = new Object[iterations.size() + 1][ROWS];
				entries[0][ID] = 0;
				entries[0][NAME] = "Backlog";
				entries[0][STARTDATE] = "N/A";
				entries[0][ENDDATE] = "N/A";
				entries[0][ESTIMATE] = "N/A";
				int i = 1;
				for(Iteration iteration : iterations) {
					entries[i][ID] = iteration.getId();
					entries[i][NAME] = iteration.getIterationNumber();
					entries[i][STARTDATE] = iteration.getStartDate();
					entries[i][ENDDATE] = iteration.getEndDate();
					entries[i][ESTIMATE] = iteration.getEstimate();					
					i++;
				}
				getTable().setData(entries);
				getTable().fireTableStructureChanged();
			}
			else {
				// do nothing, there are no requirements
			}
		
			TableColumn column = null;
			for (int i = 0; i < ROWS; i++) {
				column = table.getColumnModel().getColumn(i);
				if (i == ID) {
					column.setPreferredWidth(1);
				}
				else if (i == NAME) {
					column.setPreferredWidth(700);
				}
				else {
					column.setPreferredWidth(350);
				}
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

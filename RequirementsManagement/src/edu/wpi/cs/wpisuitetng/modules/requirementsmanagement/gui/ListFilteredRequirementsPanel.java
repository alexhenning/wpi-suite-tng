/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Josh Morse
 *    James Megin
 *    Tim Calvert
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

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 *
 * Panel to list all of the requirements, and have an option to edit them in the list
 * @author Josh
 * @author Tim C
 * @author James
 *
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class ListFilteredRequirementsPanel extends JPanel {
	
	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int DESCRIPTION = 2;
	public static final int STATUS = 3;
	public static final int PRIORITY = 4;
	public static final int ESTIMATE = 5;
	public static final int RELEASE = 6;
	public static final int ROWS = 6;
	
	/** the parent that created this*/
	ViewSingleIterationPanel parent; 
	/** is inpute enabled*/
	JTable table;
	/** the model for the table*/
	ViewFilteredReqTable tableModel;
	/** button to bring the table into edit mode*/
	Object[][] data;
	
	/**
	 * Constructor
	 * @param parent the tab that made this
	 */
	public ListFilteredRequirementsPanel(final ViewSingleIterationPanel parent) {
		this.parent = parent;

		// Add all components to this panel
		addComponents();
		updateRequirementList();
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                	DB.getSingleRequirement((String) table.getModel().getValueAt(table.getSelectedRow(), ID),
                			new SingleRequirementCallback() {
						@Override
						public void callback(RequirementModel req) {
							parent.parent.getTabController().addEditRequirementTab(req);
						}
                	});
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
	}

	/**
	 * adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	
	 */
	protected void addComponents() {
		//borderlayout so the table can expand while the filter area remains constant
		setLayout(new BorderLayout());
		
		//create the table part of the GUI
		tableModel = new ViewFilteredReqTable();
		table = new JTable(tableModel);
		table.setPreferredScrollableViewportSize(new Dimension(500, 100));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//Add the table to a scrollpane and add it
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * updates the list of requirements in the table
	 *
	 */
	public void updateRequirementList() {
		DB.getAllRequirements(new UpdateTableCallback());
	}
	
	/**
	 * the the table model
	 *
	
	 * @return the table model */
	public ViewFilteredReqTable getTable(){
		return tableModel;
	}
	
	/**
	 *
	 * Callback to populate the table with all the requirements
	 * @author Josh
	 *
	 * @version $Revision: 1.0 $
	 */
	class UpdateTableCallback implements RequirementsCallback {
		/**
		 * Callback function to populate the table with all the requirements
		 *
		 * @param reqs a list of all requirements
		 * @see edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.RequirementsCallback#callback(List<RequirementModel>)
		 */
		@Override
		public void callback(List<RequirementModel> reqs) {
			if (reqs.size() > 0) {
				// put the data in the table
				int reqnumber = 0;
				for(RequirementModel req : reqs) {
					if (parent.model == null) {
						if (req.getIteration() == null) {
							reqnumber++;
						}
					}
					else if (req.getIteration() != null) {
						if (req.getIteration().getId() == parent.model.getId()) {
							reqnumber++;
						}
					}
				}
				Object[][] entries = new Object[reqnumber][ROWS];
				int i = 0;
				boolean sameIter;
				for(RequirementModel req : reqs) {
					sameIter = false;
					if (req.getIteration() == null) {
						if (parent.model == null) {
							sameIter = true;
						}
					}
					else if (parent.model != null && req.getIteration().getId() == parent.model.getId()) {
						sameIter = true;
					}
					if (sameIter) {
						entries[i][ID] = String.valueOf(req.getId());
						entries[i][NAME] = req.getName();
						entries[i][DESCRIPTION] = req.getDescription();
						if (req.getStatus() != null) {
							entries[i][STATUS] = req.getStatus().toString();
						}
						else {
							entries[i][STATUS] = "Error: Status set to null";
						}
						if (req.getPriority() != null) {
							entries[i][PRIORITY] = req.getPriority().toString();
						}
						else {
							entries[i][PRIORITY] = "";
						}
						entries[i][ESTIMATE] = req.getEstimate()+"";
						i++;
					}
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
					column.setPreferredWidth(30); //third column is bigger
				}
				else if (i == NAME) {
					column.setPreferredWidth(500);
				}
				else if (i == DESCRIPTION) {
					column.setPreferredWidth(700);
				}
				else {
					column.setPreferredWidth(200);
				}
			}
		}
	}
}

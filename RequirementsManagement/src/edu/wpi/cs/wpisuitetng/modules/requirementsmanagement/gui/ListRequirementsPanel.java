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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewReqTable.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

/**
 *
 * Panel to list all of the requirements, and have an option to edit them in the list
 * @author Josh
 * @author Tim C
 * @author James
 *
 */
@SuppressWarnings("serial")
public class ListRequirementsPanel extends JPanel {
	
	/** the tab that created this*/
	ListRequirementsTab parent;
	/** is inpute enabled*/
	boolean inputEnabled;
	/** the table that displays the requirements*/
	JTable table;
	/** the model for the table*/
	ViewReqTable tableModel;
	/** button to bring the table into edit mode*/
	JButton editButton;
	/** button to save edits*/
	JButton saveButton;
	/** button to cancel edits */
	JButton cancelButton;
	/** panel to display the edit/save and cancel buttons */
	JPanel editPanel;
	/** Old data used to compare changes */
	Object[][] data;
	
	/**
	 * Constructor
	 * @param parent the tab that made this
	 */
	public ListRequirementsPanel(final ListRequirementsTab parent) {
		this.parent = parent;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		updateAllRequirementList();
		
		table.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2 && tableModel.getMode() != Mode.EDIT) {
                	DB.getSingleRequirement((String) table.getModel().getValueAt(table.getSelectedRow(), 0),
                			new SingleRequirementCallback() {
						@Override
						public void callback(RequirementModel req) {
							parent.tabController.addEditRequirementTab(req);
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
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		//borderlayout so the table can expand while the filter area remains constant
		setLayout(new BorderLayout());
		
		//create the table part of the GUI
		tableModel = new ViewReqTable();
		tableModel.setMode(Mode.VIEW);
		table = new JTable(tableModel) {
			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				// Just return the custom renderer
				// This somehow works differently than setDefaultRenderer, but I'm not sure how
				//   other than the fact that this works and default renderer certainly does not
				return new CustomCellRenderer();
			}
		};
		table.setPreferredScrollableViewportSize(new Dimension(500, 100));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// create panel and button to change table to edit mode
		editPanel = new JPanel();
		editButton = new JButton("Edit");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEditTable();
			}
		});
		
		// create the save and cancel buttons
		saveButton = new JButton("Save");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setViewTable(false);
			}
			
		});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setViewTable(true);
				
			}
			
		});
		
		editPanel.add(editButton);
		
		//Add the table to a scrollpane and add it
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(200, 100));
		add(scrollPane, BorderLayout.CENTER);
		add(editPanel, BorderLayout.PAGE_END);
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
	 * updates the list of requirements in the table
	 *
	 */
	public void updateAllRequirementList() {
		DB.getAllRequirements(new UpdateTableCallback());
	}
	
	/**
	 * the the table model
	 *
	 * @return the table model
	 */
	public ViewReqTable getTable(){
		return this.tableModel;
	}
	
	/**
	 * Copies data over to a new structure
	 * clone() didn't seem to work correctly
	 *
	 * @param data data to copy
	 * @return
	 */
	private Object[][] copyData(Object[][] data) {
		Object[][] copy = new Object[tableModel.getRowCount()][tableModel.getColumnCount()];
		
		for(int i = 0; i < tableModel.getRowCount(); ++i) {
			for(int j = 0; j < tableModel.getColumnCount(); ++j) {
				copy[i][j] = data[i][j];
			}
		}
		
		return copy;
	}
	
	/**
	 * Function to turn the table into edit mode
	 *
	 */
	public void setEditTable() {
		tableModel.setMode(Mode.EDIT);
		editPanel.remove(editButton);
		editPanel.add(saveButton);
		editPanel.add(cancelButton);
		editPanel.revalidate();
		editPanel.repaint();
		
		// save copy of current data (clone() didn't seem to work)
		data = copyData(tableModel.getData());
		
		setUpColumns(); // TODO disable status except for delete and complete, just display the correct one
		                // TODO set up cell editors to limit what can be typed
	}
	
	/**
	 * turn the table into view mode
	 *
	 * @param cancelled if the changes made in edit mode should be cancelled
	 */
	public void setViewTable(boolean cancelled) {
		if(table.isEditing()) {
			table.getCellEditor().stopCellEditing();
		}
		boolean noErrors = true;
		if(cancelled) {
			updateAllRequirementList();
		} else {
			// validate fields
			noErrors = validateModels();
			if(noErrors) {  // no errors, update models and save
				// get all models from data base and continue from callback
				RetrieveAllRequirementsCallback cb = new RetrieveAllRequirementsCallback();
				DB.getAllRequirements(cb);
			}	
		}
		if(noErrors) { // no errors (or cancelled), so go back to view
			tableModel.setMode(Mode.VIEW);
			editPanel.remove(saveButton);
			editPanel.remove(cancelButton);
			editPanel.add(editButton);
			editPanel.revalidate();
			editPanel.repaint();
			
			// dump saved data
			data = null;
		}
	}
	
	/**
	 * Helper function to call other functions to set up individual columns
	 *
	 */
	public void setUpColumns() {
		setUpPriorityColumn(table, table.getColumnModel().getColumn(5));
		setUpStatusColumn(table, table.getColumnModel().getColumn(4));
		setUpIterationColumn(table, table.getColumnModel().getColumn(3));
	}
	
	/**
	 * make the iteration comboboxes in the iteration column
	 *
	 * @param table the JTable that has the column
	 * @param iterColumn the iteration column
	 */
	private void setUpIterationColumn(JTable table, TableColumn iterColumn) {
		JComboBox iterationBox = new JComboBox();
		FillIterationDropdown iterationDropdown = new FillIterationDropdown(iterationBox);
		DB.getAllIterations(iterationDropdown);
		iterColumn.setCellEditor(new DefaultCellEditor(iterationBox));
	}
	
	/**
	 * make the status comboboxes in the status column
	 *
	 * @param table the JTable that has the column
	 * @param statusColumn the status column
	 */
	private void setUpStatusColumn(JTable table, TableColumn statusColumn) {

		JComboBox statusBox = new JComboBox();
		statusBox.addItem("NEW");
		statusBox.addItem("OPEN");
		statusBox.addItem("IN_PROGRESS");
		statusBox.addItem("COMPLETE");
		statusBox.addItem("DELETED");
		
		statusColumn.setCellEditor(new DefaultCellEditor(statusBox));
	}
	
	/**
	 * make the priority comboxes in the priority column
	 *
	 * @param table the JTable that hs the column
	 * @param priorityColumn the priority column
	 */
	private void setUpPriorityColumn(JTable table, TableColumn priorityColumn) {
		JComboBox priorityBox = new JComboBox();
		priorityBox.addItem("NONE");
		priorityBox.addItem("LOW");
		priorityBox.addItem("MEDIUM");
		priorityBox.addItem("HIGH");
		
		priorityColumn.setCellEditor(new DefaultCellEditor(priorityBox));
	}
	
	/**
	 * update the requirements models so that they can be sent to the database to update them there
	 *
	 * @param reqs the list of requirements
	 * @param iterations the list of all iterations
	 * @return the updated list of requirements
	 */
	public List<RequirementModel> updateModels(List<RequirementModel> reqs, List<Iteration> iterations) {
		// TODO prune out unchanged requirements and just return those that need to be updated
		if(reqs != null && reqs.size() >= 1) {
			for(int i = 0; i < tableModel.getRowCount(); i++) {
				for(RequirementModel req : reqs) {
					if(req.getId() == Integer.valueOf((String)tableModel.getValueAt(i, 0))) {
						// we have the correct requirement, update values
						req.setName((String) tableModel.getValueAt(i, 1));
						req.setDescription((String) tableModel.getValueAt(i, 2));

						// Find the right iteration from the list
						req.setIteration(null);  // assume iteration is null, then find the correct one
						for(Iteration iteration : iterations) {
							if(iteration.getIterationNumber().equals((String)tableModel.getValueAt(i, 3))) {
								req.setIteration(iteration);
								break;
							}
						}
						// update status according to iteration if necessary, otherwise use the user selected data
						if(req.getIteration() != null && (req.getStatus() != RequirementStatus.COMPLETE || req.getStatus() != RequirementStatus.DELETED)) {
							req.setStatus(RequirementStatus.IN_PROGRESS);
						} else if(req.getIteration() == null && req.getStatus() == RequirementStatus.IN_PROGRESS) {
							req.setStatus(RequirementStatus.OPEN);
						} else {
							req.setStatus(RequirementStatus.valueOf((String)tableModel.getValueAt(i, 4)));
						}
						req.setPriority(RequirementPriority.valueOf((String)tableModel.getValueAt(i, 5)));
						req.setEstimate(Integer.valueOf((String)tableModel.getValueAt(i, 6)));
						break;
					}
				}
			}
		}
		
		return reqs;
	}
	
	/**
	 * checks to see if all the changes made in the table are valid
	 *
	 * @return true if the changes made were all good, false otherwise
	 */
	public boolean validateModels() {
		boolean noErrors = true;
		// TODO add more robust validating
		// no real need to check iterations, status, or priority because the
		// user can't select invalid things from the JComboBox
		for(int i = 0; i < tableModel.getRowCount(); i++) {
			// check name
			if(((String)tableModel.getValueAt(i, 1)).length() < 1) {
				System.out.println("Error in name for Requirement in row " + i);
				noErrors = false;
			}
			if(((String)tableModel.getValueAt(i, 2)).length() < 1){
				System.out.println("Error in description for Requirement in row " + i);
				noErrors = false;
			}
			// check estimate
			try {
				if((Integer.valueOf((String)tableModel.getValueAt(i, 6))) < 0) {
					System.out.println("Error in estimate for Requirement in row " + i);
					noErrors = false;
				}
			} catch (NumberFormatException e) {
				// still an error
				System.out.println("Error in estimate for Requirement in row " + i);
				noErrors = false;
			}
		}

		return noErrors;
	}
	
	/**
	 * updates all the requirements in the database to match those in reqs
	 *
	 * @param reqs a list of all the requirements
	 */
	public void sendRequirementsToDatabase(List<RequirementModel> reqs) {
		// TODO (with updateModels) Only update those that need to be updated
		for(RequirementModel req : reqs.subList(0, reqs.size() - 1)) {  // update all but the last requirement
			DB.updateRequirements(req, new UpdateRequirementCallback(false));
		}
		// now update the last requirement with the provision that the callback updates the table
		DB.updateRequirements(reqs.get(reqs.size() - 1), new UpdateRequirementCallback(true));
	}
	
	/**
	 * Callback for updating a requirement in the database
	 * 
	 * @author Tim Calvert
	 * @author James Megin
	 *
	 */
	class UpdateRequirementCallback implements SingleRequirementCallback {
		
		boolean lastReq = false;
		
		public UpdateRequirementCallback(boolean lastReq) {
			this.lastReq = lastReq;
		}

		/**
		 * Required callback, does nothing
		 *
		 * @param req Requirement returned from database
		 */
		@Override
		public void callback(RequirementModel req) {
			if(lastReq) {  // this should be the callback from the last requirement (or close enough)
				           // so go ahead and update the list again
				updateAllRequirementList();
			}
		}
		
	}
	
	/**
	 *
	 * gets a list of all the requirements and creates a RetrieveAllIterationsCallback to update them and send them to the database
	 * @author Tim Calvert
	 * @author James Megin
	 *
	 */
	class RetrieveAllRequirementsCallback implements RequirementsCallback {
		
		/**
		 * Create RetrieveallIteratoinsCallback to update the requirments and send the to the database
		 *
		 * @param reqs a list of all requirements
		 */
		@Override
		public void callback(List<RequirementModel> reqs) {
			DB.getAllIterations(new RetrieveAllIterationsCallback(reqs));
		}
		
	}
	
	/**
	 *
	 * Retrieves all iterations from the database
	 * @author Tim Calvert
	 * @author James Megin
	 *
	 */
	class RetrieveAllIterationsCallback implements IterationCallback {
		
		/** a list of all the requirements */
		List<RequirementModel> reqs;
		
		/**
		 * Constructor for the class
		 * @param reqs List of requirements just retrieved from the db
		 */
		public RetrieveAllIterationsCallback(List<RequirementModel> reqs) {
			this.reqs = reqs;
		}

		/**
		 * Updates the requirements to what the user edited and then
		 * sends them to the database
		 *
		 * @param iterationss Iterations retrieved from the database
		 */
		@Override
		public void callback(List<Iteration> iterationss) {
			List<RequirementModel> requirements = updateModels(reqs, iterationss);
			sendRequirementsToDatabase(requirements);
			
		}
		
	}
	
	/**
	 *
	 * Callback to populate the table with all the requirements
	 * @author Josh
	 *
	 */
	class UpdateTableCallback implements RequirementsCallback {
		/**
		 * Callback function to populate the table with all the requirements
		 *
		 * @param reqs a list of all requirements
		 */
		@Override
		public void callback(List<RequirementModel> reqs) {
			if (reqs.size() > 0) {
				// put the data in the table
				Object[][] entries = new Object[reqs.size()][7];
				int i = 0;
				for(RequirementModel req : reqs) {
					entries[i][0] = String.valueOf(req.getId());
					entries[i][1] = req.getName();
					entries[i][2] = req.getDescription();
					if (req.getIteration() != null) {
						entries[i][3] = req.getIteration().getIterationNumber().toString();	
					}
					else {
						entries[i][3] = "Backlog";
					}
					if (req.getStatus() != null) {
						entries[i][4] = req.getStatus().toString();
					}
					else {
						entries[i][4] = "Error: Status set to null";
					}
					if (req.getPriority() != null) {
						entries[i][5] = req.getPriority().toString();
					}
					else {
						entries[i][5] = "";
					}
					entries[i][6] = req.getEstimate()+"";
					i++;
				}
				getTable().setData(entries);
				getTable().fireTableStructureChanged();
			}
			else {
				// do nothing, there are no requirements
			}
		
			TableColumn column = null;
			for (int i = 0; i < 7; i++) {
				column = table.getColumnModel().getColumn(i);
				if (i == 0) {
					column.setPreferredWidth(30); //third column is bigger
				}
				else if (i == 1) {
					column.setPreferredWidth(500);
				}
				else if (i == 2) {
					column.setPreferredWidth(700);
				}
				else {
					column.setPreferredWidth(200);
				}
			}
		}
		
	}
	
	/**
	 *
	 * Class to take the list of iterations from the database and fill in a combobox with their names
	 * @author Tim C
	 * @author James
	 *
	 */
	class FillIterationDropdown implements IterationCallback {
		
		/**
		 * The combo box that will get filled in
		 */
		JComboBox iterationBox;
		
		/**
		 * Constructor
		 * @param iterationBox the combobox that will get filled in
		 */
		public FillIterationDropdown(JComboBox iterationBox) {
			this.iterationBox = iterationBox;
			iterationBox.addItem("Backlog");
		}

		/**
		 * Go through the list of iterations and add their name to the combobox
		 * it does not add iterations that are already over as it would be invalid to set a project to those iterations
		 *
		 * @param iterationss the list of iterations
		 */
		@Override
		public void callback(List<Iteration> iterationss) {
			if(iterationss.size() > 0) {
				final Date now = new Date();
				for(Iteration iteration : iterationss) {
					// Make sure the iteration the only iterations that are added are still in progress
					if(now.before(iteration.getEndDate()) || now == iteration.getEndDate()) {
						iterationBox.addItem("" + iteration.getIterationNumber());
					}
				}
			}
		}
		
	}
	
	/**
	 *
	 * A custom cell renderer to allow for changing the background color
	 * when a cell has an error in it
	 * @author Tim Calvert
	 * @author James Megin
	 *
	 */
	class CustomCellRenderer extends DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(tableModel.getMode() == Mode.EDIT) {
				if(!value.equals(data[row][column])) {
					c.setBackground(Color.YELLOW);
					setToolTipText("This cell has been changed from: " + (data[row][column]).toString());
				} else {
					c.setBackground(Color.WHITE);
					setToolTipText(null);
				}
				if(column == 1) {
					if(((String)value).length() < 1 || ((String)value).length() > 100) {
						c.setBackground(Color.RED);
						setToolTipText("A requirement must have a name between 1 and 100 charecters.");
					}
				} else if(column == 2) {
					if(((String)value).length() < 1) {
						c.setBackground(Color.RED);
						setToolTipText("A requirement must have a description.");
					}
				} else if(column == 6) {
					try {
						if(((Integer.valueOf((String)value) < 0))) {
							c.setBackground(Color.RED);
							setToolTipText("A requirement estimate must be a positive number.");
						}
					} catch (NumberFormatException e) {
						// still an error
						c.setBackground(Color.RED);
						setToolTipText("A requirement estimate must be a positive number.");
					}
				}
			}
			
			return c;
		}
		
	}


}

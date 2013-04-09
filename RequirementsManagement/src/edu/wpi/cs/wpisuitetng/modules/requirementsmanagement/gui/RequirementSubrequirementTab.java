/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Josh
 *    Deniz
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel.CustomCellRenderer;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewReqTable.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEventObjectType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;


/**
 * Main panel for viewing/editing subrequirements
 * @author Josh
 * @author Deniz
 * @author Jacob Palnick
 */
@SuppressWarnings("serial")
public class RequirementSubrequirementTab extends JPanel {

	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int DESCRIPTION = 2;
	public static final int ITERATION = 3;
	public static final int STATUS = 4;
	public static final int PRIORITY = 5;
	public static final int ESTIMATE = 6;
	public static final int RELEASE = 7;
	public static final int ROWS = 7;

	/** the panel this is shown in */
	RequirementsPanel parent;
	/** tableModle to display current subrequirements */
	ViewReqTable subrequirementsTableModel;
	/** tableModel to display other possible subrequirements */
	ViewReqTable possibleSubrequirementsTableModel;
	/** tableModle to display current subrequirements */
	JTable subrequirementsTable;
	/** tableModel to display other possible subrequirements */
	JTable possibleSubrequirementsTable;
	/** subrequirement list scroll pane */
	JScrollPane subrequirementTableScrollPane;
	/** possible subrequirement list scroll pane */
	JScrollPane possibleSubrequirementTableScrollPane;
	/** list of subrequirements*/
	List<RequirementModel> subrequirements;
	JButton addChildButton;
	JButton setParrentButton;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param parent NoteTab that contains this object
	 */
	public RequirementSubrequirementTab(RequirementsPanel parent) {
		this.parent = parent;
		subrequirements = parent.getModel().getSubRequirements();

		// Add all components to this panel
		addComponents();
		
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		subrequirementsTableModel = new ViewReqTable();
		subrequirementsTableModel.setMode(Mode.VIEW);
		subrequirementsTable = new JTable(subrequirementsTableModel);// {
//			@Override
//			public TableCellRenderer getCellRenderer(int row, int column) {
//				// Just return the custom renderer
//				// This somehow works differently than setDefaultRenderer, but I'm not sure how
//				//   other than the fact that this works and default renderer certainly does not
//				return new CustomCellRenderer();
//			}
//		};
		subrequirementsTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		subrequirementsTable.setFillsViewportHeight(true);
		subrequirementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		subrequirementsTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				//TODO finish this listener
//                if (e.getClickCount() == 1) {
//                	updateSelectedOther((String) subrequirementsTable.getModel().getValueAt(subrequirementsTable.getSelectedRow(), ID));
//                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
		subrequirementTableScrollPane = new JScrollPane(subrequirementsTable);
		subrequirementTableScrollPane.setPreferredSize(new Dimension(300, 300));
		subrequirementTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		subrequirementTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		possibleSubrequirementsTableModel = new ViewReqTable();
		possibleSubrequirementsTableModel.setMode(Mode.VIEW);
		possibleSubrequirementsTable = new JTable(possibleSubrequirementsTableModel);// {
//			@Override
//			public TableCellRenderer getCellRenderer(int row, int column) {
//				// Just return the custom renderer
//				// This somehow works differently than setDefaultRenderer, but I'm not sure how
//				//   other than the fact that this works and default renderer certainly does not
//				return new CustomCellRenderer();
//			}
//		};
		possibleSubrequirementsTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		possibleSubrequirementsTable.setFillsViewportHeight(true);
		possibleSubrequirementsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		possibleSubrequirementsTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1) {
                	updateSelectedPossible((String) possibleSubrequirementsTable.getModel().getValueAt(possibleSubrequirementsTable.getSelectedRow(), ID));
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
		possibleSubrequirementTableScrollPane = new JScrollPane(possibleSubrequirementsTable);
		possibleSubrequirementTableScrollPane.setPreferredSize(new Dimension(300, 300));
		possibleSubrequirementTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		possibleSubrequirementTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		addChildButton = new JButton("Add selected as child");
		addChildButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = possibleSubrequirementsTable.getSelectedRow();
				if (selectedRow >= 0 && selectedRow < possibleSubrequirementsTable.getRowCount()) {
					parent.addChild(new Integer((String) possibleSubrequirementsTable.getModel().getValueAt(selectedRow, ID)));
				}
			}
			
		});

		setParrentButton = new JButton("Add as child to selected");
		setParrentButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = possibleSubrequirementsTable.getSelectedRow();
				if (selectedRow >= 0 && selectedRow < possibleSubrequirementsTable.getRowCount()) {
					parent.addToParent(new Integer((String) possibleSubrequirementsTable.getModel().getValueAt(selectedRow, ID)));
				}
			}
			
		});
		
		addChildButton.setMinimumSize(addChildButton.getPreferredSize());
		addChildButton.setMaximumSize(addChildButton.getPreferredSize());
		
		
		//TODO add the subcomponents to the panel and manage the layout
		
		layout.putConstraint(SpringLayout.WEST, subrequirementTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, this, 4, SpringLayout.EAST, subrequirementTableScrollPane);
		layout.putConstraint(SpringLayout.NORTH, subrequirementTableScrollPane, 4, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, possibleSubrequirementTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, this, 4, SpringLayout.EAST, possibleSubrequirementTableScrollPane);
		layout.putConstraint(SpringLayout.NORTH, possibleSubrequirementTableScrollPane, 5, SpringLayout.SOUTH, subrequirementTableScrollPane);
		layout.putConstraint(SpringLayout.SOUTH, this, 3 + 4 + addChildButton.getPreferredSize().height, SpringLayout.SOUTH, possibleSubrequirementTableScrollPane);
		
		layout.putConstraint(SpringLayout.WEST, addChildButton, 2, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, addChildButton, 5, SpringLayout.SOUTH, possibleSubrequirementTableScrollPane);
		
		layout.putConstraint(SpringLayout.WEST, setParrentButton, 2, SpringLayout.EAST, addChildButton);
		layout.putConstraint(SpringLayout.NORTH, setParrentButton, 0, SpringLayout.NORTH, addChildButton);
		
		add(subrequirementTableScrollPane);
		add(possibleSubrequirementTableScrollPane);
		add(addChildButton);
		add(setParrentButton);
		System.out.println(addChildButton.getPreferredSize().toString());
		System.out.println(addChildButton.getSize().toString());
		
		
//		noteViewer = new JPanel(new GridBagLayout());
//
//		noteScrollPane = new JScrollPane(noteViewer);
//		noteScrollPane.setPreferredSize(new Dimension(300, 300));
//		noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// Add elements to the main panel
//		add(noteScrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * called when the selection in the possible list changes.
	 * @param selectedId the id of the selected requirement
	 */
	private void updateSelectedPossible(String selectedId) {
		if (selectedId == null || selectedId.equals("")) {
			addChildButton.setEnabled(false);
			setParrentButton.setEnabled(false);
		} else {
			addChildButton.setEnabled(true);
			setParrentButton.setEnabled(true);
			for (RequirementModel req : subrequirements) {
				if((""+req.getId()).equals(selectedId)) {
					addChildButton.setEnabled(false);
					setParrentButton.setEnabled(false);
				}
			}
		}
	}

	public void update() {
		update(parent.model);
	}
	
	private boolean gotUpdatedList;
	private List<RequirementModel> allRequirements;
	public void update(RequirementModel model) {
		subrequirements = model.getSubRequirements();
		String selectedSubId = (String) subrequirementsTable.getModel().getValueAt(subrequirementsTable.getSelectedRow(), ID);
		String selectedPossibleId = (String) possibleSubrequirementsTable.getModel().getValueAt(possibleSubrequirementsTable.getSelectedRow(), ID);
		
		allRequirements = new ArrayList<RequirementModel>();
		gotUpdatedList = false;
		//TODO figure out how to do sync network request.
		addChildButton.setEnabled(false);
		setParrentButton.setEnabled(false);
		subrequirementTableScrollPane.setEnabled(false);
		possibleSubrequirementTableScrollPane.setEnabled(false);
		subrequirementsTable.setEnabled(false);
		possibleSubrequirementsTable.setEnabled(false);
		while(!gotUpdatedList) {}
		
		addChildButton.setEnabled(true);
		setParrentButton.setEnabled(true);
		subrequirementTableScrollPane.setEnabled(true);
		possibleSubrequirementTableScrollPane.setEnabled(true);
		subrequirementsTable.setEnabled(true);
		possibleSubrequirementsTable.setEnabled(true);
		
		//reselect the previous selections if possible
		subrequirementsTable.removeRowSelectionInterval(0, subrequirementsTable.getRowCount()-1);
		possibleSubrequirementsTable.removeRowSelectionInterval(0, possibleSubrequirementsTable.getRowCount()-1);
		for (int i = 0; i<subrequirementsTable.getRowCount(); i++) {
			if(((String) subrequirementsTable.getModel().getValueAt(i, ID)).equals(selectedSubId)) {
				subrequirementsTable.setRowSelectionInterval(i, i);
			}
		}
		for (int i = 0; i<possibleSubrequirementsTable.getRowCount(); i++) {
			if(((String) possibleSubrequirementsTable.getModel().getValueAt(i, ID)).equals(selectedPossibleId)) {
				possibleSubrequirementsTable.setRowSelectionInterval(i, i);
			}
		}
		updateSelectedPossible((String) possibleSubrequirementsTable.getModel().getValueAt(possibleSubrequirementsTable.getSelectedRow(), ID));
	}
	
//	/**
//	 *add the events to the history tab
//	 *
//	 * @param events the list of events
//	 */
//	public void setNotes(List<ProjectEvent> events) {
//		GridBagConstraints c = new GridBagConstraints();
//		c.fill = GridBagConstraints.HORIZONTAL;
//		c.insets = new Insets(3, 0, 3, 0);
//		c.gridx = 0;
//		c.gridy = events.size();
//		c.weightx = 0.5;
//		c.weighty = 0.5;
//		
//		noteViewer.removeAll();
//		
//		for (ProjectEvent event : events) {
//			if (event != null && event.getObjectType() == ProjectEventObjectType.REQUIREMENT) {
//				int id = Integer.parseInt(event.getObjectId());
//				if (id == this.parent.model.getId()) {
//					noteViewer.add(new RequirementHistoryPanel(event), c);
//					c.gridy -= 1;
//				}
//			}
//			
//		}
//		
//		this.revalidate();
//		this.repaint();
//	}
	
	/**
	 *
	 * Callback to populate the table with all the requirements
	 * @author Josh
	 *
	 */
	class UpdateTablesCallback implements RequirementsCallback {
		/**
		 * Callback function to populate the tables with all the requirements
		 *
		 * @param reqs a list of all requirements
		 */
		@Override
		public void callback(List<RequirementModel> reqs) {
			if (reqs.size() > 0) {
				// put the data in the table
				Object[][] subEntries = new Object[subrequirements.size()][ROWS];
				Object[][] posEntries = new Object[reqs.size()-subrequirements.size()][ROWS];
				ArrayList<String> subIdStringList = new ArrayList<String>();
				for(RequirementModel req : subrequirements) {
					subIdStringList.add(String.valueOf(req.getId()));
				}
				int sub = 0;
				int pos = 0;
				for(RequirementModel req : reqs) {
					String id = String.valueOf(req.getId());
					String name = req.getName();
					String description = req.getDescription();
					String iteraton = (req.getIteration() == null ? "Backlog" : req.getIteration().getIterationNumber().toString());
					String status = (req.getStatus() == null ? "Error: Status set to null" : req.getStatus().toString());
					String priority = (req.getPriority() == null ? "" : req.getPriority().toString());
					String estimate = req.getEstimate()+"";
					
					if (subIdStringList.contains(id)) {
						subEntries[sub][ID] = id;
						subEntries[sub][NAME] = name;
						subEntries[sub][DESCRIPTION] = description;
						subEntries[sub][ITERATION] = iteraton;	
						subEntries[sub][STATUS] = status;
						subEntries[sub][PRIORITY] = priority;
						subEntries[sub][ESTIMATE] = estimate;
						sub++;
					} else {
						posEntries[pos][ID] = id;
						posEntries[pos][NAME] = name;
						posEntries[pos][DESCRIPTION] = description;
						posEntries[pos][ITERATION] = iteraton;	
						posEntries[pos][STATUS] = status;
						posEntries[pos][PRIORITY] = priority;
						posEntries[pos][ESTIMATE] = estimate;
						pos++;
					}
				}
				subrequirementsTableModel.setData(subEntries);
				subrequirementsTableModel.fireTableStructureChanged();
				possibleSubrequirementsTableModel.setData(posEntries);
				possibleSubrequirementsTableModel.fireTableStructureChanged();
			}
			else {
				// do nothing, there are no requirements
			}
		
			TableColumn column = null;
			TableColumn column2 = null;
			for (int i = 0; i < ROWS; i++) {
				column = subrequirementsTable.getColumnModel().getColumn(i);
				column2 = possibleSubrequirementsTable.getColumnModel().getColumn(i);
				if (i == ID) {
					column.setPreferredWidth(30); //third column is bigger
					column2.setPreferredWidth(30); //third column is bigger
				}
				else if (i == NAME) {
					column.setPreferredWidth(500);
					column2.setPreferredWidth(500);
				}
				else if (i == DESCRIPTION) {
					column.setPreferredWidth(700);
					column2.setPreferredWidth(700);
				}
				else {
					column.setPreferredWidth(200);
					column2.setPreferredWidth(200);
				}
			}
			gotUpdatedList = true;
		}
		
	}


//	/**
//	 *
//	 * A custom cell renderer to allow for changing the background color
//	 * when a cell has an error in it
//	 * @author Tim Calvert
//	 * @author James Megin
//	 * @author Jacob Palnick
//	 *
//	 */
//	class CustomCellRenderer extends DefaultTableCellRenderer {
//
//		@Override
//		public Component getTableCellRendererComponent(JTable table,
//				Object value, boolean isSelected, boolean hasFocus, int row,
//				int column) {
//			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//			
//			if(tableModel.getMode() == Mode.EDIT) {
//				if(!value.equals(data[row][column])) {
//					c.setBackground(Color.YELLOW);
//					setToolTipText("This cell has been changed from: " + (data[row][column]).toString());
//				} else {
//					c.setBackground(Color.WHITE);
//					setToolTipText(null);
//				}
//				if(column == NAME) {
//					if(((String)value).length() < 1 || ((String)value).length() > 100) {
//						c.setBackground(Color.RED);
//						setToolTipText("A requirement must have a name between 1 and 100 charecters.");
//					}
//				} else if(column == DESCRIPTION) {
//					if(((String)value).length() < 1) {
//						c.setBackground(Color.RED);
//						setToolTipText("A requirement must have a description.");
//					}
//				} else if(column == ESTIMATE) {
//					try {
//						if(((Integer.valueOf((String)value) < 0))) {
//							c.setBackground(Color.RED);
//							setToolTipText("A requirement estimate must be a positive number.");
//						}
//					} catch (NumberFormatException e) {
//						// still an error
//						c.setBackground(Color.RED);
//						setToolTipText("A requirement estimate must be a positive number.");
//					}
//				}
//			}
//			
//			return c;
//		}
//		
//	}
}

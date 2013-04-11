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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.table.TableColumn;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewReqTable.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;


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
	List<String> subrequirements;
	JButton addChildButton;
	JButton setParrentButton;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param parent NoteTab that contains this object
	 */
	public RequirementSubrequirementTab(RequirementsPanel parent) {
		this.parent = parent;
		subrequirements = parent.model.getSubRequirements();

		// Add all components to this panel
		addComponents();
		
		update();
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
                	updateSelectedPossible(getSelectedPosId());
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
		
		int maxPreferedWidth = (addChildButton.getPreferredSize().width > setParrentButton.getPreferredSize().width ? addChildButton.getPreferredSize().width : setParrentButton.getPreferredSize().width);
		
		Dimension pref = new Dimension(maxPreferedWidth, addChildButton.getPreferredSize().height);
		addChildButton.setPreferredSize(pref);
		addChildButton.setMinimumSize(pref);
		setParrentButton.setPreferredSize(pref);
		setParrentButton.setMinimumSize(pref);
		
		//layout the components
		layout.putConstraint(SpringLayout.WEST, subrequirementTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, subrequirementTableScrollPane, -4, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, subrequirementTableScrollPane, 4, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, possibleSubrequirementTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, this, 4, SpringLayout.EAST, possibleSubrequirementTableScrollPane);
		layout.putConstraint(SpringLayout.NORTH, possibleSubrequirementTableScrollPane, 5, SpringLayout.SOUTH, subrequirementTableScrollPane);
		layout.putConstraint(SpringLayout.SOUTH, this, 3 + 4 + addChildButton.getPreferredSize().height, SpringLayout.SOUTH, possibleSubrequirementTableScrollPane);
		
		layout.putConstraint(SpringLayout.EAST, addChildButton, -2, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, addChildButton, 5, SpringLayout.SOUTH, possibleSubrequirementTableScrollPane);
		
		layout.putConstraint(SpringLayout.WEST, setParrentButton, 2, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, setParrentButton, 0, SpringLayout.NORTH, addChildButton);
		
		// Add elements to the main panel
		add(subrequirementTableScrollPane);
		add(possibleSubrequirementTableScrollPane);
		add(addChildButton);
		add(setParrentButton);
	}
	
	/**
	 * called when the selection in the possible list changes.
	 * @param selectedId the id of the selected requirement
	 */
	private void updateSelectedPossible(String selectedId) {
		if (selectedId == null || selectedId.equals("") || parent.model.getStatus() == RequirementStatus.COMPLETE || parent.model.getStatus() == RequirementStatus.DELETED) {
			addChildButton.setEnabled(false);
			setParrentButton.setEnabled(false);
		} else {
			addChildButton.setEnabled(true);
			setParrentButton.setEnabled(true);
			for (String req : subrequirements) {
				if(req.equals(selectedId)) {
					addChildButton.setEnabled(false);
					setParrentButton.setEnabled(false);
				}
			}
		}
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		subrequirementTableScrollPane.setEnabled(enabled);
		possibleSubrequirementTableScrollPane.setEnabled(enabled);
		subrequirementsTable.setEnabled(enabled);
		possibleSubrequirementsTable.setEnabled(enabled);
		if (enabled) {
			updateSelectedPossible(getSelectedPosId());
		} else {
			addChildButton.setEnabled(false);
			setParrentButton.setEnabled(false);
		}
	}

	public void update() {
		update(parent.model);
	}
	
//	private boolean gotUpdatedList;
	public void update(RequirementModel model) {
		subrequirements = model.getSubRequirements();
		String selectedSubId = getSelectedSubId();
		String selectedPossibleId = getSelectedPosId();
		
		//TODO figure out how to do sync network request.
		addChildButton.setEnabled(false);
		setParrentButton.setEnabled(false);
		subrequirementTableScrollPane.setEnabled(false);
		possibleSubrequirementTableScrollPane.setEnabled(false);
		subrequirementsTable.setEnabled(false);
		possibleSubrequirementsTable.setEnabled(false);
		DB.getAllRequirements(new UpdateTablesCallback(selectedSubId, selectedPossibleId));
	}
	
	private String getSelectedSubId() {
		int selectedRow = subrequirementsTable.getSelectedRow();
		if (selectedRow >= 0 && selectedRow < subrequirementsTable.getRowCount()) {
			return (String) subrequirementsTable.getModel().getValueAt(subrequirementsTable.getSelectedRow(), ID);
		} else {
			return "";
		}
	}
	
	private String getSelectedPosId() {
		int selectedRow = possibleSubrequirementsTable.getSelectedRow();
		if (selectedRow >= 0 && selectedRow < possibleSubrequirementsTable.getRowCount()) {
			return (String) possibleSubrequirementsTable.getModel().getValueAt(possibleSubrequirementsTable.getSelectedRow(), ID);
		} else {
			return "";
		}
	}
	
	/**
	 *
	 * Callback to populate the table with all the requirements
	 * @author Josh
	 * @author Jacob Palnick
	 */
	class UpdateTablesCallback implements RequirementsCallback {
		
		String selectedSub;
		String selectedPos;
		
		public UpdateTablesCallback(String selectedSub, String selectedPos) {
			super();
			this.selectedSub = selectedSub;
			this.selectedPos = selectedPos;
		}

		/**
		 * Callback function to populate the tables with all the requirements
		 *
		 * @param reqs a list of all requirements
		 */
		@Override
		public void callback(List<RequirementModel> reqs) {
			if (reqs.size() > 0) {
				// put the data in the table
				ArrayList<Object[]> subEntriesList = new ArrayList<Object[]>();
				ArrayList<Object[]> posEntriesList = new ArrayList<Object[]>();
				for(RequirementModel req : reqs) {
					String id = String.valueOf(req.getId());
					String name = req.getName();
					String description = req.getDescription();
					String iteraton = (req.getIteration() == null ? "Backlog" : req.getIteration().getIterationNumber().toString());
					String status = (req.getStatus() == null ? "Error: Status set to null" : req.getStatus().toString());
					String priority = (req.getPriority() == null ? "" : req.getPriority().toString());
					String estimate = req.getEstimate()+"";
					
					if (subrequirements.contains(id)) {
						Object[] subEntry = new Object[ROWS];
						subEntry[ID] = id;
						subEntry[NAME] = name;
						subEntry[DESCRIPTION] = description;
						subEntry[ITERATION] = iteraton;	
						subEntry[STATUS] = status;
						subEntry[PRIORITY] = priority;
						subEntry[ESTIMATE] = estimate;
						subEntriesList.add(subEntry);
					} else {
						//TODO make sure the requirement is not a parent of the current requirement
						if (req.getId() != parent.model.getId() && (req.getStatus() == RequirementStatus.NEW ||
								req.getStatus() == RequirementStatus.OPEN ||
								req.getStatus() == RequirementStatus.IN_PROGRESS)) {
							Object[] posEntry = new Object[ROWS];
							posEntry[ID] = id;
							posEntry[NAME] = name;
							posEntry[DESCRIPTION] = description;
							posEntry[ITERATION] = iteraton;	
							posEntry[STATUS] = status;
							posEntry[PRIORITY] = priority;
							posEntry[ESTIMATE] = estimate;
							posEntriesList.add(posEntry);
						}
					}
				}

				Object[][] subEntries = {};
				Object[][] posEntries = {};
				if(subEntriesList.size()>0) {
					subEntries = subEntriesList.toArray(new Object[1][1]);
				}
				if(posEntriesList.size()>0) {
					posEntries = posEntriesList.toArray(new Object[1][1]);
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
			subrequirementTableScrollPane.setEnabled(true);
			possibleSubrequirementTableScrollPane.setEnabled(true);
			subrequirementsTable.setEnabled(true);
			possibleSubrequirementsTable.setEnabled(true);
			
			//reselect the previous selections if possible
			for (int i = 0; i<subrequirementsTable.getRowCount(); i++) {
				String tmp = (String) subrequirementsTable.getModel().getValueAt(i, ID);
				if(tmp != null && tmp.equals(selectedSub)) {
					subrequirementsTable.setRowSelectionInterval(i, i);
				}
			}
			for (int i = 0; i<possibleSubrequirementsTable.getRowCount(); i++) {
				String tmp = (String) possibleSubrequirementsTable.getModel().getValueAt(i, ID);
				if(tmp != null && tmp.equals(selectedPos)) {
					possibleSubrequirementsTable.setRowSelectionInterval(i, i);
				}
			}
			updateSelectedPossible(getSelectedPosId());

		}
		
	}
}

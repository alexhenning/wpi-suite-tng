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
 *    William
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

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.UsersCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;


/**
 * Main panel for viewing/editing users
 * @author Josh
 * @author Deniz
 * @author Jacob Palnick
 * @author William Terry
 */
@SuppressWarnings("serial")
public class AssignUserToRequirementTab extends JPanel {

	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int USERNAME = 2;
	public static final int PERMISSIONLEVEL = 3;
	public static final int ROWS = 7;

	/** the panel this is shown in */
	RequirementsPanel parent;
	/** tableModel to display currently assigned users */
	ViewUserTable assignedUserTableModel;
	/** tableModel to display other users that can be assigned */
	ViewPermissionsTable possibleUserTableModel;
	/** tableModle to display currently assigned users */
	JTable assignedUserTable;
	/** tableModel to display other users that can be assigned */
	JTable possibleUserTable;
	/** user list scroll pane */
	JScrollPane assignedUserTableScrollPane;
	/** possible user list scroll pane */
	JScrollPane possibleUserTableScrollPane;
	/** list of user*/
	List<User> users;
	JButton addUserButton;
	JButton removeUserButton;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param parent NoteTab that contains this object
	 */
	public AssignUserToRequirementTab(RequirementsPanel parent) {
		this.parent = parent;
		users = parent.model.getAssignees();

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

		assignedUserTableModel = new ViewUserTable();
		assignedUserTable = new JTable(assignedUserTableModel);// {
		assignedUserTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		assignedUserTable.setFillsViewportHeight(true);
		assignedUserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		assignedUserTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
				//TODO finish this listener
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
		assignedUserTableScrollPane = new JScrollPane(assignedUserTable);
		assignedUserTableScrollPane.setPreferredSize(new Dimension(300, 300));
		assignedUserTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		assignedUserTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		possibleUserTableModel = new ViewPermissionsTable();
		possibleUserTable = new JTable(possibleUserTableModel);
		possibleUserTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
		possibleUserTable.setFillsViewportHeight(true);
		possibleUserTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		possibleUserTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1) {
//                	updateSelectedPossible(getSelectedPosId());
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
		possibleUserTableScrollPane = new JScrollPane(possibleUserTable);
		possibleUserTableScrollPane.setPreferredSize(new Dimension(300, 300));
		possibleUserTableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		possibleUserTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		addUserButton = new JButton("Assign selected user to requirement");
		addUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = possibleUserTable.getSelectedRow();
				if (selectedRow >= 0 && selectedRow < possibleUserTable.getRowCount()) {
					parent.addUser(new Integer((String) possibleUserTable.getModel().getValueAt(selectedRow, ID)));
				}
			}
			
		});

		removeUserButton = new JButton("Remove user from requirement");
		removeUserButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = possibleUserTable.getSelectedRow();
				if (selectedRow >= 0 && selectedRow < possibleUserTable.getRowCount()) {
					parent.remUser(new Integer((String) possibleUserTable.getModel().getValueAt(selectedRow, ID)));
				}
			}
			
		});
		
		int maxPreferedWidth = (addUserButton.getPreferredSize().width > removeUserButton.getPreferredSize().width ? addUserButton.getPreferredSize().width : removeUserButton.getPreferredSize().width);
		
		Dimension pref = new Dimension(maxPreferedWidth, addUserButton.getPreferredSize().height);
		addUserButton.setPreferredSize(pref);
		addUserButton.setMinimumSize(pref);
		removeUserButton.setPreferredSize(pref);
		removeUserButton.setMinimumSize(pref);
		
		//layout the components
		layout.putConstraint(SpringLayout.WEST, assignedUserTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, assignedUserTableScrollPane, -4, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, assignedUserTableScrollPane, 4, SpringLayout.NORTH, this);
		
		layout.putConstraint(SpringLayout.WEST, possibleUserTableScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, this, 4, SpringLayout.EAST, possibleUserTableScrollPane);
		layout.putConstraint(SpringLayout.NORTH, possibleUserTableScrollPane, 5, SpringLayout.SOUTH, assignedUserTableScrollPane);
		layout.putConstraint(SpringLayout.SOUTH, this, 3 + 4 + addUserButton.getPreferredSize().height, SpringLayout.SOUTH, possibleUserTableScrollPane);
		
		layout.putConstraint(SpringLayout.EAST, addUserButton, -2, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, addUserButton, 5, SpringLayout.SOUTH, possibleUserTableScrollPane);
		
		layout.putConstraint(SpringLayout.WEST, removeUserButton, 2, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, removeUserButton, 0, SpringLayout.NORTH, addUserButton);
		
		// Add elements to the main panel
		add(assignedUserTableScrollPane);
		add(possibleUserTableScrollPane);
		add(addUserButton);
		add(removeUserButton);
	}
	
	
	//kept in the event that we find the need to disqualify users from being added to a requirement
//	/**
//	 * called when the selection in the possible list changes.
//	 * @param selectedId the id of the selected requirement
//	 */
//	private void updateSelectedPossible(String selectedId) {
//		if (selectedId == null || selectedId.equals("") || parent.model.getStatus() == RequirementStatus.COMPLETE || parent.model.getStatus() == RequirementStatus.DELETED) {
//			addUserButton.setEnabled(false);
//			removeUserButton.setEnabled(false);
//		} else {
//			addUserButton.setEnabled(true);
//			removeUserButton.setEnabled(true);
//			for (User user : users) {
//				if(user.equals(selectedId)) {
//					addUserButton.setEnabled(false);
//					removeUserButton.setEnabled(false);
//				}
//			}
//		}
//	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		assignedUserTableScrollPane.setEnabled(enabled);
		possibleUserTableScrollPane.setEnabled(enabled);
		assignedUserTable.setEnabled(enabled);
		possibleUserTable.setEnabled(enabled);
		if (enabled) {
//			updateSelectedPossible(getSelectedPosId());
		} else {
			addUserButton.setEnabled(false);
			removeUserButton.setEnabled(false);
		}
	}

	public void update() {
		update(parent.model);
	}
	
//	private boolean gotUpdatedList;
	public void update(RequirementModel model) {
		users = model.getAssignees();
		String selectedSubId = getSelectedSubId();
		String selectedPossibleId = getSelectedPosId();
		
		//TODO figure out how to do sync network request.
		addUserButton.setEnabled(false);
		removeUserButton.setEnabled(false);
		assignedUserTableScrollPane.setEnabled(false);
		possibleUserTableScrollPane.setEnabled(false);
		assignedUserTable.setEnabled(false);
		possibleUserTable.setEnabled(false);
		DB.getAllUsers(new UpdateTablesCallback(selectedSubId, selectedPossibleId));
	}
	
	private String getSelectedSubId() {
		int selectedRow = assignedUserTable.getSelectedRow();
		if (selectedRow >= 0 && selectedRow < assignedUserTable.getRowCount()) {
			return (String) assignedUserTable.getModel().getValueAt(assignedUserTable.getSelectedRow(), ID);
		} else {
			return "";
		}
	}
	
	private String getSelectedPosId() {
		int selectedRow = possibleUserTable.getSelectedRow();
		if (selectedRow >= 0 && selectedRow < possibleUserTable.getRowCount()) {
			return (String) possibleUserTable.getModel().getValueAt(possibleUserTable.getSelectedRow(), ID);
		} else {
			return "";
		}
	}
	
	/**
	 *
	 * Callback to populate the table with all the requirements
	 * @author Josh
	 * @author Jacob Palnick
	 * @author William Terry
	 */
	class UpdateTablesCallback implements UsersCallback {
		
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
		 * @param users a list of all requirements
		 */
		@Override
		public void callback(List<User> users) {
			if (users.size() > 0) {
				// put the data in the table
				ArrayList<Object[]> subEntriesList = new ArrayList<Object[]>();
				ArrayList<Object[]> posEntriesList = new ArrayList<Object[]>();
				for(User user : users) {
					String id = String.valueOf(user.getIdNum());
					String name = user.getName();
					String username = user.getUsername();
					String permissions = String.valueOf(user.getPermission(user));
					if (users.contains(id)) {
						Object[] subEntry = new Object[ROWS];
						subEntry[ID] = id;
						subEntry[NAME] = name;
						subEntry[USERNAME] = username;
						subEntry[PERMISSIONLEVEL] = permissions;
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

				assignedUserTableModel.setData(subEntries);
				assignedUserTableModel.fireTableStructureChanged();
				possibleUserTableModel.setData(posEntries);
				possibleUserTableModel.fireTableStructureChanged();
			}
			else {
				// do nothing, there are no requirements
			}
		
			TableColumn column = null;
			TableColumn column2 = null;
			for (int i = 0; i < ROWS; i++) {
				column = assignedUserTable.getColumnModel().getColumn(i);
				column2 = possibleUserTable.getColumnModel().getColumn(i);
				if (i == ID) {
					column.setPreferredWidth(30); //third column is bigger
					column2.setPreferredWidth(30); //third column is bigger
				}
				else if (i == NAME) {
					column.setPreferredWidth(500);
					column2.setPreferredWidth(500);
				}
				else if (i == USERNAME) {
					column.setPreferredWidth(700);
					column2.setPreferredWidth(700);
				}
				else {
					column.setPreferredWidth(200);
					column2.setPreferredWidth(200);
				}
			}
			assignedUserTableScrollPane.setEnabled(true);
			possibleUserTableScrollPane.setEnabled(true);
			assignedUserTable.setEnabled(true);
			possibleUserTable.setEnabled(true);
			
			//reselect the previous selections if possible
			for (int i = 0; i<assignedUserTable.getRowCount(); i++) {
				String tmp = (String) assignedUserTable.getModel().getValueAt(i, ID);
				if(tmp != null && tmp.equals(selectedSub)) {
					assignedUserTable.setRowSelectionInterval(i, i);
				}
			}
			for (int i = 0; i<possibleUserTable.getRowCount(); i++) {
				String tmp = (String) possibleUserTable.getModel().getValueAt(i, ID);
				if(tmp != null && tmp.equals(selectedPos)) {
					possibleUserTable.setRowSelectionInterval(i, i);
				}
			}
//			updateSelectedPossible(getSelectedPosId());

		}
		
	}
}

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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.janeway.gui.widgets.JPlaceholderTextField;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.AddPermissionController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.EditPermissionController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.PermissionsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollablePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * GUI for a project manager to manage user permissions 
 *
 * @author William Terry
 * @author vpatara
 * @author josh
 */
@SuppressWarnings("serial")
public class PermissionsPanel extends JPanel implements ScrollablePanel {
	/** a permissions model */
	Permissions newModel;
	/** a permissions model */
	Permissions editModel;
	
	/** the tab that made this */
	ScrollableTab parent;
	/** labels */
	JLabel lbl1, lbl2, lbl3, lblname, lblusername, lblprofile, lblpermissions, lblID;
	JPlaceholderTextField usernameTextArea;
	/** a text field */
	JTextField addStatus;
	/** the buttons */
	JButton submitButton, updateButton;
	/** the dropdown menus */
	JComboBox permissionSelectNew, permissionSelectExisting;
	/** the panel for the profiles */
	JPanel profilePanel;
	/** a scroll pane */
	JScrollPane tablePane;
	/** the table for the profiles */
	JTable profileTable;
	/** the model for the table */
	ViewPermissionsTable tableModel;
	/** the top panel */
	JPanel topPanel;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	/**
	 * constructor
	 * @param permissionsTab the tab that created this
	 */
	public PermissionsPanel(){
		newModel = null;
		editModel = null;
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
		
		// Populate the form with the contents of the permissions model and update the TextUpdateListeners.
		updateFields();
	}

	@Override
	public void setTab(ScrollableTab tab) {
		parent = tab;
	}

	/**
	 *add the components to the view
	 *
	 */
	private void addComponents() {
		setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		lbl1 = new JLabel("Add ");
		lbl2 = new JLabel(" to project with ");
		lbl3 = new JLabel(" privilege");
		lblprofile = new JLabel("Selected Profile");
		lblname = new JLabel("Name: ");
		lblusername = new JLabel("Username: ");
		
		usernameTextArea =  new JPlaceholderTextField("Username", 20);
		usernameTextArea.addMouseListener(new MouseListener() {
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 1) {
					setAddPermissionStatus("");
				}
			}
		});

		addStatus = new JTextField("");
		addStatus.setOpaque(false);
		addStatus.setEditable(false);
		
		submitButton = new JButton("Submit");
		
		PermissionLevel[] levels = PermissionLevel.values();
		permissionSelectNew = new JComboBox(levels);
		permissionSelectExisting = new JComboBox(levels);
		permissionSelectExisting.setEnabled(false);
		
		permissionSelectNew.addMouseListener(new MouseListener() {
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 1) {
					setAddPermissionStatus("");
				}
			}
		});

		submitButton.addActionListener(new AddPermissionController(this));

		instantiateTable();
		tablePane = new JScrollPane(profileTable);
		instantiateProfilePanel();
		
		topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		topPanel.add(lbl1, c);
		
		c.gridx = 1;
		topPanel.add(usernameTextArea, c);
		
		c.gridx = 2;
		topPanel.add(lbl2, c);
		
		c.gridx = 3;
		topPanel.add(permissionSelectNew, c);
		
		c.gridx = 4;
		topPanel.add(lbl3, c);
		
		c.gridx = 5;
		topPanel.add(submitButton, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 6;
		c.weighty = 0.1;
		topPanel.add(addStatus, c);

		add(topPanel, BorderLayout.PAGE_START);
		add(tablePane, BorderLayout.CENTER);
		add(profilePanel, BorderLayout.LINE_END);
		
	}
	
	/**
	 * create the profile panel
	 *
	 */
	private void instantiateProfilePanel() {
		lblprofile = new JLabel("Selected Profile");
		lblname = new JLabel("Name: ");
		lblusername = new JLabel("Username: ");
		lblID = new JLabel("ID number: ");
		lblpermissions = new JLabel("Permission privileges: ");
		updateButton = new JButton("Update");
		updateButton.addActionListener(new EditPermissionController(this));
		updateButton.setEnabled(false);

		lblname.setVisible(false);
		lblID.setVisible(false);

		profilePanel =new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		profilePanel.add(lblprofile, c);
		c.gridy = 1;
		profilePanel.add(lblname, c);
		c.gridy = 2;
		profilePanel.add(lblusername, c);
		c.gridy = 3;
		profilePanel.add(lblID, c);
		c.gridy = 4;
		c.gridwidth = 1;
		profilePanel.add(lblpermissions, c);
		c.gridx = 1;
		profilePanel.add(permissionSelectExisting, c);
		c.gridx = 0;
		c.gridy = 5;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		profilePanel.add(updateButton, c);
		
	}

	/**
	 *update the labels for the profiles
	 *
	 */
	public void updateProfileLabels() {
		System.out.println("update profile labels");
		
		if(editModel != null) {
			
			// Update fields and enable elements
			lblusername.setText("Username: " + editModel.getUsername());
			permissionSelectExisting.setSelectedIndex(editModel.getPermissionLevel().ordinal());
			permissionSelectExisting.setEnabled(true);
			updateButton.setEnabled(true);
			
		} else {
			
			// Clear fields and disable elements
			lblname.setText("Name: ");
			lblusername.setText("Username: ");
			lblID.setText("ID: ");
			permissionSelectExisting.setSelectedIndex(0);
			permissionSelectExisting.setEnabled(false);
			updateButton.setEnabled(false);
		}
		profilePanel.repaint();
	}

	/**
	 *create the table
	 *
	 */
	private void instantiateTable() {
		
		tableModel = new ViewPermissionsTable();
		profileTable = new JTable(tableModel);
		profileTable.setPreferredScrollableViewportSize(new Dimension(1, 1));
		profileTable.setFillsViewportHeight(true);
		
		updateAllPermissionsList();
		
		profileTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                	DB.getSinglePermission((String) profileTable.getModel().getValueAt(profileTable.getSelectedRow(), 0),
                			new SinglePermissionCallback() {
						@Override
						public void callback(Permissions profile) {
							updateModel(profile);
						}
						@Override
						public void failure() {
							// TODO: may have to remove "Add"
							setAddPermissionStatus("Error while retrieving the permission object");
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
	 *populate the table with permissions
	 *
	 */
	public void updateAllPermissionsList() {
		DB.getAllPermissions(new UpdateTableCallback());
	}
	
	/**
	 * close the tab
	 *
	 */
	public void close() {
		parent.getTabController().closeCurrentTab();
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
	 * Sets a status for permission creation
	 *
	 * @param status to be displayed
	 */
	public void setAddPermissionStatus(String status) {
		addStatus.setText(status);
	}
	
	/**
	 * Updates the PermissionsPanel's model to contain the values of the given Permission and sets the 
	 * PermissionsPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param permission The Permission which contains the new values for the model.
	 */
	public void refreshModel() {
		updateModel(editModel);
		System.out.println("refresh permission model");
	}

	/**
	 * Updates the PermissionsPanel's model to contain the values of the given Permission.
	 *
	 * @param permission The Permissions which contains the new values for the model.
	 * @param mode The new editMode.
	 */
	protected void updateModel(Permissions permission) {
		editModel = permission;
		updateFields();	
	}
	
	/**
	 * Updates the PermissionPanel's fields to match those in the current model
	 *
	 */
	private void updateFields() {
		updateProfileLabels();
	}

	/**
	 * Returns a boolean representing whether or not input is enabled for the PermissionsPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the PermissionsPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

	/**
	 * Gets the PermissionsPanel's (new) internal model.
	 * @return
	 */
	public Permissions getNewModel() {

		PermissionLevel permissionLevel = PermissionLevel.values()[permissionSelectNew.getSelectedIndex()];
		newModel = new Permissions(usernameTextArea.getText(), permissionLevel);
		
		return newModel;
	}

	/**
	 * Gets the PermissionsPanel's (existing) internal model.
	 * @return
	 */
	public Permissions getUpdatedModel() {
		// editModel should not be null at this point
		PermissionLevel permissionLevel = PermissionLevel.values()[permissionSelectExisting.getSelectedIndex()];
		editModel.setPermissionLevel(permissionLevel);
		
		return editModel;
	}
	
	/**
	 * @return the current table model
	 */
	public ViewPermissionsTable getTable(){
		return tableModel;
	}
	
	/**
	 * Clear all fields for permission creation
	 *
	 */
	public void resetCreationFields() {
		newModel = null;
		usernameTextArea.setText("");
	}

	/**
	 * Clear all fields for permission editing
	 *
	 */
	public void resetUpdateFields() {
		updateModel(null);
	}
	
	/**
	 *
	 * Callback class to update the permissions table
	 * @author 
	 *
	 */
	class UpdateTableCallback implements PermissionsCallback {
		/**
		 * callback to update the permissions table
		 *
		 * @param profiles list of permissions
		 */
		@Override
		public void callback(List<Permissions> profiles) {
			System.out.println("profile count : " + profiles.size());
			if (profiles.size() > 0) {
				// put the data in the table
				Object[][] entries = new Object[profiles.size()][2];
				int i = 0;
				for(Permissions p: profiles) {
					entries[i][0] = p.getUsername();
					entries[i][1] = p.getPermissionLevel().toString();
					i++;
				}
				getTable().setData(entries);
				getTable().fireTableStructureChanged();
			}
			else {
				// do nothing, there are no requirements
			}
		}
	}
}

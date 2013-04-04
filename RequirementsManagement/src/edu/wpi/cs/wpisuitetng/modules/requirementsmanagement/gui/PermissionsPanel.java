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
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddPermissionController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.EditPermissionController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.PermissionsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * GUI for a project manager to manage user permissions 
 *
 * @author William Terry
 *
 */
@SuppressWarnings("serial")
public class PermissionsPanel extends JPanel {
	Permissions newModel;
	Permissions editModel;
	
	PermissionsTab parent;
//	private GridBagLayout panelLayout;
	JLabel lbl1, lbl2, lbl3, lblname, lblusername, lblprofile, lblpermissions, lblID;
	HintedTextArea usernameTextArea;
	JTextField addStatus;
	JButton submitButton, updateButton;
	JComboBox permissionSelectNew, permissionSelectExisting;
	JPanel profilePanel;
	JScrollPane tablePane;
	JTable profileTable;
	ViewPermissionsTable tableModel;
//	String sName, sUsername, sID;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	public PermissionsPanel(PermissionsTab permissionsTab){
		this.parent = permissionsTab;
		newModel = null;
		editModel = null;
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		updateFields();
	}

	private void addComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		lbl1 = new JLabel("Add ");
		lbl2 = new JLabel(" to project with ");
		lbl3 = new JLabel(" privilege");
		lblprofile = new JLabel("Selected Profile");
		lblname = new JLabel("Name: ");
		lblusername = new JLabel("Username: ");
		
		usernameTextArea = new HintedTextArea(1, 20, "Username");
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
		permissionSelectNew = new JComboBox<PermissionLevel>(levels);
		permissionSelectExisting = new JComboBox<PermissionLevel>(levels);
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
		//tablePane.setRowHeaderView(profileTable.getTableHeader());
		instantiateProfilePanel();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		add(lbl1, c);
		
		c.gridx = 1;
		add(usernameTextArea, c);
		
		c.gridx = 2;
		add(lbl2, c);
		
		c.gridx = 3;
		add(permissionSelectNew, c);
		
		c.gridx = 4;
		add(lbl3, c);
		
		c.gridx = 5;
		add(submitButton, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 6;
		c.weighty = 0.1;
		add(addStatus, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 3;
		c.weighty = 1.0;
		add(profileTable, c);
		// TODO: display tablePane and table header
//		add(tablePane, c);
		
		c.gridx = 4;
		c.gridwidth = 2;
		add(profilePanel, c);
		
	}
	
	private void instantiateProfilePanel() {
		lblprofile = new JLabel("Selected Profile");
		lblname = new JLabel("Name: ");
		lblusername = new JLabel("Username: ");
		lblID = new JLabel("ID number: ");
		lblpermissions = new JLabel("Permission privileges: ");
		updateButton = new JButton("Update");
		updateButton.addActionListener(new EditPermissionController(this));
		updateButton.setEnabled(false);
		
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

	public void updateProfileLabels() {
		System.out.println("update profile labels");
		
		if(editModel != null) {
			
			// Update fields and enable elements
//			lblname.setText("Name: " + model.);
			lblusername.setText("Username: " + editModel.getUsername());
//			lblID.setText("ID: " + model.);
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
                	});
                }
			}
			@Override public void mouseReleased(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseClicked(MouseEvent arg0) {}
		});
	}

	public void updateAllPermissionsList() {
		DB.getAllPermissions(new UpdateTableCallback());
	}
	
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
	 * Returns a boolean representing whether or not input is enabled for the DefectPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the DefectPanel and its children.
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
		return this.tableModel;
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
	
	class UpdateTableCallback implements PermissionsCallback {
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
	
//	public static void main(String[] args){
//		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
//	            public void run() {
//	            	JFrame frame = new JFrame("ComboBoxDemo");
//	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	         
//	                //Create and set up the content pane.
//	                JComponent newContentPane = new PermissionsPanel(null);
//	                newContentPane.setOpaque(true); //content panes must be opaque
//	                frame.setContentPane(newContentPane);
//	         
//	                //Display the window.
//	                frame.pack();
//	                frame.setVisible(true);
//	            }
//	        });
//	}
}

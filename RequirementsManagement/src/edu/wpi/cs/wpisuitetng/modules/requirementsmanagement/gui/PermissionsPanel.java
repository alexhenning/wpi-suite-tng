package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddPermissionController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SinglePermissionCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.IterationPanel.UpdateIterationIdCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel.UpdateTableCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * 
 * @author William Terry
 *
 */
public class PermissionsPanel extends JPanel {
	Permissions model;
	
	PermissionsTab parent;
	private GridBagLayout panelLayout;
	JLabel lbl1, lbl2, lbl3, lblname, lblusername, lblprofile, lblpermissions, lblID;
	HintedTextArea username;
	JButton submit, update;
	JComboBox permissionSelectNew, permissionSelectExisting;
	JPanel profilePanel;
	JScrollPane tablePane;
	JTable profileTable;
	String sName, sUsername, sID;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	public PermissionsPanel(PermissionsTab permissionsTab){
		this.parent = permissionsTab;
//		model = parent.permission;
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		updateFields();
	}

	
	private void addComponents(){
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		lbl1 = new JLabel("Add ");
		lbl2 = new JLabel(" to project with ");
		lbl3 = new JLabel(" privelages.");
		lblprofile = new JLabel("Selected Profile");
		lblname = new JLabel("Name: ");
		lblusername = new JLabel("Username: ");
		
		username = new HintedTextArea(1, 20, "Username");
		
		submit = new JButton("Submit");
		
		PermissionLevel[] levels = PermissionLevel.values();
		permissionSelectNew = new JComboBox<PermissionLevel>(levels);
		permissionSelectExisting = new JComboBox<PermissionLevel>(levels);
		
		submit.addActionListener(new AddPermissionController(this));
		
		instantiateTable();
		tablePane = new JScrollPane();
		tablePane.add(profileTable);
		instantiateProfilePanel();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		add(lbl1, c);
		
		c.gridx = 1;
		add(username, c);
		
		c.gridx = 2;
		add(lbl2, c);
		
		c.gridx = 3;
		add(permissionSelectNew, c);
		
		c.gridx = 4;
		add(lbl3, c);
		
		c.gridx = 5;
		add(submit, c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.weighty = 1.0;
		add(tablePane, c);
		
		c.gridx = 4;
		c.gridwidth = 2;
		add(profilePanel, c);
		
	}
	
	private void instantiateProfilePanel() {
		lblprofile = new JLabel("Selected Profile");
		lblname = new JLabel("Name: "+sName);
		lblusername = new JLabel("Username: "+sUsername);
		lblID = new JLabel("ID number: "+sID);
		lblpermissions = new JLabel("Permission privileges: ");
		update = new JButton("Update");
		update.addActionListener(new AddPermissionController(this));
		
		profilePanel =new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		profilePanel.add(lblprofile, c);
		c.gridy = 1;
		profilePanel.add(lblname, c);
		c.gridy = 2;
		profilePanel.add(lblusername, c);
		c.gridy = 3;
		c.gridwidth = 1;
		profilePanel.add(lblpermissions, c);
		c.gridx = 1;
		profilePanel.add(permissionSelectExisting, c);
		c.gridx = 0;
		c.gridy = 4;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		profilePanel.add(update, c);
		
	}

	public void updateProfileLabels(){
		lblname.setText("Name: "+sName);
		lblusername.setText("Username: "+sUsername);
		lblID.setText("ID number: "+sID);
		profilePanel.repaint();
	}

	private void instantiateTable() {
	profileTable = new JTable(new ViewPermissionsTable());
		
		updateAllPermissionsList();
		
		profileTable.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                	DB.getSinglePermissions((String) profileTable.getModel().getValueAt(profileTable.getSelectedRow(), 0),
                			new SinglePermissionCallback() {
						@Override
						public void callback(Permissions profile) {
							parent.tabController.addEditPermissionsTab(profile);
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
//		DB.getAllPermissions(new UpdateTableCallback());
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
	
	public boolean doDatesOverlap() {
		return false;
	}
	
	/**
	 * Updates the IterationPanel's model to contain the values of the given Iteration and sets the 
	 * IterationPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param iteration	The Iteration which contains the new values for the model.
	 */
	public void refreshModel() {
		updateModel(model);
	}

	/**
	 * Updates the IterationPanel's model to contain the values of the given Iteration.
	 * 
	 * @param	iteration	The Iteration which contains the new values for the model.
	 * @param	mode	The new editMode.
	 */
	protected void updateModel(Permissions permission) {
		model = permission;
		updateFields();	
	}
	
	/**
	 * Updates the PermissionPanel's fields to match those in the current model
	 *
	 */
	private void updateFields() {
		//TODO finish this
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
	 * Gets the IterationPanel's internal model.
	 * @return
	 */
	public Permissions getModel() {
//		System.out.println("getting model from panel");
		//TODO: implement getting model from panel
		
		return model;
	}
	
	public static void main(String[] args){
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	            	JFrame frame = new JFrame("ComboBoxDemo");
	                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	         
	                //Create and set up the content pane.
	                JComponent newContentPane = new PermissionsPanel(null);
	                newContentPane.setOpaque(true); //content panes must be opaque
	                frame.setContentPane(newContentPane);
	         
	                //Display the window.
	                frame.pack();
	                frame.setVisible(true);
	            }
	        });
	}
}

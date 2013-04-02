package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.IterationPanel.UpdateIterationIdCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

public class PermissionsPanel extends JPanel {
	Permissions model;
	
	PermissionsTab parent;
	private GridBagLayout panelLayout;
	JLabel lbl1, lbl2, lbl3, lblname, lblusername, lblprofile;
	JTextField username;
	JButton submit, update;
	JComboBox permissionSelect;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	public PermissionsPanel(PermissionsTab permissionsTab){
		this.parent = permissionsTab;
		model = parent.permission;
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		updateFields();
	}

	
	private void addComponents(){
		panelLayout =new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(panelLayout);
		
		lbl1 = new JLabel("Add ");
		lbl2 = new JLabel(" to project with ");
		lbl3 = new JLabel(" privelages.");
		lblprofile = new JLabel("Selected Profile");
		lblname = new JLabel("Name: ");
		lblusername = new JLabel("Username: ");
		
		submit = new JButton("Submit");
		update = new JButton("Update");
		
		permissionSelect = new JComboBox<PermissionLevel>();
		
		
		username = new JTextField();
		
		//submit.addActionListener(new AddPermissionsController(this));
		//update.addActionListener(new ADDPermissionsController(this));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(lbl1, c);
		c.gridx = 1;
		//add(startDate, c);
		c.gridy = 1;
		c.gridx = 0;
		//add(lbl2, c);
		c.gridx = 1;
		//add(endDate, c);
		c.gridy = 2;
		c.gridx = 0;
		add(lbl3, c);
		c.gridx = 1;
		//add(iterationNumber, c);
		c.gridy = 3;
		add(submit, c);
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
}

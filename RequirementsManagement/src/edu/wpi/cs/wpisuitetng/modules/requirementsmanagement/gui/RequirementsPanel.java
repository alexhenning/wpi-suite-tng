package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.EditRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

@SuppressWarnings("serial")
public class RequirementsPanel extends JPanel {
	public enum Mode {
		CREATE,
		EDIT;
	}

	/** The parent view **/
	protected RequirementsTab parent;
	
	/** The Defect displayed in this panel */
	protected RequirementModel model;

	/*
	 * Form elements
	 */
	public JTextField namefield = new JTextField(30);
	public JTextArea descriptionfield = new JTextArea(6, 0);
	RequirementPriority[] priorityStrings = RequirementPriority.values();
	public JComboBox priority = new JComboBox(priorityStrings);
	RequirementStatus[] statusStrings = RequirementStatus.values();
	public JComboBox statusfield = new JComboBox(statusStrings);
	public JTextField estimateField = new JTextField(30);
	public JTextField results = new JTextField(50);
	JButton submit = new JButton("Submit");
	
	/** The layout manager for this panel */
	protected SpringLayout layout;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;
	
	/** An enum indicating if the form is in create mode or edit mode */
	protected Mode editMode;

	/*
	 * Constants used to layout the form
	 */
	protected static final int HORIZONTAL_PADDING = 5;
	protected static final int VERTICAL_PADDING = 15;
	protected static final int LABEL_ALIGNMENT = JLabel.TRAILING;

	/**
	 * Constructs a DefectPanel for creating or editing a given Defect.
	 * 
	 * @param parent	The parent DefectView.
	 * @param defect	The Defect to edit.
	 * @param mode		Whether or not the given Defect should be treated as if it already exists 
	 * 					on the server ({@link Mode#EDIT}) or not ({@link Mode#CREATE}).
	 */
	public RequirementsPanel(RequirementsTab parent, RequirementModel requirement, Mode mode) {
		this.parent = parent;
		this.model = requirement;
		editMode = mode;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Use a SpringLayout manager
		layout = new SpringLayout();
		this.setLayout(layout);


		// Add all components to this panel
		addComponents();
		
		// TODO: prevent tab key from inserting tab characters into the description field
		
		// Populate the form with the contents of the Defect model and update the TextUpdateListeners.
		// TODO: updateFields();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//name field
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout());
		JLabel nameArea = new JLabel("Name:");
		namePanel.add(nameArea);
		namePanel.add(namefield);
		
		//priority field
		JPanel priorityPanel = new JPanel();
		priorityPanel.setLayout(new FlowLayout());
		JLabel priorityArea = new JLabel("Priority:");
		priorityPanel.add(priorityArea);
		priorityPanel.add(priority);
		
		//description field
		JPanel descPanel = new JPanel();
		descPanel.setLayout(new FlowLayout());
		JLabel descriptionArea = new JLabel("Description:");
		descPanel.add(descriptionArea);
		
		descriptionfield.setLineWrap(true);
		JScrollPane descScrollPane = new JScrollPane(descriptionfield);
		descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		descPanel.add(descScrollPane);
		//descPanel.add(descriptionfield);
		
		//status field
		JPanel statPanel = new JPanel();
		statPanel.setLayout(new FlowLayout());
		JLabel statusArea = new JLabel("Status:");
		statPanel.add(statusArea);
		// TODO: For now, it's disabled for creation view. Need to change when
		// updating is available.
		if(this.editMode == Mode.CREATE) {
			statusfield.setEnabled(false);
		} else {
			statusfield.setEnabled(true);
		}
		statPanel.add(statusfield);
		
		//estimate field
		JPanel estPanel = new JPanel();
		estPanel.setLayout(new FlowLayout());
		JLabel estimateArea = new JLabel("Estimate:");
		estPanel.add(estimateArea);
		if(this.editMode == Mode.CREATE) {
			estimateField.setEditable(false);
		} else {
			estimateField.setEditable(true);
		}
		estPanel.add(estimateField);
	
		//submit panel
		JPanel submitPanel = new JPanel();
		submitPanel.setLayout(new FlowLayout());
		if(this.editMode == Mode.CREATE) { 
			submit.setAction(new AddRequirementController(this));
			submit.setText("Save");
		} else {
			submit.setAction(new EditRequirementController(this));
			submit.setText("Update");
		}
		
		//submit button
		submitPanel.add(submit);
		//results field
		submitPanel.add(results);
		
		// Add subpanels to main panel
		// Left side (gridx = 0) and aligned right (east)
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		add(nameArea, c);
		c.gridy = 1;
		add(priorityArea, c);
		c.gridy = 2;
		add(descriptionArea, c);
		c.gridy = 3;
		add(statusArea, c);
		c.gridy = 4;
		add(estimateArea, c);
		c.gridy = 5;
		// Make the save button taller
		c.ipady = 20;
		add(submit, c);
		c.ipady = 0;
		
		// Right side (gridx = 1)
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		add(namefield, c);
		c.gridy = 2;
		add(descScrollPane, c);
		c.gridy = 4;
		add(estimateField, c);
		c.gridy = 5;
		add(results, c);

		// Right side but aligned left (west) for dropdowns
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.NONE;
		c.gridy = 1;
		add(priority, c);
		c.gridy = 3;
		add(statusfield, c);
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
	 * Updates the DefectPanel's model to contain the values of the given Defect and sets the 
	 * DefectPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param defect	The Defect which contains the new values for the model.
	 */
	protected void updateModel(RequirementModel requirement) {
		updateModel(requirement, Mode.EDIT);
	}

	/**
	 * Updates the DefectPanel's model to contain the values of the given Defect.
	 * 
	 * @param	defect	The Defect which contains the new values for the model.
	 * @param	mode	The new editMode.
	 */
	protected void updateModel(RequirementModel requirement, Mode mode) {
		// TODO: 
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
	 * Gets the DefectPanel's internal model.
	 * @return
	 */
	public RequirementModel getModel() {
		model.setName(namefield.getText());
		model.setPriority((RequirementPriority) priority.getSelectedItem());
		model.setDescription(descriptionfield.getText());
		model.setStatus((RequirementStatus) statusfield.getSelectedItem());
		model.setEstimate(estimateField.getText()); // TODO: Should be an integer
		return model;
	}
	
	/**
	 * Returns the parent DefectView.
	 * 
	 * @return the parent DefectView.
	 */
	public RequirementsTab getParent() {
		return parent;
	}

	/**
	 * Returns the edit {@link Mode} for this DefectPanel.
	 * 
	 * @return	The edit {@link Mode} for this DefectPanel.
	 */
	public Mode getEditMode() {
		return editMode;
	}

	public void setStatus(String string) {
		results.setText(string);
	}
}

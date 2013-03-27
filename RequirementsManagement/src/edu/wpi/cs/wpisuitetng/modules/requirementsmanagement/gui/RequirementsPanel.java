package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.EditRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;

@SuppressWarnings("serial")
public class RequirementsPanel extends JSplitPane {
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
	public JTextField estimateField = new JTextField("0", 30);
	public JTextField results = new JTextField(50);
	JButton submit = new JButton("Submit");
	private NoteMainPanel nt;
	private JPanel leftside = new JPanel();
	public JTabbedPane supplementPane = new JTabbedPane();

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
		super(JSplitPane.HORIZONTAL_SPLIT);
		setLeftComponent(leftside);
		setRightComponent(supplementPane);
		
		this.parent = parent;
		this.model = requirement;
		editMode = mode;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		parent.buttonGroup.setMode(mode);
		
		// TODO: prevent tab key from inserting tab characters into the description field
		
		// Populate the form with the contents of the Defect model and update the TextUpdateListeners.
		updateFields();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		leftside.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//name field
		JLabel nameArea = new JLabel("Name:");
		JLabel priorityArea = new JLabel("Priority:");
		JLabel descriptionArea = new JLabel("Description:");
		descriptionfield.setLineWrap(true);
		JScrollPane descScrollPane = new JScrollPane(descriptionfield);
		descScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		JLabel statusArea = new JLabel("Status:");
		// TODO: For now, it's disabled for creation view. Need to change when
		// updating is available.
		statusfield.setEnabled(false);
		
		//estimate field
		JLabel estimateArea = new JLabel("Estimate:");
		if(this.editMode == Mode.CREATE) {
			estimateField.setEditable(false);
		} else {
			estimateField.setEditable(true);
		}
	
		//submit panel
		if(this.editMode == Mode.CREATE) { 
			submit.setAction(new AddRequirementController(this));
			submit.setText("Save");
		} else {
			submit.setAction(new EditRequirementAction());
			submit.setText("Update");
		}
		
		// Supplement Pane (i.e., notes, history, attachments)
		nt = new NoteMainPanel(this);
		supplementPane.add("Notes", nt);
		supplementPane.add("History", new JPanel());
		
		// Add subpanels to main panel
		// Left side (gridx = 0) and aligned right (east)
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		leftside.add(nameArea, c);
		c.gridy = 1;
		leftside.add(priorityArea, c);
		c.gridy = 2;
		leftside.add(descriptionArea, c);
		c.gridy = 3;
		leftside.add(statusArea, c);
		c.gridy = 4;
		leftside.add(estimateArea, c);
		c.gridy = 5;
		// Make the save button taller
		c.ipady = 20;
		leftside.add(submit, c);
		c.ipady = 0;
		
		// Right side (gridx = 1)
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		leftside.add(namefield, c);
		c.gridy = 2;
		leftside.add(descScrollPane, c);
		c.gridy = 4;
		leftside.add(estimateField, c);
		c.gridy = 5;
		leftside.add(results, c);

		// Right side but aligned left (west) for dropdowns
		c.anchor = GridBagConstraints.WEST;
		//c.fill = GridBagConstraints.NONE;
		c.gridy = 1;
		leftside.add(priority, c);
		c.gridy = 3;
		leftside.add(statusfield, c);
		
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
	public void refreshModel() {
		updateModel(model, Mode.EDIT);
	}
	
	/**
	 * Updates the DefectPanel's model to contain the values of the given Defect and sets the 
	 * DefectPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param defect	The Defect which contains the new values for the model.
	 */
	public void updateModel(RequirementModel requirement) {
		updateModel(requirement, Mode.EDIT);
	}

	/**
	 * Updates the DefectPanel's model to contain the values of the given Defect.
	 * 
	 * @param	defect	The Defect which contains the new values for the model.
	 * @param	mode	The new editMode.
	 */
	protected void updateModel(RequirementModel requirement, Mode mode) {
		editMode = mode;
		model = requirement;
		updateFields();	
	}
	
	/**
	 * Updates the RequirementsPanel's fields to match those in the current model
	 *
	 */
	private void updateFields() {
		namefield.setText(model.getName());
		descriptionfield.setText(model.getDescription());
		for(int i = 0; i < statusfield.getItemCount(); i++) {  // This is really round about, but it didn't seem to work comparing RequirementStatuses
			if(model.getStatus() == RequirementStatus.valueOf(statusfield.getItemAt(i).toString())) {
				statusfield.setSelectedIndex(i);
			}
		}
		for(int i = 0; i < priority.getItemCount(); i++) {  // Same as above
			if(model.getPriority() == RequirementPriority.valueOf(priority.getItemAt(i).toString())) {
				priority.setSelectedIndex(i);
			}
		}
		estimateField.setText(model.getEstimate());
		if(this.editMode == Mode.CREATE) {
			estimateField.setEditable(false);
		} else {
			estimateField.setEditable(true);
		}
		if(this.editMode == Mode.CREATE) { 
			submit.setAction(new AddRequirementController(this));
			submit.setText("Save");
		} else {
			submit.setAction(new EditRequirementAction());
			submit.setText("Update");
		}
		if (editMode.equals(Mode.EDIT)) {
			parent.setEditModeDescriptors(model);
		}
		parent.buttonGroup.setMode(editMode);
		
		nt.setNotes(Arrays.asList(model.getNotes()));
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
	
	class EditRequirementAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DB.updateRequirements(model, new SingleRequirementCallback() {
				@Override
				public void callback(RequirementModel req) {
					setStatus("Requirement Updated");
				}
			});
		}
	}
}

/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //Josh
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementType;

@SuppressWarnings("serial")
public class RequirementsPanel extends JSplitPane implements KeyListener{

	/** The parent view **/
	protected RequirementsTab parent;
	
	/** The requirement displayed in this panel */
	protected RequirementModel model;

	/*
	 * Form elements
	 */
	public JTextField namefield = new JTextField(35);
	public JTextArea descriptionfield = new JTextArea(6, 0);
	RequirementPriority[] priorityStrings = RequirementPriority.values();
	RequirementType[] typeStrings = RequirementType.values();
	Iteration[] iterations;// = new String() {""};//new String()[];
	//releaseNumberStrings[0] = "";
	public JComboBox priority = new JComboBox(priorityStrings);
	public JComboBox type = new JComboBox(typeStrings);
	public JComboBox iteration = new JComboBox();// = new JComboBox(releaseNumberStrings);
	RequirementStatus[] statusStrings = RequirementStatus.values();
	public JComboBox statusfield = new JComboBox(statusStrings);
	public JTextField estimateField = new JTextField("0", 35);
	public JTextField actualEffortField = new JTextField("0", 35);
	public JTextField results = new JTextField(35);
	JButton submit = new JButton("Submit");
	private NoteMainPanel nt;
	private JPanel leftside = new JPanel();
	JScrollPane leftScrollPane;
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

	private void updateIterationList() {
		DB.getAllIterations(new UpdateIterationListCallback());
	}
	
	private void updateIterationComboBox() {
		DefaultComboBoxModel comboboxModel = new DefaultComboBoxModel();
		for(Iteration it : iterations) {
			if (it == null) {
				comboboxModel.addElement("");
			} else {
				comboboxModel.addElement(""+it.getIterationNumber());
			}
		}
		iteration.setModel(comboboxModel);
		iteration.setSelectedIndex(0);
		if (iteration.getItemCount() > 1){
			if(model.getIteration() != null) {
				String modelItStr = new Integer(model.getIteration().getIterationNumber()).toString();
				for(int i = 0; i < iteration.getItemCount(); i++) {  // Same as above
					if(modelItStr.equals(iteration.getItemAt(i).toString())) {
						iteration.setSelectedIndex(i);
					}
				}
			}
		}
//		leftside.revalidate();
	}
	
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
		leftScrollPane = new JScrollPane(leftside);
		leftScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		leftScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setLeftComponent(leftScrollPane);
		setRightComponent(supplementPane);
		
		this.parent = parent;
		this.model = requirement;
		editMode = mode;
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		updateIterationList();

		// Add all components to this panel
		addComponents();
		parent.buttonGroup.update(mode, model);
		
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
		JLabel typeArea = new JLabel("Type:");
		JLabel priorityArea = new JLabel("Priority:");
		JLabel iterationArea = new JLabel("Iteration:");
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
		JLabel actualEffortArea = new JLabel("Actual Effort:");
		if(this.editMode == Mode.CREATE) {
			estimateField.setEditable(false);
			actualEffortField.setEditable(false);
		} else {
			estimateField.setEditable(true);
			actualEffortField.setEditable(true);
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
//		if(this.editMode == Mode.EDIT) {
		nt = new NoteMainPanel(this);
		supplementPane.add("Notes", nt);
		supplementPane.add("History", new JPanel());
//		}
		
		// Add subpanels to main panel
		// Left side (gridx = 0) and aligned right (east)
		c.insets = new Insets(5, 5, 5, 5);
		c.anchor = GridBagConstraints.EAST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		leftside.add(nameArea, c);
		c.gridy = 1;
		leftside.add(typeArea, c);
		c.gridy = 2;
		leftside.add(priorityArea, c);
		c.gridy = 3;
		leftside.add(iterationArea, c);
		c.gridy = 4;
		leftside.add(descriptionArea, c);
		c.gridy = 5;
		leftside.add(statusArea, c);
		c.gridy = 6;
		leftside.add(estimateArea, c);
		c.gridy = 7;
		leftside.add(actualEffortArea, c);
		c.gridy = 8;
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
		c.anchor = GridBagConstraints.WEST;
		c.gridy = 1;
		leftside.add(type, c);
		c.gridy = 2;
		leftside.add(priority, c);
		c.gridy = 3;
		leftside.add(iteration, c);
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 4;
		leftside.add(descScrollPane, c);
		c.anchor = GridBagConstraints.WEST;
		c.gridy = 5;
		leftside.add(statusfield, c);
		c.anchor = GridBagConstraints.CENTER;
		c.gridy = 6;
		leftside.add(estimateField, c);
		c.gridy = 7;
		leftside.add(actualEffortField, c);
		c.gridy = 8;
		leftside.add(results, c);
		//pointless to allow user to edit result text
		results.setEditable(false); 
		//sets the minimum size that the user can reduce the window to manually
		leftside.setMinimumSize(new Dimension(600,700));
		leftScrollPane.setMinimumSize(new Dimension(600,700));
		supplementPane.setMinimumSize(new Dimension(525,700));
		
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
		if(editMode == Mode.CREATE && mode == Mode.EDIT) {
			nt = new NoteMainPanel(this);
			for(int i=0; i< supplementPane.getTabCount(); i++){
				if(supplementPane.getTitleAt(i).equals("Notes")) {
					supplementPane.remove(i);
					nt = null;
					nt = new NoteMainPanel(this);
					supplementPane.add(nt, i);
					supplementPane.setTitleAt(i, "Notes");
					supplementPane.setSelectedIndex(i);
				}
			}
//			supplementPane.ge
//			supplementPane.add("Notes", nt);
		}
		this.revalidate();
		editMode = mode;
		model = requirement;
		updateFields();	
	}
	
	
	/**
	 * Updates the RequirementsPanel's fields to match those in the current model
	 *
	 */
	private void updateStatusField() {
		for(int i = 0; i < statusfield.getItemCount(); i++) {  // This is really round about, but it didn't seem to work comparing RequirementStatuses
			if(model.getStatus() == RequirementStatus.valueOf(statusfield.getItemAt(i).toString())) {
				statusfield.setSelectedIndex(i);
			}
		}
	}
	
	/**
	 * Updates the RequirementsPanel's fields to match those in the current model
	 *
	 */
	private void updateFields() {
		namefield.addKeyListener(this);
		descriptionfield.addKeyListener(this);
		if(model.getIteration() != null && (model.getStatus() != RequirementStatus.COMPLETE || model.getStatus() != RequirementStatus.DELETED)) {
			model.setStatus(RequirementStatus.IN_PROGRESS);
		} else if(model.getIteration() == null && model.getStatus() == RequirementStatus.IN_PROGRESS) {
			model.setStatus(RequirementStatus.OPEN);
		}
		namefield.setText(model.getName());
		descriptionfield.setText(model.getDescription());
		updateStatusField();
		for(int i = 0; i < priority.getItemCount(); i++) {  // Same as above
			if(model.getPriority() == RequirementPriority.valueOf(priority.getItemAt(i).toString())) {
				priority.setSelectedIndex(i);
			}
		}
		for(int i = 0; i < type.getItemCount(); i++) {  // Same as above
			if(model.getType() == RequirementType.valueOf(type.getItemAt(i).toString())) {
				type.setSelectedIndex(i);
			}
		}
		if (iteration.getItemCount() > 1){
			iteration.setSelectedIndex(0);
			if(model.getIteration() != null) {
				String modelItStr = new Integer(model.getIteration().getIterationNumber()).toString();
				for(int i = 0; i < iteration.getItemCount(); i++) {  // Same as above
					if(modelItStr.equals(iteration.getItemAt(i).toString())) {
						iteration.setSelectedIndex(i);
					}
				}
			}
		}
		estimateField.setText(model.getEstimate()+"");
		actualEffortField.setText(model.getActualEffort()+"");
		if(this.editMode == Mode.CREATE || model.getStatus() == RequirementStatus.DELETED || model.getStatus() == RequirementStatus.COMPLETE) {
			estimateField.setEditable(false);
			actualEffortField.setEditable(false);
		} else {
			estimateField.setEditable(true);
			actualEffortField.setEditable(true);
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
		parent.buttonGroup.update(editMode, model);
		
		if (editMode.equals(Mode.EDIT) && (model.getStatus().equals(RequirementStatus.COMPLETE)
				|| model.getStatus().equals(RequirementStatus.COMPLETE) || model.getStatus().equals(RequirementStatus.DELETED))) {
			namefield.setEnabled(false);
			type.setEnabled(false);
			type.setBackground(Color.WHITE);
			priority.setEnabled(false);
			priority.setBackground(Color.WHITE);
			descriptionfield.setEnabled(false);
			estimateField.setEnabled(false);
			actualEffortField.setEnabled(false);
			submit.setEnabled(false);
			iteration.setEnabled(false);
			nt.setInputEnabled(false);
		}else if(namefield.getText().length() < 1 || namefield.getText().length() < 1){
			namefield.setEnabled(true);
			type.setEnabled(true);
			type.setBackground(Color.WHITE);
			priority.setEnabled(true);
			priority.setBackground(Color.WHITE);
			descriptionfield.setEnabled(true);
			estimateField.setEnabled(false);
			actualEffortField.setEnabled(false);
			submit.setEnabled(false);
			iteration.setEnabled(true);
//			if(editMode == Mode.EDIT) {
				nt.setInputEnabled(true);
		}
		else {
			namefield.setEnabled(true);
			type.setEnabled(true);
			type.setBackground(Color.WHITE);
			priority.setEnabled(true);
			priority.setBackground(Color.WHITE);
			descriptionfield.setEnabled(true);
			estimateField.setEnabled(true);
			actualEffortField.setEnabled(true);
			submit.setEnabled(true);
			iteration.setEnabled(true);
			nt.setInputEnabled(true);
		}
		
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
		model.setType((RequirementType) type.getSelectedItem());
		model.setPriority((RequirementPriority) priority.getSelectedItem());
		if (iteration.getSelectedItem().toString().equals("")){
			model.setIteration(null);
		} else {
			String selected = iteration.getSelectedItem().toString();
			for(Iteration it : iterations) {  // Same as above
				if(it != null && it.getIterationNumber() == selected) {
					model.setIteration(it);
				}
			}
		}
		model.setDescription(descriptionfield.getText());
		model.setStatus((RequirementStatus) statusfield.getSelectedItem());
		if(model.getIteration() != null && (model.getStatus() != RequirementStatus.COMPLETE || model.getStatus() != RequirementStatus.DELETED)) {
			model.setStatus(RequirementStatus.IN_PROGRESS);
			parent.buttonGroup.update(editMode, model);
			updateStatusField();
		} else if(model.getIteration() == null && model.getStatus() == RequirementStatus.IN_PROGRESS) {
			model.setStatus(RequirementStatus.OPEN);
			parent.buttonGroup.update(editMode, model);
			updateStatusField();
		}
		model.setEstimate(new Integer(estimateField.getText())); // TODO: Should be an integer
		model.setActualEffort(new Integer(actualEffortField.getText())); // TODO: Should be an integer
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
			if (!validateFields()){
				
			} else {
				getModel();
				DB.updateRequirements(model, new SingleRequirementCallback() {
					@Override
					public void callback(RequirementModel req) {
						if(model.getStatus() == RequirementStatus.DELETED) {
							setStatus("Requirement Deleted");
						} else {
							setStatus("Requirement Updated");
						}
					}
				});
			}}
	}
	
	class UpdateIterationListCallback implements IterationCallback {
		@Override
		public void callback(List<Iteration> iterationList) {
			iterations = new Iteration[iterationList.size()+1];
			iterations[0] = null;
			System.out.println("iterationList: "+iterationList);
			if (iterationList.size() > 0) {
				for(int i = 0; i<iterationList.size(); i++) {
					iterations[i+1] = iterationList.get(i);
				}
			}
			updateIterationComboBox();
		}
		
	}

	public boolean validateFields() {
		if(namefield.getText().length()<1) {
			namefield.setBackground(Color.RED);
			setStatus("Name must be 1-100 characters long.");
			return false;
		} else {
			namefield.setBackground(Color.WHITE);
		}
		if(descriptionfield.getText().length()<1) {
			descriptionfield.setBackground(Color.RED);
			setStatus("Description must be 1-5000 characters long.");
			return false;
		} else {
			descriptionfield.setBackground(Color.WHITE);
		}
		if(Integer.parseInt(estimateField.getText()) < 0){
			estimateField.setBackground(Color.RED);
			setStatus("Estimate must be non-negative integer.");
			System.out.println("Estimate value is " + estimateField.getText());
			return false;
		} else {
			estimateField.setBackground(Color.WHITE);
		}
		if(Integer.parseInt(actualEffortField.getText()) < 0){
			actualEffortField.setBackground(Color.RED);
			setStatus("Actual Effort must be a non-negative integer.");
			System.out.println("Estimate value is " + estimateField.getText());
			return false;
		} else {
			actualEffortField.setBackground(Color.WHITE);
		}
		System.out.println("Estimate value is " + estimateField.getText());

		return true;
	}
	//checked for input from keyboard
	public void keyTyped ( KeyEvent e ){  
		//check to see if name and description fields are empty or not
		if(namefield.getText().length() != 0 && descriptionfield.getText().length() != 0){
			submit.setEnabled(true);
		}
		if((namefield.getText().length()==0)
				|| ( descriptionfield.getText().length()==0)){
			submit.setEnabled(false);
		}
	
	}
	//check if key is pressed. Doesn't really do anything now, but needs to be included 
	public void keyPressed ( KeyEvent e){  

	}  
	//check if key is released. Doesn't really do anything now, but needs to be included 
	public void keyReleased ( KeyEvent e ){  
		//l1.setText( "Key Released" ) ; 
		if(namefield.getText().length() != 0 && descriptionfield.getText().length() != 0){
			submit.setEnabled(true);
		}
		if((namefield.getText().length()==0)
				|| ( descriptionfield.getText().length()==0)){
			submit.setEnabled(false);
		}
	}  

}

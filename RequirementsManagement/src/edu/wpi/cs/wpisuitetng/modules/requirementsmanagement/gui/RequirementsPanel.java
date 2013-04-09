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
import java.util.Date;
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.ProjectEventsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleRequirementCallback;
//import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ListRequirementsPanel.ListProjectEvents;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementPriority;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementType;

/**
 *
 * Panel for editing/creating a requirement
 * @author Josh
 *
 */
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
//	RequirementStatus[] statusStrings = RequirementStatus.values();
	public JTextField statusfield = new JTextField();//(statusStrings);
	public JTextField estimateField = new JTextField("0", 35);
	public JTextField actualEffortField = new JTextField("0", 35);
	public JTextField results = new JTextField(35);
	JButton submit = new JButton("Submit");
	private NoteMainPanel nt;
	private RequirementHistoryTab hs;
	private RequirementSubrequirementTab subs;
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

	/**
	 *update the list of iterations
	 *
	 */
	private void updateIterationList() {
		DB.getAllIterations(new UpdateIterationListCallback());
	}
	
	/**
	 *update the iteration dropdown menu
	 *
	 */
	private void updateIterationComboBox() {
		DefaultComboBoxModel comboboxModel = new DefaultComboBoxModel();
		for(Iteration it : iterations) {
			if (it == null) {
				comboboxModel.addElement("Backlog");
			} else if (it.getEndDate().after(new Date())) {
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
	 * Constructs a RequirementPanel for creating or editing a given Requirement.
	 * 
	 * @param parent	The parent RequirmenetView.
	 * @param requirement	The Requirement to edit.
	 * @param mode		Whether or not the given Requirement should be treated as if it already exists 
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
		
		// Populate the form with the contents of the Requirement model and update the TextUpdateListeners.
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
//		if(this.editMode == Mode.CREATE) {
//			estimateField.setEnabled(false);
//			actualEffortField.setEnabled(false);
//		} else {
//			estimateField.setEnabled(true);
//			actualEffortField.setEnabled(true);
//		}
	
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
		hs = new RequirementHistoryTab(this);
		subs = new RequirementSubrequirementTab(this);
		supplementPane.add("Notes", nt);
		supplementPane.add("History", hs);
		supplementPane.add("Sub-Requirements", subs);
		if(this.editMode == Mode.CREATE) {
			nt.setInputEnabled(false);
		} else {
			nt.setInputEnabled(true);
		}
		
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
		leftside.setMinimumSize(new Dimension(500,700));
		leftScrollPane.setMinimumSize(new Dimension(500,700));
		supplementPane.setMinimumSize(new Dimension(500,700));
		
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
	 * Updates the RequirementPanel's model to contain the values of the given Requirement and sets the 
	 * RequirementPanel's editMode to {@link Mode#EDIT}.
	 * 
	 */
	public void refreshModel() {
		updateModel(model, Mode.EDIT);
	}
	
	/**
	 * Updates the RequirementPanel's model to contain the values of the given Requirement and sets the 
	 * RequirementPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param requirement	The Requirement which contains the new values for the model.
	 */
	public void updateModel(RequirementModel requirement) {
		updateModel(requirement, Mode.EDIT);
	}

	/**
	 * Updates the RequirementPanel's model to contain the values of the given Requirement.
	 * 
	 * @param	requirement	The Requirement which contains the new values for the model.
	 * @param	mode	The new editMode.
	 */
	protected void updateModel(RequirementModel requirement, Mode mode) {
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
//		for(int i = 0; i < statusfield.getItemCount(); i++) {  // This is really round about, but it didn't seem to work comparing RequirementStatuses
//			if(model.getStatus() == RequirementStatus.valueOf(statusfield.getItemAt(i).toString())) {
//				statusfield.setSelectedIndex(i);
//			}
//		}
		statusfield.setText(model.getStatus().toString());
	}
	
	/**
	 * Updates the RequirementsPanel's fields to match those in the current model
	 *
	 */
	private void updateFields() {
		namefield.addKeyListener(this);
		descriptionfield.addKeyListener(this);
		if(model.getIteration() != null && !(model.getStatus() == RequirementStatus.COMPLETE || model.getStatus() == RequirementStatus.DELETED)) {
			model.setStatus(RequirementStatus.IN_PROGRESS);
		} else if(model.getIteration() == null && model.getStatus() == RequirementStatus.IN_PROGRESS) {
			model.setStatus(RequirementStatus.OPEN);
		}
		namefield.setText(model.getName());
		descriptionfield.setText(model.getDescription());
//		statusfield.setText(model.getStatus().toString());
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
				String modelItStr = model.getIteration().getIterationNumber().toString();
				for(int i = 0; i < iteration.getItemCount(); i++) {  // Same as above
					if(modelItStr.equals(iteration.getItemAt(i).toString())) {
						iteration.setSelectedIndex(i);
					}
				}
			}
		}
		estimateField.setText(model.getEstimate()+"");
		actualEffortField.setText(model.getActualEffort()+"");
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
		
		if(editMode == Mode.CREATE) {
			namefield.setEnabled(true);
			type.setEnabled(true);
			type.setBackground(Color.WHITE);
			priority.setEnabled(true);
			priority.setBackground(Color.WHITE);
			iteration.setEnabled(true);
			iteration.setBackground(Color.WHITE);
			descriptionfield.setEnabled(true);
			estimateField.setEnabled(false);
			actualEffortField.setEnabled(false);
			submit.setEnabled(!(namefield.getText().length() < 1 || descriptionfield.getText().length() < 1));
		} else if (model.getStatus().equals(RequirementStatus.COMPLETE)) {
			namefield.setEnabled(false);
			type.setEnabled(false);
			type.setBackground(Color.WHITE);
			priority.setEnabled(false);
			priority.setBackground(Color.WHITE);
			iteration.setEnabled(false);
			iteration.setBackground(Color.WHITE);
			descriptionfield.setEnabled(false);
			estimateField.setEnabled(false);
			actualEffortField.setEnabled(true);
			submit.setEnabled(true);
		} else if (model.getStatus().equals(RequirementStatus.DELETED)) {
			namefield.setEnabled(false);
			type.setEnabled(false);
			type.setBackground(Color.WHITE);
			priority.setEnabled(false);
			priority.setBackground(Color.WHITE);
			iteration.setEnabled(false);
			iteration.setBackground(Color.WHITE);
			descriptionfield.setEnabled(false);
			descriptionfield.setEnabled(false);
			estimateField.setEnabled(false);
			actualEffortField.setEnabled(false);
			submit.setEnabled(false);
		} else {
			namefield.setEnabled(true);
			type.setEnabled(true);
			type.setBackground(Color.WHITE);
			priority.setEnabled(true);
			priority.setBackground(Color.WHITE);
			iteration.setEnabled(true);
			iteration.setBackground(Color.WHITE);
			descriptionfield.setEnabled(true);
			estimateField.setEnabled(true);
			actualEffortField.setEnabled(false);
			submit.setEnabled(!(namefield.getText().length() < 1 || descriptionfield.getText().length() < 1));
		}
		System.out.println("namefield: "+namefield.getText());
		System.out.println("submit good: "+!(namefield.getText().length() < 1 || descriptionfield.getText().length() < 1));
		nt.setNotes(Arrays.asList(model.getNotes()));
		DB.getAllProjectEvents(new ListProjectEvents());
		updateSubmitButton();
		subs.update();
	}
	
	/**
	 *updates the submit button
	 *
	 */
	public void updateSubmitButton() {
		submit.setEnabled(!model.getStatus().equals(RequirementStatus.DELETED) && !(namefield.getText().length() < 1 || descriptionfield.getText().length() < 1));
	}
	
	/**
	 * Sets the transaction log in the history tab
	 */
	public void setHistory(List<ProjectEvent> projectEvents) {
		hs.setNotes(projectEvents);
	}

	/**
	 * Returns a boolean representing whether or not input is enabled for the RequirementPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the RequirementPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

	/**
	 * Gets the RequirementPanel's internal model.
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
				if(it != null && it.getIterationNumber().equals(selected)) {
					model.setIteration(it);
				}
			}
		}
		model.setDescription(descriptionfield.getText());
//		model.setStatus((RequirementStatus) statusfield.getSelectedItem());
		if (model.getStatus() == RequirementStatus.COMPLETE) {
			
		} else if (model.getStatus() == RequirementStatus.DELETED) {
			
		} else if (model.getIteration() != null && (model.getStatus() == RequirementStatus.OPEN || model.getStatus() == RequirementStatus.NEW)) {
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
	 * Returns the parent RequirementView.
	 * 
	 * @return the parent RequirementView.
	 */
	public RequirementsTab getParent() {
		return parent;
	}

	/**
	 * Returns the edit {@link Mode} for this RequirementPanel.
	 * 
	 * @return	The edit {@link Mode} for this RequirementPanel.
	 */
	public Mode getEditMode() {
		return editMode;
	}

	/**
	 *set the status
	 *
	 * @param string the status
	 */
	public void setStatus(String string) {
		results.setText(string);
	}
	
	/**
	 *
	 * action to edit the requirement
	 * @author TODO
	 *
	 */
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
				updateModel(model);
			}}
	}
	
	/**
	 *
	 * Callback to update the list of iterations
	 * @author TODO
	 *
	 */
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

	/**
	 * Checks to see if the fields are valid
	 *
	 * @return if the fields are valid
	 */
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
			System.out.println("Actual Effort value is " + actualEffortField.getText());
			return false;
		} else {
			actualEffortField.setBackground(Color.WHITE);
		}
		System.out.println("Estimate value is " + estimateField.getText());

		return true;
	}
	/**
	 *
	 * List the history of the requirement
	 * @author TODO
	 *
	 */
	class ListProjectEvents implements ProjectEventsCallback {
		
		@Override
		public void callback(List<ProjectEvent> projectEvents) {
			for(ProjectEvent event : projectEvents) {
				System.out.println("project event: "+event.toJSON());
			}
			setHistory(projectEvents);
		}


		
	}
	
	/**
	 * checked for input from keyboard
	 *
	 * @param e a key event
	 */
	public void keyTyped ( KeyEvent e ){  
		//check to see if name and description fields are empty or not
//		if(namefield.getText().length() != 0 && descriptionfield.getText().length() != 0){
//			submit.setEnabled(true);
//		}
//		if((namefield.getText().length()==0)
//				|| ( descriptionfield.getText().length()==0)){
//			submit.setEnabled(false);
//		}
		updateSubmitButton();
	}
	/**
	 * check if key is pressed. Doesn't really do anything now, but needs to be included 
	 *
	 * @param e a key event
	 */
	public void keyPressed ( KeyEvent e){  

	}  
	/**
	 * check if key is released. Doesn't really do anything now, but needs to be included 
	 *
	 * @param e a key event
	 */
	public void keyReleased ( KeyEvent e ){  
		//l1.setText( "Key Released" ) ; 
		updateSubmitButton();
	}  

	public void addToParent(int parentId) {
		DB.getSingleRequirement(parentId+"", new SingleRequirementCallback() {
			@Override
			public void callback(RequirementModel req) {
				req.getSubRequirements().add(model.getId()+"");
				DB.updateRequirements(req, new SingleRequirementCallback() {
					@Override
					public void callback(RequirementModel req) {
						if (req.getSubRequirements().contains(model.getId()+"")) {
							setStatus("added to parent");
						} else {
							setStatus("failed to add to parent");
						}
						
					}
				});
			}
    	});
	}
	
	public void addChild(int childId) {
		DB.getSingleRequirement(childId+"", new SingleRequirementCallback() {
			@Override
			public void callback(RequirementModel child) {
				model.getSubRequirements().add(child.getId()+"");
				DB.updateRequirements(model, new AddChildRequirementCallback(child));

			}
    	});
	}
	
	
	public void addChild(RequirementModel child) {
		model.getSubRequirements().add(child.getId()+"");
		DB.updateRequirements(model, new AddChildRequirementCallback(child));
	}
	
	class AddChildRequirementCallback implements SingleRequirementCallback {
		private RequirementModel childReq;
		
		public AddChildRequirementCallback(RequirementModel childReq) {
			this.childReq = childReq;
		}
		
		@Override
		public void callback(RequirementModel currentReq) {
			boolean added = false;
			for (String subReq : currentReq.getSubRequirements()) {
				if(subReq.equals(childReq.getId()+"")) {
					added = true;
				}
			}
			if (added) {
				subs.update();
				setStatus("added child");
			} else {
				setStatus("failed to add child");
			}
		}
	}
}

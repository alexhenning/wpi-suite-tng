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
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 *
 * The view for creating an iteration
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class ViewSingleIterationPanel extends JPanel{
	
	/** the model to hold the iteration */
	Iteration model;
	/** the model to hold the edited iteration */
	Iteration editModel;
	/** the tab that created this panel */
	ViewSingleIterationTab parent;
	/** labels to describe the text fields */
	JLabel lbl1, lbl2, lbl3;
	/** text fields for the iteration's data to be entered into */
	JTextField iterationNumber;
	/** text field that displays if the iteration was saved or not */
	JTextField result;
	/** button to submit the iteration */
	JButton submit;
	/**top panel*/
	JPanel topPanel;
	/**bottom panel*/
	ListFilteredRequirementsPanel bottomPanel;
	
	JXDatePicker startDatePicker;
	JXDatePicker endDatePicker;

	/** An enum indicating if the form is in create mode or edit mode */
	protected Mode editMode;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	/**
	 * Constructor
	 * @param iterationTab the tab that created this panel
	 */
	public ViewSingleIterationPanel(ViewSingleIterationTab iterationTab){
		this.parent = iterationTab;
		model = parent.iteration;
		editModel = null;
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
//		parent.buttonGroup.update(mode, model);

		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		updateFields();
	}

	
	/**
	 * add all the components (labels, text fields, buttons) to this panel's view
	 *
	 */
	private void addComponents(){
		setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		topPanel = new JPanel(new GridBagLayout());
		
		lbl1 = new JLabel("Start Date");
		lbl2 = new JLabel("End Date");
		lbl3 = new JLabel ("Iteration");
		
		Date startDate = model.getStartDate();
		Date endDate = model.getEndDate();
		Date currentDate = new Date();
		startDatePicker = new JXDatePicker(startDate != null ? startDate : currentDate);
		endDatePicker = new JXDatePicker(endDate != null ? endDate : currentDate);
		
		result = new JTextField();
		iterationNumber = new JTextField(model.getIterationNumber());
		
		submit = new JButton("Update");
		submit.addActionListener(new EditIterationAction());
		
		if(startDate.before(currentDate)){
			startDatePicker.setEnabled(false);
		}
		if(endDate.before(currentDate)){
			endDatePicker.setEnabled(false);
			submit.setEnabled(false);
			iterationNumber.setEnabled(false);
		}
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		topPanel.add(lbl3, c);
		c.gridx = 1;
		topPanel.add(iterationNumber, c);
		c.gridy = 1;
		c.gridx = 0;
		topPanel.add(lbl2, c);
		c.gridx = 1;
		topPanel.add(endDatePicker, c);
		c.gridy = 2;
		c.gridx = 0;
		topPanel.add(lbl1, c);
		c.gridx = 1;
		topPanel.add(startDatePicker, c);
		c.gridy = 3;
		topPanel.add(submit, c);
		c.gridy = 4;
		topPanel.add(result, c);
		c.gridy = 5;
		add(topPanel, BorderLayout.PAGE_START);
		
		/** adding bottom panel */
		bottomPanel = new ListFilteredRequirementsPanel(parent);
		add(bottomPanel, BorderLayout.CENTER);
		
		result.setEditable(false);
	}
	
	/**
	 * close this tab/panel
	 *
	 */
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
	 * Check if dates overlap
	 * TODO: IS THIS WHAT WE REALLY WANT THIS TO DO? DO WE WE EVEN USE THIS?
	 *
	 * @return if dates overlap
	 */
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
		updateModel(model, Mode.EDIT);
	}
	
	/**
	 * Updates the IterationPanel's model to contain the values of the given Iteration and sets the 
	 * IterationPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param iteration	The Iteration which contains the new values for the model.
	 */
	public void updateModel(Iteration iteration) {
		updateModel(iteration, Mode.EDIT);
	}

	/**
	 * Updates the IterationPanel's model to contain the values of the given Iteration.
	 * 
	 * @param	iteration	The Iteration which contains the new values for the model.
	 * @param	mode	The new editMode.
	 */
	protected void updateModel(Iteration iteration, Mode mode) {
		editMode = mode;
		editModel = iteration;
		updateFields();	
	}
	
	/**
	 * Updates the IterationPanel's fields to match those in the current model
	 *
	 */
	private void updateFields() {
		//TODO finish this
	}

	/**
	 * Returns a boolean representing whether or not input is enabled for the IterationPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the IterationPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

	/**
	 * Gets the IterationPanel's internal model.
	 * @return the iteration model
	 */
	public Iteration getModel() {
		model.setIterationNumber(iterationNumber.getText());

		//TODO handle the exceptions better
		model.setStartDate(startDatePicker.getDate());
		model.setEndDate(endDatePicker.getDate());
		
		return model;
	}
	
	/**
	 * Returns the parent IterationTab.
	 * 
	 * @return the parent IterationTab.
	 */
	public ViewSingleIterationTab getParent() {
		return parent;
	}

	/**
	 * Returns the edit {@link Mode} for this IterationPanel.
	 * 
	 * @return	The edit {@link Mode} for this IterationPanel.
	 */
	public Mode getEditMode() {
		return editMode;
	}

	public void setStatus(String string) {
		result.setText(string);
	}
	
	class EditIterationAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DB.updateIteration(getUpdatedModel(), new SingleIterationCallback() {
				@Override
				public void callback(Iteration iteration) {
					setStatus("Iteration Updated");
				}
			});
		}
	}
	
	
	/**
	 *
	 * callback class to update the iteration's id
	 * @author TODO
	 *
	 */
	class UpdateIterationIdCallback implements IterationCallback {
		@Override
		public void callback(List<Iteration> iterationList) {
			model.setId(iterationList.size()+1);
		}
	}

	/**
	 *
	 * Callback class to make sure that the iterations dates don't overlap with other dates
	 * @author TODO
	 *
	 */
	class CheckDateOvelapCallback implements IterationCallback {
		@Override
		public void callback(List<Iteration> iterationList) {
			List<ValidationIssue> issues = new ArrayList<ValidationIssue>();
			for (Iteration i : iterationList) {
				if(i.getId() != model.getId()) {
					if(model.getStartDate().after(i.getStartDate()) && model.getStartDate().before(i.getEndDate())) {
						issues.add(new ValidationIssue("startDate overlaps with Iteration "+i.getIterationNumber(), "startDate"));
					}
					if(model.getEndDate().after(i.getStartDate()) && model.getEndDate().before(i.getEndDate())) {
						issues.add(new ValidationIssue("endDate overlaps with Iteration "+i.getIterationNumber(), "endDate"));
					}
					if(i.getStartDate().after(model.getStartDate()) && model.getStartDate().before(i.getEndDate()) ||
							i.getEndDate().after(model.getStartDate()) && model.getEndDate().before(i.getEndDate())) {
						issues.add(new ValidationIssue("iteration overlaps with Iteration "+i.getIterationNumber()));
					}
				}
			}
			//TODO figure out how to display the issues...

		}
	}
	
	public Iteration getUpdatedModel() {
		
		editModel = new Iteration();
		editModel.setId(model.getId());
		String iterNumber = iterationNumber.getText();
		Date start = startDatePicker.getDate();
		Date end = endDatePicker.getDate();
		editModel.setIterationNumber(iterNumber);
		editModel.setStartDate(start);
		editModel.setEndDate(end);
		
		return editModel;
	}

}

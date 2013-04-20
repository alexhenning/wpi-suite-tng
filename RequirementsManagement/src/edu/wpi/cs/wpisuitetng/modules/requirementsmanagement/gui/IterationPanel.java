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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollablePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 *
 * The view for creating an iteration
 * @author Josh
 *
 */
@SuppressWarnings("serial")
public class IterationPanel extends JPanel implements ScrollablePanel {
	
	/** the model to hold the iteration */
	Iteration model;
	
	/** the tab that created this panel */
	ScrollableTab parent;
	/** the layout for this panel */
	private GridBagLayout panelLayout;
	/** labels to describe the text fields */
	JLabel lbl1, lbl2, lbl3;
	/** text fields for the iteration's data to be entered into */
	JTextField iterationNumber;
	/** text field that displays if the iteration was saved or not */
	JTextField result;
	/** button to submit the iteration */
	JButton submit;
	
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
	public IterationPanel(Iteration iteration){
		model = iteration;
		
		editMode = Mode.CREATE;
		
		if(editMode == Mode.CREATE){
			DB.getAllIterations(new UpdateIterationIdCallback());
		}
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		updateFields();
	}

	@Override
	public void setTab(ScrollableTab tab) {
		parent = tab;
	}

	
	/**
	 * add all the components (labels, text fields, buttons) to this panel's view
	 *
	 */
	private void addComponents(){
		panelLayout =new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(panelLayout);
		
		lbl1 = new JLabel("Start Date (mm/dd/yyyy)");
		lbl2 = new JLabel("End Date (mm/dd/yyyy)");
		lbl3 = new JLabel ("Iteration Number");
		
		Date startDate = model.getStartDate();
		Date endDate = model.getEndDate();
		startDatePicker = new JXDatePicker(startDate != null ? startDate : new Date());
		endDatePicker = new JXDatePicker(endDate != null ? endDate : new Date());

		result = new JTextField(25);
		iterationNumber = new JTextField();
		
		if(editMode == Mode.CREATE) {
			submit = new JButton("Submit");
		} else {
			submit = new JButton("Update");
		}
		submit.addActionListener(new ValidateIterationActionListener());
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(lbl1, c);
		c.gridx = 1;
		add(startDatePicker, c);
		c.gridy = 1;
		c.gridx = 0;
		add(lbl2, c);
		c.gridx = 1;
		add(endDatePicker, c);
		c.gridy = 2;
		c.gridx = 0;
		add(lbl3, c);
		c.gridx = 1;
		add(iterationNumber, c);
		c.gridy = 3;
		add(submit, c);
		c.gridy = 4;
		add(result, c);
		
		result.setEditable(false);
	}
	
	/**
	 * close this tab/panel
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
		model = iteration;
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
	public ScrollableTab getParent() {
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
	
	public IterationPanel getThisIterationPanel() {
		return this;
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
	
	class ValidateIterationActionListener implements ActionListener {
		
		/**
		 * Validate the iteratin being created, if its valid send it to the DB
		 *
		 * @param e action that happened
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			getModel();
			DB.getAllIterations(new ValidateIterationCallback(e));
		}
		
	}

	/**
	 *
	 * Callback class to make sure that the iteration is valid
	 * @author TODO
	 *
	 */
	class ValidateIterationCallback implements IterationCallback {
		
		ActionEvent e;
		
		public ValidateIterationCallback(ActionEvent e) {
			this.e = e;
		}
		
		/**
		 * Callback function to validate the fields, if they are valid send it to the DB
		 *
		 * @param iterationList list of all iterations
		 */
		@Override
		public void callback(List<Iteration> iterationList) {
			boolean isValid = true;
			if(model.getIterationNumber() == null || model.getIterationNumber().length() == 0) {
				result.setText("An iteration must have a name");
				isValid = false;
			}
			else if(model.getStartDate() != null && model.getEndDate() != null && model.getStartDate().after(model.getEndDate())) {
				result.setText("The start date must be before the end date");
				isValid = false;
			}
			else {
				Date iterationStart = model.getStartDate();
				Calendar noTime = Calendar.getInstance();
				noTime.setTime(iterationStart);
				noTime.set(Calendar.HOUR_OF_DAY, 0);  
				noTime.set(Calendar.MINUTE, 0);  
				noTime.set(Calendar.SECOND, 0);  
				noTime.set(Calendar.MILLISECOND, 0);
				iterationStart = noTime.getTime();
				Date iterationEnd = model.getEndDate();
				noTime.setTime(iterationEnd);
				noTime.set(Calendar.HOUR_OF_DAY, 0);  
				noTime.set(Calendar.MINUTE, 0);  
				noTime.set(Calendar.SECOND, 0);  
				noTime.set(Calendar.MILLISECOND, 0);
				iterationEnd = noTime.getTime();
				for (Iteration i : iterationList) {
					if(i != null && i.getId() != model.getId()) {
						if(i.getIterationNumber().equals(model.getIterationNumber())) {
							result.setText("An iteration with this name already exists");
							isValid = false;
						}
						Date iStart = i.getStartDate();
						noTime.setTime(iStart);
						noTime.set(Calendar.HOUR_OF_DAY, 0);  
						noTime.set(Calendar.MINUTE, 0);  
						noTime.set(Calendar.SECOND, 0);  
						noTime.set(Calendar.MILLISECOND, 0);
						iStart = noTime.getTime();
						Date iEnd = i.getEndDate();
						noTime.setTime(iEnd);
						noTime.set(Calendar.HOUR_OF_DAY, 0);  
						noTime.set(Calendar.MINUTE, 0);  
						noTime.set(Calendar.SECOND, 0);  
						noTime.set(Calendar.MILLISECOND, 0);
						iEnd = noTime.getTime();
							if(iterationStart.after(iStart) && iterationStart.before(iEnd)) {
								result.setText("start date overlaps with Iteration "+i.getIterationNumber());
								isValid = false;
							}
							if(iterationEnd.after(iStart) && iterationEnd.before(iEnd)) {
								result.setText("end date overlaps with Iteration "+i.getIterationNumber());
								isValid = false;
							}
							if((iStart.after(iterationStart) && iStart.before(iterationEnd)) ||
									(iEnd.after(iterationStart) && iEnd.before(iterationEnd)) ||
									(iStart.equals(iterationStart) || iEnd.equals(iterationEnd))) {
								result.setText("iteration overlaps with Iteration "+i.getIterationNumber());
								isValid = false;
							}
					}
				}
				if(isValid) {
					// if there's no problems, add the iteration
					new AddIterationController(getThisIterationPanel()).actionPerformed(e);
				}
			}
		}
	}
}

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
 *    vpatara
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXDatePicker;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.CurrentUserPermissionManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SingleIterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollablePanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollableTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

/**
 * The view for creating an iteration
 *
 * @author Josh
 * @author vpatara
 */
@SuppressWarnings("serial")
public class ViewSingleIterationPanel extends JPanel implements ScrollablePanel {
	
	/** the model to hold the iteration */
	Iteration model;
	/** the model to hold the edited iteration */
	Iteration editModel;
	/** the tab that created this panel */
	ScrollableTab parent;
	/** labels to describe the text fields */
	JLabel lbl1, lbl2, lbl3, lbl4, lbl5;
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
	/** text field that holds the iteration's estimate value */
	JTextField estimate;
	
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
	public ViewSingleIterationPanel(Iteration iteration) {
		model = iteration;
		editModel = null;
		
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
		setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		topPanel = new JPanel(new GridBagLayout());
		
		/** iteration is null, this is the backlog */
		if (model == null) {
			iterationNumber = new JTextField("Backlog");
			Font font = new Font("Verdana", Font.BOLD, 25);
			iterationNumber.setFont(font);
			iterationNumber.setEditable(false);
			topPanel.add(iterationNumber, c);
		}
		/** iteration is not null, retrieve and display iteration values */
		else {
			lbl1 = new JLabel("Start Date");
			lbl2 = new JLabel("End Date");
			lbl3 = new JLabel ("Iteration Number ");
			lbl4 = new JLabel ("Estimate");
			lbl5 = new JLabel("Scheduled Requirements:");
			
			Date startDate = model.getStartDate();
			Date endDate = model.getEndDate();
			Date currentDate = new Date();
			startDatePicker = new JXDatePicker(startDate != null ? startDate : currentDate);
			endDatePicker = new JXDatePicker(endDate != null ? endDate : currentDate);
			
			result = new JTextField(25);
			
			iterationNumber = new JTextField(model.getIterationNumber());
			estimate = new JTextField();
			estimate.setEditable(false);

			DB.getSingleIteration("" + model.getId(), new SetEstimateFieldCallback());
			
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

			// Allow access to users with certain permission levels
			// The username info should be ready, so use the non-blocking version
			switch (CurrentUserPermissionManager.getInstance().getCurrentProfile().getPermissionLevel()) {
			case NONE:
				// "None" can't do anything
				startDatePicker.setEnabled(false);
				endDatePicker.setEnabled(false);
				iterationNumber.setEnabled(false);
				submit.setVisible(false); // set invisible*
				result.setVisible(false);
				break;

			default:
				// Administrator can do everything
				break;
			}

			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			topPanel.add(lbl3, c);
			c.gridx = 1;
			topPanel.add(iterationNumber, c);
			c.gridy = 1;
			c.gridx = 0;
			topPanel.add(lbl4, c);
			c.gridx = 1;
			topPanel.add(estimate, c);
			c.gridy = 2;
			c.gridx = 0;
			topPanel.add(lbl1, c);
			c.gridx = 1;
			topPanel.add(startDatePicker, c);
			c.gridx  = 0;
			c.gridy = 3;
			topPanel.add(lbl2, c);
			c.gridx = 1;
			topPanel.add(endDatePicker, c);
			c.gridy = 4;
			topPanel.add(submit, c);
			c.gridy = 5;
			topPanel.add(result, c);
			c.gridy = 6;
			topPanel.add(lbl5, c);
			result.setEditable(false);
		}
		
		add(topPanel, BorderLayout.PAGE_START);
		
		/** adding bottom panel */
		bottomPanel = new ListFilteredRequirementsPanel(this);
		add(bottomPanel, BorderLayout.CENTER);
		updateIterReqs();
	}
	
	/**
	 * Updates requirement list
	 */
	public void updateIterReqs(){
		bottomPanel.updateRequirementList();
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
		updateFields();
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
	public void updateFields() {
		updateEstimate();
		updateIterReqs();
		updateName();
		updateDates();
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

	public void updateEstimate() {
		if (model != null) {
			DB.getAllRequirements(new RequirementsCallback() {

				@Override
				public void callback(List<RequirementModel> reqs) {
					// TODO Auto-generated method stub
					int estimate = 0;
					for(RequirementModel req : reqs) {
						Iteration it = req.getIteration();
						if (it != null && model != null && it.getIterationNumber().equals(model.getIterationNumber())) {
							estimate += req.getEstimate();
						}
					}
					setEstimate(estimate);
				}
			});
		}
	}

	public void updateName() {
		iterationNumber.setText((model != null) ? model.getIterationNumber() : "Backlog");
	}

	public void updateDates() {
		// For iterations other than the backlog (null)
		if(model != null) {
			startDatePicker.setDate(model.getStartDate());
			endDatePicker.setDate(model.getEndDate());
		}
	}

	public void setEstimate(int est) {
		estimate.setText(est + "");
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
	
	/**
	 * Action to make sure the changes to the iteration are valid, and then update it
	 * @author James
	 *
	 */
	class EditIterationAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			DB.getAllIterations(new ValidateSingleIterationUpdateCallback());
		}
	}
	
	/**
	 * Validate the changes to the iteration, if they are all valid update the iteration
	 * @author James
	 *
	 */
	class ValidateSingleIterationUpdateCallback implements IterationCallback {

		@Override
		public void callback(List<Iteration> iterations) {
			Iteration updateModel = getUpdatedModel();
			boolean isValid = true;
			if(updateModel.getIterationNumber() == null || updateModel.getIterationNumber().length() == 0) {
				setStatus("An iteration must have a name");
				isValid = false;
			}
			else if(updateModel.getStartDate() != null && updateModel.getEndDate() != null && updateModel.getStartDate().after(updateModel.getEndDate())) {
				setStatus("The start date must be before the end date");
				isValid = false;
			}
			else {
				Date iterationStart = updateModel.getStartDate();
				Calendar noTime = Calendar.getInstance();
				noTime.setTime(iterationStart);
				noTime.set(Calendar.HOUR_OF_DAY, 0);  
				noTime.set(Calendar.MINUTE, 0);  
				noTime.set(Calendar.SECOND, 0);  
				noTime.set(Calendar.MILLISECOND, 0);
				iterationStart = noTime.getTime();
				Date iterationEnd = updateModel.getEndDate();
				noTime.setTime(iterationEnd);
				noTime.set(Calendar.HOUR_OF_DAY, 0);  
				noTime.set(Calendar.MINUTE, 0);  
				noTime.set(Calendar.SECOND, 0);  
				noTime.set(Calendar.MILLISECOND, 0);
				iterationEnd = noTime.getTime();
				for (Iteration i : iterations) {
					if(i != null && i.getId() != updateModel.getId()) {
						if(i.getIterationNumber().equals(updateModel.getIterationNumber())) {
							setStatus("An iteration with this name already exists");
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
							setStatus("start date overlaps with Iteration "+i.getIterationNumber());
							isValid = false;
						}
						if(iterationEnd.after(iStart) && iterationEnd.before(iEnd)) {
							setStatus("end date overlaps with Iteration "+i.getIterationNumber());
							isValid = false;
						}
						if((iStart.after(iterationStart) && iStart.before(iterationEnd)) ||
								(iEnd.after(iterationStart) && iEnd.before(iterationEnd)) ||
								(iStart.equals(iterationStart) || iEnd.equals(iterationEnd))) {
							setStatus("iteration overlaps with Iteration "+i.getIterationNumber());
							isValid = false;
						}
					}
				}
				if(isValid) {
					// if there's no problems, update the iteration
					DB.updateIteration(updateModel, new SingleIterationCallback() {

						@Override
						public void callback(Iteration iteration) {
							setStatus("Iteation updated");
						}
						
					});
				}
			}

		}
	}
	

	/**
	 * Update the estimate text field to display the iterations estimate
	 *
	 * @author James
	 *
	 */
	class SetEstimateFieldCallback implements SingleIterationCallback {

		/**
		 * callback function to call a callback to calculate and set the estimate field
		 *
		 * @param iteration the iteration
		 */
		@Override
		public void callback(Iteration iteration) {
			DB.getAllRequirements(new SetIterationEstimateFieldCallback(iteration));
		}
	}

	/**
	 * Update the estimate text field to display the iterations estimate
	 *
	 * @author James
	 *
	 */
	class SetIterationEstimateFieldCallback implements RequirementsCallback {

		/** the iteration that the estimate needs to be know for */
		Iteration iteration;

		/**
		 * Constructor
		 * @param estimateField the estimate field
		 * @param iteration the iteration
		 */
		SetIterationEstimateFieldCallback(Iteration iteration) {
			this.iteration = iteration;
		}

		/**
		 * a callback function to set the estimate field
		 *
		 * @param reqs all the requirements
		 */
		@Override
		public void callback(List<RequirementModel> reqs) {
			int estimateAmount = 0;
			if (iteration != null) {
				for(RequirementModel req : reqs) {
					// If the iteration is the same as the requirment's iteration
					if (req.getIteration() != null &&
							req.getIteration().getIterationNumber().equals(iteration.getIterationNumber())) {
						// Add the requirment's estimate to the iteration's estimate
						estimateAmount += req.getEstimate();
					}
				}
			}
			estimate.setText("" + estimateAmount);
		}
	}
}

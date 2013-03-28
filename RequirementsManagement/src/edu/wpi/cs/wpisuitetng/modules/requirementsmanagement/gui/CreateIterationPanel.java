package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;

@SuppressWarnings("serial")
public class CreateIterationPanel extends JPanel{
	
	Iteration model;
	
	IterationTab parent;
	private GridBagLayout panelLayout;
	JLabel lbl1, lbl2, lbl3;
	JTextField startDate, endDate, iterationNumber;
	JButton submit;

	/** An enum indicating if the form is in create mode or edit mode */
	protected Mode editMode;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	public CreateIterationPanel(IterationTab iterationTab){
		this.parent = iterationTab;
		model = parent.iteration;
		
		editMode = Mode.CREATE;
		
		if(editMode == Mode.CREATE){
			DB.getAllIterations(new UpdateIterationIdCallback());
		}
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
//		parent.buttonGroup.update(mode, model);

		// TODO: prevent tab key from inserting tab characters into the description field
		
		// Populate the form with the contents of the Defect model and update the TextUpdateListeners.
		updateFields();
	}

	
	private void addComponents(){
		panelLayout =new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(panelLayout);
		
		lbl1 = new JLabel("Start Date (mm/dd/yyyy)");
		lbl2 = new JLabel("End Date (mm/dd/yyyy)");
		lbl3 = new JLabel ("Iteration Number");
		
		startDate = new JTextField();
		endDate = new JTextField();
		iterationNumber = new JTextField();
		
		if(editMode == Mode.CREATE) {
			submit = new JButton("Submit");
		} else {
			submit = new JButton("Update");
		}
		submit.addActionListener(new AddIterationController(this));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(lbl1, c);
		c.gridx = 1;
		add(startDate, c);
		c.gridy = 1;
		c.gridx = 0;
		add(lbl2, c);
		c.gridx = 1;
		add(endDate, c);
		c.gridy = 2;
		c.gridx = 0;
		add(lbl3, c);
		c.gridx = 1;
		add(iterationNumber, c);
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
		//TODO finidh this
//		namefield.setText(model.getName());
//		descriptionfield.setText(model.getDescription());
//		for(int i = 0; i < statusfield.getItemCount(); i++) {  // This is really round about, but it didn't seem to work comparing RequirementStatuses
//			if(model.getStatus() == RequirementStatus.valueOf(statusfield.getItemAt(i).toString())) {
//				statusfield.setSelectedIndex(i);
//			}
//		}
//		for(int i = 0; i < priority.getItemCount(); i++) {  // Same as above
//			if(model.getPriority() == RequirementPriority.valueOf(priority.getItemAt(i).toString())) {
//				priority.setSelectedIndex(i);
//			}
//		}
//		for(int i = 0; i < type.getItemCount(); i++) {  // Same as above
//			if(model.getType() == RequirementType.valueOf(type.getItemAt(i).toString())) {
//				type.setSelectedIndex(i);
//			}
//		}
//		if (iteration.getItemCount() >= 1){
//			iteration.setSelectedIndex(0);
//			for(int i = 0; i < iteration.getItemCount(); i++) {  // Same as above
//				if(new Integer(model.getIteration().getIterationNumber()).toString().equals(iteration.getItemAt(i).toString())) {
//					iteration.setSelectedIndex(i);
//				}
//			}
//		}
//		estimateField.setText(model.getEstimate());
//		if(this.editMode == Mode.CREATE) {
//			estimateField.setEditable(false);
//		} else {
//			estimateField.setEditable(true);
//		}
//		if(this.editMode == Mode.CREATE) { 
//			submit.setAction(new AddRequirementController(this));
//			submit.setText("Save");
//		} else {
//			submit.setAction(new EditRequirementAction());
//			submit.setText("Update");
//		}
//		
//		if (editMode.equals(Mode.EDIT)) {
//			parent.setEditModeDescriptors(model);
//		}
//		parent.buttonGroup.update(editMode, model);
//		
//		if (editMode.equals(Mode.EDIT) && (model.getStatus().equals(RequirementStatus.COMPLETE)
//				|| model.getStatus().equals(RequirementStatus.COMPLETE))) {
//			namefield.setEnabled(false);
//			type.setEnabled(false);
//			priority.setEnabled(false);
//			descriptionfield.setEnabled(false);
//			estimateField.setEnabled(false);
//		} else {
//			namefield.setEnabled(true);
//			type.setEnabled(true);
//			priority.setEnabled(true);
//			descriptionfield.setEnabled(true);
//			estimateField.setEnabled(true);
//		}
//		
//		nt.setNotes(Arrays.asList(model.getNotes()));
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
	public Iteration getModel() {
		System.out.println("getting model from panel");
		//TODO finish this
		model.setIterationNumber(new Integer(iterationNumber.getText()));

		try {
			Date start = new SimpleDateFormat("MM/d/yyyy", Locale.ENGLISH).parse(startDate.getText());
			model.setStartDate(start);
//			System.out.println("Start: " + startDate.getText());
//			System.out.println("start:"+start.toLocaleString());
		} catch (ParseException e) {
			e.printStackTrace();
//			String[] startDateString = startDate.getText().split("/");
//			int startMonth = new Integer(startDateString[0]);
//			int startDay = new Integer(startDateString[1]);
//			int startYear = new Integer(startDateString[2]);
//			model.setStartDate(new Date(startYear-1900, startMonth-1, startDay-1));
		}
		try {
			Date end = new SimpleDateFormat("MM/d/yyyy", Locale.ENGLISH).parse(endDate.getText());
			model.setEndDate(end);
//			System.out.println("End: " + endDate.getText());
//			System.out.println("end:"+end.toLocaleString());
		} catch (ParseException e) {
			e.printStackTrace();
//			String[] endDateString = endDate.getText().split("/");
//			int endMonth = new Integer(endDateString[0]);
//			int endDay = new Integer(endDateString[1]);
//			int endYear = new Integer(endDateString[2]);
//			model.setEndDate(new Date(endYear-1900, endMonth-1, endDay-1));
		}
		
//		model.setName(namefield.getText());
//		model.setType((RequirementType) type.getSelectedItem());
//		model.setPriority((RequirementPriority) priority.getSelectedItem());
//		//TODO set the iteration
//		model.setDescription(descriptionfield.getText());
//		model.setStatus((RequirementStatus) statusfield.getSelectedItem());
//		model.setEstimate(estimateField.getText()); // TODO: Should be an integer
		return model;
	}
	
	/**
	 * Returns the parent IterationTab.
	 * 
	 * @return the parent IterationTab.
	 */
	public IterationTab getParent() {
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

//	public void setStatus(String string) {
//		results.setText(string);
//	}
	
//	class EditRequirementAction extends AbstractAction {
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			DB.updateIteration(model, new SingleIterationCallback() {
//				@Override
//				public void callback(Iteration iteration) {
//					setStatus("Iteration Updated");
//				}
//			});
//		}
//	}
	
	
	class UpdateIterationIdCallback implements IterationCallback {
		@Override
		public void callback(List<Iteration> iterationList) {
			model.setId(iterationList.size()+1);
		}
		
	}

}

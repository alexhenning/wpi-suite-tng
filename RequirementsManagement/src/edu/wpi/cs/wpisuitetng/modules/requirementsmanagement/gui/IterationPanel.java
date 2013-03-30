package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.validators.ValidationIssue;

@SuppressWarnings("serial")
public class IterationPanel extends JPanel{
	
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

	public IterationPanel(IterationTab iterationTab){
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

		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
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
//		System.out.println("getting model from panel");
		model.setIterationNumber(iterationNumber.getText());

		try {
			Date start = new SimpleDateFormat("MM/d/yyyy", Locale.ENGLISH).parse(startDate.getText());
			model.setStartDate(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			Date end = new SimpleDateFormat("MM/d/yyyy", Locale.ENGLISH).parse(endDate.getText());
			model.setEndDate(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
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

}

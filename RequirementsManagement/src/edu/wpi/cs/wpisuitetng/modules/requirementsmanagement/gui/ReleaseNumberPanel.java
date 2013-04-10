/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddReleaseNumberController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.ReleaseNumberCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.SingleReleaseNumberCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

/**
 *
 * The view for creating and editing ReleaseNumbers
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class ReleaseNumberPanel extends JPanel {
	
	/** the model that holds the ReleaseNumber */
	ReleaseNumber model;
	/** the tab that created this panel */
	ReleaseNumberTab parent;
	/** layout for this panel */
	private GridBagLayout panelLayout;
	/** combo box holding all release numbers */
	JComboBox releaseNumbersComboBox;
	/** field representing the name of the release number */
	JTextField numberField;
	/** text field displaying whether the release number was saved or not */
	JTextField result;
	/** button to submit for creation or update */
	JButton submit;
	/** an enum indicating if the form is in create mode or edit mode */
	protected Mode editMode;
	/** a flag indicating if input is enable on the form */
	protected boolean inputEnabled;
	/** a label for combo box */
	JLabel cbLabel;
	/** a label for field */
	JLabel nfLabel;
	/** a boolean flag to indicate that the combo box item was selected by code, not the user */
	boolean autoSelected = false;
	
	/**
	 * @param releaseTab
	 */
	public ReleaseNumberPanel(ReleaseNumberTab releaseTab) {
		this.parent = releaseTab;
		model = parent.releaseNum;
		
		editMode = Mode.CREATE;
		
		inputEnabled = true;
		
		addComponents();
		
		updateReleaseNumbers();
		
		updateFields();
	}
	
	/**
	 * Add all the components to this panel's view
	 *
	 */
	private void addComponents() {
		panelLayout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		setLayout(panelLayout);
		
		releaseNumbersComboBox = new JComboBox();
		
		releaseNumbersComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("autoSelected is " + autoSelected);
				if(e.getStateChange() == ItemEvent.SELECTED && !autoSelected) {  // ignore deselects and auto selects
					DB.getAllReleaseNumbers(new ReleaseNumberCallback() {
						@Override
						public void callback(List<ReleaseNumber> releaseNumbers) {
							String releaseNumberName = (String) releaseNumbersComboBox.getSelectedItem();
							if(releaseNumberName.equals("New Release Number")) {
								updateModel(new ReleaseNumber(), Mode.CREATE);
							} else {
								for(ReleaseNumber rn : releaseNumbers) {
									if(rn.getReleaseNumber().equals(releaseNumberName)) {
										updateModel(rn);
										break;
									}
								}
							}
							result.setText("");
						}
					});
				}
				autoSelected = false;
			}
			
		});
		
		numberField = new JTextField();
		result = new JTextField();
		
		cbLabel = new JLabel("All Release Numbers       ");
		nfLabel = new JLabel("Release Number (Name)     ");
		
		submit = new JButton("Create");
		submit.setAction(new AddReleaseNumberController(this));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(cbLabel, c);
		c.gridx = 1;
		add(releaseNumbersComboBox, c);
		c.gridx = 0;
		c.gridy = 1;
		add(nfLabel, c);
		c.gridx = 1;
		add(numberField, c);
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 20;
		add(submit, c);
		c.ipady = 0;
		c.gridx = 1;
		add(result, c);
		
		result.setEditable(false);
	}
	
	/**
	 * close the tab/panel
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
	}
	
	/**
	 * Updates the ReleaseNumberPanel's model to contain the values of the given ReleaseNumber and sets the 
	 * ReleaseNumberPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * 
	 */
	public void refreshModel() {
		updateModel(model, Mode.EDIT);
	}
	
	/**
	 * Updates the ReleaseNumberPanel's model to contain the values of the given ReleaseNumber and sets the 
	 * ReleaseNumberPanel's editMode to {@link Mode#EDIT}.
	 * 
	 * @param releaseNumber	The ReleaseNumber which contains the new values for the model.
	 */
	public void updateModel(ReleaseNumber releaseNumber) {
		updateModel(releaseNumber, Mode.EDIT);
	}
	
	/**
	 * Updated the ReleaseNumberPanel's model to contain the values of the given model
	 *
	 * @param releaseNumber The ReleaseNumber which contains the new values for the model
	 * @param mode The new editMode
	 */
	protected void updateModel(ReleaseNumber releaseNumber, Mode mode) {
		editMode = mode;
		model = releaseNumber;
		updateFields();
	}
	
	/**
	 * Validates the fields
	 *
	 * @return if the fields are valid
	 */
	public boolean validateFields() {
		boolean noErrors = true;
		
		if(numberField.getText().length() < 1 || numberField.getText().length() > 100) {
			noErrors = false;
		}
		
		if(!noErrors) {
			numberField.setBackground(Color.RED);
		} else {
			numberField.setBackground(Color.WHITE);
		}
		
		return noErrors;
	}
	
	private void updateComboBoxWithReleaseNumbers(List<ReleaseNumber> releaseNumbers) {
		releaseNumbersComboBox.removeAllItems();
		releaseNumbersComboBox.addItem("New Release Number");
		List<String> rns = new ArrayList<String>();
		for(ReleaseNumber rn : releaseNumbers) {
			rns.add(rn.getReleaseNumber());
		}
		Collections.sort(rns);
		for(String s : rns) {
			releaseNumbersComboBox.addItem(s);
		}
	}
	
	/**
	 * Updates the fields to match those in the current model
	 *
	 */
	private void updateFields() {
		// set text for release number
		numberField.setText(model.getReleaseNumber());
		
		// set combo box to correct release number
		if(!autoSelected) {
			for(int i = 0; i < releaseNumbersComboBox.getItemCount(); ++i) {
				if(releaseNumbersComboBox.getItemAt(i).equals(model.getReleaseNumber())) {
					autoSelected = true;
					releaseNumbersComboBox.setSelectedIndex(i);
					break;
				}
			}
		}
		
		if(editMode == Mode.CREATE) {
			submit.setText("Create");
			submit.setAction(new AddReleaseNumberController(this));
		} else {
			submit.setText("Update");
			submit.setAction(new UpdateReleaseNumber());
		}
	}
	
	public ReleaseNumber getModel() {
		model.setReleaseNumber(numberField.getText());
		
		return model;
	}
	
	/**
	 * Returns the edit {@link Mode} for this ReleaseNumberPanel.
	 * 
	 * @return	The edit {@link Mode} for this ReleaseNumberPanel.
	 */
	public Mode getEditMode() {
		return editMode;
	}
	
	/**
	 * Sets some status message to the status box
	 *
	 * @param string Message to display
	 */
	public void setStatus(String string) {
		result.setText(string);
	}
	
	/**
	 * Retrieves all the release numbers from the data base and
	 * populates the combo box with them
	 *
	 */
	public void updateReleaseNumbers() {
		DB.getAllReleaseNumbers(new ReleaseNumberCallback() {
			@Override
			public void callback(List<ReleaseNumber> releaseNumbers) {
				updateComboBoxWithReleaseNumbers(releaseNumbers);
			}
		});
	}
	
	/**
	 *
	 * Updates a release number to the values currently displayed
	 * @author Tim
	 *
	 */
	class UpdateReleaseNumber extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(validateFields() && // no errors and the user actually changed something
					(!numberField.getText().equals(model.getReleaseNumber()))) {
				getModel();
				DB.updateReleaseNumber(model, new SingleReleaseNumberCallback() {
					@Override
					public void callback(ReleaseNumber rn) {
						setStatus("Release Number Updated");
						updateReleaseNumbers();
						updateModel(model);
					}
				});
			} else {
				if(model.getReleaseNumber().equals(model.getReleaseNumber())) {
					setStatus("No changes");
				}
			}
		}
		
	}
}

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
public class ReleaseNumberPanel extends JPanel implements KeyListener {
	
	/** the model that holds the ReleaseNumber */
	ReleaseNumber model;
	/** the tab that created this panel */
	ReleaseNumberTab parent;
	/** layout for this panel */
	//private GridBagLayout panelLayout;
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
	JPanel displayPanel;
	
	/**
	 * @param releaseTab
	 */
	public ReleaseNumberPanel(ReleaseNumberTab releaseTab) {
		this.parent = releaseTab;
		model = parent.releaseNum;
		
		editMode = Mode.CREATE;
		
		inputEnabled = true;
		
		addComponents();
		updateReleaseNumbers(null);
		updateFields();
	}
	
	/**
	 * Add all the components to this panel's view
	 *
	 */
	private void addComponents() {
		//panelLayout = new GridBagLayout();
		//GridBagConstraints c = new GridBagConstraints();
		displayPanel = new JPanel();
		displayPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//setLayout(panelLayout);
		
		releaseNumbersComboBox = new JComboBox();
		releaseNumbersComboBox.setBackground(Color.WHITE);
		
		releaseNumbersComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println("autoSelected = " + autoSelected);
				if(e.getStateChange() == ItemEvent.SELECTED) {  // ignore deselects
					if(!autoSelected) {  // ignore auto selects
						                 // these need to be separate if statements to avoid reseting
						                 // autoSelect on deselect even
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
			}
			
		});
		
		numberField = new JTextField();
		result = new JTextField();
		
		cbLabel = new JLabel("All Release Numbers       ");
		nfLabel = new JLabel("Release Number (Name)     ");
		
		submit = new JButton("Create");
		if(editMode == Mode.CREATE) {
			submit.setText("Create");
		}
		else {
			submit.setText("Submit");
		}
		submit.addActionListener(new AddReleaseNumberController(this));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		displayPanel.add(cbLabel, c);
		c.gridx = 1;
		displayPanel.add(releaseNumbersComboBox, c);
		c.gridx = 0;
		c.gridy = 1;
		displayPanel.add(nfLabel, c);
		c.gridx = 1;
		displayPanel.add(numberField, c);
		c.gridx = 0;
		c.gridy = 2;
		c.ipady = 20;
		displayPanel.add(submit, c);
		c.ipady = 0;
		c.gridx = 1;
		displayPanel.add(result, c);
		
		submit.setEnabled(false);
		numberField.addKeyListener(this);
		
		add(displayPanel, BorderLayout.CENTER);
		
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
		String status = "";
		
		if(numberField.getText().length() < 1 || numberField.getText().length() > 100) {
			noErrors = false;
			status = "There must be a release number.";
		}
		
		for(int i = 0; i < releaseNumbersComboBox.getItemCount(); ++i) {
			if(numberField.getText().equals(releaseNumbersComboBox.getItemAt(i)) &&
					!numberField.getText().equals(model.getReleaseNumber())) {
				noErrors = false;
				status = "A Release Number with the same number already exsists.";
				break;
			}
		}
		
		if(!noErrors) {
			numberField.setBackground(Color.RED);
			setStatus(status);
		} else {
			numberField.setBackground(Color.WHITE);
		}
		
		return noErrors;
	}
	
	/**
	 * fills in the fields of the combobox
	 *
	 * @param releaseNumbers a list of release numbers
	 */
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
		if(!autoSelected && !model.getReleaseNumber().equals(releaseNumbersComboBox.getSelectedItem())) {
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
			removeAllActionListeners();
			submit.addActionListener(new AddReleaseNumberController(this));
		} else {
			submit.setText("Update");
			removeAllActionListeners();
			submit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					UpdateReleaseNumber(e);
					
				}
				
			});
		}
	}
	
	private void removeAllActionListeners() {
		for(ActionListener al : submit.getActionListeners()) {
			submit.removeActionListener(al);
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
	public void updateReleaseNumbers(ReleaseNumber rn) {
		DB.getAllReleaseNumbers(new UpdateReleaseNumbersInComboBoxCallback(rn));
	}
	
	/**
	 *Determine if the submit button should be enabled
	 *
	 */
	public void enableSubmitButton() {
		boolean shouldEnable = true;
		if(editMode == Mode.CREATE && numberField.getText().length() < 1) {
			shouldEnable = false;
		}
		else {
			if(numberField.getText().equals(model.getReleaseNumber())) {
				shouldEnable = false;
			}
		}
		submit.setEnabled(shouldEnable);
	}
	
	/**
	 *
	 * Updates a release number to the values currently displayed
	 *
	 */
	public void UpdateReleaseNumber(ActionEvent e) {
		if(validateFields() && // no errors and the user actually changed something
				(!numberField.getText().equals(model.getReleaseNumber()))) {
			getModel();
			DB.updateReleaseNumber(model, new SingleReleaseNumberCallback() {
				@Override
				public void callback(ReleaseNumber rn) {
					updateReleaseNumbers(rn);
				}
			});
		} else {
			if(numberField.getText().equals(model.getReleaseNumber())) {
				setStatus("No changes");
			}
		}
	}
	
	/**
	 * checked for input from keyboard and set the submit button accordingly
	 *
	 * @param e a key event
	 */
	public void keyTyped ( KeyEvent e ){  
		enableSubmitButton();
	}
	
	/**
	 * check if key is released and set the submit button accordingly
	 *
	 * @param e a key event
	 */
	public void keyReleased ( KeyEvent e ){  
		enableSubmitButton();
	}
	
	/**
	 * Doesn't really do anything
	 *
	 * @param e a key event
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	class UpdateReleaseNumbersInComboBoxCallback implements ReleaseNumberCallback {

		ReleaseNumber rn;
		
		public UpdateReleaseNumbersInComboBoxCallback(ReleaseNumber rn) {
			this.rn = rn;
		}

		@Override
		public void callback(List<ReleaseNumber> releaseNumbers) {
			updateComboBoxWithReleaseNumbers(releaseNumbers);
			if(rn != null) {
				updateModel(rn);
			}
		}
		
	}
	
}

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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	/**
	 * @param releaseTab
	 */
	public ReleaseNumberPanel(ReleaseNumberTab releaseTab) {
		this.parent = releaseTab;
		model = parent.releaseNum;
		
		editMode = Mode.CREATE;
		
		inputEnabled = true;
		
		addComponents();
		
		DB.getAllReleaseNumbers(new ReleaseNumberCallback() {
			@Override
			public void callback(List<ReleaseNumber> releaseNumbers) {
				updateComboBoxWithReleaseNumbers(releaseNumbers);
			}
		});
		
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
		
		releaseNumbersComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final String releaseNumberName = (String) releaseNumbersComboBox.getSelectedItem();
				DB.getAllReleaseNumbers(new ReleaseNumberCallback() {
					@Override
					public void callback(List<ReleaseNumber> releaseNumbers) {
						for(ReleaseNumber rn : releaseNumbers) {
							if(rn.getReleaseNumber().equals(releaseNumberName)) {
								updateModel(rn);
								break;
							}
						}
					}
				});
			}	
		});
		
		numberField = new JTextField();
		result = new JTextField();
		
		cbLabel = new JLabel("Release Numbers       ");
		nfLabel = new JLabel("Release Number Name   ");
		
		submit = new JButton("Create");
		if(editMode == Mode.CREATE) {
			submit.setAction(new AddReleaseNumberController(this));
		} else {
			submit.setText("Update");
			submit.setAction(new UpdateReleaseNumber());
		}
		
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
		add(submit, c);
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
	 * @return True if there were no errors in the field, false otherwise
	 */
	private boolean validateFields() {
		boolean noErrors = true;
		
		if(numberField.getText().length() < 1 || numberField.getText().length() > 100) {
			noErrors = false;
		}
		
		return noErrors;
	}
	
	private void updateComboBoxWithReleaseNumbers(List<ReleaseNumber> releaseNumbers) {
		releaseNumbersComboBox.addItem("New Release Number");
		for(ReleaseNumber rn : releaseNumbers) {
			releaseNumbersComboBox.addItem(rn.getReleaseNumber());
		}
	}
	
	/**
	 * Updates the fields to match those in the current model
	 *
	 */
	private void updateFields() {
		numberField.setText(model.getReleaseNumber());
		
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
	
	public void setStatus(String string) {
		result.setText(string);
	}
	
	class UpdateReleaseNumber extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!validateFields()) {
				
			} else {
				getModel();
				DB.updateReleaseNumber(model, new SingleReleaseNumberCallback() {
					@Override
					public void callback(ReleaseNumber rn) {
						setStatus("Release Number Updated");
						updateModel(model);
					}
				});
			}
		}
		
	}
}

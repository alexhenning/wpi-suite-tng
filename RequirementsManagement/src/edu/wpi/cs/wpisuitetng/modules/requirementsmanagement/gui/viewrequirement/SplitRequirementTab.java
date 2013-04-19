/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    vpatara
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SingleRequirementCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.SplitRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;


/**
 * Main panel for splitting a requirement
 *
 * @author vpatara
 */
@SuppressWarnings("serial")
public class SplitRequirementTab extends JPanel implements KeyListener {

	/** The panel in which this is shown */
	private RequirementsPanel parent;
	/** The child requirement name field */
	private JTextField nameField;
	/** The scroll pane for the requirement splitter */
	private JScrollPane descriptionScrollPane;
	/** The child requirement description field */
	private JTextArea descriptionField;
	/** The split button */
	private JButton splitButton;

	/**
	 * Constructs a tab for splitting a requirement
	 *
	 * @param parent the single-requirement edit panel containing this panel
	 */
	public SplitRequirementTab(RequirementsPanel parent) {
		this.parent = parent;

		// Add all components to this panel
		addComponents();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 */
	protected void addComponents() {
		SpringLayout layout = new SpringLayout();
		setLayout(layout);

		// Labels
		JLabel nameLabel = new JLabel("Child requirement name:");
		JLabel descriptionLabel = new JLabel("Child requirement description:");

		// Name and description fields
		nameField = new JTextField();
		nameField.addKeyListener(this);
		descriptionField = new JTextArea(6, 40);
		descriptionField.setLineWrap(true);
		descriptionField.addKeyListener(this);
		descriptionScrollPane = new JScrollPane(descriptionField);
		descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// The split button, initially disabled
		splitButton = new JButton("Split this requirement");
		splitButton.setEnabled(false);
		splitButton.addActionListener(new SplitRequirementController(this));

		// ***** Component layouts *****

		// Name label layout
		layout.putConstraint(SpringLayout.WEST, nameLabel, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, nameLabel, 10, SpringLayout.NORTH, this);
		// Name field layout
		layout.putConstraint(SpringLayout.WEST, nameField, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, nameField, -4, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, nameField, 4, SpringLayout.SOUTH, nameLabel);

		// Description label layout
		layout.putConstraint(SpringLayout.WEST, descriptionLabel, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, descriptionLabel, 15, SpringLayout.SOUTH, nameField);
		// Description scroll pane (area) layout
		layout.putConstraint(SpringLayout.WEST, descriptionScrollPane, 4, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, descriptionScrollPane, -4, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, descriptionScrollPane, 4, SpringLayout.SOUTH, descriptionLabel);
		layout.putConstraint(SpringLayout.SOUTH, descriptionScrollPane, 160, SpringLayout.NORTH, descriptionScrollPane);

		// Split button layout
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, splitButton, 0, SpringLayout.HORIZONTAL_CENTER, this);
		layout.putConstraint(SpringLayout.NORTH, splitButton, 15, SpringLayout.SOUTH, descriptionScrollPane);

		// Add elements to the splitter panel
		add(nameLabel);
		add(nameField);
		add(descriptionLabel);
		add(descriptionScrollPane); // Rather than descriptionField
		add(splitButton);
	}


	/**
	 * Enables the split button only if the fields are non-empty
	 */
	public void updateSplitButton() {
		splitButton.setEnabled(validateFields());
	}

	/**
	 * Clears the name and description fields and disable the split button
	 */
	public void clearFields() {
		nameField.setText("");
		descriptionField.setText("");
		updateSplitButton();
	}

	/**
	 * Tells the user whether the split was successful and if so clears the
	 * fields, and then adds this child as the sub-requirement of the parent
	 *
	 * @param success whether the split was successful
	 * @param childId the id of the split child requirement, or -1 if failure
	 */
	public void reportNewChild(boolean success, int childId) {
		if(success) {
			System.out.println("Split: SUCCESS (id: " + childId + ")");
			clearFields();
			addSubRequirementToParent(childId);
		} else {
			System.out.println("Split: FAILURE");
		}
	}

	/**
	 * Invoked when a key has been typed, though it does nothing
	 *
	 * @param e key stroke event
	 */
	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * Invoked when a key has been pressed, though it does nothing
	 *
	 * @param e key stroke event
	 */
	@Override
	public void keyPressed(KeyEvent e) {}

	/**
	 * Invoked when a key has been released. Enables the split button accordingly
	 *
	 * @param e key stroke event
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		updateSplitButton();
	}


	/**
	 * Determines whether the name field or the description field is not empty
	 * (to be used by the main requirement panel to prevent lost changes)
	 *
	 * @return true if some of the fields are not empty, false otherwise
	 */
	public boolean hasUnsavedChanges() {
		return nameField.getText().length() > 0
				|| descriptionField.getText().length() > 0;
	}

	/**
	 * Determines whether the name and description fields are valid (i.e., name
	 * length is 1-100, description is non-empty)
	 *
	 * @return true if the fields are valid, false otherwise
	 */
	public boolean validateFields() {
		final int nameLength = nameField.getText().length();
		final int descriptionLength = descriptionField.getText().length();
		return 1 <= nameLength && nameLength <= 100 && 1 <= descriptionLength;
	}


	/**
	 * After a new child has been reported, makes it a sub-requirement of the parent
	 *
	 * @param childId the id of the split child requirement
	 */
	private void addSubRequirementToParent(int childId) {
		// TODO: May have to refresh the parent model before updating it

		// TODO: Cannot detect when updating fails because a lot of code has to
		// be modified (although it would make error handling easier)

		parent.model.addSubRequirement("" + childId);
		DB.updateRequirements(parent.model, new SingleRequirementCallback() {
			@Override
			public void callback(RequirementModel req) {
				System.out.println("Split and updated successfully!");
			}
		});
	}


	/**
	 * Returns the parent requirement model
	 *
	 * @return the parent requirement model
	 */
	public RequirementModel getParentModel() {
		return parent.model;
	}

	// TODO: Figure out which attributes the child should inherit from the parent
	/**
	 * Returns a child requirement split from the current requirement model
	 *
	 * @return the split child requirement
	 */
	public RequirementModel getChildModel() {
//		childModel = new RequirementModel(id, releaseNumber, status, priority,
//		name, description, estimate, actualEffort, creator, assignees,
//		creationDate, lastModifiedDate, events, subRequirements,
//		iteration, type);
		RequirementModel childModel = new RequirementModel(-1,
				parent.model.getReleaseNumber(), parent.model.getStatus(),
				parent.model.getPriority(), nameField.getText(),
				descriptionField.getText(), parent.model.getEstimate(),
				parent.model.getActualEffort(), parent.model.getCreator(),
				parent.model.getAssignees(), new Date(), new Date(),
				new ArrayList<RequirementEvent>(), new ArrayList<String>(),
				parent.model.getIteration(), parent.model.getType());
		return childModel;
	}
}

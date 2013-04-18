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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db.DB;
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
	 * Determines whether the name field or the description field is not empty
	 *
	 * @return true if some of the fields are not empty, false otherwise
	 */
	public boolean hasUnsavedChanges() {
		return nameField.getText().length() > 0
				|| descriptionField.getText().length() > 0;
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
		descriptionField = new JTextArea(6, 40);
		descriptionField.setLineWrap(true);
		descriptionScrollPane = new JScrollPane(descriptionField);
		descriptionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		// The split button
		splitButton = new JButton("Split this requirement");
		splitButton.setEnabled(false); // Initially disabled
		splitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Split!");
			}
		});

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
	 * Invoked when a key has been released. Enables the add button accordingly
	 *
	 * @param e key stroke event
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		splitButton.setEnabled(hasUnsavedChanges());
	}
}

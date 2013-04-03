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
 *    Sergey
 *    Josh
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddNoteController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;

/**
 * Main panel for notes viewing and editing
 * 
 * @author vpatara
 * @author Sergey
 * @author Josh
 */
@SuppressWarnings("serial")
public class NoteMainPanel extends JPanel {
	RequirementsPanel parent;
	boolean inputEnabled;
	public JTextArea ta;
	JPanel noteViewer;
	JPanel noteAdder;
	JScrollPane noteScrollPane;
	JButton addButton;
	List<RequirementNote> notes;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param parent NoteTab that contains this object
	 */
	public NoteMainPanel(RequirementsPanel parent) {
		this.parent = parent;
		notes = new LinkedList<RequirementNote>();
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		
		
		// TODO: updateFields();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		setLayout(new BorderLayout());

		ta = new JTextArea(5, 40);
		ta.setLineWrap(true);
		ta.setText("New note");		
		ta.setEditable(inputEnabled);
		JScrollPane textPane = new JScrollPane(ta);
		textPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		noteViewer = new JPanel(new GridBagLayout());

		noteScrollPane = new JScrollPane(noteViewer);
		noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		addButton = new JButton("Add note");
		addButton.addActionListener(new AddNoteController(this, parent.model, parent));
		addButton.setEnabled(inputEnabled);
		
		// Add elements to the main panel
		add(noteScrollPane, BorderLayout.CENTER);
		
		noteAdder = new JPanel(new BorderLayout());
		
		noteAdder.add(textPane, BorderLayout.PAGE_START);
		noteAdder.add(addButton, BorderLayout.LINE_START);
		add(noteAdder, BorderLayout.PAGE_END);
	}
	
	public void setNotes(List<RequirementNote> notes) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 0, 3, 0);
		c.gridx = 0;
		c.gridy = notes.size();
		c.weightx = 1.0;
		c.weighty = 1.0;
		
		noteViewer.removeAll();
		
		for (RequirementNote note : notes) {
			if (note != null) {
				NotePanel newNote = new NotePanel(note);
				noteViewer.add(newNote, c);
				c.gridy -= 1;
			}
		}

		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	public void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		ta.setEditable(enabled);
		ta.setEnabled(enabled);
		addButton.setEnabled(enabled);
		// TODO: implement
	}
}

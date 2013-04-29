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
 *    Sergey
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;

/**
 * Displays a single posted note
 * 
 * @author vpatara
 * @author Sergey
 * @author Josh
 */
@SuppressWarnings("serial")
public class NotePanel extends JPanel {

	/** text area to put message */
	JTextArea messageArea;
	/** text field for info */
	JTextField infoText;
	/** text field for the author */
	JTextField authorText;
	/** the message */
	String message;
	/** the aurthor */
	String author;
	/** the date */
	Date date;
	
	/**
	 * Constructs a panel for a single note
	 * 
	 * @param message
	 * @param author
	 * @param date
	 */
	public NotePanel(String message, String author, Date date) {
		
		this.message = message;
		this.author = author;
		this.date = date;

		// Add all components to this panel
		addComponents();
	}

	/**
	 * Calls first constructor given a note
	 */
	public NotePanel(RequirementNote note) {
		this(note.getBody(), note.getUser().getName(), note.getDate());
	}

	/**
	 * Adds GUI components to display a note (including a message, an author,
	 * and a date)
	 */
	protected void addComponents() {
		
		// Set layout
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout());

		// Creates area for author and adds to note panel
		authorText = new JTextField("Note by " + author);
		authorText.setEditable(false);
		authorText.setFocusable(false);
		authorText.setOpaque(false);
		add(authorText, BorderLayout.PAGE_START);

		// Creates area for message and adds to note panel
		// This area is focusable so that users can copy texts right from the note
		messageArea = new JTextArea(message);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.setBackground(Color.getHSBColor(0f, 0f, 0.98f)); // Very light gray
		add(messageArea, BorderLayout.CENTER);

		// Creates area for creation information and adds to note panel
		infoText = new JTextField("Added on " + date.toString());
		infoText.setEditable(false);
		infoText.setFocusable(false);
		infoText.setOpaque(false);
		add(infoText, BorderLayout.PAGE_END);
	}
}

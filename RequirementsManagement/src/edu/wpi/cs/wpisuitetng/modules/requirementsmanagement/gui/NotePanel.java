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
 *
 */
@SuppressWarnings("serial")
public class NotePanel extends JPanel {

	JTextArea messageArea;
	JTextField infoText;
	JTextField authorText;
	String message;
	String author;
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

	public NotePanel(RequirementNote note) {
		this(note.getBody(), note.getUser().getName(), note.getDate());
	}

	/**
	 * Adds GUI components to display a note (including a message, an author,
	 * and a date)
	 * 
	 */
	protected void addComponents() {
		
		//set layout
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		authorText = new JTextField(author);
		authorText.setSize(500, 30);
		authorText.setEditable(false);
		authorText.setFocusable(false);
		authorText.setOpaque(false);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 0;
		add(authorText, c);

		//creates area for message and adds to note panel
		messageArea = new JTextArea(message);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.setFocusable(false);
		messageArea.setOpaque(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		add(messageArea, c);
		
		//creates area for creation information and adds to note panel
		infoText = new JTextField("                                                         " +
				"added note on " + date.toString());
		infoText.setSize(500, 30);
		infoText.setEditable(false);
		infoText.setFocusable(false);
		infoText.setOpaque(false);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 2;
		add(infoText, c);
		
	}
}

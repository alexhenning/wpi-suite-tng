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
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;

/**
 * Displays a single posted note
 * 
 * @author Josh
 *
 */
@SuppressWarnings("serial")
public class RequirementHistoryPanel extends JPanel {

	JTextArea messageArea;
	JTextField infoText;
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
	public RequirementHistoryPanel(String message, String author, Date date) {
		
		this.message = message;
		this.author = author;
		this.date = date;

		// Add all components to this panel
		addComponents();
	}

	public RequirementHistoryPanel(ProjectEvent event) {
		this(event.getObjectId(), event.getUser().getName(), event.getDate());
	}

	/**
	 * Adds GUI components to display a note (including a message, an author,
	 * and a date)
	 * 
	 */
	protected void addComponents() {
		
		//set layout
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout());

		//creates area for message and adds to note panel
		messageArea = new JTextArea(message);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.setFocusable(false);
		messageArea.setOpaque(false);
		add(messageArea, BorderLayout.CENTER);
		
		//creates area for creation information and adds to note panel
		infoText = new JTextField(author + "changed on " + date.toString());
		infoText.setEditable(false);
		infoText.setFocusable(false);
		infoText.setOpaque(false);
		add(infoText, BorderLayout.PAGE_END);
		
	}
}
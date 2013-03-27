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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

	/**
	 * Adds GUI components to display a note (including a message, an author,
	 * and a date)
	 * 
	 */
	protected void addComponents() {
		
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new GridBagLayout());
//		setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();

		messageArea = new JTextArea(message);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.setFocusable(false);
		messageArea.setOpaque(false);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(messageArea, c);
		
//		infoLabel = new JTextField("<html><body><font size=4>" + author + " <font size=3>added a note at " + date.toString() + "</body></html>");
		infoText = new JTextField(author + " added a note at " + date.toString());
		infoText.setSize(400, 30);
		infoText.setEditable(false);
		infoText.setFocusable(false);
		infoText.setOpaque(false);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 1;
		add(infoText, c);
		
//		otherInfoLabel = new JLabel(date.toString());
//		c.gridx = 3;
//		c.gridy = 1;
//		c.gridwidth = 1;
//		c.gridheight = 1;
//		add(otherInfoLabel, c);
	}
}

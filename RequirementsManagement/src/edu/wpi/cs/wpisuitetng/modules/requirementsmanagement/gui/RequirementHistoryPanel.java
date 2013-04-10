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
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent.ProjectEventType;

/**
 * Displays a single posted history
 * 
 * @author Josh
 * @author Jacob Palnick
 *
 */
@SuppressWarnings("serial")
public class RequirementHistoryPanel extends JPanel {

	/** text area for the messages */
	JTextArea messageArea;
	/** text area for the info */
	JTextField infoText;
	/** the message */
	String message;
	/** the aurthor */
	String author;
	/**the date*/
	Date date;
	
	//TODO change this to show the creation events
	/**
	 * Constructs a panel for a single note
	 * 
	 * @param message
	 * @param author
	 * @param date
	 */
	public RequirementHistoryPanel(Map<String, FieldChange<?>> map, String author, Date date) {
		

		this.author = author;
		this.date = date;
		String eol = System.getProperty("line.separator");
		String message = "";
		for (Entry <String,FieldChange<?>> change: map.entrySet()) {
			System.out.println(change.getKey()+": "+change.getValue().getClass());
			//TODO improve this code
			if (message.compareTo("") != 0) {
				if (change.getKey().equals("subRequirements")) {
					message = message + eol + change.getKey() + " changed from [";
					for(Object obj : (String[])change.getValue().getOldValue()) {
						message = message + obj + ", ";
					}
					message = message + "] to [";
					for(Object obj : (String[])change.getValue().getNewValue()) {
						message = message + obj + ", ";
					}
					message = message + "].";
				} else {
					message = message + eol + change.getKey() + " changed from " + change.getValue().getOldValue() + " to " + change.getValue().getNewValue() + ".";
				}
			}
			else {
				if (change.getKey().equals("subRequirements")) {
					message = message + change.getKey() + " changed from [";
					for(Object obj : (String[])change.getValue().getOldValue()) {
						message = message + obj + ", ";
					}
					message = message + "] to [";
					for(Object obj : (String[])change.getValue().getNewValue()) {
						message = message + obj + ", ";
					}
					message = message + "].";
				} else {
					message = message + change.getKey() + " changed from " + change.getValue().getOldValue() + " to " + change.getValue().getNewValue() + ".";
				}
			}
		}
		this.message = message;

		// Add all components to this panel
		addComponents();
	}

	/**
	 * Constructs a panel for a single note
	 * 
	 * @param event a ProjectEvent for the viewed requirement
	 */
	public RequirementHistoryPanel(ProjectEvent event) {
		this.author = event.getUser().getName();
		this.date = event.getDate();
		String eol = System.getProperty("line.separator");
		String message = "";
		if (event.getType() == ProjectEventType.CHANGES) {
			for (Entry <String,FieldChange<?>> change: event.getChanges().entrySet()) {
				//TODO improve this code
				if (message.compareTo("") != 0) {
					if (change.getKey().equals("subRequirements")) {
						message = message + eol + change.getKey() + " changed from [";
						for(Object obj : (String[])change.getValue().getOldValue()) {
							message = message + obj + ", ";
						}
						message = message + "] to [";
						for(Object obj : (String[])change.getValue().getNewValue()) {
							message = message + obj + ", ";
						}
						message = message + "].";
					} else {
						message = message + eol + change.getKey() + " changed from " + change.getValue().getOldValue() + " to " + change.getValue().getNewValue() + ".";
					}
				}
				else {
					if (change.getKey().equals("subRequirements")) {
						message = message + change.getKey() + " changed from [";
						for(Object obj : (String[])change.getValue().getOldValue()) {
							message = message + obj + ", ";
						}
						message = message + "] to [";
						for(Object obj : (String[])change.getValue().getNewValue()) {
							message = message + obj + ", ";
						}
						message = message + "].";
					} else {
						message = message + change.getKey() + " changed from " + change.getValue().getOldValue() + " to " + change.getValue().getNewValue() + ".";
					}
				}
			}
		} else {
			message = "Created";
		}
		this.message = message;

		// Add all components to this panel
		addComponents();
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
		infoText = new JTextField(author + " changed on " + date.toString());
		infoText.setEditable(false);
		infoText.setFocusable(false);
		infoText.setOpaque(false);
		add(infoText, BorderLayout.PAGE_END);
		
	}
}
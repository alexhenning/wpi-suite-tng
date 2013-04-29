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
 *    Jacob Palnick
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.viewrequirement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.FieldChange;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent.ProjectEventType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ReleaseNumber;

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
				message = message + eol + getChangeMessage(change);
			} else {
				message = message + getChangeMessage(change);
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
		author = event.getUser().getName();
		date = event.getDate();
		String eol = System.getProperty("line.separator");
		String message = "";
		if (event.getType() == ProjectEventType.CHANGES) {
			for (Entry <String,FieldChange<?>> change: event.getChanges().entrySet()) {
				//TODO improve this code
				if (message.compareTo("") != 0) {
					message = message + eol + getChangeMessage(change);
				} else {
					message = message + getChangeMessage(change);
				}
			}
		} else {
			message = "Requirement created";
		}
		this.message = message;

		// Add all components to this panel
		addComponents();
	}
	
	/**
	 * handles converting a filed change object to a properly formated message.
	 * @param change
	 * @return the message for the field change
	 */
	private String getChangeMessage(Entry <String,FieldChange<?>> change) {
		String out = "";
		//TODO add any additional special filed changes
		if (change.getKey().equals("subRequirements")) {
			out = out + change.getKey() + " changed from [";
			for(int i=0; i<((String[])change.getValue().getOldValue()).length; i++) {
				out = out + (i==0 ? "" : ", ") + ((String[])change.getValue().getOldValue())[i];
			}
			out = out + "] to [";
			for(int i=0; i<((String[])change.getValue().getNewValue()).length; i++) {
				out = out + (i==0 ? "" : ", ") + ((String[])change.getValue().getNewValue())[i];
			}
			out = out + "].";
		} else if (change.getKey().equals("releaseNumber")) {
			ReleaseNumber oldValue = ((ReleaseNumber)change.getValue().getOldValue());
			ReleaseNumber newValue = ((ReleaseNumber)change.getValue().getNewValue());
			
			out = out + change.getKey() + " changed from ";
			out = out + (oldValue != null ? oldValue.getReleaseNumber() : "none" );
			out = out + " to ";
			out = out + (newValue != null ? newValue.getReleaseNumber() : "none" );
			out = out + ".";
		} else if (change.getKey().equals("iteration")) {
			Iteration oldValue = ((Iteration)change.getValue().getOldValue());
			Iteration newValue = ((Iteration)change.getValue().getNewValue());
			
			out = out + change.getKey() + " changed from ";
			out = out + (oldValue != null ? oldValue.getIterationNumber() : "Backlog" );
			out = out + " to ";
			out = out + (newValue != null ? newValue.getIterationNumber() : "Backlog" );
			out = out + ".";
		} else if (change.getKey().equals("assignees")) {
			User[] oldUsers = (User[])change.getValue().getOldValue();
			User[] newUsers = (User[])change.getValue().getNewValue();
			out = out + change.getKey() + " changed from [";
			for(int i=0; i<oldUsers.length; i++) {
				out = out + (i==0 ? "" : ", ") + oldUsers[i].getUsername();
			}
			out = out + "] to [";
			for(int i=0; i<newUsers.length; i++) {
				out = out + (i==0 ? "" : ", ") + newUsers[i].getUsername();
			}
			out = out + "].";
		} else {
			out = out + change.getKey() + " changed from " + change.getValue().getOldValue() + " to " + change.getValue().getNewValue() + ".";
		}
		return out;
		
	}

	/**
	 * Adds GUI components to display one transaction log (including a message,
	 * an author, and a date)
	 */
	protected void addComponents() {

		// Set layout
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout());

		// Creates area for logs and adds to history panel
		messageArea = new JTextArea(message);
		messageArea.setLineWrap(true);
		messageArea.setEditable(false);
		messageArea.setFocusable(false);
		messageArea.setBackground(Color.getHSBColor(0f, 0f, 0.98f)); // Very light gray
		add(messageArea, BorderLayout.CENTER);

		// Creates area for logging information and adds to history panel
		infoText = new JTextField(author + " changed on " + date.toString());
		infoText.setEditable(false);
		infoText.setFocusable(false);
		infoText.setOpaque(false);
		add(infoText, BorderLayout.PAGE_END);
	}
}

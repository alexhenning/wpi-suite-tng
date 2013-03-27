/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Chris Casola
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.NoteMainPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers.AddNoteObserver;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 *
 * Handles saving requirement notes to the server
 * @author Tim
 *
 */
public class AddNoteController extends AbstractAction implements ActionListener{
	
	private NoteMainPanel view;
	private RequirementModel model;
	private RequirementsPanel parentView;
	
	/**
	 * Default constructor for Notes
	 *
	 * @param view The NewNotePanel containing the comment field
	 * @param model The Requirement model being commented on
	 * @param parentView The RequirementPanel displaying the defect
	 */
	public AddNoteController(final NoteMainPanel view, final RequirementModel model, final RequirementsPanel parentView) {
		this.view = view;
		this.model = model;
		this.parentView = parentView;
	}
	
	/**
	 * Called by pressing add note button
	 *
	 * @param e
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		saveNote();
	}

	
	/**
	 * Save the new note to the server
	 *
	 */
	public void saveNote() {
		final String noteText = view.ta.getText();
		final Request request = Network.getInstance().makeRequest(
				"requirementsmanagement/requirementnote", HttpMethod.PUT);
		request.setBody((new RequirementNote(model.getId(), model.getCreator(), noteText)).toJSON());
		request.addObserver(new AddNoteObserver(this));
		request.send();
	}
	
	
	/**
	 * Add the comment to the view if the server responded with a success message
	 *
	 * @param response The response from the server
	 */
	public void receivedAddConfirmation(ResponseModel response) {
		Gson gson = new Gson();
		RequirementNote note = gson.fromJson(response.getBody(), RequirementNote.class);
		view.addNote(note);
	}
	
	/**
	 * Alert the user that an error occurred sending the note to the server
	 *
	 * @param response The response from the server
	 */
	public void receivedAddFailure(ResponseModel response) {
		System.err.println("Received note failure");
	}
}

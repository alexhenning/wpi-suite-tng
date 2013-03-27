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
public class AddNoteController {
	
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
	 * Save the new note to the server
	 *
	 */
	public void saveNote() {
		//final String commentText = view.getCommentField().getText();
		//if(commentText.length() > 0) {
			final AddNoteObserver saveNoteObserver = new AddNoteObserver(this);
			final Request request = Network.getInstance().makeRequest(
					"requirementsmanagement/requirementnote", HttpMethod.PUT);
			//final RequirementNote note = new RequirementNote(model.getId(), model.getCreator(), commentText);
			//view.getCommentField.setText("");
			//request.setBody(note.toJSON());
			request.addObserver(saveNoteObserver);
			request.send();
		//}
	}
	
	
	/**
	 * Add the comment to the view if the server responded with a success message
	 *
	 * @param response The response from the server
	 */
	public void receivedAddConfirmation(ResponseModel response) {
		// TODO Implement success
	}
	
	/**
	 * Alert the user that an error occurred sending the note to the server
	 *
	 * @param response The response from the server
	 */
	public void receivedAddFailure(ResponseModel response) {
		// TODO Implement failure
	}

}

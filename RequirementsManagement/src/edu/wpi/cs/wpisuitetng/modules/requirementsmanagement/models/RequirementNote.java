package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.core.models.User;

/**
 * Persistent Model that represents a Comment on a Requirement 
 */
public class RequirementNote extends RequirementEvent {

	private int requirementId;
	private String body;
	
	/**
	 * Create a RequirementComment with default properties.
	 */
	public RequirementNote() {
		type = EventType.NOTE;
		requirementId = -1;
		body = "";
	}
	
	/**
	 * Create a RequirementComment with the given properties.
	 * Other properties are the same as the default constructor.
	 * 
	 * @param requirementId the id of the Requirement the RequirementComment is associated with
	 * @param user the User who created the RequirementComment
	 * @param body the message body of the RequirementComment
	 */
	public RequirementNote(int requirementId, User user, String body) {
		this();
		this.requirementId = requirementId;
		this.user = user;
		this.body = body;
	}

	/**
	 * @return the id of the Requirement this RequirementComment is associated with
	 */
	public int getRequirementId() {
		return requirementId;
	}

	/**
	 * @param requirementId the requirementId to set
	 */
	public void setRequirementId(int requirementId) {
		this.requirementId = requirementId;
	}

	/**
	 * @return the message body of this RequirementComment
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toJSON() {
		String json;
		Gson gson = new Gson();
		json = gson.toJson(this, RequirementNote.class);
		return json;
	}

	/**
	 * Converts the given JSON string into a RequirementComment
	 * @param json JSON string containing a serialized RequirementComment
	 * @return a Comment deserialized from the given JSON string
	 */
	public static RequirementNote fromJson(String json) {
		Gson parser = new Gson();
		return parser.fromJson(json, RequirementNote.class);
	}
}

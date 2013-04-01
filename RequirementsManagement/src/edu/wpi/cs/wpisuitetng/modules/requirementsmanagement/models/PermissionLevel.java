package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

/**
 * Permission levels for different types of users
 * @author William Terry
 */
public enum PermissionLevel {
	EDIT_ALL,		//Admin level
	EDIT_LIMITED,	//Team member level
	VIEW,			//Customer level
	NONE			//User not associated with project
}

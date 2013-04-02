package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

/**
 * Permission levels for different types of users
 * @author William Terry
 */
public enum PermissionLevel {
	WRITE,		//Admin level
	WRITE_LIMITED,	//Team member level
	READ,			//Customer level
	NONE			//User not associated with project
}

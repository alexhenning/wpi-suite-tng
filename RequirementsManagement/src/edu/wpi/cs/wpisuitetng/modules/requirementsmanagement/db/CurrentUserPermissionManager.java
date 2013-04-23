/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    vpatara
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.db;

import java.util.List;
import java.util.ArrayList;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * Manager that handles the retrieval of the permission of the current user
 *
 * @author vpatara
 *
 */
public class CurrentUserPermissionManager {

	/** Singleton instance of this class */
	private static CurrentUserPermissionManager instance = new CurrentUserPermissionManager();

	/** Callbacks waiting for the retrieval of the current user & permission */
	private List <SinglePermissionCallback> waitList;
	/** Indicates whether the permission model is ready */
	private boolean hasCurrentProfile;
	/** The permission model of the current user */
	private Permissions currentProfile;
	/** Indicates whether function profileReady() has been called */
	private boolean hasUsernameReadyBeenCalled;

	/**
	 * Constructs the manager for user
	 */
	private CurrentUserPermissionManager() {
		waitList = new ArrayList <SinglePermissionCallback> ();
		hasCurrentProfile = false;
		currentProfile = new Permissions("", PermissionLevel.NONE); // Stub
		hasUsernameReadyBeenCalled = false;
	}

	/**
	 * Returns the singleton instance of this class
	 *
	 * @return the singleton instance
	 */
	public static CurrentUserPermissionManager getInstance() {
		return instance;
	}

	/**
	 * If the current permission model is ready, call the callback immediately;
	 * otherwise, add the callback to the wait-list. This is the blocking
	 * version of getCurrentProfile.
	 *
	 * @param callback the callback object waiting for the permission model
	 */
	public void addCallback(SinglePermissionCallback callback) {
		if(callback != null) {
			boolean canCallNow = false;

			// Prevents the boolean and the list to be accessed simultaneously
			// by multiple threads
			synchronized (this) {
				if(hasCurrentProfile) {
					canCallNow = true;
				} else {
					waitList.add(callback);
				}
			}

			// Need to call the callback outside the lock
			if(canCallNow)
				callback.callback(currentProfile);
		}
	}

	/**
	 * Saves current username for future permission model retrieval, to be
	 * called by RM's JanewayModule after the user is successfully logged in
	 */
	public void usernameReady() {
		System.out.println("usernameReady()");
		boolean calledFirstTime;
		synchronized (this) {
			calledFirstTime = !hasUsernameReadyBeenCalled;
			hasUsernameReadyBeenCalled = true;
		}

		// In the first call, gets the current username and profile
		if(calledFirstTime) {
			final String username = ConfigManager.getConfig().getUserName();
			System.out.println("  Current username : " + username);

			DB.getSinglePermission(username, new SinglePermissionCallback() {
				@Override
				public void callback(Permissions profile) {
					// User named "admin" should always be given ADMIN permission
					// although the database stores another permission value
					// TODO: update the value in the database to ADMIN
					if(username.equals("admin")) {
						profile.setPermissionLevel(PermissionLevel.ADMIN);
					}
					setCurrentProfile(profile);
				}

				@Override
				public void failure() {
					// The current user doesn't have the permission model,
					// so add a new permission with level NONE
					Permissions newProfile = new Permissions(username, PermissionLevel.NONE);

					// User named "admin" should always be given ADMIN permission
					if(username.equals("admin")) {
						newProfile.setPermissionLevel(PermissionLevel.ADMIN);
					}

					// Doesn't care if the new profile fails to be saved
					// because it can be recreated with level NONE anytime later
					DB.addSinglePermission(newProfile, null);
					setCurrentProfile(newProfile);
				}
			});
		}
	}

	/**
	 * Saves the current user profile (permission) and processes the waitlist
	 *
	 * @param profile the current user's permission in the project
	 */
	private void setCurrentProfile(Permissions profile) {
		System.out.println("  Current permission : "
				+ profile.getUsername() + ":"
				+ profile.getPermissionLevel());

		// Sets the current profile while preventing the race condition
		synchronized (this) {
			hasCurrentProfile = true;
			currentProfile = profile;
		}

		// Clears the callback wait-list
		for(SinglePermissionCallback callback : waitList) {
			callback.callback(currentProfile);
		}
		waitList.clear();
	}

	/**
	 * Returns the permission model of the current user, but it is not
	 * guaranteed to be non null. This is a non-blocking version.
	 *
	 * @return the current user-permission profile
	 */
	public Permissions getCurrentProfile() {
		return currentProfile;
	}
}

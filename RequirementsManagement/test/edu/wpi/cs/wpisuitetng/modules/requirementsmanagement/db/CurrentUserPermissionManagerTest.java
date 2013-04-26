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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.janeway.config.ConfigManager;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.guiTest1;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.PermissionLevel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * Test cases for CurrentUserPermissionManager
 *
 * @author vpatara
 *
 */
public class CurrentUserPermissionManagerTest {

	private CurrentUserPermissionManager manager;
	private guiTest1 serverTest;
	private boolean calledBack; // Used with adding a single callback
	private boolean calledBack1; // Used with adding multiple callbacks
	private boolean calledBack2; // Used with adding multiple callbacks
	private boolean calledBack3; // Used with adding multiple callbacks
	private Permissions calledBackProfile;

	/**
	 * Initializes the dummy server and profile manager used for this test set
	 */
	@Before
	public void setUp() {
		// Use the dummy server set up in guiTest1
		serverTest = new guiTest1();
		serverTest.setUp();
		// Set up the Janeway configuration, where the username info is stored
		ConfigManager.getConfig().setUserName("tester");
		// Get the singleton manager
		manager = CurrentUserPermissionManager.getInstance();
		manager.resetManager();
	}

	/**
	 * Closes the dummy server and resets the permission
	 */
	@After
	public void tearDown() {
		serverTest.after();
		manager.resetManager();
	}

	@Test
	public void testCallUsernameReady() {
		manager.usernameReady();
	}

	@Test
	public void testCallUsernameReadyMultipleTimes() {
		manager.usernameReady();
		// Subsequent calls should have no effect (and no errors)
		manager.usernameReady();
		manager.usernameReady();
		manager.usernameReady();
	}

	@Test
	public void testAddNullCallback() {
		manager.addCallback(null);
	}

	@Test
	public void testAddEmptyCallbackWithoutUsernameReady() {
		manager.addCallback(new SinglePermissionCallback() {
			@Override
			public void failure() {} // Never gets called
			@Override
			public void callback(Permissions profile) {} // Empty (won't be called)
		});
	}

	@Test
	public void testAddCallbackBeforeUsernameReady() throws InterruptedException {
		calledBack = false;
		calledBackProfile = null;

		// Add a callback for retrieving a single permission
		manager.addCallback(new SinglePermissionCallback() {
			@Override
			public void failure() {} // Never gets called
			@Override
			public void callback(Permissions profile) {
				calledBack = true;
				calledBackProfile = profile;
			}
		});

		// Now notify the manager that the username info is ready
		manager.usernameReady();

		// Wait two seconds for the manager to call back
		Object sync = new Object();
		synchronized (sync) {
			sync.wait(2000);
		}

		// Check if the callback gets called
		assertTrue(calledBack);
		assertNotNull(calledBackProfile);

		// Compare the direct and indirect profiles from the manager (they must match)
		Permissions directProfile = manager.getCurrentProfile();
		assertEquals(directProfile.getUsername(), calledBackProfile.getUsername());
		assertEquals(directProfile.getPermissionLevel(), calledBackProfile.getPermissionLevel());
	}

	@Test
	public void testAddCallbackRightAfterUsernameReady() throws InterruptedException {
		calledBack = false;
		calledBackProfile = null;

		// Notify the manager that the username info is ready
		manager.usernameReady();

		// Add a callback right after notifying the manager
		manager.addCallback(new SinglePermissionCallback() {
			@Override
			public void failure() {} // Never gets called
			@Override
			public void callback(Permissions profile) {
				calledBack = true;
				calledBackProfile = profile;
			}
		});

		// Wait two seconds for the manager to call back
		Object sync = new Object();
		synchronized (sync) {
			sync.wait(2000);
		}

		// Check if the callback gets called
		assertTrue(calledBack);
		assertNotNull(calledBackProfile);

		// Compare the direct and indirect profiles from the manager (they must match)
		Permissions directProfile = manager.getCurrentProfile();
		assertEquals(directProfile.getUsername(), calledBackProfile.getUsername());
		assertEquals(directProfile.getPermissionLevel(), calledBackProfile.getPermissionLevel());
	}

	@Test
	public void testAddCallbackSomeTimeAfterUsernameReady() throws InterruptedException {
		calledBack = false;
		calledBackProfile = null;
		Object sync = new Object(); // For waiting the manager to respond

		// Notify the manager that the username info is ready
		manager.usernameReady();

		// Wait two seconds for the permission to be ready for retrieval
		synchronized (sync) {
			sync.wait(2000);
		}

		// Add a callback after the profile is ready
		manager.addCallback(new SinglePermissionCallback() {
			@Override
			public void failure() {} // Never gets called
			@Override
			public void callback(Permissions profile) {
				calledBack = true;
				calledBackProfile = profile;
			}
		});

		// Wait two seconds for the manager to call back
		synchronized (sync) {
			sync.wait(2000);
		}

		// Check if the callback gets called
		assertTrue(calledBack);
		assertNotNull(calledBackProfile);

		// Compare the direct and indirect profiles from the manager (they must match)
		Permissions directProfile = manager.getCurrentProfile();
		assertEquals(directProfile.getUsername(), calledBackProfile.getUsername());
		assertEquals(directProfile.getPermissionLevel(), calledBackProfile.getPermissionLevel());
	}

	@Test
	public void testAddMultipleCallbacks() throws InterruptedException {
		calledBack1 = false;
		calledBack2 = false;
		calledBack3 = false;
		calledBackProfile = null;
		Object sync = new Object(); // For waiting the manager to respond

		// Add a callback for retrieving a single permission
		manager.addCallback(new SinglePermissionCallback() {
			@Override
			public void failure() {} // Never gets called
			@Override
			public void callback(Permissions profile) {
				calledBack1 = true;
			}
		});

		// Now notify the manager that the username info is ready
		manager.usernameReady();

		// Add another callback right after it, even before it's ready (asynchronous)
		manager.addCallback(new SinglePermissionCallback() {
			@Override
			public void failure() {} // Never gets called
			@Override
			public void callback(Permissions profile) {
				calledBack2 = true;
			}
		});

		// Wait one second for the manager to process earlier callbacks
		synchronized (sync) {
			sync.wait(1000);
		}

		// Then add another callback
		manager.addCallback(new SinglePermissionCallback() {
			@Override
			public void failure() {} // Never gets called
			@Override
			public void callback(Permissions profile) {
				calledBack3 = true;
				calledBackProfile = profile;
			}
		});

		// Wait two seconds for the manager to process the last retrieval
		synchronized (sync) {
			sync.wait(2000);
		}

		// Check if the callback gets called
		assertTrue(calledBack1);
		assertTrue(calledBack2);
		assertTrue(calledBack3);
		assertNotNull(calledBackProfile);

		// Compare the direct and indirect profiles from the manager (they must match)
		Permissions directProfile = manager.getCurrentProfile();
		assertEquals(directProfile.getUsername(), calledBackProfile.getUsername());
		assertEquals(directProfile.getPermissionLevel(), calledBackProfile.getPermissionLevel());

		// Finally, try calling usernameReady() a couple more times
		// All shouldn't make any effect
		manager.usernameReady();
		manager.usernameReady();
		manager.usernameReady();
		manager.usernameReady();
	}

	@Test
	public void testAddNewPermissionForNewUserWithNONEPermission() {
		Permissions userProfile = manager.addNewPermissionForNewUser("tester");
		assertEquals("tester", userProfile.getUsername());
		assertEquals(PermissionLevel.NONE, userProfile.getPermissionLevel());
	}

	@Test
	public void testAddNewPermissionForAdminWithADMINPermission() {
		Permissions adminProfile = manager.addNewPermissionForNewUser("admin");
		assertEquals("admin", adminProfile.getUsername());
		assertEquals(PermissionLevel.ADMIN, adminProfile.getPermissionLevel());
	}

	@Test
	public void testAddNewPermissionMultipleTimes() {
		Permissions profile1 = manager.addNewPermissionForNewUser("tester");
		Permissions profile2 = manager.addNewPermissionForNewUser("tester");
		Permissions profile3 = manager.addNewPermissionForNewUser("tester");

		assertEquals("tester", profile1.getUsername());
		assertEquals(PermissionLevel.NONE, profile1.getPermissionLevel());

		// Check if they are the same objects, not just their values
		assertEquals(profile1, profile2);
		assertEquals(profile1, profile3);
	}
}

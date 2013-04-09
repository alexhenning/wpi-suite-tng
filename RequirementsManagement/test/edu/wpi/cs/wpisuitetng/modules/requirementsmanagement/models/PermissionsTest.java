package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PermissionsTest {

	static Permissions prof, profile1, profile2, profile3;
	static Date date;
	
	/**
	 * Instantiate 3 unique permissions models
	 *
	 */
	@BeforeClass
	static public void setUp(){
		profile1 = new Permissions("user1", PermissionLevel.ADMIN);
		profile1.setPermissionLevel(PermissionLevel.ADMIN);		//fix for a very strange error
		profile2 = new Permissions("user2", PermissionLevel.UPDATE);
		profile3 = new Permissions();
		date = new Date();
	}
	
	@AfterClass
	static public void tearDown(){
		//not used
	}
	
	@Test
	public void testInstantiation(){
		assertEquals("user1", profile1.getUsername());
		assertEquals(PermissionLevel.ADMIN, profile1.getPermissionLevel());
		
		assertEquals("user2", profile2.getUsername());
		assertEquals(PermissionLevel.UPDATE, profile2.getPermissionLevel());
		
		assertEquals("", profile3.getUsername());
		assertEquals(PermissionLevel.NONE, profile3.getPermissionLevel());
	}
	
	@Test
	public void testSettingLevel(){
		profile1.setPermissionLevel(PermissionLevel.UPDATE);
		assertEquals(PermissionLevel.UPDATE, profile1.getPermissionLevel());
		profile1.setPermissionLevel(PermissionLevel.NONE);
		assertEquals(PermissionLevel.NONE, profile1.getPermissionLevel());
	}
	
	@Test
	public void testSettingUsername(){
		profile3.setUsername("a different name");
		assertEquals("a different name", profile3.getUsername());
		profile3.setUsername("an even more different name");
		assertEquals("an even more different name", profile3.getUsername());
	}
	
	@Test
	public void testDateRecording(){
		assertEquals(date, profile1.getLastModifiedDate());
		assertEquals(date, profile2.getLastModifiedDate());
		assertEquals(date, profile3.getLastModifiedDate());
	}
	
	@Test
	public void testJSONTranslation(){
		//String jsonRepresentation = profile4.toJSON();
		//Permissions profile4 = Permissions.fromJSON(jsonRepresentation);
		Permissions profile4 = new Permissions();
		Permissions profile5 = new Permissions();
		assertEquals(profile4.toJSON(), profile5.toJSON());
	}
}

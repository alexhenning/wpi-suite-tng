package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.HashSet;

import org.junit.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.JanewayModule;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.MainTabController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementEvent;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementStatus;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementType;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.NavToolbarView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.ToolbarController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.ToolbarView;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
//import edu.wpi.cs.wpisuitetng.network.TestRequest.MockObserver;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.dummyserver.DummyServer;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;


public class guiTest1 {
	class MockObserver implements RequestObserver {

		public MockObserver() {
			super();
		}

		@Override
		public void responseSuccess(IRequest iReq) {
			synchronized (this) {
				notifyAll(  );
			}
		}

		@Override
		public void responseError(IRequest iReq) {
			// TODO Auto-generated method stub

		}

		@Override
		public void fail(IRequest iReq, Exception exception) {
			// TODO Auto-generated method stub

		}
	}

	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;

	/** The panel for Requirements management */
	public JPanel buttonPanel = new JPanel();

	/** The main tab controller for this tab */
	private MainTabController mainTabController;
	/** The toolbar controller for this tab */
	private ToolbarController toolbarController;

	JTabbedPane tabPane = new JTabbedPane();
	JanewayModule module1;
	private NetworkConfiguration config;
	private static int port = 38512;




	DummyServer server;
	/**
	 * Test that communication works. DummyServer must be running on port 8080 for this test to work.
	 */
	@Before
	public void setUp() {
		config = new NetworkConfiguration("http://localhost:" + port);
		Network.getInstance().setDefaultNetworkConfiguration(config);

		// Make a new instance of the MyRequestObserver class.
		final MockObserver requestObserver = new MockObserver();

		// The request body
		String body = "This is the request body!";

		// Create the URL
		try {
			server = new DummyServer(port);
			server.start();

			// Make a new POST Request.
			Request manualRequest = new Request(config, null, HttpMethod.POST);	// construct the Request

			// Configure the request
			manualRequest.setBody(body);	// set the request body to send to the server
			manualRequest.addObserver(requestObserver);	// Add the requestObserver to the request's set of Observers

			// Send the request!
			manualRequest.send();
			synchronized (requestObserver) {
				requestObserver.wait(2000);
			}

			//assertEquals(true, (body+"\n").equals(manualRequest.getResponse().getBody()));
			assertEquals(200, manualRequest.getResponse().getStatusCode());
			assertEquals(true, "OK".equalsIgnoreCase(manualRequest.getResponse().getStatusMessage()));

			assertTrue(body.equals(server.getLastReceived().getBody()));
			assertEquals(manualRequest.getHttpMethod(), server.getLastReceived().getHttpMethod());




		} 
		catch (InterruptedException e) {

			e.printStackTrace();
		}
	}



	@Test
	public void test1()  {

		assertEquals("RequirementsManagement", new JanewayModule().getName());
		new JanewayModule().getTabs();

	}

	@Test
	public void HintedTextArea()  {

		new HintedTextArea(1, 1, "hint");

	}

	
	
	@Test
	public void MainTabView()  {
		MainTabView MainTabView1 = new MainTabView();

		Tab tab1 = new Tab(MainTabView1, tabPane);


	}


	@Test
	public void mainTabController()  {
		MainTabView MainTabView1 = new MainTabView();
		mainTabController = new MainTabController(MainTabView1);

		mainTabController.addCreateRequirementTab();

		mainTabController.addCreateIterationTab();

		Iteration iteration1 = new Iteration();
		mainTabController.addIterationTab(iteration1, "title1");

		mainTabController.addCreateReleaseNumberTab();

		mainTabController.addIterationTab(null,"title2");

		mainTabController.addPermissionTab();

		mainTabController.addEditRequirementTab(new RequirementModel());

		mainTabController.addListRequirementsTab();

		mainTabController.addShowReportsTab();

		mainTabController.addViewIterationTab();

		mainTabController.switchToLeftTab();

		mainTabController.switchToRightTab();
	}


	@Test
	public void requirement(){

		RequirementModel RequirementModel2 = new RequirementModel(3, null, RequirementStatus.NEW, null, "name3"
				, "description3", 0, 0, new User("", "", "", -1), new ArrayList<User>(), 
				new Date(), new Date(), new ArrayList<RequirementEvent>(), 
				new ArrayList<String>(), null, RequirementType.EPIC);
	}

	
	
	
	@Test
	public void ListRequirementsPanel(){
		ListRequirementsPanel ListRequirementsPanel1 = new ListRequirementsPanel();
		ListRequirementsPanel1.setEditTable();
		ListRequirementsPanel1.setViewTable(false);
		ListRequirementsPanel1.setViewTable(true);
	

	}



	@After
	public void after(){
		server.stop();	
	}


}
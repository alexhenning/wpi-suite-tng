package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;


public class CreateIterationAction extends AbstractAction{


	private final MainTabController controller;
	
	/**
	 * Create a CreateDefectAction
	 * @param controller When the action is performed, controller.addCreateDefectTab() is called
	 */
	public CreateIterationAction(MainTabController controller) {
		super("Create Iteration");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_I);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		controller.addCreateIterationTab();
	}
}

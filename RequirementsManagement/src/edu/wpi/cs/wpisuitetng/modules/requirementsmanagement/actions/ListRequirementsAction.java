package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;

@SuppressWarnings("serial")
public class ListRequirementsAction extends AbstractAction {
	
	private final MainTabController controller;
	
	/**
	 * Create a CreateDefectAction
	 * @param controller When the action is performed, controller.addListRequirementsTab() is called
	 */
	public ListRequirementsAction(MainTabController controller) {
		super("List Requirements");
		this.controller = controller;
		putValue(MNEMONIC_KEY, KeyEvent.VK_L);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.addListRequirementsTab();
	}

}

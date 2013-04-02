package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Permissions;

/**
 * 
 * @author William Terry
 *
 */
@SuppressWarnings("serial")
public class PermissionsTab extends JPanel implements IToolbarGroupProvider {
	MainTabController tabController;
	Tab containingTab;
	Permissions permission;
	JScrollPane mainPanelScrollPane;
	PermissionsPanel mainPanel;
	

	public PermissionsTab(MainTabController tabController, Permissions permission, Tab tab) {
		this.tabController = tabController;
		this.permission = permission;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("Manage Permissions");
		
		// Instantiate the main create iteration panel
		mainPanel = new PermissionsPanel(this);
		this.setLayout(new BorderLayout());
		mainPanelScrollPane = new JScrollPane(mainPanel);
		mainPanelScrollPane.getVerticalScrollBar().setUnitIncrement(10);
		
		// Prevent content of scroll pane from smearing (credit: https://gist.github.com/303464)
		mainPanelScrollPane.getVerticalScrollBar().addAdjustmentListener(new java.awt.event.AdjustmentListener(){
			public void adjustmentValueChanged(java.awt.event.AdjustmentEvent ae){
				//SwingUtilities.invokeLater(new Runnable(){
				//	public void run(){
						mainPanelScrollPane.repaint();
				//	}
				//});
			}
		});
		
		this.add(mainPanelScrollPane, BorderLayout.CENTER);
		
	}


	@Override
	public ToolbarGroupView getGroup() {
		// TODO Auto-generated method stub
		return null;
	}
}

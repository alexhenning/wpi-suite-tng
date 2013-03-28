package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar.RequirementToolbarView;

@SuppressWarnings("serial")
public class IterationTab extends JPanel implements IToolbarGroupProvider{
	
	MainTabController tabController;
	Tab containingTab;
	Iteration iteration;
	JScrollPane mainPanelScrollPane;
	IterationPanel mainPanel;
	

	public IterationTab(MainTabController tabController, Iteration iteration, Tab tab) {
		this.tabController = tabController;
		this.iteration = iteration;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("Add Iteration");
		
		// Instantiate the main create iteration panel
		mainPanel = new IterationPanel(this);
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

	/**
	 * Constructs a new DefectView where the user can view (and edit) a defect.
	 * 
	 * @param defect	The defect to show.
	 * @param editMode	The editMode for editing the Defect
	 * @param tab		The Tab holding this DefectView (can be null)
	 */
	public IterationTab(MainTabController tabController, Iteration iteration, Mode editMode, Tab tab) {
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		
//		buttonGroup = new RequirementToolbarView(tabController, this);
		
		containingTab.setIcon(new ImageIcon());
		if(editMode == Mode.CREATE) {
			containingTab.setTitle("Create Iteration");
			containingTab.setToolTipText("Create a new Iteration");
		} else {
			setEditModeDescriptors(iteration);
		}
		
//		// If this is a new Iteration, set the creator
//		if (editMode == Mode.CREATE) {
//			// TODO: requirement.setCreator(new User("", ConfigManager.getConfig().getUserName(), "", -1));
//		}
		
		// Instantiate the main create defect panel
		mainPanel = new IterationPanel(this, iteration, editMode);
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


	/**
	 * @param iteration Set the tab title, tooltip, and group name according to this Iteration
	 */
	public void setEditModeDescriptors(Iteration iteration) {
		containingTab.setTitle("Iteration #" + iteration.getIterationNumber());
		// TODO: containingTab.setToolTipText("View defect " + requirement.getTitle());
//		buttonGroup.setName("Edit Iteration");
	}
	
	@Override
	public ToolbarGroupView getGroup() {
		// TODO Auto-generated method stub
		return null;
	}

}

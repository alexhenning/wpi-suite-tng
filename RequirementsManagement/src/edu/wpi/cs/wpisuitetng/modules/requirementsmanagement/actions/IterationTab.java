package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.IToolbarGroupProvider;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.CreateIterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.DummyTab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.Tab;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;

@SuppressWarnings("serial")
public class IterationTab extends JPanel implements IToolbarGroupProvider{
	
	Tab containingTab;
	Iteration iteration;
	JScrollPane mainPanelScrollPane;
	CreateIterationPanel mainPanel;
	

	public IterationTab(Iteration iteration, Tab tab) {
		this.iteration = iteration;
		containingTab = tab;
		if(containingTab == null) {
			containingTab = new DummyTab();
		}
		containingTab.setTitle("Search Requirements");
		
		// Instantiate the main create iteration panel
		mainPanel = new CreateIterationPanel(this);
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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.listeners;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewIterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollableTab;

public class UpdateViewIterationOnFocusListener implements ChangeListener {
	ScrollableTab<ViewIterationPanel> attentiveTab;
	
	public UpdateViewIterationOnFocusListener(ScrollableTab<ViewIterationPanel> attentiveTab){
		this.attentiveTab = attentiveTab;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(((JTabbedPane) e.getSource()).getSelectedComponent() == null){
			//TODO: find a way to remove this listener if tab has been closed
		} else if( ((JTabbedPane) e.getSource()).getSelectedComponent().equals(attentiveTab) ){
			attentiveTab.getPanel().updateAllIterationList();
		}
	}
}

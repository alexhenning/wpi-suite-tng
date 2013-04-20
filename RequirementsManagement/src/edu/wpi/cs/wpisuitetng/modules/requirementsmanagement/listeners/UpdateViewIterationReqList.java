/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.listeners;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewIterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.ViewSingleIterationPanel;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils.ScrollableTab;

/**
 * Updates the requirements list within the view single iteration panel
 * @author Haley
 */

public class UpdateViewIterationReqList implements ChangeListener {
	ScrollableTab<ViewSingleIterationPanel> attentiveTab;
	
	public UpdateViewIterationReqList(ScrollableTab<ViewSingleIterationPanel> attentiveTab){
		this.attentiveTab = attentiveTab;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if(((JTabbedPane) e.getSource()).getSelectedComponent() == null){
			//TODO: find a way to remove this listener if tab has been closed
		} else if( ((JTabbedPane) e.getSource()).getSelectedComponent().equals(attentiveTab) ){
			attentiveTab.getPanel().updateFields();
		}
	}
}

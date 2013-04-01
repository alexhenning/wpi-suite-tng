package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class FilterListener implements ActionListener{
	
	public FilterListener() {
	}
	
	// This action event is triggered by changing the enum box associated with the filtering system
	public void actionPerformed(ActionEvent e) { 
	    
		JComboBox cb = (JComboBox)e.getSource();
        int selection = cb.getSelectedIndex();
        switch (selection) {
        case 0: filterById();
        		break;
        case 1: filterByName();
				break;
        case 2: filterByStatus();
				break;
        case 3: filterByPriority();
				break;
        case 4: filterByEstimate();
				break;
        }

		
	}
	
	public void filterById() {
	}
	
	public void filterByName() {
	}
	
	public void filterByStatus() {
	}
	
	public void filterByPriority() {
	}
	
	public void filterByEstimate() {
	}

}

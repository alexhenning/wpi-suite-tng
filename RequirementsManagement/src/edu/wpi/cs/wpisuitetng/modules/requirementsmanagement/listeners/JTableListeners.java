package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;

public class JTableListeners implements MouseListener{

	//myJTable.addMouseListener(
	/*void TableMouseAdapter() {
		mouseClicked(MouseEvent e);
	}
	
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JTable target = (JTable)e.getSource();
			int row = target.getSelectedRow();
			int column = target.getSelectedColumn();
			// do some action
		}
	}*/

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JTable target = (JTable)e.getSource();
			int row = target.getSelectedRow();
			Object id = target.getValueAt(row, 0);
		}
		
	}
	
}



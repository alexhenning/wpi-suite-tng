/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    //TODO
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;

/**
 *
 * Listeners for the JTable, I don't know if this is even used
 * @author TODO
 *
 */
public class JTableListeners implements MouseListener{

	/**
	 * This is not implemented
	 *
	 * @param e a mouse event
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * This is not implemented
	 *
	 * @param e a mouse event
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This is not implemented
	 *
	 * @param e a mouse event
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This is not implemented
	 *
	 * @param e a mouse event
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * What to do if the mouse is clicked, not sure if this is used
	 *
	 * @param e a mouse event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			JTable target = (JTable)e.getSource();
			int row = target.getSelectedRow();
			Object id = target.getValueAt(row, 0);//TODO what is this used for? is it even needed?
		}
	}
}



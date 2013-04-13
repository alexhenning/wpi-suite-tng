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

/**
 * @author www.how2java.com
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;

/**
 *
 * TODO: DOCUMENT THIS
 * @author TODO
 *
 */
@SuppressWarnings("serial")
public class HintedTextArea extends JTextArea {

	Font gainFont = new Font("Tahoma", Font.PLAIN, 11);
	Font lostFont = new Font("Tahoma", Font.ITALIC, 11);
	
	
	public HintedTextArea(int rows, int columns, final String hint) {
		
		setRows(rows);
		setColumns(columns);
		setText(hint);
		setFont(lostFont);
		setForeground(Color.GRAY);

		this.addFocusListener(new FocusAdapter() {

			/**
			 * what to do if focus is gained
			 *
			 * @param e the event that caused focus to be gained
			 */
			@Override
			public void focusGained(FocusEvent e) {
				if (getText().equals(hint)) {
					setText("");
					setFont(gainFont);
					setForeground(Color.BLACK);
				} else {
					setText(getText());
					setFont(gainFont);
				}
			}

			/**
			 * what to do if focus is lost
			 *
			 * @param e the event that caused focus to be lost
			 */
			@Override
			public void focusLost(FocusEvent e) {
				if (getText().equals(hint) || getText().length() == 0) {
					setText(hint);
					setFont(lostFont);
					setForeground(Color.GRAY);
				} else {
					setText(getText());
					setFont(gainFont);
					setForeground(Color.BLACK);
				}
			}
		});

	}
}
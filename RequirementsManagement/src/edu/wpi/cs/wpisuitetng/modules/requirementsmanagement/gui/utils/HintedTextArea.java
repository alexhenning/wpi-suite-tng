/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    William Terry
 *    vpatara
 * References:
 *    www.how2java.com
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;

/**
 * JTextArea modified to display a hinted message when empty
 *
 * @author William Terry
 * @author vpatara
 */
@SuppressWarnings("serial")
public class HintedTextArea extends JTextArea {

	private String hint;
	private Font gainFont = new Font("Tahoma", Font.PLAIN, 11);
	private Font lostFont = new Font("Tahoma", Font.ITALIC, 11);

	/**
	 * Constructs a hinted text area
	 *
	 * @param rows the number of rows for this text area
	 * @param columns the number of columns for this text area
	 * @param hint the hinted message when the text area is empty and inactive
	 */
	public HintedTextArea(int rows, int columns, final String hint) {

		this.hint = hint;
		setRows(rows);
		setColumns(columns);
		setText(hint);
		setFont(lostFont);
		setForeground(Color.GRAY);

		this.addFocusListener(new FocusAdapter() {

			/**
			 * When focus is gained, activates the text area
			 *
			 * @param e the event that caused focus to be gained
			 */
			@Override
			public void focusGained(FocusEvent e) {
				setActive(true);
			}

			/**
			 * When focus is lost, deactivates the text area
			 *
			 * @param e the event that caused focus to be lost
			 */
			@Override
			public void focusLost(FocusEvent e) {
				setActive(false);
			}
		});
	}

	/**
	 * Determines whether the hinted text area is empty or contains the hint
	 *
	 * @return true if empty, false otherwise
	 */
	public boolean isEmpty() {
		return getText().length() == 0 || getText().equals(hint);
	}

	/**
	 * Sets the appearance of the object according to whether it is active or
	 * focused
	 *
	 * @param active true if the focus is on the object, false otherwise
	 */
	private void setActive(boolean active) {
		if(active) {
			// Active / focused
			setFont(gainFont);
			if (getText().equals(hint)) {
				setText("");
				setForeground(Color.BLACK);
			}
		} else {
			// Inactive / not focused
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
	}
}

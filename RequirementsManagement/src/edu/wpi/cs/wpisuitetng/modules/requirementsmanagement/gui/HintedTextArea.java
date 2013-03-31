/**
 * @author www.how2java.com
 */

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;

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
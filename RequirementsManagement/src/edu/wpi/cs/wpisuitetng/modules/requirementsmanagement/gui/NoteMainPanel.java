/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    vpatara
 *    Sergey
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.AddNoteController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementNote;

/**
 * Main panel for notes viewing and editing
 * 
 * @author vpatara
 * @author Sergey
 */
@SuppressWarnings("serial")
public class NoteMainPanel extends JPanel {
	RequirementsPanel parent;
	boolean inputEnabled;
	public JTextArea ta;
	JPanel noteViewer;
	JScrollPane noteScrollPane;
	JButton addButton;
	List<RequirementNote> notes;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param parent NoteTab that contains this object
	 */
	public NoteMainPanel(RequirementsPanel parent) {
		this.parent = parent;
		notes = new LinkedList<RequirementNote>();
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
		//setTestNotes();
//		new GetRequirementController(this).actionPerformed(null);
		
		// Populate the form with the contents of the Defect model and update the TextUpdateListeners.
		// TODO: updateFields();
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		ta = new JTextArea(5, 40);
		ta.setLineWrap(true);
		ta.setText("New note");		
		JScrollPane textPane = new JScrollPane(ta);
		textPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		textPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		noteViewer = new JPanel(new GridBagLayout());
		noteViewer.setMinimumSize(new Dimension(1000, 10));

		noteScrollPane = new JScrollPane(noteViewer);
		noteScrollPane.setPreferredSize(new Dimension(300, 300));
		noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		addButton = new JButton("Add note");
		addButton.addActionListener(new AddNoteController(this, parent.model, parent));
		
		// Add elements to the main panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		add(noteScrollPane, c);
		
		c.gridy = 1;
		add(textPane, c);

		c.fill = GridBagConstraints.NONE;
		c.gridy = 2;
		add(addButton, c);
	}
	
	public void setNotes(List<RequirementNote> notes) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 2, 5, 2);
		c.gridx = 0;
		c.gridy = notes.size();
		
		noteViewer.removeAll();
		
		for (RequirementNote note : notes) {
			if (note != null) {
				noteViewer.add(new NotePanel(note), c);
				c.gridy -= 1;
			}
		}

		this.revalidate();
		noteScrollPane.revalidate();
		noteViewer.revalidate();
		this.repaint();
		noteScrollPane.repaint();
		noteViewer.repaint();
	}
	
	public void setTestNotes() {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 2, 5, 2);
		c.gridx = 0;
		c.gridy = 0;
		noteViewer.add(new NotePanel("1afsdasdfasdf", "a1longgggggggggggg", new Date()), c);
		
		c.gridy = 1;
		noteViewer.add(new NotePanel("2", "a2", new Date()), c);
		
		c.gridy = 2;
		noteViewer.add(new NotePanel("3 sdfkjlsjld fkjl sdklfdskjl fskjl dfkjl sdkjl fkjl sdj fklskjl dfj sldkjl fskjl dfkjl sdkjl fkjl sdfkjl skjl dfkjl skjl df sjldfsdjl fskjl dfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdffkjl s", "a3", new Date()), c);
		
		c.gridy = 3;
		noteViewer.add(new NotePanel("4", "a4", new Date()), c);
		
		c.gridy = 4;
		noteViewer.add(new NotePanel("5poasidfpoi asdf[pioa sd[fp ioasdf[pio asdf[p ioasdf[po iasdf[ipo asdf[ipo asd[fpioa sd[pfoi as[dfipoa s[dfpio as[dfio", "a5", new Date()), c);
	}
	
	/**
	 * Sets whether input is enabled for this panel and its children. This should be used instead of 
	 * JComponent#setEnabled because setEnabled does not affect its children.
	 * 
	 * @param enabled	Whether or not input is enabled.
	 */
	protected void setInputEnabled(boolean enabled) {
		inputEnabled = enabled;

		// TODO: implement
	}
}

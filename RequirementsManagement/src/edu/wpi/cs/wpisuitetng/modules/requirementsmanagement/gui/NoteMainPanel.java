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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.util.List;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.GetRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.RequirementsPanel.Mode;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;

/**
 * Main panel for notes viewing and editing
 * 
 * @author vpatara
 * @author Sergey
 */
@SuppressWarnings("serial")
public class NoteMainPanel extends JPanel {
	NoteMainTab parent;
	boolean inputEnabled;
	JTextArea ta;
	JPanel noteViewer;
	JScrollPane noteScrollPane;
	JButton addButton;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param parent NoteTab that contains this object
	 */
	public NoteMainPanel(NoteMainTab parent) {
		this.parent = parent;
		
		// Indicate that input is enabled
		inputEnabled = true;

		// Add all components to this panel
		addComponents();
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
		ta.setText("Add a new message here.");
		
		noteViewer = new JPanel(new GridBagLayout());
		noteViewer.setMinimumSize(new Dimension(1000, 10));
//		noteViewer = new JPanel(new BorderLayout());

		GridBagConstraints innerGridCons = new GridBagConstraints();
		innerGridCons.fill = GridBagConstraints.HORIZONTAL;
		innerGridCons.insets = new Insets(5, 2, 5, 2);
		innerGridCons.gridx = 0;
		innerGridCons.gridy = 0;
//		noteViewer.add(new NotePanel("1", "a1", new Date()), BorderLayout.LINE_START);
		noteViewer.add(new NotePanel("1afsdasdfasdf", "a1longgggggggggggg", new Date()), innerGridCons);
		
		innerGridCons.gridy = 1;
//		noteViewer.add(new NotePanel("2", "a2", new Date()), BorderLayout.LINE_START);
		noteViewer.add(new NotePanel("2", "a2", new Date()), innerGridCons);
		
		innerGridCons.gridy = 2;
//		noteViewer.add(new NotePanel("3 sdfkjlsjld fkjl sdklfdskjl fskjl dfkjl sdkjl fkjl sdj fklskjl dfj sldkjl fskjl dfkjl sdkjl fkjl sdfkjl skjl dfkjl skjl df sjldfsdjl fskjl dfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdffkjl s", "a3", new Date()), BorderLayout.LINE_START);
		noteViewer.add(new NotePanel("3 sdfkjlsjld fkjl sdklfdskjl fskjl dfkjl sdkjl fkjl sdj fklskjl dfj sldkjl fskjl dfkjl sdkjl fkjl sdfkjl skjl dfkjl skjl df sjldfsdjl fskjl dfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdfdfk jlsdkjl f skjldsdfsdfsdfsdfsdfposipdiofiposdffkjl s", "a3", new Date()), innerGridCons);
		
		innerGridCons.gridy = 3;
//		noteViewer.add(new NotePanel("4", "a4", new Date()), BorderLayout.LINE_START);
		noteViewer.add(new NotePanel("4", "a4", new Date()), innerGridCons);
		
		innerGridCons.gridy = 4;
//		noteViewer.add(new NotePanel("5poasidfpoi asdf[pioa sd[fp ioasdf[pio asdf[p ioasdf[po iasdf[ipo asdf[ipo asd[fpioa sd[pfoi as[dfipoa s[dfpio as[dfio", "a5", new Date()), BorderLayout.LINE_START);
		noteViewer.add(new NotePanel("5poasidfpoi asdf[pioa sd[fp ioasdf[pio asdf[p ioasdf[po iasdf[ipo asdf[ipo asd[fpioa sd[pfoi as[dfipoa s[dfpio as[dfio", "a5", new Date()), innerGridCons);
		
		noteScrollPane = new JScrollPane(noteViewer);
		noteScrollPane.setPreferredSize(new Dimension(300, 300));
		noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		addButton = new JButton("Add note");
		
		// Add elements to the main panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		add(noteScrollPane, c);
		
		c.gridy = 1;
		add(ta, c);

		c.fill = GridBagConstraints.NONE;
		c.gridy = 2;
		add(addButton, c);
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

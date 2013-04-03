/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Josh
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;


/**
 * Main panel for notes viewing and editing
 * 
 * @author vpatara
 * @author Sergey
 */
@SuppressWarnings("serial")
public class RequirementHistoryTab extends JPanel {
	RequirementsPanel parent;
	JPanel noteViewer;
	JScrollPane noteScrollPane;
	List<ProjectEvent> notes;

	/**
	 * Constructs a panel for notes
	 * 
	 * @param parent NoteTab that contains this object
	 */
	public RequirementHistoryTab(RequirementsPanel parent) {
		this.parent = parent;
		notes = new LinkedList<ProjectEvent>();

		// Add all components to this panel
		addComponents();
		
	}

	/**
	 * Adds the components to the panel and places constraints on them
	 * for the SpringLayout manager.
	 * @param layout the layout manager
	 */
	protected void addComponents() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		noteViewer = new JPanel(new GridBagLayout());
		noteViewer.setMinimumSize(new Dimension(1000, 10));

		noteScrollPane = new JScrollPane(noteViewer);
		noteScrollPane.setPreferredSize(new Dimension(300, 300));
		noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// Add elements to the main panel
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 5, 5, 5);
		c.gridx = 0;
		add(noteScrollPane, c);
	}
	
	public void setNotes(List<ProjectEvent> events) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 0, 5, 0);
		c.gridx = 0;
		c.gridy = events.size();
		
		noteViewer.removeAll();
		
		for (ProjectEvent event : events) {
			if (event != null) {
				int id = Integer.parseInt(event.getObjectId());
				if (id == this.parent.model.getId()) {
					noteViewer.add(new RequirementHistoryPanel(event), c);
					c.gridy -= 1;
				}
			}
			
		}
		
		this.revalidate();
		this.repaint();
	}

}

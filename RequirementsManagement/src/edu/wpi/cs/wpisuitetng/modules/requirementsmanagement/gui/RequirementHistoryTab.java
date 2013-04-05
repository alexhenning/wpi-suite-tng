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
 *    Deniz
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
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
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEventObjectType;


/**
 * Main panel for history viewing
 * @author Josh
 * @author Deniz
 */
@SuppressWarnings("serial")
public class RequirementHistoryTab extends JPanel {
	/** the panel this is shown in */
	RequirementsPanel parent;
	/** panel to display this */
	JPanel noteViewer;
	/** scroll pane */
	JScrollPane noteScrollPane;
	/** list of project events*/
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
		setLayout(new BorderLayout());
		
		noteViewer = new JPanel(new GridBagLayout());

		noteScrollPane = new JScrollPane(noteViewer);
		noteScrollPane.setPreferredSize(new Dimension(300, 300));
		noteScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		noteScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// Add elements to the main panel
		add(noteScrollPane, BorderLayout.CENTER);
	}
	
	/**
	 *add the events to the history tab
	 *
	 * @param events the list of events
	 */
	public void setNotes(List<ProjectEvent> events) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 0, 3, 0);
		c.gridx = 0;
		c.gridy = events.size();
		c.weightx = 0.5;
		c.weighty = 0.5;
		
		noteViewer.removeAll();
		
		for (ProjectEvent event : events) {
			if (event != null && event.getObjectType() == ProjectEventObjectType.REQUIREMENT) {
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

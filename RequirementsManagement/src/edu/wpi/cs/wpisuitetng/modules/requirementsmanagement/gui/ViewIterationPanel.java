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
 *    Josh
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * GUI for a project manager to manage user permissions 
 *
 * @author William Terry
 *
 */
@SuppressWarnings("serial")
public class ViewIterationPanel extends JPanel {
	
	/** the tab that made this */
	ViewIterationTab parent;
	JPanel topPanel;
	JLabel lbl1;

	/** A flag indicating if input is enabled on the form */
	protected boolean inputEnabled;

	/**
	 * constructor
	 * @param permissionsTab the tab that created this
	 */
	public ViewIterationPanel(ViewIterationTab iterTab){
		this.parent = iterTab;
		
		// Indicate that input is enabled
		inputEnabled = true;
		
		// Add all components to this panel
		addComponents();
		
		// Populate the form with the contents of the Iteration model and update the TextUpdateListeners.
		//updateFields();
	}

	/**
	 *add the components to the view
	 *
	 */
	private void addComponents() {
		setLayout(new BorderLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		lbl1 = new JLabel("Add ");
		
		topPanel = new JPanel();
		topPanel.setLayout(new GridBagLayout());
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(3, 3, 3, 3);
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		topPanel.add(lbl1, c);

		add(topPanel, BorderLayout.PAGE_START);

	}
	
	/**
	 * close the tab
	 *
	 */
	public void close() {
		parent.tabController.closeCurrentTab();
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

	/**
	 * Returns a boolean representing whether or not input is enabled for the ViewIterationPanel and its children.
	 * 
	 * @return	A boolean representing whether or not input is enabled for the ViewIterationPanel and its children.
	 */
	public boolean getInputEnabled() {
		return inputEnabled;
	}

}

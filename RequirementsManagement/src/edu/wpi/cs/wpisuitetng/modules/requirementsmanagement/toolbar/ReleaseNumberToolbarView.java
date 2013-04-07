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
package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.toolbar;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.actions.CreateReleaseNumberAction;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabController;

/**
 *
 * The toolbar for working with ReleaseNumbers
 * Always has a group of global commands (Create or Edit Release Numbers)
 * @author Tim
 *
 */
@SuppressWarnings("serial")
public class ReleaseNumberToolbarView extends ToolbarGroupView {
	
	/** Create or Edit ReleaseNumber button */
	private JButton CrtReleaseNumber;
	
	public ReleaseNumberToolbarView(final MainTabController tabController) {
		super("Release Number");
		// Construct the content panel
		JPanel content = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(layout);
		content.setOpaque(false);
		
		CrtReleaseNumber = new JButton("Create/Edit Release Numbers");
		CrtReleaseNumber.setAction(new CreateReleaseNumberAction(tabController));
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		content.add(CrtReleaseNumber, c);
		
		add(content);
		
		Double toolbarGroupWidth = CrtReleaseNumber.getPreferredSize().getWidth() + 40;
		setPreferredWidth(toolbarGroupWidth.intValue());
	}
}

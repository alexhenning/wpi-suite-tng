/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andrew Hurle
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import java.awt.Component;

import javax.swing.Icon;

/**
 * Holds values given to it, but doesn't actually change the given MainTabView.
 */
public class DummyTab extends Tab {

	/** title of the tab */
	private String title;
	/** the tabs icon */
	private Icon icon;
	/** tooltip text for the tab */
	private String toolTipText;
	/** the component */
	private Component component;

	/**
	 * Construct a DummyTab - arguments ignored
	 * 
	 * @param view ignored
	 * @param tabComponent ignored
	 */
	public DummyTab(MainTabView view, Component tabComponent) {
		super(null, null);
	}
	
	/**
	 * Same as DummyTab(null, null)
	 */
	public DummyTab() {
		this(null, null);
	}
	
	/**
	 * returns the tabs title
	 *
	 * @return the tab's title
	 */
	@Override
	public String getTitle() {
		return title;
	}
	
	/**
	 * sets the tab's title to title
	 *
	 * @param title the title that should be set to the tab
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * get the tab's icon
	 *
	 * @return the tab's icon
	 */
	@Override
	public Icon getIcon() {
		return icon;
	}
	
	/**
	 * set the tab's icon to icon
	 *
	 * @param icon the icon that should be set to the tab
	 */
	@Override
	public void setIcon(Icon icon) {
		this.icon = icon;
	}
	
	/**
	 * get the tab's tooltip text
	 *
	 * @return the tab's tooltip text
	 */
	@Override
	public String getToolTipText() {
		return toolTipText;
	}
	
	/**
	 * set the tab's tooltiptext to toolTipText
	 *
	 * @param toolTipText the tooltiptext that should be set to the tab
	 */
	@Override
	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}
	
	/**
	 * get the tab's component
	 *
	 * @return the tab's component
	 */
	public Component getComponent() {
		return component;
	}
	
	/**
	 * set the tab's component to component
	 *
	 * @param component the component that should be set to the tab
	 */
	public void setComponent(Component component) {
		this.component = component;
	}

}

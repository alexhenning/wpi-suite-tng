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

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.utils;

import java.awt.Component;

import javax.swing.Icon;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui.MainTabView;

/**
 * A wrapper class for MainTabView that can be given to components within that view
 * in order to allow them to easily change their titles and icons.
 */
public class Tab {

	/** the main tab view */
	private final MainTabView view;
	/** the tab's component */
	private final Component tabComponent;

	/**
	 * Create a Tab identified by the given MainTabView and tabComponent.
	 * 
	 * @param view The MainTabView this Tab belongs to
	 * @param tabComponent The tabComponent for this Tab
	 */
	public Tab(MainTabView view, Component tabComponent) {
		this.view = view;
		this.tabComponent = tabComponent;
	}
	
	/**
	 * get the tab's index
	 *
	 * @return the tab's index
	 */
	private int getIndex() {
		return view.indexOfTabComponent(tabComponent);
	}
	
	/**
	 * get the tab's title
	 *
	 * @return the tab's title
	 */
	public String getTitle() {
		return view.getTitleAt(getIndex());
	}
	
	/**
	 * @param title Set the title of the Tab to this String
	 */
	public void setTitle(String title) {
		view.setTitleAt(getIndex(), title);
		tabComponent.invalidate(); // needed to make tab shrink with smaller title
	}
	
	/**
	 * get the tab's icon
	 *
	 * @return the tab's icon
	 */
	public Icon getIcon() {
		return view.getIconAt(getIndex());
	}
	
	/**
	 * @param icon Set the icon of the Tab to this Icon
	 */
	public void setIcon(Icon icon) {
		view.setIconAt(getIndex(), icon);
	}
	
	/**
	 * get the tab's tooltiptext
	 * 
	 * @return the tab's tooltiptext
	 */
	public String getToolTipText() {
		return view.getToolTipTextAt(getIndex());
	}
	
	/**
	 * @param toolTipText Set the tooltip of the Tab to this String
	 */
	public void setToolTipText(String toolTipText) {
		view.setToolTipTextAt(getIndex(), toolTipText);
	}
	
	/**
	 * get the tab's component
	 *
	 * @return the tab's component
	 */
	public Component getComponent() {
		return view.getComponentAt(getIndex());
	}
	
	/**
	 * @param component Set the component contained by this Tab to this Component
	 */
	public void setComponent(Component component) {
		view.setComponentAt(getIndex(), component);
	}
	
}

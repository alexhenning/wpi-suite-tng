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
 *    Tim C
 *    James
 *    Jacob Palnick
 *    vpatara
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.gui;

import javax.swing.table.AbstractTableModel;

/**
 * Model for the possible sub-requirements table, which also tells whether a
 * requirement can be attached as a child of this requirement, and vice versa
 *
 * @author vpatara
 * @author Josh
 * @author Tim C
 * @author James
 * @author Jacob Palnick
 */

@SuppressWarnings("serial")
public class ViewPossibleSubReqTable extends AbstractTableModel {

	public static final int ID = 0;
	public static final int NAME = 1;
	public static final int DESCRIPTION = 2;
	public static final int ITERATION = 3;
	public static final int STATUS = 4;
	public static final int PRIORITY = 5;
	public static final int ESTIMATE = 6;
	public static final int THIS_AS_CHILD = 7;
	public static final int THIS_AS_PARENT = 8;

	/** name of the columns */
	private String[] columnNames = { "ID", "Name", "Description", "Iteration",
			"Status", "Priority", "Estimate", "C", "P" };
	//first list is row, second list is column data

	/** data in each cell*/
	private Object[][] data = {};

	/**
	 * get the number of columns
	 *
	 * @return the number of columns
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * get the number of rows
	 *
	 * @return the number of rows
	 */
	public int getRowCount() {
		return data.length;
	}

	/**
	 * get the name of a specified column
	 *
	 * @param col the column number
	 * @return the name of the column
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * get the object in a cell
	 *
	 * @param row the row of the cell
	 * @param col the column of the cell
	 * @return the object at the cell
	 */
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	/**
	 * get the type of class in a column
	 *
	 * @param c the column number
	 * @return the kind of class in the column
	 */
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/**
	 * see if a cell is editable, true if it is, false if it is not
	 *
	 * @param row the row of the cell
	 * @param col the column of the cell
	 * @return true if the cell is editable, false otherwise
	 */
	public boolean isCellEditable(int row, int col) {
		return false;
	}

	/**
	 * set the value at a cell
	 *
	 * @param value the value to set the cell
	 * @param row the row of the cell
	 * @param col the column of the cell
	 */
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

	/**
	 * set data
	 *
	 * @param data the data to set
	 */
	public void setData(Object[][] data){
		this.data = data;
	}

	/**
	 * Returns the data representation
	 *
	 * @return data
	 */
	public Object[][] getData() {
		return data;
	}
}
